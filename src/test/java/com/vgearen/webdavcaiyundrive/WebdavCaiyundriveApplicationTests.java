package com.vgearen.webdavcaiyundrive;

import com.vgearen.webdavcaiyundrive.model.CFile;
import com.vgearen.webdavcaiyundrive.store.CaiyunDriverClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class WebdavCaiyundriveApplicationTests {

    @Autowired
    CaiyunDriverClientService caiyunDriverClientService;

    @Test
    void TestGetFileList() {
    }

}
