<template>
  <div style="margin:100px auto; width:740px;">
    <div style="width:100%;text-algin:center">
      <h2 class="title">专题信息</h2>
    </div>
    <el-form ref="form" :model="form" label-width="80px">
      <div>
        <el-form-item label="专题图片" style="float:left">
          <el-upload
            class="avatar-uploader"
            action="http://localhost:8081/resource/image"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <img v-if="form.imageUrl" :src="form.imageUrl" class="avatar" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <div style="float:left;margin-left:48px">
          <el-form-item label="专题名称">
            <el-input v-model="form.name" style="width:300px"></el-input>
          </el-form-item>
          <el-form-item label="专题领域">
            <el-select v-model="form.field" filterable placeholder="请选择专题领域" style="width:300px">
              <el-option v-for="field in fields" :key="field" :label="field" :value="field"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="截止日器">
            <el-date-picker
              v-model="deadLine"
              type="datetime"
              placeholder="选择日期时间"
              style="width:300px"
            ></el-date-picker>
          </el-form-item>
          <el-form-item label="公开">
            <el-switch v-model="form.isPublic"></el-switch>
          </el-form-item>
        </div>
        <div class="clear"></div>
      </div>
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
export default {
  name: "subjectCreate",
  computed: {
    deadLine:{
      get:function() {
        if(this.form.deadLine == 0)
          return null;
        return new Date(this.form.deadLine);
      },
      set:function(newValue) {
        this.form.deadLine = newValue.getTime();
      }
    }
  },
  data() {
    return {
      form: {
        imageUrl: "",
        name: "",
        field: "",
        deadLine: 0,
        isPublic: false,
        introduction: "",
        goal: "",
        documents: []
      },
      fields: ["科学"]
    };
  },
  methods: {
    getFile: function(event) {
      // 获取input里的文件
      var files = event.target.files;
      var reader = new FileReader();
      for (var file in files) {
        reader.readAsText(file, "UTF-8");
        reader.onload = event => {
          this.form.documents.push(event.target.result);
        };
      }
    },
    submit() {
      window.console.log(this.form)
      return;
      this.$axios
        .post("http://localhost:8081/api/subjectMaker/createSubject", this.form)
        .then(res => {
          if (res.data) {
            this.$message({
              message: res.data.msg
            });
            this.$router.push("/usercenter/allmysubject");
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
      this.$router.push("/");
    },
    handleAvatarSuccess(res, file) {
      this.form.imageUrl = res.data.url;
    },
    beforeAvatarUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
      }
      return isLt2M;
    }
  }
};
</script>

<style scope>
.file {
  outline: none;
  background-color: transparent;
  filter: alpha(opacity=0);
  opacity: 0;
}
.title {
  margin: 30px auto;
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

.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}

.avatar-uploader .avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 220px;
  height: 220px;
  line-height: 220px;
  text-align: center;
}
.avatar-uploader .avatar {
  width: 220px;
  height: 220px;
  display: block;
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


