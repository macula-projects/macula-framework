/**
 * Copyright 2010-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.macula.boot.core.vo;

import java.util.List;

import org.springframework.data.domain.Page;

/**
 * <p> <b>PageResponse</b> 是数据集的Result实现. </p>
 * 
 * @since 2011-7-7
 * @author Wilson Luo
 * @version $Id: PageResponse.java 5734 2015-08-17 08:29:11Z wzp $
 */
public class PageResponse<T> extends Response {

	private static final long serialVersionUID = 1L;

	/** 本次请求的记录数 */
	private int size;
	
	/** 当前页码，从零开始 */
	private int number;
	
	/** 总记录数 */
	private long totalElements;
	
	/** 总页数 */
	private int totalPages;
	
	/** 本页的总记录数 */
	private int numberOfElements;
	
	/** 是否首页 */
	private boolean firstPage;
	
	/** 是否最后页 */
	private boolean lastPage;
	
	/** 内容列表 */
	private List<T> content;
	
	public PageResponse() {
	}

	public PageResponse(Page<T> page) {
		setPage(page);
	}
	
	public void setPage(Page<T> page) {
		this.size = page.getSize();
		this.number = page.getNumber();
		this.totalElements = page.getTotalElements();
		this.totalPages = page.getTotalPages();
		this.numberOfElements = page.getNumberOfElements();
		this.firstPage = page.isFirst();
		this.lastPage = page.isLast();
		this.content = page.getContent();
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(int number) {
		this.number = number;
	}

	/**
	 * @return the totalElements
	 */
	public long getTotalElements() {
		return totalElements;
	}

	/**
	 * @param totalElements the totalElements to set
	 */
	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	/**
	 * @return the totalPages
	 */
	public int getTotalPages() {
		return totalPages;
	}

	/**
	 * @param totalPages the totalPages to set
	 */
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	/**
	 * @return the numberOfElements
	 */
	public int getNumberOfElements() {
		return numberOfElements;
	}

	/**
	 * @param numberOfElements the numberOfElements to set
	 */
	public void setNumberOfElements(int numberOfElements) {
		this.numberOfElements = numberOfElements;
	}

	/**
	 * @return the firstPage
	 */
	public boolean isFirstPage() {
		return firstPage;
	}

	/**
	 * @param firstPage the firstPage to set
	 */
	public void setFirstPage(boolean firstPage) {
		this.firstPage = firstPage;
	}

	/**
	 * @return the lastPage
	 */
	public boolean isLastPage() {
		return lastPage;
	}

	/**
	 * @param lastPage the lastPage to set
	 */
	public void setLastPage(boolean lastPage) {
		this.lastPage = lastPage;
	}

	/**
	 * @return the content
	 */
	public List<T> getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(List<T> content) {
		this.content = content;
	}
}
