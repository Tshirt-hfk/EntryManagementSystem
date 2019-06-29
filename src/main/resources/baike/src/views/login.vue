<template>
  <div>
    <h1 style="color:#333; text-align:center; margin-top:30px">词条百科</h1>
    <div style="margin:70px auto 0; width:40%;">
      <el-card>
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
            <el-row>
              <el-col :span="12">
                <div style="width:40%; margin: 0 auto">
                  <el-button style="width:100%;" type="primary" @click="login">登陆</el-button>
                </div>
              </el-col>
              <el-col :span="12">
                <div style="width:40%; margin: 0 auto">
                  <el-button style="width:100%;" type="primary" @click="toRegister">注册</el-button>
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
    name:'login',
    data() {
        return {
          user_data: {
            username: "",
            password: ""
          },
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

          return (
            this.$axios.post("http://localhost:8081/api/tourist/login",this.user_data)
                    .then(result => {
                        localStorage.setItem("token",result.data.data.token);
                        this.$store.commit("status", result.data.data.status);
                        //this.$store.commit("status",result.data.data.status)
                        // 将Token存储到localStorage
                        this.$message({
                          message:result.data.msg
                        });
                        // 登录成功跳转到首页
                        this.$router.push("/");
                    })
                    .catch(error => {
                      if(error.response){
                        this.$message({
                          message:error.response.data.msg,
                          type:"warning"
                        });
                      }
                    })
          );
        },
        toRegister(){
            this.$router.push('/register')
        }
    }
};
</script>