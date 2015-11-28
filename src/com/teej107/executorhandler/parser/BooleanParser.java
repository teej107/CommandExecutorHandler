package com.teej107.executorhandler.parser;

import org.bukkit.command.CommandSender;

/**
 * @author teej107
 * @since Oct 31, 2015
 */
public class BooleanParser extends AbstractObjectParser<Boolean>
{
	@Override
	public Boolean parse(String s)
	{
		if(s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false"))
			return Boolean.parseBoolean(s);

		return null;
	}

	@Override
	public void onParseFailed(CommandSender sender, String s)
	{
		sender.sendMessage("\'" + s + "\' must be true or false");
	}
}
