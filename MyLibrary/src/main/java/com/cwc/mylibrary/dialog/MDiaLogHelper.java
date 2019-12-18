package com.cwc.mylibrary.dialog;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cwc.mylibrary.R;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnBackPressListener;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;

/**
 * Created by Administrator on 2017/4/1.
 */

public class MDiaLogHelper {
//    方法	说明
//    newDialog(Context context)	创建dialog
//    setContentHolder(Holder holder)	设置holder,必要
//    setContentWidth(int width)	宽：ViewGroup.LayoutParams.WRAP_CONTENT等
//    setContentHeight(int height)	高
//    setHeader(int resourceId)	头的布局或View
//    setFooter(int resourceId)	尾的布局或View
//    setGravity(int gravity)	dialog的位置
//    setExpanded(boolean expanded)	是否可扩展，默认是false,仅适用于ListView和GridView
//    setCancelable(boolean isCancelable)	点击外部区域是否可以取消dialog
//    setAdapter(BaseAdapter adapter)	ListView或GridView的adapter,ViewHolder不需要
//    setOnItemClickListener(OnItemClickListener listener)	ListView或GridView的item的点击事件
//    setOnClickListener(OnClickListener listener)	点击事件
//    setOnDismissListener(OnDismissListener listener)	dismiss的监听
//    setOnCancelListener(OnCancelListener listener)	取消的监听
//    getHolderView()	获取视图View
//    getHeaderView()	获取头布局
//    getFooterView()	获取尾布局
//    setMargin(left, top, right, bottom)	Add margins to your dialog. They are set to 0 except when gravity is center. In that case basic margins are applied
//    setPadding(left, top, right, bottom)	Set padding to the holder
//    setInAnimation(R.anim.abc_fade_in)	进入动画
//    setOutAnimation(R.anim.abc_fade_out)	移除动画
//    setContentBackgroundResource(resource)	dialog的背景色
//    setOverlayBackgroundResource(resource)	dialog以外的背景色


    //创建一般的的提示对话框，进度对话框，底部图片选择对话框

    /**
     * 创建提示对话框
     *
     * @param act
     * @param title
     * @param content
     * @param left_tv
     * @param right_tv
     * @param isCanOutClose
     * @param onMyTipDiaListener
     * @param back_listener
     * @return
     */
    public static DialogPlus showTipDia(Activity act, String title, String content, String left_tv, String right_tv, boolean isCanOutClose, final OnMyTipDiaListener onMyTipDiaListener, OnBackPressListener back_listener) {
        final DialogPlus dialog = DialogPlus.newDialog(act)
                .setContentHolder(new ViewHolder(R.layout.dia_tip))
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setGravity(Gravity.CENTER)
                .setCancelable(isCanOutClose)
                .setOnBackPressListener(back_listener)
                .create();
        View view = dialog.getHolderView();
        ((TextView) view.findViewById(R.id.tip_title)).setText(title);
        ((TextView) view.findViewById(R.id.tip_content)).setText(content);
        ((TextView) view.findViewById(R.id.tip_left_click)).setText(left_tv);
        ((TextView) view.findViewById(R.id.tip_right_click)).setText(right_tv);
        ((TextView) view.findViewById(R.id.tip_left_click)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMyTipDiaListener.OnLeftClick(dialog, v);
            }
        });
        ((TextView) view.findViewById(R.id.tip_right_click)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMyTipDiaListener.OnRightClick(dialog, v);
            }
        });
        dialog.show();

        return dialog;
    }

    /**
     * 创建拍照相册选择对话框（底部）
     *
     * @param act
     * @param tv_listener
     * @param back_listener
     * @return
     */
    public static DialogPlus showPhotoDia(Activity act, String title_1, String title_2, OnClickListener tv_listener, OnBackPressListener back_listener) {
        DialogPlus dialog = DialogPlus.newDialog(act)
                .setContentHolder(new ViewHolder(R.layout.dia_photo))
                .setContentBackgroundResource(R.color.transparent)
                .setOverlayBackgroundResource(R.color.transparent)
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOnClickListener(tv_listener)
                .setGravity(Gravity.BOTTOM)
                .setCancelable(true)
                .setOnBackPressListener(back_listener)
                .create();

        View view = dialog.getHolderView();
        if (!TextUtils.isEmpty(title_1) && !TextUtils.isEmpty(title_2)) {
            ((TextView) view.findViewById(R.id.photo_take_photo)).setText(title_1);
            ((TextView) view.findViewById(R.id.photo_select_album)).setText(title_2);
        }

        return dialog;
    }

    /**
     * 创建loading对话框
     *
     * @param act
     * @param back_listener
     * @return
     */
    public static DialogPlus showLoadingDia(Activity act, String loadText, OnBackPressListener back_listener) {
        DialogPlus dialog = DialogPlus.newDialog(act)
                .setContentHolder(new ViewHolder(R.layout.dia_loading))
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setGravity(Gravity.CENTER)
                .setCancelable(false)
                .setOnBackPressListener(back_listener)
                .create();

        View view = dialog.getHolderView();
        if (!TextUtils.isEmpty(loadText)) {
            TextView load_text = (TextView) view.findViewById(R.id.load_text);
            load_text.setText(loadText);
        }
        dialog.show();
        return dialog;
    }

    /**
     * @param act
     * @param title
     * @param content
     * @param btn
     * @param tv_listener
     * @return
     */
    public static DialogPlus showErrDia(Activity act, String title, String content, String btn, OnClickListener tv_listener) {
        DialogPlus dialog = DialogPlus.newDialog(act)
                .setContentHolder(new ViewHolder(com.cwc.mylibrary.R.layout.dia_err))
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOnClickListener(tv_listener)
                .setGravity(Gravity.CENTER)
                .setCancelable(false)
                .create();
        View view = dialog.getHolderView();
        ((TextView) view.findViewById(com.cwc.mylibrary.R.id.err_title)).setText(title);
        ((TextView) view.findViewById(com.cwc.mylibrary.R.id.err_content)).setText(content);
        ((TextView) view.findViewById(com.cwc.mylibrary.R.id.err_btn)).setText(btn);

        dialog.show();

        return dialog;
    }
    public static DialogPlus showErrDia1(Activity act, String title, String content, String btn, OnClickListener tv_listener) {
        DialogPlus dialog = DialogPlus.newDialog(act)
                .setContentHolder(new ViewHolder(com.cwc.mylibrary.R.layout.dia_err))
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOnClickListener(tv_listener)
                .setGravity(Gravity.CENTER)
                .setCancelable(false)
                .create();
        View view = dialog.getHolderView();
        ((TextView) view.findViewById(com.cwc.mylibrary.R.id.err_title)).setText(title);
        ((TextView) view.findViewById(com.cwc.mylibrary.R.id.err_content)).setText(content);
        ((TextView) view.findViewById(com.cwc.mylibrary.R.id.err_btn)).setText(btn);

        dialog.show();

        return dialog;
    }
    public interface OnMyTipDiaListener {
        void OnLeftClick(DialogPlus dialog, View v);

        void OnRightClick(DialogPlus dialog, View v);
    }

}
