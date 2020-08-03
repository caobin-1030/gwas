package com.mugu.gene.controller;

import cn.hutool.json.JSONObject;
import com.mugu.gene.service.SiteRrunService;
import com.mugu.gene.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author : zp
 * @Description :
 * @Date : 2020/4/1
 */
@RestController
public class SiteRrunController {

    @Autowired
    private SiteRrunService siteRrunService;

    @RequestMapping("/getRun")
    public Result getRun(HttpServletRequest request){
        return siteRrunService.getAllRun(request);
    }

    @PostMapping("/snpExtent")
    public Result snpExtent(HttpServletRequest request, String r2, String win, String human, MultipartFile file, String snp){
        return siteRrunService.runSnpExtent(request,r2,win,human,file,snp);
    }

    @PostMapping("/athree")
    public Result athree(HttpServletRequest request, MultipartFile file, String snp){
        return siteRrunService.athree(request,file,snp);
    }

    @PostMapping("/afour")
    public Result afour(HttpServletRequest request, MultipartFile file, String snp){
        return siteRrunService.afour(request,file,snp);
    }


    @PostMapping("/maanno")
    public Result maanno(HttpServletRequest request, String p, MultipartFile file, String snp){
        return siteRrunService.maanno(request,p,file,snp);
    }

    @PostMapping("/bone")
    public Result bone(HttpServletRequest request, String tis, MultipartFile file, String snp){
        return siteRrunService.bone(request,tis,file,snp);
    }

    @PostMapping("/done")
    public Result done(HttpServletRequest request, String ext, MultipartFile file, String snp){
        return siteRrunService.done(request,ext,file,snp);
    }

    @PostMapping("/bfour")
    public JSONObject bfour(HttpServletRequest request, MultipartFile file, String snp){
        return siteRrunService.bfour(request,file,snp);
    }

    @PostMapping("/btwo")
    public Result btwo(HttpServletRequest request, MultipartFile file, String snp){
        return siteRrunService.btwo(request,file,snp);
    }

    @PostMapping("/bthree")
    public Result bthree(HttpServletRequest request, String tis, MultipartFile file, String snp){
        return siteRrunService.bthree(request,tis,file,snp);
    }

    @PostMapping("/bfive")
    public Result bfive(HttpServletRequest request, String tis, String sup,String snps, MultipartFile file, String snp){
        return siteRrunService.bfive(request,tis,sup,snps,file,snp);
    }

    @PostMapping("/bsix")
    public Result bfive(HttpServletRequest request, MultipartFile file, String snp){
        return siteRrunService.bsix(request,file,snp);
    }

    @PostMapping("/bsenven")
    public Result bsenven(HttpServletRequest request, String tis, MultipartFile file, String snp){
        return siteRrunService.bsenven(request,tis,file,snp);
    }

    @PostMapping("/cthree")
    public Result cthree(HttpServletRequest request, String tis, MultipartFile file, String snp){
        return siteRrunService.cthree(request,tis,file,snp);
    }
    @PostMapping("/dtwo")
    public Result dtwo(HttpServletRequest request, MultipartFile file, String snp){
        return siteRrunService.dtwo(request,file,snp);
    }
    @PostMapping("/eone")
    public Result eone(HttpServletRequest request, String tis,String chr, MultipartFile file, String snp){
        return siteRrunService.eone(request,tis,chr,file,snp);
    }
    @PostMapping("/etwo")
    public Result etwo(HttpServletRequest request, String tis, MultipartFile file, String snp){
        return siteRrunService.etwo(request,tis,file,snp);
    }

    @PostMapping("/netwo")
    public Result netwo(HttpServletRequest request, String tis, MultipartFile file, String snp){
        return siteRrunService.netwo(request,tis,file,snp);
    }

    @PostMapping("/ethree")
    public Result ethree(HttpServletRequest request, MultipartFile file, String snp){
        return siteRrunService.ethree(request,file,snp);
    }
}
