/*
 * Copyright 2003-2005 ExcelUtils http://excelutils.sourceforge.net
 * Created on 2005-6-22
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package org.macula.boot.utils.excel.tags;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * <p>
 * <b>PageTag </b> is a class which parse the #page tag Because a bug in POI, you must place a split char in your sheet
 * near the #page tag
 * </p>
 * 
 * @author rainsoft
 * @version $Revision: 5906 $ $Date: 2015-10-19 17:40:12 +0800 (Mon, 19 Oct 2015) $
 */
public class PageTag implements ITag {

	private Log LOG = LogFactory.getLog(IfTag.class);

	public static final String KEY_PAGE = "#page";

	public int[] parseTag(Object context, HSSFWorkbook wb, HSSFSheet sheet, HSSFRow curRow, HSSFCell curCell) {
		int rowNum = curRow.getRowNum();
		LOG.debug("#page at rownum = " + rowNum);
		sheet.setRowBreak(rowNum - 1);
		sheet.removeRow(curRow);
		if (rowNum + 1 <= sheet.getLastRowNum()) {
			sheet.shiftRows(rowNum + 1, sheet.getLastRowNum(), -1, true, true);
		}
		return new int[] { 0, -1, 0 };
	}

	public String getTagName() {
		return KEY_PAGE;
	}

	public boolean hasEndTag() {
		return false;
	}
}
