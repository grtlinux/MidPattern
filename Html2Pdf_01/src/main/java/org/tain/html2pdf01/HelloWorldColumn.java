package org.tain.html2pdf01;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class HelloWorldColumn {

	private static boolean flag;
	private static String RESULT;

	static {
		flag = true;
		RESULT = "c:/hello_column.pdf";
	}

	public static void main(String[] args) throws DocumentException, IOException {
		if (flag) System.out.println(">>>>> " + new Object(){}.getClass().getEnclosingClass().getName());

		Document document = new Document();

		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));

		document.open();

		writer.setCompressionLevel(0);

		Phrase hello = new Phrase("Hello World !!!");
		PdfContentByte canvas = writer.getDirectContentUnder();
		ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, hello, 36, 788, 0);
		ColumnText.showTextAligned(canvas, Element.ALIGN_RIGHT, hello, 136, 688, 0);

		document.close();
	}
}
