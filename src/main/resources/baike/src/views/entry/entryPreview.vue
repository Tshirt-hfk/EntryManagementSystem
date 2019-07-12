<template>
<div class="content">
	<mavon-editor :editable="false" :toolbarsFlag="false" :subfield="false" defaultOpen="preview" v-model="value"/>
</div>
</template>

<script>
export default {
	name:"entryPreview",
	data(){
		return {
			name:this.$route.query.name,
			value:""
		}
	},
	mounted(){
		this.init()
	},
	methods:{
		init(){
			// 获取词条数据 
		}
	}
}
</script>

<style>
	.content {
		width: 60%;
		margin: 20px auto;
	}
</style>


