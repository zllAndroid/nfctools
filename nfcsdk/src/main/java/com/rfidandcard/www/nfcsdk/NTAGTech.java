package com.rfidandcard.www.nfcsdk;

import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;

/**
 * Created by xuhua on 16/03/2018.
 */

public class NTAGTech {
    public MifareUltralight mul = null;
    public MifareClassic mcv = null;
    public Ndef ndefv = null;
    public NdefFormatable ndeff = null;
    public NfcA atag = null;
    public NfcB btag = null;
    public void clear()
    {
        mul = null;
        mcv = null;
        ndefv = null;
        ndeff = null;
        atag = null;
        btag = null;
    }
}
