package com.teej107.executorhandler;

import com.teej107.executorhandler.command.*;
import com.teej107.executorhandler.parser.AnnotationParser;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author teej107
 * @since Oct 25, 2015
 */
public class ExecutorHandler implements CommandExecutor
{
	private JavaPlugin plugin;
	private ObjectParserManager objectParserManager;
	private CommandInfoManager commandInfoManager;

	public ExecutorHandler(JavaPlugin plugin, ObjectParserManager objectParserManager)
	{
		this.plugin = plugin;
		commandInfoManager = new CommandInfoManager();
		this.objectParserManager = objectParserManager;
	}

	public ExecutorHandler(JavaPlugin plugin)
	{
		this(plugin, new ObjectParserManager());
	}

	/**
	 * Register Executable and Parser methods inside of an Object implementing ICommand
	 *
	 * @param object The object containing the Executables
	 */
	public void registerCommands(ICommand object)
	{
		for (Method method : object.getClass().getMethods())
		{
			Parser parser = method.getAnnotation(Parser.class);
			if (parser != null)
			{
				try
				{
					AnnotationParser ap = new AnnotationParser(object, method, parser);
					objectParserManager.addParser(ap.getParserType(), ap);
				}
				catch (InvalidDeclarationException e)
				{
					e.printStackTrace();
				}
				continue;
			}

			Executable annotation = method.getAnnotation(Executable.class);
			if (!isValid(annotation))
				continue;

			try
			{
				registerCommand(object, annotation, method);
			}
			catch (InvalidDeclarationException e)
			{
				e.printStackTrace();
			}

		}
	}

	private void registerCommand(ICommand object, Executable executable, Method method)
			throws InvalidDeclarationException
	{
		CommandInfo info = new CommandInfo(object, method, executable);

		PluginCommand pc = plugin.getCommand(info.getCommandName());
		if (pc == null)
			throw new InvalidDeclarationException(method, "The command \'" + info.getCommandName() + "\' is not registered");
		pc.setExecutor(this);
		commandInfoManager.add(info);
	}

	private boolean isValid(Executable annotation)
	{
		return !(annotation == null || annotation.command().trim().isEmpty());
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] strings)
	{
		CommandMatch match = commandInfoManager.get(command.getName(), strings);
		if (match == null)
			return false;

		CommandInfo info = match.getCommandInfo();
		if (!validateCommandSender(commandSender, info))
			return true;

		List<Object> list = new ArrayList<>();
		list.add(commandSender);

		List<String> parsingArgs = match.getParsingArgs();
		List<String> infoArgs = match.getInfoArgs();
		int varArgIndex = infoArgs.indexOf(Executable.VAR_ARG);
		if (varArgIndex == -1)
		{
			for (int i = 0; i < parsingArgs.size(); i++)
			{
				Object o = parseObject(info.getParameters().get(i), info, commandSender, label, parsingArgs.get(i));
				if (o == null)
					return true;
				list.add(o);
			}
		}
		else
		{
			for (int i = 0; i < varArgIndex; i++)
			{
				Object o = parseObject(info.getParameters().get(list.size() - 1), info, commandSender, label, parsingArgs.get(i));
				if (o == null)
					return true;
				list.add(o);
			}
			Class<?> clazz = info.getParameters().get(varArgIndex);
			int endOffset = parsingArgs.size() - info.getVarArgsEndOffset();
			if (clazz.isArray())
			{
				Object objectArray = Array.newInstance(clazz.getComponentType(), endOffset - varArgIndex);
				int arraySize = 0;
				boolean isPrimitive = clazz.getComponentType().isPrimitive();
				for (int i = varArgIndex; i < endOffset; i++)
				{
					Object o = parseObject(clazz, info, commandSender, label, parsingArgs.get(i));
					if (o == null)
						return true;

					if(isPrimitive)
					{
						arraySetWrapper(objectArray, arraySize++, o);
					}
					else
					{
						Array.set(objectArray, arraySize++, o);
					}
				}
				list.add(objectArray);
			}
			else
			{
				list.add(StringUtils.join(parsingArgs.subList(varArgIndex, endOffset), " "));
			}
			for (int i = endOffset; i < parsingArgs.size(); i++)
			{
				Object o = parseObject(info.getParameters().get(list.size() - 1), info, commandSender, label, parsingArgs.get(i));
				if (o == null)
					return true;
				list.add(o);
			}
		}
		return info.call(list.toArray());
	}

	private boolean validateCommandSender(CommandSender commandSender, CommandInfo info)
	{
		String permission = info.getPermission();
		if (permission != null && !commandSender.hasPermission(permission))
		{
			commandSender.sendMessage(info.getNoPermissionMessage());
			return false;
		}
		if (!info.getCommandSender().isAssignableFrom(commandSender.getClass()))
		{
			commandSender.sendMessage(info.getWrongSenderTypeMessage().replace(Executable.COMMAND_SENDER,
					info.getCommandSender().getSimpleName()));
			return false;
		}
		return true;
	}

	private Object parseObject(Class<?> classParam, CommandInfo info, CommandSender commandSender, String label,
			String parseString)
	{
		Class<?> clazz = classParam.isArray() ? classParam.getComponentType() : classParam;
		IObjectParser<?> parser = objectParserManager.getParser(clazz);
		if (parser == null)
			throw new NullPointerException("Parser not found for " + clazz.getName());

		Object o = parser.parse(parseString);
		if (o == null)
		{
			if (parser.getParseFailListener() != null)
			{
				parser.getParseFailListener().onParseFailed(commandSender, parseString);
			}
			else if (info.getUsage() != null)
			{
				commandSender.sendMessage(info.getUsage().replace(Executable.COMMAND, label));
			}
		}
		return o;
	}

	/**
	 * Get the ObjectParserManager
	 *
	 * @return the ObjectParserManager
	 */
	public ObjectParserManager getObjectParserManager()
	{
		return objectParserManager;
	}

	private static void arraySetWrapper(Object array, int index, Object value)
	{
		if (value instanceof Boolean)
		{
			Array.set(array, index, ((Boolean) value).booleanValue());
			return;
		}
		if (value instanceof Byte)
		{
			Array.set(array, index, ((Byte) value).byteValue());
			return;
		}
		if (value instanceof Character)
		{
			Array.set(array, index, ((Character) value).charValue());
			return;
		}
		if (value instanceof Double)
		{
			Array.set(array, index, ((Double) value).doubleValue());
			return;
		}
		if (value instanceof Float)
		{
			Array.set(array, index, ((Float) value).floatValue());
			return;
		}
		if (value instanceof Integer)
		{
			Array.set(array, index, ((Integer) value).intValue());
			return;
		}
		if (value instanceof Long)
		{
			Array.set(array, index, ((Long) value).longValue());
			return;
		}
		if (value instanceof Short)
		{
			Array.set(array, index, ((Short) value).shortValue());
			return;
		}

		throw new IllegalArgumentException("No primitive wrapper");
	}
}
