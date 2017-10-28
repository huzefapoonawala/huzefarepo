package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import junit.framework.Assert;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.berryworks.edireader.EDIReader;
import com.berryworks.edireader.EDIReaderFactory;
import com.berryworks.edireader.EDISyntaxException;
import com.berryworks.edireader.error.EDISyntaxExceptionHandler;
import com.berryworks.edireader.error.RecoverableSyntaxException;
import com.jh.util.EdiParser;
import com.jh.util.FreemarkerTemplateUtil;
import com.jh.util.XmlUtil;
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

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelIterator;
import freemarker.template.Version;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:jh-template-context.xml","classpath:jh-test-context.xml","classpath:jh-common-context.xml"})
@ActiveProfiles(profiles={"development","website_sync"})
public class EDITest {

	@Resource FreemarkerTemplateUtil freemarkerTemplateUtil;
	@Resource FreeMarkerConfigurer templateConfigurer;
	@Resource EdiParser ediParser;
	
	private static final String SAMPLE_EDI_FILE_PATH = "/Users/hpoonaw/Personal/Laptop Backups/ICT-Dell-2016_11_22/HP_Data_D_2016-11-22/Personal/Work/shabbir ezzi website related/plugins related/";
	
//	@Test
	public void testEDIWithFreemarker() throws Exception{
		/*Configuration cfg = new Configuration();
//		cfg.setDirectoryForTemplateLoading(new File("/where/you/store/templates"));
		cfg.setClassForTemplateLoading(getClass(), "/");
		cfg.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
		cfg.setIncompatibleImprovements(new Version(2, 3, 20));
		
		Template template = cfg.getTemplate("tpl/edi/orgill/850.tpl");
		Assert.assertNotNull(template);
		SimpleHash hash = new SimpleHash();
		hash.put("num1", 1);
		hash.put("num2", 2);
		hash.put("userId", "JAMAICA");
		hash.put("orderId", 10);
		hash.put("orderDate", new Date());
		hash.put("segmentCount", 12);
		hash.put("firstName", "Huzefa");
		hash.put("lastName", "Poonawala");
		hash.put("shippingAddress1", "23,ezee sty");
		hash.put("shippingCity", "New Yory City");
		hash.put("shippingZone", "NY");
		hash.put("shippingPostcode", "12343");*/
		JSONArray array = new JSONArray();
//		hash.put("itemsOnOrder", array);
		array.add(JSONValue.parse("{\"onOrder\":10,\"sku\":\"1234567\"}"));
		array.add(JSONValue.parse("{\"onOrder\":2,\"sku\":\"5567657\"}"));
		JSONObject data = (JSONObject)JSONValue.parse("{\"dateAdded\":\"23-Sep-2013\",\"dateModified\":\"23-Sep-2013\",\"email\":\"institches@rstarmail.com\",\"firstName\":\"Karen\",\"fulfillOrderAtStore\":false,\"itemsOnOrder\":[{\"costPrice\":0.0,\"description\":\"OAK WALL MIRROR W\\/ LEDGE\",\"id\":0,\"onOrder\":1,\"sku\":\"4970067\"}],\"lastName\":\"Gaylord\",\"orderId\":136,\"orderStatus\":\"Pending\",\"shippingAddress\":\"38188 Nixon Rd, Purcellville, Virginia - 20132, United States.\",\"shippingAddress1\":\"38188 Nixon Rd\",\"shippingAddress2\":\"\",\"shippingCity\":\"Purcellville\",\"shippingCountry\":\"United States\",\"shippingPostcode\":\"20132\",\"shippingZone\":\"Virginia\",\"shippingZoneCode\":\"VR\",\"storeName\":\"Jamaica Hardware &amp; Paints, Inc.\",\"telephone\":\"5406689036\",\"totalBilledAmount\":121.39}");
		data.put("userId", "JAMAICA");
		data.put("orderDate", new Date());
		Writer out = new OutputStreamWriter(System.out);
//		template.process(data, out);
		freemarkerTemplateUtil.getTemplate("edi.orgill.850").process(data, out);
		out.flush();
		/*while (hash.keys().iterator().hasNext()) {
			hash.remove(hash.keys().iterator().next().toString());
		}*/
		/*System.out.println("");
		File f = new File("C:\\Users\\hpoonaw\\Desktop\\850.edi");
		Writer file = new FileWriter(f);
		template.process(data, file);
		file.flush();
		file.close();
		System.out.println(f.getName()+" "+f.getAbsolutePath());
		f.renameTo(new File(f.getAbsolutePath()+".uploaded"));		
		System.out.println(f.getName()+" "+f.getAbsolutePath());
		StringBuilder sb = new StringBuilder();
		sb.replace(0, sb.length(), "this is replaced string").append(".txt");
		System.out.println(sb.toString());*/
	}
	
//	@Test
	public void testConfigurer() throws Exception {
		Assert.assertNotNull(templateConfigurer);
		JSONArray array = new JSONArray();
		array.add(JSONValue.parse("{\"onOrder\":10,\"sku\":\"1234567\"}"));
		array.add(JSONValue.parse("{\"onOrder\":2,\"sku\":\"5567657\"}"));
		JSONObject data = (JSONObject)JSONValue.parse("{\"dateAdded\":\"23-Sep-2013\",\"dateModified\":\"23-Sep-2013\",\"email\":\"institches@rstarmail.com\",\"firstName\":\"Karen\",\"fulfillOrderAtStore\":false,\"itemsOnOrder\":[{\"costPrice\":0.0,\"description\":\"OAK WALL MIRROR W\\/ LEDGE\",\"id\":0,\"onOrder\":1,\"sku\":\"4970067\"}],\"lastName\":\"Gaylord\",\"orderId\":23,\"orderStatus\":\"Pending\",\"shippingAddress\":\"38188 Nixon Rd, Purcellville, Virginia - 20132, United States.\",\"shippingAddress1\":\"38188 Nixon Rd\",\"shippingAddress2\":\"\",\"shippingCity\":\"Purcellville\",\"shippingCountry\":\"United States\",\"shippingPostcode\":\"20132\",\"shippingZone\":\"Virginia\",\"storeName\":\"Jamaica Hardware &amp; Paints, Inc.\",\"telephone\":\"5406689036\",\"totalBilledAmount\":121.39}");
		data.put("userId", "JAMAICA");
		data.put("orderDate", new Date());
		Writer out = new OutputStreamWriter(System.out);
//		freemarkerTemplateUtil.getTemplate("edi.orgill.850");
		templateConfigurer.getConfiguration().getTemplate("edi/orgill/850.tpl").process(data, out);
		out.flush();
	}
	
//	@Test
	public void test997Ack() throws IOException {
		processEDIFile("E:/Personal/shabbir ezzi website related/plugins related/edi-orgill-ack/7188801258_2.997");
	}
	
