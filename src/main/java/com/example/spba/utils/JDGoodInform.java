package com.example.spba.utils;

import com.example.spba.config.JDconfig;
import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.request.mall.NewWareBaseproductGetRequest;
import com.jd.open.api.sdk.response.mall.NewWareBaseproductGetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JDGoodInform {

    @Autowired
    private JDconfig jdconfig;

    public String getJDGoodInform(String goodId) throws Exception {
        System.out.println(jdconfig.toString());
        JdClient client = new DefaultJdClient("https://api.jd.com/routerjson", "", jdconfig.getAppKey(), jdconfig.getAppSecret());
        NewWareBaseproductGetRequest request = new NewWareBaseproductGetRequest();
        request.setIds(goodId);
        request.setBasefields("name,imagePath");
        NewWareBaseproductGetResponse response = client.execute(request);
        System.out.println(response);
        return response.getMsg();
    }
}
