package com.android.jtblk.BIP39;

import com.android.jtblk.utils.SecureRandomUtils;

import java.util.Random;

public class RandomSeed {
    public static byte[] random(WordCount words) {
        return random(words, SecureRandomUtils.secureRandom());
    }

    public static byte[] random(WordCount words, Random random) {
        byte[] randomSeed = new byte[words.byteLength()];
        random.nextBytes(randomSeed);
        return randomSeed;
    }
}
