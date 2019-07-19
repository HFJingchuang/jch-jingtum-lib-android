package com.android.jtblk.core.types.shamap;

import com.android.jtblk.core.coretypes.hash.prefixes.Prefix;
import com.android.jtblk.core.serialized.BytesSink;

abstract public class ShaMapItem<T> {
    abstract void toBytesSink(BytesSink sink);
    public abstract ShaMapItem<T> copy();
    public abstract T value();
    public abstract Prefix hashPrefix();
}
