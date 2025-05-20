package com.example.spba.interceptor;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import com.example.spba.utils.AsyncTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class SpbaInterceptor implements HandlerInterceptor
{

    @Autowired
    private AsyncTask asyncTask;

    /**
     * 拦截请求，访问Controller之前使用的方法
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // System.out.println("拦截请求，访问Controller之前使用的方法");
        return true;
    }

    /**
     * 访问到Controller之后，渲染视图之前
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        // 忽略 OPTIONS 请求（CORS 预检请求）
        if (request.getMethod().equals("OPTIONS")) {
            return;
        }
         System.out.println("访问到Controller之后，渲染视图之前");
        if (request.getRequestURI().equals("/spba-api/user/login") || request.getRequestURI().equals("/spba-api/user/logout")) {
            return;
        }
        // 判断请求方法并获取参数
        Map<String, String[]> parameterMap = request.getParameterMap();
        String bodyParams = null;

        // 如果是 POST 或 PUT 请求，尝试读取请求体中的参数
        if (request.getMethod().equals("POST") || request.getMethod().equals("PUT")) {
            // 获取请求体内容（处理 JSON 或表单数据）
            try {
                // 如果请求体是 JSON 数据，尝试读取并转换为字符串
                if ("application/json".equals(request.getContentType())) {
                    bodyParams = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                }
                // 如果是表单数据，也可以通过 request.getParameterMap() 获取
                else {
                    bodyParams = JSONUtil.parse(parameterMap).toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 保存日志
        if (!request.getMethod().equals("GET")) {
            System.out.println("url: " + request.getRequestURI());
            System.out.println("method: " + request.getMethod());

            // 请求参数（包括 URL 参数和请求体中的参数）
            String params = bodyParams != null ? bodyParams : JSONUtil.parse(parameterMap).toString();
            asyncTask.saveOperateLog(StpUtil.getLoginIdAsLong(), request.getRequestURI(), request.getMethod(), params, ServletUtil.getClientIP(request));
        }
    }

    /**
     * 访问到Controller之后，渲染视图之后
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // System.out.println("访问到Controller之后，渲染视图之后");
    }
}