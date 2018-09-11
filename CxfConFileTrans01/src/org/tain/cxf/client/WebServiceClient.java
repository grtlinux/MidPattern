package org.tain.cxf.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.tain.cxf.filetrans.FileTransferImpl;
import org.tain.cxf.filetrans.FileTransferImplService;

public class WebServiceClient {

	private static final boolean flag;
	private static final String CONSUMER_BASE_PATH;
	static {
		flag = true;
		CONSUMER_BASE_PATH = "C:/hanwha/_CXF/Consumer/";

		if (flag) System.out.println(">>>>> " + new Object(){}.getClass().getEnclosingClass().getName());
	}

	//////////////////////////////////////////////////////

	public static void main(String[] args) throws Exception {
		// connect to the web service
		FileTransferImplService client = new FileTransferImplService();
		FileTransferImpl service = client.getFileTransferImplPort();

		if (flag) {
			// upload
			String fileName = "Consumer.pptx";
			File file = new File(CONSUMER_BASE_PATH + fileName);
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			byte[] bytes = null;

			try {
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				bytes = new byte[(int)file.length()];
				bis.read(bytes);

				service.upload(fileName, bytes);

				if (flag) System.out.println(">>>>> File uploaded: " + file.getCanonicalPath());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (bis != null) try { bis.close(); } catch (Exception e) {}
				if (fis != null) try { fis.close(); } catch (Exception e) {}
			}
		}

		if (flag) {
			// download
			String fileName = "Provider.pptx";
			byte[] bytes = null;

			bytes = service.download(fileName);

			FileOutputStream fos = null;
			BufferedOutputStream bos = null;

			try {
				String filePath = CONSUMER_BASE_PATH + fileName;
				fos = new FileOutputStream(filePath);
				bos = new BufferedOutputStream(fos);
				bos.write(bytes);

				if (flag) System.out.println(">>>>> File download: " + filePath);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (bos != null) try { bos.close(); } catch (Exception e) {}
				if (fos != null) try { fos.close(); } catch (Exception e) {}
			}
		}
	}
}
