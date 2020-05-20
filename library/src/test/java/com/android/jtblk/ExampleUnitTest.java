package com.android.jtblk;

import com.android.jtblk.client.Remote;
import com.android.jtblk.client.Transaction;
import com.android.jtblk.client.Wallet;
import com.android.jtblk.client.bean.AmountInfo;
import com.android.jtblk.client.bean.TransactionInfo;
import com.android.jtblk.connection.Connection;
import com.android.jtblk.connection.ConnectionFactory;
import com.android.jtblk.crypto.ecdsa.IKeyPair;
import com.android.jtblk.crypto.ecdsa.Seed;
import com.android.jtblk.keyStore.CipherException;
import com.android.jtblk.keyStore.KeyStore;
import com.android.jtblk.keyStore.KeyStoreFile;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    // 生产环境
//    static String server = "wss://s.jingtum.com:5020";
    // 测试环境
    private static String server = "ws://ts5.jingtum.com:5030";
    // 是否使用本地签名方式提交交易
    private static Boolean local_sign = true;
    private static Connection conn = ConnectionFactory.getCollection(server);
    private static Remote remote = new Remote(conn, local_sign);

    @Test
    public void keyStore() {
        Wallet jtKeyPair = new Wallet("shExMjiMqza4DdMaSg3ra9vxWPZsQ", null);
        try {
            KeyStoreFile keyStoreFile = KeyStore.createLight("Key123456", jtKeyPair);
            System.out.println("===============keyStore============");
            System.out.println(keyStoreFile.toString());
            Wallet decryptEthECKeyPair = KeyStore.decrypt("Key123456", keyStoreFile);
            System.out.println("address:" + decryptEthECKeyPair.getAddress());
            System.out.println("PrivateKey:" + decryptEthECKeyPair.getSecret());
        } catch (CipherException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void transfer() throws Exception {
        AmountInfo amount = new AmountInfo();
        amount.setCurrency("SWT");
        amount.setValue("10");
        Transaction tx = remote.buildPaymentTx("jwh7eSszdyTxCJQzmV7rUJbwS6qFRATiEq", "j4fkSm9kUHXtXhA3pj2dNnmSHtuqtT76Ka", amount);
        tx.setSecret("shvXBLBDdohsXHdvJx8BqRNLvLAyW");
        List<String> memos = new ArrayList<String>();
        memos.add("测试转账");
        tx.addMemo(memos);
        TransactionInfo bean = tx.submit();
        System.out.println("===============transfer============");
        if ("0".equals(bean.getEngineResultCode())) {
            System.out.println("hash:" + bean.getTxJson().getHash());
        } else {
            System.out.println("msg:" + bean.getEngineResultMessage());
        }
    }
}