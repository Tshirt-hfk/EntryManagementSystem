<template>
  <div class="index-layout">
    <div class="index-searchbar">
      <div class="logo-headl">
        <a title="首页" href="/">
          <img style="width: 120px; height: 70px;" src="/static/image/logo.png" />
        </a>
      </div>
      <div class="index-search" v-on:keyup.enter="search" >
        <el-autocomplete
          v-model="value"
          style="width: 440px"
          :fetch-suggestions="querySearch"
          placeholder="请输入词条名称"
          :trigger-on-focus="false"
        ></el-autocomplete>
        <el-button type="primary" @click="search" style="margin-left: 10px">搜索词条</el-button>
      </div>
    </div>
    <div class="index-navbar">
      <div class="index-navbar-menu">
        <el-menu
          class="el-menu-demo"
          mode="horizontal"
          @select="handleSelect"
          background-color="#459df5"
          text-color="#fff"
          active-text-color="#fff"
          :router="true"
          default-active="/"
          style="float: left;"
        >
          <el-menu-item
            index="/"
            style="width: 150px; text-align: center;font-size: large;letter-spacing:12px;text-indent:10px"
          >首页</el-menu-item>
          <el-submenu index="2" style="width : 150px; text-align: center;font-size: large;">
            <template slot="title">
              <span style="font-size: large;letter-spacing:12px;text-indent:20px">分 类</span>
            </template>
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
          <el-menu-item
            index="3"
            style="width: 150px; text-align: center;font-size: large;letter-spacing:12px;text-indent:10px"
          >专 题</el-menu-item>
        </el-menu>
        <div class="index-navbar-usercenter">
          <div class="line"></div>
          <a class="index-navbar-usercenter-link" @click="toUserCenter">
            <i class="el-icon-user-solid"></i>
            个 人 中 心
          </a>
        </div>
      </div>
    </div>
    <div class="index-main">
      <div class="index-main-wrap">
        <el-carousel height="350px" indicator-position="outside" :interval="rollingTime">
          <el-carousel-item v-for="item in homePageEntry" :key="item.id">
            <img style="width: 710px; height: 350px;cursor:pointer" :src="item.src" @click="toEntryExhibition(item.name)"/>
            <div class="index-focus-intro">
              <div class="index-focus-summary">
                <h2>
                  <a @click="toEntryExhibition(item.name)">{{item.name}}</a>
                </h2>
                <p>{{item.intro}}</p>
              </div>
            </div>
          </el-carousel-item>
        </el-carousel>
        <div class="index-main-classification">
          <div class="classification-title">
            <h2>分类推荐</h2>
            <el-tabs v-model="field">
              <el-tab-pane name="人物">
                <span slot="label">人物</span>
              </el-tab-pane>
              <el-tab-pane name="自然">
                <span slot="label">自然</span>
              </el-tab-pane>
              <el-tab-pane name="文化">
                <span slot="label">文化</span>
              </el-tab-pane>
              <el-tab-pane name="科技">
                <span slot="label">科技</span>
              </el-tab-pane>
            </el-tabs>
            <div class="classification-content">
              <classificationEntry :field="field" v-on:toEntryExhibition="toEntryExhibition"></classificationEntry>
            </div>
          </div>
        </div>
      </div>
      <div class="index-side-wrap">
        <div class="index-side-rightbox">
          <div class="index-side-rightbox-title">
            <div class="name">众智化词条</div>
            <div class="slogan">集大家智慧认识世界</div>
            <div class="number">
              共收录
              <span style="font-size: 27px">638,352</span>
              个词条
            </div>
          </div>
          <a @click="toEntryCreate">创建词条</a>
          <a @click="toRecommendEntry">完善词条</a>
        </div>
        <div class="index-side-recom">
          <div class="index-side-recom-title">
            <h3>推荐专题</h3>
          </div>
          <div class="index-side-recom-subject" v-for="item in recommendSubject" :key="item.id">
            <div class="subject-title">
              <span class="subject-identification">推荐</span>
              <a class="title-name" @click="toSubjectExhibition(item.id)">{{item.name}}</a>
            </div>
            <div class="subject-description">
              <img style="width: 133px;height: 100px;cursor: pointer" :src="item.src" @click="toSubjectExhibition(item.id)"/>
              <div class="subject-ctn">
                <p>{{item.intro}}</p>
                <a style="margin-left: 5px" v-for="fieldItem in item.field" 
                  :key="fieldItem">{{fieldItem}}</a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import myEntrySearch from "../components/myEntrySearch";
import classificationEntry from "./classificationEntry";

