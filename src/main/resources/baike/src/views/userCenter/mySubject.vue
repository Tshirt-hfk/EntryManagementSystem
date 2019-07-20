<template>
    <div class="uc-section-mysubject">
      <div class="uc-section-task-mine">
        <div class="uc-tasmine-title">专题任务</div>
        <div class="uc-tasmine-layout">
          <div class="uc-tasmine-navbtn">
            <el-radio-group class="uc-tasmine-button" v-model="tabSelection" v-on:change="change">
              <el-radio-button label="left">我参加的</el-radio-button>
              <el-radio-button label="right">我创建的</el-radio-button>
            </el-radio-group>
            <div class="uc-apply-info">
                  <a href="" class="uc-btn-apply" target="_blank">申请专题制作人权限</a>
                  <p style="font-style:normal;font-size: 10px; margin-top: 13px;">获得众智化系统认证，享受更多权限!</p>
            </div>
          </div>
          <el-card style="width: 1200px;min-height:600px">  <!--高度后期需要自适应 -->
            <router-view ></router-view>
          </el-card>
        </div>
      </div>
    </div>
</template>

<script>

export default {
    name: "mySubject",
    data() {
        return{
            tabSelection: 'left',
        };
    },
    mounted(){
    },
    methods:{
        change() {
          if(this.tabSelection=="left")
            this.$router.push("./joinedsubject")
          else if(this.tabSelection=="right")
            this.$router.push("./createdsubject")
        },
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
  width: 1200px;
  margin: 0 auto;
}
.uc-tasmine-navbtn{
  height: 60px;
  width: 100%;
  margin-bottom: 30px;
}
.uc-tasmine-button{
  margin-left: 500px;
  margin-top: 10px;
}
.uc-apply-info{
  padding: 15px 20px 0;
  float: right;
}
.uc-btn-apply{
  color: #fff;
  border: solid 1px #f18167;
  border-radius: 6px;
  background: #f18167;
  text-align: center;
  padding: 5px 5px 4px;
  font-size: 15px;
  text-decoration: none;
  margin-left: 25px; 
}
.uc-btn-apply:hover{
  color:#13227a;
}
.uc-section-task-mine{
  padding: 20px 0 50px;
}
</style>