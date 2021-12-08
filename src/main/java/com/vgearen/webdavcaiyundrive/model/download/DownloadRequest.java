
package com.vgearen.webdavcaiyundrive.model.download;


import com.vgearen.webdavcaiyundrive.model.CommonAccountInfo;

public class DownloadRequest {

    
    private String appName = "";
    
    private CommonAccountInfo commonAccountInfo;
    
    private String contentID;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public CommonAccountInfo getCommonAccountInfo() {
        return commonAccountInfo;
    }

    public void setCommonAccountInfo(CommonAccountInfo commonAccountInfo) {
        this.commonAccountInfo = commonAccountInfo;
    }

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

}
