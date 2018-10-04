package hanwha.neo.branch.common.transferfile.ws;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.namespace.QName;

import hanwha.neo.branch.common.transferfile.ws.TransferFile;
import hanwha.neo.branch.common.transferfile.ws.TransferFileService;
import hanwha.neo.branch.common.transferfile.ws.TransferFileServiceImplService;
import hanwha.neo.commons.Module;


/**
 * This class was generated by Apache CXF 2.7.15
 * 2018-02-13T19:05:54.469+09:00
 * Generated source version: 2.7.15
 *
 */
public final class WsDownloadUtil {

	private static boolean flag;

	private static String baseDir;
	private static String baseDirGroup;
	private static String fileSeperator;

	static {
		flag = true;

		if (flag) {
			baseDir = "/neo_data";
			baseDirGroup = "/neo_data_eo";
			fileSeperator = "/";
		}
	}

    private static final QName SERVICE_NAME = new QName("http://ws.transferfile.common.branch.neo.hanwha/", "TransferFileServiceImplService");

    private WsDownloadUtil() {}

    public static String wsUpload(String wsdlUrl, String uploadFilePath) throws java.lang.Exception {
    	String retVal = "SUCCESS";
    	String fileName = "i dont know.";  // 이 후 필요하면 메소드 당형성 이용하여 구현 필요
    	TransferFileService port = getWsTransferFileService(wsdlUrl);

    	try{
    		String filePath = "";
    		// if DB 의 경로 사용
    		if(uploadFilePath.contains(baseDir)) {
    			filePath = uploadFilePath.replace(baseDir + fileSeperator , "");
    			fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
        	}

    		File file = new File(uploadFilePath);
    		javax.activation.DataHandler _upload_transferFILEDataHandler = new DataHandler(new FileDataSource(file));

    		hanwha.neo.branch.common.transferfile.ws.TransferFile _upload_transferFILE = new hanwha.neo.branch.common.transferfile.ws.TransferFile();
    		_upload_transferFILE.setDataHandler(_upload_transferFILEDataHandler);
    		_upload_transferFILE.setFileName(fileName);
    		_upload_transferFILE.setFilePath(filePath);
    		_upload_transferFILE.setFileSize("" + file.length());

    		java.lang.String _upload__return = port.upload(_upload_transferFILE);  // UPLOAD

    		retVal = _upload__return;
    		System.out.println("upload.result = " + _upload__return);
    	} catch(Exception e){
			e.printStackTrace();
    		retVal = "FAIL";
    	}

        return retVal;
    }

    public static boolean wsDownload(String wsdlUrl, String downloadFilePath) throws java.lang.Exception {
    	boolean retVal = true;

    	try{
	    	TransferFileService port = getWsTransferFileService(wsdlUrl);

	        {
	        	System.out.println("Invoking download...");

	        	String _download_filePATH = downloadFilePath;

	        	hanwha.neo.branch.common.transferfile.ws.TransferFile _download__return = port.download(_download_filePATH);

	        	// if DB 의 경로 사용
	        	_download_filePATH = baseDir + fileSeperator + _download_filePATH;

	        	_download__return.setFilePath(_download_filePATH);

	        	WsDownloadUtil.saveAttach(_download__return);

	        	// size
	        	System.out.println("download.result=" + _download__return);
	        }

	        retVal = true;
    	}catch(Exception e){
			e.printStackTrace();
    		retVal = false;
    	}

    	return retVal;
    }

    public static boolean wsDownload(String wsdlUrl, String downloadFilePath, boolean isNotGroup) throws java.lang.Exception {
    	boolean retVal = true;

    	try{
	    	TransferFileService port = getWsTransferFileService(wsdlUrl);

	        {
	        	System.out.println("Invoking download...");

	        	String _download_filePATH = downloadFilePath;

	        	hanwha.neo.branch.common.transferfile.ws.TransferFile _download__return = port.download(_download_filePATH);

	        	// if DB 의 경로 사용
	        	if(isNotGroup){
	        		_download_filePATH = Module.DIR_BASE.getAbsolutePath() + File.separator + _download_filePATH;
	        	}else{
	        		_download_filePATH = Module.DIR_BASE_GROUP.getAbsolutePath() + File.separator + _download_filePATH;
	        	}

	        	_download__return.setFilePath(_download_filePATH);
	        	WsDownloadUtil.saveAttach(_download__return);

	        	// size
	        	System.out.println("download.result=" + _download__return);
	        }

	        retVal = true;
    	}catch(Exception e){
			e.printStackTrace();
    		retVal = false;
    	}

    	return retVal;
    }

