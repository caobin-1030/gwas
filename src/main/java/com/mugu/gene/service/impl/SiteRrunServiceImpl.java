package com.mugu.gene.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.mugu.gene.common.OkHttpUtil;
import com.mugu.gene.jpa.RunRepository;
import com.mugu.gene.model.GeneResult;
import com.mugu.gene.model.UserRun;
import com.mugu.gene.service.AsyncService;
import com.mugu.gene.service.SiteRrunService;
import com.mugu.gene.util.Utils;
import com.mugu.gene.web.JsonResult;
import com.mugu.gene.web.Result;
import com.mugu.gene.web.ResultMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;

/**
 * @Author : zp
 * @Description :
 * @Date : 2020/4/1
 */
@Service
public class SiteRrunServiceImpl implements SiteRrunService {

    @Autowired
    private RunRepository runRepository;

    @Autowired
    private AsyncService asyncService;

    @Value("${com.mugu.userdata}")
    private String userPath;

    @Override
    public Result getAllRun(HttpServletRequest request) {
        String uname = request.getHeader("uname");
        List<UserRun> timeDesc = runRepository.findByEmailOrderByStartTimeDesc(uname);
        if (timeDesc.size() > 10) {
            timeDesc = timeDesc.subList(0, 10);
        }
        return Result.succeed(timeDesc, ResultMsg.SUCCESS);
    }

    @Override
    public Result runSnpExtent(HttpServletRequest request, String r2, String win, String human, MultipartFile file,String snp) {
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String uname = request.getHeader("uname");
        String name = getFile(uname, uuid, file, snp);
        String o  = userPath + uname +File.separator+ uuid + "/result";
        File os = new File(o);
        if (!os.exists()){
            os.mkdirs();
        }
        human = StringUtils.isEmpty(human)? "ACB":human;
        win = StringUtils.isEmpty(win)? "10000":win;
        r2 = StringUtils.isEmpty(r2)?"0.7":r2;
        String cmd = "sh /home/soft/vcftools-0.1.16/A1_process.sh "+human+" "+ win+ " " +r2+" "+ name + " " + o;
        System.out.println(cmd);
        return getResult(uuid, uname, o, cmd,"SNP extent");
    }

    @Override
    public Result maanno(HttpServletRequest request, String p, MultipartFile file, String snp) {
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String uname = request.getHeader("uname");
        String name = getFile(uname, uuid, file, snp);
        p = StringUtils.isEmpty(p)?"0.05":p.trim();
        String o  = userPath + uname +File.separator+ uuid + "/result";
        String cmd = "perl /home/soft/A5/A5_combine_nop.pl "+name + " /home/data/Human_protein_coding.txt "+ o;
        return getResult(uuid, uname, o, cmd,"m6A annotation");
    }

    @Override
    public Result bone(HttpServletRequest request, String tis, MultipartFile file, String snp) {
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String uname = request.getHeader("uname");
        String name = getFile(uname, uuid, file, snp);
        String o  = userPath + uname +File.separator+ uuid + "/result";
        String cmd = "perl /home/soft/B1_1/B1_1_SNP_tissue_match.pl "+ name +" "+ tis + " "+ o;
        return getResult(uuid, uname, o, cmd,"GTEx");
    }

    @Override
    public Result done(HttpServletRequest request, String ext, MultipartFile file, String snp) {
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String uname = request.getHeader("uname");
        String name = getFile(uname, uuid, file, snp);
        String o  = userPath + uname +File.separator+ uuid + "/result";
        String cmd = "perl /home/soft/D1/D1_process.pl "+ name +" "+ ext + " "+ o;
        return getResult(uuid, uname, o, cmd,"GTEx");
    }

    @Override
    public Result athree(HttpServletRequest request, MultipartFile file, String snp) {
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String uname = request.getHeader("uname");
        String name = getFile(uname, uuid, file, snp);
        String o  = userPath + uname +File.separator+ uuid + "/result";
        String cmd = "perl /home/soft/A3/A3_1_match_score.pl "+ name + " "+ o;
        return getResult(uuid, uname, o, cmd,"Linsight");
    }

