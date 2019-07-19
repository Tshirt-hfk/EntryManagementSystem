<template>
  <div class="content">
      <div class="myrecsub-searchbar">
        <!-- <el-select v-model="value" filterable remote :loading="loading"
        reserve-keyword placeholder="请输入关键词" :remote-method="remoteMethod"
        style="float: right">
          <el-option v-for="item in options" :key="item.name"
          :label="item.name" :value="item.name">
          </el-option>  
        </el-select> -->
        <mySearch style="float:right" v-on:remoteMethod="remoteMethod" :options="options" :value="value" :loading="loading"></mySearch>
      </div>
    <template v-for="subject in subjects">
        <el-card class="box-card" :key="subject.id" :body-style="{ padding: '0px' }">
          <img class="subject-image" src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png">
          <div style="padding: 14px;">
            <div class="subject-top">
              <span style="color:#338de6;">{{subject.name}}</span>
              <el-button class="button" size="mini" @click="see(subject.id)">参加活动</el-button>
            </div>
            <div class="subject-bottom">
              <div v-if="subject.deadTime !== '0'" >
                <i class="el-icon-time" style="color: #707070; font-size:14px"></i>
                <span style="font-size:14px; margin-right:5px; color: #707070;">剩余时间5天</span>
                <i class="el-icon-coin" style="color: #707070; font-size:14px;margin-left: 5px"></i>
                <span style="font-size:14px; color: #707070;">{{subject.total_count}}人参加</span>
              </div>
              <div v-else>
                  <i class="el-icon-time" style="color: #f18167; font-size:14px"></i>
                  <span style="font-size:14px; margin-right:5px; color: #f18167;">活动已结束</span>
              </div>
            </div>
          </div>
        </el-card>
    </template>
  </div>
</template>

<script>

import mySearch from "../../components/mySearch"

export default {
  name: "myRecommendSubject",
  components: {
    mySearch,
  },
  computed:{
    deadline: function(){

    }
  },
  data() {
    return {
      subjects: [],
      options: [],
      loading: false,
      value: [],
      status : this.$store.state.status,
    };
  },
  mounted() {
    this.init()
  },
  methods: {
    init(){
    // 初始化数据
        this.$axios
        .post("http://localhost:8081/api/user/getRecommendSubject",{})
        .then(res => {
            if (res.data.data)
              this.subjects = res.data.data.subjects;
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
      this.$router.push({ path: "/subject", query: { id: id } });
    },
    remoteMethod(query){
      if(query !== ''){
        this.loading = true;
        this.$axios
            .post("http://localhost:8081/api/user/searchSubject", {keyword:query})
            .then(res => {
                if(res.data.data){
                    this.subjects = res.data.data.subjects;
                    this.options = res.data.data.subjects;
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
  margin-left: 30px;
  width: 1200px;
  max-height: 1210px;
}
.myrecsub-searchbar{
  width: 1050px;
  height: 50px;
}
.box-card {
  float: left;
  margin-top: 10px;
  margin-left: 30px;
  margin-bottom: 10px;
  height: 210px;
  width: 236px;
}
.box-card:hover{
  border: solid 1px #52a3f5;
}
.subject-image{
  width: 234px;
  height: 130px;
}
.subject-top{
  height: 20px;
}
.subject-bottom{
  margin-top: 13px;
  line-height: 12px;
}
.button{
  float: right;
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