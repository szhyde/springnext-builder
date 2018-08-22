package org.springnext.builder.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * 项目信息
 * 
 * @author hyde
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
	 * 包名
	 */
	@XmlTransient
	private String packageName;

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

}
