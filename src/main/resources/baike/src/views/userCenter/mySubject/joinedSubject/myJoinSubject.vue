<template>
  <div class="content">
    <div v-if="subjects.length == 0">
      <div
        style="width:95%; font-size:20px; margin-top:40px;margin-bottom: 40px;text-align: center"
      >
        <span>您还没有加入专题，赶紧加入一个吧!</span>
      </div>
    </div>
    <div v-else>
      <div class="myjoinsub-searchbar">
        <mySearch
          style="float:right"
          v-on:remoteMethod="remoteMethod"
          :options="options"
          :value="value"
          :loading="loading"
        ></mySearch>
      </div>
      <template v-for="subject in subjects">
        <el-card class="box-card" :key="subject.id" :body-style="{ padding: '0px' }">
          <img class="subject-image" :src="subject.imgUrl">
          <div style="padding: 14px;">
            <div >
              <span style="color:#338de6;float:left;">{{subject.name}}</span>
              <el-button style="float:right" size="mini" @click="see(subject.id)">查看</el-button>
              <div class="clear"></div>
            </div>
            <div class="subject-bottom">
              <i class="el-icon-time" style="color: #cdcfd1; font-size:14px"></i>
              <span style="font-size:14px; margin-right:5px; color: #cdcfd1;">剩余时间{{subject.deadline | getDay}}天</span>
              <i class="el-icon-coin" style="color: #cdcfd1; font-size:14px"></i>
              <span style="font-size:14px; color: #cdcfd1;">完成词条{{subject.finishNum}}个</span>
            </div>
          </div>
        </el-card>
      </template>
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
  data() {
    return {
      status: this.$store.state.status,
      subjects: [],
      options: [],
      loading: false,
      value: []
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
        .post("http://localhost:8081/api/user/getSubject")
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
    remoteMethod(query) {
      this.loading = true;
      this.$axios
        .post("http://localhost:8081/api/user/searchSubject", {
          keyword: query
        })
        .then(res => {
          if (res.data.data) {
            window.console.log("woxiaole");
            this.options = res.data.data.subjects;
          } else {
            window.console.log("wozhale");
          }
          this.loading = false;
        })
        .catch(error => {});
    }
  }
};
</script>

<style scoped>
.content {
  margin-left: 30px;
  width: 1200px;
}
.myjoinsub-searchbar {
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
.box-card:hover {
  border: solid 1px #52a3f5;
}
.subject-image {
  width: 234px;
  height: 130px;
}
.subject-bottom {
  margin-top: 35px;
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