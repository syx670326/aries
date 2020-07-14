package com.chilipotato.springboot.aries.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.chilipotato.springboot.aries.IdWorker;
import com.chilipotato.springboot.aries.IdWorkerConfigBean;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({
        IdWorkerConfigBean.class, IdWorker.class
})
public @interface EnableAries {}
