<template>
  <div class="usercenter-layout">
    <div class="uc-main-header">
      <!-- <div class="uc-base-info">
        <div class="uc-info-layout">
          <div class="uc-userinfo">
            <div class="uc-avatar">
              <img class="uc-portrait" title="userName"
              src="https://static.hdslb.com/images/member/noface.gif">
            </div>
            <div class="uc-name">
              <i class="uc-name-ellipsis">
                {{this.userName}}
              </i>
              <a href="" class="uc-setting">
                <img class="uc-setting-icon" src="https://wx3.sinaimg.cn/mw690/ad5be381gy1g52vj2rg7sj200f00f0ag.jpg">
              </a>
            </div>
          </div>
          <div class="uc-baikeinfo">
            <ul class="uc-lemma-info">
              <li>
                <a href="javascript:;">
                  <dl>
                    <dd>0</dd>
                    <dt>创建版本</dt>
                  </dl>
                </a>
              </li>
              <li>
                <a href="javascript:;">
                  <dl>
                    <dd>0</dd>
                    <dt>提交版本</dt>
                  </dl>
                </a>
              </li>
              <li>
                <a href="javascript:;">
                  <dl>
                    <dd>0</dd>
                    <dt>通过版本</dt>
                  </dl>
                </a>
              </li>
              <li style="margin-right: 0;margin-left: 25px;">通过率：<i style="font-style: normal;">0%</i></li>
              <li class="split">&nbsp;</li>
              <li style="margin-right: 0;">创建专题：<i style="font-style: normal;">0</i></li>
              <li class="split">&nbsp;</li>
              <li style="margin-right: 0;">加入专题：<i style="font-style: normal;">0</i></li>
              <div class="uc-lemmainfo-line"></div>
            </ul>
            <div class="uc-apply-info">
              <a href="" class="uc-btn-apply" target="_blank">申请专题制作人权限</a>
              <p style="font-style:normal;font-size: 10px; margin-top: 13px">获得众智化系统认证，享受更多权限!</p>
            </div>
          </div>
        </div>
      </div> -->
      <div class="uc-nav-tab" id="nav_tabs">
        <div class="uc-navtab-layout">
          <a @click="pageSelection(1)">我的专题</a>
          <a @click="pageSelection(2)">我的词条</a>
          <a @click="pageSelection(3)">推荐任务</a>
          <a @click="pageSelection(4)">我的收藏</a>
        </div>
      </div>
    </div>
    <div class="uc-body-wrapper">
      <!-- <div v-if="pageIndex == '1'">
        <mySubject></mySubject>
      </div>
      <div v-else-if="pageIndex == '2'">
        <myEntry></myEntry>
      </div>
      <div v-else-if="pageIndex == '3'">
        <myTask></myTask>
      </div>
      <div v-else-if="pageIndex == '4'">   
         待添加
      </div> -->
      <router-view/>
    </div>
  </div>
</template>


<script>

import myEntry from "./userCenter/myEntry"
import myTask from "./userCenter/myTask"
import mySubject from "./userCenter/mySubject"

export default {
  name: "userCenter",
  components:{
    myEntry,
    myTask,
    mySubject
  },
  data() {
    return {
      userName: this.$store.state.name,
      pageIndex: '1',
    };
  },
  methods: {
    pageSelection(page){
      if(page == '1')
        this.$router.push('/userCenter/mysubject/joinedsubject')
      else if(page == '2')
        this.$router.push('/userCenter/myentry')
      else if(page == '3')
        this.$router.push('/userCenter/mytask/recommendedentry')
    }
  }
};
</script>

