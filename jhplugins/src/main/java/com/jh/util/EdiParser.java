package com.jh.util;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.berryworks.edireader.EDIReader;
import com.berryworks.edireader.error.EDISyntaxExceptionHandler;
import com.berryworks.edireader.error.RecoverableSyntaxException;
import com.jh.vo.edi.x12.EdiInvoice;
import com.jh.vo.edi.x12.type810.Ediroot810;
import com.jh.vo.edi.x12.type810.Element;
import com.jh.vo.edi.x12.type810.Interchange;
import com.jh.vo.edi.x12.type810.Loop;
import com.jh.vo.edi.x12.type997.EdiAcknowledgement;
import com.jh.vo.edi.x12.type997.Ediroot997;
import com.jh.vo.edi.x12.type997.Group;
import com.jh.vo.edi.x12.type997.Segment;
import com.jh.vo.edi.x12.type997.Transaction;

public class EdiParser {

	private static Logger logger = Logger.getLogger(EdiParser.class);
	
	private XMLReader ediReader;
	private Transformer transformer = null;

	public EdiParser() {
		super();
		ediReader = new EDIReader();
		((EDIReader) ediReader).setSyntaxExceptionHandler(new IgnoreSyntaxExceptions());
		
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
		} catch (Exception e) {
			logger.error("Error occurred while creating transformer.", e);
		}
	}
	
	public Ediroot997 parseX12Type997(String filepath, String filename) {
		return XmlUtil.getInstance().unmarshal(Ediroot997.class, parseEDIFile(filepath, filename).replaceAll("ediroot", "ediroot997"));
	}
	
	private String parseEDIFile(String filepath, String filename) {
		String generatedOutput = null;
		Reader inputReader = null;
		File file = null;
		try {
			if (filepath != null) {
				file = new File(filepath, filename);
			} else {
				file = new File(filename);
			}
			inputReader = new FileReader(file);
			generatedOutput = parseAndConvertEDIData(inputReader);
		} catch (Exception e) {
			logger.error("Error occurred while parsing EDI file "+filename+".", e);
		}
		finally{
			try {
				inputReader.close();
			} catch (IOException ignored) {}
			/*try {
				generatedOutput.close();
			} catch (IOException ignored) {}*/
		}
		return generatedOutput.toString();
	}
	
	private String parseAndConvertEDIData(Reader inputReader) throws TransformerException{
		InputSource inputSource = new InputSource(inputReader);
		StringWriter generatedOutput = new StringWriter();
		SAXSource source = new SAXSource(ediReader, inputSource);
		StreamResult result = new StreamResult(generatedOutput);
		transformer.transform(source, result);
		return generatedOutput.toString();
	}
	
	public String parseEDIData(String filepath, List<String> filenames){
		BufferedReader inputReader = null;
		List<String> ediData2Parse = new ArrayList<String>();
		StringBuilder generatedOutput = null;
		StringBuilder sb = new StringBuilder();
		for (String filename : filenames) {
			try{
				inputReader = new BufferedReader(new FileReader(new File(filepath, filename)));
				String line = null;
				while ((line = inputReader.readLine()) != null) {
					sb.append(line);
				}
				inputReader.close();
				int idx = 1;
				while (true) {
					idx = sb.indexOf("ISA*", 1);
					ediData2Parse.add(StringUtils.substring(sb.toString(), 0, idx == -1 && sb.length() > 0 ? sb.length() : idx));
					sb.delete(0, idx == -1 ? sb.length() : idx);
					if (idx == -1) {
						break;
					}
				}
			} catch (Exception e) {
				logger.error("Error occurred while parsing EDI file "+filename+".", e);
			}
			finally{
				try {
					inputReader.close();
				} catch (IOException ignored) {}
			}
		}
		if (!ediData2Parse.isEmpty()) {
			final int PARSE_THRESHOLD = 3;
			StringReader stringReader = null;
			sb.delete(0, sb.length());
			generatedOutput = new StringBuilder();
			for (int i = 1; i <= ediData2Parse.size(); i++) {
				sb.append(ediData2Parse.get(i-1));
				if (i % PARSE_THRESHOLD == 0 || i == ediData2Parse.size()) {
					stringReader = new StringReader(sb.toString());
					try {
						generatedOutput.append(parseAndConvertEDIData(stringReader));
					} catch (Exception e) {
						logger.error("Error occurred while parsing EDI data '"+sb.toString()+"'.", e);
					}
					sb.delete(0, sb.length());
				}
			}
		}
		return generatedOutput != null ? generatedOutput.toString().replaceAll("</ediroot><\\?[\\w\\s\\S]*\\?><ediroot>", "") : null;
	}
	
	public List<EdiAcknowledgement> extractEdiAcknowledgements(String filepath, List<String> filenames) {
		Ediroot997 ediroot = null;
		List<EdiAcknowledgement> acks = new ArrayList<EdiAcknowledgement>();
		EdiAcknowledgement ack = null;
		for (String filename : filenames) {
			ediroot = parseX12Type997(filepath, filename);
			for (Group g : ediroot.getInterchange().getGroup()) {
				for (Transaction t : g.getTransaction()) {
					ack = new EdiAcknowledgement();
					for (Segment s : t.getSegment()) {
						if (s.getId().equalsIgnoreCase("AK1")) {
							ack.setType(s.getElement().get(0).getValue());
							ack.setTypeId(s.getElement().get(1).getValue());
						}
						else if (s.getId().equalsIgnoreCase("AK9")) {
							ack.setStatus(s.getElement().get(0).getValue());
						}
					}
					acks.add(ack);
				}
			}
		}
		return acks;
	}
	
	static class IgnoreSyntaxExceptions implements EDISyntaxExceptionHandler {
        public boolean process(RecoverableSyntaxException syntaxException) {
            logger.warn("EDI syntax exception ["+syntaxException.getMessage()+"].");
            return true;
        }
    }
	
	public Ediroot810 parseX12Type810(String filepath, List<String> filenames) {
		return XmlUtil.getInstance().unmarshal(1,Ediroot810.class, parseEDIData(filepath, filenames).replaceAll("ediroot", "ediroot810"));
	}
	
	public List<EdiInvoice> extractEdiInvoices(String filepath, List<String> filenames) {
		Ediroot810 ediroot810 = null;
		List<EdiInvoice> invoices = new ArrayList<EdiInvoice>();
		EdiInvoice inv = null;
		if (!filenames.isEmpty()) {
			ediroot810 = parseX12Type810(filepath, filenames);
			for (Interchange i : ediroot810.getInterchange()) {
				for (com.jh.vo.edi.x12.type810.Group g : i.getGroup()) {
					for (com.jh.vo.edi.x12.type810.Transaction t : g.getTransaction()) {
						inv = new EdiInvoice();
						extractLoopOrSegement(inv, t.getLoopOrSegment());
						invoices.add(inv);
					}
				}
			}
		}
		return invoices;
	}
	
	private void extractLoopOrSegement(EdiInvoice inv, List<Object> loopOrSegment){
		for (Object o : loopOrSegment) {
			if (o instanceof com.jh.vo.edi.x12.type810.Segment) {
				com.jh.vo.edi.x12.type810.Segment s = (com.jh.vo.edi.x12.type810.Segment)o;
				for (Element e : s.getElement()) {
					if (e.getId().equalsIgnoreCase("BIG04")) {
						inv.setPoNumber(e.getContent().trim());
					} else if(e.getId().equalsIgnoreCase("SAC13")) {
						inv.setShippingReferenceId(e.getContent().trim());
					} else if(e.getId().equalsIgnoreCase("SAC15")) {
						inv.setShippingDescription(e.getContent().trim());
					}
				}
			} else if(o instanceof Loop) {
				extractLoopOrSegement(inv, ((Loop)o).getLoopOrSegment());
			}
		}
	}
}
