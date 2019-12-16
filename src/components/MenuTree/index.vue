<template>
  <el-submenu v-if="menu.children && menu.children.length >= 1" :index="menu.id + ''">
    <template slot="title">
      <i :class="menu.icon" ></i>
      <span slot="title">{{menu.name}}</span>
    </template>
    <MenuTree v-for="item in menu.children" :key="item.id" :menu="item"></MenuTree>
  </el-submenu>
  <el-menu-item v-else :index="menu.id + ''" @click="handleRoute(menu)">
    <i :class="menu.icon"></i>
    <span slot="title">{{menu.name}}</span>
  </el-menu-item>
</template>

<script>
import { getIFrameUrl, getIFramePath } from '@/utils/iframe'
export default {
  name: 'MenuTree',
  props: {
    menu: {
      type: Object,
      required: true
    }
  },
  methods: {
    handleRoute (menu) {
      // 添加需要缓存的组件
      this.$store.state.tab.key.push(menu.url.split('/')[menu.url.split('/').length-1])
      // 缓存数组去重
      let arr = Array.from(new Set(this.$store.state.tab.key))
      // 更改vuex中的数组
      this.$store.commit('updateKey',arr);
      // 如果是嵌套页面，转换成iframe的path
      let path = getIFramePath(menu.url)
      if(!path) {
        path = menu.url
      }
      // 通过菜单URL跳转至指定路由
      this.$router.push("/" + path);
      /**
       *宋建国：2019/11/26 10:59
       *描述：设置接收消息的当前激活窗口路由变量
       *版本：1.0
       */
      this.$mqtt.setCurrRoute("/" + path);
    }
  }
}
</script>

<style scoped lang="scss">
// .el-submenu, .el-menu-item {
//   background-color: #3c4b5a44;
// }
</style>
