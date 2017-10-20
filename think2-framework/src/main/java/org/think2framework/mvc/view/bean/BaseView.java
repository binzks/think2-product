package org.think2framework.mvc.view.bean;

import java.util.List;

/**
 * 基础视图信息
 */
public class BaseView {

	private String pk; // 主键

	private List<Cell> cells; // 单元格

	private List<Action> actions; // 操作按钮

	public BaseView() {
	}

	public BaseView(String pk, List<Cell> cells, List<Action> actions) {
		this.pk = pk;
		this.cells = cells;
		this.actions = actions;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public List<Cell> getCells() {
		return cells;
	}

	public void setCells(List<Cell> cells) {
		this.cells = cells;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}
}
