package com.mugu.gene.common;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import okhttp3.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Author : zp
 * @Description :
 * @Date : 2019/10/12
 */
public class OkHttpUtil {


    public static String getpost(String url, JSONObject jsonObject) throws Exception {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        Request request = new Request.Builder().post(requestBody).url(url).build();
        for (int i = 0; i <3 ; i++) {
            Response response = client.newCall(request).execute();
            if (response.body() != null ){
                return response.body().string();
            }
            Thread.sleep(100);
        }
        return null;
    }
    public static String getposts(String uri, JSONObject jsonObject) throws Exception  {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        System.out.println(jsonObject.toString());
        RequestBody body = RequestBody.create(mediaType,JSONUtil.toJsonStr(jsonObject));
        Request request = new Request.Builder()
                .url(uri)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        for (int i = 0; i <3 ; i++) {
            Response response = client.newCall(request).execute();
            if (response.body() != null ){
                return response.body().string();
            }
            Thread.sleep(100);
        }
        return null;
    }

    public static void main(String[] args) throws IOException {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        String co = "{\"entrezgene_id_sets\":[[\"entrezgene:79844\",\"entrezgene:65980\",\"entrezgene:100506688\",\"entrezgene:10723\",\"entrezgene:100616479\",\"entrezgene:348932\",\"entrezgene:7015\",\"entrezgene:100616235\",\"entrezgene:81037\"],[\"entrezgene:23480\",\"entrezgene:1956\",\"entrezgene:55915\"],[\"entrezgene:728724\"],[\"entrezgene:554202\",\"entrezgene:407035\",\"entrezgene:51198\",\"entrezgene:1029\",\"entrezgene:100048912\",\"entrezgene:1030\",\"entrezgene:63951\"],[\"entrezgene:100526771\",\"entrezgene:6330\",\"entrezgene:6327\",\"entrezgene:120425\",\"entrezgene:196264\",\"entrezgene:10205\",\"entrezgene:915\",\"entrezgene:4297\",\"entrezgene:143941\",\"entrezgene:84866\",\"entrezgene:56912\",\"entrezgene:372\",\"entrezgene:23187\",\"entrezgene:11181\",\"entrezgene:1656\",\"entrezgene:643\",\"entrezgene:100616376\",\"entrezgene:7379\",\"entrezgene:283150\",\"entrezgene:338657\",\"entrezgene:51399\",\"entrezgene:100500840\",\"entrezgene:55823\",\"entrezgene:3145\",\"entrezgene:9854\",\"entrezgene:25988\",\"entrezgene:64137\",\"entrezgene:79671\",\"entrezgene:79849\",\"entrezgene:867\"],[\"entrezgene:768097\",\"entrezgene:54915\",\"entrezgene:128414\",\"entrezgene:1137\",\"entrezgene:3785\",\"entrezgene:1917\",\"entrezgene:79144\",\"entrezgene:5753\",\"entrezgene:6725\",\"entrezgene:79025\",\"entrezgene:85441\",\"entrezgene:26205\",\"entrezgene:50861\",\"entrezgene:100533107\",\"entrezgene:8771\",\"entrezgene:10139\",\"entrezgene:84619\",\"entrezgene:54923\",\"entrezgene:56731\",\"entrezgene:140685\",\"entrezgene:140701\",\"entrezgene:7165\",\"entrezgene:80331\",\"entrezgene:100126329\",\"entrezgene:100126352\",\"entrezgene:100126330\",\"entrezgene:100126339\",\"entrezgene:100113386\",\"entrezgene:24148\",\"entrezgene:284739\",\"entrezgene:6919\",\"entrezgene:4987\",\"entrezgene:4661\",\"entrezgene:55251\",\"entrezgene:140849\"]]}";
//        RequestBody body = RequestBody.create(mediaType, "{\"entrezgene_id_sets\":[[\"entrezgene:79844\",\"entrezgene:65980\",\"entrezgene:100506688\",\"entrezgene:10723\",\"entrezgene:100616479\",\"entrezgene:348932\",\"entrezgene:7015\",\"entrezgene:100616235\",\"entrezgene:81037\"],[\"entrezgene:23480\",\"entrezgene:1956\",\"entrezgene:55915\"],[\"entrezgene:728724\"],[\"entrezgene:554202\",\"entrezgene:407035\",\"entrezgene:51198\",\"entrezgene:1029\",\"entrezgene:100048912\",\"entrezgene:1030\",\"entrezgene:63951\"],[\"entrezgene:100526771\",\"entrezgene:6330\",\"entrezgene:6327\",\"entrezgene:120425\",\"entrezgene:196264\",\"entrezgene:10205\",\"entrezgene:915\",\"entrezgene:4297\",\"entrezgene:143941\",\"entrezgene:84866\",\"entrezgene:56912\",\"entrezgene:372\",\"entrezgene:23187\",\"entrezgene:11181\",\"entrezgene:1656\",\"entrezgene:643\",\"entrezgene:100616376\",\"entrezgene:7379\",\"entrezgene:283150\",\"entrezgene:338657\",\"entrezgene:51399\",\"entrezgene:100500840\",\"entrezgene:55823\",\"entrezgene:3145\",\"entrezgene:9854\",\"entrezgene:25988\",\"entrezgene:64137\",\"entrezgene:79671\",\"entrezgene:79849\",\"entrezgene:867\"],[\"entrezgene:768097\",\"entrezgene:54915\",\"entrezgene:128414\",\"entrezgene:1137\",\"entrezgene:3785\",\"entrezgene:1917\",\"entrezgene:79144\",\"entrezgene:5753\",\"entrezgene:6725\",\"entrezgene:79025\",\"entrezgene:85441\",\"entrezgene:26205\",\"entrezgene:50861\",\"entrezgene:51750\",\"entrezgene:8771\",\"entrezgene:10139\",\"entrezgene:84619\",\"entrezgene:54923\",\"entrezgene:56731\",\"entrezgene:140685\",\"entrezgene:140701\",\"entrezgene:7165\",\"entrezgene:80331\",\"entrezgene:100126329\",\"entrezgene:100126352\",\"entrezgene:100126330\",\"entrezgene:100126339\",\"entrezgene:100113386\",\"entrezgene:24148\",\"entrezgene:284739\",\"entrezgene:6919\",\"entrezgene:4987\",\"entrezgene:4661\",\"entrezgene:55251\",\"entrezgene:140849\"]]}");
        RequestBody body = RequestBody.create(mediaType, co);
        Request request = new Request.Builder()
                .url("http://dalai.mshri.on.ca/prixfixe/cgi/GWASnet/0.7/PrixFixeGA")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
       }

}
