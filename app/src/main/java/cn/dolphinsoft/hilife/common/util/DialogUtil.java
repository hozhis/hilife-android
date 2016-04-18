package cn.dolphinsoft.hilife.common.util;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.content.Context;

/**
 * Created by hozhis on 2016/4/17.
 */
public class DialogUtil {

    private AlertDialog.Builder builder;

    public DialogUtil(Context context){
        builder = new AlertDialog.Builder(context);
    }

    public void setTitle(String title){
        builder.setTitle(title);
    }

    public void setMessage(String message){
        builder.setMessage(message);
    }

    public void setNegativeButton(String negativeMsg,final DialogInterface.OnClickListener listener){
        builder.setNegativeButton(negativeMsg, listener);
    }

    public void setPositiveButton(String positiveMsg,final DialogInterface.OnClickListener listener){
        builder.setPositiveButton(positiveMsg,listener);
    }

    public void show(){
        builder.create().show();
    }
}
