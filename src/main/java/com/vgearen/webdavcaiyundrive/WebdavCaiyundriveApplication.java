package com.vgearen.webdavcaiyundrive;

import com.vgearen.webdavcaiyundrive.store.CaiyunDriverFileSystemStore;
import net.sf.webdav.WebdavServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.LinkedHashMap;
import java.util.Map;

@EnableScheduling
@SpringBootApplication
public class WebdavCaiyundriveApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebdavCaiyundriveApplication.class, args);
    }

    @Bean
    public ServletRegistrationBean<WebdavServlet> myServlet() {
        ServletRegistrationBean<WebdavServlet> servletRegistrationBean = new ServletRegistrationBean<>(new WebdavServlet(), "/*");
        Map<String, String> inits = new LinkedHashMap<>();
        inits.put("ResourceHandlerImplementation", CaiyunDriverFileSystemStore.class.getName());
        inits.put("rootpath", "./");
        inits.put("storeDebug", "1");
        servletRegistrationBean.setInitParameters(inits);
        servletRegistrationBean.setName("webdavServlet");
        return servletRegistrationBean;
    }

    @Bean
    public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
        ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet);
        registration.addUrlMappings("/update");
        registration.setName("restServlet");
        return registration;
    }
}
