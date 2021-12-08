![CI](https://github.com/vgearen/webdav-caiyun/actions/workflows/CI.yml/badge.svg)

# 和彩云Webdav

本项目实现了和彩云网盘的Webdav协议，可以通过此协议将网盘挂载到Windows或者Nas中。

由于本人时间精力水平有限，本项目是基于 [zxbu/webdav-aliyundriver](https://github.com/zxbu/webdav-aliyundriver) 修改实现的，如有侵权请联系删除。



## 使用方法

- [Jar包运行](#jar包运行)
- [Docker](#Docker)
- [Docker Compose](#Docker-Compose)



### Jar包运行

```bash
[root@localhost ~]# java -jar caiyun.jar --caiyun.account="orches-c-account" --caiyun.token="orches-c-token" --caiyun.encrypt="orches-i-account-encrypt" --caiyun.tel="user telnum"
```

其中：

- `account`：网页版和彩云Cookie中的 **ORCHES-C-ACCOUNT** 字段
- `token`：Cookie中的 **ORCHES-C-TOKEN** 
- `encrypt`：Cookie中的 **ORCHES-I-ACCOUNT-ENCRYPT** 
- `tel`： 和彩云的注册号码



### Docker

```bash
[root@localhost ~]# docker run -d --name=caiyun-webdav --restart=unless-stopped -p 8080:8080  -v /etc/localtime:/etc/localtime -e TZ="Asia/Shanghai" -e JAVA_OPTS="-Xmx512m" -e CAIYUN_ACCOUNT="ORCHES-C-ACCOUNT" -e CAIYUN_TOKEN="ORCHES-C-TOKEN" -e CAIYUN_ENCRYPT="ORCHES-I-ACCOUNT-ENCRYPT" -e CAIYUN_TEL="YOUR PHONE" vgearen/caiyun-webdav
```



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

```



## 使用教程

1. [Windows RaiDrive挂载]()
2. [Qnap威联通挂载]()



## TODO

1. 由于时间原因，还没研究和彩云Cookie过期机制，需要做续期





## License
This work is released under the MIT license. A copy of the license is provided in the [LICENSE](./LICENSE) file.
