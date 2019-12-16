package com.xyjsoft.admin.model;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * ---------------------------
 * app版本 (AppVersion)         
 * ---------------------------
 * 作者：  原浩
 * 时间：  2019-03-11 17:38:22
 * 说明：  我是自己写的
 * ---------------------------
 */
@ApiModel(value = "AppVersion",description = "app版本") 
public class AppVersion implements Serializable{

	private static final long serialVersionUID = 1L;
	/** 编号 */
	@ApiModelProperty(value="编号")
	private Long id;
	/** app名称 */
	@ApiModelProperty(value="app名称")
	private String apknamme;
	/** app版本号 */
	@ApiModelProperty(value="app版本号")
	private String version;
	/** app下载路径 */
	@ApiModelProperty(value="app下载路径")
	private String downloadpath;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getApknamme() {
		return apknamme;
	}

	public void setApknamme(String apknamme) {
		this.apknamme = apknamme;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDownloadpath() {
		return downloadpath;
	}

	public void setDownloadpath(String downloadpath) {
		this.downloadpath = downloadpath;
	}

}