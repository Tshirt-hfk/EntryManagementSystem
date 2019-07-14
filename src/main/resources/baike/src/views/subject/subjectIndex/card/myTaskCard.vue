<template>
  <div class="myLemma">
    <div class="operation">
      <a class="edit" @click="edit">编辑</a>
      <a class="giveUp" @click="giveup">放弃</a>
    </div>
    <div class="forMiddle">
      <div class="lemmaName cmn-ellipsis">{{name}}</div>
      <div class="restTime">
        剩余时间：
        <span class="time">{{restTime}}</span>
      </div>
    </div>
  </div>
</template>

<script>
import { setInterval } from "timers";
export default {
  name: "myTaskCard",
  props: ["task"],
  data() {
    return {
      taskId: this.task.id,
      name: this.task.name,
      deadline: this.task.deadline,
      restTime: this.timer()
    };
  },
  mounted() {
    setInterval(() => {
      this.timer();
    }, 1000);
  },
  methods: {
    timer() {
      var timeStr = "";
      var rt = parseInt((this.deadline - Date.now()) / 1000);
      var day = parseInt(rt / (24 * 3600));
      if (day > 0) timeStr = timeStr + day + "天";
      var hour = parseInt((rt % (24 * 3600)) / 3600);
      if (hour > 0) timeStr = timeStr + hour + "时";
      var min = parseInt((rt % 3600) / 60);
      if (min > 0) timeStr = timeStr + min + "分";
      var sec = parseInt(rt % 60);
      if (sec > 0) timeStr = timeStr + sec + "秒";
      this.restTime = timeStr;
    },
    edit() {
      this.$router.push("/entryedit", { query: { entryId: this.taskId } });
    },
    giveup() {
      this.$axios
        .post("http://localhost:8081/api/user/giveUpAssignment", {
          assignmentId: this.taskId
        })
        .then(res => {
          if (!res.data.errcode) {
            this.refresh();
          }
          this.$message({
            message: res.data.msg,
            type: "warning"
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
    },
    refresh() {
      this.$emit("refresh", this.taskId);
    }
  }
};
</script>

<style>
/*! CSS Used from: https://bkssl.bdimg.com/static/wiki-common/pkg/wiki-common-base_66a9374.css */
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
.cmn-ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  word-wrap: normal;
}
/*! CSS Used from: https://bkssl.bdimg.com/static/wiki-task/taskBase/taskBase_9b28203.css */
a {
  color: #459df5;
}
a:focus {
  outline: 0;
}
/*! CSS Used from: https://bkssl.bdimg.com/static/wiki-task/taskInfo-c2c/taskInfo-c2c_bb8c671.css */
a:hover {
  text-decoration: none;
}
.myLemma {
  position: relative;
  display: table;
  width: 188px;
  height: 68px;
  z-index: 2;
  float: left;
  font-size: 16px;
  text-align: center;
  margin-right: 8px;
  margin-bottom: 8px;
  border: 1px solid #a2aaaf;
}
.myLemma:hover .operation {
  opacity: 1;
}
.myLemma:hover .operation + .forMiddle {
  opacity: 0.1;
}
.myLemma .forMiddle {
  display: table-cell;
  vertical-align: middle;
  transition: 0.2s;
}
.myLemma .lemmaName {
  display: block;
  width: 187px;
  color: #333;
  font-size: 18px;
}
.myLemma .restTime {
  font-size: 12px;
  margin-top: 5px;
}
.myLemma .restTime .time {
  color: #ff9600;
  margin-left: 8px;
}
.operation {
  opacity: 0;
  position: absolute;
  z-index: 3;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  margin: 16px auto;
  height: 38px;
  transition: 0.2s;
}
.operation .edit,
.operation .giveUp {
  display: inline-block;
  color: #fff;
  padding: 7px 18px;
  border-radius: 4px;
  cursor: pointer;
}
.operation .edit {
  background-color: #459df5;
}
.operation .edit:hover {
  background-color: #1e89f3;
}
.operation .giveUp {
  margin-left: 10px;
  background-color: #98a8b2;
}
.operation .giveUp:hover {
  background-color: #8194a1;
}
</style>


