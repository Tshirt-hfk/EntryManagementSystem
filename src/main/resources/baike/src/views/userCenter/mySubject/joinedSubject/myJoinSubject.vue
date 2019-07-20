<template>
  <div class="content">
    <div v-if="subjects.length == 0">
      <div style="width:100%; font-size:20px; margin-top:40px;margin-bottom: 40px;text-align: center">
      <span>您还没有加入专题，赶紧加入一个吧!</span>
      </div>
    </div>
    <div v-else>
      <div class="myjoinsub-searchbar">
          <mySearch style="float:right" v-on:remoteMethod="remoteMethod" :options="options" :value="value" :loading="loading"></mySearch>
      </div>
      <template v-for="subject in subjects">
        <el-card class="box-card" :key="subject.id" :body-style="{ padding: '0px' }">
          <img class="subject-image" src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png">
          <div style="padding: 14px;">
            <div style="display: inline;">
              <span style="color:#338de6;">{{subject.name}}</span>
              <el-button class="button" size="mini" @click="see(subject.id)">查看</el-button>
            </div>
            <div class="subject-bottom">
              <i class="el-icon-time" style="color: #cdcfd1; font-size:14px"></i>
              <span style="font-size:14px; margin-right:5px; color: #cdcfd1;">剩余时间{{deadTime}}天</span>
              <i class="el-icon-coin" style="color: #cdcfd1; font-size:14px"></i>
              <span style="font-size:14px; color: #cdcfd1;">完成词条{{finishedSubject}}个</span>
            </div>
          </div>
        </el-card>
      </template>
    </div>
  </div>
</template>

<script>

import mySearch from "../../../../components/mySearch"

export default {
  name: "myJoinSubject",
  components: {
    mySearch,
  },
  data() {
    return {
      status : this.$store.state.status,
      subjects: [],
      deadTime: '0',
      finishedSubject: '0',
      options: [],
      loading: false,
      value: [],
    };
  },
  mounted() {
    this.init()
  },
  methods: {
    init(){
    // 初始化数据
        this.$axios
        .post("http://localhost:8081/api/user/getSubject")
        .then(res => {
            if (res.data.data)
              this.subjects=res.data.data.subjects
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
        this.loading = true;
        this.$axios
            .post("http://localhost:8081/api/user/searchSubject", {keyword:query})
            .then(res => {
                if(res.data.data){
                    window.console.log("woxiaole")
                    this.options = res.data.data.subjects;
                }else{
                    window.console.log("wozhale")
                }
                this.loading = false;
            })
            .catch(error => {
                      
            });
    }
  }
};
</script>

<style scoped>
.content {
  margin-left: 30px;
  width: 1200px;
}
.myjoinsub-searchbar{
  width: 1060px;
  height: 60px;
}
.box-card {
  float: left;
  margin-top: 10px;
  margin-left: 30px;
  margin-bottom: 15px;
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