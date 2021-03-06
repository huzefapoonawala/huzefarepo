//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.11.12 at 11:53:20 PM IST 
//


package com.jh.vo.edi.x12.type810;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
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
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element ref="{}loop"/>
 *         &lt;element ref="{}segment"/>
 *       &lt;/choice>
 *       &lt;attribute name="Control" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="DocType" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "loopOrSegment"
})
@XmlRootElement(name = "transaction")
public class Transaction {

    @XmlElements({
        @XmlElement(name = "loop", type = Loop.class),
        @XmlElement(name = "segment", type = Segment.class)
    })
    protected List<Object> loopOrSegment;
    @XmlAttribute(name = "Control", required = true)
    protected String control;
    @XmlAttribute(name = "DocType", required = true)
    protected String docType;
    @XmlAttribute(name = "Name", required = true)
    protected String name;

    /**
     * Gets the value of the loopOrSegment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the loopOrSegment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLoopOrSegment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Loop }
     * {@link Segment }
     * 
     * 
     */
    public List<Object> getLoopOrSegment() {
        if (loopOrSegment == null) {
            loopOrSegment = new ArrayList<Object>();
        }
        return this.loopOrSegment;
    }

    /**
     * Gets the value of the control property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getControl() {
        return control;
    }

    /**
     * Sets the value of the control property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setControl(String value) {
        this.control = value;
    }

    /**
     * Gets the value of the docType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocType() {
        return docType;
    }

    /**
     * Sets the value of the docType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocType(String value) {
        this.docType = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

}
