package hanwha.neo.branch.common.transferfile.client;

import java.io.File;
import java.net.URL;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.namespace.QName;

import hanwha.neo.branch.common.transferfile.ws.TransferFile;
import hanwha.neo.branch.common.transferfile.ws.TransferFileService;
import hanwha.neo.branch.common.transferfile.ws.TransferFileServiceImplService;

public class TransferFileClient {

	private static boolean flag;

	private static QName SERVICE_NAME;
	private static URL wsdlURL;

	static {
		flag = true;
		SERVICE_NAME = new QName("http://ws.transferfile.common.branch.neo.hanwha/", "TransferFileServiceImplService");
		try {
			wsdlURL = new URL("http://hanwha.eagleoffice.co.kr/neo/ws/common/transferFile?wsdl");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static void main(String[] args) {

		TransferFileServiceImplService service = new TransferFileServiceImplService(wsdlURL, SERVICE_NAME);
		TransferFileService port = service.getTransferFileServiceImplPort();

		if (flag) {
			File file = new File("C:/hanwha/_CXF/Consumer/Consumer.pptx");
			DataHandler dataHandler = new DataHandler(new FileDataSource(file));
			TransferFile transferFile = new TransferFile();
			transferFile.setDataHandler(dataHandler);

			String fileName = "b9419cc3ac10df7c0e4ebc97b2038f09.mht";
			String filePath = "301/bbs/document/2018/9/18/16/b9419cc3ac10df7c0e4ebc97b2038f09.mht";
			String fileSize = "1234567";  // String.valueOf(file.length());
			transferFile.setFileName(fileName);
			transferFile.setFilePath(filePath);
			transferFile.setFileSize(fileSize);

			String retMsg = port.upload(transferFile);
			System.out.println(">>>>> " + retMsg);

		}
	}
}