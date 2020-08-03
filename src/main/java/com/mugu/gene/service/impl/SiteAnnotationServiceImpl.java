package com.mugu.gene.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.mugu.gene.common.GeneFileReader;
import com.mugu.gene.common.GeneType;
import com.mugu.gene.controller.MediaTypeUtils;
import com.mugu.gene.jpa.RunRepository;
import com.mugu.gene.model.CountGene;
import com.mugu.gene.model.Region;
import com.mugu.gene.model.UserRun;
import com.mugu.gene.service.AsyncService;
import com.mugu.gene.service.SiteAnnotationService;
import com.mugu.gene.util.FileReadUtil;
import com.mugu.gene.util.ListUtils;
import com.mugu.gene.util.StrUtils;
import com.mugu.gene.web.Result;
import com.mugu.gene.web.ResultMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * @Author : zp
 * @Description :
 * @Date : 2019/10/17
 */

@Service
public class SiteAnnotationServiceImpl implements SiteAnnotationService {

    private Log log = LogFactory.get();

    @Autowired
    private ServletContext servletContext;

    @Value("${com.mugu.filePath}")
    private String filePath;

    @Value("${com.mugu.snpMapfile}")
    private String snpMapfile;

    @Value("${com.mugu.outFileName}")
    private String outFileName;

    @Value("${com.mugu.upload}")
    private String uploadPath;

    @Value("${com.mugu.result}")
    private String resultPath;

    @Value("${com.mugu.userdata}")
    private String userPath;

    @Autowired
    AsyncService asyncService;

    @Autowired
    RunRepository runRepository;

    @Override
    public String getSiteAnnotation() throws IOException {
        return null;
    }

    @Override
    public Result getSiteAnnotationFromFile(MultipartFile file, String pvalue, String str) {
        log.info("into getSiteAnnotationFromFile value:{}" + pvalue);
        if (!StringUtils.isEmpty(pvalue) && StrUtils.isNotStrDou(pvalue)) {
            return Result.failed("p值错误", ResultMsg.PARM_ERROR);
        }
        try {
            CountGene countGene = new CountGene();
            List<Integer> arr = new ArrayList<>();
            List<Region> regions = GeneFileReader.regionsRead(snpMapfile);
            Map<String, List<Region>> whichGene = GeneFileReader.findWhichGene(regions);
            Map<String, Map<Integer, String>> stringMapMap = GeneFileReader.stringMapMap(whichGene, arr);
            int five_prime_UTR = 0;
            int three_prime_UTR = 0;
            int promoter = 0;
            int exon = 0;
            int intron = 0;
            int repeat = 0;
            int intergenic = 0;
            File runFile;
            if (!StringUtils.isEmpty(file)) {
                String filename = file.getOriginalFilename();
                runFile = new File(uploadPath + filename);
                file.transferTo(runFile);
            } else if (StringUtils.isEmpty(file) && !StringUtils.isEmpty(str)) {
                File tmpFile = new File(filePath + FileReadUtil.generateShortUuid());
                FileWriter fw = new FileWriter(tmpFile);
                fw.write(str);
                fw.flush();
                fw.close();
                runFile = tmpFile;
            } else {
                return Result.failed("error");
            }
            BufferedReader bufr = new BufferedReader(new FileReader(runFile));
            String line;
            while ((line = bufr.readLine()) != null) {
                String[] split = line.split("\t");
                if (StrUtils.isNotStrNum(split[4]) || split.length != 10 || StrUtils.isNotStrDou(split[8])) {
                    continue;
                }
                boolean flag = !StringUtils.isEmpty(pvalue) && Double.valueOf(split[8]) < Double.valueOf(pvalue);
                if (flag || StringUtils.isEmpty(pvalue)) {
                    Map<Integer, String> integerStringMap = stringMapMap.get(split[0]);
                    int bp = Integer.parseInt(split[4]);
                    Integer reginre = ListUtils.recursionBinarySearch(arr, bp, 0, arr.size() - 1);
                    if (reginre == -1) {
                        intergenic += 1;
                        continue;
                    }
                    if (integerStringMap == null) {
                        intergenic += 1;
                        continue;
                    }
                    String type = integerStringMap.get(reginre);
                    if (GeneType.FIVE.equals(type)) {
                        five_prime_UTR = five_prime_UTR + 1;
                    } else if (GeneType.THREE.equals(type)) {
                        three_prime_UTR = three_prime_UTR + 1;
                    } else if (GeneType.PRO.equals(type)) {
                        promoter = promoter + 1;
                    } else if (GeneType.EXON.equals(type)) {
                        exon = exon + 1;
                    } else if (GeneType.INT.equals(type)) {
                        intron = intron + 1;
                    } else if (GeneType.REP.equals(type)) {
                        repeat = repeat + 1;
                    } else {
                        intergenic = intergenic + 1;
                    }
                }
            }
            bufr.close();
            countGene.setFive_prime_UTR(five_prime_UTR);
            countGene.setThree_prime_UTR(three_prime_UTR);
            countGene.setPromoter(promoter);
            countGene.setExon(exon);
            countGene.setIntron(intron);
            countGene.setRepeat(repeat);
            countGene.setIntergenic(intergenic);
            return Result.succeed(countGene, ResultMsg.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("fail to read file");
            return Result.failed("文件处理异常", ResultMsg.PARM_ERROR);
        }
    }

    @Override
    public Result getSnpMapAnalysisFile(HttpServletRequest request ,MultipartFile file, String snpId) {
        String uname = request.getHeader("uname");
        if (StringUtils.isEmpty(uname)){
            return Result.failed(ResultMsg.PARM_ERROR);
        }
        String uuid = getSnpFile(file, snpId,uname);
        String name = Objects.isNull(file) ? uuid : file.getOriginalFilename();
        String fileUploadName = userPath + uname + File.separator+ uuid  + "/upload/" + name;
        String fileResultName = userPath + uname + File.separator+ uuid  + "/result/"+ outFileName;
        UserRun userRun = new UserRun(uname,0, DateUtil.now(), fileResultName ,uuid);
        userRun.setIsFile(1);
        runRepository.save(userRun);
        asyncService.generateResult(fileUploadName, fileResultName,uuid);
        return Result.succeed(uuid, ResultMsg.SUCCESS);
    }

    @Override
    public Result getSnpMapAnalysisOut(String id, HttpServletResponse response) {
        log.info("enter getSnpMapAnalysisOut id :" + id);
        String outfilePath = resultPath + id + File.separator + outFileName;
        MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servletContext, outFileName);
        File file = new File(outfilePath);
        if (file.exists()) {
            response.setContentType(mediaType.getType());
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName());
            response.setContentLength((int) file.length());
            try {
                BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(file));
                BufferedOutputStream outStream = new BufferedOutputStream(response.getOutputStream());
                byte[] buffer = new byte[1024];
                int bytesRead = 0;
                while ((bytesRead = inStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                }
                outStream.flush();
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return Result.failed("文件未解析完", ResultMsg.SUCCESS);
        }
        log.info("download getSnpMapAnalysisOut finshed");
        return null;
    }

