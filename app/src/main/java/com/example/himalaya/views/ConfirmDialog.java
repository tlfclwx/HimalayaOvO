package com.example.himalaya.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.himalaya.R;

public class ConfirmDialog extends Dialog {

    private TextView mCancelSub;
    private TextView mGiveUp;
    private onDialogActionClickListener mClickListener;

    public ConfirmDialog(@NonNull Context context) {
        this(context, 0);
    }

    public ConfirmDialog(@NonNull Context context, int themeResId) {
        this(context, true, null);
    }

    protected ConfirmDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm);
        initView();
        initListener();
    }

    private void initListener() {
        mCancelSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onCancelSubClick();
                //让这个Dialog消失
                dismiss();
            }
        });
        mGiveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mClickListener.GiveUpClick();
               //让这个Dialog消失
               dismiss();
            }
        });
    }

    private void initView() {
        mCancelSub = this.findViewById(R.id.dialog_check_box_cancel);
        mGiveUp = this.findViewById(R.id.dialog_check_box_confirm);
    }
    public void setonDialogActionClickListener(onDialogActionClickListener listener) {
        this.mClickListener = listener;
    }
    public interface onDialogActionClickListener{
        void onCancelSubClick();

        void GiveUpClick();
    }
}
