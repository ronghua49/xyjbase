<template>
	<div id="chart" v-drag>
		<div class="cardList">
			<div class="m-card">
				<header>
					<img class="avatar" width="30" height="30" :alt="user.name" :src="user.img">
					<!--<p class="name">{{user.name}}</p>-->
				</header>
				<div class="foot">
					<el-button class="el-icon-chat-round stylesize" type="text" title="聊天" @click="getFriend"></el-button>
					<el-button class="el-icon-user stylesize" type="text" title="联系人" @click="showAdd"></el-button>
					<el-button class="el-icon-s-tools stylesize" type="text" title="设置" @click="editDialog"></el-button>
				</div>
			</div>
		</div>
		<div class="sidebar">
			<card v-show="isAdd" :session="session"></card>
			<list v-show="!isAdd" :user-list="userList" :session="session" :session-index.sync="sessionIndex" :search="search"></list>
		</div>
		<div class="main border">
			<div class="message-head">
				<span>{{userList.length>0 ? !isFriend ? ' ' : userList.filter(item => item.id === session.userId)[0].name : ' '}}</span>
				<span>
        		<i class="fa fa-minus" @click="close" title="最小化"></i>
            <i :class="[isFullscreen ?'fa fa-square-o' : 'fa fa-clone rotate']" 
            	:title="isFullscreen ? '最大化' : '向下还原'" 
            	@click="toggleFullScreen($event)"></i>
            <i class="fa fa-times fa-lg" @click="close" title="关闭"></i>
        </span>
			</div>
			<message v-show='isFriend' :session="session" :user="user" :user-list="userList"></message>
			<texts v-show='isFriend' :session="session" :user="user" ref="texts"></texts>
			<friend ref="friends" v-show='!isFriend' :receiveData="receiveData"></friend>
		</div>
		<el-dialog 
			title="修改信息"
			:visible.sync="dialogVisible"
      width="30%"
      :modal="false">
      <el-form ref="form" :model="form" label-width="80px" size="mini">
      	<el-form-item label="昵称">
			    <el-input v-model="form.usernickname"></el-input>
			  </el-form-item>
			  <el-form-item label="真实姓名">
			    <el-input v-model="form.username"></el-input>
			  </el-form-item>
			  <el-form-item label="手机号">
			    <el-input v-model="form.phone"></el-input>
			  </el-form-item>
			  <el-form-item label="头像">
			    <el-upload
					  class="avatar-uploader"
					  action="https://jsonplaceholder.typicode.com/posts/"
					  :show-file-list="false"
					  :http-request="handleAvatarSuccess">
					  <img v-if="form.headportrait" :src="form.headportrait" class="avatarImg">
					  <i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
			  </el-form-item>
			  <el-form-item label="备注">
			    <el-input v-model="form.memo"></el-input>
			  </el-form-item>
			  <el-form-item label="更换绑定">
			    <el-input v-model="form.mobel" @keyup.native="onkeyup($event)"></el-input>
			  </el-form-item>
      </el-form>
			<span slot="footer" class="dialog-footer">
		    <el-button size="mini" @click="edit" type="primary">确定修改</el-button>
		  </span>
		</el-dialog>
	</div>
</template>

