package com.example.soapcamel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.*;

import javax.xml.bind.annotation.*;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {"idOrder", "dateOrder", "FIO", "SNILS", "birthday", "gender", "period"}
)
@XmlRootElement(
        name = "Request"
)
public class Request implements Element {

    @XmlElement(
            name = "IdOrder"
    )
    private String idOrder;

    @XmlElement(
            name = "DateOrder"
    )
    private String dateOrder;

    @XmlElement(
            name = "FIO"
    )
    private FIODto FIO;

    @XmlElement(
            name = "SNILS"
    )
    private String SNILS;

    @XmlElement(
            name = "Birthday"
    )
    private String birthday;

    @XmlElement(
            name = "Gender"
    )
    private String gender;

    @XmlElement(
            name = "Period"
    )
    private Period period;


    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        idOrder = idOrder;
    }

    public String getDateOrder() {
        return dateOrder;
    }


    public void setDateOrder(String dateOrder) {
        dateOrder = dateOrder;
    }

    public FIODto getFIO() {
        return FIO;
    }


    public void setFIO(FIODto FIO) {
        this.FIO = FIO;
    }

    public String getSNILS() {
        return SNILS;
    }

    public void setSNILS(String SNILS) {
        this.SNILS = SNILS;
    }

    public String getBirthday() {
        return birthday;
    }


    public void setBirthday(String birthday) {
        birthday = birthday;
    }

    public String getGender() {
        return gender;
    }


    public void setGender(String gender) {
        gender = gender;
    }

    public Period getPeriod() {
        return period;
    }


    public void setPeriod(Period period) {
        period = period;
    }

    @Override
    public String getTagName() {
        return null;
    }

    @Override
    public String getAttribute(String name) {
        return null;
    }

    @Override
    public void setAttribute(String name, String value) throws DOMException {

    }

    @Override
    public void removeAttribute(String name) throws DOMException {

    }

    @Override
    public Attr getAttributeNode(String name) {
        return null;
    }

    @Override
    public Attr setAttributeNode(Attr newAttr) throws DOMException {
        return null;
    }

    @Override
    public Attr removeAttributeNode(Attr oldAttr) throws DOMException {
        return null;
    }

    @Override
    public NodeList getElementsByTagName(String name) {
        return null;
    }

    @Override
    public String getAttributeNS(String namespaceURI, String localName) throws DOMException {
        return null;
    }

    @Override
    public void setAttributeNS(String namespaceURI, String qualifiedName, String value) throws DOMException {

    }

    @Override
    public void removeAttributeNS(String namespaceURI, String localName) throws DOMException {

    }

    @Override
    public Attr getAttributeNodeNS(String namespaceURI, String localName) throws DOMException {
        return null;
    }

    @Override
    public Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
        return null;
    }

    @Override
    public NodeList getElementsByTagNameNS(String namespaceURI, String localName) throws DOMException {
        return null;
    }

    @Override
    public boolean hasAttribute(String name) {
        return false;
    }

    @Override
    public boolean hasAttributeNS(String namespaceURI, String localName) throws DOMException {
        return false;
    }

    @Override
    public TypeInfo getSchemaTypeInfo() {
        return null;
    }

    @Override
    public void setIdAttribute(String name, boolean isId) throws DOMException {

    }

    @Override
    public void setIdAttributeNS(String namespaceURI, String localName, boolean isId) throws DOMException {

    }

    @Override
    public void setIdAttributeNode(Attr idAttr, boolean isId) throws DOMException {

    }

    @Override
    public String getNodeName() {
        return null;
    }

    @Override
    public String getNodeValue() throws DOMException {
        return null;
    }

    @Override
    public void setNodeValue(String nodeValue) throws DOMException {

    }

    @Override
    public short getNodeType() {
        return 0;
    }

    @Override
    public Node getParentNode() {
        return null;
    }

    @Override
    public NodeList getChildNodes() {
        return null;
    }

    @Override
    public Node getFirstChild() {
        return null;
    }

    @Override
    public Node getLastChild() {
        return null;
    }

    @Override
    public Node getPreviousSibling() {
        return null;
    }

    @Override
    public Node getNextSibling() {
        return null;
    }

    @Override
    public NamedNodeMap getAttributes() {
        return null;
    }

    @Override
    public Document getOwnerDocument() {
        return null;
    }

    @Override
    public Node insertBefore(Node newChild, Node refChild) throws DOMException {
        return null;
    }

    @Override
    public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
        return null;
    }

    @Override
    public Node removeChild(Node oldChild) throws DOMException {
        return null;
    }

    @Override
    public Node appendChild(Node newChild) throws DOMException {
        return null;
    }

    @Override
    public boolean hasChildNodes() {
        return false;
    }

    @Override
    public Node cloneNode(boolean deep) {
        return null;
    }

    @Override
    public void normalize() {

    }

    @Override
    public boolean isSupported(String feature, String version) {
        return false;
    }

    @Override
    public String getNamespaceURI() {
        return null;
    }

    @Override
    public String getPrefix() {
        return null;
    }

    @Override
    public void setPrefix(String prefix) throws DOMException {

    }

    @Override
    public String getLocalName() {
        return null;
    }

    @Override
    public boolean hasAttributes() {
        return false;
    }

    @Override
    public String getBaseURI() {
        return null;
    }

    @Override
    public short compareDocumentPosition(Node other) throws DOMException {
        return 0;
    }

    @Override
    public String getTextContent() throws DOMException {
        return null;
    }

    @Override
    public void setTextContent(String textContent) throws DOMException {

    }

    @Override
    public boolean isSameNode(Node other) {
        return false;
    }

    @Override
    public String lookupPrefix(String namespaceURI) {
        return null;
    }

    @Override
    public boolean isDefaultNamespace(String namespaceURI) {
        return false;
    }

    @Override
    public String lookupNamespaceURI(String prefix) {
        return null;
    }

    @Override
    public boolean isEqualNode(Node arg) {
        return false;
    }

    @Override
    public Object getFeature(String feature, String version) {
        return null;
    }

    @Override
    public Object setUserData(String key, Object data, UserDataHandler handler) {
        return null;
    }

    @Override
    public Object getUserData(String key) {
        return null;
    }
}
