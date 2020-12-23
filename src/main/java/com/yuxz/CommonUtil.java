package com.yuxz;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @package: PACKAGE_NAME
 * @class: transaction.tidb.com.yuxz.CommonUtil
 * @description:
 * @author: yuxiuzhen
 * @date: Created in 2020/12/19 11:46 PM
 * @version: V1.0
 */
public class CommonUtil {


    /**
     * 检查文件是否存在及是否存在空文件
     *
     * @param sqlFiles 检查的文件列表
     * @return 是否异常 true:存在异常 false:不存在异常
     */
    public static boolean checkFileExist(String[] sqlFiles) {

        boolean hasError = false;
        for (String sqlFile : sqlFiles) {
            File file = new File(sqlFile);
            // 如果文件夹不存在则创建
            if (!file.exists() && !file.isDirectory()) {
                System.out.println("文件不存在,请检查文件: " + sqlFile);
                hasError = true;
                continue;
            }

            if (file.length() == 0) {
                System.out.println("没有sql,请检查空文件: " + sqlFile);
                hasError = true;
                continue;
            }
        }
        return hasError;

    }

    /**
     * 用sql生成长度为strLength的list
     *
     * @param strLength 生成的数组长度
     * @param sql       生成字符串的字符
     * @return
     */
    public static List<Integer> generatePseudoSqls(int strLength, int sql) {
        List<Integer> pseudoSqlList = new ArrayList(strLength);
        for (int i = 0; i < strLength; i++) {
            pseudoSqlList.add(sql);
        }
        return pseudoSqlList;
    }

    /**
     * 计算文件的行数
     *
     * @param filePath 文件名(包括所在路径)
     * @return 文件行数
     */
    public static int getFileLineNum(String filePath) {
        LineNumberReader lineNumberReader = null;
        int lineNumber;
        try {
            lineNumberReader = new LineNumberReader(new FileReader(filePath));
            lineNumberReader.skip(Long.MAX_VALUE);
            lineNumber = lineNumberReader.getLineNumber();

        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("文件不存在,请检查文件: " + filePath);
            return -1;
        } catch (Exception e) {
            System.out.println("lineNumberReader 关闭失败!");
            return -1;
        } finally {
            try {
                if (lineNumberReader != null) {
                    lineNumberReader.close();
                }
            } catch (IOException e) {
                System.out.println("lineNumberReader 关闭失败!");
                return -1;
            }
        }
        return lineNumber + 1;
    }

}
