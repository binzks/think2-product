package org.think2framework.mvc;

import com.fasterxml.jackson.core.type.TypeReference;
import org.think2framework.mvc.bean.Node;
import org.think2framework.mvc.bean.Workflow;
import org.think2framework.exception.ExistException;
import org.think2framework.exception.NonExistException;
import org.think2framework.exception.UndefinedException;
import org.think2framework.utils.FileUtils;
import org.think2framework.utils.JsonUtils;
import org.think2framework.utils.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkflowFactory {

	private static final Logger logger = LogManager.getLogger(WorkflowFactory.class);

	private static String workflowFiles; // 工作流配置

	private static Map<String, Workflow> workflows = new HashMap<>(); // 工作流

	/**
	 * 设置工作流配置文件，如果第一次设置则加载配置
	 *
	 * @param workflow
	 *            配置文件
	 */
	public static synchronized void setWorkflowFiles(String workflow) {
		if (StringUtils.isBlank(workflowFiles)) {
			workflowFiles = workflow;
			logger.debug("Set constant files {}", workflowFiles);
			reload();
		}
	}

	/**
	 * 重新加载常量配置文件
	 */
	public static synchronized void reload() {
		if (StringUtils.isBlank(workflowFiles)) {
			return;
		}
		File[] files = FileUtils.getFiles(workflowFiles);
		if (null == files) {
			return;
		}
		workflows.clear();
		logger.debug("All workflow cleared successfully");
		for (File file : files) {
			List<Workflow> workflowList = JsonUtils.readFile(file, new TypeReference<List<Workflow>>() {
			});
			for (Workflow workflow : workflowList) {
				append(workflow);
			}
		}
		logger.debug("Reload workflow config file {}", workflowFiles);
	}

	/**
	 * 追加一个工作流，如果已经存在则跑错
	 * 
	 * @param workflow
	 *            工作流
	 */
	public static synchronized void append(Workflow workflow) {
		String name = workflow.getName();
		if (null != workflows.get(name)) {
			throw new ExistException(Workflow.class.getName() + " " + name);
		}
		List<Node> nodes = workflow.getNodes();
		if (null == nodes || nodes.size() == 0) {
			throw new UndefinedException(Workflow.class.getName() + " " + name + " 节点");
		}
		workflows.put(name, workflow);
		logger.debug("Append workflow {}", name);
	}

	/**
	 * 根据名称获取工作流
	 * 
	 * @param name
	 *            名称
	 * @return 工作流
	 */
	public static Workflow get(String name) {
		Workflow workflow = workflows.get(name);
		if (null == workflow) {
			throw new NonExistException(Workflow.class.getName() + " " + name);
		}
		return workflow;
	}

}
