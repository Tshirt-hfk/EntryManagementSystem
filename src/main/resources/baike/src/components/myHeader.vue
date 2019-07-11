<template>
    <header style="width: 100%">
        <div class="header">
            <div class="header-middle">
                <div class="header-l" >
                    <h1 @click="toIndex">词条百科</h1>
                </div>
                <div class="header-m">
                    <mySearch/>
                    <el-button @click="search">搜索</el-button>
                </div>
                <div class="header-circle">                             
                    <el-avatar :src="circleUrl">
                    </el-avatar>                 
                </div>
                <div class="header-r">
                    <template v-if="status==='0'">       <!-- 未登陆 -->
                        <el-button @click="toLogin">登录</el-button>
                        <el-button @click="toRegister">注册</el-button>
                    </template> 
                    <template v-else-if="status==='1'">  <!-- 普通用户 -->
                        <el-button @click="loginOut">登出</el-button>
                    </template> 
                    <template v-else-if="status==='2'">  <!-- 专题制作人 -->
                        <el-button @click="loginOut">登出</el-button>
                        <el-button @click="toSubject">创建专题</el-button>
                    </template>
                    <template v-else-if="status==='3'">  <!-- 管理员 -->
                        <el-button @click="loginOut">登出</el-button>
                    </template>
                </div>
            </div>
        </div>
    </header>
</template>


<script>

import mySearch from './mySearch'

export default {
    name:'myHeader',
    components:{
		mySearch
	},
    data() {
        return{
            circleUrl: "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
            squareUrl: "https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png",
            sizeList: ["large", "medium", "small"]
        }
    },
    mounted(){
        this.identifyAuth()
    },
    computed:{
        status() {
            return this.$store.state.status
        }
    },
    methods:{
        identifyAuth: function(){
            this.$axios.get(
                "http://localhost:8081/api/user/islogin"
            ).then(res => {
                if(res.data.data){
                    window.console.log(res.data.data.status)
                    this.$store.commit("status", res.data.data.status);
                    window.console.log(this.$store.state.status)
                } else {
                this.$message({
                    message:res.data.msg,
                    type:"warning"
                });
                }
            }).catch(error => {
                if(error.response){
                    this.$message({
                        message:error.response.data.msg,
                        type:"warning"
                    });
                }
            });
        },
        toIndex(){
            this.$router.push('/')
        },
        toLogin(){
            this.$router.push('/login')
        },
        toRegister(){
            this.$router.push('/register')
        },
        loginOut(){
            this.$store.commit('status', '0')
            localStorage.clear()   
            this.$message({
                message:"success!"
            })
            this.$router.push('/') 
        },
        toSubject(){
            this.$router.push('/subjectmaker')
        },
        search(){
            //TODO
        }
    }
}
</script>

<style scoped>
    .header{
        width: 100%;
        height: 50px;
        background-color: #409eff;
    }
    .header-middle{
        margin: 0 auto;
        width: 1170px;
        display: flex;
        justify-content: space-between;
    }
    .header-l{
        width: 200px;
        height: 50px;
    }
    .header-l h1{
        color: #fff;
        font-weight: 400;
        font-size: 30px;
        margin-top: 5px;
    }
    .header-m{
        width: 300px;
        height: 50px;
        margin-top: 5px;
    }
    .header-circle {      
        margin-top: 5px;
    }
    .header-r{
        width: 400px;
        height: 50px;
        margin-top: 5px;
    }
</style>

