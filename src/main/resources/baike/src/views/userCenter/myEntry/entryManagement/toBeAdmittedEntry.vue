<template>
    <div>
        <el-table ref="multipleTable" :data="tableData" tooltip-effect="dark"
            style="width: 100%" @selection-change="handleSelectionChange"
            @row-click="jumpToEdit">
            <el-table-column type="selection" width="55"> </el-table-column>
            <el-table-column prop="name" label="词条名称" width="150"> </el-table-column>
            <el-table-column prop="name" label="保存时间" width="150"> </el-table-column>
            <el-table-column prop="name" label="操作" show-overflow-tooltip> </el-table-column>
        </el-table>
        <div style="margin-top: 20px">
            <el-button @click="admitFlag = true">提交</el-button>
            <el-button @click="deleteFlag = true">删除</el-button>
        </div>
        <el-dialog title="提示" :visible.sync="admitFlag" width="30%">
          <span>提交选中词条？</span>
          <span slot="footer" class="dialog-footer">
            <el-button @click="admitFlag = false">取 消</el-button>
            <el-button type="primary" @click="admitEntry">确 定</el-button>
          </span>
        </el-dialog>
        <el-dialog title="提示" :visible.sync="deleteFlag" width="30%">
          <span>放弃选中词条？</span>
          <span slot="footer" class="dialog-footer">
            <el-button @click="deleteFlag = false">取 消</el-button>
            <el-button type="primary" @click="deleteEntry">确 定</el-button>
          </span>
        </el-dialog>
    </div> 
</template>

<script>
export default {
  name: "toBeAdmittedEntry",
  data() {
    return {
      tableData: [],
      multipleSelection: [],
      disabledFlag: true,
      admitFlag : false,
      deleteFlag : false
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
    jumpToEdit(row){
      this.$router.push({path : "/entryedit", query :{id : row.id}});
    },
    admitEntry(){
      var array = new Array
      for(var i = 0; i < this.multipleSelection.length; i++){
        array.push(this.multipleSelection[i].id)
      }
      this.$axios
        .post("http://localhost:8081/api/user/admitEntry", {
          entryIds: array
        })
        .then(res => {
          if (res.data) {
            this.admitFlag = false;
            this.init();
            this.stateChange(4);
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
    deleteEntry(){
      var array = new Array
      for(var i = 0; i < this.multipleSelection.length; i++){
        array.push(this.multipleSelection[i].id)
      }
      this.$axios
        .post("http://localhost:8081/api/user/deleteEntry", {
          entryIds: array
        })
        .then(res => {
          if (res.data) {
            this.deleteFlag = false;
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
    stateChange(state){
      this.$emit('stateChange', state)
    }
  }
};
</script>
