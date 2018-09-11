package org.tain.cxf.server;

import javax.xml.ws.Endpoint;

import org.tain.cxf.filetrans.FileTransfer;
import org.tain.cxf.filetrans.FileTransferImpl;

public class WebServiceServer {

	private static final boolean flag;

	static {
		flag = true;

		if (flag) System.out.println(">>>>> " + new Object(){}.getClass().getEnclosingClass().getName());
	}

	/////////////////////////////////////////////////////////////

	public static void main(String[] args) throws Exception {
		if (flag) System.out.println(">>>>> " + new Object(){}.getClass().getEnclosingMethod().getName());

		if (flag) test01(args);
	}

	private static void test01(String[] args) throws Exception {
		if (flag) System.out.println(">>>>> " + new Object(){}.getClass().getEnclosingMethod().getName());

		String bindingURI = "http://localhost:8080/CxfProFileTrans01/services/FileTransferImplPort?wsdl";
		FileTransfer service = new FileTransferImpl();
		Endpoint.publish(bindingURI, service);
		System.out.println("server started at: " + bindingURI);
	}
}
