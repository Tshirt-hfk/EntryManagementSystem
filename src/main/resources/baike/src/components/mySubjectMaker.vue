<template>
<div style="margin:70px auto 0; width:40%;">
<el-form ref="form" :model="form" label-width="80px">
  <el-form-item label="专题名称">
    <el-input v-model="form.name"></el-input>
  </el-form-item>
  <el-form-item label="专题领域">
    <el-select v-model="form.field" filterable placeholder="请选择专题领域">
    <el-option
      v-for="item in fileds"
      :key="item.id"
      :label="item.name"
      :value="item.id">
    </el-option>
  </el-select>
  </el-form-item>
  <el-form-item label="专题介绍">
    <el-input type="textarea" rows=4 v-model="form.introduce" maxlength="100" show-word-limit></el-input>
  </el-form-item>
  <el-form-item label="公开">
    <el-switch v-model="form.isPublic"></el-switch>
  </el-form-item> 
  <el-form-item>
    <el-button type="primary" @click="onSubmit">立即创建</el-button>
    <el-button>取消</el-button>
  </el-form-item>
</el-form>
</div>
</template>



<script>
export default {
	name: "mySubjectMaker",
    data() {
      return {
		fileds:[
			{
				name:"科学",
				id:1
			}
		],
        form: {
          name: '',
          filed: '',
          isPublic: false,
          introduce: ''
        }
      }
	},
	mounted(){
        this.init()
    },
    methods: {
		init() {
			// TODO 初始化领域选项
		},
		onSubmit() {
			// TODO
			console.log('submit!');
		}
    }
}
</script>