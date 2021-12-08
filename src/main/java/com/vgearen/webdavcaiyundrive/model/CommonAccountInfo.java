package com.vgearen.webdavcaiyundrive.model;

public class CommonAccountInfo {
    private String account;
    private Integer accountType = 1;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }
}
