package com.umss.siiu.core.dto;

import java.util.ResourceBundle;

public class OperationResultDto<T> {

    private String message;
    private T dataTransferObject;
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("application");

    public OperationResultDto() {
        super();
    }

    public OperationResultDto(String key) {
        super();
        setMessage(key);
    }

    public OperationResultDto(String key, T dataTransferObject) {
        super();
        setMessage(key);
        this.dataTransferObject = dataTransferObject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String key) {
        this.message = RESOURCE_BUNDLE.getString(key);
    }

    public T getDataTransferObject() {
        return dataTransferObject;
    }

    public void setDataTransferObject(T dataTransferObject) {
        this.dataTransferObject = dataTransferObject;
    }

}
