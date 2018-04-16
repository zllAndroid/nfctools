package com.rfidandcard.www.nfcsdk;


/**
 * Created by xuhua on 2018/3/15.
 */

public class NFC {
    static {
        System.loadLibrary("nfc");
    }
    public static byte GET_VERSION = (byte)0x60;
    public static byte PWD_AUTH = (byte)0x1B;
    public static byte READ_TT_STATUS = (byte)0xA4;
}
