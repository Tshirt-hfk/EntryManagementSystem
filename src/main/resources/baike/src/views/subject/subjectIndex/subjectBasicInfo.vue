<template>
  <div class="layout">
    <div class="taskIntro">
      <div class="cmn-clearfix">
        <div class="taskPic">
          <img :src="basicInfo.imageUrl" width="257" height="154" alt="任务详情图片" />
        </div>
        <div class="fl">
          <div class="mb30">
            <h1 class="fwn di">{{basicInfo.title}}</h1>
          </div>
          <div class="cmn-clearfix">
            <div class="joinRequirement">
              <div class="boxInfo">
                <div class="box f16">{{basicInfo.isPublic | isPublicHandler}}</div>
                <div class="btmDesc">参加条件</div>
              </div>
              <div class="boxInfo">
                <div class="box pr">
                  <div class="hasCompleted">{{basicInfo.currentCount}}</div>
                  <div class="totalNum">
                    <span class="count">{{basicInfo.totalCount}}</span>
                  </div>
                </div>
                <div class="btmDesc">完成词条</div>
              </div>
              <div class="boxInfo">
                <div class="box">{{basicInfo.myCompletedCount}}</div>
                <div class="btmDesc">我的进度</div>
              </div>
            </div>

            <div class="createInfo">
              <p class="restTime">剩余时间：{{remainingTime()}}</p>
              <p>
                任务发起：
                <a href target="_blank" class="createMan">{{basicInfo.creator}}</a>
              </p>
              <p class="deadline">结束时间：{{basicInfo.deadline | timeHandler}}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="taskDesc">
      <el-tabs type="border-card">
        <el-tab-pane>
          <span slot="label">
            <div class="descButton">任务介绍</div>
          </span>
          <div class="descDetail">
            <div class="descContent">{{basicInfo.introduction}}</div>
          </div>
        </el-tab-pane>
        <el-tab-pane>
          <span slot="label">
            <div class="descButton">任务目标</div>
          </span>
          <div class="descDetail">
            <div class="descContent">{{basicInfo.goal}}</div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import { setInterval } from 'timers';
export default {
  name: "subjectBasicInfo",
  props:['subjectId'],
  data() {
    return {
      basicInfo: {
        imageUrl:
          "https://gss0.bdstatic.com/94o3dSag_xI4khGkpoWK1HF6hhy/baike/crop%3D0%2C0%2C460%2C275%3BeWH%3D257%2C154/sign=a13a9483bd19ebc4d4372cd9bf16e3cc/37d12f2eb9389b5097c94bb48b35e5dde7116e26.jpg",
        title: "",
        creator: "test",
        isPublic: true,
        currentCount: 50,
        totalCount: 100,
        myCompletedCount: 10,
        deadline: 1563085500233,
        introduction: "introduction",
        goal: "goal"
	  },
	
    };
  },
  mounted: function() {
	  //this.init()
  },
  methods: {
	  init(){
		this.$axios.push("http://localhost:8081/api/user/getSubject/basicInfo",{
			subjectId:subjectId,
		}).then(res => {
          if (res.data) {
            this.$message({
              message: res.data.msg
            });
            this.$router.push("/subjectmakercenter/mysubject");
          }
        }).catch(error => {
          if (error.response) {
            this.$message({
              message: error.response.data.msg,
              type: "warning"
            });
          }
        });
		setInterval(() => {

		})
	  },
	  remainingTime() {
		var now = Date.now();
		var rt = parseInt((this.basicInfo.deadline - now)/1000);
		var day = parseInt(rt/(24*3600))
		var hour = parseInt(rt%(24*3600)/3600)
		var min = parseInt(rt%3600/60)
		var sec = parseInt(rt%60)
		return day + "天" + hour + "时" + min + "分" + sec + "秒"
	  }
  },
  filters: {
    isPublicHandler(isPublic) {
      if (isPublic) {
        return "所有人";
      } else {
        return "受限";
      }
    },
    timeHandler(t) {
      var d = new Date(t);
      return (
        d.getFullYear() +
        "年" +
        (d.getMonth() + 1) +
        "月" +
        d.getDate() +
        "日" +
        d.getHours() +
        "点" +
        d.getMinutes() +
        "分"
      );
	}
  }
};
</script>

