package org.tain.html2pdf01;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.jsoup.Jsoup;
import com.itextpdf.html2pdf.jsoup.nodes.Document;
import com.itextpdf.html2pdf.jsoup.nodes.Element;
import com.itextpdf.html2pdf.jsoup.select.Elements;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfViewerPreferences;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.text.DocumentException;

/**
 * Hello world!
 *
 */
public class App1 {

	private static boolean flag;
	private static String HTML_FILE;
	private static String PDF_FILE;

	static {
		flag = true;
		HTML_FILE = "C:/hanwha/Html2Pdf/imsi.html";
		PDF_FILE = "C:/hanwha/Html2Pdf/imsi.pdf";
	}

	public static String getClassInfo() {
		final StackTraceElement e = Thread.currentThread().getStackTrace()[2];

		StringBuffer sb = new StringBuffer();

		if (flag) sb.append(e.getClassName()).append('.').append(e.getMethodName()).append("()");
		if (flag) sb.append(" - ");
		if (flag) sb.append(e.getFileName()).append('(').append(e.getLineNumber()).append(')');

		return sb.toString();
	}

	public static void main( String[] args ) throws DocumentException, IOException {
		if (flag) System.out.println(">>>>> " + getClassInfo());

		if (flag) test01(args);
    }

	/*
	 * HTML to PDF
	 */
	private static void test01(String[] args) throws DocumentException, IOException {
		String resources = "c:/hanwha/Html2Pdf";
		FileOutputStream outputStream = new FileOutputStream(PDF_FILE);

		WriterProperties writerProperties = new WriterProperties();
		PdfWriter pdfWriter = new PdfWriter(outputStream, writerProperties);
		PdfDocument pdfDoc = new PdfDocument(pdfWriter);
		pdfDoc.setTagged();
		pdfDoc.getCatalog().setViewerPreferences(new PdfViewerPreferences().setDisplayDocTitle(true));
		pdfDoc.setDefaultPageSize(PageSize.A4);

		PdfDocumentInfo pdfMetaData = pdfDoc.getDocumentInfo();
		pdfMetaData.setAuthor("Samel Huylebroeck");
		pdfMetaData.addCreationDate();
		pdfMetaData.getProducer();
		pdfMetaData.setCreator("iText Software");
		pdfMetaData.setKeywords("example, accessibility");
		pdfMetaData.setSubject("PDF accessibility");

		ConverterProperties props = new ConverterProperties();
		FontProvider fp = new FontProvider();
		fp.addStandardPdfFonts();
		fp.addDirectory(resources);  // the noto-nashk font file (.ttf extension) is placed in the resources directory.

		props.setFontProvider(fp);
		props.setBaseUri(resources);
		props.setCharset("UTF-8");

		props.setCreateAcroForm(true);
		props.setImmediateFlush(true);

		// Setup custom tagworker factory for better tagging of headers

		Document document = Jsoup.parse(new File(HTML_FILE), "UTF-8");

		// remove tab of hidden input
		Elements elements = document.getElementsByTag("input");
		for (Element el : elements) {
			if ("hidden".equalsIgnoreCase(el.attr("type"))) {
				el.remove();
			}
		}

		// apply TR height TD
		elements = document.getElementsByTag("tr");
		for (Element el : elements) {
			if (el.hasAttr("height")) {
				for (Element el1 : el.children()) {
					el1.attr("height", el.attr("height"));
				}
			}
			String style = el.attr("style").toLowerCase();
			if (!"height".contains(style)) {
				String[] keys = style.replace(" ", "").trim().split(";");
				if (keys != null) {
					for (String key : keys) {
						String[] value = key.split(":");
						if ("height".equalsIgnoreCase(value[0])) {
							for (Element el1 : el.children()) {
								el1.attr("height", value[1]);
							}
						}
					}
				}
			}
		}

		HtmlConverter.convertToPdf(document.toString(), pdfDoc, props);
		pdfDoc.close();
	}
}
