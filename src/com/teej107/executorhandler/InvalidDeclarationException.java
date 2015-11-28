package com.teej107.executorhandler;

import java.lang.reflect.Method;

/**
 * @author teej107
 * @since Oct 25, 2015
 */
public class InvalidDeclarationException extends Exception
{
	public InvalidDeclarationException(Method method, String s)
	{
		super("Error at method \'" + method.getName() + "\' in " + method.getDeclaringClass().getName() + "\n" + s);
	}
}
