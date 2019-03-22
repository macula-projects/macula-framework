/*
 * FileName: MergeTag.java 2006-10-13
 * Copyright (c) 2003-2005 try2it.com
 */
package org.macula.boot.utils.excel.tags;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.macula.boot.utils.excel.WorkbookUtils;
import org.macula.boot.utils.excel.parser.ExcelParser;

import java.util.StringTokenizer;

/**
 * <p>
 * <b>MergeTag</b> is a class which merge cell by condition example: #merge columnIndex ${value}
 * </p>
 *
 * @author <a href="mailto:wangzp@try2it.com">rainsoft</a>
 * @version $Revision: 5907 $ $Date: 2015-10-20 15:54:29 +0800 (Tue, 20 Oct 2015) $
 */
public class MergeTag implements ITag {

    public static String KEY_MERGE = "#merge";
    private Log LOG = LogFactory.getLog(MergeTag.class);

    public int[] parseTag(Object context, HSSFWorkbook wb, HSSFSheet sheet, HSSFRow curRow, HSSFCell curCell) {
        String column = "";
        String value = "";
        String merge = curCell.getStringCellValue();
        StringTokenizer st = new StringTokenizer(merge, " ");
        int pos = 0;
        while (st.hasMoreTokens()) {
            String str = st.nextToken();
            if (pos == 1) {
                column = str;
            }
            if (pos == 2) {
                value = str;
            }
            pos++;
        }

        // curCell.setEncoding(HSSFWorkbook.ENCODING_UTF_16);
        curCell.setCellValue(value);
        ExcelParser.parseCell(context, sheet, curRow, curCell);

        // last row
        HSSFRow lastRow = WorkbookUtils.getRow(curRow.getRowNum() - 1, sheet);

        // can merge flag
        boolean canMerge = true;

        // compare columns
        String[] columns = column.split(",");
        for (int index = 0; index < columns.length; index++) {
            int columnIndex = Integer.parseInt(columns[index]);
            LOG.debug("#merge compare column index " + columnIndex);

            // compare the cell
            if (curRow.getRowNum() - 1 >= sheet.getFirstRowNum()) {
                HSSFCell lastCell = WorkbookUtils.getCell(lastRow, columnIndex);
                HSSFCell compCell = WorkbookUtils.getCell(curRow, columnIndex);
                if (lastCell.getCellType() == compCell.getCellType()) {
                    switch (compCell.getCellType()) {
                        case STRING:
                            canMerge &= lastCell.getStringCellValue().equals(compCell.getStringCellValue());
                            break;
                        case BOOLEAN:
                            canMerge &= lastCell.getBooleanCellValue() == compCell.getBooleanCellValue();
                            break;
                        case NUMERIC:
                            canMerge &= lastCell.getNumericCellValue() == compCell.getNumericCellValue();
                            break;
                        default:
                            canMerge &= false;
                    }
                } else {
                    canMerge &= false;
                }
            }
        }

        if (canMerge) {
            sheet.addMergedRegion(new CellRangeAddress(lastRow.getRowNum(), curRow.getRowNum(), curCell.getColumnIndex(),
                    curCell.getColumnIndex()));
        }

        return new int[]{0, 0, 0};
    }

    public boolean hasEndTag() {
        return false;
    }

    public String getTagName() {
        return KEY_MERGE;
    }
}