export default {
  name: "index",
  components: {
    myEntrySearch,
    classificationEntry
  },
  data() {
    return {
      rollingTime: 5000,
      searchResult: [],
      timeout: null,
      value: "",
      field: "人物",
      //数据都要从数据库取
      homePageEntry: [
        {
          id: "1",
          name: "建军节",
          intro: "八一建军节致敬中国军人，中国军人二十四小时展露英雄本色",
          src:
            "/static/image/建军节.jpg"
        },
        {
          id: "2",
          name: "小红书",
          intro: "种草神器出事了，小红书被下架，启动整改全面排查",
          src:
            "/static/image/小红书.jpg"
        },
        {
          id: "3",
          name: "扎里夫",
          intro: "伊朗外长遭美国政府制裁，其回应：谢谢将我视为如此大的威胁",
          src:
            "/static/image/伊朗.jpg"
        },
        {
          id: "4",
          name: "小行星",
          intro: "险！一颗小行星擦过地球，科学家只提前1天发现",
          src:
            "/static/image/小行星.jpg"
        },
        {
          name: "哪吒",
          intro: "魔童降世，你还在等什么，不一样的小哪吒",
          src:
            "/static/image/哪吒.jpg"
        }
      ],
      recommendSubject: [
        {
          id: "1",
          name: "游戏青春",
          field: ["方磊"],
          intro: "谁还没有窝在寝室打游戏的时光呢",
          src:
            "https://gss0.bdstatic.com/94o3dSag_xI4khGkpoWK1HF6hhy/baike/eWH%3D150%2C100/sign=abd9a92af936afc31c664268822cdbe8/9a504fc2d56285354d4b36cb9eef76c6a6ef63b5.jpg"
        },
        {
          id: "2",
          name: "游戏青春",
          field: ["方磊"],
          intro: "谁还没有窝在寝室打游戏的时光呢",
          src:
            "https://gss0.bdstatic.com/94o3dSag_xI4khGkpoWK1HF6hhy/baike/eWH%3D150%2C100/sign=abd9a92af936afc31c664268822cdbe8/9a504fc2d56285354d4b36cb9eef76c6a6ef63b5.jpg"
        },
        {
          id: "3",
          name: "游戏青春",
          field: ["方磊"],
          intro: "谁还没有窝在寝室打游戏的时光呢",
          src:
            "https://gss0.bdstatic.com/94o3dSag_xI4khGkpoWK1HF6hhy/baike/eWH%3D150%2C100/sign=abd9a92af936afc31c664268822cdbe8/9a504fc2d56285354d4b36cb9eef76c6a6ef63b5.jpg"
        }
      ]
    };
  },
  mounted() {
    this.init();
  },
  methods: {
    init(){
      this.$axios
        .post("/api/user/getRecommendSubject",{})
        .then(res => {
            if (res.data.data){
              this.recommendSubject = res.data.data.subjects.splice(0, 3);
            }
        })
        .catch(error => {
            if (error.response) {
            }
        });
    },
    search() {
      var { href } = this.$router.resolve({
        name: "entryPreview",
        params: {
          name: this.value
        }
      });
      window.open(href, "_blank");
      this.value = "";
    },
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
    },
    toUserCenter() {
      this.$router.push("/usercenter/mysubject");
    },
    toEntryExhibition(entryName){
      var { href } = this.$router.resolve({
        name: "entryPreview",
        params: {
          name: entryName
        }
      });
      window.open(href, "_blank");
    },
    toSubjectExhibition(id) {
      var { href } = this.$router.resolve({
        path: "/subject",
        query: {
          id: id
        }
      });
      window.open(href, "_blank");
    },
    toEntryCreate() {},
    toRecommendEntry() {},
    querySearch(query, cb) {
        this.$axios
        .get("/data/fetchPageByName", {
          params: {
            name: query
          }
        })
        .then(res => {
          if (res.data) {
 //             this.searchResult = ;
          }
        })
        .catch(error => {
        });

        clearTimeout(this.timeout);
        this.timeout = setTimeout(() => {
          cb(this.searchResult);
        }, 3000 * Math.random());
    }
  }
};
</script>

