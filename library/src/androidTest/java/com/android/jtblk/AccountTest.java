package com.android.jtblk;

import android.support.test.runner.AndroidJUnit4;

import com.android.jtblk.client.Remote;
import com.android.jtblk.client.bean.AccountInfo;
import com.android.jtblk.client.bean.AccountOffers;
import com.android.jtblk.client.bean.AccountRelations;
import com.android.jtblk.client.bean.AccountTums;
import com.android.jtblk.client.bean.Line;
import com.android.jtblk.client.bean.Offer;
import com.android.jtblk.connection.Connection;
import com.android.jtblk.connection.ConnectionFactory;
import com.android.jtblk.utils.JsonUtils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AccountTest {
    static String addr = "jBvrdYc6G437hipoCiEpTwrWSRBS2ahXN6";
    static String server = "wss://s.jingtum.com:5020";// 生产环境
    //    static String server = "ws://ts5.jingtum.com:5020";// 测试环境
    static Boolean local_sign = true;// 是否使用本地签名方式提交交易
    static Connection conn = ConnectionFactory.getCollection(server);
    static Remote remote = new Remote(conn, local_sign);

    /**
     * 获取账户swt
     */
    @Test
    public void getAccountSwtc() {

        AccountInfo result1 = remote.requestAccountInfo(addr, null, "trust");
        Assert.assertEquals(addr, result1.getAccountData().getAccount());
        AccountInfo result2 = remote.requestAccountInfo(addr, null, "authorize");
        Assert.assertEquals(addr, result2.getAccountData().getAccount());
        AccountInfo result3 = remote.requestAccountInfo(addr, null, "freeze");
        Assert.assertEquals(addr, result3.getAccountData().getAccount());
    }

    /**
     * 获取账户可接收和发送的货币
     */
    @Test
    public void getAccountTum() {
        AccountTums result = remote.requestAccountTums(addr, null);
        Assert.assertNotEquals(0, result.getReceiveCurrencies().size());
        Assert.assertNotEquals(0, result.getSendCurrencies().size());
    }

    /**
     * 获取账户关系（trust:账户拥有的其他通证）
     */
    @Test
    public void getAccountRelations() {
        AccountRelations result1 = remote.requestAccountRelations(addr, null, "trust");
        AccountRelations result2 = remote.requestAccountRelations(addr, null, "freeze");
        AccountRelations result3 = remote.requestAccountRelations(addr, null, "authorize");
        Assert.assertNotEquals(0, result1.getLines().size());
        for (Line line : result1.getLines()) {
            System.out.println("编码：" + line.getCurrency());
            System.out.println("数量：" + line.getBalance());
            System.out.println("银关：" + line.getAccount());
            System.out.println("============trust====================");
        }
        for (Line line : result2.getLines()) {
            System.out.println("编码：" + line.getCurrency());
            System.out.println("发行方：" + line.getIssuer());
            System.out.println("冻结数量：" + line.getLimit());
            System.out.println("冻结指向账户：" + line.getLimitPeer());
            System.out.println("=============freeze===================");
        }
        for (Line line : result3.getLines()) {
            System.out.println("编码：" + line.getCurrency());
            System.out.println("发行方：" + line.getIssuer());
            System.out.println("冻结数量：" + line.getLimit());
            System.out.println("冻结指向账户：" + line.getLimitPeer());
            System.out.println("============authorize====================");
        }
    }

    /**
     * 获取账号订单列表
     *
     * @return
     */
    public static AccountOffers getAccountOffers() {
        AccountOffers bean = remote.requestAccountOffers(addr, null);
        Assert.assertEquals(1, bean.getOffers().size());
        return bean;
    }
}
