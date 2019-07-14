<template>
  <div class="lemma cmn-ellipsis">
    <span class="lemmaName" :id="assignment.id">{{assignment.name}}</span>
    <span class="overlay"></span>
    <a v-if="notGet" @click="open" class="get">领取任务</a>
    <a v-else class="get">已领取</a>
  </div>
</template>

<script>
export default {
  name: "myAssignmentCard",
  props: ["assignment"],
  data() {
    return {
      content: "内容",
      notGet: true
    };
  },
  methods: {
    open() {
      this.$alert("确认领取词条："+this.assignment.name, this.assignment.name, {
        confirmButtonText: "确定",
        callback: action => {
          // TODO: 后端领取任务
          this.$axios
            .post("http://localhost:8081/api/user/drawAssignment", {
              assignmentId: this.assignment.id
            })
            .then(res => {
              if (!res.data.errcode) {
                this.notGet = false;
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
        }
      });
    },
    refresh(){
      this.$emit("refresh");
    }
  }
};
</script>



<style>
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