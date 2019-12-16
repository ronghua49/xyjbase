<template>
  <div>


    <el-row style="text-align: left;margin-bottom: 10px">
      <el-button type="primary" class="newMachine" size="mini" @click="newYz">{{$t('action.versionSettings')}}
      </el-button>
    </el-row>
    <el-dialog v-dialogDrag title="版本设置" width="70%" @close="closeDialog" :visible.sync="dialogFormVisible">
      <el-form :model="form" ref="rulerform">
        <el-form-item label="app名称" prop="version">
          <el-input v-model="form.apknamme" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="app版本号" prop="version">
          <el-input v-model="form.version" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <el-row class="update-state">
        <el-row >
          <span>当前APP下载路径:</span>
        </el-row>
        <el-row class="update-url">
          <span>{{path}}</span>
        </el-row>
      </el-row>

      <el-row style="text-align: left;padding-top: 10px">
        <span>上传文件</span>
      </el-row>
      <el-upload
        class="upload-demo"
        ref="upload"
        :action=" url +'/FileUploadController/uploadFile'"
        :headers="header"
        :on-preview="handlePreview"
        :on-remove="handleRemove"
        :file-list="fileList"
        :limit="1"
        :on-success="success"
        :auto-upload="false">
        <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
        <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
        <div slot="tip" class="el-upload__tip">一次只能上传一个文件</div>
      </el-upload>
      <el-row class="update-state">
        <el-row class="state">
          <span >当前上传状态：</span>
          <span class="left" v-show="path==downloadpath||!downloadpath">未上传</span>
          <span class="right" v-show="path!==downloadpath&&downloadpath">已上传</span>
        </el-row>
        <el-row class="update-url" v-show="path!==downloadpath&&downloadpath">
          <span>{{downloadpath}}</span>
        </el-row>

      </el-row>


      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false" size="small">取 消</el-button>
        <el-button type="primary" size="small" @click="changeVersion">确 定</el-button>
      </div>
    </el-dialog>

    <div class="header">
      <div class="header-left">
        <span class="el-icon-tickets"></span>
        <span class="sx"></span>
        <span>APP版本信息</span>
      </div>
      <div class="header-right">
        <el-button @click="getVersion" class="fa fa-refresh" size="mini" type="primary">
          {{$t('action.refresh')}}
        </el-button>
      </div>
    </div>
    <el-table v-loading="loading" :data="tableData.content" border size="mini" style="width: 100%">
      <el-table-column type="index" align="center" label="编号" width="180">
      </el-table-column>
      <el-table-column prop="version" align="center" label="app版本号" min-width="180">
      </el-table-column>
      <el-table-column prop="downloadpath" align="center" label="app下载路径" min-width="180">
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
//import global from '@/utils/global'
  import Cookies from 'js-cookie'

  export default {
    name: "versionManagement",
    data() {
      return {
        loading: true,
        dialogFormVisible: false,
        form: {
          version: '',
          id: '',
          downloadpath: '',
          apknamme: ''
        },
        downloadpath:'',
        url: '',
        header: {},
        path: '',
        tableData: {},
        fileList: []
      }
    },
    mounted() {
      this.getVersion();
      this.getHeaders();
    },
    //销毁前，删除多余APP
    beforeDestroy(){
      if (this.path !== this.downloadpath) {
        console.log('销毁前删除');
        this.deleteApp(this.downloadpath);
      }
    },
    methods: {
      //  弹框关闭
      closeDialog() {

      },
      getHeaders() {
        let token = Cookies.get('token');
        this.header = {'token': token};
        this.url = this.global.uploadImgUrl;
      },
      newYz() {
        this.dialogFormVisible = true;
      },
      //更改版本号
      changeVersion() {
        let _this = this;
        if (!_this.form.downloadpath) {
          return this.$message.error('请上传APP文件');
        }
        _this.form.downloadpath = _this.downloadpath;
        _this.dialogFormVisible = false;
        _this.$api.common.xyjPost({
          url: '/appVersion/save',
          data: _this.form
        }).then(res => {
          console.log(res);
          this.downloadpath = '';
          //删除上个版本
          _this.deleteApp(_this.path);
          _this.getVersion();
          this.$message({
            message: '更改版本成功',
            type: 'success'
          });

        })
      },
      deleteApp(path) {
        console.log('删除多余文件');
        this.$api.LM.instance.post(this.global.uploadImgUrl + '/FileUploadController/deleteFile' + '?url='+path, {

        })
          .then(res => {
            console.log(res);

          })
      },
      success(response, file, fileList) {
        this.downloadpath = response;
        console.log('Form', this.form);
        console.log('tableData', this.tableData);

      },
      submitUpload() {
        this.$refs.upload.submit();
      },
      handleRemove(file, fileList) {
        console.log(file, fileList);
      },
      handlePreview(file) {
        console.log(file);
      },
      getVersion() {
        this.$api.common.xyjPost({
          url: '/appVersion/query',
          data: {}
        }).then(res => {
          this.loading = false;
          console.log(res);
          this.tableData = res.data;
          this.form = res.data.content[0];
          this.path = res.data.content[0].downloadpath;
        })
      }
    }
  }
</script>

<style scoped lang="scss">
  .update-state {
    text-align: left;
    .state {
      .left {
        color: #F56C6C;
      }
      .right {
        color: #67C23A;
      }
    }
  }
  .update-url{
    height: 38px;
    font-size: 14px;
    line-height: 38px;
    text-align: center;
    background-color: #F3F3F3;
  }
  .header {
    display: flex;
    height: 38px;
    font-size: 14px;
    background-color: #F3F3F3;
    justify-content: space-between;
    align-items: center;
  }

  .header-left {
    margin-left: 10px;
  }

  .header-right {
    margin-right: 10px;
  }
</style>
