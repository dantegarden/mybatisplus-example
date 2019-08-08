package com.example.standalone.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("user")
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

    @TableId  //@TableId(type = IdType.AUTO)局部主键策略
    private Long id;
    @TableField("name")
    private String name;
    private String phone;
    private String email;
    private String aboutme;
    private String passwd;
    private String avatar;
    private String type;
    private LocalDateTime createTime;
    private Boolean enable;
    private Integer agencyId;

    @TableField(exist = false)
    private String remark;
}
