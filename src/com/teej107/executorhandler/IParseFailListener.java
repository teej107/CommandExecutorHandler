package com.teej107.executorhandler;

import org.bukkit.command.CommandSender;

/**
 * @author teej107
 * @since Oct 31, 2015
 */
public interface IParseFailListener
{
	void onParseFailed(CommandSender sender, String s);
}
