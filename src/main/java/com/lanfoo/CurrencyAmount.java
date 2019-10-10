package com.lanfoo;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Pattern;

/**
 * @Auther: Kevin Cui
 * @Date: 2019/10/10 10
 * @Description: 程序使用了读写锁来保证线程安全
 */
public class CurrencyAmount {
    private volatile Map<String, BigDecimal> currencyAmountMap = new HashMap<String, BigDecimal>();
    private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final static String CURRENCY_CODE_REGEX = "^[a-zA-Z]{3} [-]?\\d+(\\.\\d+)?$";
    private boolean skip;

    /**
     * 写操作
     *
     * @param key
     * @param value
     */
    public void put(String key, BigDecimal value) {
        rwLock.writeLock().lock();
        try {
            if (currencyAmountMap.get(key) == null) {
                currencyAmountMap.put(key, BigDecimal.valueOf(0));
            }
            currencyAmountMap.put(key, currencyAmountMap.get(key).add(value));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    /**
     * 读操作
     */
    public void print() {
        rwLock.readLock().lock();
        try {
            for (Map.Entry<String, BigDecimal> entry : currencyAmountMap.entrySet()) {
                if (!BigDecimal.valueOf(0).equals(entry.getValue())) {
                    System.out.println(entry.getKey() + " " + entry.getValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rwLock.readLock().unlock();
        }
    }

    /**
     * 清空操作
     */
    public void clear() {
        rwLock.writeLock().lock();
        try {
            currencyAmountMap.clear();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    /**
     * 导入数据
     *
     * @param path
     * @return
     * @throws IOException
     */
    public List<String> readFile(String path) throws IOException {
        List<String> result = new ArrayList<String>();
        FileInputStream fileInputStream = new FileInputStream(path);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            if (Pattern.matches(CURRENCY_CODE_REGEX, line)) {
                result.add(line);
            }
        }
        bufferedReader.close();
        inputStreamReader.close();
        fileInputStream.close();
        return result;
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }
}
