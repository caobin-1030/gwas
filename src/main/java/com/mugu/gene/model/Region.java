package com.mugu.gene.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author : zp
 * @Description :
 * @Date : 2019/10/14
 */
@Data
public class Region implements Comparable<Region>, Serializable {
    private String chrom;
    private int start;
    private int end;
    private int stand;
    private String type;

    public Region(String chrom, int start, int end, int stand, String type) {
        this.chrom = chrom;
        this.start = start;
        this.end = end;
        this.stand = stand;
        this.type = type;
    }

    public Region() {
    }

    @Override
    public int compareTo(Region o) {
        return this.getStart() - o.getStart();
    }

    @Override
    public String toString() {
        return chrom + "," + start + "," + end + "," + stand + "," + type;
    }
}