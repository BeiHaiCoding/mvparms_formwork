package com.cwc.mylibrary.utils;

/**
 * Created by Administrator on 2017/4/10.
 */

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.cwc.mylibrary.Log.MLogHelper;
import com.cwc.mylibrary.R;
import com.cwc.mylibrary.Toast.MToastHelper;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 其他
 */
public class OtherUtils {

    private static final int[] weightNumber = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1};
    private static final int[] checknumber = new int[]{1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2};

    /**
     * @param source
     * @return 根据字符串获取MD5值
     */
    public static String getMD5(String source) {
        return getMD5(source.getBytes());

    }

    /**
     * 获取MD5值
     *
     * @param source
     * @return
     */
    private static String getMD5(byte[] source) {
        String s = null;
        char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
                'e', 'f'};
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
            // 用字节表示就是 16 个字节
            // System.out.println("tem "+new String(tmp));
            char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
            // 所以表示成 16 进制需要 32 个字符
            int k = 0; // 表示转换结果中对应的字符位置
            for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
                // 转换成 16 进制字符的转换
                byte byte0 = tmp[i]; // 取第 i 个字节
                str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
                // >>>
                // 为逻辑右移，将符号位一起右移
                str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
            }
            s = new String(str); // 换后的结果转换为字符串

        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * 检查密码长度
     *
     * @param s
     * @param context
     * @param min
     * @param max
     * @return
     */
    public static boolean checkPwd(String s, Context context, int min, int max) {
        if (s.length() < min) {
            MToastHelper.showShort(context, "密码长度不能小于" + min + "位");
            return false;
        }
        if (s.length() > max) {
            MToastHelper.showShort(context, "密码长度不能大于" + max + "位");
            return false;
        }
        return true;
    }

    /**
     * 手机号码验证
     */
    public static boolean checkPhone(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(14[5,7])|(17[0-9])|(18[0-9])|(19[0-9])|(16[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }


    /**
     * 检查身份证格式
     *
     * @param idCardNum
     * @return
     */
    public static boolean checkIDCard(String idCardNum) {
        if (idCardNum.length() == 15) { // 如果要进行验证的身份证号码为15位
            idCardNum = fifteen2eighteen(idCardNum);  // 将其转换为18位
        }
        if (idCardNum.length() != 18) {
            return false;
        }
        String lastNum = idCardNum.substring(17, 18);  // 获取要进行验证身份证号码的校验号
        if (lastNum.equals(getLastNum(idCardNum))) {
            // 判断校验码是否正确
            return true;
        }
        return false;
    }


    // 获取身份证号码中的校验码
    private static String getLastNum(String idCardNum) {
        int verify = 0;
        idCardNum = idCardNum.substring(0, 17);
        // 获取身份证号码中的前17位
        int sum = 0;
        int[] wi = new int[17];  // 创建int型数组
        for (int i = 0; i < 17; i++) {  // 循环向数组赋值
            String temp = idCardNum.substring(i, i + 1);
            wi[i] = Integer.parseInt(temp);
        }
        for (int i = 0; i < 17; i++) {  // 循环遍历数组
            sum = sum + weightNumber[i] * wi[i];  // 对17位本利码加权求和
        }
        verify = sum % 11;  // 取模
        if (verify == 2) {  // 如果模为2，则返回"X"
            return "X";
        } else {
            return String.valueOf(checknumber[verify]);  // 否则返回对应的校验码
        }
    }

    // 将15位身份证号码转为18位身份证号码
    private static String fifteen2eighteen(String fifteenNumber) {
        String eighteenNumberBefore = fifteenNumber.substring(0, 6);  // 获取参数身份证号码中的地区码
        String eightNumberAfter = fifteenNumber.substring(6, 15);  // 获取参数身份证号码中的出生日期码
        String eighteenNumber;
        eighteenNumber = eighteenNumberBefore + "19";  // 将地区码后面加"19"
        eighteenNumber = eighteenNumber + eightNumberAfter;  // 获取地区码加出生日期码
        eighteenNumber = eighteenNumber + getLastNum(eighteenNumber);  // 获取身份证的校验码
        return eighteenNumber;  // 将转换后的身份证号码返回
    }

    /**
     * 将集合用","拼接起来
     * @param list
     * @return
     */
    public static String getStringFromList(List<String> list) {
        if (list == null || list.size() == 0) {
            return "";
        }
        String temp = "";
        for (String tempStr : list) {
            temp += (tempStr + ",");
        }
        temp = temp.substring(0, temp.length() - 1);
        MLogHelper.i("getStringFromList",temp);
        return temp;
    }

    public static SpannableString setTextColorWithSpan(Context context, int color, String content, int start, int end) {
        SpannableString spanString = new SpannableString(content);
        spanString.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(context, color)),
                start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return spanString;
    }

    /**
     * 2  * 获取版本号
     * 3  * @return 当前应用的版本号
     * 4
     */
    public static String getVersion(Activity activity) {
        try {
            PackageManager manager = activity.getPackageManager();
            PackageInfo info = manager.getPackageInfo(activity.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
