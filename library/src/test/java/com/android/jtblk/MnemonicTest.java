package com.android.jtblk;

import com.android.jtblk.BIP39.WordCount;
import com.android.jtblk.BIP39.wordlists.Chinese_simplified;
import com.android.jtblk.BIP44.AddressIndex;
import com.android.jtblk.BIP44.BIP44;
import com.android.jtblk.BIP44.Bip44WalletGenerator;
import com.android.jtblk.BIP44.CoinTypes;
import com.android.jtblk.client.Wallet;
import com.android.jtblk.config.Config;
import com.android.jtblk.core.coretypes.AccountID;
import com.android.jtblk.core.coretypes.Amount;
import com.android.jtblk.core.coretypes.uint.UInt32;
import com.android.jtblk.core.types.known.tx.signed.SignedTransaction;
import com.android.jtblk.core.types.known.tx.txns.Payment;
import com.android.jtblk.keyStore.CipherException;
import com.android.jtblk.keyStore.KeyStore;
import com.android.jtblk.keyStore.KeyStoreFile;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Example local unit test, which will executeexecute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class MnemonicTest {

    @Test
    public void generateWallet() {
        Wallet wallet = new Bip44WalletGenerator(Chinese_simplified.INSTANCE, WordCount.TWELVE).generateWallet(null, false);
        System.out.println(wallet.getAddress());
        System.out.println(wallet.getMnemonics());
        System.out.println(wallet.getSecret());
    }

    @Test
    public void generateBip44Wallet() {
        AddressIndex addressIndex = BIP44.m()
                .purpose44()
                .coinType(CoinTypes.SWTC)
                .account(0)
                .external()
                .address(0);
        Wallet wallet = new Bip44WalletGenerator(Chinese_simplified.INSTANCE, WordCount.TWELVE).generateBip44Wallet(null, addressIndex, false);
        System.out.println(wallet.getAddress());
        System.out.println(wallet.getMnemonics());
        System.out.println(wallet.getSecret());

        wallet = new Bip44WalletGenerator(Chinese_simplified.INSTANCE, WordCount.TWELVE).generateBip44Wallet(null, addressIndex, true);
        System.out.println(wallet.getAddress());
        System.out.println(wallet.getMnemonics());
        System.out.println(wallet.getSecret());

        addressIndex = BIP44.m()
                .purpose44()
                .coinType(CoinTypes.SWTC)
                .account(0)
                .external()
                .address(1);

        wallet = new Bip44WalletGenerator(Chinese_simplified.INSTANCE, WordCount.TWELVE).generateBip44Wallet(null, addressIndex, false);
        System.out.println(wallet.getAddress());
        System.out.println(wallet.getMnemonics());
        System.out.println(wallet.getSecret());

        wallet = new Bip44WalletGenerator(Chinese_simplified.INSTANCE, WordCount.TWELVE).generateBip44Wallet(null, addressIndex, true);
        System.out.println(wallet.getAddress());
        System.out.println(wallet.getMnemonics());
        System.out.println(wallet.getSecret());
    }

    @Test
    public void fromMnemonicWithPath() {
        AddressIndex addressIndex = BIP44.m()
                .purpose44()
                .coinType(CoinTypes.SWTC)
                .account(0)
                .external()
                .address(0);
        String mnemonic = "馏 标 骗 孩 挑 蒸 座 希 原 门 挖 序";
        Wallet wallet = Bip44WalletGenerator.fromMnemonicWithPath(mnemonic, null, addressIndex, false);
        Assert.assertEquals("j41M6Qjp7k9CcrRMA4vZrX37HorymBHUaB", wallet.getAddress());
        Assert.assertEquals("馏 标 骗 孩 挑 蒸 座 希 原 门 挖 序", wallet.getMnemonics());
        Assert.assertEquals("005570847FC13CC17B93637BC54A200748EC2538385FB4E5DFDDDCE3703A0E873D", wallet.getSecret().toUpperCase());

        mnemonic = "利 闷 辑 态 狂 诸 追 曹 给 补 乳 逻";
        wallet = Bip44WalletGenerator.fromMnemonicWithPath(mnemonic, null, addressIndex, true);
        Assert.assertEquals("jPc7zYrBqQGhsC6keSzKdbCF1xtkNbgfeN", wallet.getAddress());
        Assert.assertEquals("利 闷 辑 态 狂 诸 追 曹 给 补 乳 逻", wallet.getMnemonics());
        Assert.assertEquals("ED8DC3100DBC100C954E0789F7EE47BD95042D011F2C6A39D5BD6AA2CD885FF1D3", wallet.getSecret().toUpperCase());

        addressIndex = BIP44.m()
                .purpose44()
                .coinType(CoinTypes.SWTC)
                .account(0)
                .external()
                .address(1);

        mnemonic = "接 呼 彩 它 巡 文 建 烟 轰 若 雕 隆";
        wallet = Bip44WalletGenerator.fromMnemonicWithPath(mnemonic, null, addressIndex, false);
        Assert.assertEquals("jQfAkMrHxb3X8kuPzPaX2ugNG8GMpYNLTk", wallet.getAddress());
        Assert.assertEquals("接 呼 彩 它 巡 文 建 烟 轰 若 雕 隆", wallet.getMnemonics());
        Assert.assertEquals("0013EDBDAE522E426F60CD265F5403EF79B81ACAD4600410D1F2A4577716F4588C", wallet.getSecret().toUpperCase());

        mnemonic = "希 阁 问 辑 漂 可 存 民 狠 搭 棒 摇";
        wallet = Bip44WalletGenerator.fromMnemonicWithPath(mnemonic, null, addressIndex, true);
        Assert.assertEquals("jwGg4kKqkaDc5HRVFnTmFrgRhDw7vEjwRE", wallet.getAddress());
        Assert.assertEquals("希 阁 问 辑 漂 可 存 民 狠 搭 棒 摇", wallet.getMnemonics());
        Assert.assertEquals("EDF228D440049F6C9A65B13180B43FB6C0775165C719832F97EB244166D44DECC9", wallet.getSecret().toUpperCase());
    }

    @Test
    public void fromMnemonic() {
        String mnemonic = "头 玄 秒 站 致 源 省 却 景 熙 奇 将";
        Wallet wallet = Bip44WalletGenerator.fromMnemonic(mnemonic, null, false);
        Assert.assertEquals("jHJSCzjAeZWJrR1MKzU3JdWSTrivHCccLV", wallet.getAddress());
        Assert.assertEquals("头 玄 秒 站 致 源 省 却 景 熙 奇 将", wallet.getMnemonics());
        Assert.assertEquals("shc9m9iNVPcnhxfedr5kVa2vg2Xov", wallet.getSecret());
    }

    @Test
    public void mnemonic2KeyStore() {
        AddressIndex addressIndex = BIP44.m()
                .purpose44()
                .coinType(CoinTypes.SWTC)
                .account(0)
                .external()
                .address(0);
        String mnemonic = "毅 层 驾 兼 岛 河 练 骨 夺 阻 释 宪";
        Wallet wallet = Bip44WalletGenerator.fromMnemonicWithPath(mnemonic, null, addressIndex, true);

        try {
            KeyStoreFile keyStoreFile = KeyStore.createLight("Key123456", wallet);
            wallet = KeyStore.decrypt("Key123456", keyStoreFile);
            Assert.assertEquals("jBLdBSKhbBdvAduhrhfXf6jzX1smHbctS", wallet.getAddress());
            Assert.assertEquals("EDD2EA275A5A0BB0C51F9BADE9EDA282E13121DAFEC76FBD4179B4A0B107976FA7", wallet.getSecret().toUpperCase());
        } catch (CipherException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void signLocal() {
        Wallet wallet = Wallet.generate(Chinese_simplified.INSTANCE, WordCount.TWELVE, false);

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
        SignedTransaction signedTx1 = payment.sign(wallet.getKeypairs().canonicalPriHex());// 签名
        Assert.assertEquals(signedTx.tx_blob, signedTx1.tx_blob);

        Wallet wallet1 = Wallet.generate(Chinese_simplified.INSTANCE, WordCount.TWELVE, true);

        payment = new Payment();
        payment.as(AccountID.Account, wallet1.getAddress());
        payment.as(AccountID.Destination, "j4fkSm9kUHXtXhA3pj2dNnmSHtuqtT76Ka");
        payment.as(Amount.Amount, "0.001");
        payment.as(Amount.Fee, String.valueOf(Config.FEE));// 交易燃料费
        payment.sequence(new UInt32(1));// 转出地址序列号
        payment.flags(new UInt32(0));
        payment.addMemo(memos);
        signedTx = payment.sign(wallet1.getSecret());// 签名
        signedTx1 = payment.sign(wallet1.getKeypairs().canonicalPriHex());// 签名
        Assert.assertEquals(signedTx.tx_blob, signedTx1.tx_blob);

    }
}