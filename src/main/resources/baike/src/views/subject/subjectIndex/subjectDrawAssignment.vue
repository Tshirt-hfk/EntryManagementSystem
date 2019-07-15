<template>
  <div class="layout">
    <!-- 任务词条 -->
    <div class="taskLemma">
      <div class="theaderWrap">
        <h3 class="theader pv30 dib">任务词条</h3>
        <input type="text" class="searchBox" placeholder="请输入词条名进行筛选" />
      </div>
      <div class="content">
        <div class="viewport cmn-clearfix">
          <div class="lemmaList" style>
            <myAssignmentCard
              v-for="assignment in assignments"
              v-bind:assignment="assignment"
              :key="assignment.id"
              v-on:refresh="getTasks"
            ></myAssignmentCard>
          </div>
        </div>
        <div id="taskLemmaPager" pager-type="tpager" class="wgt_horPager" style="display: none;"></div>
        <p class="f14 mt25">领取提示：每次只能选取一个词条，编辑提交之后可以继续领取。</p>
      </div>
    </div>
    <!-- 参与进度 -->
    <div class="progress cmn-clearfix">
      <div class="myProgress">
        <div class="theaderWrap">
          <h3 class="theader">我的参与进度</h3>
        </div>
        <div class="myLemmaArea cmn-clearfix">
          <div class="progressList">
            <template v-if="status===0">
              <div class="empty">
                快来参加专题，点击
                <a @click="join">加入</a>~
              </div>
            </template>
            <template v-else>
              <myTaskCard
                v-for="task in tasks"
                :key="task.id"
                v-bind:task="task"
                v-on:refresh="deleteTask"
              ></myTaskCard>
            </template>
          </div>
        </div>
        <p class="notice">参与提示：超时仍未提交的词条，将被系统回收。</p>
        <div id="myProgressPager" pager-type="tpager" class="wgt_horPager" style="display: none;"></div>
      </div>
    </div>
  </div>
</template>

<script>
import myAssignmentCard from "./card/myAssignmentCard";

import myTaskCard from "./card/myTaskCard";