<script>
	import store from './store';
	import card from './card';
	import list from './list';
	import texts from './texts';
	import message from './message';
	import friend from './friend'
	import axios from '@/http/axios';

	export default {
		data() {
			let serverData = store.fetch();

			return {
				dialogVisible:false,
				form:{
					username:'',
					usernickname:'',
					headportrait:serverData.user.img,
					phone:'',
					mobel:'',
					memo:''
				},
				file:'',
				isFriend:true,
				isAdd:false,
				receiveData:'',
				// 是否全屏
				isFullscreen:true,
				// 聊天框是否显示
				show: true,
				// 是否按下esc键
				isFull:null,
				// 登录用户
				user: serverData.user,
				// 用户列表
				userList: serverData.userList,
				// 会话列表
				sessionList: serverData.sessionList,
				// 搜索key
				search: {
					text: ''
				},
				// 选中的会话Index
				sessionIndex: {
					index: 0
				}
			};
		},
		computed: {
			session() {
				return this.sessionList.length>0 ? this.sessionList[this.sessionIndex.index] : [];
			},
		},
		watch: {
			// 每当sessionList改变时，保存到localStorage中
			sessionList: {
				deep: true,
				handler(val) {
					this.sessionList = val;
				}
			}
		},
		components: {
			card,
			list,
			texts,
			message,
			friend
		},
		methods: {
			trigger(){
				this.isFriend = false;
			},
			close() {
				this.$parent.chat = false
			},
			/**
			 *张俊娜：2019/12/3
			 *描述：监听是否按esc键
			 *版本：1.0
			 */
			checkFull() {
				//火狐浏览器
				var isFull = document.mozFullScreen||
				document.fullScreen ||
				//谷歌浏览器及Webkit内核浏览器
				document.webkitIsFullScreen ||
				document.webkitRequestFullScreen ||
				document.mozRequestFullScreen ||
				document.msFullscreenEnabled
				if(isFull === undefined) isFull = false;
		    return isFull;
			},
			/**
			 *张俊娜：2019/12/3
			 *描述：全屏退出全屏
			 *版本：1.0
			 */
			FullScreen(el) {
				if(this.isFullscreen) { //退出全屏
					if(document.exitFullscreen) {
						document.exitFullscreen()
					} else if(document.mozCancelFullScreen) {
						document.mozCancelFullScreen()
					} else if(document.webkitExitFullscreen) {
						document.webkitExitFullscreen()
					} else if(!document.msRequestFullscreen) {
						document.msExitFullscreen()
					}
				} else { //进入全屏
					if(el.requestFullscreen) {
						el.requestFullscreen()
					} else if(el.mozRequestFullScreen) {
						el.mozRequestFullScreen()
					} else if(el.webkitRequestFullscreen) {
						el.webkitRequestFullscreen();
					} else if(el.msRequestFullscreen) {
						this.isFullscreen = true;
						el.msRequestFullscreen()
					}
				}
			},
			/**
			 *张俊娜：2019/12/3
			 *描述：转换全屏
			 *版本：1.0
			 */
			toggleFullScreen(e) {
				this.isFullscreen = !this.isFullscreen;
				this.FullScreen(document.getElementById("chart"));
			},
			/**
			 *张俊娜：2019/12/11
			 *描述：用户修改
			 *版本：1.0
			 */
			edit(){
				let then = this;
        let fd = new FormData();
        fd.append("file",this.file.file);
        fd.append("type",'ImFile');
        axios({
          hostUrl:then.global.hostUrl,
          url:"/FileUploadController/uploadFileType",
          method:"post",
          data:fd
        }).then(res =>{
        	this.form.userid = this.user.id
          this.$api.im.userSave(this.form).then(res => {
          	this.user.img = res.data.headportrait
          }).catch(err => {
          	this.$message.error(err.message)
          })
        }).catch(err =>{
          then.$message.error(err.message)
        })
			},
			editDialog(){
				let query = {
					"querys": [
						{
							"hasInitValue": true,
							"operation": "EQUAL",
							"property": "id",
							"relation": "AND",
							"value": this.$mqtt.getClientId()
						}
					]
      	}
				this.$api.im.userQuery(query).then(res => {
					if(res.data.rows && res.data.rows.length>0){
						for(let key in res.data.rows[0]){
				      this.form[key] = res.data.rows[0][key]
				    }
	        	this.user.id = res.data.rows[0].id
	        	this.user.img = res.data.rows[0].headportrait
	        	this.user.name = res.data.rows[0].usernickname
					}
        }).catch(err => {
        	this.$message.error(err.message)
        })
				this.dialogVisible = true
			},
			/**
			 *张俊娜：2019/12/11
			 *描述：上传图片
			 *版本：1.0
			 */
			handleAvatarSuccess(file) {
				this.file = file
        this.form.headportrait = URL.createObjectURL(file.file);
      },
      /**
			 *张俊娜：2019/12/11
			 *描述：显示新增好友
			 *版本：1.0
			 */
      showAdd(){
      	this.isAdd = true
      	debugger
      	this.receiveData = this.$refs.texts.receiveData
      },
      /**
			 *张俊娜：2019/12/11
			 *描述：更换绑定
			 *版本：1.0
			 */
      onkeyup(event){
      	console.log(1)
      	let query = {
					"querys": [
						{
							"hasInitValue": true,
							"operation": "EQUAL",
							"property": "phone",
							"relation": "AND",
							"value": this.form.mobel
						}
					]
      	}
        if(event.keyCode != 13){//除回车键外
            //标记当前事件函数的时间戳
            this.lastTimeStamp = event.timeStamp;
            setTimeout(() => {
                //1s后比较二者是否还相同（因为只要还有事件触发，lastTimeStamp就会被改写，不再是当前事件函数的时间戳）
                if(this.lastTimeStamp == event.timeStamp){
                    this.$api.im.userQuery(query).then(res => {
                    	
                    }).catch(err => {
                    	this.$message.error(err.message)
                    })
                }
            }, 1000);
        }
      },
      /**
			 *张俊娜：2019/12/12
			 *描述：获取用户好友
			 *版本：1.0
			 */
      FriendList(){
      	let query = {
					"querys": [
						{
							"hasInitValue": true,
							"operation": "EQUAL",
							"property": "mainUserId",
							"relation": "AND",
							"value": this.$mqtt.getClientId()
						}
					]
	    	}
				this.$api.im.RelationshipQuery(query).then(res => {
					this.user.name = sessionStorage.getItem('user')
					if(res.data.rows && res.data.rows.length>0){
						this.userList = res.data.rows.map(element => {
							let obj = {};
							obj.id = element.id;
							obj.img = require('@/assets/images/2.png');
							obj.name = element.usernickname;
							obj.conversationid = element.friendUserId;
							return obj;
						});
						this.sessionList = res.data.rows.map(element => {
							let obj = {};
							obj.userId = element.id;
							obj.name = element.usernickname;
							obj.conversationid = element.friendUserId;
							obj.messages = [];
							return obj;
						});
					}
				}).catch(err => {
					this.$message.error(err)
				})
      },
      /**
			 *张俊娜：2019/12/12
			 *描述：好友列表
			 *版本：1.0
			 */
      getFriend(){
      	this.FriendList()
      	this.isAdd = false;this.isFriend = true
      }
		},
		/**
			 *张俊娜：2019/12/3
			 *描述：获取好友列表
			 *版本：1.0
			 */
		created() {
			this.FriendList()
			this.editDialog()
			this.dialogVisible = false
		},
		/**
			 *张俊娜：2019/12/3
			 *描述：窗口变化判断时候按esc键
			 *版本：1.0
			 */
		mounted() {
			let that = this;
			window.onresize = function() {
				if(!that.checkFull()) {
					that.isFullscreen = true;	
				}
			}
		}
	};
