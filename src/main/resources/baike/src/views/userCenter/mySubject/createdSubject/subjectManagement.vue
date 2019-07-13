<template>
  <div class="management">
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
  components: {
    unpublishedEntry,
    publishedEntry,
    drawedEntry,
    toAudittedEntry,
    toSubmittedEntry
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
    }
  }
};
</script>

<style>
.management {
  width: 100%;
  height: 500px;
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

