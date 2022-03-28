package com.umss.siiu.siiurest.services;

import com.umss.siiu.bpmn.service.ProcessInstanceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ProcessInstanceServiceIT {
    @Autowired
    private ProcessInstanceService processInstanceService;

    @Test
    public void createProcessInstanceTest() {
        processInstanceService.createProcessInstance();
    }
}
