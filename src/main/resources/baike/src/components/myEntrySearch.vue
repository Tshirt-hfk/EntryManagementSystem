<template>
  <el-autocomplete
  	style="width: 440px"
    :fetch-suggestions="querySearch"
    placeholder="请输入词条名称"
    :trigger-on-focus="false"
    @select="handleSelect"
	:value="value"
    @input="input"
  ></el-autocomplete>
</template>

<script>
import { fail } from "assert";
export default {
  name: "myEntrySearch",
  props: ["value"],
  data() {
    return {
      loading: false
    };
  },
  methods: {
    input(value) {
      this.$emit(input, value);
    },
    querySearch(query, cb) {
      // this.loading = true;
      // this.value = query;
      // this.$axios
      //   .post("/api/user/searchSubject", {
      //     keyword: query
      //   })
      //   .then(res => {
      //     if (res.data.data) {
      //       this.options = res.data.data.subjects;
      //     } else {
      //     }
      //     this.loading = false;
      //   })
      //   .catch(error => {});
	},
	handleSelect(item) {
        
    }
  }
};
</script>
