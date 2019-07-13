<template>
  <el-table :data="tableData" style="width: 100%" max-height="500">
    <el-table-column prop="name" label="词条名称" width="300"> </el-table-column>
    <el-table-column prop="version_wait" label="版本" width="150"> </el-table-column>
    <el-table-column prop="time_wait" label="提交时间" width="150"> </el-table-column>
  </el-table>
</template>

<script>
export default {
  name: "toBeAuditedEntry",
  props: ["subjectId"],
  data() {
    return {
      tableData: [],
    };
  },
  mounted() {
    this.init();
  },
  methods: {
    init() {
      this.$axios
        .post("http://localhost:8081/api/user/getEntry", {
          subjectId: new Number(this.subjectId),
          type: 4
        })
        .then(res => {
          if (res.data.data) {
            this.tableData = res.data.data.assignments;
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
    stateChange(state) {
      this.$emit("stateChange", state);
    },
  }
};
</script>
