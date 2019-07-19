<template>
  <div class="login-content">
    <div class="top-banner">
      <img src="https://s1.hdslb.com/bfs/static/passport/static/img/rl_top.35edfde.png">
    </div>
    <div class="login-title-line">
      <span class="tit" style="font-size: 38px;">登录</span>
    </div>
    <div style="margin:0 auto; width:600px; margin-top:70px">
      <el-card style="width: 100%;">
        <el-form>
          <el-form-item label="账号">
            <el-input placeholder="请输入账号" v-model="user_data.username"></el-input>
          </el-form-item>
          <el-form-item label="密码">
            <el-input
              @keydown.native.enter="login"
              placeholder="请输入密码"
              type="password"
              v-model="user_data.password"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <input type="checkbox">记住我
            <span style="color: #bbb; margin-left: 10px;">
              不是自己的电脑不要勾选此项
            </span>
            <a href="" class="forgot-password">忘记密码?</a>
          </el-form-item>
          <el-form-item>
            <el-row>
              <el-col :span="12">
                <div style="width:40%; margin: 0 auto">
                  <el-button style="width:100%;" type="primary" @click="login">登录</el-button>
                </div>
              </el-col>
              <el-col :span="12">
                <div style="width:40%; margin: 0 auto">
                  <el-button style="width:100%;" @click="toRegister">注册</el-button>
                </div>
              </el-col>
            </el-row>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script>
export default {
  name: "login",
  data() {
    return {
      user_data: {
        username: "",
        password: ""
      }
      //console:console
    };
  },
  methods: {
    login() {
      // this.console.log("执行登录操作");
      if (!this.user_data.username.trim() || !this.user_data.password.trim())
        return this.$message({
          message: "请输入用户名或密码!",
          type: "warning"
        });
      return this.$axios
        .post("http://localhost:8081/api/tourist/login", this.user_data)
        .then(res => {
          if (res.data.data) {
            // 将Token存储到localStorage
            localStorage.setItem("token", res.data.data.token);
            this.$store.commit("status", res.data.data.status);
            this.$store.commit("name", res.data.data.name);
            this.$message({
              message: res.data.msg
            });
            // 登录成功跳转到首页

            this.$router.push("/");
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
    toRegister() {
      this.$router.push("/register");
    }
  }
};
</script>

<style scoped>
.login-content {
  position: absolute;
  left: 0px;
  right: 0px;
  top: 50px;
  bottom: 0px;
}
.top-banner{
  background: #00a0d8;
  height: 86px;
  text-align: center;
  margin-bottom: 20px;
  margin-top: 5px;
}
.login-title-line{
  width: 980px;
  height: 28px;
  margin: 0 auto;
  border-bottom: 1px solid #ddd;
  margin-bottom: 28px;
  text-align: center;
}
.login-title-line .tit{
  line-height: 56px;
  margin: 0 auto;
  padding: 0 20px;
  background: #fff;
}
.forgot-password{
  float: right;
  color: #00a1d6;
}
</style>
