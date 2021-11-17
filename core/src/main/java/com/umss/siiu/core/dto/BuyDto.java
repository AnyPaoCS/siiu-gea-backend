/**
 * @author: Edson A. Terceros T.
 * 17
 */

package com.umss.siiu.core.dto;

import com.umss.siiu.core.model.Buy;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;

public class BuyDto extends DtoBase<Buy> {

    private BigDecimal value;
    private Integer transactionNumber;

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Integer getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(Integer transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    //
    // contro  serv   ->   dto.setF1(model.getF1)
    // dto.setF2(model.getF2)
    // dto.setF7(model.getF7) //
    // new dto (m.f1, m.f2, f3, f4 ...m7.); //
    // practica Address a S f1, Intge f2  AddressTwo a2 S p1, int f2
    // mapper mapper.map(a, a2);
    // a2  a  a->a2   Typo y nombre
    // profundiad m.F1   mP1

    //anotaciones  @Target abc12ab, DTONEsted, abc12ab.b.l.z
    //f1

    //map  json

    @Override
    public BuyDto toDto(Buy buy, ModelMapper mapper) {
        BuyDto buyDto = new BuyDto();
        buyDto.setValue(buy.getValue());
        BuyDto dtoBase = super.toDto(buy, mapper);
        // buyDto.setTransacNum(Intger.valueOf(buy.getF1)     en caso que no coincide tipo o nombre de atributo

        /*
        M.f1   D.f1  String     si
        M.f2   D.f2  Boolean    si
        M.f2   D.F2  Boolean    no   -> sobreeescribir toDto  o beforeConversion afterConversion
        * */
        return dtoBase;
    }

   /* @Override
    protected void beforeConversion(Buy buy, ModelMapper mapper) {
        super.beforeConversion(buy, mapper);
        // setTransacNum(Intger.valueOf(buy.getF1)     en caso que no coincide tipo o nombre de atributo
        setTransactionNumber(buy.getValue().intValue());
    }*/
}
