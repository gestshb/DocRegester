package com.spring.util;

import com.spring.config.ConfigBeansDerby;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringUtil {

    private static ApplicationContext applicationContext;

//    static {
//        try {
//
//            applicationContext = new AnnotationConfigApplicationContext(ConfigBeans.class);
//        } catch (Throwable ex) {
//
//            System.err.println("Initial SessionFactory creation failed." + ex);
//            throw new ExceptionInInitializerError(ex);
//        }
//    }
    public static ApplicationContext getApplicationContext() {
        if (applicationContext != null) {

            return applicationContext;

        } else {
            try {
                return applicationContext = new AnnotationConfigApplicationContext(ConfigBeansDerby.class);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
