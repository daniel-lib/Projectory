package com.app.projectory.dto;

public class BoardTaskView {
  private final Long taskId;
  private final String taskName;
  private final String taskDescription;
  private final String status;
  private final String assigneeId;
  private final String assigneeName;
  private final String assigneeProfileUrl;
  private final boolean canMove;

  public BoardTaskView(
      Long taskId,
      String taskName,
      String taskDescription,
      String status,
      String assigneeId,
      String assigneeName,
      String assigneeProfileUrl,
      boolean canMove) {
    this.taskId = taskId;
    this.taskName = taskName;
    this.taskDescription = taskDescription;
    this.status = status;
    this.assigneeId = assigneeId;
    this.assigneeName = assigneeName;
    this.assigneeProfileUrl = assigneeProfileUrl;
    this.canMove = canMove;
  }

  public Long getTaskId() {
    return taskId;
  }

  public String getTaskName() {
    return taskName;
  }

  public String getTaskDescription() {
    return taskDescription;
  }

  public String getStatus() {
    return status;
  }

  public String getAssigneeId() {
    return assigneeId;
  }

  public String getAssigneeName() {
    return assigneeName;
  }

  public String getAssigneeProfileUrl() {
    return assigneeProfileUrl;
  }

  public boolean isCanMove() {
    return canMove;
  }
}
