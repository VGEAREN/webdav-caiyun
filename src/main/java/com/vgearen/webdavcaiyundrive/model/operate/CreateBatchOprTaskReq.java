
package com.vgearen.webdavcaiyundrive.model.operate;

import com.vgearen.webdavcaiyundrive.model.CommonAccountInfo;

public class CreateBatchOprTaskReq {

    
    private Integer actionType;
    
    private CommonAccountInfo commonAccountInfo;
    
    private TaskInfo taskInfo;
    
    private Integer taskType;

    public Integer getActionType() {
        return actionType;
    }

    public void setActionType(Integer actionType) {
        this.actionType = actionType;
    }

    public CommonAccountInfo getCommonAccountInfo() {
        return commonAccountInfo;
    }

    public void setCommonAccountInfo(CommonAccountInfo commonAccountInfo) {
        this.commonAccountInfo = commonAccountInfo;
    }

    public TaskInfo getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(TaskInfo taskInfo) {
        this.taskInfo = taskInfo;
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

}
