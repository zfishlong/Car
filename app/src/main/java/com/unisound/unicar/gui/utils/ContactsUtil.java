/**
 * Copyright (c) 2012-2015 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : ContactsUtil.java
 * @ProjectName : uniCarSolution
 * @PakageName : com.unisound.unicar.gui.utils
 * @version : 1.2
 * @Author : Xiaodong.He
 * @CreateDate : 2015-7-7
 */
package com.unisound.unicar.gui.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.util.Log;

import com.unisound.unicar.gui.model.ContactInfo;

/**
 * Contacts Util
 * 
 * @author xiaodong
 * @date 20150707
 * @ModifyDate 2015-12-7
 */
public class ContactsUtil {

    private static final String TAG = ContactsUtil.class.getSimpleName();

    private static final String[] PROJECTION_CONTACTS = new String[] {
            ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.PHOTO_ID, ContactsContract.Contacts.HAS_PHONE_NUMBER};

    private static final String CONTACT_SELECT_ALL = Contacts.DISPLAY_NAME + " NOTNULL AND "
            + Contacts.DISPLAY_NAME + " != '' ";

    private static ConcurrentHashMap<Integer, String> mExampleContactMap =
            new ConcurrentHashMap<Integer, String>();

    /**
     * get Default Contact Name when no contact find
     * 
     * @param context
     * @return
     */
    // private static String getDefaultConatctName(Context context){
    // boolean isNetworkConnect = Network.checkNetworkConnected(context);
    // Logger.d(TAG, "!--->getDefaultConatctName--isNetworkConnect = " + isNetworkConnect);
    // return isNetworkConnect ? context.getString(R.string.default_name_no_contact_online)
    // : context.getString(R.string.default_name_no_contact_offline);
    // }

    /**
     * has Contact With Name in db
     * 
     * @param ctx
     * @return
     */
    // public static boolean hasContactWithName(Context ctx){
    // boolean hasContactWithName = false;
    // Map<Integer, String> contactMap = getContactMap(ctx);
    // if (contactMap.size() > 0) {
    // hasContactWithName = true;
    // }
    // return hasContactWithName;
    // }

    /**
     * 
     * @param context
     * @param domainHelpText
     * @return
     */
    // public static String getHelpTextWithContactName(Context context, String domainHelpText){
    // String textShow = domainHelpText;
    // String contactName = ContactsUtil.getRandomContactName(context);
    //
    // //if no contact find, use default name
    // if ("".equals(contactName) || null == contactName) {
    // contactName = getDefaultConatctName(context);
    // }
    //
    // Object[] nameFormatParam = new Object[1];
    // nameFormatParam[0] = contactName;
    // try {
    // textShow = String.format(domainHelpText, nameFormatParam);
    // } catch (Exception e) {
    // textShow = domainHelpText;
    // Logger.w(TAG, "!--->String.format error! Text = "+domainHelpText);
    // }
    // Logger.d(TAG, "!--->contactName = "+contactName+"; textShow = "+textShow);
    // return textShow;
    // }

    /**
     * get a random Contact Name from Contact DB
     * 
     * @param ctx
     * @return randomContactName or "" if no Contact Name find
     */
    public static String getRandomContactName(Context ctx) {
        String name = "";
        Map<Integer, String> contactMap = getContactMap(ctx);
        Integer[] keys = contactMap.keySet().toArray(new Integer[0]);
        if (keys.length > 0) {
            Random random = new Random();
            Integer randomKey = keys[random.nextInt(keys.length)];
            name = contactMap.get(randomKey);
        }
        Logger.d(TAG, "!--->getRandomContactName----name = " + name);
        contactMap = null;
        return name;
    }

    /**
     * 
     * @param ctx
     * @param mContactSyncListener
     * @return "" if mExampleContactMap is empty
     */
    public static String getRandomContactName(final Context ctx,
            final ContactAsyncListener mContactSyncListener) {
        String name = "";
        Map<Integer, String> contactMap = getContactMap(ctx, mContactSyncListener);
        Integer[] keys = contactMap.keySet().toArray(new Integer[0]);
        if (keys.length > 0) {
            Random random = new Random();
            Integer randomKey = keys[random.nextInt(keys.length)];
            name = contactMap.get(randomKey);
        }
        Logger.d(TAG, "!--->getRandomContactName----name = " + name);
        contactMap = null;
        return name;
    }

