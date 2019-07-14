<template>
  <div>
    <subjectBasicInfo :subjectId="subjectId"></subjectBasicInfo>
    <subjectDrawAssignment :subjectId="subjectId"></subjectDrawAssignment>
  </div>
</template>

<script>
import subjectBasicInfo from "./subjectIndex/subjectBasicInfo";

import subjectDrawAssignment from "./subjectIndex/subjectDrawAssignment";

export default {
  name: "subujectIndex",
  components: {
    subjectBasicInfo,
    subjectDrawAssignment
  },
  data() {
    return {
      subjectId: this.$route.query.id,
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
            for (var i = 0; i < res.data.data.tasks.length; i++) {
              this.tasks.push(res.data.data.tasks[i]);
            }
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
