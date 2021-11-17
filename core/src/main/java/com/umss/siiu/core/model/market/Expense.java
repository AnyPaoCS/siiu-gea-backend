/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.model.market;

import com.umss.siiu.core.dto.market.ExpenseDto;
import com.umss.siiu.core.model.ModelBase;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class Expense extends ModelBase<ExpenseDto> {
    @Enumerated(EnumType.STRING)
    private ExpenseType expenseType;
    private Long value;
    private String description;

    public ExpenseType getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(ExpenseType expenseType) {
        this.expenseType = expenseType;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
