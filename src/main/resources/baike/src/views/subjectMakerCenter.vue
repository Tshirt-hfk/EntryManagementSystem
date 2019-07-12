<template>
  <div class="layout">
    <el-container height="1000px">
      <el-aside width="210px">
        <el-menu :router="true" default-active="/subjectmakercenter/mysubject" class="el-menu-vertical-demo">
          <el-menu-item index="/subjectmakercenter/mysubject">
            <span slot="title">我的专题</span>
          </el-menu-item>
          <el-menu-item index="/subjectmakercenter/myentry">
            <span slot="title">我的词条</span>
          </el-menu-item>
          <el-submenu index="1">
            <template slot="title">
              <span slot="title">个人信息</span>
            </template>
            <el-menu-item index="/subjectmakercenter/passwordmodify">修改密码</el-menu-item>
            <el-menu-item index="/subjectmakercenter/emailmodify">修改邮箱</el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>
      <el-main style="width:100%">
        <router-view></router-view>
      </el-main>
    </el-container>
  </div>
</template>


<script>
export default {
  name: "subjectMakerCenter",
  data() {
    return {};
  },
  methods: {}
};
</script>

<style>
.layout {
  width: 67%;
  margin: 0 auto;
  margin-top: 30px;
  min-height:800px;
}

.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}
</style>
