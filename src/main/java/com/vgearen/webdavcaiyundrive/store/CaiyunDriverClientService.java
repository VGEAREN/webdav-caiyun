package com.vgearen.webdavcaiyundrive.store;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.vgearen.webdavcaiyundrive.client.CaiyunDriverClient;
import com.vgearen.webdavcaiyundrive.config.CaiyunProperties;
import com.vgearen.webdavcaiyundrive.model.*;
import com.vgearen.webdavcaiyundrive.model.download.DownloadRequest;
import com.vgearen.webdavcaiyundrive.model.download.result.DownloadData;
import com.vgearen.webdavcaiyundrive.model.filelist.FileListRequest;
import com.vgearen.webdavcaiyundrive.model.filelist.result.CatalogList;
import com.vgearen.webdavcaiyundrive.model.filelist.result.ContentList;
import com.vgearen.webdavcaiyundrive.model.filelist.result.FileListData;
import com.vgearen.webdavcaiyundrive.model.filelist.result.PathInfo;
import com.vgearen.webdavcaiyundrive.model.operate.CreateBatchOprTaskReq;
import com.vgearen.webdavcaiyundrive.model.operate.OperateRequest;
import com.vgearen.webdavcaiyundrive.model.operate.RenameContentRequest;
import com.vgearen.webdavcaiyundrive.model.operate.TaskInfo;
import com.vgearen.webdavcaiyundrive.model.operatefolder.CreateCatalogExtReq;
import com.vgearen.webdavcaiyundrive.model.operatefolder.CreateFolderRequest;
import com.vgearen.webdavcaiyundrive.model.operatefolder.RenameFolderRequest;
import com.vgearen.webdavcaiyundrive.model.operatefolder.result.CatalogInfo;
import com.vgearen.webdavcaiyundrive.model.operatefolder.result.CatalogInfoData;
import com.vgearen.webdavcaiyundrive.model.upload.PreUploadRequest;
import com.vgearen.webdavcaiyundrive.model.upload.UploadContentList;
import com.vgearen.webdavcaiyundrive.model.upload.result.PreUploadData;
import com.vgearen.webdavcaiyundrive.model.upload.result.UploadResult;
import com.vgearen.webdavcaiyundrive.util.JsonUtil;
import net.sf.webdav.exceptions.WebdavException;
import okhttp3.Response;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class CaiyunDriverClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CaiyunDriverClientService.class);
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static String rootPath = "/";
    private static int chunkSize = 10485760; // 100MB
    private CFile rootCFile = null;

    private static Cache<String, Set<CFile>> cFilesCache = Caffeine.newBuilder()
            .initialCapacity(128)
            .maximumSize(1024)
            .expireAfterWrite(5, TimeUnit.SECONDS)
            .build();

    private final CaiyunDriverClient client;

    @Autowired
    private CaiyunProperties caiyunProperties;

    @Autowired
    private VirtualCFileService virtualCFileService;
    private PreUploadRequest preUploadRequest;

    public CaiyunDriverClientService(CaiyunDriverClient caiyunDriverClient) {
        this.client = caiyunDriverClient;
        CaiyunDriverFileSystemStore.setBean(this);
    }

    public CFile getCFileByPath(String path) {
        path = normalizingPath(path);

        return getFileIdByPath(path);
    }

    public Set<CFile> getCFiles(String fileId) {
        Set<CFile> cFiles = cFilesCache.get(fileId, key -> {
            // 获取真实的文件列表
            return getCFilesWithNoRepeat(fileId);
        });
        Set<CFile> all = new LinkedHashSet<>(cFiles);
        // 获取上传中的文件列表
//        Collection<CFile> virtualCFiles = virtualCFileService.list(fileId);
//        all.addAll(virtualCFiles);
        return all;
    }

    private Set<CFile> getCFilesWithNoRepeat(String catalogId) {
        List<CFile> cFiles = fileListFromApi(catalogId, 1, new ArrayList<>());
        cFiles.sort(Comparator.comparing(CFile::getUpdateTime).reversed());
        Set<CFile> cFileSet  = new LinkedHashSet<>();
        for (CFile item : cFiles) {
            if (!cFileSet.add(item)) {
                LOGGER.info("当前目录下{} 存在同名文件：{}，文件大小：{}", catalogId, item.getName(), item.getSize());
            }
        }
        // 对文件名进行去重，只保留最新的一个
        return cFileSet;
    }

    private String normalizingPath(String path) {
        path = path.replaceAll("//", "/");
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }

    private CFile getFileIdByPath(String path) {
        if (!StringUtils.hasLength(path)) {
            path = rootPath;
        }
        if (path.equals(rootPath)) {
            return getRootCFile();
        }
        PathInfo pathInfo = getPathInfo(path);
        CFile cFile = getCFileByPath(pathInfo.getParentPath());
        if (cFile == null ) {
            return null;
        }
        return getCFileByParentId(cFile.getFileId(),pathInfo.getName());
    }

    private CFile getCFileByParentId(String parentId, String name) {
        Set<CFile> cFiles = getCFiles(parentId);
        for (CFile cFile : cFiles) {
            if (cFile.getName().equals(name)) {
                return cFile;
            }
        }
        return null;
    }

    public PathInfo getPathInfo(String path) {
        path = normalizingPath(path);
        if (path.equals(rootPath)) {
            PathInfo pathInfo = new PathInfo();
            pathInfo.setPath(path);
            pathInfo.setName(path);
            return pathInfo;
        }
        int index = path.lastIndexOf("/");
        String parentPath = path.substring(0, index + 1);
        String name = path.substring(index+1);
        PathInfo pathInfo = new PathInfo();
        pathInfo.setPath(path);
        pathInfo.setParentPath(parentPath);
        pathInfo.setName(name);
        return pathInfo;
    }

    private CFile getRootCFile() {
        if (rootCFile == null) {
            rootCFile = new CFile();
            rootCFile.setName("/");
            rootCFile.setFileId("00019700101000000001");
            rootCFile.setCreateTime(new Date());
            rootCFile.setUpdateTime(new Date());
            rootCFile.setFileType(FileType.folder.name());
        }
        return rootCFile;
    }

    public List<CFile> fileListFromApi(String catalogID, Integer startNum, List<CFile> all) {
        FileListRequest listQuery = new FileListRequest();
        CommonAccountInfo commonAccountInfo = new CommonAccountInfo();
        commonAccountInfo.setAccount(this.caiyunProperties.getTel());
        listQuery.setCommonAccountInfo(commonAccountInfo);
        listQuery.setCatalogID(catalogID);
        listQuery.setStartNumber(startNum);
        listQuery.setEndNumber(startNum + 99);
        listQuery.setCatalogSortType(0);
        listQuery.setContentSortType(0);
        listQuery.setFilterType(0);
        listQuery.setSortDirection(1);

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

        String json = client.post("/orchestration/personalCloud/catalog/v1.0/getDisk", listQuery);
        CaiyunResponse<FileListData> cFileListResult = JsonUtil.readValue(json, new TypeReference<CaiyunResponse<FileListData>>() {});
        if(null != cFileListResult.getData().getGetDiskResult()){
           List<CatalogList> catalogLists = cFileListResult.getData().getGetDiskResult().getCatalogList();
           if(null != catalogLists){
               for(CatalogList item:cFileListResult.getData().getGetDiskResult().getCatalogList()){
                   CFile cFile = new CFile();
                   cFile.setFileType("folder");
                   cFile.setName(item.getCatalogName());
                   cFile.setFileId(item.getCatalogID());
                   try {
                       Date updateTime = format.parse(item.getUpdateTime());
                       Date createTime = format.parse(item.getCreateTime());
                       cFile.setUpdateTime(updateTime);
                       cFile.setCreateTime(createTime);
                   } catch (ParseException e) {
                       e.printStackTrace();
                   }
                   all.add(cFile);
               }
           }
           List<ContentList> contentLists = cFileListResult.getData().getGetDiskResult().getContentList();
           if(null != contentLists){
               for(ContentList item:cFileListResult.getData().getGetDiskResult().getContentList()){
                   CFile cFile = new CFile();
                   cFile.setFileType("file");
                   cFile.setName(item.getContentName());
                   cFile.setFileId(item.getContentID());
                   try {
                       Date updateTime = format.parse(item.getUpdateTime());
                       Date createTime = format.parse(item.getUploadTime());
                       cFile.setUpdateTime(updateTime);
                       cFile.setCreateTime(createTime);
                   } catch (ParseException e) {
                       e.printStackTrace();
                   }
                   cFile.setSize(item.getContentSize());
                   all.add(cFile);
               }
           }

       }
        if (Integer.valueOf(cFileListResult.getData().getGetDiskResult().getIsCompleted()).equals(1)) {
            return all;
        }
        return fileListFromApi(catalogID, startNum + 100 , all);
    }

    public Response download(String path, HttpServletRequest request, long size ) {
        CFile cFile = getCFileByPath(path);
        CommonAccountInfo commonAccountInfo = new CommonAccountInfo();
        commonAccountInfo.setAccount(this.caiyunProperties.getTel());
        DownloadRequest downloadRequest = new DownloadRequest();
        downloadRequest.setContentID(cFile.getFileId());
        downloadRequest.setCommonAccountInfo(commonAccountInfo);
        String json = client.post("/orchestration/personalCloud/uploadAndDownload/v1.0/downloadRequest", downloadRequest);
        CaiyunResponse<DownloadData> downloadUrl = JsonUtil.readValue(json, new TypeReference<CaiyunResponse<DownloadData>>() {});
        String url = downloadUrl.getData().getDownloadURL();
        LOGGER.debug("{} url = {}", path, url);
        return client.download(url.toString(), request, size);
    }


    public void uploadPre(String path, long size, InputStream inputStream) {
        path = normalizingPath(path);
        PathInfo pathInfo = getPathInfo(path);
        CFile parent = getCFileByPath(pathInfo.getParentPath());
        if (parent == null) {
            return;
        }
        // 如果已存在，先删除
        CFile cFile = getCFileByPath(path);
        if (cFile != null) {
            if (cFile.getSize() == size) {
                //如果文件大小一样，则不再上传
                return;
            }
            remove(path);
        }

        int chunkCount = (int) Math.ceil(((double) size) / chunkSize); // 进1法
        CommonAccountInfo commonAccountInfo = new CommonAccountInfo();
        commonAccountInfo.setAccount(this.caiyunProperties.getTel());
        PreUploadRequest preUploadRequest = new PreUploadRequest();
        preUploadRequest.setParentCatalogID(parent.getFileId());
        preUploadRequest.setManualRename(2);
        preUploadRequest.setOperation(0);
        preUploadRequest.setFileCount(1);
        preUploadRequest.setTotalSize(size);
        preUploadRequest.setCommonAccountInfo(commonAccountInfo);

        UploadContentList uploadContentList = new UploadContentList();
        uploadContentList.setContentName(pathInfo.getName());
        uploadContentList.setContentSize(size);
        preUploadRequest.setUploadContentList(Arrays.asList(uploadContentList));

        LOGGER.info("开始上传文件，文件名：{}，总大小：{}, 文件块数量：{}", path, size, chunkCount);
        String json = client.post("/orchestration/personalCloud/uploadAndDownload/v1.0/pcUploadFileRequest", preUploadRequest);
        CaiyunResponse<PreUploadData> preUploadRes = JsonUtil.readValue(json, new TypeReference<CaiyunResponse<PreUploadData>>() {});
        UploadResult uploadResult = preUploadRes.getData().getUploadResult();
        if(null == uploadResult.getRedirectionUrl()){
            LOGGER.info("{} 秒传成功", path);
            return;
        }

        if (size > 0) {
            virtualCFileService.createCFile(parent.getFileId(), preUploadRes.getData());
        }
        byte[] buffer = new byte[chunkSize];
        if(chunkCount == 0){
            chunkCount++;
        }
        long point = 0;
        for (int i = 0; i < chunkCount; i++) {
            try {
                int read = IOUtils.read(inputStream, buffer, 0, buffer.length);
                if (read == -1) {
                    LOGGER.info("文件上传结束。文件名：{}，当前进度：{}/{}", path, (i + 1), chunkCount);
                    return;
                }
                client.upload(uploadResult.getRedirectionUrl()
                        , buffer, 0, read, uploadResult.getUploadTaskID(),size,point
                        , uploadResult.getNewContentIDList().get(0).getContentName());
                point += read;
                virtualCFileService.updateLength(parent.getFileId()
                        , uploadResult.getNewContentIDList().get(0).getContentID(), buffer.length);
                LOGGER.info("文件正在上传。文件名：{}，当前进度：{}/{}", path, (i + 1), chunkCount);
            } catch (IOException e) {
                virtualCFileService.remove(parent.getFileId(), uploadResult.getNewContentIDList().get(0).getContentID());
                throw new WebdavException(e);
            }
        }

        virtualCFileService.remove(parent.getFileId(), uploadResult.getNewContentIDList().get(0).getContentID());
        LOGGER.info("文件上传成功。文件名：{}", path);
        clearCache();
    }


    public void remove(String path) {
        path = normalizingPath(path);
        CFile cFile = getCFileByPath(path);
        if (cFile == null) {
            return;
        }
        CommonAccountInfo commonAccountInfo = new CommonAccountInfo();
        commonAccountInfo.setAccount(this.caiyunProperties.getTel());

        TaskInfo taskInfo = new TaskInfo();
        if(cFile.getFileType().equalsIgnoreCase(FileType.folder.name())){
            taskInfo.setCatalogInfoList(Arrays.asList(cFile.getFileId()));
            taskInfo.setContentInfoList(new ArrayList<>());
        }else {
            taskInfo.setContentInfoList(Arrays.asList(cFile.getFileId()));
            taskInfo.setCatalogInfoList(new ArrayList<>());
        }

        CreateBatchOprTaskReq createBatchOprTaskReq = new CreateBatchOprTaskReq();
        createBatchOprTaskReq.setTaskType(OperateType.TASK_TYPE_DELETE);
        createBatchOprTaskReq.setActionType(OperateType.ACTION_TYPE_DELETE);
        createBatchOprTaskReq.setCommonAccountInfo(commonAccountInfo);
        createBatchOprTaskReq.setTaskInfo(taskInfo);

        OperateRequest removeRequest = new OperateRequest();
        removeRequest.setCreateBatchOprTaskReq(createBatchOprTaskReq);
        client.post("/orchestration/personalCloud/batchOprTask/v1.0/createBatchOprTask", removeRequest);
        clearCache();
    }

    public void createFolder(String path) {
        path = normalizingPath(path);
        PathInfo pathInfo = getPathInfo(path);
        CFile parent =  getCFileByPath(pathInfo.getParentPath());
        if (parent == null) {
            LOGGER.warn("创建目录失败，未发现父级目录：{}", pathInfo.getParentPath());
            return;
        }
        CommonAccountInfo commonAccountInfo = new CommonAccountInfo();
        commonAccountInfo.setAccount(this.caiyunProperties.getTel());

        CreateCatalogExtReq createCatalogExtReq = new CreateCatalogExtReq();
        createCatalogExtReq.setNewCatalogName(pathInfo.getName());
        createCatalogExtReq.setParentCatalogID(parent.getFileId());
        createCatalogExtReq.setCommonAccountInfo(commonAccountInfo);

        CreateFolderRequest createFileRequest = new CreateFolderRequest();
        createFileRequest.setCreateCatalogExtReq(createCatalogExtReq);

        String json = client.post("/orchestration/personalCloud/catalog/v1.0/createCatalogExt", createFileRequest);
        CaiyunResponse<CatalogInfoData> createFolderRes = JsonUtil.readValue(json, new TypeReference<CaiyunResponse<CatalogInfoData>>() {});
        CatalogInfo catalogInfo = createFolderRes.getData().getCatalogInfo();
        if (catalogInfo.getCatalogName() == null) {
            LOGGER.error("创建目录{}失败: {}",path, json);
        }
        if (!catalogInfo.getCatalogName().equals(pathInfo.getName())) {
            LOGGER.info("创建目录{}与原值{}不同，重命名", catalogInfo.getCatalogName(), pathInfo.getName());
            rename(pathInfo.getParentPath() + "/" + catalogInfo.getCatalogName(), pathInfo.getName());
            clearCache();
        }
        clearCache();
    }
    public void rename(String sourcePath, String newName) {
        sourcePath = normalizingPath(sourcePath);
        CFile cFile = getCFileByPath(sourcePath);
        if(cFile.getFileType().equalsIgnoreCase(FileType.folder.name())){
            RenameFolderRequest renameFolderRequest = new RenameFolderRequest();
            renameFolderRequest.setCatalogID(cFile.getFileId());
            renameFolderRequest.setCatalogName(newName);
            CommonAccountInfo commonAccountInfo = new CommonAccountInfo();
            commonAccountInfo.setAccount(this.caiyunProperties.getTel());
            renameFolderRequest.setCommonAccountInfo(commonAccountInfo);
            client.post("/orchestration/personalCloud/catalog/v1.0/updateCatalogInfo", renameFolderRequest);
        }else {
            RenameContentRequest renameContentRequest = new RenameContentRequest();
            renameContentRequest.setContentID(cFile.getFileId());
            renameContentRequest.setContentName(newName);
            CommonAccountInfo commonAccountInfo = new CommonAccountInfo();
            commonAccountInfo.setAccount(this.caiyunProperties.getTel());
            renameContentRequest.setCommonAccountInfo(commonAccountInfo);
            client.post("/orchestration/personalCloud/content/v1.0/updateContentInfo", renameContentRequest);

        }
        clearCache();
    }

    public void move(String sourcePath, String targetPath) {
        sourcePath = normalizingPath(sourcePath);
        targetPath = normalizingPath(targetPath);

        CFile sourceCFile = getCFileByPath(sourcePath);
        CFile targetCFile = getCFileByPath(targetPath);

        CommonAccountInfo commonAccountInfo = new CommonAccountInfo();
        commonAccountInfo.setAccount(this.caiyunProperties.getTel());

        TaskInfo taskInfo = new TaskInfo();
        if(sourceCFile.getFileType().equalsIgnoreCase(FileType.folder.name())){
            taskInfo.setCatalogInfoList(Arrays.asList(sourceCFile.getFileId()));
            taskInfo.setContentInfoList(new ArrayList<>());
        }else {
            taskInfo.setContentInfoList(Arrays.asList(sourceCFile.getFileId()));
            taskInfo.setCatalogInfoList(new ArrayList<>());
        }
        taskInfo.setNewCatalogID(targetCFile.getFileId());
        CreateBatchOprTaskReq createBatchOprTaskReq = new CreateBatchOprTaskReq();
        createBatchOprTaskReq.setTaskType(OperateType.TASK_TYPE_MOVE);
        createBatchOprTaskReq.setActionType(OperateType.ACTION_TYPE_MOVE);
        createBatchOprTaskReq.setCommonAccountInfo(commonAccountInfo);
        createBatchOprTaskReq.setTaskInfo(taskInfo);

        OperateRequest operateRequest = new OperateRequest();
        operateRequest.setCreateBatchOprTaskReq(createBatchOprTaskReq);

        client.post("/orchestration/personalCloud/batchOprTask/v1.0/createBatchOprTask", operateRequest);
        clearCache();
    }
    private void clearCache() {
        cFilesCache.invalidateAll();
    }
}
