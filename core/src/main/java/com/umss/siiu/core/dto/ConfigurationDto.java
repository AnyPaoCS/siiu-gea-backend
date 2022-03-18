package com.umss.siiu.core.dto;

import com.umss.siiu.core.model.Configuration;

public class ConfigurationDto extends DtoBase<Configuration> {

    private String type;
    private String entryKey;
    private String value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEntryKey() {
        return entryKey;
    }

    public void setEntryKey(String entryKey) {
        this.entryKey = entryKey;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
