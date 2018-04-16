package com.rfidandcard.www.nfcsdk;

/**
 * Created by xuhua on 16/03/2018.
 */

public class Util {
    static {
        System.loadLibrary("nfc");
    }
    public static native String bytesToHex(byte[] bytes);
}