<style scoped>
h2,
p,
h3 {
  margin: 0;
  padding: 0;
}
.index-layout {
  width: 100%;
  margin: 0 auto;
  position: relative;
}
.index-searchbar {
  width: 700px;
  height: 120px;
  margin: 0 auto;
  position: relative;
  font-family: arial;
}
.logo-headl {
  padding: 25px 0 35px;
  float: left;
  line-height: 1;
}
.index-search {
  float: left;
  padding: 37px 0;
  margin-left: 10px;
}
.index-navbar {
  position: relative;
  background-color: #459df5;
  width: 100%;
  height: 61px;
  margin-top: 18px;
}
.index-navbar-menu {
  margin: 0 auto;
  width: 1050px;
  height: 61px;
}
.line {
  border-right: 1px solid #ddd;
  height: 50px;
  float: left;
  margin-top: 6px;
}
.index-navbar-usercenter {
  height: 61px;
  float: right;
}
.index-navbar-usercenter-link {
  font-size: large;
  color: #f5f5f5;
  text-decoration: none;
  cursor: pointer;
  width: 200px;
  float: left;
  margin-top: 17px;
  margin-left: 35px;
}
.index-main {
  width: 1050px;
  margin: 0 auto;
  min-height: 700px;
  margin-top: 10px;
}
.index-main-wrap {
  float: left;
  width: 710px;
}
.index-focus-intro {
  background-color: rgba(0, 0, 0, 0.6);
  position: absolute;
  left: 0;
  bottom: 0;
  width: 710px;
  height: 100px;
  color: #fff;
  font-family: Arial, Helvetica, sans-serif;
}
.index-focus-summary {
  bottom: 5px;
  left: 0;
  position: absolute;
  padding-left: 20px;
  padding-right: 20px;
}
.index-focus-summary h2 {
  font-size: 24px;
  line-height: 40px;
  color: #fff;
  font-weight: 400;
}
.index-focus-summary a {
  cursor: pointer;
  text-decoration: none;
}
.index-focus-summary a:hover {
  text-decoration: underline;
}
.index-focus-summary p {
  font-size: 14px;
  font-weight: 400;
  height: 50px;
  overflow: hidden;
  line-height: 22px;
  color: #ddd;
}
.index-side-wrap {
  width: 320px;
  float: right;
}
.index-side-rightbox {
  background-color: #fafafa;
  padding: 10px 20px;
}
.index-side-rightbox-title {
  position: relative;
  margin-top: 15px;
  text-align: center;
  font-weight: 400;
}
.index-side-rightbox-title .name {
  margin-bottom: 10px;
  line-height: 1.2;
  font-size: 30px;
  color: #459df5;
}
.index-side-rightbox-title .slogan {
  margin-bottom: 25px;
  line-height: 1;
  font-size: 18px;
  color: #459df5;
}
.index-side-rightbox-title .number{
  margin-bottom: 20px;
  line-height: 1;
  font-size: 18px;
  color: #459df5;
}
.index-side-rightbox a {
  position: relative;
  display: block;
  margin: 10px auto 0;
  width: 204px;
  height: 40px;
  line-height: 40px;
  color: #fff;
  font-size: 18px;
  text-align: center;
  border-radius: 3px;
  background: #409eff;
  cursor: pointer;
}
.index-side-rightbox a:hover{
  opacity: 0.9;
}
.index-side-recom {
  background-color: #fafafa;
  margin-top: 25px;
  margin-bottom: 25px;
  padding: 10px 20px;
}
.index-side-recom-title {
  border-bottom: #e6e8eb solid 2px;
  height: 28px;
  line-height: 28px;
}
.index-side-recom-title h3 {
  font-weight: 400;
  font-size: 17px;
  display: inline-block;
  border-bottom: #4d95dc solid 3px;
  line-height: 1.1;
  padding-bottom: 5px;
}
.index-side-recom-subject {
  min-height: 95px;
  margin-top: 15px;
}
.index-side-recom-subject .subject-title {
  position: relative;
  overflow: hidden;
  height: 22px;
  line-height: 22px;
  font-size: 22px;
}
.index-side-recom-subject .subject-identification {
  position: absolute;
  top: 2px;
  left: 0;
  padding: 0 3px;
  color: #f5f5f5;
  font-size: 13px;
  line-height: 1.5;
  background-color: #f18167;
}
.index-side-recom-subject .title-name {
  margin-left: 48px;
  color: #333;
  text-decoration: none;
  font-size: 20px;
  cursor: pointer;
}
.index-side-recom-subject .title-name:hover {
  color: #409eff;
}
.index-side-recom-subject .subject-description {
  margin-top: 15px;
  position: relative;
  width: 280px;
  height: 100px;
}
.index-side-recom-subject .subject-ctn {
  float: right;
  width: 130px;
}
.index-side-recom-subject .subject-ctn p {
  height: 60px;
  overflow: hidden;
  line-height: 20px;
  font-size: 14px;
  color: #888;
  font-family: Arial, Helvetica, sans-serif;
}
.index-side-recom-subject .subject-ctn a {
  color: #666;
  border: solid 1px #e2e2e2;
  border-radius: 2px;
  background: #fff;
  text-align: center;
  line-height: 1;
  padding: 5px 8px;
  font-size: 14px;
  float: right;
  margin-top: 14px;
  cursor: pointer;
}
.index-side-recom-subject .subject-ctn a:hover {
  color: #409eff;
  border: solid 1px #409eff;
}
.index-main-classification {
  margin-top: 25px;
  width: 710px;
}
.classification-title {
  height: 28px;
  margin-bottom: 10px;
  padding-bottom: 10px;
  border-bottom: 2px solid #e2e2e2;
}
.classification-title h2 {
  float: left;
  margin-right: 50px;
  margin-top: 6px;
  padding-bottom: 1px;
  font-size: 16px;
  line-height: 30px;
  color: #336;
  border-bottom: 3px solid #336;
}
.classification-content {
  margin-top: 20px;
}
</style>
