package com.android.jtblk.crypto.ecdsa;

import com.android.jtblk.config.Config;
import com.android.jtblk.encoding.B58IdentiferCodecs;
import com.android.jtblk.encoding.base58.B58;
import com.android.jtblk.utils.SecureRandomUtils;
import com.android.jtblk.utils.Sha512;
import com.android.jtblk.utils.Utils;

import org.web3j.utils.Numeric;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

import static com.android.jtblk.config.Config.getB58IdentiferCodecs;
import static com.android.jtblk.utils.Utils.uBigInt;

public class Seed {
    public static byte[] VER_K256 = new byte[]{(byte) B58IdentiferCodecs.VER_FAMILY_SEED};
    public static byte[] VER_ED25519 = new byte[]{(byte) 0x1, (byte) 0xe1, (byte) 0x4b};
    byte[] seedBytes;
    byte[] version;

    public Seed(byte[] seedBytes) {
        this(VER_K256, seedBytes);
    }

    public Seed(byte[] version, byte[] seedBytes) {
        this.seedBytes = seedBytes;
        this.version = version;
    }

    public Seed() {
    }

    @Override
    public String toString() {
        return Config.getB58().encodeToStringChecked(seedBytes, version);
    }

    public byte[] bytes() {
        return seedBytes;
    }

    public byte[] version() {
        return version;
    }

    public void setEd25519() {
        this.version = VER_ED25519;
    }

    public IKeyPair keyPair() {
        return keyPair(0);
    }

    public IKeyPair rootKeyPair() {
        return keyPair(-1);
    }

    public IKeyPair keyPair(int account) {
        if (Arrays.equals(version, VER_ED25519)) {
            if (account != 0) {
                throw new AssertionError();
            }
            return EDKeyPair.from128Seed(seedBytes);
        } else {
            return createKeyPair(seedBytes, account);
        }
    }

    public static Seed fromBase58(String b58) {
        B58.Decoded decoded = Config.getB58().decodeMulti(b58, 16, VER_K256, VER_ED25519);
        return new Seed(decoded.version, decoded.payload);
    }

    public IKeyPair fromPrivateKey(String privatekey) {
        if (Arrays.equals(this.version, VER_ED25519)) {
            return EDKeyPair.from256Seed(Numeric.hexStringToByteArray(privatekey));
        } else {
            BigInteger priv = uBigInt(Numeric.hexStringToByteArray(privatekey));
            BigInteger pub = K256KeyPair.computePublicKey(priv);
            return new K256KeyPair(priv, pub);
        }
    }

    public static Seed fromPassPhrase(String passPhrase) {
        return new Seed(passPhraseToSeedBytes(passPhrase));
    }

    public static byte[] passPhraseToSeedBytes(String phrase) {
        try {
            return new Sha512(phrase.getBytes("utf-8")).finish128();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static IKeyPair createKeyPair(byte[] seedBytes) {
        return createKeyPair(seedBytes, 0);
    }

    public static IKeyPair createKeyPair(byte[] seedBytes, int accountNumber) {// accountNumber=0
        BigInteger secret, pub, privateGen;
        // The private generator (aka root private key, master private key)
        privateGen = K256KeyPair.computePrivateGen(seedBytes);// li-ok
        byte[] publicGenBytes = K256KeyPair.computePublicGenerator(privateGen);// li-ok
        if (accountNumber == -1) {
            // The root keyPair
            return new K256KeyPair(privateGen, Utils.uBigInt(publicGenBytes));
        } else {
            secret = K256KeyPair.computeSecretKey(privateGen, publicGenBytes, accountNumber);// li-ok
            pub = K256KeyPair.computePublicKey(secret);
            return new K256KeyPair(secret, pub);
        }
    }

    public static IKeyPair getKeyPair(byte[] seedBytes) {
        return createKeyPair(seedBytes, 0);
    }

    public static IKeyPair getKeyPair(String b58) {
        return getKeyPair(getB58IdentiferCodecs().decodeFamilySeed(b58));
    }

    public String random() {
        byte[] randBytes = new byte[16];
        Random random = SecureRandomUtils.secureRandom();
        random.nextBytes(randBytes);
        return encodeSeed(randBytes);
    }

    public String encodeSeed(byte[] seedBytes) {
        if (seedBytes.length != 16) {
            throw new RuntimeException("the length of seed must be 16.");
        }
        byte[] bytes;
        if (Arrays.equals(this.version, VER_ED25519)) {
            bytes = new byte[seedBytes.length + 3];
            System.arraycopy(Config.ED25519_SEED_PREFIX, 0, bytes, 0, 3);
            System.arraycopy(seedBytes, 0, bytes, 3, seedBytes.length);
        } else {
            bytes = new byte[seedBytes.length + 1];
            bytes[0] = (byte) Config.SEED_PREFIX;
            System.arraycopy(seedBytes, 0, bytes, 1, seedBytes.length);
        }
        try {
            byte[] sha256 = MessageDigest.getInstance("SHA-256").digest(bytes);
            sha256 = MessageDigest.getInstance("SHA-256").digest(sha256);
            byte[] checksum = Arrays.copyOf(sha256, 4);
            byte[] ret = new byte[bytes.length + checksum.length];
            System.arraycopy(bytes, 0, ret, 0, bytes.length);
            System.arraycopy(checksum, 0, ret, bytes.length, checksum.length);
            return Config.getB58().encodeToString(ret);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
