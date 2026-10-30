package com.donga.service;
import java.math.*;
/** Monetary calculation used by payroll, independent of DB. */
public final class PayrollMath {
 private PayrollMath(){}
 public static BigDecimal net(BigDecimal basic,BigDecimal allowances,BigDecimal workedHours,BigDecimal standardHours,BigDecimal weightedOvertimeHours,BigDecimal bonus,BigDecimal penalty,BigDecimal insurance){
  if(basic.signum()<0||standardHours.signum()<=0||workedHours.signum()<0||weightedOvertimeHours.signum()<0)throw new IllegalArgumentException("Số giờ/mức lương không hợp lệ");
  BigDecimal hourly=basic.divide(standardHours,8,RoundingMode.HALF_UP);
  BigDecimal regular=hourly.multiply(workedHours).setScale(2,RoundingMode.HALF_UP);
  BigDecimal overtime=hourly.multiply(weightedOvertimeHours).setScale(2,RoundingMode.HALF_UP);
  return regular.add(allowances).add(overtime).add(bonus).subtract(penalty).subtract(insurance).setScale(2,RoundingMode.HALF_UP);
 }
}
