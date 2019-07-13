<template>
    <div class="pass">
      <el-card class="userboxCard">
      <div class="input">
        <div class="notice">
        <p font-size=18px>为了保障您的帐号安全，变更信息前需验证身份</p>
        </div>
        <div class="phone">
        <el-input v-model="input" placeholder="请输入密保电话"></el-input>
        </div>
        <div class="vcode">
        <el-input v-model="vcode" placeholder="请输入六位验证码"></el-input>
        </div>
        <div class="sendToPhone">
          <el-button>发送验证码</el-button>
        </div>
        <div class="confirm">
          <el-button type="primary">确认</el-button>
        </div>
      </div>
      </el-card>
    </div>
</template>

<script>
export default {
    name:"identityVerification",
    data() {
    return {
      input: '',
      vcode: ''
    }
  }
}
</script>

<style>
.notice{
  margin-bottom: 15px;
}
.input{
    padding: 20px 20px 0;
    color: #333;
    width: 365px;
    margin: 0 auto;
}
.phone{
  position: relative;
  z-index: 101;
  font-size: 14px;
  margin-bottom: 20px;
}
.vcode{
  position: relative;
  float: left;
  margin-bottom: 20px;
}
.sendToPhone{
  position: relative;
  margin-left: 10px;
  float: left;
}
.confirm{
  text-align: center;
  position: relative;
  clear: both;
}
</style>