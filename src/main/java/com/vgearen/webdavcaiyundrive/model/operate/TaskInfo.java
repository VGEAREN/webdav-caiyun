
package com.vgearen.webdavcaiyundrive.model.operate;

import java.util.List;

public class TaskInfo {


    private List<String> catalogInfoList;

    private List<String> contentInfoList;

    private String newCatalogID;

    public List<String> getCatalogInfoList() {
        return catalogInfoList;
    }

    public void setCatalogInfoList(List<String> catalogInfoList) {
        this.catalogInfoList = catalogInfoList;
    }

    public List<String> getContentInfoList() {
        return contentInfoList;
    }

    public void setContentInfoList(List<String> contentInfoList) {
        this.contentInfoList = contentInfoList;
    }

    public String getNewCatalogID() {
        return newCatalogID;
    }

    public void setNewCatalogID(String newCatalogID) {
        this.newCatalogID = newCatalogID;
    }

}
