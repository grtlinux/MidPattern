package hanwha.neo.branch.common.transferfile.ws;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataHandler;
import javax.jws.WebService;

@WebService(targetNamespace = "http://ws.transferfile.common.branch.neo.hanwha/", portName = "TransferFileServiceImplPort", serviceName = "TransferFileServiceImplService")
public class TransferFileServiceImpl implements TransferFileService {

	private static final boolean flag = true;

	//@XmlElement(required = true)
	//@XmlMimeType("application/octet-stream")
	@Override
	public String upload(TransferFile transferFile) {
		// TODO Auto-generated method stub
		String retMsg = "SUCCESS!!!";

		if (flag) {
			String fileName = transferFile.getFileName();  // server side
			String filePath = transferFile.getFilePath();  // server side
			@SuppressWarnings("unused")
			String fileSize = transferFile.getFileSize();

			DataHandler dataHandler = transferFile.getDataHandler();

			InputStream is = null;
			OutputStream os = null;

			try {
				is = dataHandler.getInputStream();
				os = new FileOutputStream(new File(filePath + fileName));

				byte[] bytes = new byte[1024];
				int nRead;
				while ((nRead = is.read(bytes)) != -1) {
					os.write(bytes, 0, nRead);
				}

			} catch (Exception e) {
				e.printStackTrace();
				retMsg = "FAIL to transfer file....";
			} finally {
				if (is != null) try { is.close(); } catch (Exception e) {}
				if (os != null) try { os.close(); } catch (Exception e) {}
			}
		}

		return retMsg;
	}

}
