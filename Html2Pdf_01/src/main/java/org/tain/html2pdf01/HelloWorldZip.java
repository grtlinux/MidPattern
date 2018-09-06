package org.tain.html2pdf01;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class HelloWorldZip {

	private static boolean flag;
	private static String RESULT;

	static {
		flag = true;
		RESULT = "c:/hello_zip.zip";
	}

	public static void main(String[] args) throws DocumentException, IOException {
		if (flag) System.out.println(">>>>> " + new Object(){}.getClass().getEnclosingClass().getName());

		ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(RESULT));

		for (int i=1; i <= 3; i++) {
			ZipEntry entry = new ZipEntry("hello_" + i + ".pdf");
			zip.putNextEntry(entry);

			Document document = new Document();

			PdfWriter writer = PdfWriter.getInstance(document, zip);
			writer.setCloseStream(false);

			document.open();

			document.add(new Paragraph("Hello " + i));

			document.close();
		}

		zip.close();
	}
}
