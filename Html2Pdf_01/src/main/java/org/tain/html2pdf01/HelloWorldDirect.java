package org.tain.html2pdf01;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class HelloWorldDirect {

	private static boolean flag;
	private static String RESULT;

	static {
		flag = true;
		RESULT = "c:/hello_direct.pdf";
	}

	public static void main(String[] args) throws DocumentException, IOException {
		if (flag) System.out.println(">>>>> " + new Object(){}.getClass().getEnclosingClass().getName());

		Document document = new Document();

		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));

		document.open();

		PdfContentByte canvas = writer.getDirectContentUnder();

		writer.setCompressionLevel(0);
		canvas.saveState();
		canvas.beginText();
		canvas.moveText(236, 788);
		canvas.setFontAndSize(BaseFont.createFont(), 12);
		canvas.showText("Hello, World...");
		canvas.endText();
		canvas.restoreState();

		document.close();
	}
}
