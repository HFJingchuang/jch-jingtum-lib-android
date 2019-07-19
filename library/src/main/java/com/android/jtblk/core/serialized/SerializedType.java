package com.android.jtblk.core.serialized;

import com.android.jtblk.core.fields.Type;

public interface SerializedType {
    Object toJSON();
    byte[] toBytes();
    String toHex();
    void toBytesSink(BytesSink to);
    Type type();
}
