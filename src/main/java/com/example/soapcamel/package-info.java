@XmlSchema(
        namespace = "urn://rostrud/uchet/1.0.0",
        elementFormDefault = XmlNsForm.QUALIFIED,
        xmlns = {
                @XmlNs(prefix="rt", namespaceURI="urn://rostrud/uchet/1.0.0"),
                @XmlNs(prefix = "xsi", namespaceURI = "http://www.w3.org/2001/XMLSchema-instance")
        }
)

package com.example.soapcamel;
import javax.xml.bind.annotation.*;