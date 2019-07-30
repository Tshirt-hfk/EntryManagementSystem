<template>
  <div class="content">
      <div class="myrecsub-searchbar">
        <el-input style="width: 250px; float: right; margin-right: 30px;" 
        v-model="searchValue" placeholder="请输入关键词"></el-input>
      </div>
    <template v-for="subject in subjects">
        <el-card class="box-card" :key="subject.id" :body-style="{ padding: '0px' }">
          <img class="subject-image" :src="subject.imgUrl">
          <div style="padding: 14px;">
            <div class="subject-top">
              <span style="color:#338de6;float:left">{{subject.name}}</span>
              <el-button style="float:right;" size="mini" @click="see(subject.id)">参加活动</el-button>
            </div>
            <div class="subject-bottom">
              <div v-if="subject.deadTime !== '0'" >
                <i class="el-icon-time" style="color: #707070; font-size:14px"></i>
                <span style="font-size:14px; margin-right:5px; color: #707070;">剩余时间{{subject.deadTime | getDay}}天</span>
                <i class="el-icon-coin" style="color: #707070; font-size:14px;margin-left: 5px"></i>
                <span style="font-size:14px; color: #707070;">{{subject.memberCount}}人参加</span>
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

export default {
  name: "myRecommendSubject",
  filters: {
    getDay: function (end_time) {
      var day = Math.ceil((end_time - new Date().getTime())/86400000)
      if(day < 0)
        day = 0
      return day
    }
  },
  watch:{
    searchValue:{
      handler(n, o){
        clearTimeout(this.timeout);
        this.timeout = setTimeout(() => {
          this.remoteMethod(n);
        }, 300);
      }
    }
  },
  data() {
    return {
      allsubjects: [],
      subjects: [],
      searchValue: '',
      timeout: null,
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
        .post("/api/user/getRecommendSubject",{})
        .then(res => {
            if (res.data.data){
              this.subjects = res.data.data.subjects;
              this.allsubjects = res.data.data.subjects;
            }
        })
        .catch(error => {
            if (error.response) {
            // this.$message({
            //     message: error.response.data.msg,
            //     type: "warning"
            // });
            }
        });
    },
    see(id) {
      this.$router.push({ path: "/subject", query: { id: id } });
    },
    remoteMethod(query){  
      if(query !== ''){
        this.$axios
            .post("/api/user/searchSubject", {keyword:query})
            .then(res => {
              if(res.data.data)
                this.subjects = res.data.data.subjects;
              else
                this.subjects = this.allsubjects.slice(0, 0);
            })
            .catch(error => {
            });
      }else{
        this.subjects = this.allsubjects;
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
  width: 1100px;
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