package com.android.jtblk.core.types.shamap;
import com.android.jtblk.core.types.known.tx.result.TransactionResult;

public interface TransactionResultVisitor {
    public void onTransaction(TransactionResult tx);
}
