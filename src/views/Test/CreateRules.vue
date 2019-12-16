<template>
    <div class="main">
      <div class="rule">
        <el-form>
          <el-form-item label="根据规则ID生成：" label-width="140px">
            <el-input v-model="id" placeholder="请输入"></el-input>
            <Xyj-button type="primary" @click="createRuleNo" :label="$t('action.search')" text="查询" />
          </el-form-item>
          <el-form-item label="根据规则类型生成：" label-width="140px">
            <el-input v-model="type" type="text" placeholder="请输入"></el-input>
            <Xyj-button type="primary" @click="createRuleNoByType" :label="$t('action.search')" text="查询" />
          </el-form-item>
        </el-form>
      </div>
    </div>
</template>

<script>
  import axios from "@/http/axios";
    export default {
        name: "CreateRules",
      data(){
        return{
          id:"",
          type:""
        }
      },
      methods:{
        createRuleNo :function(cab){
          this.$api.common.createRuleNo(
            {id: this.id}
          ).then((res) =>{
            this.$message.info(res.msg);
            cab()
          }).catch((err) =>{
            this.$message.error(err.message);
            cab()
          });
        },
        createRuleNoByType:function (cab) {
          this.$api.common.createRuleNoByType(
            {type:this.type}
          ).then((res) => {
            this.$message.info(res.msg);
            cab()
          }).catch((err)=>{
            this.$message.error(err.message);
            cab()
          });
        }
      }
    }
</script>

<style scoped lang="scss">
  .main{
    width: 100%;
  }
.rule{
  width: 500px;
  margin-left: 30%;
}
  .el-form{
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    .el-form-item{
     width: 100%;
      .el-input{
        width: 80%;
      }
    }
  }
</style>
