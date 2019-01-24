/**
 *
 */
package org.macula.boot.core.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * 分页时告诉后端需要请求多少页的内容
 *
 * @author Oliver.Zheng
 */

@Data
public class PageRequestVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer pageNumber;

    private Integer pageSize;

    // 需要排序的字段名称
    private String sort;

    // 排序的方向，ASC升序，DESC降序
    private String order;

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

        if (!Objects.equals(pageNumber, pageInfo.pageNumber) || !Objects.equals(pageSize, pageInfo.pageSize)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return pageNumber != null ? pageNumber.hashCode() : 0;
    }
}
