package com.mugu.gene.controller;

import cn.hutool.extra.mail.MailUtil;
import com.mugu.gene.jpa.LoginRepository;
import com.mugu.gene.model.User;
import com.mugu.gene.util.ResponseResult;
import com.mugu.gene.util.Utils;
import com.mugu.gene.web.Result;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static jdk.nashorn.tools.Shell.SUCCESS;

/**
 * @Author : zp
 * @Description :
 * @Date : 2019/11/1
 */
@Controller
@RequestMapping("")
public class IndexController {

    private static final Logger log = LoggerFactory.getLogger(IndexController.class) ;

    @Autowired
    LoginRepository loginRepository;

    @Value("${com.mugu.userdata}")
    private String userPath;

    @RequestMapping("/")
    public String index() {
        return "forward:index.html";
    }

    @ResponseBody
    @PostMapping("/reg")
    public Result register(String email, String password) {
        User user = loginRepository.findByEmail(email);
        if (!Objects.isNull(user)) {
            return Result.failed("用户已存在");
        }
        user = new User();
        user.setEmail(email.trim());
        user.setPassword(password.trim());
        loginRepository.save(user);
        return Result.succeed("注册成功");
    }


    @PostMapping("/login")
    @ResponseBody
    public int login(String email, String password, HttpSession session) {
        User user = loginRepository.findByEmail(email);
        if (password.trim().equals(user.getPassword())) {
            session.setAttribute("user", user);
            return 1;
        }
        return 0;
    }

    @RequestMapping("/download")
    public void downloadAll(String email, String id, HttpServletRequest request, HttpServletResponse response) {
        log.info("downloadAll file");
        if (StringUtils.isEmpty(email)) {
            email = request.getHeader("uname");
        } else {
            return;
        }
        String path = userPath + email + File.separator + id + "/result";
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        try {
            response.setContentType("application/force-download");
            response.setHeader("content-disposition", "attachment;fileName=" + URLEncoder.encode(file.getName(), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
        } catch (IOException e) {
            log.error("download err");
        }
        Utils.toZip(file, outputStream, true);
    }

    @RequestMapping("/sendEmail")
    @ResponseBody
    public void sendEmail(String email){
        User user = loginRepository.findByEmail(email);
        StringBuilder stringBuilder = new StringBuilder();
        String code = Utils.smsCode();
        user.setCode(code);
        stringBuilder.append("您正在修改密码，您的验证码是:").append(code).append(",非本人操作请忽略；");
        MailUtil.send(email, "GWAS修改密码", stringBuilder.toString(), false);
        loginRepository.save(user);
    }

    @PostMapping("/resetPwd")
    @ResponseBody
    public Result resetPwd(String email,String code,String pwd){
        User user = loginRepository.findByEmail(email);
        if (code!=null && code.equals(user.getCode())){
            user.setPassword(pwd);
            loginRepository.save(user);
            return Result.succeed("success");
        }
        return Result.failed("error");
    }
}
