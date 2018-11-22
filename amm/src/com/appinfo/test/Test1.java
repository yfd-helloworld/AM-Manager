package com.appinfo.test;

import com.appinfo.pojo.BackendUser;
import com.appinfo.service.backenduser.BackendUserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author yfd
 * @email yuanfandin@163.com
 * @creation time By 2018/11/22 14:48
 */
public class Test1 {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        BackendUserService backendUserService = (BackendUserService) context.getBean("backendUserService");
        try {
            BackendUser backendUser = backendUserService.login("admin", "123456");
            System.out.println(backendUser.getUserName());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
