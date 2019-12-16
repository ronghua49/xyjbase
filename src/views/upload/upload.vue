<template>
  <el-upload
    class="upload-demo"
    ref="upload"
    :headers="{token:token}"
    :action="uploadUrl.hostUrl + uploadUrl.url"
    :on-preview="handlePreview"
    :on-remove="handleRemove"
    :show-file-list="false"
    :before-upload="beforeUpload"
    :on-progress="uploadProcess"
    :on-success="success"
    :on-error="error"
    :on-exceed="handleExceed">
    <div class="upLoadBox">
      <el-button class="upLoadButton" size="mini" type="primary">{{uploadUrl.content}}</el-button>
      <el-progress v-show="active" class="progress" :text-inside="true" :stroke-width="5" :show-text="false"
                   status="success" :percentage="percent"></el-progress>
    </div>
  </el-upload>
</template>

<script>
  import Cookies from "js-cookie"

  export default {
    name: "shangchuan1",
    props: ["uploadUrl"],
    data() {
      return {
        token: "",
        percent: 0,
        active: false,
      }
    },
    mounted() {
      this.token = Cookies.get("token");
    },
    methods: {
      handleExceed() {

      },
      beforeUpload() {
        this.active = true;
      },
      handlePreview() {

      },
      handleRemove() {

      },
      uploadProcess(event, file, fileList) {
        this.percent = Math.floor(event.percent);
      },
      success(response, file, fileList) {
        this.$message({type: "success", message: response.msg});
        this.$emit("uploadSuccess", response);
        setTimeout(() => {
          this.active = false;
        }, 100)
      },
      error(err, file, fileList) {
        this.$message({type: "error", message: err.message});
        this.$emit("uploadError", err);
        setTimeout(() => {
          this.active = false;
        }, 100)
      }
    }
  }
</script>

<style scoped lang="scss">
  .upLoadBox {
    display: flex;
    align-items: flex-end;
    position: relative;

    .upLoadButton {
      z-index: 8;
    }

    .progress {
      width: 100%;
      z-index: 9;
      position: absolute;
      left: 0;
    }
  }

</style>
