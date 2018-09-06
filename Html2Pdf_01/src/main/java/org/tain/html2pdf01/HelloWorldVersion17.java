package org.tain.html2pdf01;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class HelloWorldVersion17 {

	private static boolean flag;
	private static String RESULT;

	static {
		flag = true;
		RESULT = "c:/hello_version17.pdf";
	}

	public static void main(String[] args) throws DocumentException, IOException {
		if (flag) System.out.println(">>>>> " + new Object(){}.getClass().getEnclosingClass().getName());

		Document document = new Document();

		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
		writer.setPdfVersion(PdfWriter.VERSION_1_7);

		document.open();

		document.add(new Paragraph("Hello, world.!!!"));

		document.close();
	}
}
