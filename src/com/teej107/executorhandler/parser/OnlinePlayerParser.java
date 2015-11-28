package com.teej107.executorhandler.parser;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author teej107
 * @since Oct 31, 2015
 */
public class OnlinePlayerParser extends AbstractObjectParser<Player>
{
	@Override
	public Player parse(String s)
	{
		return Bukkit.getPlayer(s);
	}

	@Override
	public void onParseFailed(CommandSender sender, String s)
	{
		sender.sendMessage(s + " is not online");
	}
}