    @Override
    public Result getEqtlSnpFile(MultipartFile uploadFile, String snpId, String organization,HttpServletRequest request) {
        String uname = request.getHeader("uname");
        if (StringUtils.isEmpty(uname)){
            return Result.failed(ResultMsg.USER_NOT_FOUND);
        }
        String uuid = getSnpFile(uploadFile, snpId,uname);
        String name = Objects.isNull(uploadFile) ? uuid : uploadFile.getOriginalFilename();
        String fileUploadName = userPath + uname + File.separator+ uuid  + "/upload/" + name;
        log.info("filePath" + fileUploadName);
        String fileResultName = userPath + uname + File.separator+ uuid  + "/result/"+ outFileName;
        asyncService.generateSnpResult(fileUploadName, fileResultName, organization);
        return Result.succeed(uuid, ResultMsg.SUCCESS);
    }

    @Override
    public Result linSight(MultipartFile uploadFile, String snpId, String n, String b, String c, HttpServletRequest request) {
        String uname = request.getHeader("uname");
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String path = userPath + uname + File.separator + uuid + "/upload/";
        if (uploadFile != null) {
            path = path + uploadFile.getOriginalFilename();
            try {
                uploadFile.transferTo(new File(path + uploadFile.getOriginalFilename()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            path = path + "mydata";
            File file = new File(path);
            try {
                FileWriter fw = new FileWriter(file);
                fw.write(snpId);
                fw.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        StringBuilder cmd = new StringBuilder("Rscript /home/soft/LINSIGHT/LINSIGHT_batch.R -i ");
        cmd.append(path).append(" -n ").append(n).append(" -b ").append(b).append(" -c ").append(c);
        String fileResultName = resultPath + uuid + File.separator;
        cmd.append(" -o ").append(fileResultName);
        asyncService.runCmd(cmd.toString(), uname,uuid);
        log.info("cmd : " + cmd.toString());
        return Result.succeed(uuid, ResultMsg.SUCCESS);
    }


    private String getSnpFile(MultipartFile file, String snpId,String uname) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        log.info("filename " + uuid);
        String f1ileUploadName = userPath + uname + File.separator+ uuid + "/upload/";
        String f1ileResultName = userPath + uname + File.separator+ uuid + "/result/";
        File files = new File(f1ileUploadName);
        File out = new File(f1ileResultName);
        if (!files.exists()) {
            files.mkdirs();
        }
        if (!out.exists()) {
            out.mkdirs();
        }
        if (Objects.isNull(file)) {
            File f = new File(f1ileUploadName + uuid);
            try {
                FileWriter fileWriter = new FileWriter(f);
                fileWriter.write(snpId);
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return uuid;
        }
        String filename = file.getOriginalFilename();
        try {
            file.transferTo(new File(f1ileUploadName + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uuid;
    }

}
