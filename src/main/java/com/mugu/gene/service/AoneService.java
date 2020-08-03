package com.mugu.gene.service;

import com.mugu.gene.web.JsonResult;

/**
 * @Author : zp
 * @Description :
 * @Date : 2020/4/1
 */
public interface AoneService {
    JsonResult aone(Double pval,Integer under,String peopele,String threshold);
}
