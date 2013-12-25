//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.11.13 at 12:15:57 AM IST 
//


package com.jh.vo.edi.x12.type997;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}transaction" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="ApplReceiver" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ApplSender" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Control" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="Date" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="GroupType" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="StandardCode" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="StandardVersion" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Time" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "transaction"
})
@XmlRootElement(name = "group")
public class Group {

    @XmlElement(required = true)
    protected List<Transaction> transaction;
    @XmlAttribute(name = "ApplReceiver", required = true)
    protected String applReceiver;
    @XmlAttribute(name = "ApplSender", required = true)
    protected String applSender;
    @XmlAttribute(name = "Control", required = true)
    protected int control;
    @XmlAttribute(name = "Date", required = true)
    protected String date;
    @XmlAttribute(name = "GroupType", required = true)
    protected String groupType;
    @XmlAttribute(name = "StandardCode", required = true)
    protected String standardCode;
    @XmlAttribute(name = "StandardVersion", required = true)
    protected String standardVersion;
    @XmlAttribute(name = "Time", required = true)
    protected String time;

    /**
     * Gets the value of the transaction property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the transaction property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTransaction().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Transaction }
     * 
     * 
     */
    public List<Transaction> getTransaction() {
        if (transaction == null) {
            transaction = new ArrayList<Transaction>();
        }
        return this.transaction;
    }

    /**
     * Gets the value of the applReceiver property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplReceiver() {
        return applReceiver;
    }

    /**
     * Sets the value of the applReceiver property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplReceiver(String value) {
        this.applReceiver = value;
    }

    /**
     * Gets the value of the applSender property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplSender() {
        return applSender;
    }

    /**
     * Sets the value of the applSender property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplSender(String value) {
        this.applSender = value;
    }

    /**
     * Gets the value of the control property.
     * 
     */
    public int getControl() {
        return control;
    }

    /**
     * Sets the value of the control property.
     * 
     */
    public void setControl(int value) {
        this.control = value;
    }

    /**
     * Gets the value of the date property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the value of the date property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDate(String value) {
        this.date = value;
    }

    /**
     * Gets the value of the groupType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroupType() {
        return groupType;
    }

    /**
     * Sets the value of the groupType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroupType(String value) {
        this.groupType = value;
    }

    /**
     * Gets the value of the standardCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStandardCode() {
        return standardCode;
    }

    /**
     * Sets the value of the standardCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStandardCode(String value) {
        this.standardCode = value;
    }

    /**
     * Gets the value of the standardVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStandardVersion() {
        return standardVersion;
    }

    /**
     * Sets the value of the standardVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStandardVersion(String value) {
        this.standardVersion = value;
    }

    /**
     * Gets the value of the time property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTime() {
        return time;
    }

    /**
     * Sets the value of the time property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTime(String value) {
        this.time = value;
    }

}
