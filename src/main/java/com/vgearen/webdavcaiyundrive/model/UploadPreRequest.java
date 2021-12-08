package com.vgearen.webdavcaiyundrive.model;

import java.util.List;

public class UploadPreRequest {
    private String check_name_mode = "refuse";
    private String content_hash;
    private String content_hash_name = "none";
    private String drive_id;
    private String name;
    private String parent_file_id;
    private String proof_code;
    private String proof_version = "v1";
    private Long size;
    private List<PartInfo> part_info_list;
    private String type = "file";

    public String getCheck_name_mode() {
        return check_name_mode;
    }

    public void setCheck_name_mode(String check_name_mode) {
        this.check_name_mode = check_name_mode;
    }

    public String getContent_hash() {
        return content_hash;
    }

    public void setContent_hash(String content_hash) {
        this.content_hash = content_hash;
    }

    public String getContent_hash_name() {
        return content_hash_name;
    }

    public void setContent_hash_name(String content_hash_name) {
        this.content_hash_name = content_hash_name;
    }

    public String getDrive_id() {
        return drive_id;
    }

    public void setDrive_id(String drive_id) {
        this.drive_id = drive_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent_file_id() {
        return parent_file_id;
    }

    public void setParent_file_id(String parent_file_id) {
        this.parent_file_id = parent_file_id;
    }

    public String getProof_code() {
        return proof_code;
    }

    public void setProof_code(String proof_code) {
        this.proof_code = proof_code;
    }

    public String getProof_version() {
        return proof_version;
    }

    public void setProof_version(String proof_version) {
        this.proof_version = proof_version;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public List<PartInfo> getPart_info_list() {
        return part_info_list;
    }

    public void setPart_info_list(List<PartInfo> part_info_list) {
        this.part_info_list = part_info_list;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class PartInfo {
        private Integer part_number;
        private String upload_url;

        public Integer getPart_number() {
            return part_number;
        }

        public void setPart_number(Integer part_number) {
            this.part_number = part_number;
        }

        public String getUpload_url() {
            return upload_url;
        }

        public void setUpload_url(String upload_url) {
            this.upload_url = upload_url;
        }
    }
}
