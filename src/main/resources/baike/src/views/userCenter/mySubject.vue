<template>
    <div class="uc-section-mysubject">
      <div class="uc-section-task-mine">
        <div class="uc-tasmine-title">专题任务</div>
        <div class="uc-tasmine-layout">
        <el-radio-group class="uc-tasmine-button" v-model="tabSelection" style="margin-bottom: 30px;">
          <el-radio-button label="left">我参加的</el-radio-button>
          <el-radio-button label="right">我创建的</el-radio-button>
        </el-radio-group>
        <el-card style="width: 1050px;">  <!--高度后期需要自适应 -->
          <div v-if="tabSelection == 'right'">
            <div v-if="subjectFlag == false">
                <myCreatedSubject ref="mycreatedsubject" v-on:entryInSubject="entryInSubject"></myCreatedSubject>
            </div>
            <div v-else>
                <subjectManagement :subjectId="subjectId" :subjectName="subjectName" v-on:backToSubject="backToSubject"></subjectManagement>
            </div>
          </div>
          <div v-else>
            <myJoinSubject></myJoinSubject>
          </div>
        </el-card>
        </div>
      </div>
    </div>
</template>

<script>

import myCreatedSubject from "./mySubject/createdSubject/myCreatedSubject"
import myJoinSubject from "./mySubject/joinedSubject/myJoinSubject"
import subjectManagement from "./mySubject/createdSubject/subjectManagement"

export default {
    name: "mySubject",
    components :{
        myCreatedSubject,
        myJoinSubject,
        subjectManagement
    },
    data() {
        return{
            userName: this.$store.state.name,
            tabSelection: 'left',
            subjectId: '0',
            subjectName: '',
            subjectFlag: false,
        };
    },
    methods:{
        entryInSubject(id, name){
            this.subjectId = id;
            this.subjectName = name;
            this.subjectFlag = true;
        },
        backToSubject(){
            this.subjectFlag = false;
        }
    }
}
</script>

<style>
.uc-tasmine-title{
  height: 80px;
  text-align: center;
  line-height: 80px;
  font-size: 34px;
  color: #666;
  margin: 0;
  padding: 0;
}
.uc-tasmine-layout{
  width: 1050px;
  margin: 0 auto;
}
.uc-tasmine-button{
  margin-left: 425px;
  margin-top: 10px;
}
.uc-section-task-mine{
  padding: 20px 0 50px;
  background: #f8f8f8;
}
</style>