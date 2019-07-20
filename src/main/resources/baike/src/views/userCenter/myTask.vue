<template>
  <div class="uc-section-task-recommended">
    <div class="uc-tasrec-title">推荐任务</div>
    <div class="uc-tasrec-layout">
      <el-radio-group
        class="uc-tasrec-button"
        v-model="tabSelection"
        style="margin-bottom: 30px;"
        v-on:change="change"
      >
        <el-radio-button label="left">推荐词条</el-radio-button>
        <el-radio-button label="right">推荐专题</el-radio-button>
      </el-radio-group>
      <!--高度后期需要自适应 -->
      <el-card style="width: 1200px;height:560px">
        <router-view></router-view>
      </el-card>
    </div>
  </div>
</template>

<script>
export default {
  name: "myTask",
  data() {
    return {
      userName: this.$store.state.name,
      tabSelection: "left",
      subjectId: "0",
      subjectName: "",
      subjectFlag: false
    };
  },
  mounted() {},
  methods: {
    change() {
      if (this.tabSelection == "left") {
        this.$router.push("./recommendedentry");
      } else if (this.tabSelection == "right") {
        this.$router.push("./recommendedsubject");
      }
    }
  }
};
</script>

<style>
.uc-section-task-recommended {
  padding: 20px 0 50px;
}
.uc-tasrec-title {
  height: 80px;
  text-align: center;
  line-height: 80px;
  font-size: 34px;
  color: #666;
  margin: 0;
  padding: 0;
}
.uc-tasrec-layout {
  width: 1200px;
  margin: 0 auto;
}
.uc-tasrec-button {
  margin-left: 500px;
  margin-top: 10px;
}
</style>