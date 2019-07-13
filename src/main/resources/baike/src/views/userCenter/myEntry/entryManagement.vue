<template>
  <div class="layout">
  <div class="management">
    <div class="section_title">
      <a class="actived">  我的版本  </a>   <!--连接未完成啊 -->
      <a href="">  我的收藏 </a>
    </div>
    <el-tabs class="border_card" type="border-card">
      <el-tab-pane lazy>
        <span slot="label">已通过词条</span>
        <passedEntry ref="myPassedEntry" v-on:stateChange="stateChange" v-bind:subjectId="subjectId"></passedEntry>
      </el-tab-pane>
      <el-tab-pane lazy>
        <span slot="label">待审核词条</span>
        <toBeAuditedEntry ref="myBeAuditedEntry" v-on:stateChange="stateChange" v-bind:subjectId="subjectId"></toBeAuditedEntry>
      </el-tab-pane>
      <el-tab-pane lazy>
        <span slot="label">未通过词条</span>
        <failPassEntry ref="myFailPassEntry" v-on:stateChange="stateChange" v-bind:subjectId="subjectId"></failPassEntry>
      </el-tab-pane>
      <el-tab-pane lazy>
        <span slot="label">待提交词条</span>
        <toBeAdmittedEntry  ref="myBeAdmittedEntry" v-on:stateChange="stateChange" v-bind:subjectId="subjectId"></toBeAdmittedEntry>
      </el-tab-pane>
    </el-tabs>
  </div>
  </div>
</template>

<script>
import passedEntry from "./entryManagement/passedEntry";
import toBeAuditedEntry from "./entryManagement/toBeAuditedEntry";
import failPassEntry from "./entryManagement/failPassEntry";
import toBeAdmittedEntry from "./entryManagement/toBeAdmittedEntry";
 
export default {
  name: "entryManagement",
  components: {
    passedEntry,
    toBeAuditedEntry,
    failPassEntry,
    toBeAdmittedEntry
  },
  data() {
    return {
      subjectId: this.$route.query.id,
    };
  },
  mounted() {
  },
  methods: { 
    // 状态响应，更新对应组件数据
    stateChange(state){
      if(state==3 && this.$refs.myBeAdmittedEntry){
        this.$refs.myBeAdmittedEntry.init()
        return
      }
      if(state==4 && this.$refs.myBeAuditedEntry){
        this.$refs.myBeAuditedEntry.init()
        return
      }
      if(state==5 && this.$refs.myPassedEntry){
        this.$refs.myPassedEntry.init()
        return
      }
    }
  }
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
</style>

