/*
 * FileName：ImageTag.java 2009-8-19 
 * Copyright (C) 2003-2007 try2it.com
 */
package org.macula.boot.utils.excel.tags;

import java.util.StringTokenizer;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.macula.boot.utils.excel.ExcelUtils;
import org.macula.boot.utils.excel.parser.ExcelParser;

/**
 * <p>
 * <b>ImageTag</b>is
 * </p>
 * 
 * @author rainsoft
 * @since 2009-8-19
 * @version $Revision: 5907 $ $Date: 2015-10-20 15:54:29 +0800 (Tue, 20 Oct 2015) $
 */
public class ImageTag implements ITag {

	public static final String KEY_IMAGE = "#image";

	public static final String HSSF_PATRI_ARCH = "$_HSSF_PATRI_ARCH_$";

	public int[] parseTag(Object context, HSSFWorkbook wb, HSSFSheet sheet, HSSFRow curRow, HSSFCell curCell) {
		HSSFPatriarch pa = (HSSFPatriarch) ExcelParser.getValue(context, sheet.getSheetName() + HSSF_PATRI_ARCH);
		if (null == pa) {
			pa = sheet.createDrawingPatriarch();
			ExcelUtils.addValue(context, sheet.getSheetName() + HSSF_PATRI_ARCH, pa);
		}
		String image = curCell.getStringCellValue();
		StringTokenizer st = new StringTokenizer(image, " ");
		String expr = "${imageData}";
		int width = 1;
		int height = 1;
		int imageType = HSSFWorkbook.PICTURE_TYPE_JPEG;
		int pos = 0;
		while (st.hasMoreTokens()) {
			String str = st.nextToken();
			if (pos == 1) {
				expr = str;
			}
			if (pos == 2) {
				width = Integer.parseInt(str);
			}
			if (pos == 3) {
				height = Integer.parseInt(str);
			}
			if (pos == 4) {
				imageType = Integer.parseInt(str);
			}
			pos++;
		}

		curCell.setCellValue("");
		byte[] imageData = (byte[]) ExcelParser.parseExpr(context, expr);
		if (null != imageData) {
			insertImage(wb, pa, imageData, curRow.getRowNum(), curCell.getColumnIndex(), width, height, imageType);
		}
		return new int[] { 0, 0, 0 };
	}

	private void insertImage(HSSFWorkbook wb, HSSFPatriarch pa, byte[] data, int row, int column, int width, int height,
			int imageType) {
		HSSFClientAnchor anchor = new HSSFClientAnchor(0, 2, 0, 0, (short) column, row, (short) (column + width), row
				+ height);
		anchor.setAnchorType(HSSFClientAnchor.MOVE_DONT_RESIZE);
		pa.createPicture(anchor, wb.addPicture(data, imageType));
	}

	public String getTagName() {
		return KEY_IMAGE;
	}

	public boolean hasEndTag() {
		return false;
	}
}
