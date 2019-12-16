<template>
	<div style="display:inline-block">
		<el-upload
			action=""
      style="position:relative;display:inline-block;"
      :http-request="importExcel"
      :show-file-list="false"
      :disabled="isDisabled"
    >
      <Xyj-button type="primary" :label="$t(label)" :disabled="isDisabled" @click="load" :text="text"/>
      <!-- 上传进度条 -->
      <el-progress
        id="progress"
        :text-inside="true"
        :stroke-width="32"
        color="#e8f3f5"
        v-if="progress.tableFlag"
        type="line"
        :percentage="progress.UploadPercent"
      ></el-progress>
    </el-upload>
	</div>
</template>

<script>
	import axios from "@/http/axios";
	export default{
		name: 'XyjUpload',
	  props: {
	    uploadUrl: {  // 按钮显示文本
	      type: Object,
	      default(){
	      	return {
	      		hostUrl:'',
	      		url:'',
            /*上传参数整理*/
            upParam: function (upFile) {
              let fd = new FormData();
              fd.append('file',upFile.file);
              return fd;
            },
            /*上传成功回调*/
            upSuccess : function (res) {

            }
	      	}
	      }
	    },
      text: {
	      type: String,
        default: "上传文件"
      },
      label: {
      	type: String,
      	default : ''
      },
	    isDisabled: {  // 按钮是否禁用
	      type: Boolean,
	      default: false
	    },
	  },
		data(){
			return {
				progress: {
	        tableFlag: false,
	        UploadPercent: 0
	     }
			}
		},
		methods:{
			importExcel(file) {
	      // 上传文件
	      let _this = this;
        let updata = null;
        try{
          updata = this.uploadUrl.upParam.call(this,file);
          if(this.$tu.isObjectBlank(updata)){
            this.$message.error("上传参数为空");
            this.loading = false;
            return;
          }
        }catch (err) {
          this.$message.error("整理上传参数错误" + err.message);
          this.loading = false;
          return;
        }
	      axios({
	        hostUrl: this.uploadUrl.hostUrl,
	        url: this.uploadUrl.url, //导入
	        method: "post",
          headers:{
            'Content-Type': 'multipart/form-data'
          },
	        data: updata,
	        onUploadProgress: function(progressEvent) {
	          // 进度条
	          if (progressEvent.lengthComputable) {
	            _this.progress.tableFlag = true;
	            _this.$nextTick(() => {
	              _this.progress.UploadPercent =
	                (progressEvent.loaded / progressEvent.total) * 100;
	              if (_this.progress.UploadPercent == 100)
	                setTimeout(() => {
	                  _this.progress.tableFlag = false;
	                }, 1000);
	            });
	          }
	        }
	      }).then(res => {
	          this.$nextTick(() => {
	            //上传成功回调
              this.uploadUrl.upSuccess.call(this,res);
              this.loading = false;
	          });
	        })
	        .catch(err => {
	          this.$message.error(err.message);
	          this.loading = false;
	        });
	    },
	    load(cab){
	    	cab()
	    }
		}
	}
</script>

<style scoped>
	#progress {
	  position: absolute;
	  top: 0;
	  width: 100%;
	}
	/deep/ .el-progress-bar__outer,
	/deep/ .el-progress-bar__inner {
	  border-radius: 0 !important;
	}
</style>
