package com.rfidandcard.www.nfcsdk;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;

/**
 * Created by xuhua on 2018/3/16.
 */

public class NFCActivity extends AppCompatActivity {
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    /**
     * 启动Activity，界面可见时
     */
    @Override
    protected void onStart() {
        super.onStart();
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        //一旦截获NFC消息，就会通过PendingIntent调用窗口
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()), 0);
    }
    //当获取到MifareUltralight标签
    private  NTAGTech tech = new NTAGTech();
    public void onMifareUltralight(String id,MifareUltralight tag){
        try {
            tag.connect();
            byte[] ver = tag.transceive(new byte[]{NFC.GET_VERSION});
            if(ver == null){
                Log.d(NFCActivity.class.getName(),"GET_VERSION error");
                return;
            }
            if(ver.length == 8){
                NTAG21X ntag = new NTAG21X(id,ver,tech);
                onNTAG21X(ntag);
            }
            tag.close();
        } catch (Exception e) {
            Log.d(NFCActivity.class.getName(),"GET_VERSION exception:" + e);
        }
    }

    //当读取到213标签时
    protected void onNTAG21X(NTAG21X tag){

    }

    //当获取到MifareClassic标签
    protected void onMifareClassic(String id,MifareClassic tag){

    }

    //当获取到ndef标签
    protected void onNdef(String id,Ndef tag){

    }

    //当获取到NfcA标签
    protected void onNfcA(String id,NfcA tag){

    }

    //当获取到NfcB标签
    protected void onNfcB(String id,NfcB tag){

    }

    //当获取到NfcB标签
    protected void onNdefFormatable(String id,NdefFormatable tag){

    }

    @Override
    protected void onNewIntent(Intent intent) {
        String action = intent.getAction().trim();
        if(!action.equals("android.nfc.action.TAG_DISCOVERED") && !action.equals("android.nfc.action.NDEF_DISCOVERED")){
            return;
        }
        tech.clear();
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String id = Util.bytesToHex(tag.getId());
        String[] techList = tag.getTechList();
        for(String v : techList){
            if(v == NfcA.class.getName()){
                tech.atag = NfcA.get(tag);
                if(tech.atag != null){
                    onNfcA(id, tech.atag);
                }
            }else if(v == NfcB.class.getName()){
                tech.btag = NfcB.get(tag);
                if(tech.btag != null){
                    onNfcB(id,tech.btag);
                }
            }else if(v == MifareUltralight.class.getName()){
                tech.mul = MifareUltralight.get(tag);
                if(tech.mul != null) {
                    onMifareUltralight(id, tech.mul);
                }
            }else if(v == MifareClassic.class.getName()){
                tech.mcv = MifareClassic.get(tag);
                if(tech.mcv != null) {
                    onMifareClassic(id, tech.mcv);
                }
            }else if(v == Ndef.class.getName()) {
                tech.ndefv = Ndef.get(tag);
                if(tech.ndefv != null){
                    onNdef(id,tech.ndefv);
                }
            }else if(v == NdefFormatable.class.getName()){
                tech.ndeff = NdefFormatable.get(tag);
                if(tech.ndeff != null){
                    onNdefFormatable(id,tech.ndeff);
                }
            }

        }
    }
    /**
     * 获得焦点，按钮可以点击
     */
    @Override
    public void onResume() {
        super.onResume();

        if (mNfcAdapter != null) {
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
        }
    }
    /**
     * 暂停Activity，界面获取焦点，按钮可以点击
     */
    @Override
    public void onPause() {
        super.onPause();
        //恢复默认状态
        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch(this);
        }
    }
}