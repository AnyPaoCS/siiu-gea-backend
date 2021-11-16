/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.bpmn.model.processes;

public enum TaskStatus {
    PENDING,// asociado al sistema
    ALLOCATED, //reasignacion-> si es pending alocated.
    IN_PROGRESS,
    ON_HOLD,
    DONE;

    @Override
    public String toString() {
        return super.toString().toLowerCase().replace('_', ' ');
    }
}
