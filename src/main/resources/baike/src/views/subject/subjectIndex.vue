<template>
  <div class="layout">
    <subjectBasicInfo :subjectId="subjectId"></subjectBasicInfo>
    <subjectDrawAssignment :subjectId="subjectId"></subjectDrawAssignment>
  </div>
</template>

<script>
import subjectBasicInfo from "./subjectIndex/subjectBasicInfo";

import subjectDrawAssignment from "./subjectIndex/subjectDrawAssignment";

export default {
  name: "subujectIndex",
  components: {
    subjectBasicInfo,
    subjectDrawAssignment
  },
  data() {
    return {
      subjectId: this.$route.query.id,
    };
  },
  mounted() {
  },
  methods: {
  }
};
</script>
<style scoped>
.layout {
    width: 980px;
    margin: 0 auto;
}
</style>
