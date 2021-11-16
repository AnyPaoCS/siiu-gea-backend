/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.bpmn.model;

public enum JobStatus {
    QUEUED,
    PREPARED,// termino bootstrap y se crearon las 6 tareas creadas download
    FINISHED,
    ON_HOLD,
    IN_PROGRESS;

    @Override
    public String toString() {
        return super.toString().toLowerCase().replace('_', ' ');
    }
}
