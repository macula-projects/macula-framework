/*
 * Copyright 2003-2005 try2it.com.
 * Created on 2005-7-7
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

package org.maculaframework.boot.utils.excel.tags;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.maculaframework.boot.utils.excel.WorkbookUtils;
import org.maculaframework.boot.utils.excel.parser.ExcelParser;
import org.maculaframework.boot.utils.excel.parser.SpringExpression;

/**
 * <p>
 * <b>IfTag</b> is a class which parse #if #else #end
 * </p>
 * 
 * @author rainsoft
 * @version $Revision: 5907 $ $Date: 2015-10-20 15:54:29 +0800 (Tue, 20 Oct 2015) $
 */
public class IfTag implements ITag {
	public static final String KEY_IF = "#if";

	public static final String KEY_END = "#end";

	private Log LOG = LogFactory.getLog(IfTag.class);

	public int[] parseTag(Object context, HSSFWorkbook wb, HSSFSheet sheet, HSSFRow curRow, HSSFCell curCell) {
		int ifstart = curRow.getRowNum();
		int ifend = -1;
		int ifCount = 0;
		String ifstr = "";
		boolean bFind = false;
		for (int rownum = ifstart; rownum <= sheet.getLastRowNum(); rownum++) {
			HSSFRow row = sheet.getRow(rownum);
			if (null == row)
				continue;
			for (short colnum = row.getFirstCellNum(); colnum <= row.getLastCellNum(); colnum++) {
				HSSFCell cell = row.getCell(colnum, Row.MissingCellPolicy.RETURN_NULL_AND_BLANK);
				if (null == cell)
					continue;
				if (cell.getCellType() == CellType.STRING) {
					String cellstr = cell.getStringCellValue();

					// get the tag instance for the cellstr
					ITag tag = ExcelParser.getTagClass(cellstr);

					if (null != tag) {
						if (tag.hasEndTag()) {
							if (0 == ifCount) {
								ifstart = rownum;
								ifstr = cellstr;
							}
							ifCount++;
							break;
						}
					}
					if (cellstr.startsWith(KEY_END)) {
						ifend = rownum;
						ifCount--;
						if (ifstart >= 0 && ifend >= 0 && ifend > ifstart && ifCount == 0) {
							bFind = true;
						}
						break;
					}
				}
			}
			if (bFind)
				break;
		}

		if (!bFind)
			return new int[] { 0, 0, 1 };

		// test if condition
		boolean bResult = false;
		// remove #if tag and get condition expression
		String expr = ifstr.trim().substring(KEY_IF.length()).trim();

		// parse the condition expression
		expr = (String) ExcelParser.parseStr(context, expr, true);

		// use spring expression to eval expression value

		try {

			
			LOG.debug("IfTag test expr=" + expr);
			
			bResult  = SpringExpression.eval(context, expr, Boolean.class);
			
		} catch (Exception e) {
			LOG.error("IfTag test expr error", e);
			bResult = false;
		}

		if (bResult) { // if condition is true
			// remove #if tag and #end tag only
			sheet.removeRow(WorkbookUtils.getRow(ifstart, sheet));
			sheet.removeRow(WorkbookUtils.getRow(ifend, sheet));
			// remove merged region in ifstart & ifend
			for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
				CellRangeAddress r = sheet.getMergedRegion(i);
				if (r.getFirstRow() == ifstart && r.getLastRow() == ifstart || r.getFirstRow() == ifend
						&& r.getLastRow() == ifend) {
					sheet.removeMergedRegion(i);
					// we have to back up now since we removed one
					i = i - 1;
				}
			}
			if (ifend + 1 <= sheet.getLastRowNum()) {
				sheet.shiftRows(ifend + 1, sheet.getLastRowNum(), -1, true, true);
			}
			if (ifstart + 1 <= sheet.getLastRowNum()) {
				sheet.shiftRows(ifstart + 1, sheet.getLastRowNum(), -1, true, true);
			}
			return new int[] { 1, -2, 1 };
		} else { // if condition is false
			// remove #if #end block
			for (int rownum = ifstart; rownum <= ifend; rownum++) {
				sheet.removeRow(WorkbookUtils.getRow(rownum, sheet));
			}
			// remove merged region in ifstart & ifend
			for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
				CellRangeAddress r = sheet.getMergedRegion(i);
				if (r.getFirstRow() >= ifstart && r.getLastRow() <= ifend) {
					sheet.removeMergedRegion(i);
					// we have to back up now since we removed one
					i = i - 1;
				}
			}
			if (ifend + 1 <= sheet.getLastRowNum()) {
				sheet.shiftRows(ifend + 1, sheet.getLastRowNum(), -(ifend - ifstart + 1), true, true);
			}
			return new int[] { ExcelParser.getSkipNum(ifstart, ifend), ExcelParser.getShiftNum(ifend, ifstart), 1 };
		}
	}

	public String getTagName() {
		return KEY_IF;
	}

	public boolean hasEndTag() {
		return true;
	}

	public static void main(String[] args) {

	}
}
