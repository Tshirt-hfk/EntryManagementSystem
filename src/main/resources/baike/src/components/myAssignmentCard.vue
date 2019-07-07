<template>
	<el-card class="box-card">
	<div slot="header" class="clearfix">
		<span>{{name}}</span>
		<el-button style="float: right; padding: 0px 0" v-bind:icon="icon" @click="handle(state)" autofocus="true"></el-button>
	</div>
	<div v-for="reason in reasons" :key="reason" class="text item">
		{{reason}}
	</div>
	</el-card>
</template>

<script>

export default {
	name:"myAssignmentCard",
	props:["assignment"],
	data() {
		return {
			name:"",
			reasons:[],
			state:false,
			icon:"el-icon-close"
		}
	},
	mounted() {
		if(this.assignment.name){
			this.name = this.assignment.name
		}
		if(this.assignment.reasons && this.assignment.reasons instanceof Array){
			this.reasons = this.assignment.reasons
		}
		if(this.assignment.state){
			this.state = true
			this.icon = "el-icon-check"
		}else{
			this.state = false
			this.icon = "el-icon-close"
		}
	},
	methods: {
		handle(state){
			if(!state){
				this.state = true
				this.icon = "el-icon-check"
			}else{
				this.state = false
				this.icon = "el-icon-close"
			}
		}
	}

}
</script>



<style>
  .text {
    font-size: 14px;
  }

  .item {
    margin-bottom: 18px;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }
  .clearfix:after {
    clear: both
  }

  .box-card {
    width: 180px;
	height: 180px;
  }
</style>