<template>
  <Xyj-button style="padding: 7px;" type="primary" ref="downloadTemplate"
              @click="onDefaultDownload" :label="$t('action.downloadTemplate')" text="下载模板"/>

</template>

<script>
  import axios from "axios";
  import Cookies from "js-cookie";

  export default {
    name: "download",
    props: ["downloadUrl"],
    methods: {
      onDefaultDownload() {
        axios({
          methods:this.downloadUrl.method,
          url:this.downloadUrl.hostUrl + this.downloadUrl.url,
          headers: {
            'token': Cookies.get("token"),
            "content-type": "application/json;charset=UTF-8"
          },
          responseType: 'arraybuffer',
          params:{

          },
          data:{}
        }).then(response => {
          /*let blob = new Blob([res], {type: "application/vnd.ms-excel;charset=UTF-8"});
          let objectUrl = URL.createObjectURL(blob);
          window.location.href = objectUrl;*/
          let blob = new Blob([response.data])

          let fileName = decodeURI(response.headers["content-disposition"].split("filename=")[1]);

          var link = document.createElement('a');
          link.href = window.URL.createObjectURL(blob);
          link.download = fileName;
          link.click();
          //释放内存
          window.URL.revokeObjectURL(link.href)
          this.$refs.downloadTemplate.load = false;
        }).catch(res=>{
          this.$message({type:"error",message:res.message});
          this.$refs.downloadTemplate.load = false;
        })
       /* axios.post(this.downloadUrl.hostUrl + this.downloadUrl.url, {}, {
          headers: {
            'token': Cookies.get("token"),
            "content-type": "application/json;charset=UTF-8"
          },
          responseType: 'arraybuffer',
        }).then(response => {
          /!*let blob = new Blob([res], {type: "application/vnd.ms-excel;charset=UTF-8"});
          let objectUrl = URL.createObjectURL(blob);
          window.location.href = objectUrl;*!/
          let blob = new Blob([response.data])

          let fileName = decodeURI(response.headers["content-disposition"].split("filename=")[1]);

          var link = document.createElement('a');
          link.href = window.URL.createObjectURL(blob);
          link.download = fileName;
          link.click();
          //释放内存
          window.URL.revokeObjectURL(link.href)

        })*/
      }
    }
  }
</script>

<style scoped>

</style>
