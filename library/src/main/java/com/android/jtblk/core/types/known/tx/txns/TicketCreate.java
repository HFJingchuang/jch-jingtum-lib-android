package com.android.jtblk.core.types.known.tx.txns;

import com.android.jtblk.core.serialized.enums.TransactionType;
import com.android.jtblk.core.types.known.tx.Transaction;

public class TicketCreate extends Transaction {
    public TicketCreate() {
        super(TransactionType.TicketCreate);
    }
}
