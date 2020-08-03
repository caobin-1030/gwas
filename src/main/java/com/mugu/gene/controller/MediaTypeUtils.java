package com.mugu.gene.controller;

import org.springframework.http.MediaType;

import javax.servlet.ServletContext;

/**
 * @Author : zp
 * @Description :
 * @Date : 2019/11/4
 */
public class MediaTypeUtils {
    public static MediaType getMediaTypeForFileName(ServletContext servletContext, String fileName) {
        String mineType = servletContext.getMimeType(fileName);
        try {
            return MediaType.parseMediaType(mineType);
        } catch (Exception e) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