    @Override
    public Result bfive(HttpServletRequest request, String tis, String sup,String snps, MultipartFile file, String snp) {
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String uname = request.getHeader("uname");
        String name = getFile(uname, uuid, file, snp);
        String o  = userPath + uname +File.separator+ uuid + "/result";
        File f = new File(o);
        if (!f.exists()){
            f.mkdirs();
        }
        String cmd = "perl /home/soft/B5/B5_process.pl "+ name + " "+ o +" "+ snps+" "+ sup;
        UserRun userRun = new UserRun(uname, 0, DateUtil.now(), o, uuid);
        userRun.setCmd(cmd);
        userRun.setTitle("NetWAS");
        runRepository.save(userRun);
        asyncService.netWAS(uuid,tis,sup,snps,cmd,o);
        return Result.succeed(uuid);
    }

    @Override
    public Result bsenven(HttpServletRequest request, String tis, MultipartFile file, String snp) {
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String uname = request.getHeader("uname");
        String name = getFile(uname, uuid, file, snp);
        String o  = userPath + uname +File.separator+ uuid + "/result";
        String cmd ;
        if ("GeneID".equals(tis)) {
            cmd = "perl /home/soft/B7/gene_entrezid_match.pl " + name + " " + o;
        }else {
            cmd = "perl /home/soft/B7/gene_genesymbol_match.pl " + name + " " + o;
        }
        return getResult(uuid, uname, o, cmd,"PPI Analysis");
    }

