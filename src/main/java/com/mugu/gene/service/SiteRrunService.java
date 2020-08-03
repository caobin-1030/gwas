package com.mugu.gene.service;

import cn.hutool.json.JSONObject;
import com.mugu.gene.web.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author : zp
 * @Description :
 * @Date : 2020/5/13
 */
public interface SiteRrunService {

    Result getAllRun(HttpServletRequest request);

    Result runSnpExtent(HttpServletRequest request, String r2, String win, String human, MultipartFile file, String snp);

    Result maanno(HttpServletRequest request, String p, MultipartFile file, String snp);

    Result bone(HttpServletRequest request, String tis, MultipartFile file, String snp);

    Result done(HttpServletRequest request, String ext, MultipartFile file, String snp);

    Result athree(HttpServletRequest request, MultipartFile file, String snp);

    Result bfive(HttpServletRequest request, String tis, String sup,String snps, MultipartFile file, String snp);

    Result bsenven(HttpServletRequest request, String tis, MultipartFile file, String snp);

    Result bsix(HttpServletRequest request, MultipartFile file, String snp);

    Result eone(HttpServletRequest request, String tis, String chr, MultipartFile file, String snp);

    JSONObject bfour(HttpServletRequest request, MultipartFile file, String snp);

    Result bthree(HttpServletRequest request, String tis, MultipartFile file, String snp);

    Result dtwo(HttpServletRequest request, MultipartFile file, String snp);

    Result cthree(HttpServletRequest request, String tis, MultipartFile file, String snp);

    Result btwo(HttpServletRequest request, MultipartFile file, String snp);

    Result afour(HttpServletRequest request, MultipartFile file, String snp);

    Result etwo(HttpServletRequest request, String tis, MultipartFile file, String snp);

    Result ethree(HttpServletRequest request, MultipartFile file, String snp);

    Result netwo(HttpServletRequest request, String tis, MultipartFile file, String snp);
}
