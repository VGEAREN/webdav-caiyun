package com.vgearen.webdavcaiyundrive.model;


import com.vgearen.webdavcaiyundrive.model.UploadPreRequest;

import java.util.List;

public class UploadPreResult {
    private String file_id;
    private String file_name;
    private String location;
    private Boolean rapid_upload;
    private String  type;
    private String upload_id;
    private List<UploadPreRequest.PartInfo> part_info_list;

    public String getFile_id() {
        return file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getRapid_upload() {
        return rapid_upload;
    }

    public void setRapid_upload(Boolean rapid_upload) {
        this.rapid_upload = rapid_upload;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpload_id() {
        return upload_id;
    }

    public void setUpload_id(String upload_id) {
        this.upload_id = upload_id;
    }

    public List<UploadPreRequest.PartInfo> getPart_info_list() {
        return part_info_list;
    }

    public void setPart_info_list(List<UploadPreRequest.PartInfo> part_info_list) {
        this.part_info_list = part_info_list;
    }
}
