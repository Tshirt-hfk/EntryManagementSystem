<template>
  <div id="app" style="background: #f1f1f1; height:100vh">
    <myHeader/>
    <router-view/>
  </div>
</template>

<script>
import myHeader from './components/myHeader.vue'

export default {
  name: 'app',
  components:{
    myHeader
  }
}
</script>