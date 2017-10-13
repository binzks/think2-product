package org.think2framework;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by zhoubin on 16/7/8. 配置
 */
public class Configuration implements ApplicationContextAware {

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		System.setProperty("jsse.enableSNIExtension", "false");
	}

	/**
	 * 初始化数据源配置
	 *
	 * @param datasource
	 *            数据源文件路径和名称
	 */
	public void setDatasource(String datasource) {
		ModelFactory.setDatabaseFiles(this.getClass().getResource("/").getPath() + datasource);
	}

	/**
	 * 初始化常量配置
	 *
	 * @param constant
	 *            常量路径和名称
	 */
	public void setConstant(String constant) {
		ConstantFactory.setConstantFiles(this.getClass().getResource("/").getPath() + constant);
	}

	/**
	 * 初始化消息信息
	 *
	 * @param message
	 *            消息路径和名称
	 */
	public void setMessage(String message) {
		MessageFactory.setMessage(this.getClass().getResource("/").getPath() + message);
	}

	/**
	 * 初始化工作流
	 * 
	 * @param workflow
	 *            工作流路径和名称
	 */
	public void setWorkflow(String workflow) {
		WorkflowFactory.setWorkflowFiles(this.getClass().getResource("/").getPath() + workflow);
	}

	/**
	 * 初始化模型信息
	 *
	 * @param model
	 *            模型路径和名称
	 */
	public void setModel(String model) {
		ModelFactory.setModelFiles(this.getClass().getResource("/").getPath() + model);
	}

	// /**
	// * 扫描包，初始化模型
	// *
	// * @param packages
	// * 数组map参数，query表示查询数据源，writer表示写入数据源，packages表示扫描包名，多个,隔开
	// */
	// public void setPackages(List<Map<String, String>> packages) {
	// if (packageInit) {
	// return;
	// }
	// for (Map<String, String> map : packages) {
	// // ModelFactory.scanPackages(StringUtils.toString(map.get("query")),
	// // StringUtils.toString(map.get("writer")),
	// // StringUtils.split(StringUtils.toString(map.get("packages")), ","));
	// }
	// packageInit = true;
	// }

}
