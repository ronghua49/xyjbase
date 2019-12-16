import axios from '../axios'
import config from "@/utils/global.js"
/**
 *宋建国：2019/12/6 17:04
 *描述：朋友关系查询
 *版本：1.0
 */
export const RelationshipQuery = (data) => {
  return axios({
    hostUrl: config.xyjim,
    url: '/imRelationship/query',
    method: 'post',
    data: data
  })
}
/**
 *宋建国：2019/12/6 22:48
 *描述：修改好友，单方加好友关系
 *版本：1.0
 */
export const RelationshipSave = (data) => {
  return axios({
    hostUrl: config.xyjim,
    url: '/imRelationship/save',
    method: 'post',
    data: data
  })
}
/**
 *宋建国：2019/12/7 9:08
 *描述：建立两人好友关系
 *版本：1.0
 */
export const createFriend = (data) => {
  return axios({
    hostUrl: config.xyjim,
    url: '/imRelationship/createFriend',
    method: 'post',
    data: data
  })
}

/**
 *宋建国：2019/12/7 9:08
 *描述：查询聊天历史数据
 *版本：1.0
 */
export const HistoryQuery = (data) => {
  return axios({
    hostUrl: config.xyjim,
    url: '/imChatHistory/query',
    method: 'post',
    data: data
  })
}

/**
 *张俊娜：2019/12/9
 *描述：获取好友列表
 *版本：1.0
 */
export const FindFriend = (data) => {
  return axios({
    hostUrl: config.xyjim,
    url: '/imRelationship/findFriend',
    method: 'get',
  })
}

/**
 *张俊娜：2019/12/10
 *描述：发送聊天消息
 *版本：1.0
 */
export const sendMessage = (data) => {
  return axios({
    hostUrl: config.xyjim,
    url: '/imChatHistory/chatMessage',
    method: 'post',
    data: data
  })
}

/**
 *张俊娜：2019/12/12
 *描述：添加好友
 *版本：1.0
 */
export const addFriend = (data) => {
  return axios({
    hostUrl: config.xyjim,
    url: '/imRelationship/addFriend',
    method: 'post',
    data: data
  })
}

/**
 *张俊娜：2019/12/10
 *描述：查询好友
 *版本：1.0
 */
export const userQuery = (data) => {
  return axios({
    hostUrl: config.xyjim,
    url: '/imUsertable/query',
    method: 'post',
    data: data
  })
}

/**
 *张俊娜：2019/12/11
 *描述：修改用户信息
 *版本：1.0
 */
export const userSave = (data) => {
  return axios({
    hostUrl: config.xyjim,
    url: '/imUsertable/save',
    method: 'post',
    data: data
  })
}
