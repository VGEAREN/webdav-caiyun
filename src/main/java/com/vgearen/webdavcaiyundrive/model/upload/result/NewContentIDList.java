
package com.vgearen.webdavcaiyundrive.model.upload.result;

public class NewContentIDList {

    
    private String contentID;
    
    private String contentName;
    
    private Long fileEtag;
    
    private Long fileVersion;
    
    private String isNeedUpload;
    
    private Long overridenFlag;

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public Long getFileEtag() {
        return fileEtag;
    }

    public void setFileEtag(Long fileEtag) {
        this.fileEtag = fileEtag;
    }

    public Long getFileVersion() {
        return fileVersion;
    }

    public void setFileVersion(Long fileVersion) {
        this.fileVersion = fileVersion;
    }

    public String getIsNeedUpload() {
        return isNeedUpload;
    }

    public void setIsNeedUpload(String isNeedUpload) {
        this.isNeedUpload = isNeedUpload;
    }

    public Long getOverridenFlag() {
        return overridenFlag;
    }

    public void setOverridenFlag(Long overridenFlag) {
        this.overridenFlag = overridenFlag;
    }

}
