<template>
  <el-table :data="tableData" style="width: 100%" max-height="500">
    <el-table-column prop="name" label="词条名称" width="150"> </el-table-column>
    <el-table-column prop="field" label="领域" width="150"> </el-table-column>
    <el-table-column prop="reason" label="修改原因" width="180"> </el-table-column>
    <el-table-column label="提交时间" width="180"> 
      <template slot-scope="scope">{{ scope.row.saveTime | formatDate}}</template>
    </el-table-column>
    <el-table-column label="版本" show-overflow-tooltip>
      <template>
        <span class="auditentry-version">
          <a @click="toEntryExhibition">版本</a>
        </span>
      </template>
    </el-table-column>
  </el-table>
</template>

<script>

import moment from 'moment'

export default {
  name: "toBeAuditedEntry",
  data() {
    return {
      tableData: [],
    };
  },
  mounted() {
    this.init();
  },
  filters: {
      formatDate: function (value) {
        return moment(value).format('YYYY-MM-DD HH:mm:ss')
      }
  },
  methods: {
    init() {
      this.$axios
        .post("http://localhost:8081/api/user/getEntry", {
          type: 4
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
    toEntryExhibition(){
      //待添加
    },
    stateChange(state) {
      this.$emit("stateChange", state);
    },
  }
};
</script>

<style>
.auditentry-version{
  text-decoration: underline;
  cursor: pointer;
  color: #1296db;
}
</style>