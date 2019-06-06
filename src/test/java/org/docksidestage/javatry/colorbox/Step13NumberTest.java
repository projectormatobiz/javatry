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

import static java.math.BigDecimal.*;
import static java.util.Map.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.docksidestage.bizfw.colorbox.ColorBox;
import org.docksidestage.bizfw.colorbox.size.BoxSize;
import org.docksidestage.bizfw.colorbox.space.BoxSpace;
import org.docksidestage.bizfw.colorbox.yours.YourPrivateRoom;
import org.docksidestage.unit.PlainTestCase;

/**
 * The test of Number with color-box. <br>
 * Show answer by log() for question of javadoc.
 * @author jflute
 * @author your_name_here
 */
public class Step13NumberTest extends PlainTestCase {

    // ===================================================================================
    //                                                                               Basic
    //                                                                               =====
    /**
     * How many integer-type values in color-boxes are between 0 and 54? <br>
     * (カラーボックの中に入っているInteger型で、0から54までの値は何個ある？)
     */
    public void test_countZeroToFiftyFour_IntegerOnly() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        long ans = colorBoxList.stream()
                .flatMap(spaceList -> spaceList.getSpaceList().stream())
                .map(content -> content.getContent())
                .filter(intContent -> intContent instanceof Integer)
                //                .forEach(System.out::println)
                .filter(validContent -> (int) validContent >= 0 && (int) validContent <= 54)
                .count();
        //                .mapToInt(castedIntContent -> Integer.parseInt((String) castedIntContent))
        //                .sum();
        log(ans);
    }

    /**
     * How many number values in color-boxes are between 0 and 54? <br>
     * (カラーボックの中に入っている数値で、0から54までの値は何個ある？)
     */
    public void test_countZeroToFiftyFour_Number() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        long ans = colorBoxList.stream()
                .flatMap(spaceList -> spaceList.getSpaceList().stream())
                .map(content -> content.getContent())
                .filter(numberContent -> numberContent instanceof Number)
                .filter(validContent -> ((Number) validContent).doubleValue() >= 0 && ((Number) validContent).doubleValue() <= 54)
                .count();
        log(ans);
    }

    /**
     * What color name is used by color-box that has integer-type content and the biggest width in them? <br>
     * (カラーボックスの中で、Integer型の Content を持っていてBoxSizeの幅が一番大きいカラーボックスの色は？)
     */
    public void test_findColorBigWidthHasInteger() {
        // filter でゴニョってからmapしてサイズのmax
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        ColorBox ans = colorBoxList.stream()
                // Integer 型のContentを持っているかのfilter
                .filter(target -> target.getSpaceList()
                        .stream()
                        // とてもだいじ
                        .filter(notEmptyContent -> notEmptyContent.getContent() != null)
                        .map(content -> content.getContent())
                        .findAny()
                        .orElse(null) instanceof Integer)
                // BoxSize が一番大きいやつのfilter
                .reduce((maxWidth, target) -> {
                    return maxWidth.getSize().getWidth() > target.getSize().getWidth() ? maxWidth : target;
                }).orElse(null);
        log(ans != null ? ans.getColor().getColorName() : "not found color box.");
    }

    /**
     * What is total of BigDecimal values in List in color-boxes? <br>
     * (カラーボックスの中に入ってる List の中の BigDecimal を全て足し合わせると？)
     */
    public void test_sumBigDecimalInList() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        //        List<List> ans =
        Optional ans = colorBoxList.stream()
                .flatMap(spaceList -> spaceList.getSpaceList().stream())
                .filter(listContent -> listContent.getContent() instanceof List)
                // Stream<List>になってる
                .map(listContent -> (List) listContent.getContent())
                .flatMap(a -> a.stream())
                .filter(b -> b instanceof BigDecimal)
                // .forEach(System.out::println);
                .map(c -> (BigDecimal) c)
                .reduce((answer, target) -> {
                    log(target);
                    BigDecimal sum = (BigDecimal) answer;
                    return sum.add((BigDecimal) target);
                });
        ans.ifPresentOrElse(bigDecimalSum -> log(bigDecimalSum), () -> log("not found BigDecimal."));
    }

    // ===================================================================================
    //                                                                           Challenge
    //                                                                           =========
    /**
     * What key is related to value that is max number in Map that has only number in color-boxes? <br>
     * (カラーボックスに入ってる、valueが数値のみの Map の中で一番大きいvalueのkeyは？)
     */
    public void test_findMaxMapNumberValue() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
//        long ans =
                colorBoxList.stream()
                .flatMap(spaceList -> spaceList.getSpaceList().stream())
                .map(content -> content.getContent())
                .filter(numberContent -> numberContent instanceof Map)
                .map(mapContent -> (Map) mapContent)
                .filter(a -> a.keySet().stream()
                .allMatch(b -> b instanceof Number)
                )
                .forEach(System.out::println);
//                .filter(validContent -> ((Number) validContent).doubleValue() >= 0 && ((Number) validContent).doubleValue() <= 54)
//                .count();
//        log(ans);

    }

    /**
     * What is total of number or number-character values in Map in purple color-box? <br> 
     * (purpleのカラーボックスに入ってる Map の中のvalueの数値・数字の合計は？)
     */
    public void test_sumMapNumberValue() {
    }
}
