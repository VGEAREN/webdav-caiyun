
package com.vgearen.webdavcaiyundrive.model.upload.result;


import java.util.List;

public class UploadResult {

    
    private Object catalogIDList;
    
    private Object isSlice;
    
    private List<NewContentIDList> newContentIDList;
    
    private String redirectionUrl;
    
    private String uploadTaskID;

    public Object getCatalogIDList() {
        return catalogIDList;
    }

    public void setCatalogIDList(Object catalogIDList) {
        this.catalogIDList = catalogIDList;
    }

    public Object getIsSlice() {
        return isSlice;
    }

    public void setIsSlice(Object isSlice) {
        this.isSlice = isSlice;
    }

    public List<NewContentIDList> getNewContentIDList() {
        return newContentIDList;
    }

    public void setNewContentIDList(List<NewContentIDList> newContentIDList) {
        this.newContentIDList = newContentIDList;
    }

    public String getRedirectionUrl() {
        return redirectionUrl;
    }

    public void setRedirectionUrl(String redirectionUrl) {
        this.redirectionUrl = redirectionUrl;
    }

    public String getUploadTaskID() {
        return uploadTaskID;
    }

    public void setUploadTaskID(String uploadTaskID) {
        this.uploadTaskID = uploadTaskID;
    }

}
