package org.tain.cxf.fileTransfer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataHandler;
import javax.jws.WebService;

@WebService(targetNamespace = "http://fileTransfer.cxf.tain.org/", portName = "UploadServiceImplPort", serviceName = "UploadServiceImplService")
public class UploadServiceImpl implements UploadService {

	private static final boolean flag;

	private static final String BASE_PATH;
	private static final int SIZ_BUF;

	static {
		flag = true;
		BASE_PATH = "C:/hanwha/_CXF/Provider/";
		SIZ_BUF = 1024;
	}

	@Override
	public void uploadFile(FileUploader fileUploader) {
		if (flag) System.out.println(">>>>> " + new Object(){}.getClass().getEnclosingClass().getName());

		String fileName = fileUploader.getName() + "." + fileUploader.getFileType();

		DataHandler handler = null;
		InputStream is = null;
		OutputStream os = null;

		try {
			handler = fileUploader.getHandler();
			is = handler.getInputStream();
			os = new FileOutputStream(new File(BASE_PATH + fileName));

			byte[] buf = new byte[SIZ_BUF];
			int nRead = 0;
			while ((nRead = is.read(buf)) != -1) {
				os.write(buf, 0, nRead);
			}

			os.flush();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) try { os.close(); } catch (IOException e) {}
			if (is != null) try { is.close(); } catch (IOException e) {}
		}
	}
}
