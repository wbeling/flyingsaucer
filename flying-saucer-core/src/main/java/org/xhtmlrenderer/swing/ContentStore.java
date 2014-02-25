package org.xhtmlrenderer.swing;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Document;

public class ContentStore
{
	private final Document doc;
	private final Map<String, String> store = new HashMap<>();
	
	public ContentStore(Document doc)
	{
		this.doc = doc;
	}

	public void addContent(String uri, String content)
	{
		store.put(uri, content);
	}
	
	public boolean isAvailable(String uri)
	{
		return store.containsKey(uri);
	}

	public String getContent(String uri)
	{
		return store.get(uri);
	}

	public Document getDocument()
	{
		return doc;
	}
}
