package com.jh.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.jh.vo.Item;

public class CommonUtil {

	private static Logger logger = Logger.getLogger(CommonUtil.class);
	
	private String imageFilePath;	
	public void setImageFilePath(String imageFilePath) {
		this.imageFilePath = imageFilePath;
		if (!this.imageFilePath.endsWith("/")) {
			this.imageFilePath += "/";
		}
	}
	
	private String orgillImageFileUrl;
	public void setOrgillImageFileUrl(String orgillImageFileUrl) {
		this.orgillImageFileUrl = orgillImageFileUrl;
		if (!this.orgillImageFileUrl.endsWith("/")) {
			this.orgillImageFileUrl += "/";
		}
	}

	public String getImageUrl(String imageName) {
		return imageFilePath+imageName;
	}
	
	public String getImageUrl(String imageName, String extension) {
		return imageFilePath+imageName+(extension.startsWith("\\.") ? "" : ".")+extension;
	}
	
	public String getOrgillImageUrl(String imageName) {
		return orgillImageFileUrl+imageName;
	}
	
	private String orgillImageFilePath;
	public void setOrgillImageFilePath(String orgillImageFilePath) {
		this.orgillImageFilePath = orgillImageFilePath;
		if (!this.orgillImageFilePath.endsWith("/")) {
			this.orgillImageFilePath += "/";
		}
	}
	
	public Double roundOf(double r, int decimalPlace){
	    BigDecimal bd = new BigDecimal(r);
	    bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
	    return bd.doubleValue();
	}
	
	public String roundOfAndConvert2String(double r, int decimalPlace){
	    String d = roundOf(r, decimalPlace).toString();
	    int dsize = d.length()-(d.indexOf(".")+1);
		dsize = decimalPlace-dsize;
		for (int i = 0; i < dsize; i++) {
			d += "0";
		}
	    return d;
	}

	public void loadImageFromFileSystem(String filename, OutputStream out) {
		/*if (System.getenv("file.systempath.image") == null) {
			logger.warn("Image file path might not be set, please set system variable 'file.systempath.image' with the file path of the image folder.");
		}*/
		try {
			File file = new File(this.orgillImageFilePath);
			if (!file.isDirectory()) {
				file.mkdir();
			}
			file = new File(this.orgillImageFilePath, filename);
			FileInputStream in = new FileInputStream(file);
			byte[] bs = null;
			while (in.available() > 0) {
				bs = new byte[in.available() < 1024 ? 1024 : in.available()];
				in.read(bs);
				out.write(bs);
			}
			in.close();
		} catch (FileNotFoundException e) {
			logger.warn("Image file not found '"+this.orgillImageFilePath+filename+"'");
		}
		catch (Exception e) {
			logger.error("", e);
		}
	}
	
	public String appendZeros(int number, int totalLength) {
		StringBuilder sb = new StringBuilder();
		sb.append(number);
		int size = sb.length();
		for (int i = 1; i <= totalLength-size; i++) {
			sb.insert(0, "0");
		}
		return sb.toString();
	}
	
	public void createFile(String filepath, String filename, byte[] data) throws Exception {
		File file = new File(filepath);
		if (!file.isDirectory()) {
			file.mkdir();
		}
		if (!filepath.endsWith("//")) {
			filepath = filepath+"//";
		}
		file = new File(filepath+filename);
		FileOutputStream out = new FileOutputStream(file);
		out.write(data);
		out.close();
	}
	
	public String appendTrailingSpaces(String data, int totalLength) {
		StringBuilder sb = new StringBuilder();
		sb.append(data);
		int size = sb.length();
		for (int i = 1; i <= totalLength-size; i++) {
			sb.append(" ");
		}
		return sb.toString();
	}
	
	public List<String> extractSkus(List<Item> items) {
		List<String> skusInOrgDB = new ArrayList<>();
		for (Item item : items) {
			skusInOrgDB.add(item.getSku());
		}
		return skusInOrgDB;
	}
}
