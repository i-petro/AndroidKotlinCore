package com.androidkotlincore.entityconverter;

import android.support.annotation.NonNull;

public interface IConvertersContextVisitor {
    void visit(@NonNull IConvertersContext convertersContext);
}
