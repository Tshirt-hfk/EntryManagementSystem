<template>
    <el-select
      v-model="searchValue"
      filterable
      remote
      placeholder="请输入关键词"
      :remote-method="remoteMethod"
      :loading="loading"
      style="font-size: large">
      <el-option
        v-for="item in options"
        :key="item.name"
        :label="item.name"
        :value="item.name">
      </el-option>
    </el-select>
</template>

<script>
  export default {
    name:'mySearch',
    props: {
      options:{
        type: Array,
      },
      value:{
        type: Array,
      },
      loading:{
        type: Boolean,
      }
    },
    watch :{
      loading:{
        handler(n, o){
          this.loadingSearch = n;
        }
      }
    },
    data() {
      return {
        searchValue: this.value,
        list: [],
        loadingSearch: false,
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
        this.$emit('remoteMethod', query);
          // setTimeout(() => {
          //   this.loading = false;
          //   this.options = this.list.filter(item => {
          //     return item.label.toLowerCase()
          //       .indexOf(query.toLowerCase()) > -1;
          //   });
          // }, 200);
      }
    }
  }
</script>

