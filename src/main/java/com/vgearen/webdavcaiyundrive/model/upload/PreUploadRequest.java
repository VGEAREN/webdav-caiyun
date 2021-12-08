
package com.vgearen.webdavcaiyundrive.model.upload;

import com.vgearen.webdavcaiyundrive.model.CommonAccountInfo;

import java.util.List;

public class PreUploadRequest {

    
    private CommonAccountInfo commonAccountInfo;
    
    private Integer fileCount;
    
    private Integer manualRename;
    
    private String newCatalogName = "";
    
    private Integer operation;
    
    private String parentCatalogID;
    
    private Long totalSize;
    
    private List<UploadContentList> uploadContentList;

    public CommonAccountInfo getCommonAccountInfo() {
        return commonAccountInfo;
    }

    public void setCommonAccountInfo(CommonAccountInfo commonAccountInfo) {
        this.commonAccountInfo = commonAccountInfo;
    }

    public Integer getFileCount() {
        return fileCount;
    }

    public void setFileCount(Integer fileCount) {
        this.fileCount = fileCount;
    }

    public Integer getManualRename() {
        return manualRename;
    }

    public void setManualRename(Integer manualRename) {
        this.manualRename = manualRename;
    }

    public String getNewCatalogName() {
        return newCatalogName;
    }

    public void setNewCatalogName(String newCatalogName) {
        this.newCatalogName = newCatalogName;
    }

    public Integer getOperation() {
        return operation;
    }

    public void setOperation(Integer operation) {
        this.operation = operation;
    }

    public String getParentCatalogID() {
        return parentCatalogID;
    }

    public void setParentCatalogID(String parentCatalogID) {
        this.parentCatalogID = parentCatalogID;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public List<UploadContentList> getUploadContentList() {
        return uploadContentList;
    }

    public void setUploadContentList(List<UploadContentList> uploadContentList) {
        this.uploadContentList = uploadContentList;
    }

}
