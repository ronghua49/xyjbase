<template>
	<div>
		<ul>
			<li v-for="(item,index) in friends" :key="index">
				<img src="../../../build/logo.png" alt="" />
				<div class="uploadTitle">
					<span>{{item.fromUserName}}</span>
					<span>{{item.chatcontent ? item.chatcontent : '请求加你为好友'}}</span>
				</div>
				<el-button size="mini" :type="item.alreadyread == 0 ? 'success' : 'info'" @click="Addfriends(item)">{{item.alreadyread == 0 ? '同意':'已处理'}}</el-button>
			</li>
		</ul>
	</div>
</template>

<script>
	export default{
		props:['receiveData'],
		data(){
			return {
				friends:[]
			}
		},
		methods:{
			/**
       *宋建国：2019/12/6 22:45
       *描述：申请好友确认
       *版本：1.0
       */
      Addfriends(item){
      	debugger
        if(this.receiveData === null){
          this.$message.info("申请信息不存在");
          return;
        }
        let friedsinfo = JSON.parse(this.receiveData.payloadString);
        if(friedsinfo.messageType !== this.$topic.$MESSAGETYPE.ApplyFriend){
          this.$message.info("申请好友信息不存在");
          return;
        }
        this.$api.im.createFriend({
        	topic: this.$topic.$IMNEWS,
          messageType: this.$topic.$MESSAGETYPE.ApplyFriend,
          toUserId:item.fromchatid,
          toUserNickName: item.fromUserName,
          messageString: JSON.stringify(item),
          fromUserId:item.tochatid
        }).then((res) =>{
        	this.$api.im.sendMessage({
						fromUserId:this.$mqtt._clientId,
						messageString:friedsinfo.toUserNickName + "同意与你好友关系",
						messageType:this.$topic.$MESSAGETYPE.Addfriends,
						toUserId:friedsinfo.fromUserId,
						toUserNickName:friedsinfo.toUserNickName,
						topic:this.$topic.$IMNEWS
					}).then(res => {
						
					})
        }).catch((err)=>{
          this.$message.error(error.message);
        })
      },
      find(){
      	let query = {
					"querys": [
						{
							"hasInitValue": true,
							"operation": "EQUAL",
							"property": "tochatid",
							"relation": "AND",
							"value": this.$mqtt.getClientId()
						},
						{
							"hasInitValue": true,
							"operation": "EQUAL",
							"property": "chattype",
							"relation": "AND",
							"value": 21
						},
						{
							"hasInitValue": true,
							"operation": "EQUAL",
							"property": "alreadyread",
							"relation": "AND",
							"value": this.$topic.$MESSAGETYPE.ChatText
						}
					]
	    	}
				this.$api.im.HistoryQuery(query).then(res => {
					this.friends = res.data.rows
				}).catch(err => {
					this.$message.error(err)
				})
      }
		},
		mounted(){
//			this.find()
		}
	}
</script>

<style lang="scss" scoped>
ul{
	margin: 0;
	padding: 15px;
	box-sizing: border-box;
	li{
		list-style: none;
		box-sizing: border-box;
		padding-bottom: 10px;
		border-bottom: 1px solid #ccc;
		img {
	    width: 30px;
	    height: 35px;
	    vertical-align: top;
	  }
	  .uploadTitle {
	    display: inline-block;
	    font-size: 12px;
	    color: #000;
	    width:80%;
	    span {
	      display: block;
	    }
	  }
	}
}
</style>