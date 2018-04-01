package com.nju.bigclassteacher.Util;

/**
 * 蓝牙消息封装工具类
 * 消息协议的封装
 */
public class ProtocolUtil {

    public static String bluetoothString(String content) {
        String data = null;
        String msgType = "C";
        data = String.format("%06x", calculatePlaces(msgType));
        data += msgType;
        data += String.format("%06x", calculatePlaces(content));
        data += content;
        data = String.format("%06x", calculatePlaces(data) + 1) + data;
        return data;
    }

    public static int calculatePlaces(String str) {
        int m = 0;
        char arr[] = str.toCharArray();
        for (char c : arr) {
            //中文字符
            if ((c >= 0x0391 && c <= 0xFFE5)) {
                m = m + 3;
            } else if (c <= 0x00FF) { //英文字符
                m = m + 1;
            }
        }
        return m;
    }

}