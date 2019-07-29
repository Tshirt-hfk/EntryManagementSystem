<template>
  <div>
    <h1 style="color:#333; text-align:center; margin-top:30px">欢迎来到云论坛</h1>
    <div style="margin:70px auto 0; width:40%;">
      <el-card>
        <el-form :model="user_data" status-icon :rules="rules" ref="user_data">
            <el-form-item label="昵称" prop="username">
                <el-input placeholder="请输入昵称" v-model="user_data.username"></el-input>
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
                <el-input type="email" placeholder="请输入邮箱" v-model="user_data.email"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="pass">
                <el-input type="password" placeholder="请输入密码" v-model="user_data.pass" auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="checkPass">
                <el-input type="password" placeholder="请确认密码" v-model="user_data.checkPass" auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item>
                <div style="width:20%; margin: 0 auto">
                    <el-button style="width:100%;" type="primary" @click="register('user_data')">注册</el-button>
                </div>
            </el-form-item>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script>
export default {
    name:'register',
    data() {
        var validateUsername = (rule, value, callback) => {
          if (value === '') {
            callback(new Error('请输入昵称'));
          } 
          callback();
        };
        var validateEmail = (rule, value, callback) => {
          if (value === '') {
            callback(new Error('请输入邮箱'));
          }
          else {
            var reg = /^([a-zA-Z]|[0-9])(\w|-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/
            if (!reg.test(value)) {
              callback(new Error('邮箱格式不正确！'));
            }
            callback();
          }
        };
        var validatePass = (rule, value, callback) => {
          if (value === '') {
            callback(new Error('请输入密码'));
          } else if (value.length < 8 || value.length > 15) {
            callback(new Error('密码长度在8~15'));
          } else {
            if (this.user_data.checkPass !== '') {
              this.$refs.user_data.validateField('checkPass');
            }
            callback();
          }
        };
        var validatePass2 = (rule, value, callback) => {
          if (value === '') {
            callback(new Error('请再次输入密码'));
          } else if (value !== this.user_data.pass) {
            callback(new Error('两次输入密码不一致'));
          } else {
            callback();
          }
        };
        return {
          user_data: {
            username: "",
            email:"",
            pass: "",
            checkPass:""
          },
          rules: {
            username: [
              { validator: validateUsername, trigger: 'blur'}
            ],
            email: [
              { validator: validateEmail, trigger: 'blur'}
            ],
            pass: [
              { validator: validatePass, trigger: 'blur'}
            ],
            checkPass: [
              { validator: validatePass2, trigger: 'blur' }
            ],
          }
        };
    },
    methods: {
      register(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.$axios.post("/api/tourist/register", {
              username: this.user_data.username,
              password: this.user_data.pass,
              email: this.user_data.email
            }).then(result => {
                    this.$message({
                      message:result.data.msg
                    });
                    // 注册成功跳转到登录页面
                    this.$router.push("/login");
                }).catch(error => {
                  if(error.response){
                    this.$message({
                      message:error.response.data.msg,
                      type:"warning"
                    });
                  }
                })

          } else {
             this.$message({
                    message:"请正确填写表单！",
                    type:"warning"
                  });
            return false;
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      }
    }
};
</script>