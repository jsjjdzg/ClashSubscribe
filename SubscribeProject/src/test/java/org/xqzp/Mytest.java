package org.xqzp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import org.xqzp.entity.User;
import org.xqzp.entity.V2Proxy;
import org.xqzp.entity.yamlvo.Proxy;
import org.xqzp.service.ServerService;
import org.xqzp.service.UserService;


import org.xqzp.utils.JwtUtils;
import org.xqzp.service.UserServerService;
import org.yaml.snakeyaml.Yaml;


import java.io.*;


import java.nio.charset.StandardCharsets;
import java.util.*;


@SpringBootTest
@Slf4j
public class Mytest {
    @Autowired
    UserServerService userServerService;

    @Autowired
    UserService userService;

    @Autowired
    ServerService serverService;

    @Test
    public void readYaml() throws IOException {
        ClassPathResource classPathResource=new ClassPathResource("raw.yaml");
        InputStream inputStream = classPathResource.getInputStream();
        LinkedHashMap<String,Object> yamlmap = new LinkedHashMap<>();
        Yaml yaml = new Yaml();
        yamlmap = yaml.loadAs(inputStream,LinkedHashMap.class);

    }

    @Test
    void createToken() throws IOException {
        List<User> userList = userService.list();
        for(User user:userList){
            String jwtToken = JwtUtils.getJwtToken(user.getUuid());
            user.setToken(jwtToken);
            userService.updateById(user);
        }
    }
}
