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

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * <p>
 * <b>ITag </b> is a interface which define the tag
 * </p>
 * 
 * @author rainsoft
 * @version $Revision: 5906 $ $Date: 2015-10-19 17:40:12 +0800 (Mon, 19 Oct 2015) $
 */
public interface ITag {

  /**
   * parse the tag
   * 
   * @param context data object
   * @param wb excel workbook
   * @param sheet excel sheet
   * @param curRow excel row
   * @param curCell excel cell
   * @return int[] {skip number, shift number, break flag}
   */
  public int[] parseTag(Object context, HSSFWorkbook wb, HSSFSheet sheet, HSSFRow curRow, HSSFCell curCell);

  /**
   * tag has #end flag
   * 
   * @return boolean
   */
  public boolean hasEndTag();

  /**
   * get the tag name
   * 
   * @return str
   */
  public String getTagName();
}
