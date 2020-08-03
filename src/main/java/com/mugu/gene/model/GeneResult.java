package com.mugu.gene.model;

import lombok.Data;

/**
 * @Author : zp
 * @Description :
 * @Date : 2020/6/18
 */
@Data
public class GeneResult {
    private String chr;
    private Integer start;
    private Integer end;
    private String stand;
    private String id;
    private String symbol;
    private String cyto;
    private Object score;

    public GeneResult(String chr, Integer start, Integer end, String stand, String id, String symbol, String cyto,Object score) {
        this.chr = chr;
        this.start = start;
        this.end = end;
        this.stand = stand;
        this.id = id;
        this.symbol = symbol;
        this.cyto = cyto;
        this.score = score;
    }

    @Override
    public String toString() {
        return "{" +
                "chr:'" + chr + '\'' +
                ", start:" + start +
                ", end:" + end +
                ", stand:'" + stand + '\'' +
                ", id:'" + id + '\'' +
                ", symbol:'" + symbol + '\'' +
                ", cyto:'" + cyto + '\'' +
                ", score:" + score +
                '}';
    }
}
