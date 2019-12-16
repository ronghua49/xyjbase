<template>
	<div class="m-text">
		<div class="tool">
			<!--表情-->
			<el-popover placement="top-start" width="400" trigger="click">
				<el-tabs tab-position="bottom" value="Emotions" class="emoji_tabs_box">
					<el-tab-pane v-for="(emojiCon, emojiKey, eInd) in emojiIcon" :key="emojiKey" :label="emojiKey" :name="emojiKey">
						<span v-if="eInd == 0" slot="label" class="iconfont emoji_pane_tab">&#xe612;</span>
						<span v-if="eInd == 1" slot="label" class="iconfont emoji_pane_tab">&#xe609;</span>
						<span v-if="eInd == 2" slot="label" class="iconfont emoji_pane_tab">&#xe62d;</span>
						<span v-if="eInd == 3" slot="label" class="iconfont emoji_pane_tab">&#xe63c;</span>
						<img v-for="(emoItem, emoInd) in emojiCon" :key="emoInd" :src="getIconPic(emoItem.unicode)" alt="X" @click="sendEmojiIcon(emoItem.unicode)" class="chat_emoji_item">
					</el-tab-pane>
				</el-tabs>
				<el-button class="fa fa-smile-o" size="big" type="text" slot="reference" title="表情" @click="initEmojiPic"></el-button>
			</el-popover>
			<!--文件-->
			<el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/" multiple :http-request="beforeAvatarUpload" :show-file-list="false">
				<el-button class="el-icon-folder" type="text" title="发送文件"></el-button>
			</el-upload>
			<!--聊天记录-->
			<el-button class="el-icon-chat-dot-round" type="text" slot="reference" title="聊天记录" @click="openHistory"></el-button>
		</div>
		<!--<textarea id="charInput" placeholder="按 Ctrl + Enter 发送" v-model="text" @keyup="inputing"></textarea>-->
		<div ref="text" id="charInput" contenteditable="true" @keyup="inputing" @paste="copyPaste">
			
		</div>
		<chat-upload ref="uploadFile" :file="file" :isShow="true" v-show="Object.keys(file).length !== 0"></chat-upload>
		<button class="sendBtn" @click="sendMethod">发送</button>
		<History ref="History" :data="session"></History>
	</div>
</template>

