package hanwha.test.ws;

import hanwha.neo.branch.common.transferfile.ws.WsDownloadUtil;

public class WsDownloadTestMain {

	private static final boolean flag;

	static {
		flag = true;
	}

	///////////////////////////////////////////////////////////////////////////

	public static void main(String[] args) throws Exception {
		if (flag) System.out.println(">>>>> " + new Object(){}.getClass().getEnclosingClass().getName());

		if (flag) test01(args);
	}

	private static void test01(String[] args) throws Exception {

		String wsdl;
		wsdl = "http://hanwha.eagleoffice.co.kr/neo/ws/common/transferFile?wsdl";
		//wsdl = "http://172.16.223.140/neo/ws/common/transferFile?wsdl";

		if (flag) {
			// upload
			String uploadFilePath = "/neo_data/301/bbs/document/2018/10/4/9/Consumer.pptx";
			String returnString = WsDownloadUtil.wsUpload(wsdl, uploadFilePath);
			System.out.println(">>>>> " + returnString);
		}

		if (flag) try { Thread.sleep(2000); } catch (InterruptedException e) {}

		if (flag) {
			// download
			//String downloadFilePath = "/neo_data_eo/301/bbs/document/2018/9/20/15/Consumer.pptx";
			String downloadFilePath = "301/bbs/document/2018/10/4/9/Consumer.pptx";
			WsDownloadUtil.wsDownload(wsdl, downloadFilePath);
		}
	}
}
