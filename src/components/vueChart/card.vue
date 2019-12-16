<template>
	<div>
		<footer>
			<input class="search" type="text" placeholder="搜索好友" v-model="friendId" @keyup="onkeyup($event)">
		</footer>
		<ul>
			<li @click="showFriend" v-if="!friendId">
				<img class="avatar" width="30" height="30" src="../../../build/logo.png">
				<p class="name">新的朋友</p>
			</li>
			<li v-else v-for="(item,index) in userList" :key="index" @click="showFriendDialog(item)">
				<img class="avatar" width="30" height="30" src="../../../build/logo.png">
				<p class="name">{{item.username}}</p>
			</li>
		</ul>
		<el-dialog 
			:visible.sync="dialogVisible"
      width="30%"
      :modal="false">
      <ul>
      	<li>
					<img class="avatar" width="30" height="30" src="../../../build/logo.png">
					<p class="name">{{ApplyFriends.username}}</p>
				</li>
      </ul>
      <el-input
			  type="textarea"
			  :rows="2"
			  placeholder="备注"
			  v-model="textarea">
			</el-input>
			<span slot="footer" class="dialog-footer">
		    <el-button size="mini" @click="applyFriend">申请添加为好友</el-button>
		  </span>
		</el-dialog>
	</div>
</template>

<script>
	export default{
		data(){
			return {
				friendId:'',
				lastTimeStamp: 0, //标记当前事件函数的时间戳
				userList:[],
				dialogVisible:false,
				ApplyFriends:{},
				textarea:''
			}
		},
		methods:{
			showFriend(){
				this.$parent.trigger()
				this.$parent.$refs.friends.find()
			},
			/**
       *张俊娜：2019/12/9
       *描述：申请好友
       *版本：1.0
       */
      applyFriend(){
        /**
         *宋建国：2019/12/9
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
              "value": this.friendId
            }
          ]
        }).then((res)=>{
          if(res.data.rows.length>0){
            this.$message.info(this.nickname+"用户已经加为好友");
            this.dialogVisible = false;
            return;
          }
          this.$api.im.addFriend({
            topic: this.$topic.$IMNEWS,
            messageType: this.$topic.$MESSAGETYPE.ApplyFriend,
            toUserId:this.ApplyFriends.id,
            toUserNickName: this.ApplyFriends.username,
            messageString: this.textarea,
            fromUserId:this.$mqtt.getClientId()
          }).then(res => {
          	
          }).catch(err => {
          	this.$message.error(err.message)
          })
          this.dialogVisible = false;
        }).catch((error)=>{
          this.$message.error(error.message);
        });
      },
      /**
       *张俊娜：2019/12/10
       *描述：搜索好友
       *版本：1.0
       */
      onkeyup(event){
      	let query = {
					"querys": [
						{
							"hasInitValue": true,
							"operation": "LIKE",
							"property": "username",
							"relation": "OR",
							"value": this.friendId
						},
						{
							"hasInitValue": true,
							"operation": "LIKE",
							"property": "userNickName",
							"relation": "OR",
							"value": this.friendId
						},
						{
							"hasInitValue": true,
							"operation": "LIKE",
							"property": "phone",
							"relation": "OR",
							"value": this.friendId
						}
					]
      	}
        if(event.keyCode != 13){//除回车键外
            //标记当前事件函数的时间戳
            this.lastTimeStamp = event.timeStamp;
            setTimeout(() => {
                //1s后比较二者是否还相同（因为只要还有事件触发，lastTimeStamp就会被改写，不再是当前事件函数的时间戳）
                if(this.lastTimeStamp == event.timeStamp){
                    if(this.friendId){
                    	this.$api.im.userQuery(query).then(res => {
	                    	this.userList = res.data.rows
	                    }).catch(err => {
	                    	this.$message.error(err.message)
	                    })
                    }
                }
            }, 1000);
        }
      },
      showFriendDialog(item){
      	this.ApplyFriends = item;
      	this.dialogVisible = true;
      }
		}
	}
</script>

<style scoped>
	ul{
		margin: 0;
		padding: 0;
	}
	li {
	  padding: 12px 15px;
	  cursor: pointer;
	  transition: background-color .1s;
	  list-style: none;
	  &:hover {
	    background-color: rgba(88, 88, 88, 0.03);
	  }
	}
	.active {
	  background-color: rgba(88, 88, 88, 0.1);
	}
	.avatar,
	.name {
	  vertical-align: middle;
	}
	.avatar {
	  border-radius: 2px;
	}
	.name {
	  display: inline-block;
	  margin: 0 0 0 15px;
	}
	footer {
  padding: 15.5px 12px;
  border-bottom: 1px solid #ccc;
}
.search {
  padding: 0 10px;
  width: 100%;
  font-size: 12px;
  color: #000;
  height: 30px;
  line-height: 30px;
  border: solid 1px #fafafa;
  border-radius: 4px;
  outline: none;
  background-color: #fafafa;
  box-sizing: border-box;
}
</style>