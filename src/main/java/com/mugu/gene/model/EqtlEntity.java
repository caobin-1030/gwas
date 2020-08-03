//package com.mugu.gene.model;
//
//import lombok.Data;
//
//import javax.persistence.*;
//
///**
// * @Author : zp
// * @Description :
// * @Date : 2019/11/4
// */
//@Data
//@Entity
//@Table(name = "tb_gtex")
//public class EqtlEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private long id;
//    @Column(name = "variant_id")
//    private String variantId;
//    @Column(name = "gene_id")
//    private String geneId;
//    @Column(name = "tss_distance")
//    private String tssDistance;
//    @Column(name = "ma_samples")
//    private String maSamples;
//    @Column(name = "ma_count")
//    private String maCount;
//    @Column(name = "maf")
//    private String maf;
//    @Column(name = "pval_nominal")
//    private String pvalNominal;
//    @Column(name = "slope")
//    private String slope;
//    @Column(name = "slope_se")
//    private String slopeSe;
//    @Column(name = "pval_nominal_threshold")
//    private String pvalNominalThreshold;
//    @Column(name = "min_pval_nominal")
//    private String minPvalNominal;
//    @Column(name = "pval_beta")
//    private String pvalBeta;
//    @Column(name = "organization")
//    private String organization;
//
//    @Override
//    public String toString() {
//        return geneId + "\t" + tssDistance + "\t" + maSamples + "\t" + maCount + "\t" + maf + "\t" + pvalNominal + "\t" + slope + "\t" + slopeSe + "\t" + pvalNominalThreshold + "\t" + minPvalNominal + "\t" + pvalBeta + "\t";
//    }
//}
