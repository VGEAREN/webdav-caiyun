
package com.vgearen.webdavcaiyundrive.model.operatefolder;

import com.vgearen.webdavcaiyundrive.model.CommonAccountInfo;

public class CreateCatalogExtReq {

    
    private CommonAccountInfo commonAccountInfo;
    
    private String newCatalogName;
    
    private String parentCatalogID;

    public CommonAccountInfo getCommonAccountInfo() {
        return commonAccountInfo;
    }

    public void setCommonAccountInfo(CommonAccountInfo commonAccountInfo) {
        this.commonAccountInfo = commonAccountInfo;
    }

    public String getNewCatalogName() {
        return newCatalogName;
    }

    public void setNewCatalogName(String newCatalogName) {
        this.newCatalogName = newCatalogName;
    }

    public String getParentCatalogID() {
        return parentCatalogID;
    }

    public void setParentCatalogID(String parentCatalogID) {
        this.parentCatalogID = parentCatalogID;
    }

}
