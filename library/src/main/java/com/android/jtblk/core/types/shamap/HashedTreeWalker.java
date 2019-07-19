package com.android.jtblk.core.types.shamap;

import com.android.jtblk.core.coretypes.hash.Hash256;

public interface HashedTreeWalker {
    public void onLeaf(Hash256 h, ShaMapLeaf le);
    public void onInner(Hash256 h, ShaMapInner inner);
}
