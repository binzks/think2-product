//package org.think2framework.mvc.view.core;
//
//import org.think2framework.context.ModelFactory;
//import org.think2framework.mvc.view.HtmlTag;
//import org.think2framework.mvc.view.bean.Item;
//import org.think2framework.orm.Query;
//import org.think2framework.utils.StringUtils;
//
//import java.util.List;
//import java.util.Map;
//
//public class DataSelectTag extends SelectTag implements HtmlTag {
//
//	public DataSelectTag(List<Item> items) {
//		super();
//		if (null == items) {
//			return;
//		}
//		for (Item item : items) {
//			Query query = ModelFactory.createQuery(item.getModel());
//			List<Map<String, Object>> list = query.queryForList();
//			if (null != list) {
//				for (Map<String, Object> map : list) {
//					setOption(StringUtils.toString(map.get(item.getKey())),
//							StringUtils.toString(map.get(item.getValue())));
//				}
//			}
//		}
//	}
//
//	@Override
//	public String htmlString() {
//
//	}
//
//}
