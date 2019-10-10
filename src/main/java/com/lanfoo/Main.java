package com.lanfoo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @Auther: Kevin Cui
 * @Date: 2019/10/10 17
 * @Description:
 */
public class Main {
    private final static String CURRENCY_CODE_REGEX = "^[a-zA-Z]{3} [-]?\\d+(\\.\\d+)?$";

    public static void main(String[] args) {
        final CurrencyAmount currencyAmount = new CurrencyAmount();
        new Thread(new Runnable() {
            public void run() {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Types \"quit\" to exit.");
                System.out.println("Please enter the filename of import data, types \"skip\" to skip.");

                while (true) {
                    String message = scanner.nextLine();
                    if ("skip".equals(message)) {
                        currencyAmount.setSkip(true);
                    } else if (!currencyAmount.isSkip()) {
                        try {
                            List<String> result = currencyAmount.readFile(message);
                            for (String str : result) {
                                currencyAmount.put(str.split(" ")[0], BigDecimal.valueOf(Double.valueOf(str.split(" ")[1])));
                            }
                            System.out.println("Import successful.");
                            currencyAmount.setSkip(true);
                        } catch (FileNotFoundException e) {
                            System.out.println("File not found, types \"skip\" to skip.");
                        } catch (IOException e) {
                            System.out.println("Unknown error, types \"skip\" to skip.");
                        }
                    } else {
                        if (message.equals("print")) {
                            currencyAmount.print();
                        } else if (message.equals("clear")) {
                            currencyAmount.clear();
                        } else {
                            if (Pattern.matches(CURRENCY_CODE_REGEX, message)) {
                                currencyAmount.put(message.split(" ")[0], BigDecimal.valueOf(Double.valueOf(message.split(" ")[1])));
                            } else {
                                System.out.println("Invalid date format. Please enter the date in the format like 'USD 1000'");
                            }
                        }
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        currencyAmount.print();
                        TimeUnit.SECONDS.sleep(60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
