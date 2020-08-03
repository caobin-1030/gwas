package com.mugu.gene.common;

import com.mugu.gene.model.Region;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.*;

/**
 * @Author : zp
 * @Description :
 * @Date : 2019/10/18
 */
public class GeneFileReader {

    /**
     * 读取文件转换为基因数列
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static List<Region> regionsRead(String file) throws IOException {
        List<Region> regionList = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String strLine = null;
        while (null != (strLine = bufferedReader.readLine())) {
            Region region = new Region();
            String[] split = strLine.split(",");
            region.setChrom(split[0]);
            region.setStart(Integer.valueOf(split[1]));
            region.setEnd(Integer.valueOf(split[2]));
            region.setStand(Integer.valueOf(split[3]));
            region.setType(split[4]);
            regionList.add(region);
        }
        bufferedReader.close();
        return regionList;
    }


    /**
     * 将所有的基因组分类
     *
     * @param regionList 所有的基因list
     * @return
     */
    public static Map<String, List<Region>> findWhichGene(List<Region> regionList) {
        Set<String> chrSet = new HashSet<>();
        //按染色体存放基因
        Map<String, List<Region>> reg = new HashMap<>();
        for (Region region : regionList) {
            String chrom = region.getChrom();
            boolean add = chrSet.add(chrom);
            if (add) {
                List<Region> regions1 = new ArrayList<>();
                regions1.add(region);
                reg.put(chrom, regions1);
            } else {
                List<Region> regions2 = reg.get(chrom);
                regions2.add(region);
                reg.put(chrom, regions2);
            }
        }
        return reg;
    }

    /**
     * 将基因拆为map
     *
     * @param map
     * @return
     */
    public static Map<String, Map<Integer, String>> stringMapMap(Map<String, List<Region>> map, List<Integer> arr) {
        Map<String, Map<Integer, String>> geneMap = new HashMap<>(35);
        for (Map.Entry<String, List<Region>> entry : map.entrySet()) {
            Map<Integer, String> whichGene = new HashMap<>();
            List<Region> value = entry.getValue();
            Collections.sort(value);
            for (int i = 0; i < value.size(); i++) {
                Region region = value.get(i);
                if (i == value.size() - 1) {
                    arr.add(value.get(i).getStart());
                    whichGene.put(region.getStart(), region.getType());
                    whichGene.put(region.getEnd(), "intergenic");
                    continue;
                }
                Region next = value.get(i + 1);
                if (region.getEnd() != next.getStart()) {
                    whichGene.put(region.getEnd(), "intergenic");
                    arr.add(region.getEnd());
                } else {
                    whichGene.put(region.getStart(), region.getType());
                    arr.add(region.getStart());
                }
            }
            Collections.sort(arr);
            geneMap.put(entry.getKey(), whichGene);
        }
        return geneMap;
    }

    /**
     * 将m6ASNPmap文件读进内存
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static Map<String, List<String>> parseSnpToMap(String file) throws IOException {
        BufferedReader buffer = new BufferedReader(new FileReader(new File(file)));
        Map<String, List<String>> snpMap = new HashMap<>();
        String strLine = null;
        while (null != (strLine = buffer.readLine())) {
            String key = strLine.split("\t")[1];
            List<String> list = snpMap.get(key);
            if (!StringUtils.isEmpty(list)) {
                String value = null;
                for (String strs : list) {
                    String[] split = strs.split("\t");
                    String[] split1 = strLine.split("\t");
                    if (split.length > 2 && split1.length > 2) {
                        value = split[0] + "/" + split1[0] + "\t" + split[1] + "/" + split1[1] + "\t" + split[2] + "/" + split1[2] + "\t";
                    } else {
                        value = strs;
                    }
                }
                list.clear();
                list.add(value);
            } else {
                list = new ArrayList<>();
                list.add(strLine);
                snpMap.put(key, list);
            }
        }
        buffer.close();
        return snpMap;
    }

    public static void parseSnp(Map<String, List<String>> snpMap, String file, String fileout) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        FileOutputStream fileOutputStream = new FileOutputStream(new File(fileout));
        String strLine = null;
        while (null != (strLine = bufferedReader.readLine())) {
            String[] split = strLine.split("\t");
            String s1 = split[0];
            StringBuilder sb = new StringBuilder();
            sb.append(strLine);
            int i = 0;
            if ("hg19chrc".equals(s1)) {
                String out = strLine + "\t" + "m6AID" + "\t" + "m6Aposition" + "\t" + "m6Afunction" + "\n";
                byte[] outFileBytes = out.getBytes();
                fileOutputStream.write(outFileBytes);
                continue;
            }
            List<String> list = snpMap.get(split[1]);
            if (list != null) {
                sb.append("\t").append(list.get(0));
            }
            sb.append("\n");
            String outfile = sb.toString();
            byte[] outFileBytes = outfile.getBytes();
            fileOutputStream.write(outFileBytes);

        }
        fileOutputStream.close();
        bufferedReader.close();
    }
}
