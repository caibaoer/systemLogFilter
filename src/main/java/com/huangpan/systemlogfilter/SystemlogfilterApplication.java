package com.huangpan.systemlogfilter;

import com.huangpan.systemlogfilter.filter.SystemLogFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SystemlogfilterApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemlogfilterApplication.class, args);
    }
    @Bean
    public FilterRegistrationBean getSystemLogFilter(){
        FilterRegistrationBean filterRegistrationBean=new FilterRegistrationBean();
        //这里是add  不是set
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setFilter(new SystemLogFilter());
        filterRegistrationBean.setName("systemLogFilter");
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }


}
