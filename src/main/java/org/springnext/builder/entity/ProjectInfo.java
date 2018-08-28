package org.springnext.builder.entity;

import java.io.File;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * 项目信息
 * 
 * @author HyDe
 *
 */
@XmlRootElement(name="project")
public class ProjectInfo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Maven 项目的GroupID
	 */
	private String groupId;

	/**
	 * Maven 项目的ArtifactID
	 */
	private String artifactId;

	/**
	 * Maven 项目的版本号
	 */
	private String version;

	/**
	 * 项目名, Maven产生的文档用
	 */
	private String name;

	/**
	 * 项目主页的URL, Maven产生的文档用
	 */
	private String url;

	/**
	 * 项目的详细描述, Maven 产生的文档用。
	 */
	private String description;
	
	/**
	 * 项目根目录
	 */
	@XmlTransient
	private String projectSrc;

	/**
	 * 包名
	 */
	@XmlTransient
	private String packageName;
	
	/**
	 * 模块名
	 */
	@XmlTransient
	private String modelName;
	
	/**
	 * 菜单模块名
	 */
	@XmlTransient
	private String menuModelName;
	
	/**
	 * 表前缀
	 */
	@XmlTransient
	private String tablePrefix;
	
	/**
	 * 作者
	 */
	@XmlTransient
	private String author;

	@XmlElement(name="groupId")
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	@XmlElement(name="artifactId")
	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	@XmlElement(name="version")
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@XmlElement(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name="url")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@XmlElement(name="description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getProjectSrc() {
		return projectSrc;
	}

	public void setProjectSrc(String projectSrc) {
		this.projectSrc = projectSrc;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	
	public String getMenuModelName() {
		return menuModelName;
	}

	public void setMenuModelName(String menuModelName) {
		this.menuModelName = menuModelName;
	}

	public String getTablePrefix() {
		return tablePrefix;
	}

	public void setTablePrefix(String tablePrefix) {
		this.tablePrefix = tablePrefix;
	}
	
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getRelativePackageBasePath() {
		return "src" + File.separator + "main" + File.separator + "java" + File.separator;
	}
	
	public String getRelativeResourcesPath() {
		return "src" + File.separator + "main" + File.separator + "resources" + File.separator;
	}

	public String getPackagePath() {
		return getPackageName().replace(".", File.separator)+File.separator;
	}
	
	public String getTemplatesPath() {
		return "templates"+File.separator;
	}
	
	public String getAbsoluteSrcPath() {
		return getProjectSrc().replace("pom.xml", "");
	}
	
	public String getAbsolutePackageBasePath() {
		return getAbsoluteSrcPath()+getRelativePackageBasePath();
	}
	
	public String getAbsoluteResourcesPath() {
		return getAbsoluteSrcPath()+getRelativeResourcesPath();
	}
	
	public String getAbsolutePackagePath() {
		return getAbsolutePackageBasePath()+getPackagePath();
	}
	
	public String getAbsoluteModelNamePackagePath() {
		return getAbsolutePackagePath()+getModelName()+File.separator;
	}
	
	public String getAbsoluteResourcesFilePath() {
		return getAbsoluteResourcesPath()+getModelName()+File.separator;
	}
}
