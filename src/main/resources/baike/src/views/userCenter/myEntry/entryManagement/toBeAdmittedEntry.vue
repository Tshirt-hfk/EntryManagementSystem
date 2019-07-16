<template>
    <div>
        <el-table ref="multipleTable" :data="tableData" tooltip-effect="dark"
            style="width: 100%" @selection-change="handleSelectionChange"
            @cell-mouse-enter="getId">
            <el-table-column type="selection" width="55"> </el-table-column>
            <el-table-column type="expand">
              <template slot-scope="props">
                <el-form label-position="left" inline class="demo-table-expand">
                  <el-form-item label="词条内容">
                    <span>{{ props.row.content }}</span>
                  </el-form-item>
                </el-form>
              </template>
            </el-table-column>
            <el-table-column prop="name" label="词条名称" width="100"> </el-table-column>
            <el-table-column prop="field" label="领域" width="80"> </el-table-column>
            <el-table-column prop="time" label="保存时间" width="180"> </el-table-column>
            <el-table-column prop="endTime" label="结束时间" width="180"> </el-table-column>
            <el-table-column prop="name" label="状态" show-overflow-tooltip> </el-table-column>
            <el-table-column align="right">
              <template>
                <el-button size="mini" type="primary" @click="jumpToEdit">编辑</el-button>
              </template>
            </el-table-column>
        </el-table>
        <div style="margin-top: 20px">
            <el-button @click="admitFlag = true">提交</el-button>
            <el-button @click="deleteFlag = true">放弃</el-button>
        </div>
        <el-dialog title="修改原因" :visible.sync="admitFlag" width="30%">
          <span>
            <el-button size="mini" @click="reason='更正错误'" plain>更正错误</el-button>
            <el-button size="mini" @click="reason='内容扩充'" plain>内容扩充</el-button>
            <el-button size="mini" @click="reason='删除冗余'" plain>删除冗余</el-button>
            <el-button size="mini" @click="reason='目录结构'" plain>目录结构</el-button>
            <el-button size="mini" @click="reason='概述'" plain>概述</el-button>
            <el-button size="mini" @click="reason='图片'" plain>图片</el-button>
            <div style="margin: 15px 0;"></div>
            <el-input type="textarea" v-model="reason" maxlength="30" show-word-limit></el-input>
            <div style="margin: 10px 0;"></div>
            <el-alert title="请在提交前确认" type="warning"
            description="不要乱搞"
            show-icon>  </el-alert>
          </span>
          <span slot="footer" class="dialog-footer">
            <el-button @click="admitFlag = false">返 回</el-button>
            <el-button type="primary" @click="admitEntry">提 交</el-button>
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
      deleteFlag : false,
      entryId: 0,
      reason: ""
    };
  },
  mounted() {
    this.init();
  },
  methods: {
    init() {
      this.$axios
        .post("http://localhost:8081/api/user/getEntry", {
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
    getId(row){
      this.entryId = row.id;
    },
    jumpToEdit(){
      this.$router.push({path : "/entryedit", query :{id : this.entryId}});
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
        .post("http://localhost:8081/api/user/giveUpTask", {
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

<style>
  .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }
</style>