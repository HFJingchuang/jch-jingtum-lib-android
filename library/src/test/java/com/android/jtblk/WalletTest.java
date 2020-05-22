package com.android.jtblk;

import com.android.jtblk.BIP39.WordCount;
import com.android.jtblk.BIP39.wordlists.Chinese_simplified;
import com.android.jtblk.BIP44.AddressIndex;
import com.android.jtblk.BIP44.BIP44;
import com.android.jtblk.BIP44.Bip44WalletGenerator;
import com.android.jtblk.BIP44.CoinTypes;
import com.android.jtblk.client.Wallet;

import org.junit.Assert;
import org.junit.Test;

/**
 * Example local unit test, which will executeexecute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class WalletTest {


    @Test
    public void generate() {
        Wallet wallet = Wallet.generate(Chinese_simplified.INSTANCE, WordCount.TWELVE, false);
        System.out.println(wallet.getAddress());
        System.out.println(wallet.getSecret());
        System.out.println(wallet.getMnemonics());
        System.out.println(wallet.getKeypairs().canonicalPubHex());
        System.out.println(wallet.getKeypairs().canonicalPriHex());
    }

    @Test
    public void generateED25519() {
        Wallet wallet = Wallet.generate(Chinese_simplified.INSTANCE, WordCount.TWELVE, true);
        System.out.println(wallet.getAddress());
        System.out.println(wallet.getSecret());
        System.out.println(wallet.getMnemonics());
        System.out.println(wallet.getKeypairs().canonicalPubHex());
        System.out.println(wallet.getKeypairs().canonicalPriHex());
    }

    @Test
    public void fromSecret() {
        Wallet wallet = Wallet.fromSecret("EDD430F8AC8F8CDA0EC9549B15DFAAA9B3EC33071F2856BDDC182FDF594086B3C3", true);
        Assert.assertEquals("jJ8pcZBHGhDR8if4PB1GNmbC2GrNdTqjP9", wallet.getAddress());
        Assert.assertEquals("EDD430F8AC8F8CDA0EC9549B15DFAAA9B3EC33071F2856BDDC182FDF594086B3C3", wallet.getSecret());


        wallet = Wallet.fromSecret("00225B5F82E41415A87348C10200806815D8B8E2AD2588394CBCEF20FE162732C2", true);
        Assert.assertEquals("j9hLkMCGvYY69z7N9kBW9DNj4GGWVedwSH", wallet.getAddress());
        Assert.assertEquals("00225B5F82E41415A87348C10200806815D8B8E2AD2588394CBCEF20FE162732C2", wallet.getSecret());
    }

    @Test
    public void fromMnemonics() {
        String mnemonic = "汁 凡 表 差 钉 念 啦 凯 芳 炎 敌 际";
        Wallet wallet = Wallet.fromMnemonics(mnemonic, true);
        Assert.assertEquals("jhDPP9naq8ZtPBFimLGN4mHPWhQpjR9EX4", wallet.getAddress());
        Assert.assertEquals("sEdT1XqD8P9vWmi9F2GP3buMpyHDajW", wallet.getSecret());

        mnemonic = "诬 雕 霍 含 复 消 潜 代 借 恐 充 飘";
        wallet = Wallet.fromMnemonics(mnemonic, false);
        Assert.assertEquals("jLb68W1yAwEmQGai1REGFzYWDbCaZA7XQK", wallet.getAddress());
        Assert.assertEquals("sh3DaceYnV4LwqjSuS2Yy65QD7D51", wallet.getSecret());
    }

    @Test
    public void fromMnemonicWithPath() {
        AddressIndex addressIndex = BIP44.m()
                .purpose44()
                .coinType(CoinTypes.SWTC)
                .account(0)
                .external()
                .address(0);
        String mnemonic = "利 闷 辑 态 狂 诸 追 曹 给 补 乳 逻";
        Wallet wallet = Wallet.fromMnemonicWithPath(mnemonic, addressIndex, true);
        Assert.assertEquals("jPc7zYrBqQGhsC6keSzKdbCF1xtkNbgfeN", wallet.getAddress());
        Assert.assertEquals("利 闷 辑 态 狂 诸 追 曹 给 补 乳 逻", wallet.getMnemonics());
        Assert.assertEquals("ED8DC3100DBC100C954E0789F7EE47BD95042D011F2C6A39D5BD6AA2CD885FF1D3", wallet.getSecret().toUpperCase());


        mnemonic = "馏 标 骗 孩 挑 蒸 座 希 原 门 挖 序";
        wallet = Wallet.fromMnemonicWithPath(mnemonic, addressIndex, false);
        Assert.assertEquals("j41M6Qjp7k9CcrRMA4vZrX37HorymBHUaB", wallet.getAddress());
        Assert.assertEquals("馏 标 骗 孩 挑 蒸 座 希 原 门 挖 序", wallet.getMnemonics());
        Assert.assertEquals("005570847FC13CC17B93637BC54A200748EC2538385FB4E5DFDDDCE3703A0E873D", wallet.getSecret().toUpperCase());

        addressIndex = BIP44.m()
                .purpose44()
                .coinType(CoinTypes.SWTC)
                .account(0)
                .external()
                .address(1);

        mnemonic = "接 呼 彩 它 巡 文 建 烟 轰 若 雕 隆";
        wallet = Wallet.fromMnemonicWithPath(mnemonic, addressIndex, false);
        Assert.assertEquals("jQfAkMrHxb3X8kuPzPaX2ugNG8GMpYNLTk", wallet.getAddress());
        Assert.assertEquals("接 呼 彩 它 巡 文 建 烟 轰 若 雕 隆", wallet.getMnemonics());
        Assert.assertEquals("0013EDBDAE522E426F60CD265F5403EF79B81ACAD4600410D1F2A4577716F4588C", wallet.getSecret().toUpperCase());

        mnemonic = "希 阁 问 辑 漂 可 存 民 狠 搭 棒 摇";
        wallet = Wallet.fromMnemonicWithPath(mnemonic, addressIndex, true);
        Assert.assertEquals("jwGg4kKqkaDc5HRVFnTmFrgRhDw7vEjwRE", wallet.getAddress());
        Assert.assertEquals("希 阁 问 辑 漂 可 存 民 狠 搭 棒 摇", wallet.getMnemonics());
        Assert.assertEquals("EDF228D440049F6C9A65B13180B43FB6C0775165C719832F97EB244166D44DECC9", wallet.getSecret().toUpperCase());

    }
}