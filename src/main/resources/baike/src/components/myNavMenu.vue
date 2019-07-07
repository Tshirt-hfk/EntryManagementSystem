<template>	
<div> 
<el-menu default-active="1" class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose">
  <el-submenu index="1">
    <template slot="title">
      <i class="el-icon-location"></i>
      <span slot="title">分类</span>
    </template>
    <div v-for="category in data.categoies" :key="category.id">
      <el-menu-item :index="'1-'+category.id" v-on:click="toCategory(category.id)">{{category.name}}</el-menu-item>
    </div>
  </el-submenu>
  <el-menu-item index="2">
    <i class="el-icon-menu"></i>
    <span slot="title">导航二</span>
  </el-menu-item>
  <el-menu-item index="3" disabled>
    <i class="el-icon-document"></i>
    <span slot="title">导航三</span>
  </el-menu-item>
  <el-menu-item index="4">
    <i class="el-icon-setting"></i>
    <span slot="title">导航四</span>
  </el-menu-item>
</el-menu>
</div>
</template>


<script>
  export default {
  	name:"myNavMenu",
    data() {
      return {
        data:{
          categoies:[
            {
              name:"艺术",
              id:1,
            },{
              name:"科学",
              id:2,
            },{
              name:"自然",
              id:3,
            }
          ]
        }
      };
    },
    mounted(){
        this.init()
    },
    methods: {
      init(){
          // 初始化，获取
          // this.$axios.get(  
          //       "http://localhost:8081/api/tourist/subdomain?id=0",
          //   ).then(res => {
          //       if(res.data.data){
          //           this.data.categoies = res.data.data.categoies
          //       } else {
          //       this.$message({
          //           message:res.data.msg,
          //           type:"warning"
          //       });
          //       }
          //   }).catch(error => {
          //       if(error.response){
          //           this.$message({
          //               message:error.response.data.msg,
          //               type:"warning"
          //           });
          //       }
          //   });
      },
      handleOpen(key, keyPath) {
        console.log(key, keyPath);
      },
      handleClose(key, keyPath) {
        console.log(key, keyPath);
      },
      toCategory(categoryId) {
        this.$router.push({path:'/category',query:{id:categoryId}})  
      }
    }
  }
</script>

<style>
  .el-menu-vertical-demo:not(.el-menu--collapse) {
    width: 200px;
    min-height: 400px;
  }
</style>