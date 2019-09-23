package com.cl.mybatisplusdemo.model;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User implements Serializable  {
    private static final long serialVersionUID = 1L;
    @TableId
    private int id;
    private String name;
    private String mobile;

}
