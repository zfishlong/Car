/**
 * Copyright (c) 2012-2016 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : DateUtil.java
 * @ProjectName : uniCarSolution
 * @PakageName : com.unisound.unicar.gui.utils
 * @version : 1.0
 * @Author : Xiaodong.He
 * @CreateDate : 2016-2-16
 */
package com.unisound.unicar.gui.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author xiaodong.he
 * @date 2016-2-16
 */
public class DateUtil {

    public static final String TAG = DateUtil.class.getSimpleName();

    /**
     * 
     * @param dateStr
     * @param formaterString: e.g: "yyyy-MM-dd"
     * @return
     */
    public static Date string2Date(String dateStr, String formaterString) {
        Date date = null;
        SimpleDateFormat formater = new SimpleDateFormat();
        formater.applyPattern(formaterString);
        try {
            date = formater.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 
     * @param date
     * @param formaterString
     * @return
     */
    public static String date2String(Date date, String formaterString) {
        return new SimpleDateFormat(formaterString).format(date);
    }

    /**
     * 
     * @param dueDate
     * @return
     */
    public static boolean isDueDate(Date dueDate) {
        if (dueDate == null) {
            return true;
        }
        Date cDate = new Date();
        if (cDate.getTime() > dueDate.getTime()) {
            return true;
        } else {
            return false;
        }
    }
}
