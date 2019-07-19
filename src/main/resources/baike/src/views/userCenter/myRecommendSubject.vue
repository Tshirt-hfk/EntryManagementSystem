<template>
  <div class="content">
      <div>
        <el-select v-model="value" filterable remote :loading="loading"
        reserve-keyword placeholder="请输入关键词" :remote-method="remoteMethod">
        <el-option v-for="item in options" :key="item.name"
        :label="item.name" :value="item.name">
    </el-option>  
        </el-select>
      </div>
    <template v-for="subject in subjects">
        <el-card class="box-card" :key="subject.id" :body-style="{ padding: '0px' }">
          <img class="subject-image" src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png">
          <div style="padding: 14px;">
            <div style="display: inline;">
              <span style="color:#338de6;">{{subject.name}}</span>
              <el-button class="button" size="mini" @click="see(subject.id)">参加活动</el-button>
            </div>
            <div class="subject-bottom">
              <div v-if="subject.deadTime !== '0'" >
                <i class="el-icon-time" style="color: #707070; font-size:14px"></i>
                <span style="font-size:14px; margin-right:5px; color: #707070;">剩余时间{{subject.deadTime}}天</span>
                <i class="el-icon-coin" style="color: #707070; font-size:14px;margin-left: 5px"></i>
                <span style="font-size:14px; color: #707070;">{{subject.total_count}}人参加</span>
              </div>
              <div v-else>
                  <i class="el-icon-time" style="color: #f18167; font-size:14px"></i>
                  <span style="font-size:14px; margin-right:5px; color: #f18167;">活动已结束</span>
              </div>
            </div>
          </div>
        </el-card>
    </template>
  </div>
</template>

<script>
export default {
  name: "myRecommendSubject",
  data() {
    return {
      options: [],
      loading: false,
      value: [],
      status : this.$store.state.status,
      subjects: [
          {
              id: '100',
              name: '方磊',
              total_count: '2',
              deadTime: '0',
          },
          {
              id: '1',
              name: '燥起来',
              total_count: '10',
              deadTime: '5'
          },
          {
              id: '2',
              name: '科研狂人',
              total_count: '5',
              deadTime: '3'
          },
          {
              id: '3',
              name: '燥起来',
              total_count: '10',
              deadTime: '5'
          },
          {
              id: '4',
              name: '燥起来',
              total_count: '10',
              deadTime: '5'
          },
          {
              id: '5',
              name: '燥起来',
              total_count: '10',
              deadTime: '5'
          },
          {
              id: '6',
              name: '燥起来',
              total_count: '10',
              deadTime: '5'
          },
          {
              id: '7',
              name: '燥起来',
              total_count: '10',
              deadTime: '5'
          },
          {
              id: '8',
              name: '燥起来',
              total_count: '10',
              deadTime: '5'
          },
          {
              id: '9',
              name: '燥起来',
              total_count: '10',
              deadTime: '5'
          },
          {
              id: '10',
              name: '燥起来',
              total_count: '10',
              deadTime: '5'
          },
          {
              id: '11',
              name: '燥起来',
              total_count: '10',
              deadTime: '5'
          },
          {
              id: '12',
              name: '燥起来',
              total_count: '10',
              deadTime: '5'
          }
      ],
    };
  },
  mounted() {
    
  },
  methods: {
    init(){
    // 初始化数据
        this.$axios
        .post("http://localhost:8081/api/user/getRecommendSubject")
        .then(res => {
            if (res.data.data)
              this.subjects=res.data.data.subjects
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
      this.$router.push({ path: "/subject", query: { id: id } });
    },
    remoteMethod(query){
        if (query !== '') {
            this.loading = true;
            // setTimeout(() => {
            //     this.loading = false;
            //     this.options = this.list.filter(item => {
            //     return item.label.toLowerCase()
            //         .indexOf(query.toLowerCase()) > -1;
            //     });
            // }, 200);
            this.$axios
                .post("http://localhost:8081/api/user/searchSubject", {keyword:query})
                .then(res => {
                    if(res.data.data){
                        window.console.log("wozhale")
                        this.options = res.data.data.subjects;
                    }else{
                        window.console.log("wokule")
                    }
                    this.loading = false;
                })
                .catch(error => {
                      
                });
        } else {
            this.options = [];
        }
    }
  }
};
</script>

<style scoped>
.content {
  margin-left: 30px;
  width: 1200px;
  max-height: 1210px;
}
.box-card {
  float: left;
  margin-top: 10px;
  margin-left: 30px;
  margin-bottom: 10px;
  height: 210px;
  width: 236px;
}
.box-card:hover{
  border: solid 1px #52a3f5;
}
.subject-image{
  width: 234px;
  height: 130px;
}
.subject-bottom{
  margin-top: 13px;
  line-height: 12px;
}
.button{
  float: right;
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