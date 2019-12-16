<template>

    <div @contextmenu.prevent="currOpenChange(item)" slot="reference" class="menu-wrapper">
      <div style="width: 140px;" class="column-item">{{item.name}}</div>
      <div style="width: 80px;" class="column-item"><i :class="item.icon"></i></div>
      <div style="width: 100px;" class="column-item">
        <el-tag v-if="item.type === 0" size="small">目录</el-tag>
        <el-tag v-else-if="item.type === 1" size="small" type="success">菜单</el-tag>
        <el-tag v-else-if="item.type === 2" size="small" type="info">按钮</el-tag>
      </div>
      <div style="width: 140px;" class="column-item">{{item.parentName}}</div>
      <el-tooltip class="item" effect="dark" :disabled="!item.url" :content="item.url" placement="top-start">
        <div style="width: 200px;" class="column-item">{{item.url}}</div>
      </el-tooltip>
      <div style="width: 140px;" class="column-item">{{item.perms}}</div>
      <div style="width: 50px;" class="column-item">{{item.orderNum}}</div>
      <el-row type="flex" style="z-index: 100">
        <kt-button ref="kButton" icon="fa fa-plus" :label="$t('action.add')" perms="sys:menu:add"
                   @click="handleCurrAdd(item)" :disabled="item.type === 2" :type="item.type === 0?'primary':''"/>
        <kt-button ref="kButton" icon="fa fa-edit" :label="$t('action.edit')" perms="sys:menu:edit"
                   @click="handleEdit(item)"/>
        <kt-button ref="kButton" icon="fa fa-trash" :label="$t('action.delete')" perms="sys:menu:delete"
                   type="danger" @click="handleDelete(item)"/>
      </el-row>
    </div>

</template>

<script>
  import KtButton from "@/views/Core/KtButton";

  export default {
    name: "menu-item",
    props: ["item", "rootData"],
    components: {
      KtButton,
    },
    data() {
      return {
        popoverVisible: false,
      }
    },
    methods: {
      change() {
        console.log("']]]]]]]]]]]]]]]]]");
        this.popoverVisible = true;
      },
      currOpenChange() {
        // this.popoverVisible = true;
      },
      handleCurrAdd(val){
        this.$bus.$emit("handleCurrAdd",val);
      },
      handleEdit(val) {
        this.$bus.$emit("handleEdit", val)
      },
      handleDelete(val) {
        this.$bus.$emit("handleDelete", val)
      },
      addBtns(val) {
        this.$bus.$emit("add-Btns", val)
      },
    }
  }
</script>

<style scoped lang="scss">
  .menu-wrapper {
    display: flex;
    /*background-color: #f0f0f0;*/
    width: 100%;
    align-items: center;
    z-index: 99;
    font-size: 12px;
  }

  .column-item {
    padding: 0 10px;
    height: 40px !important;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    line-height: 40px;
    text-align: left;
    /*display: flex;*/
    /*align-items: center;*/
  }
</style>
