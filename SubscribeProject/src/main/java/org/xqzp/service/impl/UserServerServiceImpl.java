package org.xqzp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.xqzp.entity.Server;
import org.xqzp.entity.V2Proxy;
import org.xqzp.entity.yamlvo.Proxy;
import org.xqzp.entity.User;
import org.xqzp.entity.UserServer;
import org.xqzp.exception.ProxyException;
import org.xqzp.mapper.UserServerMapper;
import org.xqzp.service.ServerService;
import org.xqzp.service.UserServerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xqzp.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
@Service
public class UserServerServiceImpl extends ServiceImpl<UserServerMapper, UserServer> implements UserServerService {

    @Autowired
    ServerService serverService;

    @Autowired
    UserService userService;

    public Map getProxyInfoMap(String uuid){
        //查询用户信息
        QueryWrapper<User> userwrapper = new QueryWrapper<>();
        userwrapper.eq("uuid",uuid);
        User user = userService.getOne(userwrapper);
        //根据用户信息查询用户拥有的节点信息

        //1.先获取user和server的对应关系
        QueryWrapper<UserServer> wrapper = new QueryWrapper<>();
        wrapper.eq("uid",user.getId());
        List<UserServer> userServerList = baseMapper.selectList(wrapper);

        ArrayList<Integer> array = new ArrayList<>();
        for(UserServer userServer:userServerList){
            array.add(userServer.getSid());
        }

        if(array.size()==0){
            throw new ProxyException(404,"没有可以使用的结点");
        }
        List<Server> serverList = (List<Server>) serverService.listByIds(array);


        //返回一个节点组
        List proxyList = new ArrayList<Proxy>();

        //返回结点的名称
        ArrayList<String> proxyNameList = new ArrayList<String>();

        //将用户信息和服务器信息封装进节点信息
        for (Server server:serverList){
            Proxy proxy = new Proxy();
            proxy.addServerInfo(server);
            proxy.addUserInfo(user);
            proxyList.add(proxy);
            proxyNameList.add(server.getName());
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("proxyList",proxyList);
        map.put("proxyNameList",proxyNameList);

        return map;
    }



}
