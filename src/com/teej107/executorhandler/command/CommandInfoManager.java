package com.teej107.executorhandler.command;

import com.teej107.executorhandler.Executable;

import java.util.*;

/**
 * @author teej107
 * @since Oct 26, 2015
 */
public class CommandInfoManager implements Comparator<CommandMatch>
{
	private Map<String, Collection<CommandInfo>> commandMap;

	public CommandInfoManager()
	{
		commandMap = new HashMap<>();
	}

	public void add(CommandInfo info)
	{
		getCollection(commandMap, info.getCommandName()).add(info);
	}

	private <K> Collection<CommandInfo> getCollection(Map<K, Collection<CommandInfo>> map, K key)
	{
		Collection<CommandInfo> collection = map.get(key);
		if (collection == null)
		{
			collection = new ArrayList<>();
			map.put(key, collection);
		}
		return collection;
	}

	public CommandMatch get(String command, String... args)
	{
		Collection<CommandInfo> commandCollection = commandMap.get(command);
		if (commandCollection == null)
			return null;

		Set<CommandMatch> matched = new TreeSet<>(this);
		commandCollection:
		for (CommandInfo info : commandCollection)
		{
			if (info.hasVarArgs())
			{
				if (info.getCommandArgs().length > args.length)
					continue;
			}
			else if (info.getCommandArgs().length != args.length)
				continue;

			List<String> infoArgs = new ArrayList<>(info.getCommandArgs().length);
			Collections.addAll(infoArgs, info.getCommandArgs());

			List<String> argsList = new ArrayList<>(args.length);
			Collections.addAll(argsList, args);
			int defIndex = 0;
			for (String definedArgument : info.getDefinedArguments())
			{
				int index = argsList.indexOf(definedArgument);
				if (defIndex > index)
					continue commandCollection;

				defIndex = index;
				argsList.remove(index);
				infoArgs.remove(definedArgument);
			}
			for (String leftOver : infoArgs)
			{
				if (!(leftOver.equals(Executable.VAR_ARG) || leftOver.equals(Executable.OBJECT)))
					continue commandCollection;
			}
			if (info.getDefinedArguments().size() == 1)
			{
				int index = -1;
				String arg = info.getDefinedArguments().get(0);
				for (int i = 0; i < info.getCommandArgs().length; i++)
				{
					if (info.getCommandArgs()[i].equalsIgnoreCase(arg))
					{
						index = i;
						break;
					}
				}
				int argsIndex = info.hasVarArgs() && info.getVarArgsStart() < index ? args.length - info.getVarArgsEndOffset() : index;
				if (!args[argsIndex].equalsIgnoreCase(info.getCommandArgs()[index]))
					continue;
			}
			matched.add(new CommandMatch(info, argsList, infoArgs));
		}

		Iterator<CommandMatch> itr = matched.iterator();
		return itr.hasNext() ? itr.next() : null;
	}

	@Override
	public int compare(CommandMatch o1, CommandMatch o2)
	{
		return o2.getCommandInfo().getDefinedArguments().size() - o1.getCommandInfo().getDefinedArguments().size();
	}
}
