package com.xyjsoft.core.mqtt;

/**
 * @author sjg
 * 2019年12月3日 上午10:30:18  
 * MQTT 交互协议
 */
public class topics {
	/**
	 * @describe IM消息
	 * @author sjg 
	 * @date 2019年12月3日 上午10:31:02
	 */
	public static String $IMNEWS = "$XIM/NEWS/";
	/**
	 * @describe 系统广播消息
	 * @author sjg 
	 * @date 2019年12月3日 上午10:31:12
	 */
	public static String $IMBROADCAST = "$XIM/BROADCAST";
	/**
	 * @describe 推送页面消息
	 * @author sjg 
	 * @date 2019年12月3日 上午10:31:24
	 */
	public static String $PAGENEWS = "$PAG/NEWS/";
	/**
	 * @describe 系统广播页面消息
	 * @author sjg 
	 * @date 2019年12月3日 上午10:31:36
	 */
	public static String $PAGEBROADCAST = "$PAG/BROADCAST";
	/**
	 * @describe 消息服务器系统消息
	 * @author sjg 
	 * @date 2019年12月3日 上午10:31:47
	 */
	public static String $SYS = "$SYS/";
	
	/**
	 * @describe 微服务通信协议
	 * 每个微服务唯一通信主题格式：$WFW/服务名称
	 * @author sjg 
	 * @date 2019年12月3日 上午10:38:43
	 */
	public static String $WFW = "$WFW/";
}
