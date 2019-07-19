<template>
  <div>
    <el-table
      ref="multipleTable"
      :data="tableData"
      tooltip-effect="dark"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="120"></el-table-column>
      <el-table-column label="名称" width="120">
        <template slot-scope="scope">{{ scope.row.name }}</template>
      </el-table-column>
      <el-table-column label="分类" width="240">
        <template slot-scope="scope">{{ scope.row.field }}</template>
      </el-table-column>
    </el-table>
    <div style="margin-top: 20px">
      <el-button @click="dialogFormVisible = true">新建词条</el-button>
      <el-button @click="publishEntry">发布词条</el-button>
    </div>
    <el-dialog title="新建词条" :visible.sync="dialogFormVisible" center width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="名称" label-width="50px">
          <el-input v-model="form.name" autocomplete="on"></el-input>
        </el-form-item>
        <el-form-item label="分类" label-width="50px">
          <el-cascader placeholder="请选择领域" :options="options" filterable></el-cascader>
        </el-form-item>
      </el-form>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="createEntry">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>


<script>
import subjectIndexVue from '../../../../subject/subjectIndex.vue';
export default {
  name: "unpublishedEntry",
  props: ["subjectId"],
  data() {
    return {
      tableData: [],
      multipleSelection: [],
      form: {
        name: "",
        field: ""
      },
      dialogFormVisible: false,
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
          type: 1
        })
        .then(res => {
          if (res.data.data) {
            window.console.log(res.data.data);
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
    createEntry() {
      this.$axios
        .post("http://localhost:8081/api/subjectMaker/createEntry", {
          subjectId: new Number(this.subjectId),
          name: this.form.name,
          field: this.form.field
        })
        .then(res => {
          if (res.data) {
            this.dialogFormVisible = false;
            this.init();
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
    publishEntry() {
      var array = new Array
      for(var i=0;i<this.multipleSelection.length;i++){
        array.push(this.multipleSelection[i].id)
      }
      window.console.log(array)
      this.$axios
        .post("http://localhost:8081/api/subjectMaker/publishAssignment", {
          entryIds: array,
          subjectId: new Number(this.subjectId)
        })
        .then(res => {
          if (res.data) {
            this.dialogFormVisible = false;
            this.init();
            this.stateChange(2);
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
      window.console.log(state)
      this.$emit('stateChange', state)
    }
  }
};
</script>
