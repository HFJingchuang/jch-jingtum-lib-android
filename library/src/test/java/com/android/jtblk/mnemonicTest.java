package com.android.jtblk;

import com.android.jtblk.BIP39.WordCount;
import com.android.jtblk.BIP39.wordlists.Chinese_simplified;
import com.android.jtblk.BIP39.wordlists.English;
import com.android.jtblk.BIP44.AddressIndex;
import com.android.jtblk.BIP44.BIP44;
import com.android.jtblk.BIP44.Bip44WalletGenerator;
import com.android.jtblk.BIP44.CoinTypes;
import com.android.jtblk.client.Transaction;
import com.android.jtblk.client.Wallet;
import com.android.jtblk.client.bean.AmountInfo;
import com.android.jtblk.client.bean.TransactionInfo;
import com.android.jtblk.config.Config;
import com.android.jtblk.core.coretypes.AccountID;
import com.android.jtblk.core.coretypes.Amount;
import com.android.jtblk.core.coretypes.Currency;
import com.android.jtblk.core.coretypes.uint.UInt32;
import com.android.jtblk.core.runtime.Value;
import com.android.jtblk.core.types.known.tx.signed.SignedTransaction;
import com.android.jtblk.core.types.known.tx.txns.Payment;
import com.android.jtblk.crypto.ecdsa.Seed;
import com.android.jtblk.keyStore.CipherException;
import com.android.jtblk.keyStore.KeyStore;
import com.android.jtblk.keyStore.KeyStoreFile;

import org.junit.Assert;
import org.junit.Test;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.android.jtblk.utils.Utils.bigHex;
import static com.android.jtblk.utils.Utils.uBigInt;

/**
 * Example local unit test, which will executeexecute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class mnemonicTest {


    @Test
    public void mnemonic() {
        AddressIndex addressIndex = BIP44.m()
                .purpose44()
                .coinType(CoinTypes.SWTC)
                .account(0)
                .external()
                .address(0);
        Wallet wallet = new Bip44WalletGenerator(Chinese_simplified.INSTANCE, WordCount.TWELVE).generateBip44Wallet(null, addressIndex);
        System.out.println(wallet.getAddress());
        System.out.println(wallet.getMnemonics());
        System.out.println(wallet.getSecret());
    }

    @Test
    public void fromMnemonic() {
        AddressIndex addressIndex = BIP44.m()
                .purpose44()
                .coinType(CoinTypes.SWTC)
                .account(0)
                .external()
                .address(0);
        String mnemonic = "这 辊 稳 面 瞧 穿 灾 棋 送 安 路 谐";
        Wallet wallet = Bip44WalletGenerator.fromMnemonic(mnemonic, null, addressIndex);
        Assert.assertEquals("j4fkSm9kUHXtXhA3pj2dNnmSHtuqtT76Ka", wallet.getAddress());
        Assert.assertEquals("这 辊 稳 面 瞧 穿 灾 棋 送 安 路 谐", wallet.getMnemonics());
        Assert.assertEquals("a348f30450b51afd11714e0cc5d8b0c7adba7f5a1021c4281c2edad6230aff05", wallet.getSecret().toLowerCase());

        AddressIndex addressIndex1 = BIP44.m()
                .purpose44()
                .coinType(CoinTypes.SWTC)
                .account(0)
                .external()
                .address(1);
        wallet = Bip44WalletGenerator.fromMnemonic(mnemonic, null, addressIndex1);
        Assert.assertEquals("jfs2y3SRSJ8gRMfTJQfb2cpTh9LTBZ1ipk", wallet.getAddress());
        Assert.assertEquals("这 辊 稳 面 瞧 穿 灾 棋 送 安 路 谐", wallet.getMnemonics());
        Assert.assertEquals("a96167544e8747d5459eb347ce03d44e862878ddc9a6bbb0194291577f8cb7dd", wallet.getSecret().toLowerCase());
    }

    @Test
    public void mnemonic2KeyStore() {
        AddressIndex addressIndex = BIP44.m()
                .purpose44()
                .coinType(CoinTypes.SWTC)
                .account(0)
                .external()
                .address(0);
        String mnemonic = "这 辊 稳 面 瞧 穿 灾 棋 送 安 路 谐";
        Wallet wallet = Bip44WalletGenerator.fromMnemonic(mnemonic, null, addressIndex);

        try {
            KeyStoreFile keyStoreFile = KeyStore.createLight("Key123456", wallet);
            wallet = KeyStore.decrypt("Key123456", keyStoreFile);
            Assert.assertEquals("j4fkSm9kUHXtXhA3pj2dNnmSHtuqtT76Ka", wallet.getAddress());
            Assert.assertEquals("a348f30450b51afd11714e0cc5d8b0c7adba7f5a1021c4281c2edad6230aff05", wallet.getSecret().toLowerCase());
        } catch (CipherException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void signLocal() {
        Wallet wallet = Wallet.generate();

//        AmountInfo amountInfo = new AmountInfo();
//        amountInfo.setCurrency("SWT");// 转出代币简称
//        amountInfo.setValue("0.001");// 转出代币数量
//        amountInfo.setIssuer("");// 转出代币银关

        Payment payment = new Payment();
        payment.as(AccountID.Account, wallet.getAddress());
        payment.as(AccountID.Destination, "j4fkSm9kUHXtXhA3pj2dNnmSHtuqtT76Ka");
        payment.as(Amount.Amount, "0.001");
        payment.as(Amount.Fee, String.valueOf(Config.FEE));// 交易燃料费
        payment.sequence(new UInt32(1));// 转出地址序列号
        payment.flags(new UInt32(0));
        List<String> memos = new ArrayList<String>();// 交易备注
        memos.add("SWT转账");
        memos.add("测试数据1");
        memos.add("测试数据2");
        payment.addMemo(memos);
        SignedTransaction signedTx = payment.sign(wallet.getSecret());// 签名
        SignedTransaction signedTx1 = payment.sign(wallet.getKeypairs().privHex());// 签名
        System.out.println(signedTx.tx_blob);
        Assert.assertEquals(signedTx.tx_blob, signedTx1.tx_blob);

    }
}