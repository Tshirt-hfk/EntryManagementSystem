<template>
    <div class="index-layout">
        <div class="index-searchbar">
            <div class="logo-headl">
                <a title="首页" href="/">
                <img src="https://wx3.sinaimg.cn/mw690/ad5be381gy1g51mw04nn2j206t01mgli.jpg">
                </a>
            </div>
            <div class="index-search">
                <mySearch></mySearch>
                <el-button type="primary" @click="search" style="margin-left: 10px">搜索词条</el-button>
            </div>
        </div>
        <div class="index-navbar">
            <el-menu class="el-menu-demo" mode="horizontal" @select="handleSelect" background-color="#459df5"
            text-color="#fff" active-text-color="#fff" :router="true"
            default-active="/" style="padding-left: 350px; width:100%;">
            <el-menu-item index="/" style="width: 150px; text-align: center;font-size: large;letter-spacing:12px;text-indent:10px">首页</el-menu-item>
            <el-submenu index="2" style="width : 150px; text-align: center;font-size: large;">
                <template slot="title"><span style="font-size: large;letter-spacing:12px;text-indent:20px">分 类</span></template>
                <el-menu-item index="2-1" style="padding-left: 20px">艺术</el-menu-item>
                <el-menu-item index="2-2" style="padding-left: 20px">科学</el-menu-item>
                <el-menu-item index="2-3" style="padding-left: 20px">自然</el-menu-item>
                <el-menu-item index="2-4" style="padding-left: 20px">文化</el-menu-item>
                <el-menu-item index="2-5" style="padding-left: 20px">地理</el-menu-item>
                <el-menu-item index="2-6" style="padding-left: 20px">生活</el-menu-item>
                <el-menu-item index="2-7" style="padding-left: 20px">社会</el-menu-item>
                <el-menu-item index="2-8" style="padding-left: 20px">人物</el-menu-item>
                <el-menu-item index="2-9" style="padding-left: 20px">经济</el-menu-item>
                <el-menu-item index="2-10" style="padding-left: 20px">体育</el-menu-item>
                <el-menu-item index="2-11" style="padding-left: 20px">历史</el-menu-item>
            </el-submenu>
            <el-menu-item index="3" style="width: 150px; text-align: center;font-size: large;letter-spacing:12px;text-indent:10px">专 题</el-menu-item>
            <el-menu-item index="4" style="width: 150px; text-align: center;font-size: large;letter-spacing:12px;text-indent:8px">订单管理</el-menu-item>
            <div class="line"> </div>
            <div class="index-navbar-usercenter">
                <a class="index-navbar-usercenter-link" href="/usercenter">
                <i class="el-icon-user-solid"> </i>
                    个 人 中 心
                </a>
            </div>
            </el-menu>
        </div>
    </div>
</template>

<script>

import mySearch from "../components/mySearch";


export default {
    name:'index',
    components:{
        mySearch
    },
    data(){
        return {
        }
    },
    mounted(){
        
    },
    methods:{
        search() {
        //TODO
        },
        handleSelect(key, keyPath) {
            console.log(key, keyPath);
        }
    }
}
</script>

<style>
.index-layout {
  width: 100%;
  margin: 0 auto;
  position: relative;
}
.index-searchbar{
  width: 808px;
  height: 105px;
  margin: 0 auto;
  position: relative;
  font-family: arial;
}
.logo-headl{
  padding: 25px 0 35px;
  float: left;
  line-height: 1;
}
.index-search{
  float: left;
  padding: 37px 0;
}
.index-navbar{
    position: relative;
    width: 1800px;
    height: 70px;
    margin-top: 18px;
}
.line{
    float: left;
    border-right: 1px solid #ddd;
    height: 50px;
    margin-top: 5px;
    padding-left: 350px;
}
.index-navbar-usercenter{
    float:left;
    width: 200px;
    margin-top: 15px;
    margin-left: 60px;
}
.index-navbar-usercenter-link{
    font-size: large;
    color: #f5f5f5;
    text-decoration: none;
}
</style>
