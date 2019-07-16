<template>
    <el-select
      v-model="value"
      filterable
      remote
      placeholder="请输入关键词"
      :remote-method="remoteMethod"
      :loading="loading"
      style="width: 440px; font-size: large">
      <el-option
        v-for="item in options"
        :key="item.value"
        :label="item.label"
        :value="item.value">
      </el-option>
    </el-select>
</template>

<script>
  export default {
	  name:'mySearch',
    data() {
      return {
        options: [],
        value: [],
        list: [],
        loading: false,
        states: ["Alabama", "Alaska", "Arizona",
        "Arkansas", "California", "Colorado",
        "Connecticut", "Delaware", "Florida",
        "Georgia", "Hawaii", "Idaho", "Illinois",
        "Indiana", "Iowa", "Kansas", "Kentucky",
        "Louisiana", "Maine", "Maryland",
        "Massachusetts", "Michigan", "Minnesota",
        "Mississippi", "Missouri", "Montana",
        "Nebraska", "Nevada", "New Hampshire",
        "New Jersey", "New Mexico", "New York",
        "North Carolina", "North Dakota", "Ohio",
        "Oklahoma", "Oregon", "Pennsylvania",
        "Rhode Island", "South Carolina",
        "South Dakota", "Tennessee", "Texas",
        "Utah", "Vermont", "Virginia",
        "Washington", "West Virginia", "Wisconsin",
        "Wyoming"]
      }
    },
    mounted() {
      this.init()   
    },
    methods: {
      init(){
        this.list = this.states.map(item => {
            return { value: item, label: item };
        });
      },
      remoteMethod(query) {
        if (query !== '') {
          this.loading = true;
          // setTimeout(() => {
          //   this.loading = false;
          //   this.options = this.list.filter(item => {
          //     return item.label.toLowerCase()
          //       .indexOf(query.toLowerCase()) > -1;
          //   });
          // }, 200);
          this.$axios.post("http://localhost:8081/api/tourist/search", {
                  keyword:query
              }).then(res => {
                        if(res.data.data){
                          this.option = res.data.data.option
                        }else{
                          this.$message({
                            message:result.data.msg
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
        }
      }
    }
  }
</script>

