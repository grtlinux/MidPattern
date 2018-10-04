package hanwha.test.ws;

import java.io.File;
import java.nio.file.Files;

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
		String file = "201/bbs/attaches/2018/10/4/11/729AA911-E182-06E6-E0CE-8DA2F1B346D2.file";

		if (flag) {
			// file copy
			System.out.println(">>>>> starting file copy....");
			String src = "C:/hanwha/_CXF/테스트 파일.zip";  // 9mb
			String dst = "/neo_data/" + file;

			Files.copy(new File(src).toPath(), new File(dst).toPath());

			System.out.println(">>>>> end file copy....");
		}

		if (flag) {
			// upload: local file -> group file
			System.out.println(">>>>> starting upload!");
			String uploadFilePath = "/neo_data/" + file;
			String returnString = WsDownloadUtil.wsUpload(wsdl, uploadFilePath);
			//String returnString = WsDownloadUtil.wsUpload(uploadFilePath);
			System.out.println(">>>>> " + returnString);
			System.out.println(">>>>> end upload!");
		}

		if (flag) {
			// waiting
			System.out.println(">>>>> sleep time 10 seconds...");
			try { Thread.sleep(10000); } catch (InterruptedException e) {}
		}

		if (flag) {
			// file delete: local file
			String uploadFilePath = "/neo_data/" + file;
			File delFile = new File(uploadFilePath);
			if (delFile.exists()) {
				if (delFile.delete()) {
					System.out.println(">>>>> success to delete file ....." + uploadFilePath);
				} else {
					System.out.println(">>>>> fail to delete file....." + uploadFilePath);
				}
			} else {
				System.out.println(">>>>> file dosn't exist....." + uploadFilePath);
			}
		}

		if (flag) {
			// waiting
			System.out.println(">>>>> sleep time 10 seconds...");
			try { Thread.sleep(10000); } catch (InterruptedException e) {}
		}

		if (flag) {
			// download
			System.out.println(">>>>> starting download!");
			//String downloadFilePath = "/neo_data_eo/301/bbs/document/2018/9/20/15/Consumer.pptx";
			String downloadFilePath = "201/bbs/attaches/2018/10/4/11/729AA911-E182-06E6-E0CE-8DA2F1B346D2.file";
			WsDownloadUtil.wsDownload(wsdl, downloadFilePath);
			//WsDownloadUtil.wsDownload(downloadFilePath);
			System.out.println(">>>>> end download!");
		}
	}
}
