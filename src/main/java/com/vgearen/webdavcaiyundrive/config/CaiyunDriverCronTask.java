package com.vgearen.webdavcaiyundrive.config;


import com.vgearen.webdavcaiyundrive.model.CFile;
import com.vgearen.webdavcaiyundrive.store.CaiyunDriverClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CaiyunDriverCronTask {

    @Autowired
    private CaiyunDriverClientService caiyunDriverClientService;

    /**
     * 每隔5分钟请求一下接口，保证token不过期
     */
    @Scheduled(initialDelay = 30 * 1000, fixedDelay = 5 * 60 * 1000)
    public void refreshToken() {
        try {
            CFile root = caiyunDriverClientService.getCFileByPath("/");
            caiyunDriverClientService.getCFiles(root.getFileId());
        } catch (Exception e) {
            // nothing
        }

    }
}
