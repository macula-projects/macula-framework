/**
 * 
 */
package org.macula.boot.core.vo;

import java.io.Serializable;

/**
 * 分页时告诉后端需要请求多少页的内容
 * 
 * @author Oliver.Zheng
 */
public class PageRequestVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer pageNumber;
	
	private Integer pageSize;
	
	// 需要排序的字段名称
	private String sort;
	
	// 排序的方向，ASC升序，DESC降序
	private String order;

	public Integer getPageNumber() {

		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	/**
	 * @return the sort
	 */
	public String getSort() {
		return sort;
	}

	/**
	 * @param sort the sort to set
	 */
	public void setSort(String sort) {
		this.sort = sort;
	}

	/**
	 * @return the order
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(String order) {
		this.order = order;
	}

	/*** PageInfo在ProductApi.getProductPageMap(priceGroupType,listTypeCode)中做为Map集合KEY对象存在 重写equals、hashCode方法 ***/
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		PageRequestVo pageInfo = (PageRequestVo) obj;
		if ((pageNumber != null ? !pageNumber.equals(pageInfo.pageNumber) : pageInfo.pageNumber != null)
				|| (pageSize != null ? !pageSize.equals(pageInfo.pageSize) : pageInfo.pageSize != null)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return pageNumber != null ? pageNumber.hashCode() : 0;
	}
}
