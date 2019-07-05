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

import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.maculaframework.boot.utils.excel.ExcelUtils;
import org.maculaframework.boot.utils.excel.WorkbookUtils;
import org.maculaframework.boot.utils.excel.parser.ExcelParser;

/**
 * <p>
 * <b>ForeachTag </b> is a class which parse the #foreach tag
 * </p>
 * 
 * @author rainsoft
 * @version $Revision: 5907 $ $Date: 2015-10-20 15:54:29 +0800 (Tue, 20 Oct 2015) $
 */
public class ForeachTag implements ITag {

	private Log LOG = LogFactory.getLog(ForeachTag.class);

	public static final String KEY_FOREACH = "#foreach";

	public static final String KEY_END = "#end";

	public int[] parseTag(Object context, HSSFWorkbook wb, HSSFSheet sheet, HSSFRow curRow, HSSFCell curCell) {
		int forstart = curRow.getRowNum();
		int forend = -1;
		int forCount = 0;
		String foreach = "";
		boolean bFind = false;
		LOG.debug("ForeachTag: start=" + forstart);
		for (int rownum = forstart; rownum <= sheet.getLastRowNum(); rownum++) {
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
							if (0 == forCount) {
								forstart = rownum;
								foreach = cellstr;
							}
							forCount++;
							break;
						}
					}
					if (cellstr.startsWith(KEY_END)) {
						forend = rownum;
						forCount--;
						if (forstart >= 0 && forend >= 0 && forend > forstart && forCount == 0) {
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

		String properties = "";
		String property = "";
		// parse the collection an object
		StringTokenizer st = new StringTokenizer(foreach, " ");
		int pos = 0;
		while (st.hasMoreTokens()) {
			String str = st.nextToken();
			if (pos == 1) {
				property = str;
			}
			if (pos == 3) {
				properties = str;
			}
			pos++;
		}
		// get collection
		Object collection = ExcelParser.parseStr(context, properties);
		if (null == collection) {
			return new int[] { 0, 0, 1 };
		}
		// get the iterator of collection
		Iterator<?> iterator = ExcelParser.getIterator(collection);

		// iterator
		int shiftNum = forend - forstart - 1;
		// set the start row number
		ExcelUtils.addValue(context, property + "StartRowNo", new Integer(forstart + 1));

		int old_forend = forend;
		int propertyId = 0;
		int shift = 0;
		if (null != iterator) {
			while (iterator.hasNext()) {
				Object obj = iterator.next();

				ExcelUtils.addValue(context, property, obj);
				// Iterator ID
				ExcelUtils.addValue(context, property + "Id", new Integer(propertyId));
				// Index start with 1
				ExcelUtils.addValue(context, property + "Index", new Integer(propertyId + 1));

				// shift the #foreach #end block
				sheet.shiftRows(forstart, sheet.getLastRowNum(), shiftNum, true, true);
				// copy the body fo #foreach #end block
				WorkbookUtils.copyRow(sheet, forstart + shiftNum + 1, forstart, shiftNum);
				// parse
				shift = ExcelParser.parse(context, wb, sheet, forstart, forstart + shiftNum - 1);

				forstart += shiftNum + shift;
				forend += shiftNum + shift;
				propertyId++;
			}
			ExcelUtils.addValue(context, property + "Size", new Integer(propertyId));
		}
		// set the end row number
		ExcelUtils.addValue(context, property + "EndRowNo", new Integer(forstart));
		// delete #foreach #end block
		for (int rownum = forstart; rownum <= forend; rownum++) {
			sheet.removeRow(WorkbookUtils.getRow(rownum, sheet));
		}

		// remove merged region in forstart & forend
		for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
			CellRangeAddress r = sheet.getMergedRegion(i);
			if (r.getFirstRow() >= forstart && r.getLastRow() <= forend) {
				sheet.removeMergedRegion(i);
				// we have to back up now since we removed one
				i = i - 1;
			}
		}

		if (forend + 1 <= sheet.getLastRowNum()) {
			sheet.shiftRows(forend + 1, sheet.getLastRowNum(), -(forend - forstart + 1), true, true);
		}
		return new int[] { ExcelParser.getSkipNum(forstart, forend), ExcelParser.getShiftNum(old_forend, forstart), 1 };
	}

	public String getTagName() {
		return KEY_FOREACH;
	}

	public boolean hasEndTag() {
		return true;
	}
}
