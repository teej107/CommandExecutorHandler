package com.teej107.executorhandler.command;

import java.util.List;

/**
 * @author teej107
 * @since Nov 11, 2015
 */
public class CommandMatch
{
	private CommandInfo info;
	private List<String> parsingArgs;
	private List<String> infoArgs;

	public CommandMatch(CommandInfo info, List<String> parsingArgs, List<String> infoArgs)
	{
		this.info = info;
		this.parsingArgs = parsingArgs;
		this.infoArgs = infoArgs;
	}

	public List<String> getInfoArgs()
	{
		return infoArgs;
	}

	public List<String> getParsingArgs()
	{
		return parsingArgs;
	}

	public CommandInfo getCommandInfo()
	{
		return info;
	}

}
