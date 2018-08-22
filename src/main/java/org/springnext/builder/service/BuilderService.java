package org.springnext.builder.service;

import java.io.File;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;
import org.springnext.builder.entity.ProjectInfo;
import org.springnext.builder.utils.FileUtils;

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
	public void editProjectInfo(String filePath, ProjectInfo oldProjectInfo, ProjectInfo newProjectInfo) {
		File file = new File(filePath);
		String srcPath = filePath.replace("pom.xml", "");
		if (!file.exists()) {
			return;
		}
		if (!oldProjectInfo.getPackageName().equals(newProjectInfo.getPackageName())) {

			String baseDir = "src" + File.separator + "main" + File.separator + "java" + File.separator;
			String oldFileDir = srcPath + baseDir + oldProjectInfo.getPackageName().replace(".", File.separator);
			String newFileDir = srcPath + baseDir + newProjectInfo.getPackageName().replace(".", File.separator);

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
			Document document = sr.read(filePath);
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


}
