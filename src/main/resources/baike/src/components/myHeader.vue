<template>
  <header class="header">
    <div class="header-middle">
      <div class="header-l">
        <h1 @click="toIndex" style="cursor: pointer">众智化词条系统</h1>
      </div>
      <div class="header-m">
          <a class="header-link" href="/"> 首页</a>
          <a class="header-link" href="/"> 方磊</a>
          <a class="header-link" href="/"> 方磊</a>
          <a class="header-link" href="/"> 方磊</a>
          <a class="header-link" href="/"> 方磊</a>
      </div>
      <div class="header-r">
        <template v-if="status==='0'">
          <!-- 未登陆 -->
          <el-link @click="toLogin" style="float:left; margin-top:7px">登录</el-link>
          <div class="header-login-line"> </div>
          <el-link @click="toRegister" style="float:left;margin-left:7px;margin-top:7px">注册</el-link>
        </template>
        <template v-else-if="status==='1'">
          <!-- 普通用户 -->
          <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link">
                <a href="http:localhost:8080/#/usercenter">
                    <div class="header-icon">
                        <img src="https://static.hdslb.com/images/member/noface.gif" class="header-face">
                    </div>
                </a>
                {{this.userName}}<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="1">我的专题</el-dropdown-item>
                <el-dropdown-item command="2">我的任务</el-dropdown-item>
                <el-dropdown-item command="3">我的收藏</el-dropdown-item>
                <el-dropdown-item command="4" divided>登出</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <el-link @click="loginOut" style="margin-left:10px; margin-bottom:3px; margin-right:12px">消息</el-link>
          <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link">
                设置<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="5">账号设置</el-dropdown-item>
                <el-dropdown-item command="6">资料设置</el-dropdown-item>
                <el-dropdown-item command="7">消息设置</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <el-link @click="toSubject" class="header-createsubject">创建专题</el-link>
        </template>
        <template v-else-if="status==='2'">
          <!-- 专题制作人 -->
          <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link">
                <a href="http:localhost:8080/#/usercenter">
                    <div class="header-icon">
                        <img src="https://static.hdslb.com/images/member/noface.gif" class="header-face">
                    </div>
                </a>
                {{this.userName}}<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="1">我的专题</el-dropdown-item>
                <el-dropdown-item command="2">我的任务</el-dropdown-item>
                <el-dropdown-item command="3">我的收藏</el-dropdown-item>
                <el-dropdown-item command="4" divided>登出</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <el-link @click="loginOut" style="margin-left:10px; margin-bottom:3px; margin-right:12px">消息</el-link>
          <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link">
                设置<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="5">账号设置</el-dropdown-item>
                <el-dropdown-item command="6">资料设置</el-dropdown-item>
                <el-dropdown-item command="7">消息设置</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <el-link @click="toSubject" class="header-createsubject">创建专题</el-link>
        </template>
        <template v-else-if="status==='3'">
          <!-- 管理员 -->
          <el-button @click="loginOut">登出</el-button>
        </template>
      </div>
    </div>
    <div class="header-line"></div>
  </header>
</template>


<script>
import mySearch from "./mySearch";

export default {
  name: "myHeader",
  components: {
    mySearch
  },
  data() {
    return {
      userName: this.$store.state.name,
      circleUrl:
        "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
      squareUrl:
        "https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png",
      sizeList: ["large", "medium", "small"]
    };
  },
  mounted() {
    this.identifyAuth();
  },
  computed: {
    status() {
      return this.$store.state.status;
    },
    name() {
      return this.$store.state.name;
    }
  },
  methods: {
    identifyAuth: function() {
      this.$axios
        .get("http://localhost:8081/api/user/islogin")
        .then(res => {
          if (res.data.data) {
            window.console.log(res.data.data.status);
            this.$store.commit("status", res.data.data.status);
            this.$store.commit("name", res.data.data.name);
            window.console.log(this.$store.state.status);
          } else {
            this.$message({
              message: res.data.msg,
              type: "warning"
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
    toUserCenter() {
      this.$router.push("/usercenter");
    },
    toIndex() {
      this.$router.push("/");
    },
    toLogin() {
      this.$router.push("/login");
    },
    toRegister() {
      this.$router.push("/register");
    },
    loginOut() {
      this.$store.commit("status", "0");
      localStorage.clear();
      this.$message({
        message: "success!"
      });
      this.$router.push("/");
    },
    toSubject() {
      this.$router.push("/subjectcreate");
    },
    search() {
      //TODO
    },
    handleCommand(command){
        if(command == '1')
            this.$router.push("/usercenter/allmysubject");
        else if(command == '2')
            this.$router.push("/usercenter/allmyentry");
        else if(command == '3')
            this.$router.push("/usercenter/allmyentry");
        else if(command == '4')
            this.loginOut();
    }
  }
};
</script>

<style scoped>
.header {
  position: relative;
  top: 0px;
  left: 0px;
  right: 0px;
  height: 35px;
}
.header-middle {
  margin: 0 auto;
  width: 100%;
  display: flex;
  justify-content: space-between;
  height: 35px;
}
.header-l {
  width: 200px;
  height: 35px;
  margin-left: 110px;
}
.header-l h1 {
  color: #409eff;
  font-weight: 300;
  font-size: 21px;
  margin-top: 4px;
}
.header-m{
  width: 900px;
  height: 35px;
  margin-right:300px;
  margin-top: 6px;
}
.header-link{
  color: #707070;
  font-size: 14px;
  margin-left: 10px;
}
.header-r {
  width: 450px;
  height: 35px;
  font-size: 18px;
}
.header-line{
  width: 2000px;
  border-bottom: 1px solid #ddd;
}
.header-login-line{
  float:left;
  border-right: 1px solid #707070;
  height: 20px;
  margin-left: 7px;
  margin-top: 7px;
}
.header-icon{
  float: left;
  width: 30px;
  height: 30px;
  left: 0;
  top: 0;
  margin-top: -5px;
  margin-right: 7px;
}
.header-face{
  border: 1px solid #8bbb0f;
  width: 100%;
  height: 100%;
  border-radius: 50%;
}
.el-dropdown-link{
  cursor: pointer;
}
.el-icon-arrow-down{
  font-size: 12px;
}
.header-createsubject{
    width: 80px;
    height: 35px;
    background-color: #409EFF;
    line-height: 42px;
    text-align: center;
    font-size: 14px;
    color: #fff;
    border-radius: 0 0 6px 6px;
    margin-left: 10px;
}
</style>