export default {
  name: "subjectDrawAssignment",
  props: ["subjectId"],
  components: {
    myAssignmentCard,
    myTaskCard
  },
  data() {
    return {
      status: 0,
      assignments: [],
      tasks: []
    };
  },
  mounted() {
    this.getTasks();
    this.getAssginments();
  },
  methods: {
    getTasks() {
      this.$axios
        .post("http://localhost:8081/api/user/getTask", {
          subjectId: new Number(this.subjectId),
          type: 3
        })
        .then(res => {
          if (res.data.data) {
            this.status = 1;
            for (var i = 0; i < res.data.data.tasks.length; i++) {
              this.tasks.push(res.data.data.tasks[i]);
            }
          }
          //this.$message({
            //message: res.data.msg
          //});
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
    getAssginments() {
      this.$axios
        .post("http://localhost:8081/api/user/getAssignment", {
          subjectId: new Number(this.subjectId)
        })
        .then(res => {
          //window.console.log(res.data.data)
          if (res.data.data) {
            for (var i = 0; i < res.data.data.assignments.length; i++) {
              this.assignments.push(res.data.data.assignments[i]);
            }
          }
          //this.$message({
            //message: res.data.msg
          //});
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
    deleteTask(id) {
      window.console.log(id);
      for (var i = 0; i < this.tasks.length; i++) {
        if (this.tasks[i].id == id) {
          this.tasks.splice(i, 1);
          return;
        }
      }
    },
    join() {
      this.$axios
        .post("http://localhost:8081/api/user/joinSubject", {
          subjectId: new Number(this.subjectId)
        })
        .then(res => {
          //window.console.log(res.data.data)
          if (!res.data.errcode) {
            this.status = 1;
          }
          this.$message({
            message: res.data.msg
          });
        })
        .catch(error => {
          if (error.response) {
            this.$message({
              message: error.response.data.msg,
              type: "warning"
            });
          }
        });
    }
  }
};
</script>

<style>
/*! CSS Used from: https://bkssl.bdimg.com/static/wiki-common/pkg/wiki-common-base_66a9374.css */
input {
  font-family: "Helvetica Neue", Helvetica, Arial, "PingFang SC",
    "Hiragino Sans GB", "Microsoft YaHei", "WenQuanYi Micro Hei", sans-serif;
}
input {
  font-size: 100%;
}
h3,
p,
ul,
li,
input {
  margin: 0;
  padding: 0;
}
a {
  color: #338de6;
  text-decoration: none;
}
a:focus {
  outline: thin dotted;
}
a:active,
a:hover {
  outline: 0;
}
a:hover {
  text-decoration: underline;
}
h3 {
  font-size: 1.17em;
}
ul,
li {
  list-style: none;
}
input {
  vertical-align: middle;
}
input {
  line-height: normal;
}
input::-moz-focus-inner {
  border: 0;
}
.cmn-clearfix {
  *zoom: 1;
}
.cmn-clearfix:after {
  content: "\0020";
  display: block;
  height: 0;
  font-size: 0;
  clear: both;
  overflow: hidden;
  visibility: hidden;
}
.cmn-ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  word-wrap: normal;
}
h3 {
  color: #333;
}
/*! CSS Used from: https://bkssl.bdimg.com/static/wiki-task/taskBase/taskBase_9b28203.css */
a {
  color: #459df5;
}
a:focus {
  outline: 0;
}
input {
  border: 1px solid #e2e7ea;
}
input:focus {
  outline: 0;
}
.dib {
  display: -moz-inline-box;
  -moz-box-orient: vertical;
  display: inline-block;
  *zoom: 1;
  *display: inline;
}
.f14 {
  font-size: 14px;
}
.mt25 {
  margin-top: 25px;
}
.mr10 {
  margin-right: 10px;
}
.pv30 {
  padding: 30px 0;
}
.theaderWrap {
  position: relative;
}
.theader {
  margin-bottom: 5px;
  font-size: 24px;
  font-weight: 400;
}
/*! CSS Used from: https://bkssl.bdimg.com/static/wiki-task/widget/taskPager/taskPager_ab8217b.css */
[pager-type="tpager"] {
  text-align: center;
  font-size: 0;
}
/*! CSS Used keyframes */
@-webkit-keyframes LOADING {
  0% {
    -webkit-transform: scale(0.5);
    transform: scale(0.5);
    background: #2b8ccd;
  }
  50% {
    -webkit-transform: scale(1);
    transform: scale(1);
    background: #fff;
  }
  100% {
    -webkit-transform: scale(0.5);
    transform: scale(0.5);
    background: #2b8ccd;
  }
}
@keyframes LOADING {
  0% {
    -webkit-transform: scale(0.5);
    transform: scale(0.5);
    background: #2b8ccd;
  }
  50% {
    -webkit-transform: scale(1);
    transform: scale(1);
    background: #fff;
  }
  100% {
    -webkit-transform: scale(0.5);
    transform: scale(0.5);
    background: #2b8ccd;
  }
}

/*! CSS Used from: https://bkssl.bdimg.com/static/wiki-task/taskInfo-c2c/taskInfo-c2c_bb8c671.css */
a:hover {
  text-decoration: none;
}
.progress {
  margin-top: 35px;
  padding: 40px 0;
  border-top: 1px solid #e2e7ea;
}
.progress .myProgress {
  position: relative;
  width: 587px;
  height: 600px;
  float: left;
}
.progress .myProgress .notice {
  position: absolute;
  bottom: 35px;
  font-size: 14px;
}
.progress .myProgress #myProgressPager {
  position: absolute;
  bottom: 0;
  width: 100%;
  text-align: center;
}
.progress .myLemmaArea {
  position: relative;
  width: 595px;
  margin-top: 30px;
}
/*! CSS Used from: https://bkssl.bdimg.com/static/wiki-task/widget/taskPager/taskPager_ab8217b.css */
[pager-type="tpager"] {
  text-align: center;
  font-size: 0;
}
/*! CSS Used keyframes */
@-webkit-keyframes LOADING {
  0% {
    -webkit-transform: scale(0.5);
    transform: scale(0.5);
    background: #2b8ccd;
  }
  50% {
    -webkit-transform: scale(1);
    transform: scale(1);
    background: #fff;
  }
  100% {
    -webkit-transform: scale(0.5);
    transform: scale(0.5);
    background: #2b8ccd;
  }
}
@keyframes LOADING {
  0% {
    -webkit-transform: scale(0.5);
    transform: scale(0.5);
    background: #2b8ccd;
  }
  50% {
    -webkit-transform: scale(1);
    transform: scale(1);
    background: #fff;
  }
  100% {
    -webkit-transform: scale(0.5);
    transform: scale(0.5);
    background: #2b8ccd;
  }
}
</style>
