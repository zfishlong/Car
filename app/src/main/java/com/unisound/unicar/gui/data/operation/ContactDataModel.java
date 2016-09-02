/**
 * Copyright (c) 2012-2012 YunZhiSheng(Shanghai) Co.Ltd. All right reserved.
 * 
 * @FileName : ContactDataModel.java
 * @ProjectName : iShuoShuo_v1.3.1
 * @PakageName : com.unisound.unicar.gui.data.operation
 * @Author : Dancindream
 * @CreateDate : 2012-11-6
 */
package com.unisound.unicar.gui.data.operation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Process;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import cn.yunzhisheng.common.PinyinConverter;

import com.unisound.unicar.gui.data.interfaces.IBaseListener;
import com.unisound.unicar.gui.model.ContactInfo;
import com.unisound.unicar.gui.model.PhoneNumberInfo;
import com.unisound.unicar.gui.preference.PrivatePreference;
import com.unisound.unicar.gui.preference.SessionPreference;
import com.unisound.unicar.gui.utils.ContactsUtil;
import com.unisound.unicar.gui.utils.JsonTool;
import com.unisound.unicar.gui.utils.Logger;
import com.unisound.unicar.gui.utils.NumberFormat;
import com.unisound.unicar.namesplit.ImeNameExtender;

/**
 * @Module : database
 * @Comments : 描述
 * @Author : Dancindream
 * @CreateDate : 2012-11-6
 * @ModifiedBy : Dancindream
 * @ModifiedDate: 2012-11-6
 * @Modified: 2012-11-6: 实现基本功能
 */
public class ContactDataModel {
    private static final int mDataInit = 0;
    private static final int mDataUpdate = 1;
    private int mDataType = mDataInit;

    public ContactDataModel(Context context) {
        mContext = context;
        // 由于在服务创建时，就已经初始化，故此处的INIT屏蔽
        // try {
        // PinyinConverter.init(context.getAssets().open("un2py.mg"));
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
    }

    public static final String TAG = "ContactDataModel";
    // Contacts projection
    private static final String[] PROJECTION_CONTACTS = new String[] {
            ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.PHOTO_ID, ContactsContract.Contacts.HAS_PHONE_NUMBER};

    private static final String CONTACT_SELECT_ALL = Contacts.DISPLAY_NAME + " NOTNULL AND "
            + Contacts.DISPLAY_NAME + " != '' ";

    private static final int CONTACT_ID_INDEX = 0;
    private static final int CONTACT_DISPLAY_NAME_INDEX = 1;
    private static final int CONTACT_PHOTO_ID_INDEX = 2;
    private static final int CONTACT_HAS_PHONE_NUMBER_INDEX = 3;

    private final static String[] PROJECTION_PHONE = new String[] {Phone.CONTACT_ID, Phone.NUMBER,
            Phone.TYPE, Phone.LABEL, Phone.IS_PRIMARY, Phone.IS_SUPER_PRIMARY};
    private static final int PHONE_CONTACT_ID_INDEX = 0;
    private static final int PHONE_NUMBER_INDEX = 1;
    private static final int PHONE_TYPE_INDEX = 2;
    private static final int PHONE_LABEL_INDEX = 3;
    private static final int PHONE_IS_PRIMARY_INDEX = 4;
    private static final int PHONE_IS_SUPER_PRIMARY_INDEX = 5;

    public static final String JSON_TYPE_OF_CONTENT = "jsonType";
    public static final String JSON_TYPE_OF_CONTENT_PHONE = "PHONE";
    public static final String JSON_TYPE_OF_CONTENT_CONATCT = "CONTACT";
    public static final String JSON_TYPE_OF_CONTENT_ID = "ID";

    public static final String JSON_TYPE_OF_CONTENT_DATA = "data";

    private Thread mWorkThread = null;
    private ArrayList<ContactInfo> mContacts = new ArrayList<ContactInfo>();
    private ArrayList<PhoneNumberInfo> phoneList = new ArrayList<PhoneNumberInfo>();
    private ConcurrentHashMap<Long, Integer> mHashContactIDIndex =
            new ConcurrentHashMap<Long, Integer>();
    private ConcurrentHashMap<Long, ArrayList<PhoneNumberInfo>> mPhones =
            new ConcurrentHashMap<Long, ArrayList<PhoneNumberInfo>>();

