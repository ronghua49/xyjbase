package com.xyjsoft.core.mqtt;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel(description="消息类型")
public class MessageType {
	/**
	 * @describe 文本
	 * @author sjg 
	 * @date 2019年12月6日 下午8:49:25
	 */
	@ApiModelProperty(value="文本")
	public static String ChatText = "0";
	/**
	 * @describe 图片
	 * @author sjg 
	 * @date 2019年12月6日 下午8:49:32
	 */
	@ApiModelProperty(value="图片")
	public static String ChatImage = "1";
	/**
	 * @describe 文件
	 * @author sjg 
	 * @date 2019年12月6日 下午8:49:34
	 */
	@ApiModelProperty(value="文件")
	public static String ChatFile = "2";
	/**
	 * @describe 语音
	 * @author sjg 
	 * @date 2019年12月6日 下午8:49:35
	 */
	@ApiModelProperty(value="确认消息提示")
	public static String ChatVoice = "3";
	/**
	 * @describe 小视频
	 * @author sjg 
	 * @date 2019年12月6日 下午8:49:37
	 */
	@ApiModelProperty(value="小视频")
	public static String ChatVideo = "4";
	/**
	 * @describe 好友确认消息
	 * @author sjg 
	 * @date 2019年12月6日 下午8:49:38
	 */
	@ApiModelProperty(value="好友确认消息")
	public static String Addfriends = "20";
	/**
	 * @describe 申请加好友
	 * @author sjg 
	 * @date 2019年12月6日 下午8:49:41
	 */
	@ApiModelProperty(value="申请加好友")
	public static String ApplyFriend = "21";
	/**
	 * @describe 创建群消息
	 * @author sjg 
	 * @date 2019年12月6日 下午8:49:44
	 */
	@ApiModelProperty(value="创建群消息")
	public static String CreateGroup = "22";
	/**
	 * @describe 退出群消息
	 * @author sjg 
	 * @date 2019年12月6日 下午8:49:58
	 */
	@ApiModelProperty(value="退出群消息")
	public static String QuitGroup = "23";
	/**
	 * @describe 进群消息
	 * @author sjg 
	 * @date 2019年12月6日 下午8:50:15
	 */
	@ApiModelProperty(value="进群消息")
	public static String InGroup = "24"; //
}