    /**
     * update Contact Example Map
     * 
     * @author xiaodong.he
     * @date 2015-12-7
     * @param contactInfoList
     */
    public static void updateContactExampleMap(ArrayList<ContactInfo> contactInfoList) {
        if (contactInfoList == null || contactInfoList.size() <= 0) {
            Logger.d(TAG, "updateContactExampleMap---contactList is null");
            return;
        }
        mExampleContactMap.clear();
        int contactListSize = contactInfoList.size();
        int count =
                (contactListSize < GUIConfig.MAX_EXAMPLE_CONTACT_NUM)
                        ? contactListSize
                        : GUIConfig.MAX_EXAMPLE_CONTACT_NUM;
        Logger.d(TAG, "updateContactExampleMap---contactListSize = " + contactListSize);
        for (int i = 0; i < count; i++) {
            ContactInfo cInfo = contactInfoList.get(i);
            mExampleContactMap.put(cInfo.getId(), cInfo.getDisplayName());
        }
        Logger.d(TAG, "updateContactExampleMap--End-MapSize = " + mExampleContactMap.size());
    }

    /**
     * 
     * @return mExampleContactMap
     */
    // public static ConcurrentHashMap<Integer, String> getmExampleContactMap() {
    // return mExampleContactMap;
    // }

    /**
     * get Contact Example Map
     * 
     * @param ctx
     * @return
     */
    public static Map<Integer, String> getContactMap(Context ctx) {
        return getContactMap(ctx, null);
    }

    /**
     * getContactMap: if mExampleContactMap is empty, then get ContactMa pAsync On Copfile
     * 
     * @author xiaodong.he
     * @date 2015-12-07
     * @param ctx
     * @param listener
     * @return mExampleContactMap
     */
    public static Map<Integer, String> getContactMap(Context ctx, ContactAsyncListener listener) {
        if (mExampleContactMap.size() == 0) {
            Logger.w(TAG, "getContactMap--map is null, begin getContactMapAsyncOnCopfile...");
            // getContactMapAsyncOnDB(ctx, listener);
            getContactMapAsyncOnCopfile(listener);
        }
        return mExampleContactMap;
    }

    /**
     * 
     * @param ctx
     * @return
     */
    // private static Map<Integer, String> getContactMapSync(Context ctx) {
    // Map<Integer, String> contactMap = new HashMap<Integer, String>();
    // Cursor cursor = null;
    // try {
    // cursor =
    // ctx.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null,
    // null, null, null);
    // int contactIdIndex = 0;
    // int nameIndex = 0;
    // if (cursor == null) {// XD added 20150908
    // return contactMap;
    // }
    // if (cursor.getCount() > 0) {
    // contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
    // nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
    // }
    // while (cursor.moveToNext() || contactMap.size() < GUIConfig.MAX_EXAMPLE_CONTACT_NUM) {
    // String contactId = cursor.getString(contactIdIndex);
    // String name = cursor.getString(nameIndex);
    // // Logger.d(TAG, "!--->contactId = " + contactId + "; name = " + name);
    //
    // int cid = 0;
    // try {
    // cid = Integer.parseInt(contactId);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // // if name if is phone number, do not save it
    // if (!isMobileNumber(name)) {
    // contactMap.put(cid, name);
    // } else {
    // // Logger.d(TAG, "name = " + name + " is Mobile Number");
    // }
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // } finally {
    // if (null != cursor) {
    // cursor.close();
    // }
    // }
    // return contactMap;
    // }

