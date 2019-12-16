<template>
  <div class="app-container calendar-list-container">
    <div style="margin:0 5%; width: 90%;">
      <editor
        class="editor"
        :value="content"
        :setting="editorSetting"
        @show="editors"></editor>
    </div>
    <div style="width: 500px; height: 100px; border: 1px solid red">{{content}}</div>
  </div>

</template>

<script>
  import editor from '@/components/bcomponents/tinymceEditor' // 根据editor.vue组件位置引入
    export default {
        name: "TinymceEditorTest",
      components: {editor}, // 引入组件
      data(){
          return{
            editorSetting: { // 配置富文本编辑器高
              height: 300
            },
            content: '', // 富文本编辑器双向绑定的内容
          }
      },
      methods: {
        editors(content) { // editor组件传过来的值赋给content
          console.log(content)
          this.content = content
        },
      }
    }
</script>

<style scoped>

</style>