	public void processEDIFile(String filename) throws IOException {
		InputSource inputSource = null;
		StringWriter generatedOutput = null;
		Reader inputReader = null;
		try {
			inputReader = new FileReader(filename);
//			generatedOutput = new FileWriter("E:/Personal/shabbir ezzi website related/plugins related/edi-orgill-ack/7188801258_2.xml");
			generatedOutput = new StringWriter();
			inputSource = new InputSource(inputReader);
			XMLReader ediReader = new EDIReader();

			/* // Tell the ediReader if an xmlns="http://..." is desired
	            if (namespaceEnabled) {
	                ((EDIReader) ediReader).setNamespaceEnabled(namespaceEnabled);
	            }*/

			// Tell the ediReader to handle EDI syntax errors instead of aborting
			//	            if (recover) {
			((EDIReader) ediReader).setSyntaxExceptionHandler(new IgnoreSyntaxExceptions());
			//	            }

			// Establish the SAXSource
			SAXSource source = new SAXSource(ediReader, inputSource);
			
			// Establish a Transformer
			Transformer transformer = TransformerFactory.newInstance().newTransformer();

			// Use a StreamResult to capture the generated XML output
			StreamResult result = new StreamResult(generatedOutput);

			// Call the Transformer to generate XML output from the parsed input
			transformer.transform(source, result);			
			System.out.println(generatedOutput.toString());
		} catch (TransformerConfigurationException e) {
	            System.err.println("\nUnable to create Transformer: " + e);
	        } catch (TransformerException e) {
	            System.err.println("\nFailure to transform: " + e);
	            System.err.println(e.getMessage());
	        }
	        try {
	            inputReader.close();
	        } catch (IOException ignored) {
	        }
	        try {
	            generatedOutput.close();
	        } catch (IOException ignored) {
	        }
	}
	
