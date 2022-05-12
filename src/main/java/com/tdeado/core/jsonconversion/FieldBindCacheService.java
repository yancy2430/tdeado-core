package com.tdeado.core.jsonconversion;


import com.tdeado.core.annotations.FieldBind;

public interface FieldBindCacheService {
    default Object getValue(FieldBind fieldBind, Object value){
        return null;
    };
}
