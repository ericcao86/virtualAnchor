package com.iflytek.tps.foun.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel("基础数据查询返回体")
public class Metadata {
    @ApiModelProperty("基础数据ID")
    public String id;
    @ApiModelProperty("基础数据编号")
    public String codeNo;
    @ApiModelProperty("基础数据NAME")
    public String codeName;
    @ApiModelProperty("基础数据类型")
    public String category;
    @ApiModelProperty("基础数据类型名")
    public String categoryName;
    @ApiModelProperty("基础数据父ID")
    public String parentNo;
    @ApiModelProperty("基础数据描述")
    public String  description;
    @ApiModelProperty("基础数据状态")
    public String metaState;
    @ApiModelProperty("下级级联基础数据")
    public List<Metadata> subordinates;

    @ApiModelProperty("菜单ICON")
    public String menuIcon;
}
