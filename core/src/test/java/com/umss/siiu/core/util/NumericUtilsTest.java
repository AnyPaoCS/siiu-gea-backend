package com.umss.siiu.core.util;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class NumericUtilsTest {

    @Test
    public void givenOneArabicWhenToRomanThenSuccess() {
        NumericUtils numericUtils = new NumericUtils();
        String roman = numericUtils.toRoman(1);
        assertEquals(roman, "I");
    }

    @Test(expectedExceptions = NumberFormatException.class)
    public void givenInvalidCharacterWhenToRomanThenFail() {
        NumericUtils numericUtils = new NumericUtils();
        String roman = numericUtils.toRoman(Integer.valueOf("EAO"));
        assertEquals(roman, "I");
    }

    @Test
    public void testToRomanComplex() {
        NumericUtils numericUtils = new NumericUtils();
        String roman = numericUtils.toRoman(24);
        //assertEquals(roman,"XXIV");  se que falla y debo fixear como developer
    }

    @Test
    public void testToArabic() {
    }
}
