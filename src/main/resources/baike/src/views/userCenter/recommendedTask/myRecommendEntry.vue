<template>
  <div class="content">
    <div class="myrecentry-searchbar">
        <mySearch style="float:right" v-on:remoteMethod="remoteMethod" :options="options" :value="value" :loading="loading"></mySearch>
    </div>
    <template v-for="entry in entrys">
        <a class="box-card" @click="see(entry.id)" :title="entry.name"
        :key="entry.id" :body-style="{ padding: '0px' }">
            <div class="rec-entry-header">
                <div class="rec-entry-title">{{entry.name}}</div>
                <div style="display: inline;float: right;margin-top: 5px">({{entry.field}})</div> 
            </div>
            <div class="rec-entry-body">
                <p>{{entry.reason1}}</p>
                <p>{{entry.reason2}}</p>
            </div>         
        </a>
    </template>
  </div>
</template>

<script>

import mySearch from "../../../components/mySearch"

export default {
  name: "myRecommendEntry",
  components: {
    mySearch,
  },
  data() {
    return {
      entrys: [],
      options: [],
      loading: false,
      value: [],
      status : this.$store.state.status,
      deadTime: '0',
      finishedSubject: '0',
    };
  },
  mounted() {
    this.init()
  },
  methods: {
    init(){
    // 初始化数据
        this.$axios
        .post("/api/user/getRecommendEntry",{})
        .then(res => {
            if (res.data.data)
              this.entrys = res.data.data.assignments
        })
        .catch(error => {
            if (error.response) {
            this.$message({
                message: error.response.data.msg,
                type: "warning"
            });
            }
        });
    },
    see(id) {
      this.$router.push({ path: "/entryedit", query: { id: id } });
    },
    remoteMethod(query){
        if(query !== ''){
        this.loading = true;
        this.value = query;
        this.$axios
            .post("/api/user/searchEntry", {keyword:query})
            .then(res => {
                if(res.data.data){
                    this.entrys = res.data.data.assignments;
                    this.options = res.data.data.assignments;
                }
                this.loading = false;
            })
            .catch(error => {
                      
            });
      }else{
        this.options = [];
        this.init();
      }
    }
  }
};
</script>

<style scoped>
.content {
  margin-left: 10px;
  width: 1200px;
}
.myrecentry-searchbar{
  width: 1125px;
  height: 70px;
}
.box-card {
  padding: 10px 20px;
  display: block;
  background: #fafafa;
  text-decoration: none;
  border: solid 1px #d5d5d5;
  box-shadow: 0 1px 0 #e7e7e7;
  font-size: 14px;
  transition: .15s linear;
  float: left;
  margin: 0 12px 15px 0;
  height: 107px;
  width: 236px;
}
.box-card:hover{
  border: solid 1px #52a3f5;
  background:rgba(82, 163, 245,0.1)
}
.rec-entry-header{
    width: 100%;
    text-align: center;
}
.rec-entry-title{
   display: inline;
   font-size:20px;
   color:#52a3f5;
}
.rec-entry-body{
    height: 47px;
    color: #666;
    font-size: 16px;
    line-height: 20px;
    margin-top: 14px;
    margin-left: 10px;
}
.rec-entry-body p{
  line-height: 25px;
}
.rec-entry-body p::before{
    margin-right: 5px;
    content: "★";
    color: #52a3f5;
}
.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both;
} 
</style>