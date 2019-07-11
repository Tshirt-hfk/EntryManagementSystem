<template>
<div style="margin:70px auto 0; width:40%;">
		<h2 class="title">专题信息</h2>
		<el-form ref="form" :model="form" label-width="80px">
			<el-form-item label="专题名称">
				<el-input v-model="form.name"></el-input>
			</el-form-item>
			<el-form-item label="专题领域">
				<el-select v-model="form.field" filterable placeholder="                                                                                                                                      ">
				<el-option
				v-for="field in fields"
				:key="field"
				:label="field"
				:value="field">
				</el-option>
			</el-select>
			</el-form-item>
			<el-form-item label="专题介绍">
				<el-input type="textarea" rows=4 v-model="form.introduction" maxlength="100" show-word-limit></el-input>
			</el-form-item>
			<el-form-item label="专题目标">
				<el-input type="textarea" rows=4 v-model="form.goal" maxlength="100" show-word-limit></el-input>
			</el-form-item>
			<el-form-item label="公开">
				<el-switch v-model="form.isPublic"></el-switch>
			</el-form-item> 
			<el-form-item>
				<el-row>
					<el-col :span="12">
						<div style="width:30%; margin: 0 auto">
						<el-button style="width:100%;" type="primary" @click="cancel">取消</el-button>
						</div>
					</el-col>
					<el-col :span="12">
						<div style="width:30%; margin: 0 auto">
						<el-button style="width:100%;" type="primary" @click="post">提交</el-button>
						</div>
					</el-col>
				</el-row>
			</el-form-item>
		</el-form>
</div>
	
</template>

<script>

import myAssignmentCard from "../components/myAssignmentCard"

export default {
	name:"subjectMaker",
	components:{
		myAssignmentCard
	},
	data(){
		return {		 
			active:1,
			form: {
				name: '',
				field: '',
				isPublic: false,
				introduction: '',
				goal:''
			},
			fields: [
				"科学",
			],
			count:5,
			assignments:[
				{
					id:1,
					name:"test1",
					state:false
				},
				{
					id:2,
					name:"test2",
					state:false
				},
				{
					id:3,
					name:"test3",
					state:false
				},
				{
					id:4,
					name:"test4",
					state:false
				},
				{
					id:5,
					name:"test4",
					state:false
				}
			]
		}
	},
	methods:{
		post(){
			this.$axios.post("http://localhost:8081/api/subjectmaker/subject/create",this.form)
				.then(res => {
					if(res.data.data){
						this.count = res.data.data.count
						this.assignments = res.data.data.assignments
						this.$route.push('/subjectmanage')
					}else{
						this.$message({
							message:result.data.msg
						});
					}
				})
				.catch(error => {
					if(error.response){
					this.$message({
						message:error.response.data.msg,
						type:"warning"
					});
					}
				})
		},
		cancel(){
			if(this.active==2){
				this.active=this.active-1
			}else{
				
			}
		}
	}

	
}
</script>

<style>
	.title{
		margin: 20px auto;
	}
	.el-row {
		margin-bottom: 10px;
	}
	.el-col {
		border-radius: 4px;
		margin-bottom: 10px;
	}
	.button {
		margin: 20px auto;
	}
</style>


