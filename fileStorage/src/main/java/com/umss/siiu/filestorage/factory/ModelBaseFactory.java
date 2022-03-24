package com.umss.siiu.filestorage.factory;


import com.umss.siiu.bpmn.model.Job;
import com.umss.siiu.core.model.ModelBase;
import com.umss.siiu.core.model.User;

public class ModelBaseFactory {

    private ModelBaseFactory() {

    }

    public static ModelBase<?> createModelBase(String type, Long id) {
        ModelBase<?> modelBase = null;

        switch (type) {
            case "job":
                modelBase = new Job();
                break;
            case "user":
                modelBase = new User();
                break;
            default:
                modelBase = new ModelBase<>();
                break;
        }
        modelBase.setId(id);
        return modelBase;
    }
}
