<template>
  <div class="content">
    <template v-for="entry in entrys">
        <a class="box-card" @click="see(entry.id)" :title="entry.name"
        :key="entry.id" :body-style="{ padding: '0px' }">
            <div class="rec-entry-header">
                <div class="rec-entry-title">{{entry.name}}</div>
                <div style="display: inline;float: right;margin-top: 5px">({{entry.field}})</div> 
            </div>
            <div class="rec-entry-body">
                <p>{{entry.reason1}}</p>
                <p>{{entry.reason2}}</p>
            </div>         
        </a>
    </template>
  </div>
</template>

<script>
export default {
  name: "myRecommendEntry",
  data() {
    return {
      status : this.$store.state.status,
      entrys: [
          {
            id: '1',
            name: '撩妹',
            field: '娱乐',
            reason1: '内容不全',
            reason2: '参考资料缺失'
          },
          {
            id: '3',
            name: '掉头发',
            field: '科学',
            reason1: '正文缺少图片',
            reason2: '目录缺失'
          },
          {
            id: '4',
            name: '加班',
            field: '科学',
            reason1: '概述缺失',
            reason2: '概述图不清晰'
          },
          {
            id: '7',
            name: '航天器',
            field: '科学',
            reason1: '存在相似内容',
            reason2: '正文缺少图片'
          },
          {
            id: '10',
            name: '打游戏',
            field: '娱乐',
            reason1: '存在相似内容',
            reason2: '正文缺少图片'
          },
          {
            id: '11',
            name: '打游戏',
            field: '娱乐',
            reason1: '存在相似内容',
            reason2: '正文缺少图片'
          },
          {
            id: '12',
            name: '打游戏',
            field: '娱乐',
            reason1: '存在相似内容',
            reason2: '正文缺少图片'
          },
          {
            id: '13',
            name: '打游戏',
            field: '娱乐',
            reason1: '存在相似内容',
            reason2: '正文缺少图片'
          },
          {
            id: '14',
            name: '打游戏',
            field: '娱乐',
            reason1: '存在相似内容',
            reason2: '正文缺少图片'
          },
          {
            id: '15',
            name: '打游戏',
            field: '娱乐',
            reason1: '存在相似内容',
            reason2: '正文缺少图片'
          },
          {
            id: '16',
            name: '打游戏',
            field: '娱乐',
            reason1: '存在相似内容',
            reason2: '正文缺少图片'
          },
          {
            id: '17',
            name: '打游戏',
            field: '娱乐',
            reason1: '存在相似内容',
            reason2: '正文缺少图片'
          },
          {
            id: '18',
            name: '打游戏',
            field: '娱乐',
            reason1: '存在相似内容',
            reason2: '正文缺少图片'
          }
      ],
      deadTime: '0',
      finishedSubject: '0',
    };
  },
  mounted() {
    
  },
  methods: {
    init(){
    // 初始化数据
        this.$axios
        .post("http://localhost:8081/api/user/getRecommendEntry")
        .then(res => {
            if (res.data.data)
              this.entrys = res.data.data.subjects
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
    see(id) {
      this.$router.push({ path: "/entryedit", query: { id: id } });
    }
  }
};
</script>

<style scoped>
.content {
  margin-left: 10px;
  width: 1200px;
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