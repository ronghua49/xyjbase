<template>
  <div>
    <div class="list-header item-row">
      <p :style="{width:column.width + 'px'}" v-for="(column,ins) in columns" :key="ins">{{column['label']}}</p>
<!--      <p class="address">设备位置</p>-->
<!--      <p class="time">时间</p>-->
    </div>
    <div class="item-row" v-for="(item,index) in tableData">
      <p :style="{width:column.width + 'px'}" v-for="(column,ins) in columns" :key="ins">{{item[column['prop']]}}</p>
<!--      <p class="address">{{item[columns['position']]}}</p>-->
<!--      <p class="time">{{item[columns['date']]}}</p>-->
    </div>
  </div>
</template>

<script>
  export default {
    name: "home-list",
    props: {
      tableData: {
        type: Array,
        default: function () {
          return []
        }
      },
      columns:{
        type: Array,
        default: function () {
          return []
        }
      },
    },
    data() {
      return {

      }
    },
    methods: {

    }
  }
</script>

<style scoped lang="scss">
  .list-header{
    background:linear-gradient(-90deg,rgba(17,34,59,0.5),rgba(32,71,138,0.5),rgba(17,34,59,0.5));
    color: #CCCCCC!important;
    height: 50px;
  }
  .item-row {
    display: flex;
    align-items: center;
    color: #ffffff;
    font-size: 18px;
    & > p{
      margin: 4px;
    }
    .name{
      width: 150px;
    }
    .address{
      width: 150px;
    }
    .time{
      width: 280px;
    }
  }
</style>
