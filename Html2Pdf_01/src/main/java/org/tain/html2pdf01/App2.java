package org.tain.html2pdf01;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.html.simpleparser.StyleSheet;
import com.itextpdf.text.pdf.PdfWriter;

public class App2 {

	private static boolean flag;
	private static String HTML_FILE;
	private static String PDF_FILE;

	static {
		flag = true;
		HTML_FILE = "C:/hanwha/Html2Pdf/imsi.html";
		PDF_FILE = "C:/hanwha/Html2Pdf/imsi.pdf";
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

		if (flag) test01(args);
    }

	private static void test01(String[] args) throws DocumentException, IOException {
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(PDF_FILE));
		
		document.open();
		
		StyleSheet styles = new StyleSheet();
		styles.loadStyle("imdb", "size", "-e");
		
		HTMLWorker htmlWorker = new HTMLWorker(document, null, styles);
		Map<String, Object> providers = new HashMap<String, Object>();
		//providers.put(HTMLWorker.IMG_PROVIDER, new MyImageFactory());
		htmlWorker.setProviders(providers);
		htmlWorker.parse(new FileReader(HTML_FILE));
		
		document.close();
	}
}
