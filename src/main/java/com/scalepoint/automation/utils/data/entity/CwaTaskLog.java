package com.scalepoint.automation.utils.data.entity;

import com.scalepoint.ecc.thirdparty.integrations.model.cwa.TaskType;
import com.scalepoint.ecc.thirdparty.integrations.model.enums.EventType;

/**
 * Created by bza on 5/26/2017.
 */
public class CwaTaskLog {

    private int claimId;
    private TaskType taskType;
    private String taskId;
    private EventType taskStatus;
    private String taskPayload;

    public int getClaimId() {
        return claimId;
    }

    public void setClaimId(int claimId) {
        this.claimId = claimId;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public EventType getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(EventType taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskPayload() {
        return taskPayload;
    }

    public void setTaskPayload(String taskPayload) {
        this.taskPayload = taskPayload;
    }
}
