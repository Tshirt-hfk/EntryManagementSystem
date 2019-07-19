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
      <el-button @click="publishFlag = true">发布词条</el-button>
    </div>
    <el-dialog title="新建词条" :visible.sync="dialogFormVisible" center width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="名称" label-width="50px">
          <el-input v-model="form.name" autocomplete="on"></el-input>
        </el-form-item>
        <el-form-item label="分类" label-width="50px">
          <el-cascader v-model="form.field" placeholder="请选择领域" :options="options" filterable></el-cascader>
        </el-form-item>
      </el-form>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="createEntry">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="发布原因" :visible.sync="publishFlag" width="650px">
      <span>
        <el-button style="margin-bottom:10px" size="mini" @click="inputValue='词条新建';handleInputConfirm()" plain>词条新建</el-button>
        <el-button size="mini" @click="inputValue='正文缺少图片';handleInputConfirm()" plain>正文缺少图片</el-button>
        <el-button size="mini" @click="inputValue='参考资料缺失';handleInputConfirm()" plain>参考资料缺失</el-button>
        <el-button size="mini" @click="inputValue='基本信息栏缺失';handleInputConfirm()" plain>基本信息栏缺失</el-button>
        <el-button size="mini" @click="inputValue='概述图不清晰';handleInputConfirm()" plain>概述图不清晰</el-button>
        <el-button style="margin-left: -1px" size="mini" @click="inputValue='概述缺失或过短';handleInputConfirm()" plain>概述缺失或过短</el-button>
        <div style="margin: 15px 0;"></div>
        <div class="publish-tag" >
          <el-tag style="font-size: 15px" :key="tag" v-for="tag in dynamicTags" closable
            :disable-transitions="false" @close="handleClose(tag)">{{tag}}</el-tag>
        </div>
        <div style="margin: 10px 0;"></div>
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="publishFlag = false">返 回</el-button>
        <el-button type="primary" @click="publishEntry">提 交</el-button>
      </span>
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
      reason: '',
      tableData: [],
      multipleSelection: [],
      form: {
        name: "",
        field: ""
      },
      dialogFormVisible: false,
      publishFlag: false,
      dynamicTags: [],
      inputValue: '',
      options: [
        {
          value: '动物',
          label: '动物'
        }
      ]
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
    },
    handleInputConfirm() {
        let inputValue = this.inputValue;
        if (inputValue && this.dynamicTags.indexOf(inputValue) === -1) {
            this.dynamicTags.push(inputValue);
        }
        this.inputValue = '';
    },
    handleClose(tag) {
        this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1);
    },
  }
};
</script>

<style>
.el-tag + .el-tag {
    margin-left: 10px;
    margin-top: 5px;
}
.publish-tag{
  width: 610px;
  height: 80px;
  border: solid 1px #cdcdcd;
}
</style>
