package com.mugu.gene.service;

import com.mugu.gene.web.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author : zp
 * @Description :
 * @Date : 2019/10/17
 */

public interface SiteAnnotationService {
    String getSiteAnnotation() throws IOException;

    Result getSiteAnnotationFromFile(MultipartFile file, String pvalue, String str);

    Result getSnpMapAnalysisFile(HttpServletRequest request,MultipartFile file ,String snpId);

    Result getSnpMapAnalysisOut(String id, HttpServletResponse response);

    Result getEqtlSnpFile(MultipartFile uploadFile,String snpId, String organization,HttpServletRequest request);

    Result linSight(MultipartFile uploadFile,String snpId,String n, String b, String c, HttpServletRequest request);
}
