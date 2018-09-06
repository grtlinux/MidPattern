package org.tain.html2pdf01;

import java.io.IOException;

import com.itextpdf.text.DocumentException;

public class App4 {

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

	}
}
