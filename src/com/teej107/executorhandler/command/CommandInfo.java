package com.teej107.executorhandler.command;

import com.teej107.executorhandler.*;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

import static com.teej107.executorhandler.Executable.OBJECT;
import static com.teej107.executorhandler.Executable.VAR_ARG;

/**
 * @author teej107
 * @since Oct 25, 2015
 */
public class CommandInfo
{
	private ICommand command;
	private Method method;
	private Executable annotation;
	private String[] commandArgs;
	private String commandString;
	private Class<? extends CommandSender> commandSender;
	private List<Class<?>> parameterList;
	private List<String> definedArguments;
	private int varArgsEndOffset, varArgsStart;

	public CommandInfo(ICommand command, Method method, Executable annotation) throws InvalidDeclarationException
	{
		this.command = command;
		this.method = method;
		this.annotation = annotation;
		if (method.getReturnType() != boolean.class)
			throw new InvalidDeclarationException(method, "Method doesn't return a boolean");

		String[] fullCommand = annotation.command().trim().split(Pattern.quote(" "));
		commandString = fullCommand[0];
		commandArgs = Arrays.copyOfRange(fullCommand, 1, fullCommand.length);
		Class<?>[] parameters = method.getParameterTypes();
		if (parameters.length == 0)
			throw new InvalidDeclarationException(method, "Empty method parameters are not permitted");
		if (!CommandSender.class.isAssignableFrom(parameters[0]))
			throw new InvalidDeclarationException(method,
					"First method parameter is not an instance of " + CommandSender.class.getName());

		commandSender = (Class<? extends CommandSender>) parameters[0];
		parameterList = new ArrayList<>();
		definedArguments = new ArrayList<>();
		varArgsStart = -1;
		varArgsEndOffset = -1;
		for (int i = 0; i < commandArgs.length; i++)
		{
			boolean varArg = commandArgs[i].equals(VAR_ARG);
			if (varArg)
			{
				if(hasVarArgs())
					throw new InvalidDeclarationException(method, "Multiple varargs are not supported");
				varArgsStart = i;
			}
			if (commandArgs[i].equals(OBJECT) || varArg)
			{
				parameterList.add(parameters[parameterList.size() + 1]);
				if (hasVarArgs())
				{
					varArgsEndOffset++;
				}
			}
			else
			{
				definedArguments.add(commandArgs[i]);
			}
		}
		if (parameters.length - 1 != parameterList.size())
			throw new InvalidDeclarationException(method, "The number of parameters do not match");
	}

	public boolean call(Object... objects)
	{
		try
		{
			return (boolean) method.invoke(command, objects);
		}
		catch (IllegalAccessException | InvocationTargetException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public String getCommandName()
	{
		return commandString;
	}

	public String[] getCommandArgs()
	{
		return commandArgs;
	}

	public Class<? extends CommandSender> getCommandSender()
	{
		return commandSender;
	}

	public List<Class<?>> getParameters()
	{
		return parameterList;
	}

	public String getPermission()
	{
		String p = annotation.permission();
		return p.trim().isEmpty() ? null : p;
	}

	public String getUsage()
	{
		String usage = annotation.usage();
		return usage.trim().isEmpty() ? null : ChatColor.translateAlternateColorCodes('&', usage);
	}

	public String getNoPermissionMessage()
	{
		return ChatColor.translateAlternateColorCodes('&', annotation.noPermissionMessage());
	}

	public String getWrongSenderTypeMessage()
	{
		return ChatColor.translateAlternateColorCodes('&', annotation.wrongSenderTypeMessage());
	}

	/*public String getDescription()
	{
		String description = annotation.description();
		return description.trim().isEmpty() ? null : ChatColor.translateAlternateColorCodes('&', description);
	}*/

	public boolean hasVarArgs()
	{
		return varArgsStart != -1;
	}

	public int getVarArgsStart()
	{
		return varArgsStart;
	}

	public int getVarArgsEndOffset()
	{
		return varArgsEndOffset;
	}

	public List<String> getDefinedArguments()
	{
		return definedArguments;
	}

	@Override
	public String toString()
	{
		return commandString + " " + StringUtils.join(commandArgs, ' ');
	}
}
