package com.umss.siiu.core.repository;

import com.umss.siiu.core.model.Buy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BuyDelayRepositoryTest {
    @Autowired
    private BuyDelayRepository buyDelayRepository;

    @Test
    public void test() {
        Buy buy = new Buy();
        BigDecimal expected = new BigDecimal("23");
        buy.setValue(expected);
        Buy buyPersisted = buyDelayRepository.save(buy);
        Buy searchResult = buyDelayRepository.getByIdDelay(buyPersisted.getId());
        assertEquals(0, searchResult.getValue().compareTo(expected));
    }
}