    /**
     * get Contact Map asynchronously On DataBase
     * 
     * @author xiaodong.he
     * @date 2015-12-7
     * @param ctx
     * @param listener
     */
    public static void getContactMapAsyncOnDB(final Context ctx, final ContactAsyncListener listener) {
        Thread queryThread = new Thread() {
            @Override
            public void run() {
                Logger.d(TAG, "getContactMapAsync--------Begin");
                if (mExampleContactMap != null && mExampleContactMap.size() > 0) {
                    mExampleContactMap.clear();
                }
                Cursor cursor = null;
                try {
                    // cursor =
                    // ctx.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                    // null, null, null, null);
                    cursor =
                            query(ctx, ctx.getContentResolver(),
                                    ContactsContract.Contacts.CONTENT_URI, PROJECTION_CONTACTS,
                                    CONTACT_SELECT_ALL, null, null);
                    int contactIdIndex = 0;
                    int nameIndex = 0;
                    if (cursor == null) {// XD added 20150908
                        if (null != listener) {
                            listener.onQueryError();
                            Logger.w(TAG, "getContactMapAsync---cursor is null--onQueryError");
                        }
                        return;
                    }
                    if (cursor.getCount() > 0) {
                        contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                        nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                    }
                    while (cursor.moveToNext()
                            || mExampleContactMap.size() < GUIConfig.MAX_EXAMPLE_CONTACT_NUM) {
                        String contactId = cursor.getString(contactIdIndex);
                        String name = cursor.getString(nameIndex);
                        // Logger.d(TAG, "!--->contactId = " + contactId + "; name = " + name);
                        int cid = 0;
                        try {
                            cid = Integer.parseInt(contactId);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // if name if is phone number, do not save it
                        if (!StringUtil.isMobileNumber(name)) {
                            mExampleContactMap.put(cid, name);
                        } else {
                            // Logger.d(TAG, "name = " + name + " is Mobile Number");
                        }
                    }
                    if (null != listener) {
                        listener.onQueryCompleted(mExampleContactMap);
                        Logger.d(TAG, "getContactMapAsync--------onQueryCompleted");
                    }
                } catch (Exception e) {
                    if (null != listener) {
                        listener.onQueryError();
                        Logger.w(TAG, "getContactMapAsync---Exception--onQueryError");
                    }
                    e.printStackTrace();
                } finally {
                    if (null != cursor) {
                        cursor.close();
                    }
                }
            }
        };
        queryThread.start();
    }

    /**
     * get ContactMap asynchronously On Cop file
     * 
     * @author xiaodong.he
     * @date 2015-12-7
     * @param listener
     */
    public static void getContactMapAsyncOnCopfile(final ContactAsyncListener listener) {
        Thread queryThread = new Thread() {
            @Override
            public void run() {
                Logger.d(TAG, "getContactMapOnCopfile--------Begin");
                if (mExampleContactMap != null && mExampleContactMap.size() > 0) {
                    mExampleContactMap.clear();
                }
                File mContactSavedFile =
                        new File(Environment.getExternalStorageDirectory(),
                                Constant.CopFileConstant.contactCOPName);
                try {
                    FileInputStream fi = new FileInputStream(mContactSavedFile);
                    BufferedReader br = new BufferedReader(new InputStreamReader(fi));
                    String line;
                    while ((line = br.readLine()) != null
                            && mExampleContactMap.size() < GUIConfig.MAX_EXAMPLE_CONTACT_NUM) {
                        JSONObject jObj = JsonTool.parseToJSONObject(line);

                        String savedType =
                                JsonTool.getJsonValue(jObj,
                                        Constant.CopFileConstant.JSON_TYPE_OF_CONTENT);
                        // Logger.i(TAG, "getContactMapAsyncOnCopfile--file--" + jObj.toString()
                        // + "; savedType = " + savedType);
                        if (savedType == null) {
                            return;
                        }
                        if (Constant.CopFileConstant.JSON_TYPE_OF_CONTENT_CONATCT.equals(savedType)) {
                            long contactId = Long.valueOf(JsonTool.getJsonValue(jObj, "contactId"));
                            String displayName = JsonTool.getJsonValue(jObj, "displayName");
                            // int id = Integer.valueOf(JsonTool.getJsonValue(jObj, "id"));
                            // int contactType = Integer.valueOf(JsonTool.getJsonValue(jObj,
                            // "contactType"));
                            // int photoId = Integer.valueOf(JsonTool.getJsonValue(jObj,
                            // "photoId"));
                            // String quanpin = JsonTool.getJsonValue(jObj, "quanpin");
                            int hasPhoneNumber =
                                    Integer.valueOf(JsonTool.getJsonValue(jObj, "hasPhoneNumber"));
                            Logger.d(TAG, "getContactMapOnCopfile--contactId = " + contactId
                                    + "; displayName = " + displayName + "; hasPhoneNumber = "
                                    + hasPhoneNumber);
                            if (hasPhoneNumber == 1) {
                                mExampleContactMap.put((int) contactId, displayName);
                            }
                        }
                    }
                    if (null != listener) {
                        listener.onQueryCompleted(mExampleContactMap);
                        Logger.d(TAG, "getContactMapAsyncOnCopfile---onQueryCompleted--dataSize = "
                                + mExampleContactMap.size());
                    }
                } catch (Exception e) {
                    if (null != listener) {
                        listener.onQueryError();
                        Logger.w(TAG, "getContactMapAsyncOnCopfile---Exception--onQueryError");
                    }
                    e.printStackTrace();
                }
            }
        };
        queryThread.start();
    }

    public static Cursor query(Context context, ContentResolver resolver, Uri uri,
            String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        try {
            return resolver.query(uri, projection, selection, selectionArgs, sortOrder);
        } catch (SQLiteException e) {
            Logger.e(TAG, "Catch a SQLiteException when query: ", e);
            return null;
        }
    }

    /**
     * get Contact Name By CONTACT_ID
     * 
     * @param ctx
     * @param contactId
     * @return
     */
    private String getContactNameById(Context ctx, String contactId) {
        Logger.d(TAG, "!--->getContactNameById ---------");
        String name = "";
        String[] projection =
                {ContactsContract.PhoneLookup.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID};
        Cursor cursor = null;
        try {
            cursor =
                    ctx.getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            projection, // Which columns to return.
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = '" + contactId
                                    + "'", // WHERE clause.
                            null, // WHERE clause value substitution
                            null); // Sort order.

            if (cursor == null) {
                Logger.d(TAG, "getPeople null");
                return name;
            }
            Logger.d(TAG, "getPeople cursor.getCount() = " + cursor.getCount());
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                // 取得联系人名字
                int nameFieldColumnIndex =
                        cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
                name = cursor.getString(nameFieldColumnIndex);
                Logger.d(TAG, "!--->name = " + name + ";nameFieldColumnIndex = "
                        + nameFieldColumnIndex);// FC ?
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }
        return name;
    }

