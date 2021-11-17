package com.umss.siiu.core.dto.market;

import com.umss.siiu.core.dto.DtoBase;
import com.umss.siiu.core.model.market.Expense;
import com.umss.siiu.core.model.market.ExpenseType;

public class ExpenseDto extends DtoBase<Expense> {
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