<script>
	import chatUpload from "./upload"
	import axios from "@/http/axios"
	import jquery from 'jquery'
	import html2canvas from "html2canvas"
	import History from "./history"
	let emojiData = require('../../assets/emoji/emoji.json')
	export default {
		props: ['session','user'],
		components: {
			chatUpload,
			History
		},
		data() {
			return {
				text: '',
				emojiIcon: emojiData.icon, // 导入的emoji表情配置文件内容
				emojiPath: new Map(), // emoji表情地址map对象,
				inputRange: '', // 光标
				file: {},
				receiveData:null
			};
		},
		methods: {
			inputing(e) {
				if(e.ctrlKey && e.keyCode === 13 && this.text.length) {
					this.session.messages.push({
						text: this.text,
						date: new Date(),
						self: true
					});
					this.$refs.text.innerHTML = '';
				}
			},
			/**
			 *张俊娜：2019/12/4
			 *描述：发送消息
			 *版本：1.0
			 */
			sendMethod() {
				jquery('#charInput .fileImg').remove()
				if(Object.keys(this.file).length !== 0){
					this.$api.im.sendMessage({
						fromUserId:this.$mqtt._clientId,
						messageString:this.$refs.uploadFile.$el.innerHTML,
						messageType:this.$topic.$MESSAGETYPE.ChatText,
						toUserId:this.session.conversationid,
						toUserNickName:this.session.name,
						topic:this.$topic.$IMNEWS
					}).then(res => {
						
					})
					this.session.messages.push({
						text: this.$refs.uploadFile.$el.innerHTML,
						date: new Date(),
						self: true,
					}); 
				}else{
					let imgUrl = '';
					let msg = this.$refs.text.innerHTML.toString().indexOf('unicode') > -1 ?
						this.formatInputCon().replace(/<br>/g, '\r\n') :
						this.$refs.text.innerHTML;
					let sessionPush = this.$refs.text.innerHTML.toString().indexOf('unicode') > -1 ?
						this.changeEmojiCon(this.formatInputCon().replace(/<br>/g, '\r\n')) :
						this.$refs.text.innerHTML;
					this.$api.im.sendMessage({
						fromUserId:this.$mqtt._clientId,
						messageString:msg,
						messageType:this.$topic.$MESSAGETYPE.ChatText,
						toUserId:this.session.conversationid,
						toUserNickName:this.session.name,
						topic:this.$topic.$IMNEWS
					}).then(res => {
						
					})
					if(!(sessionPush.toString().indexOf('pasteImg') > -1) && !(sessionPush.toString().indexOf('uploadBorder') > -1)) {
						let imgReg = /<img.*?(?:>|\/>)/gi //匹配图片中的img标签
						let srcReg = /src=[\'\"]?([^\'\"]*)[\'\"]?/i // 匹配图片中的src
						let str = sessionPush
						let arr = str.match(imgReg) //筛选出所有的img
						if(arr) {
							for(let i = 0; i < arr.length; i++) {
								let src = arr[i].match(srcReg)
								// 获取图片地址
								imgUrl = src[1]
							}
						}
					}
					this.session.messages.push({
						text: sessionPush,
						date: new Date(),
						self: true,
						imgurl: imgUrl
					});
				}
				
				this.$refs.text.innerHTML = ''
				this.file = {}
			},
			/**
			 *张俊娜：2019/11/25 14:38
			 *描述：推送页面消息 接收消息
			 *版本：1.0
			 */
			imMessageArrived: function(payloadMessage) {
				debugger
				this.receiveData = payloadMessage
				this.$message.info(payloadMessage.payloadString);
			},
			/**
			 *张俊娜：2019/11/25 14:38
			 *描述：推送页面消息 接收消息
			 *版本：1.0
			 */
			imMessageDelivered: function(message) {
				debugger
				this.receiveData = null
			},
			/**
			 *张俊娜：2019/11/28 17:18
			 *描述：广播消息
			 *版本：1.0
			 */
			broadcastArrived: function(payloadMessage) {
				this.receiveData = payloadMessage
				debugger
				let imgUrl = '';
				if(!JSON.parse(payloadMessage.payloadString).messageString){
					return
				}
				let sessionPush = JSON.parse(payloadMessage.payloadString).messageString.toString().indexOf('unicode') > -1 ?
					this.changeEmojiCon(JSON.parse(payloadMessage.payloadString).messageString) :
					JSON.parse(payloadMessage.payloadString).messageString
				if(sessionPush.toString().indexOf('base64') > -1 && !(sessionPush.toString().indexOf('uploadBorder') > -1)) {
					let imgReg = /<img.*?(?:>|\/>)/gi //匹配图片中的img标签
					let srcReg = /src=[\'\"]?([^\'\"]*)[\'\"]?/i // 匹配图片中的src
					let str = sessionPush
					let arr = str.match(imgReg) //筛选出所有的img
					if(arr) {
						for(let i = 0; i < arr.length; i++) {
							let src = arr[i].match(srcReg)
							// 获取图片地址
							imgUrl = src[1]
						}
					}
				}
				this.session.messages.push({
					text: sessionPush,
					date: new Date(),
					self: false,
					imgurl: imgUrl
				});
			},
			/**
			 *张俊娜：2019/11/28 17:19
			 *描述：广播消息
			 *版本：1.0
			 */
			broadcastDelivered: function(message) {
				debugger
			},
			/**
			 *张俊娜：2019/12/4
			 *描述：通过Unicode码从map中获取emoji
			 *版本：1.0
			 */
			getIconPic(unicode) {
				return this.emojiPath.get(unicode)
			},
			/**
			 *张俊娜：2019/12/4
			 *描述：点击表情，将表情添加到输入框
			 *版本：1.0
			 */
			sendEmojiIcon(code, type) {
				let inputNode = document.getElementById('charInput')
				let html = code.toString().indexOf('base64') > -1 ?
					"<img class='photo' src='" + code + "' alt='' >" :
					"<img class='pasteImg' src='" + this.getIconPic(code) + "' unicode = '" + code + "' alt='' >"
				if(type == 2) {
					html = "<img class='fileImg' src='" + code + "' alt='' >"
				}
				let sel = window.getSelection()
				let range = this.inputRange
				let el = document.createElement("div")
				let frag = document.createDocumentFragment(),
					node, lastNode

				if(!inputNode) {
					return
				}

				if(!range) {
					inputNode.focus()
					range = window.getSelection().getRangeAt(0)
				}

				range.deleteContents()
				el.innerHTML = html

				while((node = el.firstChild)) {
					lastNode = frag.appendChild(node)
				}
				range.insertNode(frag)

				if(lastNode) {
					range = range.cloneRange()
					range.setStartAfter(lastNode)
					range.collapse(true)
					sel.removeAllRanges()
					sel.addRange(range)
				}
			},
			/**
			 *张俊娜：2019/12/4
			 *描述：初始化emoji的map对象
			 *版本：1.0
			 */
			initEmojiPic() {
				let map = new Map()

				for(const key in emojiData.icon) {
					emojiData.icon[key].forEach(item => {
						map.set(item.unicode, require('../../assets/emoji/icon/' + item.unicode + '.png'))
					})
				}
				this.emojiPath = map
			},
			/**
			 *张俊娜：2019/12/4
			 *描述：将输入框中的图片替换为emoji表情
			 *版本：1.0
			 */
			formatInputCon() {
				let inputValue = this.$refs.text.innerHTML
				inputValue = inputValue.replace(/<img.*?(?:>|\/>)/gi, (val) => {
					let iPic = ''
					if(val.toString().indexOf('pasteImg') > -1){
						let unicode = val.match(/unicode=[\'\"]?([^\'\"]*)[\'\"]?/i)[1]
						let icon = this.emojiIcon
						// 遍历查找Unicode表情
						for(const key in icon) {
							if(icon.hasOwnProperty(key)) {
								const iType = icon[key]
								let flag = false
	
								for(let index = 0; index < iType.length; index++) {
									const element = iType[index]
	
									if(element.unicode == unicode) {
										iPic = element.emoji
										flag = true
										break
									}
								}
	
								if(flag) {
									break
								}
							}
						}
					}else{
						iPic = val
					}
					console.log(iPic)
					return iPic
				})
				
				return inputValue
			},
			/**
			 *张俊娜：2019/12/4
			 *描述：将emoji表情转换为图片
			 *版本：1.0
			 */
			changeEmojiCon(str) {
				let patt = /[\ud800-\udbff][\udc00-\udfff]/g // 检测utf16字符正则

				str = str.replace(patt, (char) => {
					let H, L, code

					if(char.length === 2) {
						H = char.charCodeAt(0) // 取出高位
						L = char.charCodeAt(1) // 取出低位
						code = (H - 0xD800) * 0x400 + 0x10000 + L - 0xDC00 // 转换算法
						return "&#" + code + ";"
					} else {
						return char
					}
				})

				str = str.replace(/&#{1}[0-9]+;{1}/ig, (a) => {
					let unicode = a.replace(/^&#{1}/ig, '')
					unicode = unicode.replace(/;{1}$/ig, '')
					unicode = 'U+' + (parseFloat(unicode).toString(16).toUpperCase())
					return "<img class='pasteImg' src='" + this.getIconPic(unicode) + "'/>"
				})

				return str
			},
			/**
			 *张俊娜：2019/12/4
			 *描述：复制粘贴图片
			 *版本：1.0
			 */
			copyPaste(e) {
				let sUsrAg = navigator.userAgent;
				let that = this;

				if(e.clipboardData || e.originalEvent) {
					//某些chrome版本使用的是event.originalEvent
					var clipboardData = (e.clipboardData || e.originalEvent.clipboardData);
					if(clipboardData.items) {
						// for chrome
						var items = clipboardData.items,
							len = items.length,
							blob = null;
						for(var i = 0; i < len; i++) {
							if(items[i].type.indexOf("image") !== -1) {
								blob = items[i].getAsFile();
							}
						}
						if(blob !== null) {
							var reader = new FileReader();
							reader.readAsDataURL(blob);   
							reader.onload = function() {
								that.sendEmojiIcon(reader.result)
								if(sUsrAg.indexOf("Firefox") > -1) {
									that.$refs.text.removeChild(document.getElementsByClassName('photo')[0])
								}   
							}
						}
					}
				}
			},
			/**
			 *张俊娜：2019/12/9
			 *描述：上传文件
			 *版本：1.0
			 */
			beforeAvatarUpload(file) {
				let then = this;
        let fd = new FormData();
        fd.append("file",file.file);
        fd.append("type",'ImFile');
        axios({
          hostUrl:then.global.hostUrl,
          url:"/FileUploadController/uploadFileType",
          method:"post",
          data:fd
        }).then(res =>{
          then.file = file.file;
					then.$nextTick(() => {
						html2canvas(then.$refs.uploadFile.$el.firstChild).then(canvas => {
							// 转成图片，生成图片地址
							let imgUrl = canvas.toDataURL("image/png");
							then.sendEmojiIcon(imgUrl, 2)
						})
					})
        }).catch(err =>{
          then.$message.error(err.message)
        })
			},
			/**
			 *张俊娜：2019/12/9
			 *描述：历史记录
			 *版本：1.0
			 */
			openHistory(){
				this.$refs.History.open()
			}
		},
		created() {
			/**
			 *宋建国：2019/11/28 17:28
			 *描述：订阅推送页面消息
			 *版本：1.0
			 */
			 
			setTimeout(()=>{
				this.$mqtt.ImSubscribe(this.$topic.$IMNEWS, {
				messageArrived: this.imMessageArrived,
				messageDelivered: this.imMessageDelivered
			});
			/**
			 *宋建国：2019/11/28 17:28
			 *描述：系统广播页面消息
			 *版本：1.0
			 */
			this.$mqtt.ImSubscribe(this.$topic.$IMBROADCAST, {
				messageArrived: this.broadcastArrived,
				messageDelivered: this.broadcastDelivered
			});
			},2000);
		}
	};
</script>

<style lang="scss" scoped>
	.m-text {
  height: 160px;
  border-top: solid 1px #ddd;
  border-bottom: solid 1px #ccc;
  bottom: 2px;
  box-sizing: border-box;
  padding: 10px 15px;
  background: #fff;
  #charInput {
    padding: 10px;
    height: 54%;
    width: 100%;
    border: none;
    outline: none;
    font-family: "Micrsofot Yahei";
    resize: none;
    box-sizing: border-box;
    overflow-y: auto;
    /deep/ {
      .pasteImg {
        width: 20px;
        height: 20px;
        vertical-align: sub;
      }
    }
  }
  .sendBtn {
    position: absolute;
    bottom: 10px;
    right: 15px;
    outline: none;
    width: 60px;
    height: 30px;
    color: #666;
    border-color: #ddd;
    cursor: pointer;
  }
}
.emoji_tabs_box {
  /deep/ {
    .el-tabs__nav {
      display: flex;
      width: 100%;
    }
    .el-tabs__item {
      flex: 1;
      margin: 0px;
      text-align: center;
      font-weight: 500;
    }
  }
  .emoji_pane_tab {
    font-size: 18px;
  }
  .el-tabs__header {
    margin-bottom: 0;
    padding-top: 5px;
    margin-top: 10px;
    border-top: 1px solid #e6e6e6;
  }
  .el-tabs__active-bar {
    display: none;
  }
}
.el-popover {
  /deep/ {
    .el-tabs__item {
      height: 25px;
    }
  }
  .el-tabs__active-bar {
    background: #409eff;
  }
  .el-button {
    & + .el-button {
      margin-left: 0;
    }
  }
  .chat_emoji_item {
    width: 25px;
    height: 25px;
    margin: 3px;
    cursor: pointer;
    user-select: none;
  }
  .el-tabs__nav-wrap {
    &::after {
      background-color: #fff;
    }
  }
}
.chat_page_con {
  padding: 20px 50px;
  box-sizing: border-box;
  width: 100wv;
  height: 100vh;
  overflow: hidden;
}
@font-face {
  font-family: 'iconfont';
  src: url('//at.alicdn.com/t/font_1098946_1k69hkxpndp.eot');
  src: url('//at.alicdn.com/t/font_1098946_1k69hkxpndp.eot?#iefix') format('embedded-opentype'), url('//at.alicdn.com/t/font_1098946_1k69hkxpndp.woff2') format('woff2'), url('//at.alicdn.com/t/font_1098946_1k69hkxpndp.woff') format('woff'), url('//at.alicdn.com/t/font_1098946_1k69hkxpndp.ttf') format('truetype'), url('//at.alicdn.com/t/font_1098946_1k69hkxpndp.svg#iconfont') format('svg');
}
.chatframe-icon {
  display: inline-block;
  height: 40px;
  border-bottom: 1px solid #e8e8e8;
  vertical-align: top;
  margin-left: 10px;
  .iconfont {
    display: inline-block;
    font-size: 20px;
    cursor: pointer;
  }
  .open_emoji_icon {
    color: #ffbf1f;
  }
}
/deep/ {
  .el-tabs__content {
    height: 140px;
  }
  .el-button {
    margin: 0;
  }
  .photo {
    max-height: 200px;
    max-width: 300px;
  }
}
.upload-demo {
  display: inline-block;
}

</style>