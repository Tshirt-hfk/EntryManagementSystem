<template>
  <el-table :data="tableData" style="width: 100%">
    <el-table-column prop="name" label="名称" width="180"></el-table-column>
    <el-table-column label="领域" width="250">
      <template slot-scope="scope">
        <span v-for="item in scope.row.field" :key="item">{{item}},</span>
      </template>
    </el-table-column>
  </el-table>
</template>

<script>
export default {
  name: "publishedEntry",
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
          type: 2
        })
        .then(res => {
          if (res.data.data) {
            this.tableData = res.data.data.assignments;
          } else {
            //this.$message({
              //message: res.data.msg
            //});
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
