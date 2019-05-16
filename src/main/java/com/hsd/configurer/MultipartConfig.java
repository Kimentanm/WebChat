package com.hsd.configurer;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;
import java.io.File;

/**
 * @Author: Jinmeng Tang
 * @Date: Created in 2018/8/2 上午10:26
 */
@Configuration
public class MultipartConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //文件最大
        factory.setMaxFileSize("10MB"); //KB,MB
        /// 设置总上传数据总大小
        factory.setMaxRequestSize("10MB");
        File file = new File("/tmp/hsd-server");
        if (!file.exists()) {
            file.mkdir();
        }
        factory.setLocation("/tmp/hsd-server");
        return factory.createMultipartConfig();
    }
}
