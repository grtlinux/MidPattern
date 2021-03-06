
/*
 * 
 */

package hanwha.neo.branch.common.transferfile.ws;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.2.7
 * Tue Sep 18 15:52:09 KST 2018
 * Generated source version: 2.2.7
 * 
 */


@WebServiceClient(name = "TransferFileServiceImplService", 
                  wsdlLocation = "http://hanwha.eagleoffice.co.kr/neo/ws/common/transferFile?wsdl",
                  targetNamespace = "http://ws.transferfile.common.branch.neo.hanwha/") 
public class TransferFileServiceImplService extends Service {

    public final static URL WSDL_LOCATION;
    public final static QName SERVICE = new QName("http://ws.transferfile.common.branch.neo.hanwha/", "TransferFileServiceImplService");
    public final static QName TransferFileServiceImplPort = new QName("http://ws.transferfile.common.branch.neo.hanwha/", "TransferFileServiceImplPort");
    static {
        URL url = null;
        try {
            url = new URL("http://hanwha.eagleoffice.co.kr/neo/ws/common/transferFile?wsdl");
        } catch (MalformedURLException e) {
            System.err.println("Can not initialize the default wsdl from http://hanwha.eagleoffice.co.kr/neo/ws/common/transferFile?wsdl");
            // e.printStackTrace();
        }
        WSDL_LOCATION = url;
    }

    public TransferFileServiceImplService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public TransferFileServiceImplService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public TransferFileServiceImplService() {
        super(WSDL_LOCATION, SERVICE);
    }

    /**
     * 
     * @return
     *     returns TransferFileService
     */
    @WebEndpoint(name = "TransferFileServiceImplPort")
    public TransferFileService getTransferFileServiceImplPort() {
        return super.getPort(TransferFileServiceImplPort, TransferFileService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns TransferFileService
     */
    @WebEndpoint(name = "TransferFileServiceImplPort")
    public TransferFileService getTransferFileServiceImplPort(WebServiceFeature... features) {
        return super.getPort(TransferFileServiceImplPort, TransferFileService.class, features);
    }

}
