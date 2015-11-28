package com.teej107.executorhandler.parser;

import org.bukkit.command.CommandSender;

/**
 * @author teej107
 * @since Oct 26, 2015
 */
public class StringParser extends AbstractObjectParser<String>
{
	@Override
	public String parse(String s)
	{
		return s;
	}

	@Override
	public void onParseFailed(CommandSender sender, String s)
	{

	}
}
