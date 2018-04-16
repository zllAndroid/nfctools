package com.rfidandcard.www.nfcsdk;

import android.util.Log;

import java.io.IOException;

/**
 * Created by xuhua on 16/03/2018.
 */

//NTAG213 215 216 213TT
public class NTAG21X extends NTAG {

    //tt状态
    public class TTStatus {
        //tt status open
        public static final byte OPEN = 0x4F;

        //tt status closed
        public static final byte CLOSED = 0x43;

        //tt status incorrect
        public static final byte INCORRECT = 0x49;

        public  byte[] status = null;
        //
       public  TTStatus(byte[] v){
            status = v;
        }
        //获取tt message
        public byte[] message() {
            return new byte[]{status[0],status[1],status[2],status[3]};
        }
        //获取tt status
        public byte status() {
            return status[4];
        }
    }

    private byte fixedHeader;
    private byte vendorID;
    private byte productType;
    private byte productSubType;
    private byte major;
    private byte minor;
    private byte storageSize;
    private byte protocolType;

    public static final String NTAG213 = "NTAG213";
    public static final String NTAG215 = "NTAG215";
    public static final String NTAG216 = "NTAG216";

    private String type = null;

    NTAG21X(String id,byte[] ver,NTAGTech tech) throws NFCException {
        super(id,tech);
        if(ver == null || ver.length != 8){
            throw new NFCException("ver data error");
        }
        fixedHeader = ver[0];
        vendorID = ver[1];
        productType = ver[2];
        productSubType = ver[3];
        major = ver[4];
        minor = ver[5];
        storageSize = ver[6];
        protocolType = ver[7];
        //检测卡片类型
        if(storageSize == 15){
            type = NTAG213;
        }else {
            throw new NFCException("NTAG21X "+storageSize + " not imp");
        }
    }
    //认证卡片,密码大于4个多余的被忽略，不够的用0x00填充
    public byte[] auth(byte[] pass) throws IOException {
        byte[] cmd = new byte[]{NFC.PWD_AUTH,(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff};
        System.arraycopy(pass,0,cmd,1,pass.length < 4?pass.length:4);
        return post(cmd);
    }
    //设置卡密码
    public void setPassword(byte[] pass) throws IOException {
        int addr = 0;
        if(this.type == NTAG213){
            addr = 0x2B;
        }
        write(addr,pass);
    }
    //获取tt status 状态
    public TTStatus readTTStatus() throws IOException {
        if(!isHasTT()){
            throw new IOException("NTAG21X TT can invoke");
        }
        byte[] cmd = new byte[]{NFC.READ_TT_STATUS,0x00};

        return new TTStatus(post(cmd));
    }
    //
    public String getType() {
        return this.type;
    }
    //
    @Override
    public String toString() {
        if(!isHasTT()){
            return this.type;
        }
        return this.type + "TT";
    }
    //是否包含TT功能
    public boolean isHasTT() {
        return major == 3;
    }
    //
    public void debug() {
        Log.d("NTAG21X","--------------"+toString()+"---------------");
        Log.d("NTAG21X","FixedHeader="+fixedHeader);
        Log.d("NTAG21X","VendorID="+vendorID);
        Log.d("NTAG21X","ProductType="+productType);
        Log.d("NTAG21X","ProductSubType="+productSubType);
        Log.d("NTAG21X","Major="+major);
        Log.d("NTAG21X","Minor="+minor);
        Log.d("NTAG21X","StorageSize="+storageSize);
        Log.d("NTAG21X","ProtocolType="+protocolType);
        Log.d("NTAG21X","-----------------------------------");
    }
}
