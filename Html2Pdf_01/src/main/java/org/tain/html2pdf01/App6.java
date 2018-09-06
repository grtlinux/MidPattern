package org.tain.html2pdf01;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

public class App6 {

	private static boolean flag;
	//private static String HTML_FILE;
	private static String PDF_FILE;
	//private static String CSS_FILE;
	private static String FONT_FILE;

	static {
		flag = true;
		//HTML_FILE = "C:/hanwha/Html2Pdf/imsi.html";
		PDF_FILE = "C:/hanwha/Html2Pdf/imsi.pdf";
		//CSS_FILE = "C:/hanwha/Html2Pdf/pdf.css";
		//FONT_FILE = "C:/hanwha/Html2Pdf/Classic+Xing+kai+Font.ttf";  // 한글
		//FONT_FILE = "C:/hanwha/Html2Pdf/YuryeoB.ttf";  // 한글
		//FONT_FILE = "C:/hanwha/Html2Pdf/designhouseLight.ttf";  // 한글
		//FONT_FILE = "C:/hanwha/Html2Pdf/Hangyeoregyeolche.ttf";  // 한글
		//FONT_FILE = "C:/hanwha/Html2Pdf/malgun.ttf";   // 한글
		//FONT_FILE = "C:/hanwha/Html2Pdf/NanumGothic-Regular.ttf";  // 한글
		//FONT_FILE = "C:/hanwha/Html2Pdf/또치_굴림체.ttf";   // 한글
		FONT_FILE = "C:/hanwha/Html2Pdf/GulimChe.ttf";  // 한글/한자
	}

	public static String getClassInfo() {
		final StackTraceElement e = Thread.currentThread().getStackTrace()[2];

		StringBuffer sb = new StringBuffer();

		if (flag) sb.append(e.getClassName()).append('.').append(e.getMethodName()).append("()");
		if (flag) sb.append(" - ");
		if (flag) sb.append(e.getFileName()).append('(').append(e.getLineNumber()).append(')');

		return sb.toString();
	}

	public static void main( String[] args ) throws DocumentException, IOException {
		if (flag) System.out.println(">>>>> " + getClassInfo());

		if (!flag) test01(args);
		if (flag) test02(args);
    }

	private static void test01(String[] args) throws DocumentException, IOException {

		Rectangle pageSize = new Rectangle(344, 720);
		pageSize.setBackgroundColor(new BaseColor(0xFf, 0xFF, 0xDE));

		BaseFont bfont = BaseFont.createFont(FONT_FILE, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		Font font = new Font(bfont, 12);

		Document document = new Document(pageSize);
		PdfWriter.getInstance(document, new FileOutputStream(PDF_FILE));

		document.open();

		document.add(new Paragraph("Hello, World!! 안녕하세요.", font));

		document.close();
	}

	private static void test02(String[] args) throws DocumentException, IOException {

		Document document = new Document(PageSize.A4, 50, 50, 50, 50);
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(PDF_FILE));

		document.open();

		BaseFont bfont = BaseFont.createFont(FONT_FILE, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		Font font = new Font(bfont, 12);

		document.add(new Paragraph("First Page", font));
		document.add(new Paragraph("한글입니다.", font));

		document.close();
		writer.close();
	}
}
