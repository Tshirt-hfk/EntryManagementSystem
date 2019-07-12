<template>
  <div class = "header">
      <div class="user-info">
        <div class="block"><el-avatar :size="144" :src="circleUrl"></el-avatar></div>
        <div class="sub-title">
            <p class="username" font-size=20px>用户名</p> <i class="el-icon-setting"></i>                
        </div>
      </div>

      <div class="com-inline-block">
          <ul class="lemma-info">
              <li class=""> "编辑词条数"</li>

          </ul>
      </div>
  </div>
</template>

<script>
  export default {
    data () {
      return {
        circleUrl: "https://gss0.bdstatic.com/7Ls0a8Sm1A5BphGlnYG/sys/portrait/item/82480d81.jpg",
      }
    }
  }
</script>

<style scoped>
    .header{
        margin-top: 5px;
        width: 100%;
        height: 300px;
        background-color: #96d2f2;
    }
    .user-info{
        width: 150px;
        padding-top: 50px;
        vertical-align: middle;
    }
    .sub-title{
        margin-top: 10px;
        text-align: center;
    }
</style>

