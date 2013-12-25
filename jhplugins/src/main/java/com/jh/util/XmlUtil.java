package com.jh.util;


import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;

public class XmlUtil {

	private static XmlUtil xmlUtil;
	private Map<Integer, Unmarshaller> unmarshallers;
	private Map<Integer, Marshaller> marshallers;
	
	public XmlUtil(Map<Integer, Unmarshaller> unmarshallers,
			Map<Integer, Marshaller> marshallers) {
		super();
		this.unmarshallers = unmarshallers;
		this.marshallers = marshallers;
	}

	public static void init(Map<Integer, Unmarshaller> unmarshallers, Map<Integer, Marshaller> marshallers) {
		if (xmlUtil == null) {
			xmlUtil = new XmlUtil(unmarshallers, marshallers);
		}
	}
	
	public static XmlUtil getInstance() {
		return xmlUtil;
	}
	
	public String convertObject2Xml(Class<? extends Object> class1, Object object) {
		StringWriter writer = new StringWriter();
		try {
			JAXBContext.newInstance(class1).createMarshaller().marshal(object, writer);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return removeUnwantedElements(writer.toString());
	}
	
	public <V> V convertXml2Object(Class<V> class1, String xml) {
		Object object = null;
		try {
			object = JAXBContext.newInstance(class1).createUnmarshaller().unmarshal(new StringReader(xml.toString()));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return class1.cast(object);
	}
	
	public String convertObject2Xml(Object of, Object object) {
		StringWriter writer = new StringWriter();
		try {
			JAXBContext.newInstance(of.getClass().getPackage().getName()).createMarshaller().marshal(object, writer);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return removeUnwantedElements(writer.toString());
	}
	
	@SuppressWarnings("unchecked")
	public <V> V convertXml2Object(Class<V> class1, Object of, String xml) {
		JAXBElement<V> jaxElement = null;
		try {
			jaxElement = (JAXBElement<V>) JAXBContext.newInstance(of.getClass().getPackage().getName()).createUnmarshaller().unmarshal(new StringReader(xml.toString()));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return jaxElement.getValue();
	}
	
	public String removeUnwantedElements(String xml) {
		return xml.replaceAll("<\\?[\\w\\s\\S]*\\?>", "");
	}
	
	public String convertObject2Xml(Object object, Marshaller marshaller) {
		StringWriter stringWriter = new StringWriter();
		try {
			marshaller.marshal(object, new StreamResult(stringWriter));
		} catch (XmlMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return removeUnwantedElements(stringWriter.toString());
	}
	
	public String marshal(Object object) {
		return marshal(marshallers.get(0), object);
	}
	
	public String marshal(Integer type, Object object) {
		return marshal(marshallers.get(type), object);
	}
	
	private String marshal(Marshaller marshaller, Object object) {
		StringWriter stringWriter = new StringWriter();
		try {
			marshaller.marshal(object, new StreamResult(stringWriter));
		} catch (XmlMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return removeUnwantedElements(stringWriter.toString());
	}
	
	public <V> V unmarshal(Class<V> class1, String xml) {
		return class1.cast(unmarshal(unmarshallers.get(0), xml));
	}
	
	public <V> V unmarshal(Integer type, Class<V> class1, String xml) {
		return class1.cast(unmarshal(unmarshallers.get(type), xml));
	}
	
	@SuppressWarnings("rawtypes")
	private Object unmarshal(Unmarshaller unmarshaller, String xml){
		Object object = null;
		try {
			object = unmarshaller.unmarshal(new StreamSource(new StringReader(xml.toString())));
			if (object instanceof JAXBElement) {
				object = ((JAXBElement)object).getValue();
			}
		} catch (XmlMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return object;
	}
}
