package org.tain.html2pdf01;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

//import com.itextpdf.kernel.xmp.impl.XMPSerializerHelper;
//import com.itextpdf.xmp.impl.XMPSerializerHelper;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

public class App3 {

	private static boolean flag;
	private static String HTML_FILE;
	private static String PDF_FILE;
	private static String IMG_PATH;

	static {
		flag = true;
		HTML_FILE = "C:/hanwha/Html2Pdf/imsi.html";
		PDF_FILE = "C:/hanwha/Html2Pdf/imsi.pdf";
		IMG_PATH = "C:/hanwha/Html2Pdf";
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

		if (!flag) test01(args);
		if (flag) test02(args);
    }

	private static void test01(String[] args) throws DocumentException, IOException {

		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(PDF_FILE));
		document.open();
		XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream(HTML_FILE));
		document.close();
	}

	private static void test02(String[] args) throws DocumentException, IOException {

		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(PDF_FILE));

		document.open();

		CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
		HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
		htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
		htmlContext.setImageProvider(new AbstractImageProvider() {
			public String getImageRootPath() {
				return IMG_PATH;
			}
		});

		PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
		HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
		CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);

		XMLWorker worker = new XMLWorker(css, true);
		XMLParser p = new XMLParser(worker);
		p.parse(new FileInputStream(HTML_FILE));

		document.close();
	}
}