<template>
  <div class="content">
    <div v-if="subjects.length == 0">
      <a class="nothing-a" @click="toSubject">
        <div class="nothing-a-display">
          <i class="el-icon-circle-plus nothing-icon"> </i>
          <p>您还没有创建专题</p>
          <p>赶紧创建吧</p>
        </div>
      </a>
    </div>
    <div v-else>
      <div class="mycreatesub-searchbar">
          <mySearch style="float:right" v-on:remoteMethod="remoteMethod" :options="options" :value="value" :loading="loading"></mySearch>
      </div>
      <template v-for="subject in subjects">
        <el-card class="box-card" :key="subject.id" :body-style="{ padding: '0px' }">
          <img class="subject-image" src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png">
          <div style="padding: 14px;">
            <div style="display: inline;">
              <span style="color:#338de6;">{{subject.name}}</span>
              <el-button class="button" size="mini" @click="entryInSubject(subject.id,subject.name)">查看</el-button>
            </div>
            <div class="subject-bottom">
              <i class="el-icon-time" style="color: #cdcfd1; font-size:14px"></i>
              <span style="font-size:14px; margin-right:5px; color: #cdcfd1;">剩余时间{{deadTime}}天</span>
              <i class="el-icon-coin" style="color: #cdcfd1; font-size:14px"></i>
              <span style="font-size:14px; color: #cdcfd1;">完成词条{{finishedSubject}}个</span>
            </div>
          </div>
        </el-card>
      </template>
    </div>
  </div>
</template>

<script>

import mySearch from "../../../../components/mySearch"

export default {
  name: "myCreatedSubject",
  components: {
    mySearch,
  },
  data() {
    return {
      status : this.$store.state.status,
      subjects: [],
      deadTime: '0',
      finishedSubject: '0',
      defaultCard: false,
      options: [],
      loading: false,
      value: [],
    };
  },
  mounted() {
    this.init()
  },
  methods: {
    init(){
      // 初始化数据
        this.$axios
          .post("http://localhost:8081/api/subjectMaker/getSubject")
          .then(res => {
            if (res.data.data)
              this.subjects = res.data.data.subjects;
          })
          .catch(error => {
            if (error.response) {
              
            }
          });
    },
    entryInSubject(id, name) {
      window.console.log(id);
      this.$emit('entryInSubject', id, name);
    },
    toSubject(){
      if(this.status == '1'){
        this.$alert('您还没有专题创建权限', '提示', {
          confirmButtonText: '确定',
          callback: action => {
            
          }
        });
      }else
        this.$router.push("/subjectcreate");
    },
    remoteMethod(query){
        this.loading = true;
        this.$axios
            .post("http://localhost:8081/api/user/searchSubject", {keyword:query})
            .then(res => {
                if(res.data.data){
                    window.console.log("woxiaole")
                    this.options = res.data.data.subjects;
                }else{
                    window.console.log("wozhale")
                }
                this.loading = false;
            })
            .catch(error => {
                      
            });
    }
  }
};
</script>

<style scoped>
.content {
  margin-left: 30px;
  width: 1200px;
}
.mycreatesub-searchbar{
  width: 1060px;
  height: 60px;
}
.nothing-a{
  background: #f0f0f0;
  text-align: center;
  cursor: pointer;
  position: relative;
  transition: .15s linear;
  width: 234px;
  height: 210px;
  display: block;
  border: solid 1px #d5d5d5;
  margin: 10px 12px 15px 10px;
  font-size: 0;
  text-decoration: none;
}
.nothing-a:hover {
  border: solid 1px #52a3f5;
}
.nothing-a:hover .nothing-icon{
  background: #52a3f5;
}
.nothing-a-display{
  width: 100%;
  height: 150px;
  margin-top: 33px;
}
.nothing-a-display p{
  font-size: 18px;
  line-height: 13px;
  color: #888;
  display: block;
  margin-top: 11px;
}
.nothing-icon{
  font-size: 70px;
  color: #ffffff;
  background: #515151;
  border-radius: 50%;
}
.box-card {
  float: left;
  margin-top: 10px;
  margin-left: 30px;
  margin-bottom: 15px;
  height: 210px;
  width: 236px;
}
.box-card:hover{
  border: solid 1px #52a3f5;
}
.subject-image{
  width: 234px;
  height: 130px;
}
.subject-bottom{
  margin-top: 13px;
  line-height: 12px;
}
.button{
  float: right;
}
.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both;
}
</style>


