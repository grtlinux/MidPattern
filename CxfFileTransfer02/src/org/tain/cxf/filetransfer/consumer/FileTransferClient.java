package org.tain.cxf.filetransfer.consumer;

import java.io.File;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

public class FileTransferClient {

	private static final boolean flag;

	static {
		flag = true;
	}

	///////////////////////////////////////////////////////////

	public static void main(String[] args) throws Exception {
		if (flag) System.out.println(">>>>> " + new Object(){}.getClass().getEnclosingClass().getName());

		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.getInInterceptors().add(new LoggingInInterceptor());
		factory.getOutInterceptors().add(new LoggingOutInterceptor());
		factory.setServiceClass(UploadServiceImpl.class);
		factory.setAddress("http://localhost:8080/CxfFileTransfer02/services/UploadServiceImplPort?wsdl");

		UploadServiceImpl service = (UploadServiceImpl) factory.create();

		DataSource dataSource = new FileDataSource(new File("C:/hanwha/_CXF/Consumer/Consumer.pptx"));
		FileUploader file = new FileUploader();
		file.setName("Provider");
		file.setFileType("pptx");
		file.setHandler(new DataHandler(dataSource));   // TODO-20180911: ERROR
		service.uploadFile(file);
	}
}