    private static TransferFileService getWsTransferFileService(String wsdlUrl) throws MalformedURLException {
		URL wsdlURL = TransferFileServiceImplService.WSDL_LOCATION;
		//wsdlURL = new URL("http://127.0.0.1:8080/neo/ws/common/transferFile?wsdl");
		//wsdlURL = new URL("http://localhost:8080/TestServerMTOM/services/TransferFileServiceImplPort?wsdl");
		if(wsdlUrl == null || "".equals(wsdlUrl)){
			wsdlUrl = "file:/neo_src/app/neo/WEB-INF/wsdl/transferFile.wsdl";
		}
		wsdlURL = new URL(wsdlUrl);

		TransferFileServiceImplService ss = new TransferFileServiceImplService(wsdlURL, SERVICE_NAME);
		TransferFileService port = ss.getTransferFileServiceImplPort();
		return port;
	}

    static void saveAttach(TransferFile attach) throws Exception {
		saveFile(attach.getFilePath(), attach.getDataHandler());
	}

    static void saveFile(String filePath, DataHandler dh) throws Exception {
		File file = new File(filePath);

		if (file.exists())
			return;

		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		OutputStream bos = null;
		BufferedInputStream bis = null;
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			is = dh.getInputStream();
			bis = new BufferedInputStream(is);

			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			int i = 0;
			while ((i = bis.read()) != -1) {
				bos.write(i);
			}
			bos.flush();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (is  != null) try { is.close();  } catch (IOException e) { throw e; }
			if (bis != null) try { bis.close(); } catch (IOException e) { throw e; }
			if (fos != null) try { fos.close(); } catch (IOException e) { throw e; }
			if (bos != null) try { bos.close(); } catch (IOException e) { throw e; }
		}
	}

    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////

	public static void main(String args[]) throws java.lang.Exception {
		String wsdl = null;
		wsdl = "http://127.0.0.1:8080/neo/ws/common/transferFile?wsdl";
		wsdl = "http://localhost:8080/TestServerMTOM/services/TransferFileServiceImplPort?wsdl";
		wsdl = "http://hanwha.eagleoffice.co.kr/neo/ws/common/transferFile?wsdl";

		if (!flag) {
			// upload
			QName SERVICE_NAME = new QName("http://ws.transferfile.common.branch.neo.hanwha/", "TransferFileServiceImplService");
			// URL wsdlURL = new URL("http://hanwha.eagleoffice.co.kr/neo/ws/common/transferFile?wsdl");
			URL wsdlURL = new URL(wsdl);

			TransferFileServiceImplService service = new TransferFileServiceImplService(wsdlURL, SERVICE_NAME);
			TransferFileService port = service.getTransferFileServiceImplPort();

			File file = new File("C:/hanwha/_CXF/Consumer/Consumer.pptx");
			DataHandler dataHandler = new DataHandler(new FileDataSource(file));
			TransferFile transferFile = new TransferFile();
			transferFile.setDataHandler(dataHandler);

			String fileName = "Consumer.pptx";
			String filePath = "301/bbs/document/2018/9/20/15/Consumer.pptx";
			String fileSize = "1234567";  // String.valueOf(file.length());
			transferFile.setFileName(fileName);
			transferFile.setFilePath(filePath);
			transferFile.setFileSize(fileSize);

			String retMsg = port.upload(transferFile);
			System.out.println(">>>>> " + retMsg);
		}

		if (!flag) {
			String filePath = "/neo_data/301/bbs/document/2018/9/20/15/Consumer.pptx";
			File file = new File(filePath);
    		System.out.println(">> " + file.getAbsolutePath());
    		System.out.println(">> " + file.getCanonicalPath());
    		System.out.println(">> " + file.getName());
    		System.out.println(">> " + file.getParent());
    		System.out.println(">> " + file.toString());
    		System.out.println(">> " + file.getPath().replace('\\', '/'));
		}

		if (!flag) {
			String filePath = "/neo_data/301/bbs/document/2018/9/20/15/Consumer.pptx";
			String filePath2 = "";
			String fileName = "";
			File file = new File(filePath);
			System.out.println(">>>>> length = " + file.length());

    		if (filePath.contains(baseDir)) {
    			filePath2 = filePath.replace(baseDir + fileSeperator , "");
        	}

    		fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
    		System.out.println(">>>>> baseDir = " + baseDir);
    		System.out.println(">>>>> filePath2 = " + filePath2);
    		System.out.println(">>>>> fileName = " + fileName);
		}

		if (!flag) {
			System.out.printf(">>>>> Module.DIR_BASE = %s%n", Module.DIR_BASE.getAbsoluteFile());
			System.out.printf(">>>>> Module.DIR_BASE_GROUP = %s%n", Module.DIR_BASE_GROUP.getAbsoluteFile());
		}

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
