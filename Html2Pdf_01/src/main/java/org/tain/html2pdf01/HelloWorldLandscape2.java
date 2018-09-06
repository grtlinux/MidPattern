package org.tain.html2pdf01;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

public class HelloWorldLandscape2 {

	private static boolean flag;
	private static String RESULT;

	static {
		flag = true;
		RESULT = "c:/hello_landscape2.pdf";
	}

	public static void main(String[] args) throws DocumentException, IOException {
		if (flag) System.out.println(">>>>> " + new Object(){}.getClass().getEnclosingClass().getName());

		Document document = new Document(new Rectangle(792, 612));

		PdfWriter.getInstance(document, new FileOutputStream(RESULT));

		document.open();

		document.add(new Paragraph("Hello, world!!! landscape 2"));

		document.close();
	}
}
