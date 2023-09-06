package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class IdProvider implements InitializingBean, DisposableBean, BeanPostProcessor {

    Logger logger = Logger.getLogger(IdProvider.class);

    public String provideId(Book book) {
        return this.hashCode() + "_" + book.hashCode();
    }

    public void initIdProvider(){
        logger.info("provider INIT");
    }

    public void destroyIdProvider(){
        logger.info("provider DESTROY");
    }

    public void defaultInit(){
        logger.info("default INIT in provider");
    }

    public void defaultDestroy(){
        logger.info("default DESTROY in provider");
    }

    @Override
    public void destroy() throws Exception {
        logger.info("provider destroy");

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("provider afterPropertiesSet");

    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        logger.info("postProcessBeforeInitialization invoke by bean " + beanName);
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        logger.info("postProcessAfterInitialization invoke by bean " + beanName);
        return null;    }

    @PostConstruct
    public void postConstructIdProvider(){
        logger.info("postConstruct annotated method called");

    }

    @PreDestroy
    public void preDestroyIdProvider(){
        logger.info("preDestroy annotated method called");

    }
}
