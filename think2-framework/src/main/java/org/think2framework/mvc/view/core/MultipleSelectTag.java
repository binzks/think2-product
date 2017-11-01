package org.think2framework.mvc.view.core;

import org.think2framework.mvc.view.HtmlTag;
import org.think2framework.utils.StringUtils;

/**
 * 多选框，多选框的值是,1,1,的格式
 */
public class MultipleSelectTag extends SelectTag implements HtmlTag {

	public MultipleSelectTag() {
		super();
		setAttribute("class", "multiselect");
		setAttribute("multiple", "multiple");
	}

	@Override
	protected Boolean checkValue(String value) {
		return StringUtils.contains(this.value, "," + value + ",");
	}
}
