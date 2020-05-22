package com.android.jtblk;

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
        Wallet wallet = Wallet.generate();
        System.out.println(wallet.getAddress());
        System.out.println(wallet.getSecret());
        System.out.println(wallet.getKeypairs().canonicalPubHex());
        System.out.println(wallet.getKeypairs().canonicalPriHex());
    }

    @Test
    public void generateED25519() {
        Wallet wallet = Wallet.generateED25519();
        System.out.println(wallet.getAddress());
        System.out.println(wallet.getSecret());
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
}