<style>
/*! CSS Used from: https://bkssl.bdimg.com/static/wiki-common/pkg/wiki-common-base_66a9374.css */
h1,
p {
  margin: 0;
  padding: 0;
}
a {
  color: #338de6;
  text-decoration: none;
}
a:focus {
  outline: thin dotted;
}
a:active,
a:hover {
  outline: 0;
}
a:hover {
  text-decoration: underline;
}
h1 {
  font-size: 2em;
}
img {
  border: 0;
  -ms-interpolation-mode: bicubic;
}
.cmn-clearfix {
  *zoom: 1;
}
.cmn-clearfix:after {
  content: "\0020";
  display: block;
  height: 0;
  font-size: 0;
  clear: both;
  overflow: hidden;
  visibility: hidden;
}
h1 {
  color: #333;
}
/*! CSS Used from: https://bkssl.bdimg.com/static/wiki-task/taskBase/taskBase_9b28203.css */
a {
  color: #459df5;
}
a:focus {
  outline: 0;
}
.di {
  display: inline;
}
.f16 {
  font-size: 16px;
}
.fl {
  float: left;
}
.fwn {
  font-weight: 400;
}
.mb30 {
  margin-bottom: 30px;
}
.mc15 {
  margin-left: 15px;
}
.mc15 {
  margin-right: 15px;
}
.pr {
  position: relative;
}
.layout {
  width: 980px;
  margin: 0 auto;
}
.wNum-yellow {
  color: #ff9600;
  font-size: 24px;
}
.wNum-yellow:before {
  content: "\e600";
  font-family: baikeFont_taskIcon;
  font-size: 17px;
  vertical-align: middle;
  margin-right: 5px;
}
/*! CSS Used from: https://bkssl.bdimg.com/static/wiki-task/taskInfo-c2c/taskInfo-c2c_bb8c671.css */
a:hover {
  text-decoration: none;
}
.task-header {
  background: #f5f8fa;
}
.taskIntro {
  padding-top: 40px;
}
.taskIntro .taskPic {
  float: left;
  margin-right: 35px;
}
.taskIntro .createMan {
  color: #333;
}
.taskIntro .createMan:hover {
  color: #459df5;
}
.taskIntro .joinRequirement {
  float: left;
  color: #fff;
  margin-right: 35px;
}
.taskIntro .joinRequirement .boxInfo {
  float: left;
  width: 68px;
  margin-right: 4px;
  text-align: center;
}
.taskIntro .joinRequirement .box {
  width: 100%;
  height: 50px;
  font-size: 16px;
  line-height: 50px;
  border-radius: 6px;
  overflow: hidden;
  background-color: #74beef;
}
.taskIntro .joinRequirement .hasCompleted {
  margin-top: 1px;
  line-height: 1.5;
  overflow: hidden;
  width: 100%;
}
.taskIntro .joinRequirement .totalNum {
  line-height: 1.5;
  overflow: hidden;
}
.taskIntro .joinRequirement .totalNum .count {
  display: inline-block;
  border-top: 1px solid #fff;
}
.taskIntro .joinRequirement .btmDesc {
  color: #333;
  font-size: 14px;
  margin-top: 2px;
}
.taskIntro .createInfo {
  float: left;
  font-size: 16px;
  color: #666;
}
.taskIntro .createInfo .num {
  color: #398fe3;
  font-size: 18px;
}
.taskIntro .createInfo .num:first-child {
  margin-left: -7px;
}
.taskIntro .createInfo p {
  line-height: 27px;
}
/*! CSS Used from: https://bkssl.bdimg.com/static/wiki-task/widget/taskInfo/taskDesc/taskDesc_534c81a.css */
.taskDesc {
  font-size: 0;
  margin-top: 18px;
  position: relative;
}
.taskDesc .descButton {
  display: inline-block;
  position: relative;
  z-index: 2;
  font-size: 18px;
  padding: 0px 24px;
}
.taskDesc .descSelected {
  top: 1px;
  color: #333;
  background-color: #fff;
  border-top: 2px solid #459df5;
}
.taskDesc .wrap {
  position: relative;
  z-index: 1;
  color: #686a6a;
  padding: 0 30px;
  background-color: #fff;
  border: 1px solid #e2e7ea;
}
.taskDesc .title {
  text-align: center;
  font-size: 18px;
  line-height: 36px;
  border-bottom: 1px solid #e2e7ea;
}
.taskDesc .descContent {
  height: 138px;
  font-size: 14px;
  line-height: 24px;
  padding: 5px 0;
  white-space: pre-line;
  overflow: auto;
}
.taskDesc .descDetail {
  display: none;
}
.taskDesc .descDetail:first-child {
  display: block;
}
.descContent::-webkit-scrollbar-button:disabled {
  display: none !important;
}
.descContent::-webkit-scrollbar {
  width: 8px;
}
.descContent::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}
.descContent::-webkit-scrollbar-thumb:hover {
  background-color: #adadad;
}
/*! CSS Used fontfaces */
@font-face {
  font-family: baikeFont_taskIcon;
  src: url(https://bkssl.bdimg.com/static/wiki-task/taskBase/resource/font/taskIcon_9f975d6.eot?#iefix);
  src: url(https://bkssl.bdimg.com/static/wiki-task/taskBase/resource/font/taskIcon_9f975d6.eot?#iefix)
      format("embedded-opentype"),
    url(https://bkssl.bdimg.com/static/wiki-task/taskBase/resource/font/taskIcon_c7115f4.woff2)
      format("woff2"),
    url(https://bkssl.bdimg.com/static/wiki-task/taskBase/resource/font/taskIcon_8b0c1a2.woff)
      format("woff"),
    url(https://bkssl.bdimg.com/static/wiki-task/taskBase/resource/font/taskIcon_762490b.ttf)
      format("truetype"),
    url(https://bkssl.bdimg.com/static/wiki-task/taskBase/resource/font/taskIcon.otf)
      format("opentype"),
    url(https://bkssl.bdimg.com/static/wiki-task/taskBase/resource/font/taskIcon_4b740eb.svg)
      format("svg");
  font-weight: 400;
  font-style: normal;
}
/*! CSS Used from: https://bkssl.bdimg.com/static/wiki-common/pkg/wiki-common-base_66a9374.css */
input {
  font-family: "Helvetica Neue", Helvetica, Arial, "PingFang SC",
    "Hiragino Sans GB", "Microsoft YaHei", "WenQuanYi Micro Hei", sans-serif;
}
input {
  font-size: 100%;
}
h3,
p,
ul,
li,
input {
  margin: 0;
  padding: 0;
}
a {
  color: #338de6;
  text-decoration: none;
}
a:focus {
  outline: thin dotted;
}
a:active,
a:hover {
  outline: 0;
}
a:hover {
  text-decoration: underline;
}
h3 {
  font-size: 1.17em;
}
ul,
li {
  list-style: none;
}
input {
  vertical-align: middle;
}
input {
  line-height: normal;
}
input::-moz-focus-inner {
  border: 0;
}
.cmn-clearfix {
  *zoom: 1;
}
.cmn-clearfix:after {
  content: "\0020";
  display: block;
  height: 0;
  font-size: 0;
  clear: both;
  overflow: hidden;
  visibility: hidden;
}
h3 {
  color: #333;
}
/*! CSS Used from: https://bkssl.bdimg.com/static/wiki-task/taskBase/taskBase_9b28203.css */
a {
  color: #459df5;
}
a:focus {
  outline: 0;
}
input {
  border: 1px solid #e2e7ea;
}
input:focus {
  outline: 0;
}
.dib {
  display: -moz-inline-box;
  -moz-box-orient: vertical;
  display: inline-block;
  *zoom: 1;
  *display: inline;
}
.f14 {
  font-size: 14px;
}
.mt25 {
  margin-top: 25px;
}
.mr10 {
  margin-right: 10px;
}
.pv30 {
  padding: 30px 0;
}
.layout {
  width: 980px;
  margin: 0 auto;
}
.theaderWrap {
  position: relative;
}
.theader {
  margin-bottom: 5px;
  font-size: 24px;
  font-weight: 400;
}
.theaderRight {
  position: absolute;
  right: 0;
  bottom: 0;
}
.t-type {
  color: #459df5;
}
.t-type:hover {
  border-bottom: 2px solid #459df5;
  text-decoration: none;
}
.t-typeSelected {
  color: #333;
  cursor: default;
}
.t-typeSelected:hover {
  border-bottom: 0;
}
.t-separator {
  margin: 0 8px;
  border-right: 1px solid #d0d2d3;
}
.loadingWrap {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 9;
}
.loading {
  list-style: none;
  position: absolute;
  top: 50%;
  left: 50%;
  -webkit-transform: translate(-50%, -50%);
  -ms-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
  font-size: 0;
}
.loading li {
  height: 0;
  position: absolute;
  top: 50%;
  left: 0;
  width: 0;
  margin: 0;
  height: 10px;
  width: 10px;
  border: 3px solid #2b8ccd;
  border-radius: 100%;
  -webkit-transform: transformZ(0);
  -ms-transform: transformZ(0);
  transform: transformZ(0);
  -webkit-animation: LOADING 1s infinite;
  animation: LOADING 1s infinite;
}
.loading li:nth-child(1n) {
  left: -20px;
  -webkit-animation-delay: -0.4s;
  animation-delay: -0.4s;
}
.loading li:nth-child(2n) {
  left: 0;
  -webkit-animation-delay: -0.2s;
  animation-delay: -0.2s;
}
.loading li:nth-child(3n) {
  left: 20px;
  -webkit-animation-delay: 0s;
  animation-delay: 0s;
}
/*! CSS Used from: https://bkssl.bdimg.com/static/wiki-task/taskInfo-c2c/taskInfo-c2c_bb8c671.css */
a:hover {
  text-decoration: none;
}
.h_underline:hover {
  text-decoration: underline;
}
.taskLemma .searchBox {
  position: absolute;
  top: 50%;
  right: 0;
  margin-top: -15px;
  padding: 7px 10px 7px 30px;
  width: 250px;
  border: 1px solid #e2e7ea;
  background: url(https://bkssl.bdimg.com/static/wiki-task/taskInfo-c2c/resource/img/search_bf61ad0.png)
    no-repeat 5px;
}
.taskLemma .searchBox:focus {
  border-color: #459df5;
}
.taskLemma .viewport {
  width: 990px;
  position: relative;
  min-height: 40px;
}
.taskLemma #taskLemmaPager {
  text-align: center;
  margin-top: 20px;
}
.progress {
  margin-top: 35px;
  padding: 40px 0;
  border-top: 1px solid #e2e7ea;
}
.progress .myProgress {
  position: relative;
  width: 587px;
  height: 600px;
  float: left;
}
.progress .myProgress .notice {
  position: absolute;
  bottom: 35px;
  font-size: 14px;
}
.progress .myProgress #myProgressPager {
  position: absolute;
  bottom: 0;
  width: 100%;
  text-align: center;
}
.progress .myLemmaArea {
  position: relative;
  width: 595px;
  margin-top: 30px;
}
.progress .progressRanking {
  width: 338px;
  float: right;
  height: 585px;
  padding-left: 28px;
  margin-top: 15px;
  border-left: 1px solid #e2e7ea;
}
.progress .progressRanking .headline {
  position: relative;
  font-size: 14px;
}
.progress .r-ul {
  margin-top: 15px;
}
.progress .r-ul:nth-of-type(2) {
  display: none;
}
.progress .r-ul li {
  position: relative;
  font-size: 14px;
  line-height: 35px;
}
.progress .r-ul li:hover {
  background-color: snow;
}
.progress .r-ul li .userName {
  color: #333;
  margin-left: 28px;
}
.progress .r-ul li .userName:hover {
  color: #459df5;
}
.progress .r-ul li .complete {
  position: absolute;
  right: 0;
}
.progress .r-ul li:nth-child(1) {
  font-size: 16px;
}
.progress .r-ul li:nth-child(1) .ranking {
  font-size: 18px;
  color: #f75549;
}
.progress .r-ul li:nth-child(1) .complete {
  color: #f75549;
}
.progress .r-ul li:nth-child(2) {
  font-size: 16px;
}
.progress .r-ul li:nth-child(2) .ranking {
  font-size: 18px;
  color: #f98d5c;
}
.progress .r-ul li:nth-child(2) .complete {
  color: #f98d5c;
}
.progress .r-ul li:nth-child(3) {
  font-size: 16px;
}
.progress .r-ul li:nth-child(3) .ranking {
  font-size: 18px;
  color: #f9d689;
}
.progress .r-ul li:nth-child(3) .complete {
  color: #f9d689;
}
.progress .ranking {
  display: inline-block;
  width: 28px;
  text-align: center;
  color: #bfc0c1;
}
.empty {
  font-size: 22px;
  color: #d3d3d3;
  text-align: center;
  margin-top: 222px;
}
/*! CSS Used from: https://bkssl.bdimg.com/static/wiki-task/widget/taskPager/taskPager_ab8217b.css */
[pager-type="tpager"] {
  text-align: center;
  font-size: 0;
}
/*! CSS Used keyframes */
@-webkit-keyframes LOADING {
  0% {
    -webkit-transform: scale(0.5);
    transform: scale(0.5);
    background: #2b8ccd;
  }
  50% {
    -webkit-transform: scale(1);
    transform: scale(1);
    background: #fff;
  }
  100% {
    -webkit-transform: scale(0.5);
    transform: scale(0.5);
    background: #2b8ccd;
  }
}
@keyframes LOADING {
  0% {
    -webkit-transform: scale(0.5);
    transform: scale(0.5);
    background: #2b8ccd;
  }
  50% {
    -webkit-transform: scale(1);
    transform: scale(1);
    background: #fff;
  }
  100% {
    -webkit-transform: scale(0.5);
    transform: scale(0.5);
    background: #2b8ccd;
  }
}
</style>

