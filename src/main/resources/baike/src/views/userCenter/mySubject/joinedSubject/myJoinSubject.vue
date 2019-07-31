<template>
  <div class="content">
    <div v-if="allsubjects.length == 0">
      <div
        style="width:95%; font-size:20px; margin-top:40px;margin-bottom: 40px;text-align: center"
      >
        <span>您还没有加入专题，赶紧加入一个吧!</span>
      </div>
    </div>
    <div v-else>
      <div class="myjoinsub-searchbar">
        <el-input style="width: 250px; float: right; margin-right: 30px;" 
        v-model="searchValue" placeholder="请输入关键词"></el-input>
      </div>
      <div style="width: 100%; height: 480px;">
      <template v-for="subject in displayData">
        <el-card class="box-card" :key="subject.id" :body-style="{ padding: '0px' }">
          <img class="subject-image" :src="subject.imgUrl">
          <div style="padding: 14px;">
            <div style="height: 20px;">
              <span style="color:#338de6;float:left;">{{subject.name}}</span>
              <el-button style="float:right" size="mini" @click="see(subject.id)">查看</el-button>
              <div class="clear"></div>
            </div>
            <div class="subject-bottom">
              <i class="el-icon-time" style="color: #cdcfd1; font-size:14px"></i>
              <span style="font-size:14px; margin-right:5px; color: #cdcfd1;">剩余时间{{subject.deadline | getDay}}天</span>
              <i class="el-icon-coin" style="color: #cdcfd1; font-size:14px"></i>
              <span style="font-size:14px; color: #cdcfd1;">完成{{subject.finishNum}}个</span>
            </div>
          </div>
        </el-card>
      </template>
      </div>
      <div style="width: 100%;margin-top: 25px;">
        <el-pagination
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-size="pagesize"
          layout="total, prev, pager, next, jumper"
          :total="subjects.length"
          style="width: 360px;margin: 0 auto"
        ></el-pagination>
      </div>
    </div>
  </div>
</template>

<script>

import mySearch from "../../../../components/mySearch";

export default {
  name: "myJoinSubject",
  components: {
    mySearch
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
      status: this.$store.state.status,
      currentPage: 1,
      pagesize: 8,
      searchValue: '',
      timeout: null,
      allsubjects: [],
      subjects: [],
      displayData: []
    };
  },
  mounted() {
    this.init();
  },
  filters: {
      getDay: function (end_time) {
        var day = Math.ceil((end_time - new Date().getTime())/86400000)
        if(day < 0)
          day = 0
        return day
      }
  },
  methods: {
    init() {
      // 初始化数据
      this.$axios
        .post("/api/user/getSubject")
        .then(res => {
          if (res.data.data){
            this.subjects = res.data.data.subjects;
            this.allsubjects = res.data.data.subjects;
            this.displayData = res.data.data.subjects.slice(0, 8);
          }
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
    handleCurrentChange(val) {
      this.currentPage = val;
      let indexleft = val - 1;
      let size = this.pagesize;
      this.displayData = this.subjects.slice(indexleft * size, val * size);
    },
    remoteMethod(query) {
      if (query !== "") {
        this.subjects = this.allsubjects.filter(subject => {
          return subject.name.toLowerCase().indexOf(query.toLowerCase()) > -1;
        });
        this.displayData = this.subjects.slice(0, this.pagesize);
      } else {
        this.subjects = this.allsubjects;
        this.displayData = this.subjects.slice(0, this.pagesize);
      }
    },
  }
};
</script>

<style scoped>
.content {
  margin-left: 30px;
  width: 100%;
  max-height: 1210px;
}
.myjoinsub-searchbar {
  width: 1060px;
  height: 60px;
}
.box-card {
  float: left;
  margin-top: 10px;
  margin-left: 30px;
  margin-bottom: 10px;
  height: 210px;
  width: 236px;
}
.box-card:hover {
  border: solid 1px #52a3f5;
}
.subject-image {
  width: 234px;
  height: 130px;
}
.subject-bottom {
  margin-top: 13px;
  line-height: 12px;
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