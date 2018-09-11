package org.tain.cxf.filetrans;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 3.0.1
 * 2018-09-12T01:24:22.987+09:00
 * Generated source version: 3.0.1
 * 
 */
@WebService(targetNamespace = "http://filetrans.cxf.tain.org/", name = "FileTransferImpl")
@XmlSeeAlso({ObjectFactory.class})
public interface FileTransferImpl {

    @WebMethod
    @RequestWrapper(localName = "upload", targetNamespace = "http://filetrans.cxf.tain.org/", className = "org.tain.cxf.filetrans.Upload")
    @ResponseWrapper(localName = "uploadResponse", targetNamespace = "http://filetrans.cxf.tain.org/", className = "org.tain.cxf.filetrans.UploadResponse")
    public void upload(
        @WebParam(name = "arg0", targetNamespace = "")
        java.lang.String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        byte[] arg1
    );

    @WebMethod
    @RequestWrapper(localName = "download", targetNamespace = "http://filetrans.cxf.tain.org/", className = "org.tain.cxf.filetrans.Download")
    @ResponseWrapper(localName = "downloadResponse", targetNamespace = "http://filetrans.cxf.tain.org/", className = "org.tain.cxf.filetrans.DownloadResponse")
    @WebResult(name = "return", targetNamespace = "")
    public byte[] download(
        @WebParam(name = "arg0", targetNamespace = "")
        java.lang.String arg0
    );
}
