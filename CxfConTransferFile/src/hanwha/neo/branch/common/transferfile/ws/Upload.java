
package hanwha.neo.branch.common.transferfile.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for upload complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="upload">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TRANSFER_FILE" type="{http://ws.transferfile.common.branch.neo.hanwha/}transferFile" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "upload", propOrder = {
    "transferfile"
})
public class Upload {

    @XmlElement(name = "TRANSFER_FILE")
    protected TransferFile transferfile;

    /**
     * Gets the value of the transferfile property.
     * 
     * @return
     *     possible object is
     *     {@link TransferFile }
     *     
     */
    public TransferFile getTRANSFERFILE() {
        return transferfile;
    }

    /**
     * Sets the value of the transferfile property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransferFile }
     *     
     */
    public void setTRANSFERFILE(TransferFile value) {
        this.transferfile = value;
    }

}
