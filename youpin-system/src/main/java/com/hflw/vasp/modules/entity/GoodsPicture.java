package com.hflw.vasp.modules.entity;


import com.hflw.vasp.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 商品图片表
 *
 * @author liuyf
 * @date 2019-04-02 10:04:45
 */
@Accessors(chain = true)
@Data
@Entity
@Table(name = "d_goods_picture")
@ApiModel("商品图片表")
public class GoodsPicture extends BaseEntity {

    /**
     * 商品id
     */
    private Long goodsId;
    /**
     * 图片url
     */
    private String picUrl;
    /**
     * 顺序号
     */
    private Integer picSeq;
    /**
     * 是否主图
     */
    private Integer isMain;

}