    /**
     * get Contact Name By NUMBER
     * 
     * @param ctx
     * @param number
     * @return
     */
    public static String queryContactNameByNumber(Context ctx, String number) {
        Logger.d(TAG, "!--->getContactNameById ---------");
        String name = "";
        String[] projection =
                {ContactsContract.PhoneLookup.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID};
        Cursor cursor = null;
        try {
            cursor =
                    ctx.getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, // Which
                                                                                            // columns
                                                                                            // to
                                                                                            // return.
                            ContactsContract.CommonDataKinds.Phone.NUMBER + " = '" + number + "'", // WHERE
                                                                                                   // clause.
                            null, // WHERE clause value substitution
                            null); // Sort order.

            if (cursor == null) {
                Logger.d(TAG, "getPeople null");
                return name;
            }
            Logger.d(TAG, "getPeople cursor.getCount() = " + cursor.getCount());
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                // 取得联系人名字
                int nameFieldColumnIndex =
                        cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
                name = cursor.getString(nameFieldColumnIndex);
                Logger.d(TAG, "!--->name = " + name + ";nameFieldColumnIndex = "
                        + nameFieldColumnIndex);// FC ?
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }
        return name;
    }

    /**
     * 
     * @param ctx
     */
    public static void testReadAllContacts(Context ctx) {
        Logger.d(TAG, "!-->testReadAllContacts()------");
        StringBuffer buffer = new StringBuffer();

        Cursor cursor =
                ctx.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null,
                        null, null);
        int contactIdIndex = 0;
        int nameIndex = 0;

        if (cursor.getCount() > 0) {
            contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        }
        while (cursor.moveToNext()) {
            String contactId = cursor.getString(contactIdIndex);
            String name = cursor.getString(nameIndex);

            // Logger.i(TAG, "contactId = "+contactId+"; name = "+name);
            /*
             * 查找该联系人的phone信息
             */
            Cursor phones =
                    ctx.getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,
                            null, null);
            int phoneIndex = 0;
            if (phones.getCount() > 0) {
                phoneIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            }
            while (phones.moveToNext()) {
                String phoneNumber = phones.getString(phoneIndex).replaceAll("-", "");
                String content = contactId + ";" + name + ";" + phoneNumber;

                buffer.append(content + "\n");
                Logger.i(TAG, "!--->contactId = " + contactId + "; name = " + name
                        + "; phoneNumber = " + phoneNumber);

            }

            /*
             * 查找该联系人的email信息
             */
            // Cursor emails =
            // ctx.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            // null,
            // ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=" + contactId,
            // null, null);
            // int emailIndex = 0;
            // if(emails.getCount() > 0) {
            // emailIndex = emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
            // }
            // while(emails.moveToNext()) {
            // String email = emails.getString(emailIndex);
            // Logger.i(TAG, email);
            // }

        }

        boolean result = FileOperation.writeContacts(buffer.toString());
        Logger.d(TAG, "!--->result = " + result);
    }

    /**
     * ContactAsyncListener
     * 
     * @author xiaodong.he
     * @date 2015-12-07
     */
    public interface ContactAsyncListener {

        public void onQueryCompleted(ConcurrentHashMap<Integer, String> mExampleContactMap);

        public void onQueryError();

    }
}
