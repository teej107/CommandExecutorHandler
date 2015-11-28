package com.teej107.executorhandler.parser;

import org.bukkit.command.CommandSender;

/**
 * @author teej107
 * @since Oct 31, 2015
 */
public class DoubleParser extends AbstractObjectParser<Double>
{
	@Override
	public Double parse(String s)
	{
		try
		{
			return Double.parseDouble(s);
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
