# 基础镜像使用java
FROM java:8
# 作者
MAINTAINER random
# VOLUME 指定临时文件目录为/tmp，在主机/var/lib/docker目录下创建了一个临时文件并链接到容器的/tmp
VOLUME /tmp
# 将jar包添加到容器中并更名为zzyy_docker.jar
ADD SubscribeProject-prod-1.0-SNAPSHOT.jar SubscribeProject.jar
# 运行jar包
RUN bash -c 'touch /SubscribeProject.jar'
ENTRYPOINT ["java","-jar","/SubscribeProject.jar","--spring.config.location=/config/application.properties,/config/application-prod.yaml"]
#暴露8001端口和微服务的端口相对应
EXPOSE 8080
