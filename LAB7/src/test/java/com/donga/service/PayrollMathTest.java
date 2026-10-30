package com.donga.service;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
class PayrollMathTest {
 @Test void salaryWorkedFullMonthAndOvertime(){
  BigDecimal actual=PayrollMath.net(new BigDecimal("8000000"),new BigDecimal("1000000"),new BigDecimal("160"),new BigDecimal("160"),new BigDecimal("15"),new BigDecimal("500000"),new BigDecimal("200000"),new BigDecimal("840000"));
  assertEquals(new BigDecimal("9210000.00"),actual);
 }
 @Test void invalidStandardHours(){assertThrows(IllegalArgumentException.class,()->PayrollMath.net(BigDecimal.ONE,BigDecimal.ZERO,BigDecimal.ONE,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO));}
}
