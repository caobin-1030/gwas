package com.mugu.gene.service;


/**
 * @Author : zp
 * @Description :
 * @Date : 2019/11/4
 */
public interface AsyncService {
    void generateResult(String file, String fileout,String uuid);

    void generateSnpResult(String file, String fileout, String organization);

    void runCmd(String cmd,String uname,String uuid);

    void runHbase(String id,String tis,String p);

    void runCmds(String cmd,String uname,String uuid,String o);

    void netWAS(String uuid, String tis, String sup, String snps, String cmd,String o);

    void runBfour(String cmd, String uname, String uuid, String name, String o);
}
