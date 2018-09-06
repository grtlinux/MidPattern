package org.tain.html2pdf01;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class HelloWorldLetter {

	private static boolean flag;
	private static String RESULT;

	static {
		flag = true;
		RESULT = "C:/hello_letter.pdf";
	}

	public static void main(String[] args) throws DocumentException, IOException {
		if (flag) System.out.println(">>>>> " + new Object(){}.getClass().getEnclosingClass().getName());

		Document document = new Document(PageSize.LETTER);

		PdfWriter.getInstance(document, new FileOutputStream(RESULT));

		document.open();

		document.add(new Paragraph("Hello, world!!! Letter"));

		document.close();
	}
}
