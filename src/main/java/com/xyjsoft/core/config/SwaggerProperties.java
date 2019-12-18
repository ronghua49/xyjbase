package com.xyjsoft.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import springfox.documentation.service.Contact;

/**
 * 类名:SwaggerProperties
 * 类描述:swagger文档说明
 *
 * @author gsh456
 * @version 1.0
 * @date 2019-04-12 17:05
 * @since JDK1.8
 */

@ConfigurationProperties(prefix = "swagger.info")
public class SwaggerProperties {
    private String title ="讯宜捷平台  API Doc";
    private String description ="这是 restful API 接口文档";
    private String version="1.0";
    private String license;
    private String licenseUrl;
    private String author;
    private String email;
    private String url;
    private Contact contact ;

    public Contact getContact() {
        return new Contact(author,url,email);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
