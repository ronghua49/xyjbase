<template>
  <el-button :size="size" :type="type" :icon="icon" class="XyjBtn" :data-perms="getPerms()"
    :loading="load" :disabled="!hasPerms(perms)" @click="handleClick" style="padding: 7px;">
    {{label}}
  </el-button>
</template>

<script>
import { hasPermission } from '@/permission/index.js'
export default {
  name: 'XyjButton',
  props: {
    label: {  // 按钮显示文本
      type: String,
      default: 'Button'
    },
    text: {  // 按钮显示文本
      type: String,
      default: 'Button'
    },
    icon: {  // 按钮显示图标
      type: String,
      default: ''
    },
    size: {  // 按钮尺寸
      type: String,
      default: 'mini'
    },
    type: {  // 按钮类型
      type: String,
      default: null
    },
    loading: {  // 按钮加载标识
      type: Boolean,
      default: false
    },
    disabled: {  // 按钮是否禁用
      type: Boolean,
      default: false
    },
    perms: {  // 按钮权限标识，外部使用者传入
      type: String,
      default: null
    },
    tableName: {
      type: String,
      default: ""
    }
  },
  data() {
    return {
      load: this.loading
    }
  },
  methods: {
    handleClick: function () {
      // 按钮操作处理函数
      this.load = true;
      this.$emit('click', ()=>{
      	this.load = false;
      });
    }, 
    hasPerms: function (perms) {
      // 根据权限标识和外部指示状态进行权限判断
      let data;
      if(this.tableName){
        // 如果是表格的按钮拼接上表名
        let val = this.$route.path.replace(/\//g,":").substr(1);
        data =  val+':'+this.tableName+this.text
      }else {
        data =  this.$route.path.replace(/\//g,":").substr(1)+':'+this.text;
      }
      //return !this.disabled;
      return hasPermission(data) & !this.disabled
    },
    getPerms(){
      if(this.tableName) { return this.tableName+this.text; }
      else { return this.text; }
    }
  },
  created(){
  	this.getPerms();
  },
  mounted() {
//  this.getPerms();
  }
}
</script>

<style scoped>

</style>
