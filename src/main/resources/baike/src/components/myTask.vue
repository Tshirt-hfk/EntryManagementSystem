<template>
  <div class="myLemma" id="29139302" data>
    <div class="operation">
      <a
        href="/edit/%E4%B8%AD%E8%81%8C%E5%BF%83%E7%90%86%E5%81%A5%E5%BA%B7%E9%98%B3%E5%85%89%E6%95%99%E8%82%B2%E5%AD%A6%E7%94%9F%E7%94%A8%E4%B9%A6/12138104"
        class="edit"
        target="_blank"
      >编辑</a>
      <a href="javascript:;" class="giveUp">放弃</a>
    </div>
    <div class="forMiddle">
      <div class="lemmaName cmn-ellipsis">{{task.name}}</div>
      <div class="received">{{task.state}}</div>
      <div class="restTime" serial="0" lefttime="15635">
        剩余编辑时间
        <span class="time">{{task.time}}</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "myTask",
  props:['task'],

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
.progress .myLemma {
  position: relative;
  display: table;
  width: 188px;
  height: 90px;
  z-index: 2;
  float: left;
  font-size: 16px;
  text-align: center;
  margin-right: 8px;
  margin-bottom: 8px;
  border: 1px solid #a2aaaf;
}
.progress .myLemma:hover .operation {
  opacity: 1;
}
.progress .myLemma:hover .operation + .forMiddle {
  opacity: 0.1;
}
.progress .myLemma .forMiddle {
  display: table-cell;
  vertical-align: middle;
  transition: 0.2s;
}
.progress .myLemma .lemmaName {
  display: block;
  width: 187px;
  color: #333;
}
.progress .myLemma .restTime {
  font-size: 12px;
}
.progress .myLemma .restTime .time {
  color: #ff9600;
  margin-left: 8px;
}
.progress .myLemma .received {
  color: #333;
  font-size: 14px;
}
.progress .operation {
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
.progress .operation .edit,
.progress .operation .giveUp {
  display: inline-block;
  color: #fff;
  padding: 7px 18px;
  border-radius: 4px;
}
.progress .operation .edit {
  background-color: #459df5;
}
.progress .operation .edit:hover {
  background-color: #1e89f3;
}
.progress .operation .giveUp {
  margin-left: 10px;
  background-color: #98a8b2;
}
.progress .operation .giveUp:hover {
  background-color: #8194a1;
}
</style>


