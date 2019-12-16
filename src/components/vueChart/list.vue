<template>
	<div class="m-list">
		<footer>
			<input class="search" type="text" placeholder="搜索" v-model="search.text">
		</footer>
		<ul>
			<li v-for="(item,index) in userList.filter(x => x.name.indexOf(search.text) > -1)" :class="{ active: session.userId === item.id }" @click="select(item)" :key="index">
				<img class="avatar" width="30" height="30" :alt="item.name" :src="item.img">
				<p class="name">{{item.name}}</p>
			</li>
		</ul>
	</div>
</template>

<script>
	let vm = {}
	export default {
		props: ['userList', 'sessionIndex', 'session', 'search'],
		data() {
			vm = this;
			return {

			}
		},
		methods: {
			select(value) {
				this.sessionIndex.index = this.userList.indexOf(value);
			}
		},
	};
</script>

<style lang="scss" scoped>
	ul {
  margin: 0 !important;
  padding: 0 !important;
}
li {
  padding: 12px 15px;
  /*border-bottom: 1px solid #292c33;*/
  cursor: pointer;
  transition: background-color .1s;
  list-style: none;
  &:hover {
    background-color: rgba(88, 88, 88, 0.03);
  }
}
.active {
  background-color: rgba(88, 88, 88, 0.1);
}
.avatar,
.name {
  vertical-align: middle;
}
.avatar {
  border-radius: 2px;
}
.name {
  display: inline-block;
  margin: 0 0 0 15px;
}
footer {
  padding: 15.5px 12px;
  border-bottom: 1px solid #ccc;
}
.search {
  padding: 0 10px;
  width: 100%;
  font-size: 12px;
  color: #000;
  height: 30px;
  line-height: 30px;
  border: solid 1px #fafafa;
  border-radius: 4px;
  outline: none;
  background-color: #fafafa;
  box-sizing: border-box;
}
</style>