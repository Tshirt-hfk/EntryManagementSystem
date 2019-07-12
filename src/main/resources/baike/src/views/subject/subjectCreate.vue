<template>
  <div style="margin:70px auto 0; width:40%;">
    <h2 class="title">专题信息</h2>
    <el-form ref="form" :model="form" label-width="80px">
      <el-form-item label="专题名称">
        <el-input v-model="form.name"></el-input>
      </el-form-item>
      <el-form-item label="专题领域">
        <el-select v-model="form.field" filterable placeholder="请选择专题领域">
          <el-option v-for="field in fields" :key="field" :label="field" :value="field"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="专题介绍">
        <el-input
          type="textarea"
          rows="4"
          v-model="form.introduction"
          maxlength="100"
          show-word-limit
        ></el-input>
      </el-form-item>
      <el-form-item label="专题目标">
        <el-input type="textarea" rows="4" v-model="form.goal" maxlength="100" show-word-limit></el-input>
      </el-form-item>
      <el-form-item label="相关文件">
        <input type="file" accept="text/plain" @change="getFile" />
      </el-form-item>
      <el-form-item label="公开">
        <el-switch v-model="form.isPublic"></el-switch>
      </el-form-item>
      <el-form-item>
        <el-row>
          <el-col :span="12">
            <div style="width:30%; margin: 0 auto">
              <el-button style="width:100%;" type="primary" @click="cancel">取消</el-button>
            </div>
          </el-col>
          <el-col :span="12">
            <div style="width:30%; margin: 0 auto">
              <el-button style="width:100%;" type="primary" @click="submit">提交</el-button>
            </div>
          </el-col>
        </el-row>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import myAssignmentCard from "../../components/myAssignmentCard";

export default {
  name: "subjectCreate",
  components: {
    myAssignmentCard
  },
  data() {
    return {
      active: 1,
      form: {
        name: "",
        field: "",
        isPublic: false,
        introduction: "",
        goal: "",
        filedata: ""
      },
      fields: ["科学"]
    };
  },
  methods: {
    getFile: function(event) {
      // 获取input里的文件
      var file=event.target.files[0];
      var reader = new FileReader();
      reader.readAsText(file, "UTF-8")
      reader.onload = (event) => {
        this.form.filedata = event.target.result;
      }
    },
    submit() {
      this.$axios
        .post("http://localhost:8081/api/subjectMaker/createSubject", this.form)
        .then(res => {
          if (res.data) {
            this.$message({
              message: res.data.msg
            });
            this.$router.push("/subjectmakercenter/mysubject");
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
    cancel() {
      if (this.active == 2) {
        this.active = this.active - 1;
      } else {
      }
    }
  }
};
</script>

<style>
.file {
  outline: none;
  background-color: transparent;
  filter: alpha(opacity=0);
  opacity: 0;
}
.title {
  margin: 20px auto;
}
.el-row {
  margin-bottom: 10px;
}
.el-col {
  border-radius: 4px;
  margin-bottom: 10px;
}
.button {
  margin: 20px auto;
}
</style>


