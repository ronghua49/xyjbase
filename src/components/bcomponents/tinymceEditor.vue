<template>
  <textarea :id='id' :value='value'></textarea>
</template>

<script>
  // Import TinyMCE
  import tinymce from 'tinymce/tinymce'
  import 'tinymce/themes/silver/theme'
  import 'tinymce/skins/ui/oxide/skin.css'
  import 'tinymce/plugins/advlist' // 这几条引入是因为我的导入不了，不知道为啥
  import 'tinymce/plugins/link'
  import 'tinymce/plugins/image'
  import 'tinymce/plugins/code'
  import 'tinymce/plugins/table'
  import 'tinymce/plugins/textcolor'
  import 'tinymce/plugins/paste'
  import 'tinymce/plugins/textcolor'
  import 'tinymce/plugins/colorpicker'
  const INIT = 0
  const CHANGED = 2
    export default {
        name: "tinymceEditor",
      props: {
        value: {//编辑器输入的内容
          type: String,
          editor: null,
          required: true
        },
        setting: {},//编辑器宽高等css设置
      },
      watch: {
        value: function(val) {
          console.log('init ' + val)
          if (this.status === INIT || tinymce.activeEditor.getContent() !== val) {
            tinymce.activeEditor.setContent(val)
          }
          this.status = CHANGED
        }
      },
      data() {
        return {
          status: INIT,
          id: 'editor-' + new Date().getMilliseconds()
        }
      },
      methods: {
      },
      mounted() {
        const _this = this;
        //初始化
        const setting =
          {
            selector: '#' + _this.id,
            language_url: '../../../static/tinymce/zh_CN.js',// 配置语言的路由
            language: 'zh_CN',
            init_instance_callback: function(editor) {
              // EDITOR = editor
              console.log('Editor: ' + editor.id + ' is now initialized.')
              editor.on('input change undo redo', () => {
                var content = editor.getContent()
                _this.$emit('show', content)
              })
            },
            //编辑器的css样式
            content_style: `
    *                         { padding:0; margin:0; }
    html, body                { height:100%; }
    img                       { max-width:100%; display:block;height:auto; }
    a                         { text-decoration: none; }
    iframe                    { width: 100%; }
    p                         { line-height:1.6; margin: 0px; }
    table                     { word-wrap:break-word; word-break:break-all; max-width:100%; border:none; border-color:#999; }
    .mce-object-iframe        { width:100%; box-sizing:border-box; margin:0; padding:0; }
    ul,ol                     { list-style-position:inside; }
  `,
            insert_button_items: 'image link | inserttable',
            paste_retain_style_properties: 'all',
            paste_word_valid_elements: '*[*]', // word需要它
            paste_data_images: true, // 粘贴的同时能把内容里的图片自动上传，非常强力的功能
            paste_convert_word_fake_lists: false, // 插入word文档需要该属性
            paste_webkit_styles: 'all',
            paste_merge_formats: true,
            nonbreaking_force_tab: false,
            paste_auto_cleanup_on_paste: false,
            'plugins': [
              /*'advlist link image', 'code',*/
              'advlist','code',
              'table textcolor paste textcolor colorpicker'
            ], // 配置:编辑器工具栏显示的内容
            'toolbar_items_size': 'small',
            'block_formats': 'Paragraph=p;Heading 1=h1;Heading 2=h2;Heading 3=h3;Heading 4=h4;Heading 5=h5;Heading 6=h6;',
            /*'toolbar1': 'table |insertfile undo redo | formatselect | link unlink | uploadimg image media', // 工具栏1*/
            'toolbar2': ' fontsizeselect | forecolor backcolor | fontselect bold italic underline strikethrough | alignleft aligncenter alignright alignjustify | outdent indent | removeformat', // 工具栏2
          }
        Object.assign(setting, _this.setting)
        //调用初始化方法
        tinymce.init(setting)
      },
    }
</script>

<style scoped>

</style>
