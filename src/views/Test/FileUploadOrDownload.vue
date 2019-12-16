<template>
    <div class="file">
      <div>
        文件类型：<el-input v-model="type" placeholder="请输入上传文件类型"></el-input>
        <el-upload
          :http-request="importExcel"
          class="upload-demo"
          action=""
          multiple
          :show-file-list="false">
          <Xyj-button type="primary" @click="(cb)=>{cb()}" :label="$t('action.Import')" text="上传" />
        </el-upload>
      </div>
      <div>
        下载路径：<el-input v-model="path" placeholder="请输入下载路径"></el-input>
        <Xyj-button type="primary" @click="downLoad" :label="$t('action.download')" text="下载" />
      </div>
    </div>
</template>

<script>
    import axios from "../../http/axios";

    export default {
        name: "FileUploadOrDownload",
      data(){
          return{
            type:"",
            path:""
          }
      },
      methods:{
        importExcel(file){
          let then = this;
          let fd = new FormData();
          fd.append("file",file.file);
          fd.append("type",then.type);
          axios({
            hostUrl:then.global.hostUrl,
            url:"/FileUploadController/uploadFileType",
            method:"post",
            data:fd
          }).then(res =>{
            then.$confirm(res, '上传成功', {
              confirmButtonText: '确定',
              type: 'warning'
            })
          }).catch(err =>{
            then.$message.error(err.message)
          })
        },
        downLoad(cab){
          let then = this;
          let fd = new FormData();
          fd.append("path",then.path);
          axios({
            hostUrl: then.global.hostUrl,
            url:"/FileUploadController/downLoadFile",
            method: "post",
            data:fd,
            responseType: "blob"
          }).then(res =>{
            // 处理返回的文件流
            const blob = new Blob([res.data], {
              type: "application/vnd.ms-excel;charset=utf-8"
            });
            if ("download" in document.createElement("a")) {
              // 非IE下载
              const elink = document.createElement("a");
              elink.download = decodeURIComponent(res.filename);
              elink.style.display = "none";
              elink.href = URL.createObjectURL(blob);
              document.body.appendChild(elink);
              elink.click();
              URL.revokeObjectURL(elink.href); // 释放URL 对象
              document.body.removeChild(elink);
            } else {
              // IE10+下载
              navigator.msSaveBlob(blob, decodeURIComponent(res.filename));
            }
            cab();
          }).catch(err =>{
          	cab();
            this.$message.error(err.message);
          })
        }
      }
    }
</script>

<style scoped>
  .file{
    width: 100%;
  }
  .file>div{
    display: flex;
    justify-content: center;
    align-items: center;
    margin-bottom: 20px;
  }
.el-input{
  width: 200px;
  margin: 0 10px;
}
</style>
