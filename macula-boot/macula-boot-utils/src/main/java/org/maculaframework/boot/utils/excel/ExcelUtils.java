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

package org.maculaframework.boot.utils.excel;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.LazyDynaBean;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.maculaframework.boot.utils.excel.parser.ExcelParser;

/**
 * <p>
 * <b>ExcelUtils</b> is a class which parse the excel report template
 * </p>
 * 
 * @author rainsoft
 * @version $Revision: 5907 $ $Date: 2015-10-20 15:54:29 +0800 (Tue, 20 Oct 2015) $
 */
@SuppressWarnings("unchecked")
public class ExcelUtils {
	static ThreadLocal<DynaBean> context = new ThreadLocal<DynaBean>();

//	/**
//	 * parse the excel template and output excel to outputStream.
//	 *
//	 * @param ctx ServletContext
//	 * @param config Excel Template Name
//	 * @param context All Data
//	 * @param out OutputStream
//	 * @throws ExcelException
//	 */
//	public static void export(ServletContext ctx, String config, Object context, OutputStream out) throws ExcelException {
//		try {
//			HSSFWorkbook wb = WorkbookUtils.openWorkbook(ctx, config);
//			parseWorkbook(context, wb);
//			wb.write(out);
//		} catch (Exception e) {
//			throw new ExcelException("export excel error: ", e);
//		}
//	}
//
//	/**
//	 * parse the excel template in a sheet and output excel to outputStream.
//	 *
//	 * @param ctx
//	 * @param config
//	 * @param sheetIndex
//	 * @param context
//	 * @param out
//	 * @throws ExcelException
//	 */
//	public static void export(ServletContext ctx, String config, int sheetIndex, Object context, OutputStream out)
//			throws ExcelException {
//		try {
//			HSSFWorkbook wb = WorkbookUtils.openWorkbook(ctx, config);
//			parseWorkbook(context, wb, sheetIndex);
//			wb.write(out);
//		} catch (Exception e) {
//			throw new ExcelException("export excel error: ", e);
//		}
//	}
//
//	/**
//	 * parse the excel template and output excel to outputStream in default context.
//	 *
//	 * @param ctx
//	 * @param config
//	 * @param out
//	 * @throws ExcelException
//	 */
//	public static void export(ServletContext ctx, String config, OutputStream out) throws ExcelException {
//		try {
//			export(ctx, config, getContext(), out);
//		} catch (Exception e) {
//			throw new ExcelException("export excel error: ", e);
//		}
//	}
//
//	/**
//	 * parse the excel template in a sheet and output excel to outputStream in default context.
//	 *
//	 * @param ctx
//	 * @param config
//	 * @param sheetIndex
//	 * @param out
//	 * @throws ExcelException
//	 */
//	public static void export(ServletContext ctx, String config, int sheetIndex, OutputStream out) throws ExcelException {
//		try {
//			export(ctx, config, sheetIndex, getContext(), out);
//		} catch (Exception e) {
//			throw new ExcelException("export excel error: ", e);
//		}
//	}

	/**
	 * parse excel and export
	 * 
	 * @param fileName
	 * @param context
	 * @param out
	 * @throws ExcelException
	 */
	public static void export(String fileName, Object context, OutputStream out) throws ExcelException {
		try {
			HSSFWorkbook wb = WorkbookUtils.openWorkbook(fileName);
			parseWorkbook(context, wb);
			wb.write(out);
		} catch (Exception e) {
			throw new ExcelException("export excel error: ", e);
		}
	}

	/**
	 * parse exel and export
	 * 
	 * @param fileName
	 * @param sheetIndex
	 * @param out
	 * @throws ExcelException
	 */
	public static void export(String fileName, int sheetIndex, Object context, OutputStream out) throws ExcelException {
		try {
			HSSFWorkbook wb = WorkbookUtils.openWorkbook(fileName);
			parseWorkbook(context, wb, sheetIndex);
			wb.write(out);
		} catch (Exception e) {
			throw new ExcelException("export excel error: ", e);
		}
	}

	/**
	 * parse excel and export excel
	 * 
	 * @param fileName
	 * @param out
	 * @throws ExcelException
	 */
	public static void export(String fileName, OutputStream out) throws ExcelException {
		try {
			export(fileName, getContext(), out);
		} catch (Exception e) {
			throw new ExcelException("export excel error: ", e);
		}
	}

	/**
	 * parse excel and export excel
	 * 
	 * @param fileName
	 * @param sheetIndex
	 * @param out
	 * @throws ExcelException
	 */
	public static void export(String fileName, int sheetIndex, OutputStream out) throws ExcelException {
		try {
			export(fileName, sheetIndex, getContext(), out);
		} catch (Exception e) {
			throw new ExcelException("export excel error: ", e);
		}
	}

