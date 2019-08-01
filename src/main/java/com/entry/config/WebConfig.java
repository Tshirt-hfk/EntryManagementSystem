package com.entry.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.entry.security.JwtInterceptor;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Value("${file.upload-path}")
    private String filePath;

    @Value("${file.show-url}")
    private String showUrl;

    /**
     * 静态文件资源
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        registry.addResourceHandler(showUrl+"**").addResourceLocations(
                "classpath:"+filePath);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加拦截器
        registry.addInterceptor(new JwtInterceptor()).addPathPatterns("/api/**").excludePathPatterns("/api/tourist/*","/api/entry/*","/resource/**","/error");
        //放掉某些特定不需要校验token的路由
    }
}
