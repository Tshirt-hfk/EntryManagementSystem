<template>
  <el-table :data="tableData" style="width: 100%">
    <el-table-column label="名称" width="180">
      <template slot-scope="scope">{{ scope.row.name }}</template>
    </el-table-column>
    <el-table-column label="分类" width="180">
      <template slot-scope="scope">{{ scope.row.field }}</template>
    </el-table-column>
    <el-table-column label="领取人" width="180">
      <template slot-scope="scope">{{ scope.row.username }}</template>
    </el-table-column>
  </el-table>
</template>

<script>
export default {
  name: "drawedEntry",
  props: ["subjectId"],
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
        .post("http://localhost:8081/api/subjectMaker/getAssignment", {
          subjectId: new Number(this.subjectId),
          type: 3
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
    stateChange(state){
      this.$emit('stateChange', state)
    }
  }
};
</script>