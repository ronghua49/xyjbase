/**
 *宋建国：2019/11/28 16:48
 *描述：数据交换协议
 *版本：1.0
 */
//IM消息
export const $IMNEWS = "$XIM/NEWS/";
  //系统广播消息
export const $IMBROADCAST = "$XIM/BROADCAST";
  //推送页面消息
export const $PAGENEWS = "$PAG/NEWS/";
  //系统广播页面消息
export const   $PAGEBROADCAST = "$PAG/BROADCAST";
  //消息服务器系统消息
export const $SYS = "$SYS/";
//与微服务直接通信主题
//格式：$WFW/服务名称
export const $WFW = "$WFW/"
//消息发送类型
export const $MESSAGETYPE = {
  ChatText: "0", //文本
  ChatImage: "1",//图片
  ChatFile: "2", //文件
  ChatVoice: "3", //语音
  ChatVideo: "4", //小视频
  Addfriends: "20", //好友确认消息
  ApplyFriend: "21", //申请加好友
  CreateGroup: "22", //创建群消息
  QuitGroup: "23",//退出群消息
  InGroup: "24", //进群消息
}

export default {
  $IMNEWS,
  $IMBROADCAST,
  $PAGENEWS,
  $PAGEBROADCAST,
  $SYS,
  $WFW,
  $MESSAGETYPE
}
