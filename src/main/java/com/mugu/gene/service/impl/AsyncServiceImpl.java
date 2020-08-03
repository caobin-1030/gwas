package com.mugu.gene.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.mail.MailUtil;
import com.mugu.gene.common.FullSnpFileRead;
import com.mugu.gene.common.GeneFileReader;
import com.mugu.gene.config.AsyncConfig;
import com.mugu.gene.jpa.RunRepository;
import com.mugu.gene.model.UserRun;
import com.mugu.gene.service.AsyncService;
import com.mugu.gene.util.Utils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author : zp
 * @Description :
 * @Date : 2019/11/4
 */
@Service
public class AsyncServiceImpl implements AsyncService {

    private static final Logger log = LoggerFactory.getLogger(AsyncConfig.class) ;

    @Value("${com.mugu.m6aMap}")
    private String snpMapFilePath;
    @Value("${com.mugu.filePath}")
    private String filePath;
    @Value("${com.mugu.gtexPath}")
    private String gtexPath;

    @Autowired
    private RunRepository runRepository;

    @Override
    @Async
    public void generateResult(String file, String fileout,String uuid) {
        UserRun userRun = runRepository.findByUuid(uuid);
        log.info("generateResult线程名称：【" + Thread.currentThread().getName() + "】");
        try {
            Map<String, List<String>> map = GeneFileReader.parseSnpToMap(snpMapFilePath);
            GeneFileReader.parseSnp(map, file, fileout);
        } catch (IOException e) {
            e.printStackTrace();
        }
        userRun.setEndTime(DateUtil.now());
        userRun.setStauts(2);
        runRepository.save(userRun);
        log.info("exit generateResult");
    }

    @Override
    @Async
    public void generateSnpResult(String file, String fileout, String organization) {
        log.info("generateSnpResult线程名称：" + Thread.currentThread().getName());
        String organPath = gtexPath + organization + ".signifpairs.txt";
        try {
            Map<String, List<String>> map = FullSnpFileRead.readFullSnpFileToMap(organPath);
            BufferedReader buffer = new BufferedReader(new FileReader(new File(file)));
            FileOutputStream outputStream = new FileOutputStream(new File(fileout));
            String strLine = null;
            while (null != (strLine = buffer.readLine())) {
                String[] split = strLine.split("\t");
                String str = split[0] + "_" + split[4] + "_" + split[2] + "_" + split[3];
                String variantId = str.replace("chr", "");
                if ("hg19chrc".equals(split[0])) {
                    String out = strLine + "\tgeneId\ttssDistance\tmaSamples\tmaCount\tmaf\tpvalNominal\tslope\tslopeSe\tpvalNominalThreshold\tminPvalNominal\tpvalBeta\n";
                    outputStream.write(out.getBytes());
                    continue;
                }
                List<String> byVariantId = map.get(variantId);
                if (StringUtils.isEmpty(byVariantId)) {
                    outputStream.write((strLine + "\n").getBytes());
                    continue;
                }
                for (String eqtlEntity : byVariantId) {
                    String out = strLine + "\t" + eqtlEntity + "\n";
                    outputStream.write(out.getBytes());
                }
            }
            buffer.close();
            outputStream.close();
            log.info("end");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Async
    public void runCmd(String cmd, String uname,String uuid) {
        UserRun userRun = runRepository.findByUuid(uuid);
        userRun.setStauts(1);
        runRepository.save(userRun);
        log.info("start run cmd "+ cmd);
        try {
            String run = Utils.run(cmd);
            log.info(run);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String s = "cmd" + cmd;
        userRun.setEndTime(DateUtil.now());
        userRun.setStauts(2);
        runRepository.save(userRun);
        MailUtil.send(uname,"GWAS", userRun.getTitle()+" analysis has been completed. Please go to the download module to view the results. " +
                "<a href=\"http://gwas.shengxin.ren/download.html\">To Download</a>", true);
    }

    @Override
    public void runHbase(String id, String tis, String p) {
        UserRun userRun = runRepository.findByUuid(id);
        userRun.setStauts(1);
        runRepository.save(userRun);
        try {
            Utils.run(userRun.getCmd());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String o = userRun.getPath();
        String uid = Utils.runHumanBase(o, tis, p);
        String result = null;
        while (result==null){
            result = Utils.getResult(uid);
            try {
                TimeUnit.MINUTES.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String results = Utils.getResults(result);
        File file = new File(o+"/result.txt");
        if (results!=null) {
            try {
                FileWriter fw = new FileWriter(file);
                fw.write(results);
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        MailUtil.send(userRun.getEmail(),"GWAS", userRun.getTitle()+" analysis has been completed. Please go to the download module to view the results. " +
                "<a href=\"http://gwas.shengxin.ren/download.html\">To Download</a>", true);
    }

    @Override
    @Async
    public void runCmds(String cmd, String uname,String uuid,String o) {
        UserRun userRun = runRepository.findByUuid(uuid);
        userRun.setStauts(1);
        runRepository.save(userRun);
        log.info("start run cmd "+ cmd);
        try {
            String run = Utils.runs(cmd,o);
            log.info(run);
        } catch (Exception e) {
            e.printStackTrace();
        }
        File file = new File(o+"/permu");
        if (file.exists()){
            file.delete();
        }
        userRun.setEndTime(DateUtil.now());
        userRun.setStauts(2);
        runRepository.save(userRun);
        MailUtil.send(uname,"GWAS", userRun.getTitle()+" analysis has been completed. Please go to the download module to view the results. " +
                "<a href=\"http://gwas.shengxin.ren/download.html\">To Download</a>", true);
    }

    @Override
    @Async
    public void netWAS(String uuid, String tis, String sup, String snps, String cmd,String o) {
        UserRun userRun = runRepository.findByUuid(uuid);
        userRun.setStauts(1);
        runRepository.save(userRun);
        log.info("start run cmd "+ cmd);
        try {
            String run = Utils.run(cmd);
            log.info(run);
            String name = o+"/netwas_input.txt";
            File file = new File(name);
            log.info(file.getName() + file.exists() + ">>>>>>>>>>");
            if (file.exists()){
                CloseableHttpClient client = HttpClients.createDefault();
                HttpPost post = new HttpPost("https://hb.flatironinstitute.org/api/netwas/");
                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                builder.addBinaryBody("gwas_file", file, ContentType.DEFAULT_BINARY, name);
                builder.addTextBody("gwas_format", "vegas", ContentType.DEFAULT_BINARY);
                builder.addTextBody("tissue", "adipose_tissue", ContentType.DEFAULT_BINARY);
                builder.addTextBody("p_value", "0.01", ContentType.DEFAULT_BINARY);
                //
                HttpEntity entity = builder.build();
                post.setEntity(entity);
                HttpResponse response = null;
                try {
                    response = client.execute(post);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!StringUtils.isEmpty(response)) {
                    entity = response.getEntity();
                }
                String result = null;
                try {
                    result = EntityUtils.toString(entity);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                log.info(result);
            }
            userRun.setStauts(2);
            runRepository.save(userRun);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void runBfour(String cmd, String uname, String uuid, String name, String o) {
        try {
            String run = Utils.run(cmd);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
