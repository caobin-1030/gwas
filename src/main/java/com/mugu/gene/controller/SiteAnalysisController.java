package com.mugu.gene.controller;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.mugu.gene.service.SiteAnnotationService;
import com.mugu.gene.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author : zp
 * @Description :
 * @Date : 2019/10/17
 */

@RestController
@RequestMapping("/run")
public class SiteAnalysisController {

    private Log log = LogFactory.get();

    @Autowired
    private SiteAnnotationService annotationService;

    @RequestMapping(value = "/siteannotation", method = RequestMethod.POST)
    public Result getSiteAnnotation(@RequestParam(value = "uploadFile", required = false) MultipartFile uploadFile,
                                    @RequestParam(value = "pvalue", required = false) String pvalue,
                                    @RequestParam(value = "snpId", required = false) String snpId) {
        log.info("enter into siteannotation");
        return annotationService.getSiteAnnotationFromFile(uploadFile, pvalue, snpId);
    }

    @RequestMapping(value = "/snpMapAnalysis", method = RequestMethod.POST)
    public Result getSnpMapAnalysisFile(@RequestParam(value = "uploadFile", required = false) MultipartFile uploadFile,
                                        @RequestParam(value = "snpId", required = false) String snpId, HttpServletRequest request) {
        log.info("enter into snpMapAnalysis");
        return annotationService.getSnpMapAnalysisFile(request, uploadFile, snpId);
    }

    @RequestMapping(value = "/eqtlSnpFile", method = RequestMethod.POST)
    public Result getEqtlSnpFile(@RequestParam(value = "uploadFile", required = false) MultipartFile uploadFile,
                                 @RequestParam(value = "organization", required = false) String organization, String snpId, HttpServletRequest request) {
        log.info("enter into eqtlSnpFile");
        return annotationService.getEqtlSnpFile(uploadFile, snpId, organization, request);
    }

    @RequestMapping(value = "/linSight", method = RequestMethod.POST)
    public Result linSight(@RequestParam(value = "uploadFile", required = false) MultipartFile uploadFile,
                           @RequestParam(value = "snpId", required = false) String snpId, String n, String b, String c, HttpServletRequest request) {
        return annotationService.linSight(uploadFile, snpId, n, b, c, request);
    }

    @RequestMapping(value = "/downLoadResult", method = RequestMethod.GET)
    public Result getSnpMapAnalysisOut(@RequestParam(value = "id") String id, HttpServletResponse response) {
        return annotationService.getSnpMapAnalysisOut(id, response);
    }

}
