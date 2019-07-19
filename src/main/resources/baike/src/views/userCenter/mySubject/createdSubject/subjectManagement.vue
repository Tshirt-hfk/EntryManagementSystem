<template>
  <div class="management">
    <div class="manage-back">
      <div class="manage-back-btn">
        <a style="cursor: pointer;" @click="backToSubject">
          <i class="el-icon-back"></i>
          返回专题页
        </a>
      </div>
      <div class="manage-back-title">
        {{subjectName}}
      </div>
    </div>
    <el-tabs type="border-card">
      <el-tab-pane lazy>
        <span slot="label">未发布词条</span>
        <unpublishedEntry ref="myUnpublishedEntry" v-on:stateChange="stateChange" v-bind:subjectId="subjectId"></unpublishedEntry>
      </el-tab-pane>
      <el-tab-pane lazy>
        <span slot="label">已发布词条</span>
        <publishedEntry ref="myPublishedEntry" v-on:stateChange="stateChange" v-bind:subjectId="subjectId"></publishedEntry>
      </el-tab-pane>
      <el-tab-pane lazy>
        <span slot="label">被领取词条</span>
        <drawedEntry ref="myDrawedEntry" v-on:stateChange="stateChange" v-bind:subjectId="subjectId"></drawedEntry>
      </el-tab-pane>
      <el-tab-pane lazy>
        <span slot="label">待审核词条</span>
        <toAudittedEntry  ref="myToAudittedEntry" v-on:stateChange="stateChange" v-bind:subjectId="subjectId"></toAudittedEntry>
      </el-tab-pane>
      <el-tab-pane lazy>
        <span slot="label">待提交词条</span>
        <toSubmittedEntry ref="myToSubmittedEntry" v-on:stateChange="stateChange" v-bind:subjectId="subjectId"></toSubmittedEntry>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import unpublishedEntry from "./subjectManagement/unpublishedEntry";
import publishedEntry from "./subjectManagement/publishedEntry";
import drawedEntry from "./subjectManagement/drawedEntry";
import toAudittedEntry from "./subjectManagement/toAudittedEntry";
import toSubmittedEntry from "./subjectManagement/toSubmittedEntry";
 
export default {
  name: "subjectManagement",
  props: ["subjectId", "subjectName"],
  components: {
    unpublishedEntry,
    publishedEntry,
    drawedEntry,
    toAudittedEntry,
    toSubmittedEntry
  },
  data() {
    return {
      
    };
  },
  mounted() {
  },
  methods: { 
    // 状态响应，更新对应组件数据
    stateChange(state){
      if(state==1 && this.$refs.myUnpublishedEntry){
        this.$refs.myUnpublishedEntry.init()
        return
      }
      if(state==2 && this.$refs.myPublishedEntry){
        this.$refs.myPublishedEntry.init()
        return
      }
      if(state==3 && this.$refs.myDrawedEntry){
        this.$refs.myDrawedEntry.init()
        return
      }
      if(state==4 && this.$refs.myToAudittedEntry){
        this.$refs.myToAudittedEntry.init()
        return
      }
      if(state==5 && this.$refs.myToSubmittedEntry) {
        this.$refs.myToSubmittedEntry.init()
        return
      }
    },
    backToSubject(){
      this.$emit("backToSubject");
    }
  }
};
</script>

<style>
.management {
  width: 100%;
  height: 500px;
}
.manage-back{
  width: 100%;
  height: 100px;
  text-align: left;
}
.manage-back-btn{
  height: 30px;
  font-size: 18px;
  color: #0683d8;
  text-decoration: none;
}
.manage-back-title{
  text-align: center;
  font-size: 25px;
}
</style>

