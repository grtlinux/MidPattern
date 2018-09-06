package org.tain.html2pdf01;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

public class HelloWorldMaximum {

	private static boolean flag;
	private static String RESULT;
	static {
		flag = true;
		RESULT = "c:/hello_maximum.pdf";
	}

	public static String getClassInfo() {
		final StackTraceElement e = Thread.currentThread().getStackTrace()[2];

		StringBuffer sb = new StringBuffer();

		if (flag) sb.append(e.getClassName()).append('.').append(e.getMethodName()).append("()");
		if (flag) sb.append(" - ");
		if (flag) sb.append(e.getFileName()).append('(').append(e.getLineNumber()).append(')');

		return sb.toString();
	}

	public static void main(String[] args) throws DocumentException, IOException {
		if (flag) System.out.println(">>>>> " + getClassInfo());

		Document document = new Document(new Rectangle(14400, 14400));
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
		writer.setUserunit(75000f);

		document.open();

		document.add(new Paragraph("Hello, World!!! Maximum...."));

		document.close();
	}
}
