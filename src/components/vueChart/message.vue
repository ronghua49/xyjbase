<template>
	<div class="main-box" v-if="session.messages && session.messages.length>0">
		<div class="m-message" v-scroll-bottom="session.messages">
				<ul>
					<li v-for="(item,index) in session.messages" :key="index">
						<p class="time"><span>{{item.date | time}}</span></p>
						<div class="main" :class="{ self: item.self,afterself:item.text.toString().indexOf('uploadBorder') > -1 }">
							<img class="avatar" width="30" height="30" :src="item | avatar" />
							<div class="text">
								<div v-if="item.imgurl">
									<el-image
								    style="maxWidth: 300px; maxHeight: 200px"
								    :src="item.imgurl" 
								    :preview-src-list="imgArr">
								 </el-image>
								  <span v-html="item.text.replace(/<img.*?(?:>|\/>)/gi,'')"></span>
								</div>
								<chat-upload v-else-if="item.text.toString().indexOf('uploadBorder') > -1" ref="uploadFile" :content="item.text" :isShow="false"></chat-upload>
								<span v-else v-html="item.text"></span>
							</div>
						</div>
					</li>
				</ul>
			
		</div>
	</div>
</template>

<script>
	import chatUpload from "./upload"
	import vue from 'vue'
	let vm = {}
	export default {
		props: ['session', 'user', 'userList'],
		components:{chatUpload},
		computed: {
			sessionUser() {
				let users = this.userList.filter(item => item.id === this.session.userId);
				return users[0];
			},
		},
		data() {
			vm = this;
			return {
				imgArr:[]
			}
		},
		filters: {
			// 筛选出用户头像
			avatar(item) {
				// 如果是自己发的消息显示登录用户的头像
				let user = item.self ? vm.user : vm.sessionUser;
				return user && user.img;
			},
			// 将日期过滤为 hour:minutes
			time(date) {
				if(typeof date === 'string') {
					date = new Date(date);
				}
				return date.getHours() + ':' + date.getMinutes();
			}
		},
		directives: {
			// 发送消息后滚动到底部
			'scroll-bottom' (el) {
				vue.nextTick(() => {
					el.scrollTop = el.scrollHeight - el.clientHeight;
				});
			}
		},
		watch:{
			'session.messages'(val){
				this.imgArr = []
				val.forEach( x => {
					if(x.imgurl){
						this.imgArr.push(x.imgurl);
					}
				})
			}
		}
	};
</script>

<style lang="scss" scoped>
	.main-box {
  height: calc(100% - 223px)
}
img {
  max-width: 200px;
  max-height: 200px;
}
.m-message {
  padding: 10px 15px;
  overflow-y: scroll;
  box-sizing: border-box;
  height: 100%;
  ul {
    margin: 0 !important;
    padding: 0 !important;
  }
  li {
    margin-bottom: 15px;
    list-style: none;
  }
  .time {
    margin: 7px 0;
    text-align: center;
    & > span {
      display: inline-block;
      padding: 0 18px;
      font-size: 12px;
      color: #fff;
      border-radius: 2px;
      background-color: #dcdcdc;
    }
  }
  .avatar {
    float: left;
    margin: 0 10px 0 0;
    border-radius: 3px;
  }
  .text {
    display: inline-block;
    position: relative;
    padding: 0 10px;
    max-width: calc(100% - 40px);
    min-height: 30px;
    line-height: 2.5;
    font-size: 12px;
    text-align: left;
    word-break: break-all;
    background-color: #fafafa;
    border-radius: 4px;
    box-sizing: border-box;
    &:before {
      content: " ";
      position: absolute;
      top: 9px;
      right: 100%;
      border: 6px solid transparent;
      border-right-color: #fafafa;
    }
    /deep/ {
      .pasteImg {
        width: 20px;
        height: 20px;
        vertical-align: sub;
      }
    }
  }
  .self {
    text-align: right;
    .avatar {
      float: right;
      margin: 0 0 0 10px;
    }
    .text {
      background-color: #b2e281;
      &:before {
        right: inherit;
        left: 100%;
        border-right-color: transparent;
        border-left-color: #b2e281;
      }
    }
  }
  .afterself {
    text-align: right;
    .avatar {
      float: right;
      margin: 0 0 0 10px;
    }
    .text {
      background-color: #fafafa;
      &:before {
        right: inherit;
        left: 100%;
        border-right-color: transparent;
        border-left-color: #fafafa;
      }
    }
  }
}
.fade-enter-active,
.fade-leave-active {
  transition: opacity .5s;
}
.fade-enter,
.fade-leave-to {
  opacity: 0;
}
/deep/ {
  .el-image-viewer__close {
    color: #fff !important;
  }
}

</style>