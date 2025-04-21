package com.example.spba.utils;

import com.example.spba.config.ExpressConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Slf4j
public class express {

    /*
     * 通过快递单号查询快递信息
     * @param no 快递单号
     * @return 返回json格式的快递信息
     */

    private static final ExpressConfig expressConfig = new ExpressConfig();


    public static Object getexpressinfo(String no) {
        String host = "https://wuliu.market.alicloudapi.com";// 【1】请求地址 支持http 和 https 及 WEBSOCKET
        String path = "/kdi";  // 【2】后缀
//        String appcode = "你自己的AppCode"; // 【3】开通服务后 买家中心-查看AppCode
//        String no = "780098068058";// 【4】请求参数，详见文档描述
        String appcode = expressConfig.getAppcode(); // 【3】开通服务后 买家中心-查看AppCode
        System.out.println("appcode:" + appcode);
        String type = "zto"; //  【4】请求参数，不知道可不填 95%能自动识别
        String urlSend = host + path + "?no=" + no + "&type=" + type;  // 【5】拼接请求链接
        try {
            URL url = new URL(urlSend);
            HttpURLConnection httpURLCon = (HttpURLConnection) url.openConnection();
            httpURLCon.setRequestProperty("Authorization", "APPCODE " + appcode);// 格式Authorization:APPCODE (中间是英文空格)
            int httpCode = httpURLCon.getResponseCode();
            if (httpCode == 200) {
                String json = read(httpURLCon.getInputStream());
                System.out.println("正常请求计费(其他均不计费)");
                System.out.println("获取返回的json:");
                System.out.print(json);
                return json;
            } else {
                Map<String, List<String>> map = httpURLCon.getHeaderFields();
                String error = map.get("X-Ca-Error-Message").get(0);
                if (httpCode == 400 && error.equals("Invalid AppCode `not exists`")) {
                    System.out.println("AppCode错误 ");
                    return "AppCode错误 ";
                } else if (httpCode == 400 && error.equals("Invalid Url")) {
                    System.out.println("请求的 Method、Path 或者环境错误");
                    return "请求的 Method、Path 或者环境错误";
                } else if (httpCode == 400 && error.equals("Invalid Param Location")) {
                    System.out.println("参数错误");
                    return "参数错误";
                } else if (httpCode == 403 && error.equals("Unauthorized")) {
                    System.out.println("服务未被授权（或URL和Path不正确）");
                    return "服务未被授权（或URL和Path不正确）";
                } else if (httpCode == 403 && error.equals("Quota Exhausted")) {
                    System.out.println("套餐包次数用完 ");
                    return "套餐包次数用完";
                } else if (httpCode == 403 && error.equals("Api Market Subscription quota exhausted")) {
                    System.out.println("套餐包次数用完，请续购套餐");
                    return "套餐包次数用完，请续购套餐";
                } else {
                    System.out.println("参数名错误 或 其他错误");
                    System.out.println(error);
                    return "参数名错误 或 其他错误";

                }
            }

        } catch (MalformedURLException e) {
            System.out.println("URL格式错误");
            return "URL格式错误";
        } catch (UnknownHostException e) {
            System.out.println("URL地址错误");
            return "URL地址错误";
        } catch (Exception e) {
            // 打开注释查看详细报错异常信息
            e.printStackTrace();
            System.out.println("系统异常");
        }
        return "系统异常";
    }

    /*
     * 读取返回结果
     */
    private static String read(InputStream is) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        while ((line = br.readLine()) != null) {
            line = new String(line.getBytes(), StandardCharsets.UTF_8);
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }
}