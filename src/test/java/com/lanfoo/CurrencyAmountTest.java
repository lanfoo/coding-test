package com.lanfoo;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * @Auther: Kevin Cui
 * @Date: 2019/10/10 17
 * @Description:
 */
public class CurrencyAmountTest {
    @Test
    public void test() {
        final CurrencyAmount currencyAmount = new CurrencyAmount();
        for (int i = 0; i < 100; i++) {
            final int tempI = i;
            new Thread(new Runnable() {
                public void run() {
                    currencyAmount.put(String.valueOf(tempI), BigDecimal.valueOf(tempI));
                }
            }, String.valueOf(tempI)).start();
        }

        for (int i = 0; i < 100; i++) {
            final int tempI = i;
            new Thread(new Runnable() {
                public void run() {
                    currencyAmount.print();
                }
            }, String.valueOf(tempI)).start();
        }
    }
}
