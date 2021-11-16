package com.umss.siiu.bpmn.model;

import com.umss.siiu.bpmn.dto.ConfigurationDto;
import com.umss.siiu.core.model.ModelBase;

import javax.persistence.Entity;

@Entity
public class Configuration extends ModelBase<ConfigurationDto> {
    private String type;
    private String entryKey;
    private String value;

    private boolean active;

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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