	/**
	 * @param inputStream
	 * @param context
	 * @param out
	 * @throws ExcelException
	 */
	public static void export(InputStream inputStream, Object context, OutputStream out) throws ExcelException {
		try {
			HSSFWorkbook wb = WorkbookUtils.openWorkbook(inputStream);
			parseWorkbook(context, wb);
			wb.write(out);
		} catch (Exception e) {
			throw new ExcelException("export excel error: ", e);
		}
	}

	/**
	 * parse workbook
	 * 
	 * @param context
	 * @param wb
	 * @throws ExcelException
	 */
	public static void parseWorkbook(Object context, HSSFWorkbook wb) throws ExcelException {
		try {
			int sheetCount = wb.getNumberOfSheets();
			for (int sheetIndex = 0; sheetIndex < sheetCount; sheetIndex++) {
				HSSFSheet sheet = wb.getSheetAt(sheetIndex);
				parseSheet(context, wb, sheet);
				// set print area
				WorkbookUtils.setPrintArea(wb, sheetIndex);
			}
		} catch (Exception e) {
			throw new ExcelException("parseWorkbook error: ", e);
		}
	}

	/**
	 * parse Workbook
	 * 
	 * @param context
	 * @param wb
	 * @param sheetIndex
	 * @throws ExcelException
	 */
	public static void parseWorkbook(Object context, HSSFWorkbook wb, int sheetIndex) throws ExcelException {
		try {
			HSSFSheet sheet = wb.getSheetAt(sheetIndex);
			if (null != sheet) {
				parseSheet(context, wb, sheet);
				// set print area
				WorkbookUtils.setPrintArea(wb, sheetIndex);
			}

			int i = 0;
			while (i++ < sheetIndex) {
				wb.removeSheetAt(0);
			}

			i = 1;
			while (i < wb.getNumberOfSheets()) {
				wb.removeSheetAt(i);
			}
		} catch (Exception e) {
			throw new ExcelException("parseWorkbook error: ", e);
		}
	}

	/**
	 * parse Excel Template File
	 * 
	 * @param context datasource
	 * @param sheet Workbook sheet
	 */
	public static void parseSheet(Object context, HSSFWorkbook wb, HSSFSheet sheet) throws ExcelException {
		try {
			ExcelParser.parse(context, wb, sheet, sheet.getFirstRowNum(), sheet.getLastRowNum());
			try {
				// remove the last #page
				int breaks[] = sheet.getRowBreaks();
				if (breaks.length > 0) {
					sheet.removeRowBreak(breaks[breaks.length - 1]);
				}
			} catch (Exception ne) {
			}
		} catch (Exception e) {
			throw new ExcelException("parseSheet error: ", e);
		} finally {
			ExcelUtils.context.set(null);
		}
	}

	public static void addService(Object context, String key, Object service) {
		addValue(context, key, service);
	}

	public static void addService(String key, Object service) {
		addValue(key, service);
	}

	/**
	 * add a object to context
	 * 
	 * @param context must be a DynaBean or Map type
	 * @param value
	 */
	public static void addValue(Object context, String key, Object value) {
		if (context instanceof DynaBean) {
			((DynaBean) context).set(key, value);
		} else if (context instanceof Map) {
			((Map<String, Object>) context).put(key, value);
		}
	}

	/**
	 * add a object to default context
	 * 
	 * @param key
	 * @param value
	 */
	public static void addValue(String key, Object value) {
		getContext().set(key, value);
	}

	/**
	 * register extended tag package, default is net.sf.excelutils.tags
	 * 
	 * @param packageName
	 */
	public synchronized static void registerTagPackage(String packageName) {
		ExcelParser.tagPackageMap.put(packageName, packageName);
	}

	/**
	 * get a global context, it's thread safe
	 * 
	 * @return DynaBean
	 */
	public static DynaBean getContext() {
		DynaBean ctx = context.get();
		if (null == ctx) {
			ctx = new LazyDynaBean();
			setContext(ctx);
		}
		return ctx;
	}

	/**
	 * set global context
	 * 
	 * @param ctx DynaBean
	 */
	public static void setContext(DynaBean ctx) {
		context.set(ctx);
	}

	/**
	 * can value be show
	 * 
	 * @param value
	 * @return boolean
	 */
	public static boolean isCanShowType(Object value) {
		if (null == value)
			return false;
		String valueType = value.getClass().getName();
		return "java.lang.String".equals(valueType) || "java.lang.Double".equals(valueType)
				|| "java.lang.Integer".equals(valueType) || "java.lang.Boolean".equals(valueType)
				|| "java.sql.Timestamp".equals(valueType) || "java.util.Date".equals(valueType)
				|| "java.lang.Byte".equals(valueType) || "java.math.BigDecimal".equals(valueType)
				|| "java.math.BigInteger".equals(valueType) || "java.lang.Float".equals(valueType)
				|| value.getClass().isPrimitive();
	}
}