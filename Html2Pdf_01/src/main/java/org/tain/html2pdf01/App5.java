package org.tain.html2pdf01;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

public class App5 {

	private static boolean flag;
	//private static String HTML_FILE;
	private static String PDF_FILE;
	private static String CSS_FILE;
	private static String FONT_FILE;

	static {
		flag = true;
		//HTML_FILE = "C:/hanwha/Html2Pdf/imsi.html";
		PDF_FILE = "C:/hanwha/Html2Pdf/imsi.pdf";
		CSS_FILE = "C:/hanwha/Html2Pdf/pdf.css";
		//FONT_FILE = "C:/hanwha/Html2Pdf/Classic+Xing+kai+Font.ttf";  // 한글
		//FONT_FILE = "C:/hanwha/Html2Pdf/YuryeoB.ttf";  // 한글
		//FONT_FILE = "C:/hanwha/Html2Pdf/designhouseLight.ttf";  // 한글
		//FONT_FILE = "C:/hanwha/Html2Pdf/Hangyeoregyeolche.ttf";  // 한글
		//FONT_FILE = "C:/hanwha/Html2Pdf/malgun.ttf";   // 한글
		//FONT_FILE = "C:/hanwha/Html2Pdf/NanumGothic-Regular.ttf";  // 한글
		//FONT_FILE = "C:/hanwha/Html2Pdf/또치_굴림체.ttf";   // 한글
		FONT_FILE = "C:/hanwha/Html2Pdf/GulimChe.ttf";  // 한글/한자
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

	@SuppressWarnings("static-access")
	private static void test01(String[] args) throws DocumentException, IOException {

		Document document = new Document(PageSize.A4, 50, 50, 50, 50);

		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(PDF_FILE));
		writer.setInitialLeading(12.5f);

		document.open();

		XMLWorkerHelper helper = XMLWorkerHelper.getInstance();

		// CSS
		CSSResolver cssResolver = new StyleAttrCSSResolver();
		CssFile cssFile = helper.getCSS(new FileInputStream(CSS_FILE));
		cssResolver.addCss(cssFile);

		// HTML, Font
		XMLWorkerFontProvider fontProvider = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
		fontProvider.register(FONT_FILE, "MalgunGothic");
		CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);

		// Html Context
		HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
		htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());

		// Pipeline
		PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
		HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
		CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);

		// XMLWorker
		XMLWorker worker = new XMLWorker(css, true);
		XMLParser xmlParser = new XMLParser(worker, Charset.forName("UTF-8"));

		// body style font is MalgunGothic
		String htmlString = "<html><head></head>"
				+ "<body style='font-family: MalgunGothic;'>"
				+ "<p>PDF 안에 들어갈 내용입니다.</p>"
				+ "<h3>한글, English, 漢字.</h3>"
				+ "<br/>"
				+ "<img src='ichigo.jpg' class='poster' />"
				+ "<table border='1'>"
				+ "<tr>"
				+ "    <td>안녕</td>"
				+ "    <td>세상아 !</td>"
				+ "</tr>"
				+ "<tr>"
				+ "    <td>Hello</td>"
				+ "    <td>World</td>"
				+ "</tr>"
				+ "</table>"
				+ "</body></html>";

		StringReader reader = new StringReader(htmlString);
		xmlParser.parse(reader);

		document.close();
		writer.close();
	}
}
