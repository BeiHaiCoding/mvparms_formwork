package com.cwc.mylibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 打开或关闭软键盘
 *
 * @author zhy
 */
public class KeyBoardHelper {
    /**
     * 打开软键盘
     *
     * @param mEditText 输入框
     * @param mContext  上下文
     */
    public static void openKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 关闭软键盘
     *
     * @param mEditText 输入框
     * @param mContext  上下文
     */
    public static void closeKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }


    /**
     * 非空关闭
     * @param mActivity
     */
    public static void closeKeybord2(Activity mActivity) {
        InputMethodManager imm = (InputMethodManager) mActivity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        if (mActivity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (mActivity.getCurrentFocus() != null)
                imm.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 打开则会关闭，关闭则会打开
     * @param mActivity
     */
    public static  void openOrClose(Activity mActivity){
        InputMethodManager inputMethodManager=(InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

}