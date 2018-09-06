package org.tain.html2pdf01;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class HelloWorld {

	private static final String RESULT = "C:/hello.pdf";

	public static void main(String[] args) throws DocumentException, IOException {
		new HelloWorld().createPdf(RESULT);
	}

	private void createPdf(String fileName) throws DocumentException, IOException {
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(fileName));
		document.open();
		document.add(new Paragraph("Hello, World!!!"));
		document.close();
	}
}
