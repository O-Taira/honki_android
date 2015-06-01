package com.yokmama.learn10.chapter07.lesson34.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.yokmama.learn10.chapter07.lesson34.R;

/**
 * Created by kayo on 15/04/17.
 */
public class TranslateAnimationDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder bld = new AlertDialog.Builder(
                getActivity());
        bld.setTitle("タイトル");
        bld.setMessage("メッセージ");
        bld.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismissAllowingStateLoss();
            }
        });
        return bld.create();
    }
}