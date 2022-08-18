package org.xqzp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xqzp.entity.V2Proxy;
import org.xqzp.entity.yamlvo.Proxy;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Map;

@Service
public class V2Service {

    @Autowired
    UserServerService userServerService;

    public String createContribute(String uuid) {
        Map proxyInfoMap = userServerService.getProxyInfoMap(uuid);
        ArrayList proxyList = (ArrayList) proxyInfoMap.get("proxyList");

        ObjectMapper objectMapper = new ObjectMapper();
        StringWriter sw=new StringWriter();

        for(Object proxy:proxyList){
            V2Proxy v2Proxy = new V2Proxy().addProxyInfo((Proxy) proxy);
            try {
                //将json对象转换成byte数组,base64只能对原始byte类型的数组进行加密
                byte[] bytes1 = objectMapper.writeValueAsBytes(v2Proxy);
                //将加密形成的字符串,拼接上协议头,再凭借上换行符
                String base64 = Base64.getEncoder().encodeToString(bytes1);
                String proxyStr = "vmess://"+base64+"\n";
                sw.write(proxyStr);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        //对整体再进行一次base64加密
        byte[] bytes2 = sw.toString().getBytes(StandardCharsets.UTF_8);
        String finalstr = Base64.getEncoder().encodeToString(bytes2);
        return finalstr;
    }
}
