
/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

package org.tain.cxf.filetransfer.consumer;

import java.util.logging.Logger;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 3.0.1
 * 2018-09-11T23:58:55.341+09:00
 * Generated source version: 3.0.1
 * 
 */

@javax.jws.WebService(
                      serviceName = "UploadServiceImplService",
                      portName = "UploadServiceImplPort",
                      targetNamespace = "http://provider.filetransfer.cxf.tain.org/",
                      wsdlLocation = "http://localhost:8080/CxfFileTransfer02/services/UploadServiceImplPort?wsdl",
                      endpointInterface = "org.tain.cxf.filetransfer.consumer.UploadServiceImpl")
                      
public class UploadServiceImplPortImpl implements UploadServiceImpl {

    private static final Logger LOG = Logger.getLogger(UploadServiceImplPortImpl.class.getName());

    /* (non-Javadoc)
     * @see org.tain.cxf.filetransfer.consumer.UploadServiceImpl#uploadFile(org.tain.cxf.filetransfer.consumer.FileUploader  arg0 )*
     */
    public void uploadFile(org.tain.cxf.filetransfer.consumer.FileUploader arg0) { 
        LOG.info("Executing operation uploadFile");
        System.out.println(arg0);
        try {
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

}