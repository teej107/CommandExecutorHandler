package com.teej107.executorhandler;

import com.teej107.executorhandler.parser.*;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * A manager to store and fetch IObjectParsers
 * @author teej107
 * @since Oct 26, 2015
 */
public class ObjectParserManager
{
	private Map<Class<?>, IObjectParser<?>> parserMap;

	public ObjectParserManager()
	{
		parserMap = new HashMap<>();
		addParsers();
	}

	private void addParsers()
	{
		addParser(int.class, new IntegerParser());
		addParser(double.class, new DoubleParser());
		addParser(boolean.class, new BooleanParser());

		addParser(String.class, new StringParser());

		addParser(Material.class, new MaterialParser());
		addParser(Player.class, new OnlinePlayerParser());
		addParser(OfflinePlayer.class, new OfflinePlayerParser());
	}

	/**
	 * Adds a parser to the ObjectParserManager's parser Map.This will override any current parser associated with the given Class
	 * @param type The class of the Object that the IObjectParser will return
	 * @param parser The IObjectParser
	 * @param <T> class type
	 * @return true if nothing was overridden. Otherwise false
	 */
	public <T> boolean addParser(Class<T> type, IObjectParser<T> parser)
	{
		return parserMap.put(type, parser) == null;
	}

	/**
	 * Get if there is an IObjectParser for the class
	 * @param type the class
	 * @return true if there is an IObjectParser for the class. Otherwise false
	 */
	public boolean hasParser(Class<?> type)
	{
		return parserMap.containsKey(type);
	}

	/**
	 * Get the parser from the given class.
	 * @param type the parser class type
	 * @param <T> class type
	 * @return The IObjectParser associated from the given class. It will return null if there is no parser associated with the class
	 */
	public <T> IObjectParser<T> getParser(Class<T> type)
	{
		return (IObjectParser<T>) parserMap.get(type);
	}
}
