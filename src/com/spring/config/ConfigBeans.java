/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author pc
 */
@Configuration
@EnableTransactionManagement
@ComponentScan("com.spring")
public class ConfigBeans {

    @Bean
    public LocalSessionFactoryBean sessionFactory() throws IOException, UnknownHostException, ClassNotFoundException {
        LocalSessionFactoryBean session = new LocalSessionFactoryBean();
        session.setDataSource(restDataSource());
        session.setPackagesToScan(new String[]{"com.spring.*"});
        session.setHibernateProperties(hibernateProperties());
        return session;
    }

    @Bean
    public DataSource restDataSource() throws IOException {

        BasicDataSource datasource = new BasicDataSource();

        //client=================================================
        String host = null;
        Properties pro = null;
        Path path = Paths.get(System.getProperty("user.home"), ".setting", "setting.properties");

        try {
            if (Files.exists(path)) {
                BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
                Properties props = new Properties();
                props.load(reader);
                host = props.getProperty("host");
            }

        } catch (Exception e) {
            Logger.getLogger(ConfigBeans.class.getName()).log(Level.SEVERE, null, e);
        }

        Socket socket = null;
        ObjectInputStream in = null;

        try {
            int port = 2319;
            InetAddress address = InetAddress.getByName(host);
            socket = new Socket(address, port);
            in = new ObjectInputStream(socket.getInputStream());
            pro = (Properties) in.readObject();

        } catch (UnknownHostException ex) {
            Logger.getLogger(ConfigBeans.class.getName()).log(Level.SEVERE, null, ex);

        } catch (ClassNotFoundException | IOException ex) {
            Logger.getLogger(ConfigBeans.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
                socket.close();
            } catch (Exception e) {
            }
        }
        datasource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        datasource.setUrl("jdbc:sqlserver://" +
                pro.getProperty("host") +
                ":" + pro.getProperty("port") +
                ";databaseName=" + 
                pro.getProperty("db"));
        datasource.setUsername(pro.getProperty("user"));
        datasource.setPassword(pro.getProperty("password"));

//        //=======================================================
//        datasource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//        datasource.setUrl("jdbc:sqlserver://192.168.56.200:1433;databaseName=archiver");
//        datasource.setUsername("sa");
//        datasource.setPassword("aaaa");
        return datasource;

    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);
        return txManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    Properties hibernateProperties() {
        return new Properties() {
            {
                setProperty("hibernate.hbm2ddl.auto",
                        "update");
                setProperty("hibernate.dialect", "com.spring.util.SQLServerDialect");
                setProperty("hibernate.globally_quoted_identifiers", "true");
                setProperty("hibernate.show_sql", "true");
                setProperty("hibernate.enable_lazy_load_no_trans", "true");
                setProperty("hibernate.jdbc.batch_size", "50");
                //setProperty("hibernate.current_session_context_class", "thread");

            }
        };
    }
}
