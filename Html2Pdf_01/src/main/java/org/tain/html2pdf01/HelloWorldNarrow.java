package org.tain.html2pdf01;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

public class HelloWorldNarrow {

	private static boolean flag;
	private static String RESULT;

	static {
		// initialize parameters
		flag = true;
		RESULT = "C:/hello_narrow.pdf";
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

		Rectangle pagesize = new Rectangle(216f, 720f);
		Document document = new Document(pagesize, 36f, 72f, 108f, 180f);

		PdfWriter.getInstance(document, new FileOutputStream(RESULT));

		document.open();

		document.add(new Paragraph(
				"Hello World! Hello People! "
				+ "Hello Sky! Hello Sun! Hello Moon! Hello Starts!!"));

		document.close();
	}
}
