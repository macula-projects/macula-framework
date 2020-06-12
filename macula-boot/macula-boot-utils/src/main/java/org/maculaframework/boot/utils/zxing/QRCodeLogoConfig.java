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

/**
 * MatrixToLogoImageConfig.java 2013-11-22
 */
package org.maculaframework.boot.utils.zxing;

import java.awt.*;

/**
 * <p>
 * <b>MatrixToLogoImageConfig</b> is
 * </p>
 *
 * @since 2013-11-22
 * @author zhengping_wang
 * @version $Id: QRCodeLogoConfig.java 5584 2015-05-18 07:54:35Z wzp $
 */
public class QRCodeLogoConfig {
	//logo默认边框颜色  
	public static final Color DEFAULT_BORDERCOLOR = Color.GRAY;
	//logo默认边框宽度  
	public static final int DEFAULT_BORDER = 1;
	//logo大小默认为照片的1/5  
	public static final int DEFAULT_LOGOPART = 4;

	private final int border;
	private final Color borderColor;
	private final int logoPart;

	/** 
	 * Creates a default config with on color #BLACK and off color 
	 * #WHITE, generating normal black-on-white barcodes. 
	 */
	public QRCodeLogoConfig() {
		this(DEFAULT_BORDERCOLOR, DEFAULT_BORDER, DEFAULT_LOGOPART);
	}

	public QRCodeLogoConfig(Color borderColor, int border, int logoPart) {
		this.borderColor = borderColor;
		this.border = border;
		this.logoPart = logoPart;
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public int getBorder() {
		return border;
	}

	public int getLogoPart() {
		return logoPart;
	}
}
