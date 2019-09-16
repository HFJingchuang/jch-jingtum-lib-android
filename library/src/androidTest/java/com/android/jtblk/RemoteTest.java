package com.android.jtblk;

import android.support.test.runner.AndroidJUnit4;
import android.text.TextUtils;
import android.util.Log;

import com.android.jtblk.client.Remote;
import com.android.jtblk.client.Transaction;
import com.android.jtblk.client.bean.Account;
import com.android.jtblk.client.bean.AccountInfo;
import com.android.jtblk.client.bean.AccountTx;
import com.android.jtblk.client.bean.AmountInfo;
import com.android.jtblk.client.bean.Memo;
import com.android.jtblk.client.bean.TransactionInfo;
import com.android.jtblk.client.bean.Transactions;
import com.android.jtblk.connection.Connection;
import com.android.jtblk.connection.ConnectionFactory;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 交易相关测试
 *
 * @author yyl
 * @date 2019年5月17日
 */
@RunWith(AndroidJUnit4.class)
public class RemoteTest {

    static String server = "wss://s.jingtum.com:5020";
    static Boolean local_sign = true;
    static Connection conn = ConnectionFactory.getCollection(server);
    static Remote remote = new Remote(conn, local_sign);

    /**
     * 数据上链
     *
     * @return
     */
    @Test
    public void pollServerTest()  {
        List<String> nodeList = new ArrayList<>();
        nodeList.add("ws://118.24.14.162:5020");
        nodeList.add("wss://s.jingtum.com:5020");
        nodeList.add("wss://106.14.154.38:5021");
        for (int i = 0; i < nodeList.size(); i++) {
            try {
                conn.close();
                server = nodeList.get(i);
                conn = ConnectionFactory.getCollection(server);
                remote = new Remote(conn, local_sign);
                Log.v("RemoteTest", server);
                if (TextUtils.equals(remote.getStatus(), "OPEN")) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Assert.assertEquals("wss://s.jingtum.com:5020", server);
    }

}
