package com.teej107.executorhandler.parser;

import org.bukkit.command.CommandSender;

/**
 * @author teej107
 * @since Oct 26, 2015
 */
public class IntegerParser extends AbstractObjectParser<Integer>
{
	@Override
	public Integer parse(String s)
	{
		try
		{
			return Integer.parseInt(s);
		}
		catch (NumberFormatException e)
		{
		}
		return null;
	}

	@Override
	public void onParseFailed(CommandSender sender, String s)
	{
		sender.sendMessage("\'" + s + "\' is not a number");
	}
}
