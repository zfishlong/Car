/**
 * Copyright (c) 2012-2015 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : StringUtil.java
 * @ProjectName : uniCarSolution_dev_xd_20151010
 * @PakageName : com.unisound.unicar.gui.utils
 * @version : 1.0
 * @Author : Xiaodong.He
 * @CreateDate : 2015-10-30
 */
package com.unisound.unicar.gui.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;

import android.text.TextUtils;

/**
 * 
 * @author xiaodong.he
 * @date 2015-10-30
 */
public class StringUtil {

    private static final String TAG = StringUtil.class.getSimpleName();

    /**
     * 
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String clearSpecialCharacter(String str) throws PatternSyntaxException {
        if (null == str || "".equals(str)) {
            return str;
        }
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 
     * @param text
     * @param wakeupWord
     * @return
     */
    public static String clearWakeupWord(String text, String wakeupWord) {
        if (!TextUtils.isEmpty(text) && text.contains(wakeupWord)) {
            Logger.d(TAG, "clearWakeupWord---begin--wakeupWord = " + wakeupWord + "; text: " + text);
            text = text.replace(wakeupWord, "");
        }
        return text;
    }

    /**
     * check a string whether a Mobile Number
     * 
     * @param mobiles
     * @return
     */
    public static boolean isMobileNumber(String mobiles) {
        if (mobiles == null || "".equals(mobiles)) {
            return false;
        }
        mobiles = mobiles.replaceAll(" ", "");
        mobiles = mobiles.replaceAll("-", "");
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 
     * @author xiaodong.he
     * @date 2016-3-23
     * @param jsonArray
     * @return "value-0、...、value-n"
     */
    public static String getJSONArrayStringValues(JSONArray jsonArray) {
        if (jsonArray == null || jsonArray.length() == 0) {
            return "";
        }
        try {
            int lenth = jsonArray.length();
            StringBuffer nameString = new StringBuffer();
            for (int i = 0; i < lenth; i++) {
                String value = jsonArray.getString(i);
                if (i == lenth - 1) {
                    nameString.append(value);
                } else {
                    nameString.append(value + "、");
                }
            }
            return nameString.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

}
