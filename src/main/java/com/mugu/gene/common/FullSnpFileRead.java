package com.mugu.gene.common;

import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author : zp
 * @Description :
 * @Date : 2019/11/5
 */
public class FullSnpFileRead {

    public static Map<String, List<String>> readFullSnpFileToMap(String file) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        Map<String, List<String>> map = new HashMap<>();
        String strLine = null;
        while (null != (strLine = bufferedReader.readLine())) {
            String[] split = strLine.split("\t");
            String key = split[0].replace("_b37", "");
            String val = strLine.replace(split[0], "");
            List<String> list = map.get(split[0]);
            if (StringUtils.isEmpty(list)) {
                List<String> strings = new ArrayList<>();
                strings.add(val);
                map.put(key, strings);
            } else {
                list.add(val);
                map.put(key, list);
            }
        }
        bufferedReader.close();
        return map;
    }


    public static void main(String[] args) {
        File f = new File("D:\\network\\");
        for (File f1 : f.listFiles()) {
            if (f1.isDirectory()) {
                if (f1.listFiles().length == 0) {
                    f1.delete();
                }
            }
        }
    }
}
