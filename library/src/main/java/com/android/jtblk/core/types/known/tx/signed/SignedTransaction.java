package com.android.jtblk.core.types.known.tx.signed;

import java.util.Arrays;

import com.android.jtblk.client.Wallet;
import com.android.jtblk.core.coretypes.Amount;
import com.android.jtblk.core.coretypes.Blob;
import com.android.jtblk.core.coretypes.STObject;
import com.android.jtblk.core.coretypes.hash.HalfSha512;
import com.android.jtblk.core.coretypes.hash.Hash256;
import com.android.jtblk.core.coretypes.hash.prefixes.HashPrefix;
import com.android.jtblk.core.coretypes.uint.UInt32;
import com.android.jtblk.core.serialized.BytesList;
import com.android.jtblk.core.serialized.MultiSink;
import com.android.jtblk.core.serialized.enums.TransactionType;
import com.android.jtblk.core.types.known.tx.Transaction;
import com.android.jtblk.crypto.ecdsa.IKeyPair;
import com.android.jtblk.crypto.ecdsa.Seed;
import com.android.jtblk.exceptions.PrivateKeyFormatException;

import org.web3j.utils.Numeric;

import static com.android.jtblk.utils.Utils.uBigInt;

public class SignedTransaction {
    private SignedTransaction(Transaction of) {
        // TODO: is this just over kill ?
        txn = (Transaction) STObject.translate.fromBytes(of.toBytes());
    }

    // This will eventually be private
    @Deprecated
    public SignedTransaction() {
    }

    public Transaction txn;
    public Hash256 hash;

    public byte[] signingData;
    public byte[] previousSigningData;
    public String tx_blob;

    public void sign(String base58Secret) {
        if (Wallet.isValidSecret(base58Secret)) {
            sign(Seed.fromBase58(base58Secret).keyPair());
        } else {
            Seed seed = new Seed();
            if (base58Secret.length() == 66) {
                if (base58Secret.toUpperCase().startsWith("ED")) {
                    seed.setEd25519();
                }
                base58Secret = base58Secret.substring(2);
            } else {
                throw new PrivateKeyFormatException("the length of private key must be 66");
            }
            sign(seed.fromPrivateKey(base58Secret));
        }

    }

    public static SignedTransaction fromTx(Transaction tx) {
        return new SignedTransaction(tx);
    }

    public void sign(IKeyPair keyPair) {
        prepare(keyPair, null, null, null);
    }

    public void prepare(IKeyPair keyPair,
                        Amount fee,
                        UInt32 Sequence,
                        UInt32 lastLedgerSequence) {

        Blob pubKey = new Blob(keyPair.canonicalPubBytes());

        // This won't always be specified
        if (lastLedgerSequence != null) {
            txn.put(UInt32.LastLedgerSequence, lastLedgerSequence);
        }
        if (Sequence != null) {
            txn.put(UInt32.Sequence, Sequence);
        }
        if (fee != null) {
            txn.put(Amount.Fee, fee);
        }

        txn.signingPubKey(pubKey);


        if (Transaction.CANONICAL_FLAG_DEPLOYED) {
            txn.setCanonicalSignatureFlag();
        }

        txn.checkFormat();
        signingData = txn.signingData();
        //System.out.println("------------");
        //System.out.println(JsonUtils.toJsonString(txn));
        if (previousSigningData != null && Arrays.equals(signingData, previousSigningData)) {
            return;
        }
        try {
            txn.txnSignature(new Blob(keyPair.signMessage(signingData)));

            BytesList blob = new BytesList();
            HalfSha512 id = HalfSha512.prefixed256(HashPrefix.transactionID);
            txn.toBytesSink(new MultiSink(blob, id));
            tx_blob = blob.bytesHex();
            hash = id.finish();
        } catch (Exception e) {
            // electric paranoia
            previousSigningData = null;
            throw new RuntimeException(e);
        } /*else {*/
        previousSigningData = signingData;
        // }
    }

    public TransactionType transactionType() {
        return txn.transactionType();
    }
}
