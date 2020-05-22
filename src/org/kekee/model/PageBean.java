package org.kekee.model;

import lombok.Data;

/**
 * @author cocoa
 */
@Data
public class PageBean {
	private int page;
	private int pageSize;
	@SuppressWarnings("unused")
	private int start;
	public PageBean(int page, int pageSize) {
		super();
		this.page = page;
		this.pageSize = pageSize;
	}
}
