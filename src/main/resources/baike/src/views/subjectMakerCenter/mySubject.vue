<template>
  <div class="content">
    <template v-for="subject in subjects">
      <el-card class="box-card" :key="subject.id">
        <div slot="header" class="clearfix">
          <span>{{subject.name}}</span>
          <el-button style="float: right; padding: 3px 0" type="text" @click="see(subject.id)">查看</el-button>
        </div>
        <div class="text">{{subject.introduction}}</div>
      </el-card>
    </template>
  </div>
</template>

<script>
export default {
  name: "mySubject",
  data() {
    return {
      subjects: [
        {
          id: 1,
          name: "test",
          introduction: "test"
        },
        {
          id: 2,
          name: "test",
          introduction: "test"
        },
        {
          id: 3,
          name: "test",
          introduction: "test"
        },
        {
          id: 4,
          name: "test",
          introduction: "test"
        }
      ]
    };
  },
  mounted() {
    // 初始化数据
    this.$axios
      .post("http://localhost:8081/api/subjectMaker/getSubject")
      .then(res => {
        if (res.data.data) {
          this.subjects=res.data.data.subjects
          this.$message({
            message: res.data.msg
          });
        } else {
          this.$message({
            message: res.data.msg
          });
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
  methods: {
    see(id) {
      this.$router.push({ path: "/subjectmakercenter/subjectmanagement", query: { id: id } });
    }
  }
};
</script>

<style>
.content {
  margin-left: 10px;
  height: 800px;
  width: 960px;
}
.text {
  font-size: 14px;
}
.box-card {
  float: left;
  margin-top: 10px;
  margin-left: 10px;
  height: 170px;
  width: 150px;
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


