package com.teej107.executorhandler.parser;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;

/**
 * @author teej107
 * @since Oct 26, 2015
 */
public class MaterialParser extends AbstractObjectParser<Material>
{
	@Override
	public Material parse(String s)
	{
		return Material.matchMaterial(s);
	}

	@Override
	public void onParseFailed(CommandSender sender, String s)
	{
		sender.sendMessage("\'" + s + "\' is not a valid material");
	}
}