    // private PinyinMatcher matcher = null;
    private Uri mContactUri, mPhoneUri;

    private File mContactSavedFile;

    private final String contactCOPName = "datacontatct.cop";

    private IBaseListener contactModelListener;

    private volatile boolean mSysContactLoaded = false;

    public void initComonent(Context context) {
        mContext = context;
    }

    public void init() {
        mContactSavedFile = new File(Environment.getExternalStorageDirectory(), contactCOPName);
        mDataType = mDataInit;
        sycContacts();
    }

    public void registerObserver() {
        mContactUri = ContactsContract.Contacts.CONTENT_URI;
        mPhoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        startSystemOnChangeThread();
        registerContentObserver(mContactUri);
        registerContentObserver(mPhoneUri);
    }

    public void update() {
        long time = System.currentTimeMillis();
        Logger.d(TAG, "contact update startTime:" + time);
        mDataType = mDataUpdate;
        mSysContactLoaded = false;
        sycContacts();
        Logger.d(TAG, "contact update endTime:" + (System.currentTimeMillis() - time));
    }

    public void saveContactToFile(ArrayList<ContactInfo> l, ArrayList<PhoneNumberInfo> phones) {
        FileOutputStream fos = null;
        try {
            Logger.i(TAG, "--saveContactToFile--" + mContactSavedFile.getPath().toString() + " "
                    + Environment.getExternalStorageDirectory());
            Logger.i(TAG, "--saveContactToFile--" + l.size() + " " + phones.size());
            if (mContactSavedFile.exists()) {
                mContactSavedFile.delete();
            }

            mContactSavedFile.createNewFile();

            fos = new FileOutputStream(mContactSavedFile);
            for (int i = 0; i < l.size(); i++) {
                ContactInfo info = l.get(i);

                int id = info.getId();
                long contactId = info.getContactId();
                int contactType = info.getContactType();
                String displayName = info.getDisplayName();
                int photoId = info.getPhotoId();
                String quanpin = info.getQuanpin();
                int hasPhoneNumber = info.hasPhoneNumber();
                ArrayList<String> nickNameList = info.getNickName();

                JSONObject jObject = new JSONObject();
                JsonTool.putJSONObjectData(jObject, "jsonType", "CONTACT");
                JsonTool.putJSONObjectData(jObject, "contactId", String.valueOf(contactId));
                JsonTool.putJSONObjectData(jObject, "id", String.valueOf(id));
                JsonTool.putJSONObjectData(jObject, "contactType", String.valueOf(contactType));
                JsonTool.putJSONObjectData(jObject, "displayName", displayName);
                JsonTool.putJSONObjectData(jObject, "photoId", String.valueOf(photoId));
                JsonTool.putJSONObjectData(jObject, "quanpin", quanpin);
                JsonTool.putJSONObjectData(jObject, "hasPhoneNumber",
                        String.valueOf(hasPhoneNumber));

                JSONArray nickNameArray = new JSONArray();
                for (int k = 0; nickNameList != null && k < nickNameList.size(); k++) {
                    nickNameArray.put(k, nickNameList.get(k));
                }
                JsonTool.putJSONObjectData(jObject, "nickNameList", nickNameArray);

                Logger.i(TAG, "-contact-" + jObject.toString());

                fos.write((jObject.toString() + "\n").getBytes());
            }

            for (int k = 0; phones != null && k < phones.size(); k++) {
                PhoneNumberInfo phone = phones.get(k);
                JSONObject obj = new JSONObject();
                JsonTool.putJSONObjectData(obj, JSON_TYPE_OF_CONTENT, JSON_TYPE_OF_CONTENT_PHONE);
                JsonTool.putJSONObjectData(obj, "contactType",
                        String.valueOf(phone.getContactType()));
                JsonTool.putJSONObjectData(obj, "id", String.valueOf(phone.getId()));
                JsonTool.putJSONObjectData(obj, "contactId", String.valueOf(phone.getContactId()));
                JsonTool.putJSONObjectData(obj, "number", phone.getNumber());
                JsonTool.putJSONObjectData(obj, "rawNumber", phone.getRawNumber());
                JsonTool.putJSONObjectData(obj, "type", String.valueOf(phone.getType()));
                JsonTool.putJSONObjectData(obj, "label", phone.getLabel());
                JsonTool.putJSONObjectData(obj, "primaryValue",
                        String.valueOf(phone.getPrimaryValue()));
                JsonTool.putJSONObjectData(obj, "superPrimaryValue",
                        String.valueOf(phone.getSuperPrimaryValue()));
                JsonTool.putJSONObjectData(obj, "attribution", phone.getAttribution());

                Logger.i(TAG, "-phnoe-" + obj.toString());

                fos.write((obj.toString() + "\n").getBytes());
            }

            // mHashContactIDIndex
            if (mHashContactIDIndex != null && mHashContactIDIndex.size() > 0) {
                JSONObject jObjc = new JSONObject();
                JsonTool.putJSONObjectData(jObjc, JSON_TYPE_OF_CONTENT, JSON_TYPE_OF_CONTENT_ID);
                Iterator<Entry<Long, Integer>> i = mHashContactIDIndex.entrySet().iterator();
                JSONArray jArray = new JSONArray();
                while (i.hasNext()) {
                    Entry<Long, Integer> e = (Entry<Long, Integer>) i.next();
                    Long key = (Long) e.getKey();
                    Integer value = (Integer) e.getValue();

                    JSONObject jObject = new JSONObject();
                    JsonTool.putJSONObjectData(jObject, "key", key);
                    JsonTool.putJSONObjectData(jObject, "value", value);

                    jArray.put(jObject);
                }
                JsonTool.putJSONObjectData(jObjc, "data", jArray);

                Logger.i(TAG, "-ContactID-" + jObjc.toString());

                fos.write((jObjc.toString() + "\n").getBytes());
            }
            phoneList.clear();
            // fos.close();
            if (mDataType == mDataInit) {
                contactModelListener.onDataDone(SessionPreference.SAVE_CONTACT_DATA_DONE);
            } else if (mDataType == mDataUpdate) {
                contactModelListener.onDataDone(SessionPreference.SAVE_UPDATE_CONTACT_DATA_DONE);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sycContacts() {
        Logger.d(TAG, "sycContacts");

        Runnable runnable = null;
        runnable = new SyncRunnable();

        mWorkThread = new Thread(runnable);
        mWorkThread.setPriority(Process.THREAD_PRIORITY_BACKGROUND);
        mWorkThread.setName(TAG + "_SyncContact_");
        mWorkThread.start();
    }

    private class SyncRunnable implements Runnable {
        @Override
        public void run() {
            synchronized (ContactDataModel.class) {
                Logger.d(TAG, "SyncRunnable start");
                mContacts.clear();
                mHashContactIDIndex.clear();
                mPhones.clear();

                // 读Phone中联系人
                long start = System.currentTimeMillis();
                Logger.d(TAG, "--start sync contact read");
                if (PrivatePreference.contact_type.equals("SYSTEM")) {
                    sycSystemContact();
                    Logger.d(TAG, "--start sync contact read mid:"
                            + (System.currentTimeMillis() - start));

                    sycSystemPhoneNumber();
                    Logger.d(TAG, "end sync contact read:" + (System.currentTimeMillis() - start)
                            + " ms");
                }
                Logger.d(TAG, "SyncRunnable mContacts =" + mContacts.toString() + "; phoneList = "
                        + phoneList.toString());
                ContactsUtil.updateContactExampleMap(mContacts); // XD added 2015-12-7
                saveContactToFile(mContacts, phoneList);
                Logger.d(TAG, "SyncRunnable end");
            }
        }
    }

    private void sycSystemContact() {
        Logger.d(TAG, "sycSystemContact");
        String select = CONTACT_SELECT_ALL;

        Cursor contactCursor =
                query(mContext, mContext.getContentResolver(),
                        ContactsContract.Contacts.CONTENT_URI, PROJECTION_CONTACTS, select, null,
                        null);

        if (contactCursor != null) {
            if (contactCursor.getCount() > 0) {
                ImeNameExtender imeWordsExtender = new ImeNameExtender();
                while (contactCursor.moveToNext()) {
                    ContactInfo i = new ContactInfo();
                    long contactId = contactCursor.getLong(CONTACT_ID_INDEX);
                    i.setContactId(contactId);
                    i.setContactType(ContactInfo.CONTACT_TYPE_PHONE);
                    String displayName = contactCursor.getString(CONTACT_DISPLAY_NAME_INDEX);
                    int hasPhoneNumber = contactCursor.getInt(CONTACT_HAS_PHONE_NUMBER_INDEX);
                    i.setDisplayName(displayName);
                    /* 2014.02.10 added by sc for add split name */
                    ArrayList<String> extendedNameList =
                            imeWordsExtender.extendImeNames(new String[] {displayName});
                    i.setNickNameList(extendedNameList);
                    /**/
                    i.setPhotoId(contactCursor.getInt(CONTACT_PHOTO_ID_INDEX));
                    i.setHasPhoneNumber(hasPhoneNumber);
                    String[] pinyins = PinyinConverter.getNameSpell1(displayName);
                    if (pinyins != null) {
                        i.setQuanpin(pinyins[0]);
                    }

                    Logger.i(TAG, "contact :" + i.toString() + " ");

                    StringBuilder sb = new StringBuilder();
                    for (int j = 0; extendedNameList != null && j < extendedNameList.size(); j++) {
                        sb.append(extendedNameList.get(j) + " ");
                    }
                    i.setId((int) getContactId(i.getContactId(), i.getContactType()));

                    // TODO
                    if (true) {
                        // TODO
                        mContacts.add(i);
                        int index = mContacts.size() - 1;
                        mHashContactIDIndex.put(getContactId(i.getContactId(), i.getContactType()),
                                index);
                    }
                    // else {
                    // if (i.hasPhoneNumber() == ContactInfo.CONTACT_HAS_NUMBER) {
                    // mContacts.add(i);
                    // int index = mContacts.size() - 1;
                    // mHashContactIDIndex.put(
                    // getContactId(i.getContactId(),
                    // i.getContactType()), index);
                    // } else {
                    // mContacts.add(i);
                    // }
                    // }
                }
            }
            contactCursor.close();
            contactCursor = null;
        }
    }

    private void sycSystemPhoneNumber() {
        Logger.d(TAG, "sycSystemPhoneNumber");
        Cursor phones =
                mContext.getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION_PHONE, null,
                        null, null);

        if (phones != null) {
            if (phones.getCount() > 0) {
                while (phones.moveToNext()) {
                    PhoneNumberInfo numberInfo = new PhoneNumberInfo();
                    numberInfo.setContactId(phones.getInt(PHONE_CONTACT_ID_INDEX));
                    numberInfo.setContactType(ContactInfo.CONTACT_TYPE_PHONE);
                    numberInfo.setLabel(phones.getString(PHONE_LABEL_INDEX));
                    String number = phones.getString(PHONE_NUMBER_INDEX);
                    numberInfo.setNumber(number);
                    numberInfo
                            .setRawNumber(NumberFormat.getCleanPhoneNumber(numberInfo.getNumber()));
                    numberInfo.setType(phones.getInt(PHONE_TYPE_INDEX));
                    numberInfo.setPrimaryValue(phones.getInt(PHONE_IS_PRIMARY_INDEX));
                    numberInfo.setSuperPrimaryValue(phones.getInt(PHONE_IS_SUPER_PRIMARY_INDEX));

                    phoneList.add(numberInfo);

                    Logger.i(TAG, "phone:" + numberInfo.toString());

                    long contactid =
                            getContactId(numberInfo.getContactId(), numberInfo.getContactType());

                    if (mPhones.containsKey(contactid)) {
                        mPhones.get(contactid).add(numberInfo);
                    } else {
                        ArrayList<PhoneNumberInfo> phoneList = new ArrayList<PhoneNumberInfo>();
                        phoneList.add(numberInfo);
                        mPhones.put(contactid, phoneList);
                    }
                }
            }

            phones.close();
            phones = null;
        }
    }

    private long getContactId(long contactId, int contactType) {
        return contactId * 10 + contactType;
    }

    public Cursor query(Context context, ContentResolver resolver, Uri uri, String[] projection,
            String selection, String[] selectionArgs, String sortOrder) {
        try {
            return resolver.query(uri, projection, selection, selectionArgs, sortOrder);
        } catch (SQLiteException e) {
            Logger.e(TAG, "Catch a SQLiteException when query: ", e);
            return null;
        }
    }

    public void setDataModelListener(IBaseListener l) {
        contactModelListener = l;
    }

    protected void onChange(int changeId) {
        Logger.d(TAG, "ContactDataModel onChange changeId : " + changeId);
        update();
    }

    public void release() {
        Logger.d(TAG, "release");
        contactModelListener = null;
        if (mHashContactIDIndex != null) {
            mHashContactIDIndex.clear();
            mHashContactIDIndex = null;
        }
        synchronized (mContacts) {
            if (mContacts != null) {
                mContacts.clear();
                mContacts = null;
            }
        }

        synchronized (mPhones) {
            if (mPhones != null) {
                mPhones.clear();
                mPhones = null;
            }
        }
        mContactUri = null;
        mPhoneUri = null;
        mHandler.removeMessages(0);
        mHandler = null;
        unregisterContentObserver();
        mContext = null;
        mContentObserver = null;
    }

    protected Context mContext = null;
    public static final int FLAG_UPDATE = 0;
    private int mOnChangeCount = 0;
    private static Queue<Integer> mSyncTaskQueue = new LinkedList<Integer>();
    private Thread mSystemOnChangeThread = null;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Logger.d(TAG, "handleMessage mOnChangeCount :  " + mOnChangeCount);
            onChange(mOnChangeCount);
        };
    };

