/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.dto.market;

import com.umss.siiu.core.dto.DtoBase;
import com.umss.siiu.core.model.market.Sale;

import java.util.Date;

public class SaleDto extends DtoBase<Sale> {
    private Long employeeId;
    private Date date;
    private String description;

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
