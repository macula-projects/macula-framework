/**
 * PageVo.java 2015年8月18日
 */
package org.macula.boot.core.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;

/**
 * <p>
 * <b>PageVo</b> 给API包返回使用的VO
 * </p>
 *
 * @since 2015年8月18日
 * @author Rain
 * @version $Id: PageVo.java 5746 2015-08-18 03:59:33Z wzp $
 */
public class PageVo<T> implements Page<T>, Serializable {

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
	private boolean first;
	
	/** 是否最后页 */
	private boolean last;
	
	/** 内容列表 */
	private List<T> content;
	
	public PageVo() {
	}

	public PageVo(Page<T> page) {
		setPage(page);
	}
	
	public void setPage(Page<T> page) {
		this.size = page.getSize();
		this.number = page.getNumber();
		this.totalElements = page.getTotalElements();
		this.totalPages = page.getTotalPages();
		this.numberOfElements = page.getNumberOfElements();
		this.first = page.isFirst();
		this.last = page.isLast();
		this.content = page.getContent();
	}

	/**
	 * @return the size
	 */
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
	public boolean isFirst() {
		return first;
	}

	/**
	 * @param firstPage the firstPage to set
	 */
	public void setFirst(boolean first) {
		this.first = first;
	}

	/**
	 * @return the lastPage
	 */
	@Override
	public boolean isLast() {
		return last;
	}

	/**
	 * @param lastPage the lastPage to set
	 */
	public void setLast(boolean last) {
		this.last = last;
	}

	/**
	 * @return the content
	 */
	@Override
	public List<T> getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(List<T> content) {
		this.content = content;
	}

	@Override
	public boolean hasContent() {
		return !content.isEmpty();
	}

	@Override
	public boolean hasNext() {
		return getNumber() + 1 < getTotalPages();
	}

	@Override
	public boolean hasPrevious() {
		return getNumber() > 0;
	}

	@Override
	public Iterator<T> iterator() {
		return content.iterator();
	}
	
	@Override
	public Sort getSort() {
		return null;
	}
	
	@Override
	public Pageable nextPageable() {
		return null;
	}

	@Override
	public Pageable previousPageable() {
		return null;
	}

	@Override
	public <S> Page<S> map(Converter<? super T, ? extends S> converter) {
		Assert.notNull(converter, "Converter must not be null!");

		List<S> result = new ArrayList<S>(content.size());

		for (T element : this) {
			result.add(converter.convert(element));
		}
		
		PageVo<S> vo = new PageVo<S>();
		
		vo.setContent(result);
		vo.setFirst(this.isFirst());
		vo.setLast(this.isLast());
		vo.setNumber(this.getNumber());
		vo.setNumberOfElements(this.getNumberOfElements());
		vo.setSize(this.getSize());
		vo.setTotalElements(this.getTotalElements());
		vo.setTotalPages(this.getTotalPages());
		
		return vo;
	}
}
