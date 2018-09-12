package org.tain.cxf.filetransfer.consumer;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.0.1
 * 2018-09-12T18:06:24.522+09:00
 * Generated source version: 3.0.1
 * 
 */
@WebServiceClient(name = "FileTransferImplService", 
                  wsdlLocation = "http://localhost:8080/CxfProFileTrans03/services/FileTransferImplPort?wsdl",
                  targetNamespace = "http://provider.filetransfer.cxf.tain.org/") 
public class FileTransferImplService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://provider.filetransfer.cxf.tain.org/", "FileTransferImplService");
    public final static QName FileTransferImplPort = new QName("http://provider.filetransfer.cxf.tain.org/", "FileTransferImplPort");
    static {
        URL url = null;
        try {
            url = new URL("http://localhost:8080/CxfProFileTrans03/services/FileTransferImplPort?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(FileTransferImplService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "http://localhost:8080/CxfProFileTrans03/services/FileTransferImplPort?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public FileTransferImplService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public FileTransferImplService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public FileTransferImplService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public FileTransferImplService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public FileTransferImplService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public FileTransferImplService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }    

    /**
     *
     * @return
     *     returns FileTransferImpl
     */
    @WebEndpoint(name = "FileTransferImplPort")
    public FileTransferImpl getFileTransferImplPort() {
        return super.getPort(FileTransferImplPort, FileTransferImpl.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns FileTransferImpl
     */
    @WebEndpoint(name = "FileTransferImplPort")
    public FileTransferImpl getFileTransferImplPort(WebServiceFeature... features) {
        return super.getPort(FileTransferImplPort, FileTransferImpl.class, features);
    }

}
