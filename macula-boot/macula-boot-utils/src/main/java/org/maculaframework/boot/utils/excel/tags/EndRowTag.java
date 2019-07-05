/*
 * Copyright 2004-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.maculaframework.boot.utils.excel.tags;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * <p>
 * <b>EndRowTag</b> is a class which will delete rows after endExcelTag
 * </p>
 * 
 * @author <a href="mailto:wangzp@try2it.com">rainsoft</a>
 * @version $Revision: 5906 $ $Date: 2015-10-19 17:40:12 +0800 (Mon, 19 Oct 2015) $
 */
public class EndRowTag implements ITag {

	private Log LOG = LogFactory.getLog(EndRowTag.class);

	public static final String KEY_ENDROW = "#endRow";

	public static final String KEY_ENDCOLUMN = "#endColumn";

	public int[] parseTag(Object context, HSSFWorkbook wb, HSSFSheet sheet, HSSFRow curRow, HSSFCell curCell) {
		// remove the rowBreaks after #endRow
		int breaks[] = sheet.getRowBreaks();
		for (int i = 0; null != breaks && i < breaks.length; i++) {
			if (breaks[i] >= curRow.getRowNum()) {
				sheet.removeRowBreak(breaks[i]);
			}
		}
		LOG.debug("EndRowTag at " + curRow.getRowNum());
		// remove the blank row after #endRow
		for (int rownum = sheet.getLastRowNum(); rownum > curRow.getRowNum(); rownum--) {
			HSSFRow row = sheet.getRow(rownum);
			sheet.removeRow(row);
		}

		return new int[] { 0, 0, 0 };
	}

	public boolean hasEndTag() {
		return false;
	}

	public String getTagName() {
		return KEY_ENDROW;
	}
}