<style scoped>
dd,dl,dt,ul,li{
  margin: 0;
  padding: 0;
}
.usercenter-layout {
  width: 100%;
  margin: 0 auto;
  margin-top: 2px;
  min-height:800px;
}
.uc-base-info{
  position: relative;
  width: 100%;
  height: 330px;
  padding-top: 70px;
  background: url(https://bkssl.bdimg.com/static/wiki-usercenter/widget/header/resource/img/baseInfoBg_ec29f86.png) center;
  text-align: center;
}
.uc-info-layout{
  position: relative;
  width: 980px;
  margin: 0 auto;
}
.uc-userinfo{
  display: inline-block;
  width: 150px;
  vertical-align: middle;
}
.uc-avatar{
  width: 144px;
  height: 144px;
  border: solid 3px #FFF;
  border-radius: 150px;
  position: relative;
}
.uc-portrait{
  position: absolute;
  left: 0;
  top: 0;
  border-radius: 150px;
  width: 144px;
  height: 144px;
}
.uc-name{
  margin-top: 10px;
  text-align: center;
}
.uc-name-ellipsis{
  display: inline-block;
  margin: 0 5px 0 0;
  max-width: 118px;
  font-size: 18px;
  color: #000;
  overflow: hidden;
  font-style: normal;
}
.uc-setting{
  position: relative;
  display: inline-block;
  border: 1px solid rgba(82,163,245,.5);
  width: 21px;
  height: 21px;
  border-radius: 20px;
}
.uc-setting-icon{
  display: inline-block;
  left: 0;
  top: 0;
  border-radius: 150px;
  width: 21px;
  height: 21px;
}
.uc-baikeinfo{
  display: inline-block;
  position: relative;
  margin-left: 45px;
  padding: 20px 35px;
  border-radius: 3px;
  width: 425px;
  background: rgba(255,255,255,.8);
  box-shadow: 0 1px 1px rgba(0,0,0,.22);
  vertical-align: middle;
  color: #666;
}
.uc-lemma-info{
  padding: 5px 0 15px;
  margin-right: -32px;
  font-size: 0;
  text-align: left;
  color: #888;
}
.uc-lemma-info li{
  display: inline-block;
  width: 124px;
  margin-right: 24px;
  font-size: 14px;
  text-align: center;
}
.uc-lemma-info li a{
  display: inline-block;
  margin-bottom: 13px;
  padding: 25px 0 15px;
  border: 2px solid #71b5fa;
  border-radius: 100px;
  outline: 0;
  width: 100px;
  height: 60px;
  background: #fff;
  text-decoration: none;
  transition: .1s linear;
  cursor: default;
}
.uc-lemma-info li a dl{
  text-align: center;
}
.uc-lemma-info li a dl dd{
  font-size: 39px;
  line-height: 38px;
  color: #333;
  text-decoration: none;
}
.uc-lemma-info li a dl dt{
  color: #888;
  font-size: 12px;
  line-height: 22px;
  text-decoration: none;
}
.uc-lemma-info li.split{
  margin-right: 0;
  width: 1px;
  height: 21px;
  background: #ddd;
}
.uc-lemmainfo-line{
  margin-top: 10px;
  width: 420px;
  border-bottom: 1px solid #ddd;
}
.uc-apply-info{
  padding: 15px 10px 0;
}
.uc-btn-apply{
  color: #fff;
  border: solid 1px #f18167;
  border-radius: 6px;
  background: #f18167;
  text-align: center;
  padding: 5px 5px 4px;
  font-size: 15px;
  text-decoration: none;
}
.uc-nav-tab{
  height: 49px;
  background: #52a3f5;
  border-bottom: solid 1px #4C96E2;
}
.uc-navtab-layout{
  width: 980px;
  margin: 0 auto;
}
.uc-nav-tab a{
  display: block;
  float: left;
  width: 24.8%;
  height: 50px;
  line-height: 50px;
  color: #f2f9ff;
  font-size: 21px;
  text-align: center;
  text-decoration: none;
  cursor: pointer;
}
.uc-nav-tab a:hover{
  background:rgba(255, 255, 255,0.1)
}
.uc-nav-tab-clicked{
  background: #0683d8;
  font-weight: bold;
  cursor: default;
}
.uc-body-wrapper{
  position: relative;
  overflow: hidden;
}
</style>
