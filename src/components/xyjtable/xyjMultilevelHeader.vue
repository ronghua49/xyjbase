<template>
    <el-table-column v-bind="column" >
      <template v-for="(item,index) in getChildren">
        <xyj-multilevel-header v-if="item.children && item.children.length" :key='item.arrange' :column="item"></xyj-multilevel-header>
        <el-table-column v-else-if="item.isAllowSearch !== 2" :key=item.id v-bind="item" sortable = "custom"></el-table-column>
      </template>
    </el-table-column>
</template>

<script>
  export default {
    name: 'xyjMultilevelHeader',
    props: {
      column: {
        type: Object,
        default(){
          return {
            resizable: true,//对应列是否可以通过拖动改变宽度（需要在 el-table 上设置 border 属性为真）
            minWidth: '10px',//列最小宽度
            showOerflowTooltip: true, //当内容过长被隐藏时显示 tooltip
            headerAlign: 'center',//表头对齐方式，若不设置该项，则使用表格的对齐方式
            children: [] //子列头
          }
        }
      }
    },
    computed:{
      getChildren(){
        return this.column.children.sort((a,b) => {
          return a.arrange-b.arrange
        })
      }
    }
  }
</script>

<style scoped>

</style>
