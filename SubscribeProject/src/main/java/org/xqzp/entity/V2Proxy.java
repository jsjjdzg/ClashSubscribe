package org.xqzp.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.xqzp.entity.yamlvo.Proxy;
import java.util.ArrayList;


@NoArgsConstructor
@Data
public class V2Proxy {

    @JsonProperty("v")
    private String v="2";

    @JsonProperty("ps")
    private String name;

    @JsonProperty("add")
    private String server;

    @JsonProperty("port")
    private String port;

    @JsonProperty("id")
    private String uuid;

    @JsonProperty("aid")
    private String aid="0";

    @JsonProperty("scy")
    private String scy="auto";

    @JsonProperty("net")
    private String network;

    @JsonProperty("path")
    private String path;

    @JsonProperty("tls")
    private String tls="";

    @JsonProperty("alpn")
    private String alpn;


    public V2Proxy addProxyInfo(Proxy proxy){
        BeanUtils.copyProperties(proxy,this);
        this.port = proxy.getPort().toString();
        if(proxy.getTls()){
            this.tls="tls";
        }

        String path = proxy.getOpts().get("path");
        if(!StringUtils.isEmpty(path)){
            this.path = path;
        }

        ArrayList<String> alpnArray = proxy.getAlpn();
        if(alpnArray.size()!=0){
            this.alpn = alpnArray.get(0);
        }
        return this;
    }

}
