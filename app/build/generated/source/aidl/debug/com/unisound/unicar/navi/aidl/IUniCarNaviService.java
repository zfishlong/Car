/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\Users\\xiaoJie\\AndroidStudioProjects\\Car\\app\\src\\main\\aidl\\com\\unisound\\unicar\\navi\\aidl\\IUniCarNaviService.aidl
 */
package com.unisound.unicar.navi.aidl;
public interface IUniCarNaviService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.unisound.unicar.navi.aidl.IUniCarNaviService
{
private static final java.lang.String DESCRIPTOR = "com.unisound.unicar.navi.aidl.IUniCarNaviService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.unisound.unicar.navi.aidl.IUniCarNaviService interface,
 * generating a proxy if needed.
 */
public static com.unisound.unicar.navi.aidl.IUniCarNaviService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.unisound.unicar.navi.aidl.IUniCarNaviService))) {
return ((com.unisound.unicar.navi.aidl.IUniCarNaviService)iin);
}
return new com.unisound.unicar.navi.aidl.IUniCarNaviService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_startNavi:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.startNavi(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onControlCommand:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onControlCommand(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_registerCallback:
{
data.enforceInterface(DESCRIPTOR);
com.unisound.unicar.navi.aidl.IUniCarNaviCallback _arg0;
_arg0 = com.unisound.unicar.navi.aidl.IUniCarNaviCallback.Stub.asInterface(data.readStrongBinder());
this.registerCallback(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getCallback:
{
data.enforceInterface(DESCRIPTOR);
com.unisound.unicar.navi.aidl.IUniCarNaviCallback _result = this.getCallback();
reply.writeNoException();
reply.writeStrongBinder((((_result!=null))?(_result.asBinder()):(null)));
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.unisound.unicar.navi.aidl.IUniCarNaviService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void startNavi(java.lang.String json) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(json);
mRemote.transact(Stub.TRANSACTION_startNavi, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onControlCommand(java.lang.String json) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(json);
mRemote.transact(Stub.TRANSACTION_onControlCommand, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void registerCallback(com.unisound.unicar.navi.aidl.IUniCarNaviCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_registerCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public com.unisound.unicar.navi.aidl.IUniCarNaviCallback getCallback() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
com.unisound.unicar.navi.aidl.IUniCarNaviCallback _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCallback, _data, _reply, 0);
_reply.readException();
_result = com.unisound.unicar.navi.aidl.IUniCarNaviCallback.Stub.asInterface(_reply.readStrongBinder());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_startNavi = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onControlCommand = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_registerCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_getCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
}
public void startNavi(java.lang.String json) throws android.os.RemoteException;
public void onControlCommand(java.lang.String json) throws android.os.RemoteException;
public void registerCallback(com.unisound.unicar.navi.aidl.IUniCarNaviCallback callback) throws android.os.RemoteException;
public com.unisound.unicar.navi.aidl.IUniCarNaviCallback getCallback() throws android.os.RemoteException;
}
