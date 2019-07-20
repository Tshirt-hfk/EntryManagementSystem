<template>
    <div class="index-layout">
        <div class="index-searchbar">
            <div class="logo-headl">
                <a title="首页" href="/">
                <img src="https://wx3.sinaimg.cn/mw690/ad5be381gy1g51mw04nn2j206t01mgli.jpg">
                </a>
            </div>
            <div class="index-search">
                <mySearch style="width: 440px;"></mySearch>
                <el-button type="primary" @click="search" style="margin-left: 10px">搜索词条</el-button>
            </div>
        </div>
        <div class="index-navbar">
            <div class = "index-navbar-menu">
                <el-menu class="el-menu-demo" mode="horizontal" @select="handleSelect" background-color="#459df5"
                text-color="#fff" active-text-color="#fff" :router="true"
                default-active="/" style="float: left;">
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
                </el-menu>
                <div class="index-navbar-usercenter">
                    <div class="line"> </div>
                    <a class="index-navbar-usercenter-link" @click="toUserCenter">
                    <i class="el-icon-user-solid"> </i>
                        个 人 中 心
                    </a>
                </div>
            </div>
        </div>
        <div class="index-main">
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
        },
        toUserCenter(){
            this.$router.push("/usercenter/mysubject");
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
    background-color: #459df5;
    width: 100%;
    height: 61px;
    margin-top: 18px;
}
.index-navbar-menu{
  margin: 0 auto;
  width: 1100px;
  height: 61px;
}
.line{
    border-right: 1px solid #ddd;
    height: 50px;
    float: left;
    margin-top: 6px;
}
.index-navbar-usercenter{
    height: 61px;
    float: right;
}
.index-navbar-usercenter-link{
    font-size: large;
    color: #f5f5f5;
    text-decoration: none;
    cursor: pointer;
    width: 200px;
    float: left;
    margin-top: 17px;
    margin-left: 35px;
}
.index-main{
  width: 80%;
  margin: 0 auto;
  min-height: 700px;
  margin-top: 10px;
}
</style>
