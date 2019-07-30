<template>
  <div class="content">
    <div v-if="allsubjects.length == 0">
      <a class="nothing-a" @click="toSubject">
        <div class="nothing-a-display">
          <i class="el-icon-circle-plus nothing-icon"></i>
          <p>您还没有创建专题</p>
          <p>赶紧创建吧</p>
        </div>
      </a>
    </div>
    <div v-else>
      <template v-if="state==1">
        <template v-for="subject in subjects">
          <el-card class="box-card" :key="subject.id" :body-style="{ padding: '0px' }">
            <img class="subject-image" :src="subject.imageUrl" />
            <div style="padding: 12px 12px 0 12px;">
              <div>
                <span style="color:#338de6;float:left">{{subject.name}}</span>
                <el-button
                  style="float:right"
                  size="mini"
                  @click="entryInSubject(subject.id,subject.name)"
                >查看</el-button>
                <div class="clear"></div>
              </div>
              <div class="subject-bottom">
                <i class="el-icon-time" style="color: #cdcfd1; font-size:14px"></i>
                <span style="font-size:14px; margin-right:5px; color: #cdcfd1;">剩余时间{{subject.deadline | getDay}}天</span>
                <i class="el-icon-coin" style="color: #cdcfd1; font-size:14px"></i>
                <span style="font-size:14px; color: #cdcfd1;">完成{{subject.finishNum}}个</span>
              </div>
            </div>
          </el-card>
        </template>
      </template>
      <template v-else>
        <subjectManagement
          :subjectId="subjectId"
          :subjectName="subjectName"
          v-on:backToSubject="backToSubject"
        ></subjectManagement>
      </template>
    </div>
  </div>
</template>

<script>
import mySearch from "../../../../components/mySearch";
import subjectManagement from "./subjectManagement";

export default {
  name: "myCreatedSubject",
  components: {
    mySearch,
    subjectManagement
  },
  data() {
    return {
      status: this.$store.state.status,
      state: 1,
      allsubjects: [],
      subjects: [],
      subjectId: 0,
      subjectName: "",
      defaultCard: false,
      options: [],
      loading: false,
      value: [],
    };
  },
  mounted() {
    this.init();
  },
  filters: {
    getDay: function (end_time) {
      var day = Math.ceil((end_time - new Date().getTime())/86400000)
      if(day < 0)
          day = 0
      return day
    }
  },
  methods: {
    init() {
      // 初始化数据
      this.$axios
        .post("/api/subjectMaker/getSubject")
        .then(res => {
          if (res.data.data){
            this.subjects = res.data.data.subjects;
            this.allsubjects = res.data.data.subjects;
          }
        })
        .catch(error => {
          if (error.response) {
          }
        });
    },
    entryInSubject(id, name) {
      this.subjectId = id;
      this.subjectName = name;
      this.state = 2;
    },
    backToSubject() {
      this.state = 1;
    },
    toSubject() {
      if (this.status == "1") {
        this.$alert("您还没有专题创建权限", "提示", {
          confirmButtonText: "确定",
          callback: action => {}
        });
      } else this.$router.push("/subjectcreate");
    },
    remoteMethod(query) {
      if (query !== "") {
        this.subjects = this.allsubjects.filter(subject => {
          return subject.name.toLowerCase().indexOf(query.toLowerCase()) > -1;
        });
        this.displayData = this.subjects.slice(0, this.pagesize);
      } else {
        this.subjects = this.allsubjects;
        this.displayData = this.subjects.slice(0, this.pagesize);
      }
    },
  }
};
</script>

<style scope>
.content {
  margin-left: 30px;
  width: 1200px;
  max-height: 1210px;
}
.nothing-a {
  background: #f0f0f0;
  text-align: center;
  cursor: pointer;
  position: relative;
  transition: 0.15s linear;
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
.nothing-a:hover .nothing-icon {
  background: #52a3f5;
}
.nothing-a-display {
  width: 100%;
  height: 150px;
  margin-top: 33px;
}
.nothing-a-display p {
  font-size: 18px;
  line-height: 13px;
  color: #888;
  display: block;
  margin-top: 11px;
}
.nothing-icon {
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
.box-card:hover {
  border: solid 1px #52a3f5;
}
.subject-image {
  width: 234px;
  height: 130px;
}
.subject-bottom {
  margin-top: 13px;
  line-height: 12px;
}
.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both;
}
.clear {
  clear: both;
  width: 0;
  height: 0;
  margin: 0;
  padding: 0;
  border: 0;
}
</style>


