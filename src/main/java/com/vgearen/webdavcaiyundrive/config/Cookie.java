package com.vgearen.webdavcaiyundrive.config;

public class Cookie {

    private static String token;

    private static String account;

    private static String encrypt;

    private static String tel;

    public static String getCookie() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("ORCHES-C-TOKEN=").append(token).append(";")
                .append("ORCHES-C-ACCOUNT=").append(account).append(";")
                .append("ORCHES-I-ACCOUNT-ENCRYPT=").append(encrypt).append(";");
        return stringBuffer.toString();
    }


    public static void setToken(String token) {
        Cookie.token = token;
    }

    public static void setAccount(String account) {
        Cookie.account = account;
    }

    public static void setEncrypt(String encrypt) {
        Cookie.encrypt = encrypt;
    }

    public static String getTel() {
        return tel;
    }

    public static void setTel(String tel) {
        Cookie.tel = tel;
    }
}
