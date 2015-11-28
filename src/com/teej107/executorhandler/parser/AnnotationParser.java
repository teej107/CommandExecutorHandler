package com.teej107.executorhandler.parser;

import com.teej107.executorhandler.*;
import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author teej107
 * @since Nov 08, 2015
 */
public class AnnotationParser extends AbstractObjectParser
{
	private ICommand command;
	private Method method;
	private Parser annotation;

	public AnnotationParser(ICommand command, Method method, Parser annotation) throws InvalidDeclarationException
	{
		this.command = command;
		this.method = method;
		this.annotation = annotation;

		if(method.getReturnType() == Void.class)
			throw new InvalidDeclarationException(method, "Method must return an Object");

		Class<?>[] parameters = method.getParameterTypes();
		if (parameters.length != 1)
			throw new InvalidDeclarationException(method, "Only 1 method parameter is permitted. It must be " + String.class.getName());
		if (parameters[0] != String.class)
			throw new InvalidDeclarationException(method,
					"Method parameter is not " + String.class.getName());
	}

	public Class<?> getParserType()
	{
		return method.getReturnType();
	}

	@Override
	public Object parse(String s)
	{
		try
		{
			return method.invoke(command, s);
		}
		catch (IllegalAccessException | InvocationTargetException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void onParseFailed(CommandSender sender, String s)
	{
		if(annotation.parseFailedMessage().trim().isEmpty())
		{
			sender.sendMessage("Unable to parse \'" + s + "\'");
		}
		else
		{
			sender.sendMessage(annotation.parseFailedMessage().replace(Parser.OBJECT, s));
		}
	}
}
