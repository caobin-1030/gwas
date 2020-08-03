package com.mugu.gene.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author : zp
 * @Description :
 * @Date : 2019/10/15
 */
@Data
public class CountGene implements Serializable {
    int five_prime_UTR;
    int three_prime_UTR;
    int promoter;
    int exon;
    int intron;
    int repeat;
    int intergenic;

    @Override
    public String toString() {
        return "five_prime_UTR:" + five_prime_UTR +
                ", three_prime_UTR:" + three_prime_UTR +
                ", promoter:" + promoter +
                ", exon:" + exon +
                ", intron:" + intron +
                ", repeat:" + repeat +
                ", intergenic:" + intergenic;
    }
}
