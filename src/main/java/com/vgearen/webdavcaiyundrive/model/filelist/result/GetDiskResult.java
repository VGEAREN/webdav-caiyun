
package com.vgearen.webdavcaiyundrive.model.filelist.result;

import java.util.List;

public class GetDiskResult {

     
    private List<CatalogList> catalogList;
     
    private List<ContentList> contentList;
     
    private Integer isCompleted;
     
    private Long nodeCount;
     
    private String parentCatalogID;

    public List<CatalogList> getCatalogList() {
        return catalogList;
    }

    public void setCatalogList(List<CatalogList> catalogList) {
        this.catalogList = catalogList;
    }

    public List<ContentList> getContentList() {
        return contentList;
    }

    public void setContentList(List<ContentList> contentList) {
        this.contentList = contentList;
    }

    public Integer getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Integer isCompleted) {
        this.isCompleted = isCompleted;
    }

    public Long getNodeCount() {
        return nodeCount;
    }

    public void setNodeCount(Long nodeCount) {
        this.nodeCount = nodeCount;
    }

    public String getParentCatalogID() {
        return parentCatalogID;
    }

    public void setParentCatalogID(String parentCatalogID) {
        this.parentCatalogID = parentCatalogID;
    }

}
