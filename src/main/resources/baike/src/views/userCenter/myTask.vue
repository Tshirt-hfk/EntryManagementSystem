<template>
    <div class="uc-section-mytask">
    <div class="uc-section-task-recommended">
        <div class="uc-tasrec-title">推荐任务</div>
        <div class="uc-tasrec-layout">
        <el-radio-group class="uc-tasrec-button" v-model="tabSelection1" style="margin-bottom: 30px;">
          <el-radio-button label="left">推荐词条</el-radio-button>
          <el-radio-button label="right">推荐专题</el-radio-button>
        </el-radio-group>
        <el-card style="width: 992px; height:600px">  <!--高度后期需要自适应 -->
          <div v-if="tabSelection1 == 'right'">
            
          </div>
          <div v-else>
            
          </div>
        </el-card>
        </div>
      </div>
      <div class="uc-section-task-mine">
        <div class="uc-tasrec-title">任务记录</div>
        <div class="uc-tasrec-layout">
        <el-radio-group class="uc-tasrec-button" v-model="tabSelection2" style="margin-bottom: 30px;">
          <el-radio-button label="left">我参加的</el-radio-button>
          <el-radio-button label="right">我创建的</el-radio-button>
        </el-radio-group>
        <div style="width: 1500px; height:600px">  <!--高度后期需要自适应 -->
          <div v-if="tabSelection2 == 'right'">
            <myCreatedSubject></myCreatedSubject>
          </div>
          <div v-else>
            <myJoinSubject></myJoinSubject>
          </div>
        </div>
        </div>
      </div>
    </div>
</template>

<script>

import myCreatedSubject from "./mySubject/createdSubject/myCreatedSubject"
import myJoinSubject from "./mySubject/joinedSubject/myJoinSubject"

export default {
    name: "myTask",
    components :{
        myCreatedSubject,
        myJoinSubject,
    },
    data() {
        return{
            userName: this.$store.state.name,
            tabSelection1: 'left',
            tabSelection2: 'left',
        };
    },
    methods:{

    }
}
</script>

<style>
.uc-section-task-recommended{
  padding: 20px 0 50px;
}
.uc-tasrec-title{
  height: 80px;
  text-align: center;
  line-height: 80px;
  font-size: 34px;
  color: #666;
  margin: 0;
  padding: 0;
}
.uc-tasrec-layout{
  width: 1500px;
  margin: 0 auto;
}
.uc-tasrec-button{
  margin-left: 395px;
  margin-top: 10px;
}
.uc-section-task-mine{
  padding: 20px 0 50px;
  background: #f8f8f8;
}
</style>