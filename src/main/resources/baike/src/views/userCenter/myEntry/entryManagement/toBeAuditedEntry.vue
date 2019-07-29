<template>
  <div>
    <el-input style="width: 300px; float: right;margin-bottom: 10px;" v-model="searchValue" placeholder="请输入关键词"></el-input>
    <el-table :data="displayData" style="width: 100%" max-height="500">
      <el-table-column prop="name" label="词条名称" width="150"> </el-table-column>
      <el-table-column prop="field" label="领域" width="250"> </el-table-column>
      <el-table-column prop="reason" label="修改原因" width="180"> </el-table-column>
      <el-table-column label="提交时间" width="180"> 
        <template slot-scope="scope">{{ scope.row.saveTime | formatDate}}</template>
      </el-table-column>
      <el-table-column align="right">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="getTaskContent(scope.row.id)">预览</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="toaudit-page">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :current-page="currentPage" :page-sizes="[10, 20, 50]" :page-size="pagesize"
        layout="total, sizes, prev, pager, next, jumper" :total="tableData.length" 
        style="width: 540px;margin: 0 auto"> </el-pagination>
    </div>
    <entryReview
      :relationData="relationData"
      :form="form"
      :drawerFlag="drawerFlag"
      v-on:handleClose="handleClose"
    ></entryReview>
  </div>
</template>

<script>

import moment from 'moment'
import entryReview from "../../../../components/entryReview"

export default {
  name: "toBeAuditedEntry",
  components:{
    entryReview,
  },
  watch:{
    searchValue:function(n, o){
        this.remoteMethod(n);
    }
  },
  data() {
    return {
      drawerFlag: false,
      relationData: [],
      form: {
        entryName: "",
        field: [],
        imageUrl: "",
        intro: "",
        infoBox: [],
        content: "",
        reference: []
      },
      searchValue: '',
      currentPage: 1,
      pagesize: 10,
      entries: [],
      tableData: [],
      displayData: [],
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
        .post("/api/user/getEntry", {
          type: 4
        })
        .then(res => {
          if (res.data.data) {
            this.entries = res.data.data.assignments;
            this.tableData = res.data.data.assignments;
            this.displayData = res.data.data.assignments.slice(0, 10);
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
    handleSizeChange(val) {
      this.pagesize = val;
      let index = this.currentPage - 1;
      this.displayData = this.tableData.slice(index*val, (index + 1)*val);
    },
    handleCurrentChange(val) {
      this.currentPage = val;
      let indexleft = val - 1;
      let size = this.pagesize;
      this.displayData = this.tableData.slice(indexleft*size, val*size);
    },
    remoteMethod(query) {
      if (query !== "") {
        this.tableData = this.entries.filter(entry => {
          return entry.name.toLowerCase().indexOf(query.toLowerCase()) > -1;
        });
        this.displayData = this.tableData.slice(0, this.pagesize);
      } else {
        this.tableData = this.entries;
        this.displayData = this.tableData.slice(0, this.pagesize);
      }
    },
    getTaskContent(id){
      if(!this.form.entryName){
        this.$axios
          .post("/api/user/getTaskContent", {
              taskId: new Number(id)
          })
          .then(res => {
            if (res.data.data) {
              this.form.entryName = res.data.data.entryName;
              this.form.imageUrl = res.data.data.imageUrl;
              this.form.intro = res.data.data.intro;
              for (var field of res.data.data.field) {
                this.form.field.push(field);
              }
              for (var info of res.data.data.infoBox) {
                this.form.infoBox.push(info);
              }
              this.form.content = res.data.data.content;
              this.drawerFlag = true;
            } else {
              this.$message({
                message: res.data.msg,
                type: "warning"
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
      }else
        this.drawerFlag = true;
    },
    handleClose(done) {
      this.drawerFlag = false;
    }
  }
};
</script>

<style>
.auditentry-version{
  text-decoration: underline;
  cursor: pointer;
  color: #1296db;
}
.toaudit-page{
  width: 100%;
  margin-top: 25px;
}
</style>