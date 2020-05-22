package com.android.jtblk.crypto.ecdsa;

import java.math.BigInteger;

public interface IKeyPair {
	String canonicalPubHex();
	
	byte[] canonicalPubBytes();

	String canonicalPriHex();
	
	BigInteger pub();
	
	BigInteger priv();
	
	String privHex();
	
	boolean verifySignature(byte[] message, byte[] sigBytes);
	
	byte[] signMessage(byte[] message);
	
	byte[] pub160Hash();
}
