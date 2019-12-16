<template>
	<div>
		<div class="uploadBorder" v-if="isShow">
			<img src="../../../build/logo.png" alt="" />
			<div class="uploadTitle">
				<span>{{file.name}}</span>
				<span v-if="file.size">{{(file.size/1024).toFixed(1)}}K</span>
			</div>
		</div>
		<div class="afterUpload" v-else v-html="content"></div>
	</div>
</template>

<script>
	export default{
		props:{
			file:{
				
			},
			isShow:{
				
			},
			content:{
				
			},
			show:{
				type:Boolean,
				default:false
			}
		},
		data(){
			return {
				
			}
		},
		watch:{
			file(val){
				console.log(val)
			}
		}
	}
</script>

<style lang="scss" scoped>
	.uploadBorder {
  position: absolute;
  left: -999px;
  width: 270px;
  height: 47px;
  padding: 5px 10px;
  box-sizing: border-box;
  border: 1px solid #ccc;
  img {
    width: 30px;
    height: 35px;
    vertical-align: sub;
  }
  .uploadTitle {
    display: inline-block;
    font-size: 12px;
    color: #000;
    span {
      display: block;
    }
  }
}
.afterUpload {
  .uploadBorder {
    position: static;
    width: 220px;
    height: 65px;
    display: flex;
    background: #fafafa;
    justify-content: space-around;
    align-items: center;
    border: none;
    img {
      width: 30px;
      height: 35px;
      vertical-align: sub;
      order: 2;
    }
    .uploadTitle {
      display: inline-block;
      font-size: 12px;
      color: #000;
      order: 1;
      span {
        display: block;
      }
    }
  }
}

</style>