<template>
  <div class="layout">
    <div class="management">
      <el-tabs type="border-card">
        <el-tab-pane>
          <span slot="label">未发布词条</span>
          <template v-for="assignment in assignmentW">
            <div class="lemma cmn-ellipsis" :key="assignment.id">
              <span class="lemmaName" :id="assignment.id">{{assignment.name}}</span>
              <span class="overlay"></span>
              <a @click="draw(assignment)" class="get">领取</a>
            </div>
          </template>
        </el-tab-pane>
        <el-tab-pane>
          <span slot="label">已发布词条</span>
          <template v-for="assignment in assignmentW">
            <div class="lemma cmn-ellipsis" :key="assignment.id">
              <span class="lemmaName" :id="assignment.id">{{assignment.name}}</span>
              <span class="overlay"></span>
              <a @click="cancel(assignment)" class="get">取消</a>
            </div>
          </template>
        </el-tab-pane>
        <el-tab-pane>
          <span slot="label">待审核词条</span>
          <template v-for="assignment in assignmentW">
            <div class="lemma cmn-ellipsis" :key="assignment.id">
              <span class="lemmaName" :id="assignment.id">{{assignment.name}}</span>
              <span class="overlay"></span>
              <a @click="audit(assignment)" class="get">审核</a>
            </div>
          </template>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import myAssignmentCard from "../components/myAssignmentCard";

export default {
  name: "subjectManagement",
  components: {
    myAssignmentCard
  },
  data() {
    return {
      subjectId: this.$route.query.id,
      assignmentW: [
        {
          id: 1,
          name: "test1"
        }
      ],
      assignmentD: [
        {
          id: 2,
          name: "test2"
        }
      ],
      assignmentA: [
        {
          id: 3,
          name: "test3"
        }
      ]
    };
  },
  mounted() {
    this.$axios
      .get("http://localhost:8081/api/subjectmaker/subject", {
        parms: { id: this.subjectId }
      })
      .then(res => {
        if (res.data.data) {
          this.assginmentW = res.data.data.assginmentW;
          this.assginmentD = res.data.data.assginmentD;
          this.assginmentA = res.data.data.assginmentA;
        } else {
          this.$message({
            message: res.data.msg
          });
        }
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
  methods: {
    draw(assignment) {
      this.$confirm("此操作将发布该词条?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          this.assignmentD.unshift(assignment);
          this.assignmentW.splice(this.assignmentW.lastIndexOf(assignment), 1);
          //TODO
          this.$message({
            type: "success",
            message: "发表成功!"
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消"
          });
        });
    },
    cancel(assignment) {
      this.$confirm("此操作将取消发布该词条?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          this.assignmentW.unshift(assignment);
          this.assignmentD.splice(this.assignmentD.lastIndexOf(assignment), 1);
          //TODO
          this.$message({
            type: "success",
            message: "发表成功!"
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消"
          });
        });
    },
    audit(assignment) {}
  }
};
</script>

<style>
.layout {
  width: 980px;
  margin: 0 auto;
}
.management {
  margin-top: 20px;
  width: 100%;
  height: 500px;
}

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

