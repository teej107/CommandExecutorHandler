package com.teej107.executorhandler.parser;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

/**
 * @author teej107
 * @since Oct 31, 2015
 */
public class OfflinePlayerParser extends AbstractObjectParser<OfflinePlayer>
{
	@Override
	public OfflinePlayer parse(String s)
	{
		return Bukkit.getOfflinePlayer(s);
	}

	@Override
	public void onParseFailed(CommandSender sender, String s)
	{

	}
}
