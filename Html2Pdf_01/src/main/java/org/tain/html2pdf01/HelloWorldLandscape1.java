package org.tain.html2pdf01;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class HelloWorldLandscape1 {

	private static boolean flag;
	private static String RESULT;

	static {
		flag = true;
		RESULT = "c:/hello_landscape1.pdf";
	}

	public static void main(String[] args) throws DocumentException, IOException {
		if (flag) System.out.println(">>>>> " + new Object(){}.getClass().getEnclosingClass().getName());

		Document document = new Document(PageSize.LETTER.rotate());

		PdfWriter.getInstance(document, new FileOutputStream(RESULT));

		document.open();

		document.add(new Paragraph("Hello, world!!! Landsape1"));

		document.close();
	}
}
