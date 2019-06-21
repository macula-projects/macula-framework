/**
 * MatrixToImageWriterEx.java 2013-11-22
 */
package org.maculaframework.boot.utils.zxing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * <p>
 * <b>QRCodeUtils</b> 二维码生成助手
 * </p>
 *
 * @since 2013-11-22
 * @author zhengping_wang
 * @version $Id: QRCodeUtils.java 5584 2015-05-18 07:54:35Z wzp $
 */
public class QRCodeUtils {

	private static final QRCodeLogoConfig DEFAULT_CONFIG = new QRCodeLogoConfig();
	
	private static final int ADD_LOGO_MINI_WIDTH = 150;

	/** 
	 * 根据内容生成二维码数据 
	 * 
	 * @param content 二维码文字内容[为了信息安全性，一般都要先进行数据加密] 
	 * @param width 二维码照片宽度
	 * @param height 二维码照片高度
	 * @return BitMatrix 二维码点阵数组
	 * @throws WriterException 
	 */
	public static BitMatrix createQRCodeMatrix(String content, int width, int height) throws WriterException {
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		//设置字符编码  
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		// 指定纠错等级  
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.MARGIN, 1);
		BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
		return matrix;
	}

	/** 
	 * 写入二维码、以及将照片logo写入二维码中 
	 * 
	 * @param content 二维码文字内容[为了信息安全性，一般都要先进行数据加密] 
	 * @param width 二维码照片宽度
	 * @param height 二维码照片高度
	 * @param logoStream LOGO照片流
	 * @param config 二维码配置
	 * @return 二维码图片
	 */
	public static BufferedImage createQRCode(String content, int width, int height, InputStream logoStream, QRCodeLogoConfig config) throws IOException, WriterException {
		BitMatrix matrix = createQRCodeMatrix(content, width, height);
		BufferedImage image = null;
		
		// 如果二维码宽度大于100，则可以添加logo
		if (logoStream != null && width >= ADD_LOGO_MINI_WIDTH) {	
			
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			MatrixToImageWriter.writeToStream(matrix, "jpg", outStream);
			
			//添加logo图片, 此处一定需要重新进行读取，而不能直接使用二维码的BufferedImage 对象
			InputStream inputStream = new ByteArrayInputStream(outStream.toByteArray());
			image = ImageIO.read(inputStream);
			
			overlapImage(image, logoStream, config != null ? config : DEFAULT_CONFIG);			
		} else {
			image = MatrixToImageWriter.toBufferedImage(matrix);
		}
		
		return image;
	}
	
	/** 
	 * 写入二维码、以及将照片logo写入二维码中 
	 * 
	 * @param content 二维码文字内容[为了信息安全性，一般都要先进行数据加密] 
	 * @param width 二维码照片宽度
	 * @param height 二维码照片高度
	 * @param logoStream LOGO照片流
	 * @return 二维码图片
	 */
	public static BufferedImage createQRCode(String content, int width, int height, InputStream logoStream) throws IOException, WriterException {
		return createQRCode(content, width, height, logoStream, null);
	}
	
	/** 
	 * 根据内容生成二维码数据 
	 * 
	 * @param content 二维码文字内容[为了信息安全性，一般都要先进行数据加密] 
	 * @param width 二维码照片宽度
	 * @param height 二维码照片高度
	 * @return BufferedImage 二维码图片
	 */
	public static BufferedImage createQRCode(String content, int width, int height) throws IOException, WriterException {
		return createQRCode(content, width, height, null, null);
	}

	// 将照片logo添加到二维码中间 
	private static void overlapImage(BufferedImage image, InputStream logoStream, QRCodeLogoConfig logoConfig) throws IOException {
		Graphics2D g = image.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		
		// logo的长和宽，考虑到logo照片贴到二维码中，建议大小不要超过二维码的1/5;  
		int logoWidth = image.getWidth() / logoConfig.getLogoPart();
		int logoHeight = image.getHeight() / logoConfig.getLogoPart();
		
		// logo起始位置，此目的是为logo居中显示  
		int logoX = (image.getWidth() - logoWidth) / 2;
		int logoY = (image.getHeight() - logoHeight) / 2;
		
		// 阴影的宽度
		int shadow = image.getWidth() / 50;
		
		//给logo画边框  
		paintBorderShadow(g, shadow, logoX, logoY, logoWidth, logoHeight);	
		
		//绘制LOGO图  
		BufferedImage logo = ImageIO.read(logoStream);
		RoundRectangle2D clip = new RoundRectangle2D.Double(logoX, logoY, logoWidth, logoHeight, logoWidth / 4, logoHeight / 4);
		g.setClip(clip);	
		g.drawImage(logo, logoX, logoY, logoWidth, logoHeight, null);
		g.dispose();
	}
	
	// 颜色运算
	private static Color getMixedColor(Color c1, float pct1, Color c2, float pct2) {
		float[] clr1 = c1.getComponents(null);
		float[] clr2 = c2.getComponents(null);
		for (int i = 0; i < clr1.length; i++) {
			clr1[i] = (clr1[i] * pct1) + (clr2[i] * pct2);
		}
		return new Color(clr1[0], clr1[1], clr1[2], clr1[3]);
	}
	
	// 绘制阴影
	private static void paintBorderShadow(Graphics2D g2, int shadowWidth, int x, int y, int width, int height) {
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int sw = shadowWidth * 2;
		for (int i = sw; i >= 2; i -= 2) {
			float pct = (float) (sw - i) / (sw - 1);
			g2.setColor(getMixedColor(Color.LIGHT_GRAY, pct, Color.WHITE, 1.0f - pct));
			g2.setStroke(new BasicStroke(i));
			g2.drawRoundRect(x, y, width, height, width / 4, height / 4);
		}
	}

	public static void main(String[] args) {
		try {
			int width =300;
			int height = 300;
			String content = "http://www.taobao.com";

			QRCodeLogoConfig logoConfig = new QRCodeLogoConfig(Color.GRAY, 1, 4);
			InputStream logoStream = new FileInputStream("D:/infinitus_logo.jpg");
			BufferedImage image = QRCodeUtils.createQRCode(content, width, height, logoStream, logoConfig);
			
			//写入logo照片到二维码  
			ImageIO.write(image, "jpg", new File("D:/imgQRCode.jpg"));
			System.out.println("生成二维码结束！");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
