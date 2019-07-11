<template>
  <div id="app" style="background: #f1f1f1">
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