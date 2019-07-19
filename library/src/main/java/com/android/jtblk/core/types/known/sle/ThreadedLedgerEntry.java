package com.android.jtblk.core.types.known.sle;


import com.android.jtblk.core.coretypes.hash.Hash256;
import com.android.jtblk.core.coretypes.uint.UInt32;
import com.android.jtblk.core.fields.Field;
import com.android.jtblk.core.serialized.enums.LedgerEntryType;

// this class has a PreviousTxnID and PreviousTxnLgrSeq
abstract public class ThreadedLedgerEntry extends LedgerEntry {
    public ThreadedLedgerEntry(LedgerEntryType type) {
        super(type);
    }
    public UInt32 previousTxnLgrSeq() {return get(UInt32.PreviousTxnLgrSeq);}
    public Hash256 previousTxnID() {return get(Hash256.PreviousTxnID);}
    public void previousTxnLgrSeq(UInt32 val) {put(Field.PreviousTxnLgrSeq, val);}
    public void previousTxnID(Hash256 val) {put(Field.PreviousTxnID, val);}
}