</script>

<style lang="scss">
	#chart {
  position: relative;
  margin: 20px auto;
  width: 800px;
  height: 600px;
  overflow: hidden;
  border-radius: 3px;
  text-align: left;
  line-height: normal;
}
.cardList{
	float: left;
	background-color: #2e3238;
}
.cardList :after{
	content: '';
	height: 0;
	line-height: 0;
	display: block;
	visibility: hidden;
	clear: both;
}
.sidebar,
.main,.cardList {
  height: 100%;
}
.sidebar {
  float: left;
  width: 200px;
  color: #000;
  background-color: #eee;
}
.main {
  position: relative;
  overflow: hidden;
  background-color: #fafafa;
}
.m-text {
  position: absolute;
  width: 100%;
  bottom: 0;
  left: 0;
}
.m-message {
  height: calc(100% - 160px);
}
.border {
  border: 1px solid #cccccc;
  border-left: none;
  border-bottom: none;
}
.message-head {
  display: flex;
  justify-content: space-between;
  box-sizing: border-box;
  border-bottom: 1px solid #cccccc;
  padding: 21px;
}
i {
  font-style: normal;
  cursor: pointer;
  vertical-align: middle;
  margin-left: 10px;
}
.rotate {
  transform: rotateY(180deg);
}
.m-card {
  padding: 12px;
  /*border-bottom: solid 1px #24272c;*/
}
.avatar,
.name {
  vertical-align: middle;
  vertical-align: middle;
}
.avatar {
  border-radius: 2px;
  border-radius: 2px;
}
.name {
  display: inline-block;
  margin: 0 0 0 15px;
  font-size: 16px;
  display: inline-block;
  margin: 0 0 0 15px;
}
.foot{
	margin-top: 15px;
	text-align: center;
	/deep/ .stylesize{
		display: block;
		margin: 0!important;
		font-size:20px;
		color: #999;
	}
}
.avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 100px;
    height: 100px;
    line-height: 100px!important;
    text-align: center;
    margin-left: 0;
  }
  .avatarImg {
    width: 100px;
    height: 100px;
    display: block;
  }
</style>