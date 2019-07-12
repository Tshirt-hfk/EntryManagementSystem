<template>
  <div class="layout">
    <div class="management">
      <div class="section_title">
        <a href="" class="actived">  我的版本  </a>   <!--连接未完成啊 -->
        <a href="">  我的收藏 </a>
      </div>
      <el-tabs class="border_card" type="border-card">
        <el-tab-pane>
          <span slot="label">已通过版本</span>
          <template>
          <el-table
            :data="tableData1"
            style="width: 100%"
            max-height="500">
            <el-table-column
              prop="name"
              label="词条名称"
              width="200">
            </el-table-column>
            <el-table-column
              prop="version_pass"
              label="全部版本"
              width="150">
            </el-table-column>
            <el-table-column
              prop="time_pass"
              label="通过时间"
              width="150">
            </el-table-column>
            <el-table-column
              prop="modify_pass"
              label="被其它人修改版本"
              width="150">
            </el-table-column>
          </el-table>
        </template>
        </el-tab-pane>
        <el-tab-pane>
          <span slot="label">待通过版本</span>
          <template>
          <el-table
            :data="tableData2"
            style="width: 100%"
            max-height="500">
            <el-table-column
              prop="name_wait"
              label="词条名称"
              width="300">
            </el-table-column>
            <el-table-column
              prop="version_wait"
              label="版本"
              width="150">
            </el-table-column>
            <el-table-column
              prop="time_wait"
              label="提交时间"
              width="150">
            </el-table-column>
          </el-table>
        </template>
        </el-tab-pane>      
        <el-tab-pane>
          <span slot="label">未通过版本</span>
          <template>
          <el-table
            :data="assignments[2]"
            style="width: 100%"
            max-height="500">
            <el-table-column
              prop="name_no"
              label="词条名称"
              width="150">
            </el-table-column>
            <el-table-column
              prop="reason_no"
              label="未通过原因"
              width="150">
            </el-table-column>
            <el-table-column
              prop="version_no"
              label="版本"
              width="100">
            </el-table-column>
            <el-table-column
              prop="time_no_sub"
              label="提交时间"
              width="125">
            </el-table-column>
            <el-table-column
              prop="time_no_reject"
              label="未通过时间"
              width="125">
            </el-table-column>
          </el-table>
        </template>
        </el-tab-pane>
        <el-tab-pane>
          <span slot="label">草稿箱</span>
          <template>
            <el-table
              ref="multipleTable"
              :data="tableData4"
              tooltip-effect="dark"
              style="width: 100%"
              @selection-change="handleSelectionChange">
              <el-table-column
                type="selection"
                width="55">
              </el-table-column>
              <el-table-column
                prop="name_draft"
                label="词条名称"
                width="150">
              </el-table-column>
              <el-table-column
                prop="save_time"
                label="保存时间"
                width="150">
              </el-table-column>
              <el-table-column
                prop="operation"
                label="操作"
                show-overflow-tooltip>
              </el-table-column>
            </el-table>
            <div style="margin-top: 20px">
              <el-button @click="toggleSelection()">删除</el-button>
            </div>
          </template>
        </el-tab-pane>
        <el-tab-pane>
          <span slot="label">待提交词条</span>
          <template v-for="assignment in assignments[4]">
            <div class="lemma cmn-ellipsis" :key="assignment.id">
              <span class="lemmaName" :id="assignment.id">{{assignment.name}}</span>
              <span class="overlay"></span>
              <a @click="this.$route.push({path:'/entrycompare', query:{id:assignment.id}})" class="get">查看</a>
              <a  class="get">提交</a>
            </div>
          </template>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>

export default {
  name: "subjectManagement",
  data() {
    return {
      tableData1: [{
          name: '王小虎',
 //         version_pass: '上海',
  //        time_pass: '普陀区',
  //        modify_pass: '上海市普陀区金沙江路 1518 弄'
        }],
      tableData2: [{
          name_wait: '王小虎',
          version_wait: '上海',
          time_wait: '普陀区',
        }],
      subjectId: this.$route.query.id,
      assignments: [
        [
          {
            name: ""
          }
        ],
        [
          {
            name: "test2"
          }
        ],
        [
          {
            name: "test3"
          }
        ],
        [
          {
            name: "test4"
          }
        ]
      ]
    };
  },
  mounted() {
    if(this.$store.state.name !== ''){
      this.$axios
      .get("http://localhost:8081/api/user/entry/get", {
      params: { name: this.$store.state.name }
      })
      .then(res => {
        if(res.data.data){
          this.tableData1 = res.data.data.assignments[0];
          this.assignments = res.data.data.assignments;
        }else{
          this.$message({
            message: res.data.msg
          })
        }
      })
      .catch(error =>{
        if (error.response) {
          this.$message({
            message: error.response.data.msg,
            type: "warning"
          });
        }
      })
    }
  }
/*
    if(this.subjectId){
      window.console.log(this.subjectId)
       this.$axios
      .get("http://localhost:8081/api/subjectmaker/assignment/get", {
        params: { id: this.subjectId }
      })
      .then(res => {
        if (res.data.data) {
          this.assginments = res.data.data.assginments;
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
    } */
   
};
</script>

<style>
.layout {
  width: 980px;
  margin: 0 auto;
}
.management {
  width: 100%;
  height: 500px;
}
.section_title{
    height: 80px;
    text-align: center;
    line-height: 80px;
    font-size: 34px;
    color: #666;
}
.section_title a.actived{
    font-size: 20px;
    color: #666;
    cursor: default;
    margin: 0 20px;
}
.section_title a{
    transition: .3s ease-out;
    font-size: 16px;
    outline: 0;
    text-decoration: none;
    color: #338de6;
}
.border_card{
  width: 900px;
  margin: 0 auto; 
}

.cmn-ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  word-wrap: normal;
}
.lemma {
  position: relative;
  float: left;
  width: 178px;
  color: #333;
  font-size: 16px;
  line-height: 68px;
  text-align: center;
  padding: 0 5px;
  margin-right: 7px;
  margin-bottom: 7px;
  border: 1px solid #a2aaaf;
}
.lemma:hover .get {
  filter: alpha(opacity=100);
  opacity: 1;
  z-index: 2;
}
.lemma:hover .overlay {
  filter: alpha(opacity=60);
  opacity: 0.6;
  z-index: 1;
}
.overlay {
  filter: alpha(opacity=0);
  opacity: 0;
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background-color: #f3f3f3;
  transition: 0.2s;
}
.get {
  filter: alpha(opacity=0);
  opacity: 0;
  position: absolute;
  color: #fff;
  line-height: 40px;
  height: 40px;
  width: 90px;
  top: 50%;
  left: 50%;
  margin-top: -20px;
  margin-left: -45px;
  border-radius: 4px;
  background-color: #459df5;
  transition: 0.2s;
  cursor: pointer;
}
.get:hover {
  background-color: #1e89f3;
}
</style>

