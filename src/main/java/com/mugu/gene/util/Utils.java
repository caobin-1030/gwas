package com.mugu.gene.util;

import cn.hutool.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Author : zp
 * @Description :
 * @Date : 2020/5/13
 */
public class Utils {

    private static final String hbaseUrl = "https://hb.flatironinstitute.org/api/netwas/";

    public static String run(String command) throws Exception {
        StringBuilder sb = new StringBuilder();
        Process process = Runtime.getRuntime().exec(command);
        final InputStream is1 = process.getInputStream();
        final InputStream is2 = process.getErrorStream();
        new Thread(() -> {
            BufferedReader br1 = new BufferedReader(new InputStreamReader(is1));
            try {
                String line1 = null;
                while ((line1 = br1.readLine()) != null) {
                    if (line1 != null) {
                        sb.append(line1).append("\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is1.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(() -> {
            BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
            try {
                String line2 = null;
                while ((line2 = br2.readLine()) != null) {
                    if (line2 != null) {
                        sb.append(line2).append("\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        process.waitFor();
//        process.destroy();
        return sb.toString();
    }

    public static String runs(String command,String o) throws Exception {
        StringBuilder sb = new StringBuilder();
        String[] envp = new String[]{
                "PATH=/usr/lib64/gsl/bin:${PATH}",
                "C_INCLUDE_PATH=/usr/lib64/gsl/include:${C_INCLUDE_PATH}",
                "LD_LIBRARY_PATH=/usr/lib64/gsl/lib:${LD_LIBRARY_PATH}"
        };
        Process process = Runtime.getRuntime().exec(command,envp , new File(o));
        final InputStream is1 = process.getInputStream();
        final InputStream is2 = process.getErrorStream();
        new Thread(() -> {
            BufferedReader br1 = new BufferedReader(new InputStreamReader(is1));
            try {
                String line1 = null;
                while ((line1 = br1.readLine()) != null) {
                    if (line1 != null) {
                        sb.append(line1).append("\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is1.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(() -> {
            BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
            try {
                String line2 = null;
                while ((line2 = br2.readLine()) != null) {
                    if (line2 != null) {
                        sb.append(line2).append("\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        process.waitFor();
//        process.destroy();
        return sb.toString();
    }
    public static void toZip(File sourceFile, OutputStream out, boolean keepDirStructure)
            throws RuntimeException {
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            compress(sourceFile, zos, sourceFile.getName(), keepDirStructure);
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void compress(File sourceFile, ZipOutputStream zos, String name,
                                boolean keepDirStructure) throws Exception {
        byte[] buf = new byte[1024];
        if (sourceFile.isFile()) {
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if (keepDirStructure) {
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    if (keepDirStructure) {
                        compress(file, zos, name + "/" + file.getName(), keepDirStructure);
                    } else {
                        compress(file, zos, file.getName(), keepDirStructure);
                    }
                }
            }
        }
    }

    public static String smsCode() {
        String random = new Random().nextInt(1000000) + "";
        if (random.length() != 6) {
            return smsCode();
        } else {
            return random;
        }
    }

    public static String runHumanBase(String textFileName,String tis,String p){
        CloseableHttpClient client = HttpClients.createDefault();
        File file = new File(textFileName);
        HttpPost post = new HttpPost(hbaseUrl);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addBinaryBody("gwas_file", file, ContentType.DEFAULT_BINARY, textFileName);
        builder.addTextBody("gwas_format", "vegas", ContentType.DEFAULT_BINARY);
        builder.addTextBody("tissue", tis, ContentType.DEFAULT_BINARY);
        builder.addTextBody("p_value", p, ContentType.DEFAULT_BINARY);
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
        JSONObject js = new JSONObject(result);
        return js.getStr("id");
    }

    public static String getResult(String id){
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(hbaseUrl+id);
        try {
            CloseableHttpResponse execute = client.execute(httpGet);
            String entity =  EntityUtils.toString(execute.getEntity());
            JSONObject js = new JSONObject(entity);
            String status = js.getStr("status");
            if ("completed".equals(status)){
                js.getStr("results_file");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static String getResults(String url){
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        try {
            CloseableHttpResponse execute = client.execute(httpGet);
            String entity =  EntityUtils.toString(execute.getEntity());
            return entity;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
