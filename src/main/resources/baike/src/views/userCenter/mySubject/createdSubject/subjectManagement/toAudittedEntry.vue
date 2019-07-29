<template>
  <div>
    <el-input style="width: 300px; float: right;" v-model="searchValue" placeholder="请输入关键词"></el-input>
    <el-table :data="displayData" style="width: 100%">
      <el-table-column label="名称" width="180">
        <template slot-scope="scope">{{ scope.row.name }}</template>
      </el-table-column>
      <el-table-column label="领域" width="250">
        <template slot-scope="scope">
          <span v-for="item in scope.row.field" :key="item">{{item}},</span>
        </template>
      </el-table-column>
      <el-table-column label="领取人" width="180">
        <template slot-scope="scope">{{ scope.row.username }}</template>
      </el-table-column>
      <el-table-column align="left">
        <template slot-scope="scope">
          <el-button @click="getTaskContent(scope.row.id)">查 看</el-button>
          <el-button type="primary" @click="audit(scope.row, true)">通 过</el-button>
          <el-button type="danger" @click="rejectFlag = true">拒绝</el-button>
          <el-dialog title="未通过原因" :visible.sync="rejectFlag" width="600px">
            <span>
              <div style="margin: 15px 0;"></div>
              <el-input type="textarea" v-model="reason" maxlength="30" show-word-limit></el-input>
              <div style="margin: 10px 0;"></div>
            </span>
            <span slot="footer" class="dialog-footer">
              <el-button @click="rejectFlag = false">返 回</el-button>
              <el-button type="danger" @click="audit(scope.row, false);rejectFlag=false">确 定</el-button>
            </span>
          </el-dialog>
          <entryReview
            :relationData="relationData"
            :form="form"
            :drawerFlag="drawerFlag"
            v-on:handleClose="handleClose"
          ></entryReview>
        </template>
      </el-table-column>
    </el-table>
    <div class="audit-page">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :current-page="currentPage" :page-sizes="[10, 20, 50]" :page-size="pagesize"
        layout="total, sizes, prev, pager, next, jumper" :total="tableData.length" 
        style="width: 540px;margin: 0 auto"> </el-pagination>
    </div>
  </div>
</template>


<script>

import entryReview from "../../../../../components/entryReview"

export default {
  name: "toAudittedEntry",
  props: ["subjectId"],
  components:{
    entryReview,
  },
  watch:{
    searchValue:{
      handler(n, o){
        clearTimeout(this.timeout);
        this.timeout = setTimeout(() => {
          this.remoteMethod(n);
        }, 300);
      }
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
      timeout: null,
      currentPage: 1,
      pagesize: 10,
      entries: [],
      tableData: [],
      displayData: [],
      rejectFlag: false,
      reason: '',
    };
  },
  mounted() {
    this.init();
  },
  methods: {
    init() {
      this.$axios
        .post("/api/subjectMaker/getAssignment", {
          subjectId: new Number(this.subjectId),
          type: 4
        })
        .then(res => {
          if (res.data.data) {
            this.entries = res.data.data.assignments;
            this.tableData = res.data.data.assignments;
            this.displayData = res.data.data.assignments.slice(0, 10);
          } else {
            //this.$message({
             // message: res.data.msg
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
        .post("/api/subjectMaker/auditTask", {
          taskId: row.id,
          assignmentId: row.assignmentId,
          userId: row.userId,
          reason: this.reason,
          pass: pass
        })
        .then(res => {
          if (res.data) {
            this.init();
            this.stateChange(5);
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
          .post("/api/subjectMaker/getTaskContent", {
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
.audit-page{
  width: 100%;
  margin-top: 25px;
}
</style>