package com.mugu.gene.util;

import com.mugu.gene.model.CountGene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * @Author : zp
 * @Description :
 * @Date : 2019/10/18
 */
public class FileReadUtil {

    private static String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};


    public static CountGene fileMap(Map<String, String> map, String filePath) throws IOException {
        CountGene countGene = new CountGene();
        int five_prime_UTR = 0;
        int three_prime_UTR = 0;
        int promoter = 0;
        int exon = 0;
        int intron = 0;
        int repeat = 0;
        int intergenic = 0;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(filePath)));
        String strLine = null;
        while (null != (strLine = bufferedReader.readLine())) {
            String[] split = strLine.split("\t");
            if (map.get(split[0]) != null) {
                String type = split[1];
                if ("five_prime_UTR".equals(type)) {
                    five_prime_UTR = five_prime_UTR + 1;
                } else if ("three_prime_UTR".equals(type)) {
                    three_prime_UTR = three_prime_UTR + 1;
                } else if ("promoter".equals(type)) {
                    promoter = promoter + 1;
                } else if ("exon".equals(type)) {
                    exon = exon + 1;
                } else if ("intron".equals(type)) {
                    intron = intron + 1;
                } else if ("repeat".equals(type)) {
                    repeat = repeat + 1;
                } else {
                    intergenic = intergenic + 1;
                }
            }
        }
        countGene.setFive_prime_UTR(five_prime_UTR);
        countGene.setThree_prime_UTR(three_prime_UTR);
        countGene.setPromoter(promoter);
        countGene.setExon(exon);
        countGene.setIntron(intron);
        countGene.setRepeat(repeat);
        countGene.setIntergenic(intergenic);
        return countGene;
    }

    public static String generateShortUuid() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();
    }


}
