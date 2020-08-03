//package com.mugu.gene.config;
//
//import com.mugu.gene.model.User;
//import org.springframework.context.annotation.Configuration;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//
//@Configuration
//public class UserFilter implements Filter {
//
//    @Override
//    public void destroy() {
//
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response,
//                         FilterChain chain) throws IOException, ServletException {
//        HttpServletResponse resp = (HttpServletResponse) response;
//        HttpServletRequest req = (HttpServletRequest) request;
//        HttpSession session = req.getSession();
//        User user = (User) session.getAttribute("user");
//        String uri = req.getRequestURI();
//        if (user == null) {
//            //判断用户是否是选择跳到登录界面
//            if (!uri.contains("/run/")) {
//                chain.doFilter(request, response);
//            } else {
//                resp.sendRedirect(req.getContextPath() + "/home.html");
//            }
//        } else {
//            request.setAttribute("user",user);
//            chain.doFilter(request, response);
//        }
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//
//    }
//}