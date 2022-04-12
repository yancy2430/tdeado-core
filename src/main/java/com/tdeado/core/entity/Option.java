package com.tdeado.core.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 操作实体
 */
@Data
@ToString
@Accessors(chain = true)
public  class Option {
    private Integer value;
    private String label;
}
