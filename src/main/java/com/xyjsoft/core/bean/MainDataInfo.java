package com.xyjsoft.core.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.Date;

/**
 * 类名:MainDataInfo
 * 类描述:TODO
 *
 * @author gsh456
 * @version 1.0
 * @date 2019-04-16 11:58
 * @since JDK1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "主表返回数据封装实体")
public class MainDataInfo {
   /** 版本id */
   @ApiModelProperty(value = "版本id")
   private Long versionId;
   /** 车型基本信息id */
   @ApiModelProperty(value = "车型基本信息id")
   private Long carId;
   /** 代办数量/听证次数 */
   @ApiModelProperty(value = "代办数量/听证次数/")
   private Integer dataNum;
   /** 基点车版本自增ID */
   @ApiModelProperty(value = "版本号")
   private String version ;
   /** 工厂名称 */
   @ApiModelProperty(value = "工厂名称")
   private String factoryName ;
   /** (1:花都一厂,2:花都二厂,3:花都三厂) */
   @ApiModelProperty(value = "(1:花都一厂,2:花都二厂,3:花都三厂)")
   private String factoryType ;
   /** 工厂地址 */
   @ApiModelProperty(value = "工厂地址")
   private String factoryAddress;
   /** 冻结日期 */
   @ApiModelProperty(value="冻结日期")
   private Date frozenDate;
   /** 定版状态(0:编辑中,1:冻结,2:解冻中,3:审批中,4:审批完成) */
   @ApiModelProperty(value="定版状态(0:编辑中,1:冻结,2:解冻中,3:审批中,4:审批完成)")
   private Integer confirm;
   /** 状态（0：编辑中，1：审批中、2：完成） */
   @ApiModelProperty(value="状态（0：编辑中，1：审批中、2：完成）")
   private Integer state;
   /** 是否标杆车(0:不是,1:是标杆车) */
   @ApiModelProperty(value="是否标杆车(0:不是,1:是标杆车)")
   private Integer ifTypical;
   /** 平台 */
   @ApiModelProperty(value = "平台")
   private String platform ;
   /** 品牌 */
   @ApiModelProperty(value = "品牌")
   private String brand ;
   /** 阶段 */
   @ApiModelProperty(value = "阶段")
   private String stage ;
   /** 车型 */
   @ApiModelProperty(value = "车型")
   private String cartype ;
   /** 类型 */
   @ApiModelProperty(value = "类型")
   private String ctype ;
   /** SOP时点 */
   @ApiModelProperty(value="SOP时点",required = true,example = "2019-06-16")
   private LocalDate sopdate;
   /** 合同日期 */
   @ApiModelProperty(value="合同日期" ,required = true,example = "2019-06-16")
   private LocalDate contractdate;
   /** 基点车版本自增ID */
   @ApiModelProperty(value="基点车版本自增ID")
   private Long baseCarVersionId;
   /** 基点车型 */
   @ApiModelProperty(value="基点车型")
   private String basecar;
   /** 基点车阶段 */
   @ApiModelProperty(value="基点车阶段")
   private String basestage;
   /** 对比车版本自增ID */
   @ApiModelProperty(value="对比车版本自增ID")
   private Long compareCarVersionId;
   /** 对比车型 */
   @ApiModelProperty(value="对比车型")
   private String compareType;
   /** 对比车型阶段 */
   @ApiModelProperty(value="对比车型阶段")
   private String compareStage;

}
