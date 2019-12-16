import tu from '../utils/toolutil';
import {websocket} from "../utils/global";
import topic from "@/mqtt/topics";
var mqtt = require('../mqtt/paho-mqtt');

/**
 *宋建国：2019/11/23 14:30
 *描述：初始化MQTT连接
 * host: 网址路径
 * port：端口号
 * path: 访问路径
 * clientId: 客户端ID
 * eventRoute：接收消息分发自定义事件
 *版本：1.0
 */
export function createMqtt(host, port){
  /**
   *宋建国：2019/11/29 10:35
   *描述：错误类型
   *版本：1.0
   */
  const XYJERROR = {
    FROM_USERID_NOT: {code: 0, text: "发送方ID不能为空"},
    TO_USERID_NOT: {code: 1, text: "接收方ID不能为空"},
    MESSAGESTRING_NOT: {code: 2, text: "消息不能为空"},
    TOPIC_NOT: {code: 3, text: "消息主题不能为空"},
    INVALID_MESSAGE: {code: 3, text: "无效的消息体"},
    TO_USER_NICK_NAME:{code: 4, text: "接收消息用户昵称不能为空"},
    MESSAGE_TYPE: {code: 5, text: "消息类型不能为空"}
  };
  /**
   *宋建国：2019/11/30 10:58
   *描述：已订阅消息
   *版本：1.0
   */
  this.alreadyTopic = {};
  /**
   *宋建国：2019/11/29 10:20
   *描述：聊天消息结构
   *版本：1.0
   */
  this.xyjPayload = function (newPayload) {
    let payload;
    if (typeof newPayload === "string" ||
      newPayload instanceof ArrayBuffer ||
      (ArrayBuffer.isView(newPayload) && !(newPayload instanceof DataView))
    ) {
      payload = newPayload;
    } else if (typeof newPayload === "object"){
      //消息来自与那个用户ID
      newPayload['fromUserId'] = xyjthis.getClientId();
      //消息发送到那个用户ID
      if (!newPayload.hasOwnProperty('toUserId')){
        throw XYJERROR.TO_USERID_NOT;
      }
      //消息类型
      if(!newPayload.hasOwnProperty('MessageType')){
        throw XYJERROR.MESSAGE_TYPE;
      }
      //消息发送消息内容
      if (!newPayload.hasOwnProperty('messageString')){
        throw XYJERROR.MESSAGESTRING_NOT;
      }
      //判断消息主题
      if(!newPayload.hasOwnProperty('topic')){
        throw XYJERROR.TOPIC_NOT;
      }
      payload = newPayload;
    }else{
      throw XYJERROR.INVALID_MESSAGE;
    }

    Object.defineProperties(this,{
      /**
       *宋建国：2019/11/29 12:05
       *描述：获取发送消息内容
       * 支持直接发送字符串
       * 支持发送讯宜捷自定义结构体
       *版本：1.0
       */
      "payloadString":{
        enumerable : true,
        get : function () {
          if (typeof payload === "string")
            return payload;
          else if(typeof payload === "object"){
            return JSON.stringify(payload);
          }
        }
      },
      /**
       *宋建国：2019/11/29 12:04
       *描述：获取发送主题类型
       *版本：1.0
       */
      "topic":{
        enumerable: true,
        get: function () {
          return payload.topic;
        }
      },
      /**
       *宋建国：2019/11/29 12:04
       *描述：获取发送目标用户ID
       *版本：1.0
       */
      "toUserId":{
        enumerable: true,
        get: function () {
          return payload.toUserId;
        }
      },
      /**
       *宋建国：2019/11/29 11:48
       *描述：组合后的精准主题
       *版本：1.0
       */
      "onlyTopic":{
        enumerable: true,
        get: function () {
          //返回消息主题
          return xyjthis.getTopic(payload.topic,payload.toUserId);
        }
      }
    });
  };

  if (tu.isBlank(host)){
    console.info('请传入消息服务器IP地址');
    return;
  }
  this._host = host;

  if (tu.isBlank(port)){
    console.info('请传入消息服务器端口号');
    return;
  }
  this._port = Number(port);
  //聊天消息类型
  this._XIM = '$XIM';
  this.getXIM = function () {
    return this._XIM;
  }
  /**
   *宋建国：2019/11/24 0:16
   *描述：接收消息路由分配到不同的页面接收函数
   *版本：1.0
   */
  this._RouteEvent = {
    /**
     *宋建国：2019/11/23 14:35
     *描述：聊天消息
     *版本：1.0
     */
    $XIMMessageArrived: function (payloadMessage) {

    },

    $XIMMessageDelivered: function (message) {

    }
  }
  /**
   *宋建国：2019/11/26 10:11
   *描述：当前激活页面路由,为了解决调用当前激活页面接收MQTT消息事件
   *版本：1.0
   */
  this._currRoute = '';
  this.getCurrRoute = function () {
    return this._currRoute;
  }
  this.setCurrRoute = function (route) {
    this._currRoute = route;
  }
  this._path = websocket.Path;
  
  this._username = '';
  this.getUserName = function(){
  	return this._username;
  }
  let tmpClientId = sessionStorage.getItem('user');
  this._clientId = (tmpClientId === null) ? 'illegal' : tmpClientId;
  this._tmpclient = {};
  this.getHost = function(){
    return this._host;
  }
  this.getPort = function () {
    return this._port;
  }
  this.getPath = function () {
    return this._path;
  }
  this.getClientId = function () {
    return this._clientId;
  }
  this.getClient = function () {
    return this._client;
  }
  /**
   *宋建国：2019/11/21 15:41
   *描述：连接断开时调用。connect（）方法成功后。建立连接断开时使用的回叫。由于客户端启动了断开连接，
   * 或者由于服务器或网络导致客户端断开连接，因此可能会丢失连接。例如，如果客户端无法连接，
   * 则可以在不调用connectionComplete回调的情况下调用断开回调。
   * 单个响应对象参数传递到包含以下字段的onConnectionLost
   *版本：1.0
   */

  this._tmpclient.onConnectionLost = function(responseObject) {
//  if (!xyjthis.getClient().isConnected()){
//    setInterval(xyjthis.xyjConnect(),1000);
//  }
  }
  /**
   *宋建国：2019/11/21 15:42
   *描述：传递邮件时调用。该客户将要进行的所有处理均已完成。因此，例如，在此客户端发送Qos = 2消息的情况下，
   * 已从服务器接收PubComp流，并且已在调用此回调之前从持久性存储中删除了该消息。传递给onMessageDelivered回调的参数为：
   *版本：1.0
   */
  this._tmpclient.onMessageDelivered = function(message){
    /**
     *宋建国：2019/11/26 11:10
     *描述：获取消息类型
     *版本：1.0
     */
    try {
      let topicType = message.topic.substring(0,4);
      if(topicType === xyjthis.getXIM()){ //聊天消息
        xyjthis._RouteEvent.$XIMMessageDelivered(message);
      }else{ //页面推送消息
        let route = xyjthis.getCurrRoute();
        /**
         *宋建国：2019/11/26 11:03
         *描述：页面推送消息
         * 注意：已经订阅的消息服务端都会发送过来，如果订阅的消息页面不是当前激活窗口，则此消息在此处将丢弃，
         * 页面中的图形数据适合此方法
         *版本：1.0
         */
        if(xyjthis._RouteEvent.hasOwnProperty(route)) {
          let reData = JSON.parse(message.payloadString);
          let funName = '';
          if (xyjthis.isBroadcast(reData.topic)){
            funName = '@'+ reData.topic;
          }else{
            funName = '@'+ reData.topic + reData.fromUserId;
          }
          if (xyjthis._RouteEvent[route].hasOwnProperty(funName)) {
            xyjthis._RouteEvent[route][funName](message);
          } else {
            let tmpmessage = JSON.stringify(message);
            console.info('抛弃消息' + tmpmessage);
          }
        }else{
          let tmpmessage = JSON.stringify(message);
          console.info('抛弃消息' + tmpmessage);
        }
      }
    }catch (err) {
      console.error("this._client.onMessageArrived" + err);
    }
  }
  /**
   *宋建国：2019/11/21 15:42
   *描述：当消息到达此Paho.MQTT.client中时调用。传递给onMessageArrived回调的参数为：
   *版本：1.0
   */
  this._tmpclient.onMessageArrived = function(payloadMessage){
    /**
     *宋建国：2019/11/26 11:10
     *描述：获取消息类型
     *版本：1.0
     */
    try {
      let topicType = payloadMessage.topic.substring(0,4);
      if(topicType === xyjthis.getXIM()){ //聊天消息
        xyjthis._RouteEvent.$XIMMessageArrived(payloadMessage);
      }else{ //页面推送消息
        let route = xyjthis.getCurrRoute();
        /**
         *宋建国：2019/11/26 11:03
         *描述：页面推送消息
         * 注意：已经订阅的消息服务端都会发送过来，如果订阅的消息页面不是当前激活窗口，则此消息在此处将丢弃，
         * 页面中的图形数据适合此方法
         *版本：1.0
         */
        if(xyjthis._RouteEvent.hasOwnProperty(route)) {
          let funName = '$'+ payloadMessage.topic;
          if (xyjthis._RouteEvent[route].hasOwnProperty(funName)) {
            xyjthis._RouteEvent[route][funName](payloadMessage);
          } else {
            let tmpmessage = JSON.stringify(payloadMessage);
            console.info('抛弃消息' + tmpmessage);
          }
        }else{
          let tmpmessage = JSON.stringify(payloadMessage);
          console.info('抛弃消息' + tmpmessage);
        }
      }
    }catch (err) {
      console.error("this._client.onMessageArrived" + err);
    }
  }
  /**
   *宋建国：2019/11/21 15:42
   *描述：与服务器成功建立连接时调用。在connect（）方法之后。传递给onConnected回调的参数为：
   *版本：1.0
   */
  this._tmpclient.onConnected = function(reconnect, uri){
    for(let topic in xyjthis.alreadyTopic) {
      xyjthis.getClient().subscribe(topic,xyjthis.alreadyTopic[topic]);
    }
  }
    /**
     *宋建国：2019/11/21 15:42
     *描述：设置分发路由事件
     *版本：1.0
     */
  this.setXyjRoute = function(route,topic,xyjEvent){
    if(!xyjEvent.hasOwnProperty('messageArrived')){
      console.error(route+'页面请设置消息接收事件messageArrived');
      return false;
    }
    if(!xyjEvent.hasOwnProperty('messageDelivered')){
      console.error(route+'页面请设置消息接收事件messageDelivered');
      return false;
    }
    if(!this._RouteEvent.hasOwnProperty(route)){
      this._RouteEvent[route]={};
    }
    this._RouteEvent[route]['$'+topic] = xyjEvent.messageArrived;
    this._RouteEvent[route]['@'+topic] = xyjEvent.messageDelivered;
    return true;
  }
  /**
   *宋建国：2019/11/26 13:48
   *描述：删除页面接收消息事件
   *版本：1.0
   */
  this.delXyjRoute = function (route,topic) {
    let funName = '$[' + route + ']' + topic;
    if(this._RouteEvent.hasOwnProperty(route)){
      delete this._RouteEvent[funName];
    }
    funName = '@[' + route + ']' + topic;
    if(this._RouteEvent.hasOwnProperty(funName)){
      delete this._RouteEvent[funName];
    }
  }
  /**
   *宋建国：2019/11/26 11:42
   *描述：设置聊天接收消息事件
   *版本：1.0
   */
  this.setImXyjRoute = function (imEvent) {
    if(!imEvent.hasOwnProperty('messageArrived')){
      console.error('请设置聊天接收消息事件messageArrived');
      return false;
    }
    if(!imEvent.hasOwnProperty('messageDelivered')){
      console.error('请设置聊天接收消息事件messageDelivered');
      return false;
    }
    this._RouteEvent.$XIMMessageArrived = imEvent.messageArrived;
    this._RouteEvent.$XIMMessageDelivered = imEvent.messageDelivered;
    return true;
  }

  /**
   *宋建国：2019/11/23 16:40
   *描述：连接成功
   *版本：1.0
   */

  this._onConnect = function(context){

  }

  this.setOnConnect= function(fonConnect){
    this._onConnect = fonConnect;
  }

  /**
   *宋建国：2019/11/23 16:40
   *描述：连接失败
   *版本：1.0
   */
  this._onFail = function(context){
    console.info(context.errorMessage);
  }
  this.setOnFail = function(fOnFail){
    this._onFail = fOnFail;
  }
  /**
   *宋建国：2019/11/23 23:57
   *描述：mqtt连接函数
   *版本：1.0
   */
  this.xyjConnect = function(){
    this._username = sessionStorage.getItem('user');
    this._clientId = sessionStorage.getItem('chatid');
    let clientid = 'web' + tu.createRandom();
    this._client = new mqtt.Paho.Client(host, Number(port), this._path, clientid);
    this._client.onConnectionLost = this._tmpclient.onConnectionLost;
    this._client.onMessageDelivered = this._tmpclient.onMessageDelivered;
    this._client.onMessageArrived = this._tmpclient.onMessageArrived;
    this._client.onConnected = this._tmpclient.onConnected;
    if(!websocket.enable){
      console.log('已关闭消息服务连接');
      return;
    }//关闭消息服务连接
    let connectOptions = {
      invocationContext: {
        host: this._host,//IP地址
        port: this._port,//端口号
        path: this._path,
        clientId: clientid//客户端编号
      },
      timeout: websocket.ConnectTimeOut,//连接超时时间
      keepAliveInterval: websocket.keepAliveInterval,//心跳间隔
      cleanSession: false,//false客户端掉线后 服务器端不会清除session，当重连后可以接收之前订阅主题的消息。当客户端上线后会接受到它离线的这段时间的消息
      useSSL: false,//是否启用SSL
      userName: this._username,  //用户名
      password: 'public',  //密码
      onSuccess: this._onConnect,//连接成功回调事件
      onFailure: this._onFail,//连接失败回调事件
     // willMessage:{},
     // hosts:{},
     // ports:{},
      reconnect:true, //true：断网重连
     // mqttVersion:4,
    //  mqttVersionExplicit:false,
     // uris: {}
    }
    this._client.connect(connectOptions);
  }
  //关闭连接
  this.disconnect = function(){
  	this.alreadyTopic = {};
  	this._client.disconnect();
  }
  //获取连接状态
  this.isConnected = function(){
  	this._client.connected;
  }
  /**
   *宋建国：2019/11/23 23:59
   *描述：订阅页面推送数据
   *版本：1.0
   */
  this.subscribe = function (route,filter,xyjEvent,subscribeOptions) {
    if(!websocket.enable){
      console.info('已关闭消息服务连接');
      return;
    }//关闭消息服务连接
    if(tu.isBlank(filter)){
      console.error('subscribe方法，请传入主题');
      return;
    }
    let tmpfilter = xyjthis.getTopic(filter);
    /**
     *宋建国：2019/11/25 14:49
     *描述：绑定外部事件
     *版本：1.0
     */
    if (!this.setXyjRoute(route,tmpfilter,xyjEvent)){
      return;
    }
    /**
     *宋建国：2019/11/26 10:44
     *描述：修改当前激活页面路由变量
     *版本：1.0
     */
    this.setCurrRoute(route);

    let scribeOptions = {
      qos: websocket.subscribeqos,
      invocationContext: null,
      onSuccess: function (Context) {

      },
      onFailure: function (Context) {
        console.error('订阅失败：'+Context.errorMessage);
      },
      timeout: websocket.subscribetimeout
    };
    if(!tu.isObjectBlank(subscribeOptions)){
      Object.assign(scribeOptions,subscribeOptions);
    }
    this._client.subscribe(tmpfilter,scribeOptions);
    //存储已经订阅的消息
    this.alreadyTopic[tmpfilter] = scribeOptions;
  }
  /**
   *宋建国：2019/11/29 12:37
   *描述：是否是广播消息
   * 返回true:代表是广播消息
   * false: 点对点消息
   *版本：1.0
   */
  this.isBroadcast = function(filter){
    if (filter === topic.$IMBROADCAST||
      filter === topic.$PAGEBROADCAST){
      return true
    }else{
      return false
    }
  }
  /**
   *宋建国：2019/11/29 12:44
   *描述：组合主题
   *版本：1.0
   */
  this.getTopic = function (filter,clientid) {
    let tmpTopic = '';
    if (xyjthis.isBroadcast(filter)){
      //如果是广播消息，则不拼装用户ID
      tmpTopic = filter;
    }else{
      //如果是点对点发送消息需要拼装用户ID
      if (tu.isBlank(clientid)){
        tmpTopic = filter + xyjthis.getClientId();
      }else{
        tmpTopic = filter + clientid;
      }
    }
    return tmpTopic;
  }
  /**
   *宋建国：2019/11/23 23:59
   *描述：订阅页面推送数据
   *版本：1.0
   */
  this.ImSubscribe = function (filter,xyjEvent,subscribeOptions) {
    if(!websocket.enable){
      console.log('已关闭消息服务连接');
      return;
    }//关闭消息服务连接
    if(tu.isBlank(filter)){
      console.log('ImSubscribe方法，请传入要订阅的主题');
      return;
    }
    let tmpfilter = xyjthis.getTopic(filter);
    /**
     *宋建国：2019/11/25 14:49
     *描述：绑定外部事件
     *版本：1.0
     */
    if(!this.setImXyjRoute(xyjEvent)){
      return;
    }

    let scribeOptions = {
      qos: websocket.subscribeqos,
      invocationContext: null,
      onSuccess: function (Context) {

      },
      onFailure: function (Context) {
        console.error('订阅失败：'+Context.errorMessage);
      },
      timeout: websocket.subscribetimeout
    };
    if(!tu.isObjectBlank(subscribeOptions)){
      Object.assign(scribeOptions,subscribeOptions);
    }
    this._client.subscribe(tmpfilter,scribeOptions);
    //存储已经订阅的消息
    this.alreadyTopic[tmpfilter] = scribeOptions;
  }
  /**
   *宋建国：2019/11/26 13:35
   *描述：取消订阅
   *版本：1.0
   */
  this.unsubscribe = function(filter,subscribeOptions){
    let scribeOptions = {
      //qos: websocket.subscribeqos,
      invocationContext: null,
      onSuccess: function (Context) {
        console.info('取消订阅成功');
      },
      onFailure: function (Context) {
        console.error('取消订阅失败：'+Context.errorMessage);
      },
      timeout: websocket.subscribetimeout
    };
    if(!tu.isObjectBlank(subscribeOptions)){
      Object.assign(scribeOptions,subscribeOptions);
    };
    //处理多个页面订阅同一个主题
    if(this.getSubscribeNum(filter)==1){
      this._client.unsubscribe(filter,scribeOptions);
      delete this.alreadyTopic[filter];
    };
  }
  /**
   *宋建国：2019/11/26 14:36
   *描述：清理页面，取消页面的订阅，删除页面的接收消息事件
   *版本：1.0
   */
  this.clearSubScribe = function (route) {
    if(!this._RouteEvent.hasOwnProperty(route)){
      return;
    }
    for(let topicName in this._RouteEvent[route]){
      let leftstr = topicName.substring(0,1);
      if(leftstr === '$'){
        let topic = topicName.substring(1,topicName.length);
        this.unsubscribe(topic);
      }
    }
    delete this._RouteEvent[route];
  }
  /**
   *宋建国：2019/11/26 14:46
   *描述：获取订阅测试
   *版本：1.0
   */
  this.getSubscribeNum = function(topic){
    let num = 0;
    for(let route in this._RouteEvent){
      let topicName = '$' + topic;
      let tmpobject = this._RouteEvent[route];
      if(tmpobject.hasOwnProperty(topicName)){
        num = num+1;
      }
    }
    return num
  }
  /**
   *宋建国：2019/11/24 1:19
   *描述：向订阅者发布消息
   *  保留如果为true，则服务器将保留并传递消息对于当前和将来的订阅。
   *  如果为false，则服务器只将消息传递给当前订阅服务器，这是新消息的默认值。
   *  如果消息已发布，则接收的消息的保留布尔值设置为true
   *  保留的布尔值设置为true
   *  子选项是在消息发布后生成的
   *版本：1.0
   */
  this.send = function(payload){
    if(!websocket.enable){
      console.info('已关闭消息服务连接');
      return;
    }
    let tmptopic = payload.onlyTopic;
    /**
     *宋建国：2019/11/30 10:41
     *描述：
     * tmptopic: 主题
     * payload.payloadString： 发送消息JSON字符串
     * websocket.sendqos：消息质量，0，1，2
     * websocket.sendretained： true：mqtt服务器会保留每次发布的消息，若订阅主题的客户端重启，则会把此主题之前发布的消息重新推送到客户端
     *                          false: mqtt服务器不保留每次发布的消息
     *
     *版本：1.0
     */
    this._client.send(tmptopic,payload.payloadString,websocket.sendqos,websocket.sendretained);
  }
  let xyjthis = this;
}

export default{
  createMqtt
}