    private ContentObserver mContentObserver = new ContentObserver(mHandler) {

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            mOnChangeCount++;
            Logger.d(TAG, "onChange mOnchangeCount :  " + mOnChangeCount);
            synchronized (mSyncTaskQueue) {
                mSyncTaskQueue.offer(mOnChangeCount);
                Logger.d(TAG, "onChange mSyncTaskQueue size : " + mSyncTaskQueue.size());
                if (mSyncTaskQueue != null) {
                    mSyncTaskQueue.notifyAll();
                }
            }
        }

    };

    protected void registerContentObserver(Uri uri) {
        Logger.d(TAG, "registerContentObserver:uri " + uri);
        ContentResolver resolver = mContext.getContentResolver();
        resolver.registerContentObserver(uri, true, mContentObserver);
    }

    protected void unregisterContentObserver() {
        Logger.d(TAG, "unregisterContentObserver");
        ContentResolver resolver = mContext.getContentResolver();
        resolver.unregisterContentObserver(mContentObserver);
    }



    public static int popSyncTaskQueue() {
        synchronized (mSyncTaskQueue) {
            if (mSyncTaskQueue != null) {
                Iterator<Integer> iterable = mSyncTaskQueue.iterator();
                if (iterable.hasNext()) {
                    int taskQueue = iterable.next();
                    Logger.d(TAG, "taskQueue : " + taskQueue);
                    mSyncTaskQueue.clear();
                    return taskQueue;
                }
            }
        }
        return 0;
    }


    private class SyncSystemRunnable implements Runnable {
        @Override
        public void run() {
            Logger.d(TAG, "SyncSystemRunnable run");
            while (true) {
                synchronized (mSyncTaskQueue) {
                    if (mSyncTaskQueue != null && mSyncTaskQueue.isEmpty()) {
                        Logger.d(TAG, "SyncSystemRunnable mSyncTaskQueue.isEmpty()");
                        try {
                            mSyncTaskQueue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                int onChangeQueue = popSyncTaskQueue();
                Logger.d(TAG, "SyncSystemRunnable onChangeQueue :　" + onChangeQueue);
                if (onChangeQueue != 0) {
                    mHandler.sendEmptyMessageDelayed(FLAG_UPDATE, 500);
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void startSystemOnChangeThread() {
        Logger.d(TAG, "startOnChangeThread");
        if (mSystemOnChangeThread == null || !mSystemOnChangeThread.isAlive()) {
            mSystemOnChangeThread = new Thread(new SyncSystemRunnable());
            mSystemOnChangeThread.setPriority(Thread.MIN_PRIORITY);
            mSystemOnChangeThread.setName(TAG);
            mSystemOnChangeThread.start();
        }
    }
}
