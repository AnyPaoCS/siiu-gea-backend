/**
 * @author: Edson A. Terceros T.
 */

package com.siiu.umss.siiu.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.Date;

@Entity
public class Contract extends ModelBase {
    @ManyToOne(optional = false)
//    @JoinColumn(name = "emp_id")
    private Employee employee;

    @OneToOne(optional = false)
    private Position position;

    private Date initDate;
    private Date endDate;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Date getInitDate() {
        return initDate;
    }

    public void setInitDate(Date initDate) {
        this.initDate = initDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
