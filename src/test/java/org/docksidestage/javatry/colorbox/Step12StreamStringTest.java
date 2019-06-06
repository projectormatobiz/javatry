/*
 * Copyright 2019-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.docksidestage.javatry.colorbox;

import static org.docksidestage.bizfw.colorbox.yours.YourPrivateRoom.*;

import java.awt.*;
import java.io.File;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.docksidestage.bizfw.colorbox.ColorBox;
import org.docksidestage.bizfw.colorbox.space.BoxSpace;
import org.docksidestage.bizfw.colorbox.yours.YourPrivateRoom;
import org.docksidestage.unit.PlainTestCase;

/**
 * The test of String with color-box, using Stream API you can. <br>
 * Show answer by log() for question of javadoc.
 * @author jflute
 * @author projectormatobiz
 */
public class Step12StreamStringTest extends PlainTestCase {

    // ===================================================================================
    //                                                                            length()
    //                                                                            ========
    /**
     * What is color name length of first color-box? <br>
     * (最初のカラーボックスの色の名前の文字数は？)
     */
    public void test_length_basic() {
        // Listを同じように取得する。
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        // streamを作る
        String answer = colorBoxList.stream()
                // 最初の一つを返す
                .findFirst()
                // 全体をBoxColorにする
                .map(colorBox -> colorBox.getColor()) // consciously split as example
                // さらにそこからStringにする
                .map(boxColor -> boxColor.getColorName())
                // 長さを返す
                .map(colorName -> {
                    log(colorName); // for visual check
                    return String.valueOf(colorName.length());
                }).orElse("not found"); // basically no way because of not-empty list and not-null returns
        log(answer);
    }

    /**
     * Which string has max length in color-boxes? <br>
     * (カラーボックスに入ってる文字列の中で、一番長い文字列は？)
     */
    public void test_length_findMax() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        String answer = colorBoxList.stream()
                .flatMap(spaceList -> spaceList.getSpaceList().stream())
                .filter(strContent -> strContent.getContent() instanceof String)
                .map(content -> String.valueOf(content.getContent()))
                // これってどっちがいいの？
                //                .max((v1, v2) -> Integer.compare(v1.length(), v2.length()))
                .reduce((ans, target) -> {
                    return ans.length() < target.length() ? target : ans;
                })
                .orElse("not found");

