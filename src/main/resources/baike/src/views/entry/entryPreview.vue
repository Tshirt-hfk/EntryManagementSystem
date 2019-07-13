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
			window.console.log("test")
			// 获取词条数据 
			this.$axios.get(
                "http://127.0.0.1:5000/fetchPageById",{params:{
					entryId:new Number(this.name)
				}}
            ).then(res => {
                if(res.data){
					window.console.log("test")
                    this.value=res.data.page_content
                } else {
                this.$message({
                    message:res.data.msg,
                    type:"warning"
                });
                }
            }).catch(error => {
                if(error.response){
                    this.$message({
                        message:error.response.data.msg,
                        type:"warning"
                    });
                }
            });
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


