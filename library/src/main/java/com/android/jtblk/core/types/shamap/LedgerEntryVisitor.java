package com.android.jtblk.core.types.shamap;

import com.android.jtblk.core.types.known.sle.LedgerEntry;

public interface LedgerEntryVisitor {
    public void onEntry(LedgerEntry entry);
}
