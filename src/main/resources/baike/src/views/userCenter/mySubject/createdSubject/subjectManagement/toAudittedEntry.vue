<template>
  <div>
    <el-table :data="tableData" style="width: 100%">
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
          <el-button @click="see(scope.row)">查 看</el-button>
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
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>


<script>
export default {
  name: "toAudittedEntry",
  props: ["subjectId"],
  data() {
    return {
      tableData: [],
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
        .post("http://localhost:8081/api/subjectMaker/getAssignment", {
          subjectId: new Number(this.subjectId),
          type: 4
        })
        .then(res => {
          if (res.data.data) {
            this.tableData = res.data.data.assignments;
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
        .post("http://localhost:8081/api/subjectMaker/auditTask", {
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
    }
  }
};
</script>
