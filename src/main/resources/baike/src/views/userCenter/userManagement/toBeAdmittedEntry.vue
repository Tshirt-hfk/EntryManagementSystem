<template>
    <div>
        <el-table ref="multipleTable" :data="tableData" tooltip-effect="dark"
            style="width: 100%" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55"> </el-table-column>
            <el-table-column prop="name" label="词条名称" width="150"> </el-table-column>
            <el-table-column prop="name" label="保存时间" width="150"> </el-table-column>
            <el-table-column prop="name" label="操作" show-overflow-tooltip> </el-table-column>
        </el-table>
        <div style="margin-top: 20px">
            <el-button @click="admitEntry">提交</el-button>
            <el-button @click="toggleSelection()">删除</el-button>
        </div>
    </div> 
</template>

<script>
export default {
  name: "toBeAdmittedEntry",
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
        .post("http://localhost:8081/api/user/getEntry", {
          subjectId: new Number(this.subjectId),
          type: 3
        })
        .then(res => {
          if (res.data.data) {
            this.tableData = res.data.data.assignments;
            window.console.log(tableData.name);
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
