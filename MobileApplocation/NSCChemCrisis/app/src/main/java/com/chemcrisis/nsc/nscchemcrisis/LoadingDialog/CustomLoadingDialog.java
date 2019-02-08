package com.chemcrisis.nsc.nscchemcrisis.LoadingDialog;

import android.app.AlertDialog;
import android.content.Context;

import com.chemcrisis.nsc.nscchemcrisis.R;

import dmax.dialog.SpotsDialog;

public class CustomLoadingDialog {

    private AlertDialog loadingDialog;
    public CustomLoadingDialog(Context context){
        loadingDialog = new SpotsDialog.Builder().setContext(context).setTheme(R.style.CustomLoadingDialog).build();
    }
    public void showDialog(){
        loadingDialog.show();
    }
    public void dismissDialog(){
        loadingDialog.dismiss();
    }

}
