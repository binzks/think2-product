package org.think2framework.mvc.view.core;

import org.think2framework.mvc.view.HtmlTag;

public class FileTag extends AbstractHtmlTag implements HtmlTag {

	public FileTag() {
		super("input");
		setAttribute("id", "input-file" + System.currentTimeMillis());
		setAttribute("type", "file");
	}

}
