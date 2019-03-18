/*
 * Copyright 2005-2013 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package com.chinatour.interceptor;

import com.chinatour.Principal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;

/**
 * Interceptor - 会员权限
 *
 * @author SHOP++ Team
 * @version 3.0
 */
public class MemberInterceptor extends HandlerInterceptorAdapter {

    /**
     * 重定向视图名称前缀
     */
    private static final String REDIRECT_VIEW_NAME_PREFIX = "redirect:";

    /**
     * "重定向URL"参数名称
     */
    private static final String REDIRECT_URL_PARAMETER_NAME = "redirectUrl";

    /**
     * "会员"属性名称
     */
    private static final String MEMBER_ATTRIBUTE_NAME = "member";

    /**
     * 默认登录URL
     */
    private static final String DEFAULT_LOGIN_URL = "/index.jsp";

    /**
     * 登录URL
     */
    private String loginUrl = DEFAULT_LOGIN_URL;

    @Value("${url_escaping_charset}")
    private String urlEscapingCharset;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Principal principal = (Principal) session.getAttribute("peerUser");
        if (principal != null) {
            return true;
        } else {
            String requestType = request.getHeader("X-Requested-With");
            if (requestType != null && requestType.equalsIgnoreCase("XMLHttpRequest")) {
                response.addHeader("loginStatus", "accessDenied");
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return false;
            } else {
                if (request.getMethod().equalsIgnoreCase("GET")) {
                    String redirectUrl = request.getQueryString() != null ? request.getRequestURI() + "?" + request.getQueryString() : request.getRequestURI();
                    response.sendRedirect(request.getContextPath() + loginUrl + "?" + REDIRECT_URL_PARAMETER_NAME + "=" + URLEncoder.encode(redirectUrl, urlEscapingCharset));
                } else {
                    response.sendRedirect(request.getContextPath() + loginUrl);
                }
                return false;
            }
        }
    }


    /**
     * 获取登录URL
     *
     * @return 登录URL
     */
    public String getLoginUrl() {
        return loginUrl;
    }

    /**
     * 设置登录URL
     *
     * @param loginUrl 登录URL
     */
    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

}