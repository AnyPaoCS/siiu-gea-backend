/**
 * @author: Edson A. Terceros T.
 * 17
 */

package com.umss.siiu.core.model;

import com.umss.siiu.core.dto.BuyDto;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class Buy extends ModelBase<BuyDto> {

    @Column(precision = 10, scale = 5)
    private BigDecimal value;

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
