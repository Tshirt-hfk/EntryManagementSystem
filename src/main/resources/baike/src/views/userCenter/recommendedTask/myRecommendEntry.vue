<template>
  <div class="content">
    <div class="myrecentry-searchbar">
      <el-input style="width: 250px; float: right; margin-right: 30px;" 
      v-model="searchValue" placeholder="请输入关键词"></el-input>
    </div>
    <template v-for="entry in entrys">
        <a class="box-card" @click="see(entry.id)" :title="entry.name"
        :key="entry.id" :body-style="{ padding: '0px' }">
            <div class="rec-entry-header">
                <div class="rec-entry-title">{{entry.name}}</div>
                <div v-for="item in entry.field" :key="item" style="float: right;margin-top: 5px;margin-left: 5px">
                  ({{item}})</div> 
            </div>
            <div class="rec-entry-body">
                <p>{{entry.reason[0]}}</p>
                <p>{{entry.reason[1]}}</p>
            </div>         
        </a>
    </template>
  </div>
</template>

<script>

export default {
  name: "myRecommendEntry",
  watch:{
    searchValue:{
      handler(n, o){
        clearTimeout(this.timeout);
        this.timeout = setTimeout(() => {
          this.remoteMethod(n);
        }, 300);
      }
    }
  },
  data() {
    return {
      status : this.$store.state.status,
      searchValue: '',
      allentrys: [],
      entrys: [],
      deadTime: '0',
      finishedSubject: '0',
    };
  },
  mounted() {
    this.init()
  },
  methods: {
    init(){
    // 初始化数据
        this.$axios
        .post("/api/user/getRecommendEntry",{})
        .then(res => {
            if (res.data.data){
              this.entrys = res.data.data.assignments;
              this.allentrys = res.data.data.assignments;
            }
        })
        .catch(error => {
            if (error.response) {
            // this.$message({
            //     message: error.response.data.msg,
            //     type: "warning"
            // });
            }
        });
    },
    see(id) {
      this.$router.push({ path: "/entryedit", query: { id: id } });
    },
    remoteMethod(query){
      if(query !== ''){
        this.$axios
            .post("/api/user/searchEntry", {keyword:query})
            .then(res => {
              if(res.data.data)
                this.entrys = res.data.data.assignments;
              else
                this.entrys = this.allentrys.slice(0, 0);
            })
            .catch(error => {
            });
      }else
        this.entrys = this.allentrys
    }
  }
};
</script>

<style scoped>
.content {
  margin-left: 10px;
  width: 1200px;
}
.myrecentry-searchbar{
  width: 1125px;
  height: 70px;
}
.box-card {
  padding: 10px 20px;
  display: block;
  background: #fafafa;
  text-decoration: none;
  border: solid 1px #d5d5d5;
  box-shadow: 0 1px 0 #e7e7e7;
  font-size: 14px;
  transition: .15s linear;
  float: left;
  margin: 0 12px 15px 0;
  height: 107px;
  width: 236px;
}
.box-card:hover{
  border: solid 1px #52a3f5;
  background:rgba(82, 163, 245,0.1)
}
.rec-entry-header{
    width: 100%;
    text-align: center;
}
.rec-entry-title{
   display: inline;
   font-size:20px;
   color:#52a3f5;
   padding-left: 40px;
}
.rec-entry-body{
    height: 47px;
    color: #666;
    font-size: 16px;
    line-height: 20px;
    margin-top: 14px;
    margin-left: 10px;
}
.rec-entry-body p{
  line-height: 25px;
}
.rec-entry-body p::before{
    margin-right: 5px;
    content: "★";
    color: #52a3f5;
}
.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both;
} 
</style>