package com.ou.generator.util;

import java.io.File;

/**
 * @author vince
 * @date 2020/1/3 10:54
 */
public class TestUtil {

    public static void main(String[] args) {
        String path = "/template/dsa.dsaaa.ccc.ddd";
        String s = path.replaceAll("\\.", File.separator);
        System.out.println(s);
    }
}
