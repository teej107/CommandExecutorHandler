package com.teej107.executorhandler;

/**
 * @author teej107
 * @since Oct 26, 2015
 */
public interface IObjectParser<T>
{
	T parse(String s);
	IParseFailListener getParseFailListener();
	void setParseFailListener(IParseFailListener listener);
}
