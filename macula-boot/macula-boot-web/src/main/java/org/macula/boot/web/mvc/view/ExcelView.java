/*
 *  Copyright (c) 2010-2019   the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.macula.boot.web.mvc.view;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.macula.ApplicationContext;
import org.macula.core.utils.StringUtils;
import org.macula.utils.excel.ExcelUtils;
import org.springframework.web.servlet.view.document.AbstractExcelView;

/**
 * <p>
 * <b>ExcelView</b> 生成Excel的VIEW
 * </p>
 * 
 * @since 2011-9-1
 * @author zhengping_wang
 * @version $Id: ExcelView.java 5906 2015-10-19 09:40:12Z wzp $
 */
public class ExcelView extends AbstractExcelView {

	private String fileName;

	public ExcelView(String template) {
		this.setUrl("classpath:views" + template);
		this.setApplicationContext(ApplicationContext.getContainer());
	}

	public ExcelView(String template, String fileName) {
		this(template);
		this.fileName = fileName;
	}

	@Override
	protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (StringUtils.isNotEmpty(fileName)) {
			String agent = request.getHeader("USER-AGENT");
			String finalFileName = processFileName(fileName, agent);
			response.setHeader("Content-Disposition", "attachment; filename=\"" + finalFileName + "\"");
		}
		ExcelUtils.parseWorkbook(model, workbook);
	}

	private String processFileName(String fileName, String agent) throws IOException {
		String codedfilename = null;
		if (null != agent && (-1 != agent.indexOf("MSIE") || (-1 != agent.indexOf("Trident")) || -1 != agent.indexOf("Edge"))) {
			String prefix = fileName.lastIndexOf(".") != -1 ? fileName.substring(0, fileName.lastIndexOf(".")) : fileName;
			String extension = fileName.lastIndexOf(".") != -1 ? fileName.substring(fileName.lastIndexOf(".")) : "";
			String name = java.net.URLEncoder.encode(prefix, "UTF8"); 
			if (name.lastIndexOf("%0A") != -1) {
				name = name.substring(0, name.length() - 3);
			}
			int limit = 150 - extension.length();
			if (name.length() > limit) {
				name = java.net.URLEncoder.encode(prefix.substring(0, Math.min(prefix.length(), limit / 9)), "UTF-8");
				if (name.lastIndexOf("%0A") != -1) {
					name = name.substring(0, name.length() - 3);
				}
			}
			codedfilename = name + extension;
		} else if (null != agent && -1 != agent.indexOf("Chrome")) {
			codedfilename = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
		} else if (null != agent && -1 != agent.indexOf("Safari")) {
			codedfilename = new String(fileName.getBytes("UTF-8"), "ISO8859-1") + "\"";
		} else if (null != agent && -1 != agent.indexOf("Mozilla")) {
			codedfilename = "=?UTF-8?B?" + (new String(org.apache.commons.codec.binary.Base64.encodeBase64(fileName.getBytes("UTF-8"))))
					+ "?=";
		} else {
			codedfilename = fileName;
		}
		return codedfilename;
	}
}
