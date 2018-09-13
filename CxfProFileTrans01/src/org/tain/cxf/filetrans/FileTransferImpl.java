package org.tain.cxf.filetrans;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.jws.WebService;
import javax.xml.ws.WebServiceException;

@WebService(targetNamespace = "http://filetrans.cxf.tain.org/", portName = "FileTransferImplPort", serviceName = "FileTransferImplService")
public class FileTransferImpl implements FileTransfer {

	private static final boolean flag;
	private static final String PROVIDER_BASE_PATH;

	static {
		flag = true;
		PROVIDER_BASE_PATH = "C:/hanwha/_CXF/Provider/";

		if (flag) System.out.println(">>>>> " + new Object(){}.getClass().getEnclosingClass().getName());
	}

	@Override
	public void upload(String fileName, byte[] bytes) {
		String filePath = PROVIDER_BASE_PATH + fileName;

		FileOutputStream fos = null;
		BufferedOutputStream bos = null;

		try {
			fos = new FileOutputStream(filePath);
			bos = new BufferedOutputStream(fos);
			bos.write(bytes);
			bos.flush();

			if (flag) System.out.println(">>>>> Received file : " + filePath);
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebServiceException(e);
		} finally {
			if (bos != null) try { bos.close(); } catch (Exception e) {}
			if (fos != null) try { fos.close(); } catch (Exception e) {}
		}
	}

	@Override
	public byte[] download(String fileName) {
		String filePath = PROVIDER_BASE_PATH + fileName;
		if (flag) System.out.println(">>>>> Sending file : " + filePath);

		FileInputStream fis = null;
		BufferedInputStream bis = null;
		byte[] bytes = null;

		try {
			File file = new File(filePath);
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			bytes = new byte[(int) file.length()];
			bis.read(bytes);
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebServiceException(e);
		} finally {
			if (bis != null) try { bis.close(); } catch (Exception e) {}
			if (fis != null) try { fis.close(); } catch (Exception e) {}
		}

		return bytes;
	}

}
