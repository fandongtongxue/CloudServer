package me.fandong.cloudserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CloudserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudserverApplication.class, args);
    }
    /**
     * @date:2018/3/5 14:02
     * @author:hejr
     * @description: 文件上传配置
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //单个文件最大
        factory.setMaxFileSize("25MB");
        /// 设置总上传数据总大小
        factory.setMaxRequestSize("50MB");
        return factory.createMultipartConfig();
    }
}
