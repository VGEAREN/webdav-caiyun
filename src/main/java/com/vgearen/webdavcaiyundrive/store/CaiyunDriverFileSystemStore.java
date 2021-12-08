package com.vgearen.webdavcaiyundrive.store;

import com.vgearen.webdavcaiyundrive.model.FileType;
import com.vgearen.webdavcaiyundrive.model.CFile;
import com.vgearen.webdavcaiyundrive.model.filelist.result.PathInfo;
import net.sf.webdav.ITransaction;
import net.sf.webdav.IWebdavStore;
import net.sf.webdav.StoredObject;
import net.sf.webdav.Transaction;
import net.sf.webdav.exceptions.WebdavException;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Optional;
import java.util.Set;

public class CaiyunDriverFileSystemStore implements IWebdavStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(CaiyunDriverFileSystemStore.class);

    private static CaiyunDriverClientService caiyunDriverClientService;

    public CaiyunDriverFileSystemStore(File file) {
    }

    public static void setBean(CaiyunDriverClientService caiyunDriverClientService) {
        CaiyunDriverFileSystemStore.caiyunDriverClientService = caiyunDriverClientService;
    }

    @Override
    public void destroy() {
        LOGGER.info("destroy");

    }

    @Override
    public ITransaction begin(Principal principal, HttpServletRequest req, HttpServletResponse resp) {
        LOGGER.debug("begin");
        return new Transaction(principal, req, resp);
    }

    @Override
    public void checkAuthentication(ITransaction transaction) {
        LOGGER.debug("checkAuthentication");
    }

    @Override
    public void commit(ITransaction transaction) {
        LOGGER.debug("commit");
    }

    @Override
    public void rollback(ITransaction transaction) {
        LOGGER.debug("rollback");

    }

    @Override
    public void createFolder(ITransaction transaction, String folderUri) {
        LOGGER.info("createFolder: {}", folderUri);
        caiyunDriverClientService.createFolder(folderUri);
    }

    @Override
    public void createResource(ITransaction transaction, String resourceUri) {
        LOGGER.info("createResource: {}", resourceUri);
    }

    @Override
    public InputStream getResourceContent(ITransaction transaction, String resourceUri) {
        LOGGER.info("getResourceContent: {}", resourceUri);
        Enumeration<String> headerNames = transaction.getRequest().getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String s = headerNames.nextElement();
            LOGGER.debug("{} request: {} = {}",resourceUri,  s, transaction.getRequest().getHeader(s));
        }
        HttpServletResponse response = transaction.getResponse();
        long size = getResourceLength(transaction, resourceUri);
        Response downResponse = caiyunDriverClientService.download(resourceUri, transaction.getRequest(), size);
        response.setContentLengthLong(downResponse.body().contentLength());
        LOGGER.debug("{} code = {}", resourceUri, downResponse.code());
        for (String name : downResponse.headers().names()) {
            LOGGER.debug("{} downResponse: {} = {}", resourceUri, name, downResponse.header(name));
            response.addHeader(name, downResponse.header(name));
        }
        response.setStatus(downResponse.code());
        return downResponse.body().byteStream();
    }

    @Override
    public long setResourceContent(ITransaction transaction, String resourceUri, InputStream content, String contentType, String characterEncoding) {
        LOGGER.info("setResourceContent {}", resourceUri);
        HttpServletRequest request = transaction.getRequest();
        HttpServletResponse response = transaction.getResponse();

        long contentLength = request.getContentLength();
        if (contentLength < 0) {
            contentLength = Long.parseLong(Optional.ofNullable(request.getHeader("content-length"))
                    .orElse(request.getHeader("X-Expected-Entity-Length")));
        }
        caiyunDriverClientService.uploadPre(resourceUri, contentLength, content);

        if (contentLength == 0) {
            String expect = request.getHeader("Expect");

            // 支持大文件上传
            if ("100-continue".equalsIgnoreCase(expect)) {
                try {
                    response.sendError(100, "Continue");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        }
        return contentLength;
    }

    @Override
    public String[] getChildrenNames(ITransaction transaction, String folderUri) {
//        LOGGER.info("getChildrenNames: {}", folderUri);
        CFile cFile = caiyunDriverClientService.getCFileByPath(folderUri);
        if (cFile.getFileType().equals(FileType.file.name())) {
            return new String[0];
        }
        Set<CFile> cFiles = caiyunDriverClientService.getCFiles(cFile.getFileId());
        return cFiles.stream().map(CFile::getName).toArray(String[]::new);
    }

    @Override
    public long getResourceLength(ITransaction transaction, String path) {
//        LOGGER.info("getResourceLength: {}", path);
        CFile cFile = caiyunDriverClientService.getCFileByPath(path);
        if (cFile == null || cFile.getSize() == null) {
            return 384;
        }

        return cFile.getSize();
    }

    @Override
    public void removeObject(ITransaction transaction, String uri) {
        LOGGER.info("removeObject: {}", uri);
        caiyunDriverClientService.remove(uri);
    }

    @Override
    public boolean moveObject(ITransaction transaction, String destinationPath, String sourcePath) {
        LOGGER.info("moveObject: {} -> {}", sourcePath,destinationPath);

        PathInfo destinationPathInfo = caiyunDriverClientService.getPathInfo(destinationPath);
        PathInfo sourcePathInfo = caiyunDriverClientService.getPathInfo(sourcePath);
        // 名字相同，说明是移动目录
        if (sourcePathInfo.getName().equals(destinationPathInfo.getName())) {
            caiyunDriverClientService.move(sourcePath, destinationPathInfo.getParentPath());
        } else {
            if (!destinationPathInfo.getParentPath().equals(sourcePathInfo.getParentPath())) {
                throw new WebdavException("不支持目录和名字同时修改");
            }
            // 名字不同，说明是修改名字。不考虑目录和名字同时修改的情况
            caiyunDriverClientService.rename(sourcePath, destinationPathInfo.getName());
        }
        return true;
    }

    @Override
    public StoredObject getStoredObject(ITransaction transaction, String uri) {
        LOGGER.info("getStoredObject: {}", uri);
        CFile cFile = caiyunDriverClientService.getCFileByPath(uri);
        if (cFile != null) {
            StoredObject so = new StoredObject();
            so.setFolder(cFile.getFileType().equalsIgnoreCase(FileType.folder.name()));
            so.setResourceLength(getResourceLength(transaction, uri));
            so.setCreationDate(cFile.getCreateTime());
            so.setLastModified(cFile.getUpdateTime());
            return so;
        }
        return null;
    }
}
