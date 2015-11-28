package com.teej107.executorhandler;

import java.lang.annotation.*;

/**
 * @author teej107
 * @since Nov 07, 2015
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Parser
{
	String OBJECT = "<object>";
	String parseFailedMessage() default "";
}