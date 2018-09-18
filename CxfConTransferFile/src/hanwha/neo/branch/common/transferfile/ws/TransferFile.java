
package hanwha.neo.branch.common.transferfile.ws;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for transferFile complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="transferFile">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dataHandler" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="fileName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="filePath" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fileSize" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "transferFile", propOrder = {
    "dataHandler",
    "fileName",
    "filePath",
    "fileSize"
})
public class TransferFile {

    //protected byte[] dataHandler;
    protected DataHandler dataHandler;
    protected String fileName;
    protected String filePath;
    protected String fileSize;

    /**
     * Gets the value of the dataHandler property.
     *
     * @return
     *     possible object is
     *     byte[]
     */
    public DataHandler getDataHandler() {
        return dataHandler;
    }

    /**
     * Sets the value of the dataHandler property.
     *
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setDataHandler(DataHandler value) {
        this.dataHandler = ((DataHandler) value);
    }

    /**
     * Gets the value of the fileName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the value of the fileName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setFileName(String value) {
        this.fileName = value;
    }

    /**
     * Gets the value of the filePath property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Sets the value of the filePath property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setFilePath(String value) {
        this.filePath = value;
    }

    /**
     * Gets the value of the fileSize property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getFileSize() {
        return fileSize;
    }

    /**
     * Sets the value of the fileSize property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setFileSize(String value) {
        this.fileSize = value;
    }

}
