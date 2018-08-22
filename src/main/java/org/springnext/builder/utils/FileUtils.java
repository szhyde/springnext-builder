package org.springnext.builder.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.PushbackInputStream;
import java.io.Writer;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/**
 * 文件工具类
 * @author hyde
 *
 */
public class FileUtils {

	/**
	 * 复制目录
	 * @param sourcePath
	 * @param newPath
	 * @throws IOException
	 */
	public static void copyDir(String sourcePath, String newPath) throws IOException {
		File file = new File(sourcePath);
		String[] filePath = file.list();

		if (!(new File(newPath)).exists()) {
			(new File(newPath)).mkdirs();
		}

		for (int i = 0; i < filePath.length; i++) {
			if ((new File(sourcePath + File.separator + filePath[i])).isDirectory()) {
				copyDir(sourcePath + File.separator + filePath[i], newPath + File.separator + filePath[i]);
			}

			if (new File(sourcePath + File.separator + filePath[i]).isFile()) {
				copyFile(sourcePath + File.separator + filePath[i], newPath + File.separator + filePath[i]);
			}

		}
	}

	/**
	 * 复制文件
	 * @param oldPath
	 * @param newPath
	 * @throws IOException
	 */
	public static void copyFile(String oldPath, String newPath) throws IOException {
		File oldFile = new File(oldPath);
		File file = new File(newPath);
		FileInputStream in = new FileInputStream(oldFile);
		FileOutputStream out = new FileOutputStream(file);
		;

		byte[] buffer = new byte[2097152];
		int readByte = 0;
		while ((readByte = in.read(buffer)) != -1) {
			out.write(buffer, 0, readByte);
		}

		in.close();
		out.close();
	}

	/**
	 * 通过目录找包名
	 * 
	 * @param fileList
	 * @return
	 */
	public static String finePackage(File[] fileList) {
		String fileName = "";
		String tempString = "";
		for (File file : fileList) {
			if ("BootApplication.java".equals(file.getName())) {
				return "";
			}
			if (file.isDirectory()) {
				fileName = file.getName();
				tempString = finePackage(file.listFiles());
				if (!"".equals(tempString)) {
					fileName = fileName + "." + tempString;
				}
			}
		}
		return fileName;
	}

	/**
	 * 先根遍历序递归删除文件夹
	 *
	 * @param dirFile 要被删除的文件或者目录
	 * @return 删除成功返回true, 否则返回false
	 */
	public static boolean deleteFile(File dirFile) {
		// 如果dir对应的文件不存在，则退出
		if (!dirFile.exists()) {
			return false;
		}

		if (dirFile.isFile()) {
			return dirFile.delete();
		} else {

			for (File file : dirFile.listFiles()) {
				deleteFile(file);
			}
		}

		return dirFile.delete();
	}

	/**
	 * 按目录更新文件内容
	 * @param sourcePath
	 * @param oldPackage
	 * @param newPackage
	 * @throws IOException
	 */
	public static void editDir(File sourcePath, String oldText, String newText) throws IOException {
		File[] filePath = sourcePath.listFiles();

		for (File file : filePath) {
			if (file.isDirectory()) {
				editDir(file, oldText, newText);
			}

			if (file.isFile()) {
				editFile(file, oldText, newText);
			}
		}
	}
	
	/**
	 * 修改文件的内容
	 * @param sourcePath
	 * @param oldPackage
	 * @param newPackage
	 * @throws IOException
	 */
	public static void editFile(File sourcePath, String oldText, String newText) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(sourcePath));
		ArrayList<String> list = new ArrayList<>();
		String str = null;
		while ((str = br.readLine()) != null) {
			str = str.replace(oldText, newText);
			list.add(str);
		}
		br.close();

		PrintWriter pw = new PrintWriter(sourcePath);
		for (String s : list) {
			pw.println(s);
		}
		pw.close();
	}

	/**
	 * 把XML写入文件
	 * @param document
	 * @param xmlFile
	 * @throws IOException
	 */
	public static void saveDocument(Document document, File xmlFile) throws IOException {
		Writer osWrite = new OutputStreamWriter(new FileOutputStream(xmlFile));// 创建输出流
		OutputFormat format = OutputFormat.createPrettyPrint(); // 获取输出的指定格式
		format.setEncoding("UTF-8");// 设置编码 ，确保解析的xml为UTF-8格式
		XMLWriter writer = new XMLWriter(osWrite, format);// XMLWriter
															// 指定输出文件以及格式
		writer.write(document);// 把document写入xmlFile指定的文件(可以为被解析的文件或者新创建的文件)
		writer.flush();
		writer.close();
	}
	
	/**
	 * Xml->Java Object.
	 * @param xml
	 * @param cls
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Object fromXml(String xml,Class cls) {
		try {
			// 处理类utf-8编码的文件，保存时会在第一行最开始处自动加入bom格式的相关信息
			PushbackInputStream pushbackInputStream = new PushbackInputStream(
					new BufferedInputStream(new FileInputStream(new File(xml))), 3);
			byte[] bom = new byte[3];
			if (pushbackInputStream.read(bom) != -1) {
				if (!(bom[0] == (byte) 0xEF && bom[1] == (byte) 0xBB && bom[2] == (byte) 0xBF)) {
					pushbackInputStream.unread(bom);
				}
			}
			JAXBContext jaxbContext = JAXBContext.newInstance(cls);

			// 设置忽略pom.xml的命名空间
			SAXParserFactory sax = SAXParserFactory.newInstance();
			sax.setNamespaceAware(false);// 设置忽略命名空间
			XMLReader xmlReader = sax.newSAXParser().getXMLReader();
			Source source = new SAXSource(xmlReader, new InputSource(pushbackInputStream));

			// 反向生成
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			return unmarshaller.unmarshal(source);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
