package com.chinatour.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 此类为自选项
 * @author whoisme
 *
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class OptionalExcurition  extends BaseUuidEntity{

    @JsonProperty
    private String name;    //自选项名称
    
    @JsonProperty
    private BigDecimal price;    //价格
    
    @JsonProperty 
    private String remark;    //备注
    
    @JsonProperty
    private String type;    //自选项类型
    
    @JsonProperty
    private String cityId;    //城市Id(备用)
    
    @JsonProperty
    private String destinationName;    //城市名字（备用）
    
}







