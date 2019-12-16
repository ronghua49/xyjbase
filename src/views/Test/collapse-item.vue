<template>
  <div v-if="tableTreeDdata.length > 0" class="myself-menu-wrapper">
    <el-collapse v-model="activeNames" @change="handleChange">
      <div style="margin-bottom: 1px;" class="myself-menu-wrapper-list" v-for="(item,index) in tableTreeDdata" >
        <el-collapse-item  :key="index" v-if="item.children.length > 0" :name="item.id">
          <template slot="title">
            <menu-item :rootData="rootData" :item="item">

            </menu-item>
          </template>
          <div v-if="tableTreeDdata.length > 0 &&item.children.length > 0" class="my}}}}}">
            <collapse-list style="padding-right: 20px;" :tableTreeDdata="item.children">

            </collapse-list>
          </div>
          <span v-else>||||||||||||||||||||||||||||||</span>
          <!--<menu-item style="padding-right: 20px;" v-if="item.children.length > 0" :item="item.children">

          </menu-item>-->
        </el-collapse-item>

        <menu-item :rootData="rootData" style="padding-right: 20px;" v-else :item="item">

        </menu-item>
      </div>

    </el-collapse>

  </div>
</template>

<script>
  import menuItem from "./menu-item"
  import collapseList from "./collapse-item"

  export default {
    name: "collapse-list",
    props: ["tableTreeDdata","rootData"],
    components: {
      collapseList,
      menuItem,
    },
    data() {
      return {
        activeNames: [],
      }
    },
    methods: {
      handleChange(val) {
        console.log(val);
      },
    }
  }
</script>

<style lang="scss">
.myself-menu-wrapper{
  .el-collapse-item__wrap{
    border-bottom-color: transparent;
  }
  .el-collapse-item__header{
    height: 40px;
    /*border-bottom: none;*/
  }
  .el-collapse-item__content{
    padding-bottom: 0;
  }
}
</style>
