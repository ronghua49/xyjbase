<template>
	<div>
		<el-dialog
			v-dialogDrag
		  :title="data.name"
		  :visible.sync="dialogVisible"
		  width="40%"
		  :modal="false">
		  <div v-loading="loading">
		  	<ul>
		  		<li v-for="(item,index) in historyList" :key="index">
		  			<img src="../../../build/logo.png" alt="" />
		  			<div>
		  				<span class="gray">{{item.id}}</span>
		  				<span>{{item.chatcontent}}</span>
		  			</div>
		  			<span class="gray">{{item.createTime}}</span>
		  		</li>
		  	</ul>
		  </div>
		</el-dialog>
	</div>
</template>

<script>
	export default{
		props:['data'],
		data(){
			return {
				dialogVisible:false,
				historyList:[],
				loading:false,
			}
		},
		methods:{
			open(){
				this.loading = true;
				this.dialogVisible = true;
				this.$api.im.HistoryQuery({
          querys: [
            {
              "hasInitValue": true,
              "operation": "EQUAL",
              "property": "conversationid",
              "relation": "AND",
              "value": this.data.conversationid
            }
          ],
          sorter:[
            {
              "property": "createTime",
              "direction": "DESC"
            }
          ]
        }).then((res)=>{
       	  this.loading = false
					this.historyList = res.data.rows;
        }).catch((error)=>{
        	this.loading = false
            this.$message.error(error.message);
        })
			}
		}
	}
</script>

<style lang="scss" scoped>
	ul {
		margin: 0;
		padding: 0;
		li{
			list-style: none;
			display: flex;
			justify-content: space-between;
			border-top: 1px solid #ccc;
			box-sizing: border-box;
			padding: 10px 0;
			img{
				width: 30px;
				height: 30px;
				vertical-align: top;
			}
			.gray{
				color: #ccc;
			}
			>div{
				width: 70%;
				text-align: left;
				span{
					display: block;
				}
			}
		}
	}
</style>