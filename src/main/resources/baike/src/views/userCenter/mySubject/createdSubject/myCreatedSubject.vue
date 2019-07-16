<template>
  <div class="content">
    <div v-if="status == '1'">
      <div style="width:100%; font-size:20px; margin-top:15px">
      <span>您没有权限，点击</span>
      <el-link type="primary" @click="apply">这里</el-link>
      <span>申请专题制作人权限</span>
      </div>
    </div>
    <div v-else-if="subjects.length == 0">
      <div style="width:100%; font-size:20px; margin-top:15px">
      <span>您还没有创建专题，赶紧创建一个吧</span>
      </div>
    </div>
    <div v-else>
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
  </div>
</template>

<script>
export default {
  name: "myCreatedSubject",
  data() {
    return {
      status : this.$store.state.status,
      subjects: [],
    };
  },
  mounted() {
    this.init()
  },
  methods: {
    init(){
      // 初始化数据
        this.$axios
          .post("http://localhost:8081/api/subjectMaker/getSubject")
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
    apply(){

    },
    see(id) {
      this.$router.push({ path: "./subjectmanagement", query: { id: id } });
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
  height: 250px;
  width: 200px;
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


