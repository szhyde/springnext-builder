package org.springnext.builder.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;
import org.springnext.builder.entity.Class;
import org.springnext.builder.entity.ProjectInfo;
import org.springnext.builder.entity.Table;
import org.springnext.builder.utils.FileUtils;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

@Service
public class BuilderService {

	/**
	 * 加载项目信息
	 * 
	 * @param filePath
	 */
	public ProjectInfo loadProjectInfo(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			return null;
		}
		ProjectInfo projectInfo = (ProjectInfo)FileUtils.fromXml(filePath,ProjectInfo.class);
		if (projectInfo == null) {
			projectInfo = new ProjectInfo();
		}
		file = new File(filePath.replace("pom.xml", "") + "src/main/java");
		if (file.exists()) {
			File[] fileList = file.listFiles();
			projectInfo.setPackageName(FileUtils.finePackage(fileList));
		}
		return projectInfo;
	}

	/**
	 * 修改项目信息
	 * 
	 * @param filePath
	 * @param oldProjectInfo
	 * @param newProjectInfo
	 */
	public void editProjectInfo(ProjectInfo oldProjectInfo, ProjectInfo newProjectInfo) {
		File file = new File(oldProjectInfo.getProjectSrc());
		String srcPath = oldProjectInfo.getAbsoluteSrcPath();
		if (!file.exists()) {
			return;
		}
		if (!oldProjectInfo.getPackageName().equals(newProjectInfo.getPackageName())) {

			String baseDir = oldProjectInfo.getRelativePackageBasePath();
			String oldFileDir = srcPath + baseDir + oldProjectInfo.getPackagePath();
			String newFileDir = srcPath + baseDir + newProjectInfo.getPackagePath();

			//找到要删除的旧包的根目录，目前不支持新包名为老包名的上一级或下一级
			//TODO 新包不可以是老包目录的上一级或下一级
			String[] oldPackage = oldProjectInfo.getPackageName().split(".");
			String[] newPackage = newProjectInfo.getPackageName().split(".");
			String oldFileBaseDir = "";
			for (int i = 0; i < oldPackage.length && i < newPackage.length; i++) {
				oldFileBaseDir += oldPackage[i];
				if (oldPackage[i].equals(newPackage[i])) {
					oldFileBaseDir += File.separator;
				} else {
					return;
				}
			}
			
			try {
				//把文件复印到新包目录中
				FileUtils.copyDir(oldFileDir, newFileDir);
				//删除旧包
				FileUtils.deleteFile(new File(oldFileBaseDir));
				//修改文件中的包目录
				FileUtils.editDir(new File(newFileDir), oldProjectInfo.getPackageName(), newProjectInfo.getPackageName());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		//修改pom.xml
		try {
			SAXReader sr = new SAXReader(); // 需要导入jar包:dom4j
			// 关联xml
			Document document = sr.read(oldProjectInfo.getProjectSrc());
			// 获取根元素
			Element root = document.getRootElement();
			Element groupId = root.element("groupId");
			groupId.setText(newProjectInfo.getGroupId());
			Element artifactId = root.element("artifactId");
			artifactId.setText(newProjectInfo.getArtifactId());
			Element version = root.element("version");
			version.setText(newProjectInfo.getVersion());
			Element name = root.element("name");
			name.setText(newProjectInfo.getName());
			Element url = root.element("url");
			url.setText(newProjectInfo.getUrl());
			Element description = root.element("description");
			description.setText(newProjectInfo.getDescription());

			FileUtils.saveDocument(document, file);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 生成Manager文件
	 * 
	 * @param filePath
	 */
	public void generatorManager(ProjectInfo projectInfo,List<Table> tables) {
		Map<String,Object> root = new HashMap<String,Object>();
        root.put("projectInfo", projectInfo);
		try {
			new JPAEntityFileGenerator(tables).generate(projectInfo);
			new DTOEntityFileGenerator(tables).generate(projectInfo);
			Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
			cfg.setDirectoryForTemplateLoading(new File(this.getClass().getClassLoader().getResource("").getPath()+"\\templates\\manager"));
            cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS));
            Template temp;
            String fileName = "";
            File file;
            FileWriter fw;
            BufferedWriter bw;
            
            List<Class> poList = new ArrayList<Class>();
            
            for (Table table : tables) {
            	poList.add(new POGenerator(table, projectInfo).generate());
			}
            
            temp = cfg.getTemplate("jpa/Repository.ftl");
			for (Class po : poList) {
				fileName = po.getType().getTypeName()+"Repository.java";
				root.put("po", po);
				file = new File(projectInfo.getAbsoluteModelNamePackagePath()+"repository"+File.separator+"jpa"+File.separator);
				if(!file.exists()) {
					file.mkdirs();
				}
				file = new File(projectInfo.getAbsoluteModelNamePackagePath()+"repository"+File.separator+"jpa"+File.separator + fileName);
	            fw = new FileWriter(file);
	            bw = new BufferedWriter(fw);
	            temp.process(root, bw);
	            bw.flush();
	            fw.close();
			}
			
			temp = cfg.getTemplate("jpa/Service.ftl");
			for (Class po : poList) {
				fileName = po.getType().getTypeName()+"Service.java";
				root.put("po", po);
				file = new File(projectInfo.getAbsoluteModelNamePackagePath()+"service"+File.separator);
				if(!file.exists()) {
					file.mkdirs();
				}
				file = new File(projectInfo.getAbsoluteModelNamePackagePath()+"service"+File.separator + fileName);
	            fw = new FileWriter(file);
	            bw = new BufferedWriter(fw);
	            temp.process(root, bw);
	            bw.flush();
	            fw.close();
			}
			
			temp = cfg.getTemplate("layui/Controller.ftl");
			for (Class po : poList) {
				fileName = po.getType().getTypeName()+"Controller.java";
				root.put("po", po);
				file = new File(projectInfo.getAbsoluteModelNamePackagePath()+"controller"+File.separator);
				if(!file.exists()) {
					file.mkdirs();
				}
				file = new File(projectInfo.getAbsoluteModelNamePackagePath()+"controller"+File.separator + fileName);
	            fw = new FileWriter(file);
	            bw = new BufferedWriter(fw);
	            temp.process(root, bw);
	            bw.flush();
	            fw.close();
			}
			
			temp = cfg.getTemplate("layui/add.ftl");
			for (Class po : poList) {
				fileName = "add.html";
				root.put("po", po);
				file = new File(projectInfo.getAbsoluteResourcesPath()+"templates"+File.separator+projectInfo.getModelName()+File.separator+po.getType().getTypeName().toLowerCase()+File.separator);
				if(!file.exists()) {
					file.mkdirs();
				}
				file = new File(projectInfo.getAbsoluteResourcesPath()+"templates"+File.separator+projectInfo.getModelName()+File.separator+po.getType().getTypeName().toLowerCase()+File.separator + fileName);
	            fw = new FileWriter(file);
	            bw = new BufferedWriter(fw);
	            temp.process(root, bw);
	            bw.flush();
	            fw.close();
			}
			
			temp = cfg.getTemplate("layui/edit.ftl");
			for (Class po : poList) {
				fileName = "edit.html";
				root.put("po", po);
				file = new File(projectInfo.getAbsoluteResourcesPath()+"templates"+File.separator+projectInfo.getModelName()+File.separator+po.getType().getTypeName().toLowerCase()+File.separator);
				if(!file.exists()) {
					file.mkdirs();
				}
				file = new File(projectInfo.getAbsoluteResourcesPath()+"templates"+File.separator+projectInfo.getModelName()+File.separator+po.getType().getTypeName().toLowerCase()+File.separator + fileName);
	            fw = new FileWriter(file);
	            bw = new BufferedWriter(fw);
	            temp.process(root, bw);
	            bw.flush();
	            fw.close();
			}
			
			temp = cfg.getTemplate("layui/list.ftl");
			for (Class po : poList) {
				fileName = "list.html";
				root.put("po", po);
				file = new File(projectInfo.getAbsoluteResourcesPath()+"templates"+File.separator+projectInfo.getModelName()+File.separator+po.getType().getTypeName().toLowerCase()+File.separator);
				if(!file.exists()) {
					file.mkdirs();
				}
				file = new File(projectInfo.getAbsoluteResourcesPath()+"templates"+File.separator+projectInfo.getModelName()+File.separator+po.getType().getTypeName().toLowerCase()+File.separator + fileName);
	            fw = new FileWriter(file);
	            bw = new BufferedWriter(fw);
	            temp.process(root, bw);
	            bw.flush();
	            fw.close();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

}
