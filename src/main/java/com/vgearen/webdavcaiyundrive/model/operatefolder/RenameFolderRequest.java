
package com.vgearen.webdavcaiyundrive.model.operatefolder;

import com.vgearen.webdavcaiyundrive.model.CommonAccountInfo;

public class RenameFolderRequest {


    private String catalogID;

    private String catalogName;

    private CommonAccountInfo commonAccountInfo;

    public String getCatalogID() {
        return catalogID;
    }

    public void setCatalogID(String catalogID) {
        this.catalogID = catalogID;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public CommonAccountInfo getCommonAccountInfo() {
        return commonAccountInfo;
    }

    public void setCommonAccountInfo(CommonAccountInfo commonAccountInfo) {
        this.commonAccountInfo = commonAccountInfo;
    }

}
