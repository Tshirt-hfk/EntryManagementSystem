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
    <el-table-column label="领取人" width="280">
      <template slot-scope="scope">
        <el-button @click="see(scope.row)">查 看</el-button>
        <el-button @click="audit(scope.row, true)">通 过</el-button>
      </template>
    </el-table-column>
  </el-table>
</template>


<script>
export default {
  name: "toAudittedEntry",
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
        .post("http://localhost:8081/api/subjectMaker/getAssignment", {
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
    toggleSelection(rows) {
      if (rows) {
        rows.forEach(row => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    stateChange(state) {
      this.$emit("stateChange", state);
    },
    audit(row, pass) {
      this.$axios
        .post("http://localhost:8081/api/subjectMaker/auditTask", {
          assignmentId: row.id,
          userId: row.userId,
          pass: pass
        })
        .then(res => {
          if (res.data) {
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
    }
  }
};
</script>
