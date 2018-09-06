package org.tain.html2pdf01;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class HelloWorldMirroredMarginsTop {

	private static boolean flag;
	private static String RESULT;

	static {
		flag = true;
		RESULT = "c:/hello_mirroredMarginsTop.pdf";
	}

	public static void main(String[] args) throws DocumentException, IOException {
		if (flag) System.out.println(">>>>> " + new Object(){}.getClass().getEnclosingClass().getName());

		Document document = new Document();

		PdfWriter.getInstance(document, new FileOutputStream(RESULT));

		document.setPageSize(PageSize.A4);
		document.setMargins(36, 72, 108, 180);
		document.setMarginMirroringTopBottom(true);

		document.open();

		document.add(new Paragraph(
				"the left margin of this odd page is 36pt (0.5 inch); "
				+ "the right margin 72pt (1 inch); "
				+ "the top margin 108pt (1.5 inch); "
				+ "the bottom margin 180pt (2.5 inch)." ));

		Paragraph paragraph = new Paragraph();
		paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
		for (int i=0; i < 20; i++) {
			paragraph.add("Hello world! hello People! Hello Sky!...");
		}
		document.add(paragraph);
		document.add(new Paragraph("The top margin 180pt (2.5inch)."));

		document.close();
	}
}
