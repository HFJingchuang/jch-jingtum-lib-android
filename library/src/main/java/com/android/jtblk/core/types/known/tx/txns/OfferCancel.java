package com.android.jtblk.core.types.known.tx.txns;

import com.android.jtblk.core.coretypes.uint.UInt32;
import com.android.jtblk.core.fields.Field;
import com.android.jtblk.core.serialized.enums.TransactionType;
import com.android.jtblk.core.types.known.tx.Transaction;

public class OfferCancel extends Transaction {
    public OfferCancel() {
        super(TransactionType.OfferCancel);
    }
    
    public UInt32 offerSequence() {return get(UInt32.OfferSequence);}
    public void offerSequence(UInt32 val) {put(Field.OfferSequence, val);}
}