	static class IgnoreSyntaxExceptions implements EDISyntaxExceptionHandler {

        public boolean process(RecoverableSyntaxException syntaxException) {
            System.out.println("Syntax Exception. class: " + syntaxException.getClass().getName() + "  message:" + syntaxException.getMessage());
            // Return true to indicate that you want parsing to continue.
            return true;
        }
    }
	
//	@Test
	public void test997() {
		/*Ediroot997 ediroot = ediParser.parseX12Type997(null, "E:/Personal/shabbir ezzi website related/plugins related/edi-orgill-ack/7188801258.997");
		Assert.assertNotNull(ediroot);*/		
	}
	
	@Test
	public void testAcks() {
		List<EdiAcknowledgement> acks = ediParser.extractEdiAcknowledgements(SAMPLE_EDI_FILE_PATH+"/edi-orgill-ack/",new ArrayList<String>(Arrays.asList(new String[]{"7188801258.997"/*, "7188801258_2.997"*/})));
		for (EdiAcknowledgement ack : acks) {
			System.out.println(ack.toString());
		}
	}
	
	@Test
	public void test810() throws IOException, SAXException {
//		processEDIFile("E:/Personal/shabbir ezzi website related/plugins related/edi-orgill-invoice/7188801258.810");
		/*FileReader inputReader = new FileReader("E:/Personal/shabbir ezzi website related/plugins related/edi-orgill-invoice/7188801258_20_DEC_2013.810");
		InputSource source = new InputSource(inputReader);
		EDIReader reader = EDIReaderFactory.createEDIReader(source, true);
		reader.parse(source);
		inputReader.close();*/
		/*BufferedReader inputReader = new BufferedReader(new FileReader("E:/Personal/shabbir ezzi website related/plugins related/edi-orgill-invoice/7188801258_20_DEC_2013.810"));
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = inputReader.readLine()) != null) {
			sb.append(line);
		}
		inputReader.close();
		System.out.println(StringUtils.countMatches(sb.toString(), "ISA*"));
		int idx = 1;
		while (true) {
			idx = sb.indexOf("ISA*", 1);
			System.out.println(StringUtils.substring(sb.toString(), 0, idx == -1 && sb.length() > 0 ? sb.length() : idx));
			sb.delete(0, idx == -1 ? sb.length() : idx);
			if (idx == -1) {
				break;
			}
		}*/
		/*String[] data = sb.toString().split("ISA\\*[\\w\\W]*IEA[\\w\\W]*~");
		for (String d : data) {
			System.out.println(d);
		}*/
		/*StringReader stringReader = new StringReader(sb.toString());
		InputSource source = new InputSource(stringReader);*/
		/*Ediroot810 edi810 = ediParser.parseX12Type810(null, "E:/Personal/shabbir ezzi website related/plugins related/edi-orgill-invoice/7188801258_20_DEC_2013.810");
		Assert.assertNotNull(edi810);
		for (Interchange i : edi810.getInterchange()) {
			for (com.jh.vo.edi.x12.type810.Group g : i.getGroup()) {
				for (com.jh.vo.edi.x12.type810.Transaction t : g.getTransaction()) {
					EdiInvoice inv = new EdiInvoice();
					extractLoopOrSegement(inv, t.getLoopOrSegment());
					System.out.println(inv.toString());
				}
			}
		}*/
		
		String data = ediParser.parseEDIData(SAMPLE_EDI_FILE_PATH+"/edi-orgill-invoice/", Arrays.asList(new String[]{"7188801258.810"}));
		System.out.println(data);
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
	
	@Test
	public void testInvoices() {
		List<EdiInvoice> list = ediParser.extractEdiInvoices(SAMPLE_EDI_FILE_PATH+"/edi-orgill-invoice/", Arrays.asList(new String[]{"7188801258.810"}));
		for (EdiInvoice ediInvoice : list) {
			System.out.println(ediInvoice.toString());
		}
	}
}
