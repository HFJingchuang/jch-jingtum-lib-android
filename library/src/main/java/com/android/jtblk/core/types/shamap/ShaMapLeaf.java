package com.android.jtblk.core.types.shamap;

import com.android.jtblk.core.coretypes.hash.Hash256;
import com.android.jtblk.core.coretypes.hash.prefixes.Prefix;
import com.android.jtblk.core.serialized.BytesSink;

public class ShaMapLeaf extends ShaMapNode {
    public Hash256 index;
    public ShaMapItem item;
    public long version = -1;

    protected ShaMapLeaf(Hash256 index, ShaMapItem item) {
        this.index = index;
        this.item = item;
    }

    @Override public boolean isLeaf() {return true;}
    @Override public boolean isInner() {return false;}

    @Override
    Prefix hashPrefix() {
        return item.hashPrefix();
    }

    @Override
    public void toBytesSink(BytesSink sink) {
        item.toBytesSink(sink);
        index.toBytesSink(sink);
    }

    public ShaMapLeaf copy() {
        return new ShaMapLeaf(index, item.copy());
    }
}
