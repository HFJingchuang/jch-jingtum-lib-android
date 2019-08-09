package com.android.jtblk.utils;

import android.text.TextUtils;

import com.android.jtblk.client.Wallet;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtils {

    /**
     * 校验钱包地址是否有效
     *
     * @param address
     * @return
     */
    public static boolean isValidAddress(String address) {
        if (!TextUtils.isEmpty(address)) {
            return Wallet.isValidAddress(address);
        }
        return false;
    }

    /**
     * 判断string是否为数字
     *
     * @param ledger_index
     * @return
     */
    public static boolean isNumeric(String ledger_index) {
        if (!TextUtils.isEmpty(ledger_index)) {
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(ledger_index);
            if (!isNum.matches()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 交易交易类型是否有效
     *
     * @param type  关系类型
     * @param value 关系值
     * @return
     */
    public static boolean isValidType(String type, String value) {
        if (StringUtils.isEmpty(type) || StringUtils.isEmpty(value)) {
            return false;
        }
        String[] offer_types = new String[]{"Sell", "Buy"};
        String[] relation_types = new String[]{"trust", "authorize", "freeze", "unfreeze"};
        String[] accountSet_types = new String[]{"property", "delegate", "signer"};
        boolean flag = false;
        switch (type) {
            case "relation":
                if (ArrayUtils.contains(relation_types, value)) {
                    flag = true;
                }
                break;
            case "offer":
                if (ArrayUtils.contains(offer_types, value)) {
                    flag = true;
                }
                break;
            case "accountSet":
                if (ArrayUtils.contains(accountSet_types, value)) {
                    flag = true;
                }
                break;
            default:
        }
        return flag;
    }
}
