package com.android.jtblk.BIP39;

public enum WordCount {
    /**
     * 12个助记词
     */
    TWELVE(128),
    /**
     * 15个助记词
     */
    FIFTEEN(160),
    /**
     * 18个助记词
     */
    EIGHTEEN(192),
    /**
     * 21个助记词
     */
    TWENTY_ONE(224),
    /**
     * 24个助记词
     */
    TWENTY_FOUR(256);

    private final int bitLength;

    WordCount(int bitLength) {
        this.bitLength = bitLength;
    }

    public int bitLength() {
        return bitLength;
    }

    public int byteLength() {
        return bitLength / 8;
    }
}