    @Override
    public Result bsix(HttpServletRequest request, MultipartFile file, String snp) {
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String uname = request.getHeader("uname");
        String name = getFile(uname, uuid, file, snp);
        String o  = userPath + uname +File.separator+ uuid + "/result";
        File f = new File("/home/soft/B6_depict/template.cfg");
        File file1 = new File(name);
        try {
            BufferedReader bf = new BufferedReader(new FileReader(f));

            FileWriter fw = new FileWriter(file1.getParent()+"/template.cfg");
            String str;
            while ((str=bf.readLine())!=null){
                if (str.contains("analysis_path:")){
                    str = "analysis_path: "+o;
                }else if (str.contains("gwas_summary_statistics_file:")){
                    str = "gwas_summary_statistics_file:"+ name;
                }
                fw.write(str+"\n");
            }
            fw.close();
            bf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String cmd = "/home/soft/depict/src/python/depict.py "+ file1.getParent()+"/template.cfg";
        return getResult(uuid,uname,o,cmd,"Depict Analysis");
    }

    @Override
    public Result eone(HttpServletRequest request, String tis, String chr, MultipartFile file, String snp) {
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String uname = request.getHeader("uname");
        String name = getFile(uname, uuid, file, snp);
        String o  = userPath + uname +File.separator+ uuid + "/result";
        String cmd = "perl /home/soft/fusion_twas-master/make_input/E1.process.pl "+ name + " " +chr+" " +tis + " "+ o;
        return getResult(uuid,uname,o,cmd,"FUSION ANALYSIS");
    }

    @Override
    public JSONObject bfour(HttpServletRequest request, MultipartFile file, String snp) {
        JSONObject resp = new JSONObject();
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String uname = request.getHeader("uname");
        String name = getFile(uname, uuid, file, snp);
        try {
            List<String> list = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader(new File(name)));
            String str ;
            while ((str=br.readLine())!=null){
                list.add(str);
            }
            JSONObject js = new JSONObject();
            js.put("dbsnp_ids",list);
            js.put("downstream_window",50000);
            js.put("r_square_threshold",0.25);
            js.put("upstream_window",500000);
            System.out.println(js.toString());
            String getpost = OkHttpUtil.getpost("http://dalai.mshri.on.ca/prixfixe/cgi/GWASnet/0.7/SnpsToGeneSets", js);
            JSONObject jo = new JSONObject(getpost);
            JSONObject result = jo.getJSONObject("result");
            JSONArray ldRegions = result.getJSONArray("ld_regions");
            List<List<String>> arrs = new ArrayList<>();
            List<GeneResult> genes = new ArrayList<>();
            for (int i = 0; i <ldRegions.size() ; i++) {
                JSONObject o = (JSONObject)ldRegions.get(i);
                JSONArray entrezgenes = o.getJSONArray("entrezgenes");
                String chrom = o.getStr("chrom");
                String cyto = o.getStr("cyto");
                List<String> arr = new ArrayList<>();
                for (int j = 0; j <entrezgenes.size() ; j++) {
                    JSONObject jsonObject = (JSONObject)entrezgenes.get(j);
                    String id = jsonObject.getStr("id");
                    genes.add(new GeneResult(chrom,Integer.valueOf(jsonObject.getStr("start_incl")),
                            Integer.valueOf(jsonObject.getStr("end_excl")),jsonObject.getStr("strand"),id,jsonObject.getStr("symbol"),cyto,0));
                    arr.add(id);
                }
                arrs.add(arr);
//                resp.put(chrom,genes);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("entrezgene_id_sets",arrs);
            String response = OkHttpUtil.getposts("http://dalai.mshri.on.ca/prixfixe/cgi/GWASnet/0.7/PrixFixeGA", jsonObject);
            JSONObject object = new JSONObject(response);
            JSONObject res = object.getJSONObject("result");

            JSONArray scores = res.getJSONArray("scores");
            Map<Object,Object> map = new HashMap<>();
            for (int i = 0; i <scores.size() ; i++) {
                JSONObject jsonOb = (JSONObject)scores.get(i);
                for (JSONObject.Entry a: jsonOb.entrySet()) {
                    map.put(a.getKey(),a.getValue());
                }
            }
            Set<String> set = new HashSet<>();
            for (GeneResult gene: genes) {
                Object o = map.get(gene.getId());
                gene.setScore(o);
                set.add(gene.getChr());
            }
            for (Object x : set){
                List<GeneResult> li = new ArrayList<>();
                for (GeneResult gene: genes) {
                    if (gene.getChr().equals(x)){
                        li.add(gene);
                    }
                }
                resp.put(x.toString(),li);
            }
            System.out.println(resp.toString());
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Result bthree(HttpServletRequest request, String tis, MultipartFile file, String snp) {
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String uname = request.getHeader("uname");
        String name = getFile(uname, uuid, file, snp);
        String o  = userPath + uname +File.separator+ uuid + "/result";
        String cmd = "perl /home/soft/smr_Linux/B3_process.pl "+ name +" " +tis + " "+ o;
        return getResult(uuid,uname,o,cmd,"SMR Analysis");

    }

    @Override
    public Result dtwo(HttpServletRequest request, MultipartFile file, String snp) {
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String uname = request.getHeader("uname");
        String name = getFile(uname, uuid, file, snp);
        String o  = userPath + uname +File.separator+ uuid + "/result";
        String cmd = "perl /home/soft/D2/D2_process.pl "+ name + " "+ o;
        return getResult(uuid,uname,o,cmd,"GSEA");
    }

    @Override
    public Result cthree(HttpServletRequest request, String tis, MultipartFile file, String snp) {
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String uname = request.getHeader("uname");
        String name = getFile(uname, uuid, file, snp);
        String o  = userPath + uname +File.separator+ uuid + "/result";
        String cmd = "Rscript /home/soft/C3/C3_r_script.r -i "+ name + " -t "+ tis + " -o "+ o;
        return getResult(uuid,uname,o,cmd,"GO And KEGG");
    }

    @Override
    public Result btwo(HttpServletRequest request, MultipartFile file, String snp) {
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String uname = request.getHeader("uname");
        String name = getFile(uname, uuid, file, snp);
        String o  = userPath + uname +File.separator+ uuid + "/result";
        String cmd = "sh /home/soft/fastenloc_1/B2_process.sh "+ name + " "+ o;
        return getResults(uuid,uname,o,cmd,"Fastenloc");
    }

    @Override
    public Result afour(HttpServletRequest request, MultipartFile file, String snp) {
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String uname = request.getHeader("uname");
        String name = getFile(uname, uuid, file, snp);
        String o  = userPath + uname +File.separator+ uuid + "/result";
        String cmd = "sh /home/soft/PAINTOR_V3.0/make_input/A4.process_new.sh "+ name + " "+ o;
        return getResults(uuid,uname,o,cmd,"PAINTOR");
    }

    @Override
    public Result etwo(HttpServletRequest request, String tis, MultipartFile file, String snp) {
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String uname = request.getHeader("uname");
        String name = getFile(uname, uuid, file, snp);
        if (!name.endsWith(".gz")) {
            try {
                Utils.run("gzip " + name);
//                String ehco_$PATH = Utils.run("ehco $PATH");
//                System.out.println(ehco_$PATH);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String o  = userPath + uname +File.separator+ uuid + "/result";
        String cmd ="sh /home/soft/new_function/script/process.sh "+name+".gz "+ o+" "+ tis;
//        if ("All_Tissue".equals(tis)){
//            cmd = "sh /home/soft/new_function/script/process_all.sh "+name+" "+ o;
//        }
        return getResult(uuid,uname,o,cmd,"MetaXcan");
    }

    @Override
    public Result ethree(HttpServletRequest request, MultipartFile file, String snp) {
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String uname = request.getHeader("uname");
        String name = getFile(uname, uuid, file, snp);
        String o  = userPath + uname +File.separator+ uuid + "/result";
        File p = new File(o+"/permu");
        boolean mkdir = p.mkdirs();
        System.out.println(mkdir);
        String cmd = "Rscript /home/soft/E3/GSEA_copy.R "+ name + " "+ o;
        return getResults(uuid,uname,o,cmd,"TWAS");
    }

    @Override
    public Result netwo(HttpServletRequest request, String tis, MultipartFile file, String snp) {
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String uname = request.getHeader("uname");
        String name = getFile(uname, uuid, file, snp);
        String o  = userPath + uname +File.separator+ uuid + "/result";
        String cmd ;
        if ("GeneID".equals(tis)) {
            cmd = "perl /home/soft/E2/E2_geneID_match.pl " + name + " " + o;
        }else {
            cmd = "perl /home/soft/E2/E2_genesymbol_match.pl " + name + " " + o;
        }
        return getResult(uuid, uname, o, cmd,"CTD Analysis");
    }

//    private Result getRun(String uuid, String uname, String o, String name, String cmd) {
//        File file = new File(o);
//        if (!file.exists()){
//            file.mkdirs();
//        }
//        UserRun userRun = new UserRun(uname, 0, DateUtil.now(), o, uuid);
//        userRun.setCmd(cmd);
//        userRun.setIsFile(0);
//        userRun.setTitle("PAINTOR");
//        runRepository.save(userRun);
//        asyncService.runBfour(cmd, uname, uuid,name,o);
//        return Result.succeed(uuid,ResultMsg.SUCCESS);
//    }

    private Result getResult(String uuid, String uname, String o, String cmd,String title) {
        File file = new File(o);
        if (!file.exists()){
            file.mkdirs();
        }
        UserRun userRun = new UserRun(uname, 0, DateUtil.now(), o, uuid);
        userRun.setCmd(cmd);
        userRun.setIsFile(0);
        userRun.setTitle(title);
        runRepository.save(userRun);
        asyncService.runCmd(cmd, uname, uuid);
        return Result.succeed(uuid,ResultMsg.SUCCESS);
    }

    private Result getResults(String uuid, String uname, String o, String cmd,String title) {
        File file = new File(o);
        if (!file.exists()){
            file.mkdirs();
        }
        UserRun userRun = new UserRun(uname, 0, DateUtil.now(), o, uuid);
        userRun.setCmd(cmd);
        userRun.setIsFile(0);
        userRun.setTitle(title);
        runRepository.save(userRun);
        asyncService.runCmds(cmd, uname, uuid,o);
        return Result.succeed(uuid,ResultMsg.SUCCESS);
    }


    private String getFile(String uname,String uuid,MultipartFile file,String snp){
        File dir = new File(userPath + uname +File.separator+ uuid + "/upload/");
        if (!dir.exists()){
            dir.mkdirs();
        }
        String name;
        if (file == null){
            File fi = new File( userPath + uname +File.separator+ uuid + "/upload/"+uuid);
            try {
                FileWriter fw = new FileWriter(fi);
                fw.write(snp);
                fw.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            name = userPath + uname +File.separator+ uuid + "/upload/"+uuid;
        }else {
            name = userPath + uname +File.separator+ uuid + "/upload/"+file.getOriginalFilename();
            try {
                file.transferTo(new File(name));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return name;
    }

}
