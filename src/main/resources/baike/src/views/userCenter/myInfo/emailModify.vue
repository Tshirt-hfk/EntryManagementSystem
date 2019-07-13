<template>
    <div class="pass">
      <el-card class="box-card">
      <div class="input">
        <div class="notice">
        <p font-size=18px>请输入您要绑定的邮箱地址，绑定后即可用该邮箱</p>
        </div>
        <div class="phone">
        <el-input v-model="input" placeholder="请输入有效邮箱地址"></el-input>
        </div>
        <div class="vcode">
        <el-input v-model="vcode" placeholder="邮箱验证码"></el-input>
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
    name:"emailModify",
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