<template>
  <el-table :data="tableData" style="width: 100%" max-height="500" @row-click="jumpToPreview">
    <el-table-column prop="name" label="词条名称" width="200"> </el-table-column>
    <el-table-column prop="name" label="全部版本" width="150"> </el-table-column>
    <el-table-column prop="name" label="通过时间" width="150"> </el-table-column>
    <el-table-column prop="name" label="被其它人修改版本" width="150"> </el-table-column>
  </el-table>
</template>

<script>
export default {
  name: "passedEntry",
  data() {
    return {
      tableData: []
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
          type: 5
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
    jumpToPreview(){
      this.$router.push({path : "/entry", query :{id : id}});
    },
    stateChange(state){
      this.$emit('stateChange', state)
    }
  }
};
</script>
