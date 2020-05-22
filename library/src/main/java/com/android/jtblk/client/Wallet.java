package com.android.jtblk.client;

import com.android.jtblk.crypto.ecdsa.IKeyPair;
import com.android.jtblk.crypto.ecdsa.Seed;
import com.android.jtblk.encoding.B58IdentiferCodecs;
import com.android.jtblk.encoding.common.B16;
import com.android.jtblk.exceptions.PrivateKeyFormatException;

import static com.android.jtblk.config.Config.getB58IdentiferCodecs;

public class Wallet {
    private IKeyPair keypairs = null;
    private String secret = null;
    private String mnemonics = null;

    public Wallet() {
    }

    public Wallet(String secret, String mnemonics) {
        this.keypairs = Seed.fromBase58(secret).keyPair();
        this.secret = secret;
        this.mnemonics = mnemonics;
    }

    /**
     * 随机生成钱包地址
     *
     * @return
     */
    public static Wallet generate() {
        String secret = new Seed().random();
        IKeyPair keypairs = Seed.fromBase58(secret).keyPair();
        Wallet wallet = new Wallet();
        wallet.setKeypairs(keypairs);
        wallet.setSecret(secret);
        return wallet;
    }

    /**
     * 随机生成ED25519钱包地址
     *
     * @return
     */
    public static Wallet generateED25519() {
        Seed seed = new Seed();
        seed.setEd25519();
        String secret = seed.random();
        IKeyPair keypairs = Seed.fromBase58(secret).keyPair();
        Wallet wallet = new Wallet();
        wallet.setKeypairs(keypairs);
        wallet.setSecret(secret);
        return wallet;
    }

    /**
     * 根据密钥生成钱包
     *
     * @param secret
     * @param isED25519
     * @return
     */
    public static Wallet fromSecret(String secret, boolean isED25519) {
        Wallet wallet = new Wallet();
        if (Wallet.isValidSecret(secret)) {
            IKeyPair keypairs = Seed.fromBase58(secret).keyPair();
            wallet = new Wallet();
            wallet.setKeypairs(keypairs);
            wallet.setSecret(secret);
        } else {
            Seed seed = new Seed();
            if (secret.length() == 64) {
                if (isED25519) {
                    seed.setEd25519();
                }
            } else if (secret.length() == 66) {
                if (secret.toUpperCase().startsWith("ED")) {
                    seed.setEd25519();
                }
                secret = secret.substring(2);
            } else {
                throw new PrivateKeyFormatException("deriving keypair requires correct prefixed private key");
            }
            wallet.setKeypairs(seed.fromPrivateKey(secret));
        }
        return wallet;
    }

    public String getPublicKey() {
        if (this.keypairs == null) {
            return null;
        }
        return this.keypairs.canonicalPubHex();
    }

    /**
     * 使用钱包密钥对信息进行签名
     *
     * @param message
     * @return
     */
    public String sign(String message) {
        byte[] der = this.keypairs.signMessage(message.getBytes());
        return B16.encode(der);
    }

    /**
     * 校验信息的自作签名是否正确
     *
     * @param message
     * @param signature
     * @return
     */
    public boolean verify(String message, String signature) {
        // (byte[] hash, byte[] sigBytes
        return this.keypairs.verifySignature(message.getBytes(), signature.getBytes());
    }

    /**
     * 获取公钥地址
     *
     * @return
     */
    public String getAddress() {
        byte[] bytes = this.keypairs.pub160Hash();
        return encodeAddress(bytes);
    }

    /**
     * 工具方法：公钥编码
     *
     * @return
     */
    private static String encodeAddress(byte[] a) {
        return getB58IdentiferCodecs().encodeAddress(a);
    }

    public static boolean isValidAddress(String address) {
        try {
            getB58IdentiferCodecs().decode(address, B58IdentiferCodecs.VER_ACCOUNT_ID);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidSecret(String secret) {
        try {
            Seed.fromBase58(secret).keyPair();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getSecret() {
        if (this.secret == null || this.secret.isEmpty()) {
            return this.keypairs.canonicalPriHex();
        } else {
            return this.secret;
        }
    }

    public IKeyPair getKeypairs() {
        return keypairs;
    }

    public void setKeypairs(IKeyPair keypairs) {
        this.keypairs = keypairs;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getMnemonics() {
        return this.mnemonics;
    }

    public void setMnemonics(String mnemonics) {
        this.mnemonics = mnemonics;
    }
}