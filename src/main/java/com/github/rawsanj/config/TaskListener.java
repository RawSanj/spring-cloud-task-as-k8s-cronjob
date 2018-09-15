package com.github.rawsanj.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.task.listener.TaskExecutionListener;
import org.springframework.cloud.task.repository.TaskExecution;

public class TaskListener implements TaskExecutionListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(TaskListener.class.getName());

    @Override
    public void onTaskStartup(TaskExecution taskExecution) {
        LOGGER.info("Load Currency Task - STARTED...!");
    }

    @Override
    public void onTaskEnd(TaskExecution taskExecution) {
        LOGGER.info("Load Currency Task - COMPLETED...!");
        if (taskExecution.getExitMessage() == null){
            taskExecution.setExitMessage("Success!");
        }
    }

    @Override
    public void onTaskFailed(TaskExecution taskExecution, Throwable throwable) {
        LOGGER.info("Load Currency Task - FAILED...!");
        LOGGER.info("Task Failed due to: {} because {}", throwable.getMessage(), throwable.getCause().getMessage());
        taskExecution.setExitMessage("Failed!");
        taskExecution.setErrorMessage(throwable.getMessage());
    }
}
