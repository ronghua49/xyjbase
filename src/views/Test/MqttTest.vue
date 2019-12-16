<template>
  <div>
    <div>订阅主题: <el-input v-model="scribe" placeholder="订阅主题"></el-input></div>
    <div>接收聊天ID:<el-input v-model="touserid" placeholder="接收聊天ID"></el-input></div>
    <div>接收消息用户昵称<el-input v-model="nickname" placeholder="接收消息用户昵称"></el-input></div>
    <div>发送消息内容:<el-input v-model="messageData" placeholder="发送消息内容"></el-input></div>
    <div>聊天会话ID:<el-input v-model="conversationid" placeholder="聊天会话ID"></el-input></div>
    <div>
      <el-button type="primary" @click="connectMethod">mqtt订阅</el-button>
      <el-button type="primary" @click="sendMethod">发送消息</el-button>
      <el-button type="primary" @click="applyFriend">申请好友</el-button>
      <el-button type="primary" @click="Addfriends">申请好友确认</el-button>
      <el-button type="primary" @click="">删除好友关系</el-button>
      <el-button type="primary" @click="ChatHistory">查询会话ID聊天历史消息</el-button>
      <el-button type="primary" @click="">查询会话列表</el-button>
      <el-button type="primary" @click="">查询好友关系</el-button>
    </div>
    <div style="white-space:pre">
      <div> 消息交换显示
        <el-input
          type="textarea"
          :autosize="{ minRows: 2, maxRows: 100}"
          placeholder="请输入内容"
          v-model="textarea2">
        </el-input>
      </div>
      <div> 显示HTTP请求数据
        <el-input
          type="textarea"
          :autosize="{ minRows: 4, maxRows: 100}"
          placeholder="显示查询数据"
          v-model="httptext">
        </el-input>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
      name: "MqttTest",
      data(){
        return{
          scribe: 'testtopic',
          touserid: '2',
          messageData: '测试发送消息',
          textarea2: '',
          nickname: 'lyic',//接收消息昵称
          receiveData: null, //接收消息
          httptext: '', //显示查询数据
          conversationid: 1 //聊天会话ID
        }
      },
    methods:{
      connectMethod(){

      },
      sendMethod(){
        let sendPlayload = new this.$mqtt.xyjPayload(
          {
            topic: this.$topic.$PAGENEWS,
            toUserId:this.touserid,
            MessageType: this.$topic.$MESSAGETYPE.ChatText,
            toUserNickName: this.nickname,
            messageString: this.messageData
          });
        this.$mqtt.send(sendPlayload);
      },
      /**
       *宋建国：2019/12/6 16:47
       *描述：申请好友
       *版本：1.0
       */
      applyFriend(){
        /**
         *宋建国：2019/12/6 17:14
         *描述：查询此用户是否已经是好友
         *版本：1.0
         */
        this.$api.im.RelationshipQuery({
          querys: [
            {
              "hasInitValue": true,
              "operation": "EQUAL",
              "property": "mainUserId",
              "relation": "AND",
              "value": this.$mqtt.getClientId()
            },
            {
              "hasInitValue": true,
              "operation": "EQUAL",
              "property": "friendUserId",
              "relation": "AND",
              "value": this.touserid
            }
          ]
        }).then((res)=>{
          if(res.data.rows.length>0){
            this.$message.info(this.nickname+"用户已经加为好友");
            return;
          }
          //发送申请好友
          let sendPlayload = new this.$mqtt.xyjPayload(
            {
              topic: this.$topic.$IMNEWS,
              MessageType: this.$topic.$MESSAGETYPE.ApplyFriend,
              toUserId:this.touserid,
              toUserNickName: this.nickname,
              messageString: this.messageData
            });
          this.$mqtt.send(sendPlayload);
        }).catch((error)=>{
          this.$message.error(error.message);
        });
      },
      /**
       *宋建国：2019/12/6 22:45
       *描述：申请好友确认
       *版本：1.0
       */
      Addfriends(){
        if(this.receiveData === null){
          this.$message.info("申请信息不存在");
          return;
        }
        let friedsinfo = JSON.parse(this.receiveData.payloadString);
        if(friedsinfo.MessageType !== this.$topic.$MESSAGETYPE.ApplyFriend){
          this.$message.info("申请好友信息不存在");
          return;
        }
        this.$api.im.createFriend({
          mainUserId: friedsinfo.fromUserId,
          friendUserId: friedsinfo.toUserId,
          usernickname: friedsinfo.toUserNickName,
        }).then((res) =>{
          //
          let sendPlayload = new this.$mqtt.xyjPayload(
            {
              topic: this.$topic.$IMNEWS,
              MessageType: this.$topic.$MESSAGETYPE.Addfriends,
              toUserId: friedsinfo.fromUserId,
              messageString: friedsinfo.toUserNickName + "同意与你好友关系"
            });
          this.$mqtt.send(sendPlayload);
        }).catch((err)=>{
          this.$message.error(error.message);
        })
      },

      /**
       *宋建国：2019/12/7 9:38
       *描述：查询聊天历史数据
       *版本：1.0
       */
      ChatHistory(){
          this.$api.im.HistoryQuery({
            querys: [
              {
                "hasInitValue": true,
                "operation": "EQUAL",
                "property": "conversationid",
                "relation": "AND",
                "value": this.conversationid
              }
            ],
            sorter:[
              {
                "property": "createTime",
                "direction": "DESC"
              }
            ]
          }).then((res)=>{
            debugger
              this.httptext= JSON.stringify(res.data.rows);
          }).catch((error)=>{
            debugger
              this.$message.error(error.message);
          })
      },
      /**
       *宋建国：2019/11/25 14:38
       *描述：推送页面消息 接收消息
       *版本：1.0
       */
      newsMessageArrived: function (payloadMessage) {
        this.textarea2 = this.textarea2 + ' ' + payloadMessage.payloadString;
      },
      /**
       *宋建国：2019/11/25 14:38
       *描述：推送页面消息 接收消息
       *版本：1.0
       */
      newsMessageDelivered: function (message) {
        this.$message.info('newsMessageDelivered');
      },
      /**
       *宋建国：2019/11/28 17:18
       *描述：广播消息
       *版本：1.0
       */
      broadcastArrived: function (payloadMessage) {
        this.textarea2 = this.textarea2 + '       ' + payloadMessage.payloadString;
      },
      /**
       *宋建国：2019/11/28 17:19
       *描述：广播消息
       *版本：1.0
       */
      broadcastDelivered: function (message) {
        this.$message.info('broadcastDelivered');
      },
      /**
       *宋建国：2019/12/6 22:00
       *描述：接收聊天消息
       *版本：1.0
       */
      imMessageArrived: function (payloadMessage) {
        this.receiveData = payloadMessage ;
        this.textarea2 = payloadMessage.payloadString;

      },
      /**
       *宋建国：2019/12/6 22:00
       *描述：聊天发送成功
       *版本：1.0
       */
      imMessageDelivered: function (message) {
        this.receiveData = null;
      }
    },
    created(){
      /**
       *宋建国：2019/11/28 17:28
       *描述：订阅推送页面消息
       *版本：1.0
       */
      this.$mqtt.subscribe(this.$route.path,this.$topic.$PAGENEWS,{
        messageArrived: this.newsMessageArrived,
        messageDelivered: this.newsMessageDelivered
      });
      /**
       *宋建国：2019/11/28 17:28
       *描述：系统广播页面消息
       *版本：1.0
       */
      this.$mqtt.subscribe(this.$route.path,this.$topic.$PAGEBROADCAST,{
        messageArrived: this.broadcastArrived,
        messageDelivered: this.broadcastDelivered
      });
      /**
       *宋建国：2019/12/6 21:58
       *描述：接收聊天消息
       *版本：1.0
       */
      this.$mqtt.ImSubscribe(this.$topic.$IMNEWS,{
        messageArrived: this.imMessageArrived,
        messageDelivered: this.imMessageDelivered
      });
    }

  }
</script>

<style scoped>

</style>
