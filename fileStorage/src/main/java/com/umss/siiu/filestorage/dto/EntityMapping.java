/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.filestorage.dto;

import java.util.HashMap;
import java.util.Map;

public class EntityMapping {
    private Map<String, String> entries = new HashMap<>();

    public Map<String, String> getEntries() {
        return entries;
    }

    public void setEntries(Map<String, String> entries) {
        this.entries = entries;
    }
}
