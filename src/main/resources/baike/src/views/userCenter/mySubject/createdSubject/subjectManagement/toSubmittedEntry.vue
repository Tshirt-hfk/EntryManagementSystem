<template>
  <div>
    <el-input style="width: 300px; float: right;" v-model="searchValue" placeholder="请输入关键词"></el-input>
    <el-table
      ref="multipleTable"
      :data="displayData"
      tooltip-effect="dark"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="100"></el-table-column>
      <el-table-column label="名称" width="150">
        <template slot-scope="scope">{{ scope.row.name }}</template>
      </el-table-column>
      <el-table-column label="领域" width="250">
        <template slot-scope="scope">
          <span v-for="item in scope.row.field" :key="item">{{item}},</span>
        </template>
      </el-table-column>
      <el-table-column align="right">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="getAssignmentContent(scope.row.id)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="submit-page">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :current-page="currentPage" :page-sizes="[10, 20, 50]" :page-size="pagesize"
        layout="total, sizes, prev, pager, next, jumper" :total="tableData.length" 
        style="width: 540px;margin: 0 auto"> </el-pagination>
    </div>
    <div style="margin-top: 20px">
      <el-button @click="toSubmitDatabase">提交</el-button>
      <el-button @click="publishFlag = true">重新发布</el-button>
    </div>
    <el-dialog title="发布原因" :visible.sync="publishFlag" width="800px" top="30vh" center>
      <div style="width: 100%; text-align: center">
        <el-tag
          style="font-size: 16px"
          :key="tag"
          v-for="tag in dynamicReasons"
          closable
          :disable-transitions="false"
          @close="handleTagClose(tag)">
          {{tag}}</el-tag>
        <el-cascader
          class="select-new-tag"
          v-if="selectVisible"
          placeholder="请选择原因"
          ref="saveTagSelect"
          :options="reasonOptions"
          v-model="selectValue"
          :props="{ multiple: true, checkStrictly: true, emitPath: false }"
          :show-all-levels="false"
          filterable
          clearable
          @change="addReason"
        ></el-cascader>
        <el-button v-else class="button-new-tag" size="small" @click="showSelect">添加原因</el-button>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="publishFlag = false">返 回</el-button>
        <el-button type="primary" @click="publishEntry">提 交</el-button>
      </span>
    </el-dialog>
    <entryReview
      :relationData="relationData"
      :form="form"
      :drawerFlag="drawerFlag"
      v-on:handleClose="handleClose"
    ></entryReview>
  </div>
</template>


<script>

import entryReview from "../../../../../components/entryReview"

export default {
  name: "toSubmittedEntry",
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
      multipleSelection: [],
      publishFlag: false,
      dynamicReasons: [],
      selectValue: '',
      selectVisible: false,
      reasonOptions: [
        {
            value: "休闲",
            label: "休闲",
            children: [
              {
                value: "活动",
                label: "活动",
                children: [
                  { value: "旅游", label: "旅游" },
                ]
              },
              {
                value: "娱乐",
                label: "娱乐",
                children: [
                  { value: "电影", label: "电影" },
                  { value: "电视剧", label: "电视剧" },
                ]
              },
              {
                value: "制品",
                label: "制品",
                children: [{ value: "玩具", label: "玩具" }]
              }
            ]
          },
      ],
      inputValue: '',
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
          type: 5
        })
        .then(res => {
          if (res.data.data) {
            this.entries = res.data.data.assignments;
            this.tableData = res.data.data.assignments;
            this.displayData = res.data.data.assignments.slice(0, 10);
          } else {
            //this.$message({
            //  message: res.data.msg
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
    toSubmitDatabase(){
      var array = new Array();
      for (var i = 0; i < this.multipleSelection.length; i++) {
        array.push(this.multipleSelection[i].id);
      }
      this.$axios
        .post("/api/subjectMaker/submitEntry", {
          entryIds: array,
          subjectId: new Number(this.subjectId),
        })
        .then(res => {
          if (res.data) {
            this.init();
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
    publishEntry() {
      var array = new Array();
      for (var i = 0; i < this.multipleSelection.length; i++) {
        array.push(this.multipleSelection[i].id);
      }
      this.$axios
        .post("/api/subjectMaker/publishAssignment", {
          entryIds: array,
          subjectId: new Number(this.subjectId),
          reason: this.dynamicReasons,
        })
        .then(res => {
          if (res.data) {
            this.init();
            this.stateChange(2);
            this.publishFlag = false;
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
    handleTagClose(tag) {
      this.dynamicReasons.splice(this.dynamicReasons.indexOf(tag), 1);
    },
    getAssignmentContent(id){
      window.console.log(id)
      if(!this.form.entryName){
        this.$axios
          .post("/api/subjectMaker/getAssignmentContent", {
              assignmentId: new Number(id)
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
    },
    showSelect() {
      this.selectVisible = true;
    },
    addReason(){
      let selectValue = this.selectValue;
      if (selectValue) {
        this.dynamicReasons.push(selectValue[0]);
      }
      this.selectVisible = false;
      this.selectValue = '';
    },
  }
};
</script>

<style>
.submit-page{
  width: 100%;
  margin-top: 25px;
}
</style>