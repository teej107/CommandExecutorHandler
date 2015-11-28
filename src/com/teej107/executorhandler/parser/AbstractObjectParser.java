package com.teej107.executorhandler.parser;

import com.teej107.executorhandler.IObjectParser;
import com.teej107.executorhandler.IParseFailListener;

/**
 * @author teej107
 * @since Oct 31, 2015
 */
public abstract class AbstractObjectParser<T> implements IObjectParser<T>, IParseFailListener
{
	private IParseFailListener listener;

	public AbstractObjectParser()
	{
		this.listener = this;
	}

	@Override
	public IParseFailListener getParseFailListener()
	{
		return listener;
	}

	@Override
	public void setParseFailListener(IParseFailListener listener)
	{
		this.listener = listener;
	}
}
