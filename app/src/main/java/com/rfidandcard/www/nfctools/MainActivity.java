package com.rfidandcard.www.nfctools;

import android.app.ProgressDialog;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.rfidandcard.www.nfcsdk.NFCActivity;
import com.rfidandcard.www.nfcsdk.NTAG21X;

import java.io.IOException;

public class MainActivity extends NFCActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        oldpassEdit = (EditText)findViewById(R.id.editoldpass);
        newpassEdit = (EditText)findViewById(R.id.editnewpass);
        urlEdit = (EditText)findViewById(R.id.editurl);
        textEdit = (EditText)findViewById(R.id.edittxt);
        uuidEdit = (EditText)findViewById(R.id.edituuid);
    }

    private EditText oldpassEdit;
    private EditText newpassEdit;
    private EditText urlEdit;
    private EditText textEdit;
    private EditText uuidEdit;
    private int action = 0;

    private   ProgressDialog waitingDialog = null;

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//
//    }
    //当读取到213标签时
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onNTAG21X(NTAG21X tag) {
        super.onNTAG21X(tag);
//        uuidEdit.setText(tag.getUUID());
        try {
            NTAG21X.TTStatus ttStatus = tag.readTTStatus();
            byte status = ttStatus.status();
            byte[] message = ttStatus.message();

            Log.e("TTStatus",status+"**"+message[0]+"**"+message[1]+"**"+message[2]+"**"+message[3]);
//            String srt2=new String(ttStatus,"UTF-8");
//            String isoString = new String(a4h,"ISO-8859-1");
//            Log.e("a4h",""+a4h[0]+"-"+a4h[1]+"-"+a4h[2]+"-"+a4h[3]+"**"+tag.getType());
            uuidEdit.setText(status+"**"+message[0]+"**"+message[1]+"**"+message[2]+"**"+message[3]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] a4h =null;
//        try {
//             a4h = tag.read(0x00);
//            String srt2=new String(a4h,"UTF-8");
//            String isoString = new String(a4h,"ISO-8859-1");
//            Log.e("a4h",""+a4h[0]+"-"+a4h[1]+"-"+a4h[2]+"-"+a4h[3]+"**"+tag.getType());
//            uuidEdit.setText(srt2+"-----"+isoString);
//        } catch (IOException e) {
//            Toast.makeText(getApplicationContext(), "失败", Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }
        String oldpass = oldpassEdit.getText().toString();
//        String newpass = "H7nz0JdOi29l9e6TQq7uaXfp4b2q4E3ms8GiOYdeh/nT1oEfOJ6q1FWjnnY6zW367YptUCkHvStm6gdhrYaSjwMlSFsiMhYOol8ksbqT+gB8+2YRsbKj6C87cHk2aGS7rY4mrA08yU3mgjUGfcutaLIFsoYXfyXCKgB7s312jZYzwTci4NpWrbR1CgiJG44sSlTgMLHDu3hRFnaPM8aPS8nfwk8ShSMkCpg3ytUsqvVLj7eN8Uh4DK0azssQCB3mVTgl+cdsI07vXfgEQQ/lNPPja8U467UU9jQjmylM9ZU57uD6449shXDcRombVXUKAP7TJBNY/lat52KbdfPzcQ==";
        String newpass = newpassEdit.getText().toString();

        byte[] newpassb = newpass.getBytes();
        byte[] oldpassb = oldpass.getBytes();
        if(oldpass.length() == 0){
            oldpassb = new byte[]{(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff};
        }
        try {
            tag.auth(oldpassb);
        }catch (Exception e){
            action = 0;
            Toast.makeText(getApplicationContext(), "密码认证失败", Toast.LENGTH_SHORT).show();
            if(waitingDialog != null){
                waitingDialog.hide();
                waitingDialog = null;
            }
            return;
        }
        try {
           if(action == 1){
               tag.setPassword(newpassb);
                byte[] b29 = tag.read(0x29);
                b29[3] = 0;
                tag.write(0x29,new byte[]{b29[0],b29[1],b29[2],b29[3]});
                action = 0;
                Toast.makeText(getApplicationContext(),"密码设置成功",Toast.LENGTH_SHORT).show();
            }else if(action == 2){
                byte[] b29 = tag.read(0x29);
                b29[3] = (byte)0xff;
                tag.write(0x29,new byte[]{b29[0],b29[1],b29[2],b29[3]});
                tag.setPassword(new byte[]{(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff});
                action = 0;
                Toast.makeText(getApplicationContext(),"密码已经取消",Toast.LENGTH_SHORT).show();
            }else if(action == 3){
                action = 0;
                Toast.makeText(getApplicationContext(),"密码校验成功",Toast.LENGTH_SHORT).show();
            }else if(action == 4){
               String urltxt = urlEdit.getText().toString();
               String texttxt = textEdit.getText().toString();
               NdefMessage message = null;
               if(urltxt.length() > 4){
                   NdefRecord record = NdefRecord.createUri(urltxt);
                   message = new NdefMessage(record);
               }else if(texttxt.length() > 0){
                   NdefRecord record = NdefRecord.createTextRecord("en",texttxt);
                   message = new NdefMessage(record);
               }
               if(message != null){
                    tag.writeNdef(message);
               }
               action = 0;
               Toast.makeText(getApplicationContext(),"数据写入成功",Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            action = 0;
            Toast.makeText(getApplicationContext(),"操作失败1:"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        if(waitingDialog != null){
            waitingDialog.hide();
            waitingDialog = null;
        }
    }

    public void setPassword(View v){
        String txt = newpassEdit.getText().toString();
//        if(txt.length()==0){
//            newpassEdit.requestFocus();
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.showSoftInput(newpassEdit,InputMethodManager.SHOW_FORCED);
//            Toast.makeText(getApplicationContext(),"新密码长度大于0",Toast.LENGTH_SHORT).show();
//            return;
//        }
        waitingDialog = new ProgressDialog(MainActivity.this);
        waitingDialog.setTitle("请开启手机NFC功能，并触碰标签");
        waitingDialog.setIndeterminate(true);
        waitingDialog.setCancelable(false);
        waitingDialog.show();
        action = 1;
    }

    public void delPassword(View v){
        String txt = oldpassEdit.getText().toString();
        if(txt.length() ==0){
            Toast.makeText(getApplicationContext(),"原密码长度必须为4",Toast.LENGTH_SHORT).show();
            return;
        }
        waitingDialog = new ProgressDialog(MainActivity.this);
        waitingDialog.setTitle("请开启手机NFC功能，并触碰标签");
        waitingDialog.setIndeterminate(true);
        waitingDialog.setCancelable(false);
        waitingDialog.show();
        action = 2;
    }

    public void checkPassword(View v) {
        String txt = oldpassEdit.getText().toString();
        if(txt.length() !=4){
            Toast.makeText(getApplicationContext(),"原密码长度必须为4",Toast.LENGTH_SHORT).show();
            return;
        }
        waitingDialog = new ProgressDialog(MainActivity.this);
        waitingDialog.setTitle("请开启手机NFC功能，并触碰标签");
        waitingDialog.setIndeterminate(true);
        waitingDialog.setCancelable(false);
        waitingDialog.show();
        action = 3;
    }

    public void writeContent(View v){
        String url = urlEdit.getText().toString();
        String text = textEdit.getText().toString();
        if(url.length()  == 0 && text.length() == 0){
            Toast.makeText(getApplicationContext(),"URL或者文本为不必须填写",Toast.LENGTH_SHORT).show();
            return;
        }
        waitingDialog = new ProgressDialog(MainActivity.this);
        waitingDialog.setTitle("请开启手机NFC功能，并触碰标签");
        waitingDialog.setIndeterminate(true);
        waitingDialog.setCancelable(false);
        waitingDialog.show();
        action = 4;
    }
}
