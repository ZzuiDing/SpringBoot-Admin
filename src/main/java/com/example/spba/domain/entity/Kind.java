package com.example.spba.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 商品种类
 * @TableName kind
 */
@TableName(value ="kind")
@Data
public class Kind {
    /**
     * 
     */
    @TableField(value = "kind")
    private String kind;
    private String Kind_Id;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Kind other = (Kind) that;
        return (this.getKind() == null ? other.getKind() == null : this.getKind().equals(other.getKind()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getKind() == null) ? 0 : getKind().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", kind=").append(kind);
        sb.append("]");
        return sb.toString();
    }
}