        log(answer);
    }

    /**
     * How many characters are difference between max and min length of string in color-boxes? <br>
     * (カラーボックスに入ってる文字列の中で、一番長いものと短いものの差は何文字？)
     */
    public void test_length_findMaxMinDiff() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        // TODO:もっと良い処理を知りたい
        // Streamは使い回し出来ないみたいなので、中間処置してから、一度普通のオブジェクトに落とし込んで、もっかいStreamし始めるとか
        List<String> strAll = colorBoxList.stream()
                .flatMap(spaceList -> spaceList.getSpaceList().stream())
                .filter(strContent -> strContent.getContent() instanceof String)
                .map(content -> String.valueOf(content.getContent()))
                .collect(Collectors.toList());
        String longest = strAll.stream().reduce((ans, target) -> {
            return ans.length() > target.length() ? ans : target;
        }).orElse("not found");
        String shortest = strAll.stream().reduce((ans, target) -> {
            return ans.length() < target.length() ? ans : target;
        }).orElse("not found");
        log(longest.length() - shortest.length());
    }

    /**
     * Which value (toString() if non-string) has second-max legnth in color-boxes? (without sort)<br>
     * (カラーボックスに入ってる値 (文字列以外はtoString()) の中で、二番目に長い文字列は？ (ソートなしで))
     */
    public void test_length_findSecondMax() {
        // そうしろって言われたからそうしたけどなんで
        final String[] max = { "" };
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        String secondLengthString = colorBoxList.stream()
                .flatMap(spaceList -> spaceList.getSpaceList().stream())
                .filter(notNullContent -> notNullContent.getContent() != null)
                .map(content -> content.getContent().toString())
                .reduce((ans, target) -> {
                    // 本当にこれでいいんだろうか
                    log(target);
                    if (max[0].length() < target.length()) {
                        ans = max[0];
                        max[0] = target;
                    } else if (ans.length() < target.length()) {
                        ans = target;
                    }
                    return ans;
                })
                .orElse("not found second length value.");
        log(secondLengthString);
    }

    /**
     * How many total lengths of strings in color-boxes? <br>
     * (カラーボックスに入ってる文字列の長さの合計は？)
     */
    public void test_length_calculateLengthSum() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        int ans = colorBoxList.stream()
                .flatMap(spaceList -> spaceList.getSpaceList().stream())
                .filter(strContent -> strContent.getContent() instanceof String)
                .mapToInt(content -> content.getContent().toString().length())
                .sum();
        //                .reduce((sum, value) -> {
        //                    return sum += value;
        //                })
        //                .orElse(-1);
        //        log(ans != -1 ? ans : "not found color box.");
        log(ans);
    }

    /**
     * Which color name has max length in color-boxes? <br>
     * (カラーボックスの中で、色の名前が一番長いものは？)
     */
    public void test_length_findMaxColorSize() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        String ans = colorBoxList.stream()
                .map(colorList -> colorList.getColor().getColorName())
                .max(Comparator.comparing(String::valueOf))
                .orElse("not found color box.");
        log(ans);
    }

    // ===================================================================================
    //                                                            startsWith(), endsWith()
    //                                                            ========================
    /**
     * What is color in the color-box that has string starting with "Water"? <br>
     * ("Water" で始まる文字列をしまっているカラーボックスの色は？)
     */
    public void test_startsWith_findFirstWord() {
        //mapすると情報が失われる気がするのでfilterで取得する
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        ColorBox ans = colorBoxList.stream()
                .filter(target -> target.getSpaceList()
                        .stream()
                        .filter(content -> content.getContent() instanceof String)
                        .map(content -> String.valueOf(content.getContent()))
                        // ここのfindしてるところ大丈夫かな、一応2件Waterにしたら2件出てきた
                        .findAny()
                        .orElse("")
                        .startsWith("Water"))
                //                .forEach(System.out::println);
                .findFirst()
                .orElse(null);
        log(ans != null ? ans.getColor().getColorName() : "not found box.");

    }

    /**
     * What is color in the color-box that has string ending with "front"? <br>
     * ("front" で終わる文字列をしまっているカラーボックスの色は？)
     */
    public void test_endsWith_findLastWord() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        ColorBox ans = colorBoxList.stream()
                .filter(target -> target.getSpaceList()
                        .stream()
                        .filter(content -> content.getContent() instanceof String)
                        .map(a -> String.valueOf(a.getContent()))
                        .findAny()
                        .orElse("")
                        .endsWith("front"))
                .findFirst()
                .orElse(null);
        log(ans != null ? ans.getColor().getColorName() : "not found box.");
    }

    // ===================================================================================
    //                                                            indexOf(), lastIndexOf()
    //                                                            ========================
    /**
     * What number character is starting with "front" of string ending with "front" in color-boxes? <br>
     * (あなたのカラーボックスに入ってる "front" で終わる文字列で、"front" は何文字目から始まる？)
     */
    public void test_indexOf_findIndex() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        String ans = colorBoxList.stream()
                .flatMap(spaceList -> spaceList.getSpaceList().stream())
                .filter(strContent -> strContent.getContent() instanceof String)
                .map(content -> String.valueOf(content.getContent()))
                .filter(target -> target.endsWith("front"))
                .findFirst()
                .orElse("");
        log(!ans.equals("") ? ans.indexOf("front") + 1 : "not found color box.");
    }

    /**
     * What number character is starting with the late "ど" of string containing plural "ど"s in color-boxes? (e.g. "どんどん" => 3) <br>
     * (あなたのカラーボックスに入ってる「ど」を二つ以上含む文字列で、最後の「ど」は何文字目から始まる？ (e.g. "どんどん" => 3))
     */
    public void test_lastIndexOf_findIndex() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        String ans = colorBoxList.stream()
                .flatMap(spaceList -> spaceList.getSpaceList().stream())
                .filter(strContent -> strContent.getContent() instanceof String)
                .map(content -> String.valueOf(content.getContent()))
                .filter(target -> target.indexOf("ど", target.indexOf("ど")) != 0)
                .findFirst()
                .orElse("");
        log(!ans.equals("") ? ans.lastIndexOf("ど") + 1 : "not found color box.");
    }

    // ===================================================================================
    //                                                                         substring()
    //                                                                         ===========
    /**
     * What character is first of string ending with "front" in color-boxes? <br>
     * (カラーボックスに入ってる "front" で終わる文字列の最初の一文字は？)
     */
    public void test_substring_findFirstChar() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        String ans = colorBoxList.stream()
                .flatMap(spaceList -> spaceList.getSpaceList().stream())
                .filter(strContent -> strContent.getContent() instanceof String)
                .map(content -> String.valueOf(content.getContent()))
                .filter(target -> target.endsWith("front"))
                .findFirst()
                .orElse("");
        // これで良いのかな？stream内でやらなくていいのかな
        log(!ans.equals("") ? ans.substring(0, 1) : "not found color box.");
    }

    /**
     * What character is last of string starting with "Water" in color-boxes? <br>
     * (カラーボックスに入ってる "Water" で始まる文字列の最後の一文字は？)
     */
    public void test_substring_findLastChar() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        String ans = colorBoxList.stream()
                .flatMap(spaceList -> spaceList.getSpaceList().stream())
                .filter(strContent -> strContent.getContent() instanceof String)
                .map(content -> String.valueOf(content.getContent()))
                .filter(target -> target.startsWith("Water"))
                .findFirst()
                .orElse("");
        // これで良いのかな？stream内でやらなくていいのかな
        log(!ans.equals("") ? ans.substring(ans.length() - 1) : "not found color box.");
    }

    // ===================================================================================
    //                                                                           replace()
    //                                                                           =========
    /**
     * How many characters does string that contains "o" in color-boxes and removing "o" have? <br>
     * (カラーボックスに入ってる "o" (おー) を含んだ文字列から "o" を全て除去したら何文字？)
     */
    public void test_replace_remove_o() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        String ans = colorBoxList.stream()
                .flatMap(spaceList -> spaceList.getSpaceList().stream())
                .filter(strContent -> strContent.getContent() instanceof String)
                .map(content -> String.valueOf(content.getContent()))
                .filter(target -> target.indexOf("o") != 0)
                .reduce((sum, target) -> {
                    sum += target.replaceAll("o", "");
                    return sum;
                })
                .orElse("");
        log(!ans.equals("") ? ans.length() : "not found color box.");
    }

    /**
     * What string is path string of java.io.File in color-boxes, which is replaced with "/" to Windows file separator? <br>
     * カラーボックスに入ってる java.io.File のパス文字列のファイルセパレーターの "/" を、Windowsのファイルセパレーターに置き換えた文字列は？
     */
    public void test_replace_fileseparator() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        List<String> ans = colorBoxList.stream()
                .flatMap(spaceList -> spaceList.getSpaceList().stream())
                .filter(strContent -> strContent.getContent() instanceof File)
                .map(content -> (File) content.getContent())
                .map(replace -> replace.getPath().replaceAll("/", "\\\\"))
                .collect(Collectors.toList());
        // やっつけかんすごい
        ans.forEach(res -> System.out.println("C:" + res));
        //        log(!ans.equals("") ? ans.length() : "not found color box.");
    }

    // ===================================================================================
    //                                                                    Welcome to Devil
    //                                                                    ================
    /**
     * What is total length of text of DevilBox class in color-boxes? <br>
     * (カラーボックスの中に入っているDevilBoxクラスのtextの長さの合計は？)
     */
    public void test_welcomeToDevil() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        int ans = colorBoxList.stream()
                .flatMap(spaceList -> spaceList.getSpaceList().stream())
                .map(content -> content.getContent())
                .filter(devil -> devil instanceof DevilBox)
                .map(enableDevil -> {
                    DevilBox devil = (DevilBox) enableDevil;
                    devil.wakeUp();
                    devil.allowMe();
                    devil.open();
                    try {
                        return devil.getText().length();
                    } catch (DevilBoxTextNotFoundException e) {
                        // やっつけ感がすごい
                        return 0;
                    }
                })
                .reduce((sum, target) -> {
                    return sum += target;
                })
                .orElse(-1);
        log(ans != -1 ? ans : "not found color box.");
    }

    // ===================================================================================
    //                                                                           Challenge
    //                                                                           =========
    /**
     * What string is converted to style "map:{ key = value ; key = value ; ... }" from java.util.Map in color-boxes? <br>
     * (カラーボックスの中に入っている java.util.Map を "map:{ key = value ; key = value ; ... }" という形式で表示すると？)
     */
    public void test_showMap_flat() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        List<Map> ans = colorBoxList.stream()
                .flatMap(spaceList -> spaceList.getSpaceList().stream())
                .map(content -> content.getContent())
                .filter(target -> target instanceof Map)
                .map(mapContent -> (Map) mapContent)
                .collect(Collectors.toList());
        for (Map an : ans) {
            printMap(an);
        }
    }

    /**
     * What string is converted to style "map:{ key = value ; key = map:{ key = value ; ... } ; ... }" from java.util.Map in color-boxes? <br>
     * (カラーボックスの中に入っている java.util.Map を "map:{ key = value ; key = map:{ key = value ; ... } ; ... }" という形式で表示すると？)
     */
    public void test_showMap_nested() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        List<Map> ans = colorBoxList.stream()
                .flatMap(spaceList -> spaceList.getSpaceList().stream())
                .map(content -> content.getContent())
                .filter(target -> target instanceof Map)
                .map(mapContent -> (Map) mapContent)
                .collect(Collectors.toList());
        for (Map an : ans) {
            printMapDeep(an);
            System.out.println("");
        }
    }

    private void printMap(Map an) {
        System.out.print("map: {");
        for (Object key : an.keySet()) {
            System.out.print(key + " = " + an.get(key) + " ; ");
        }
        System.out.println("}");
    }

    private void printMapDeep(Map an) {
        boolean flag = false;
        System.out.print("map: {");
        for (Object key : an.keySet()) {
            if (an.get(key) instanceof Map) {
                // valueがMapなら深く
                System.out.print(key + " = ");
                printMapDeep((Map) an.get(key));
                flag = true;
            } else {
                // そうじゃないなら出力
                System.out.print(key + " = " + an.get(key) + " ; ");
            }
        }
        System.out.print("}");
    }

    // ===================================================================================
    //                                                                           Good Luck
    //                                                                           =========
    // too difficult to be stream?
    ///**
    // * What string of toString() is converted from text of SecretBox class in upper space on the "white" color-box to java.util.Map? <br>
    // * (whiteのカラーボックスのupperスペースに入っているSecretBoxクラスのtextをMapに変換してtoString()すると？)
    // */
    //public void test_parseMap_flat() {
    //}
    //
    ///**
    // * What string of toString() is converted from text of SecretBox class in both middle and lower spaces on the "white" color-box to java.util.Map? <br>
    // * (whiteのカラーボックスのmiddleおよびlowerスペースに入っているSecretBoxクラスのtextをMapに変換してtoString()すると？)
    // */
    //public void test_parseMap_nested() {
    //}
}
