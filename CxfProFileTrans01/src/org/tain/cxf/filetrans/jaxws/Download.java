
package org.tain.cxf.filetrans.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class was generated by Apache CXF 2.2.7
 * Thu Sep 13 00:54:29 KST 2018
 * Generated source version: 2.2.7
 */

@XmlRootElement(name = "download", namespace = "http://filetrans.cxf.tain.org/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "download", namespace = "http://filetrans.cxf.tain.org/")

public class Download {

    @XmlElement(name = "arg0")
    private java.lang.String arg0;

    public java.lang.String getArg0() {
        return this.arg0;
    }

    public void setArg0(java.lang.String newArg0)  {
        this.arg0 = newArg0;
    }

}

