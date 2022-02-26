![CI](https://github.com/vgearen/webdav-caiyun/actions/workflows/CI.yml/badge.svg)

# 和彩云Webdav

本项目实现了和彩云网盘的Webdav协议，可以通过此协议将网盘挂载到Windows或者Nas中。项目是基于 [zxbu/webdav-aliyundriver](https://github.com/zxbu/webdav-aliyundriver) 修改实现的，如有侵权请联系删除。



## 使用方法

- [Jar包运行](#jar包运行)
- [Docker](#Docker)
- [Docker Compose](#Docker-Compose)



### Jar包运行
[点击下载JAR](https://github.com/VGEAREN/webdav-caiyun/releases/latest)
```bash
[root@localhost ~]# java -jar caiyun-webdav.jar --caiyun.account="orches-c-account" --caiyun.token="orches-c-token" --caiyun.encrypt="orches-i-account-encrypt" --caiyun.tel="user telnum"
```

其中：

- `caiyun.account`：网页版和彩云Cookie中的 **ORCHES-C-ACCOUNT** 字段
- `caiyun.token`：Cookie中的 **ORCHES-C-TOKEN** 
- `caiyun.encrypt`：Cookie中的 **ORCHES-I-ACCOUNT-ENCRYPT** 
- `caiyun.tel`： 和彩云的注册号码
- `caiyun.auth.user-name`：`可选` 默认admin
- `caiyun.auth.password`：`可选` 默认admin


### Docker

```bash
[root@localhost ~]# docker run -d --name=caiyun-webdav --restart=unless-stopped -p 8080:8080  -v /etc/localtime:/etc/localtime -e TZ="Asia/Shanghai" -e JAVA_OPTS="-Xmx512m" -e CAIYUN_ACCOUNT="ORCHES-C-ACCOUNT" -e CAIYUN_TOKEN="ORCHES-C-TOKEN" -e CAIYUN_ENCRYPT="ORCHES-I-ACCOUNT-ENCRYPT" -e CAIYUN_TEL="YOUR PHONE" vgearen/caiyun-webdav
```

默认认证账号密码admin/admin，需要修改添加参数` -e CAIYUN_AUTH_USER_NAME="USERNAME" -e CAIYUN_AUTH_PASSWORD="PASSWORD"` 

### Docker Compose

```yaml
version: '3'
services:
  caiyun-webdav:
    image: vgearen/caiyun-webdav
    container_name: caiyun-webdav
    restart: unless-stopped
    volumes:
      - /etc/localtime:/etc/localtime
    ports:
      - "8080:8080"
    tty: true
    environment:
      - TZ=Asia/Shanghai
      - CAIYUN_ACCOUNT=<change me>
      - CAIYUN_TOKEN=<change me>
      - CAIYUN_ENCRYPT=<change me>
      - CAIYUN_TEL=<change me>
      # - CAIYUN_AUTH_USER_NAME=<change me>
      # - CAIYUN_AUTH_PASSWORD=<change me>

```

## 获取Cookie的方法
打开网页版和彩云，Windows下按`F12`调出`开发者工具`，依次选择 **应用程序** -> **存储** -> **Cookie**   

[获取Cookie的方法](https://github.com/VGEAREN/webdav-caiyun/wiki/Cookie%E8%8E%B7%E5%8F%96)



## 客户端兼容性

| 客户端           | 兼容情况 |
| ---------------- | -------- |
| QNAP HyBirdMount | ✅        |
| RaiDrive         | ✅        |
| macOS Finder     | ✅        |
| Windows原生      | ✅        |



## 使用教程

1. [Windows RaiDrive挂载]()
2. [Qnap威联通挂载](https://github.com/VGEAREN/webdav-caiyun/wiki/Qnap%E5%A8%81%E8%81%94%E9%80%9A%E6%8C%82%E8%BD%BD%E5%92%8C%E5%BD%A9%E4%BA%91)



## TODO

1. 账号密码登录





## License
This work is released under the MIT license. A copy of the license is provided in the [LICENSE](./LICENSE) file.
