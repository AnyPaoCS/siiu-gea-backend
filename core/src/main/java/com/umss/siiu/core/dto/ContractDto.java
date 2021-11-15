/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.dto;

import com.umss.siiu.core.model.Contract;

public class ContractDto extends DtoBase<Contract> {
    private PositionDto position;
    private String positionName;

    public PositionDto getPosition() {
        return position;
    }

    public void setPosition(PositionDto position) {
        this.position = position;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }
}
