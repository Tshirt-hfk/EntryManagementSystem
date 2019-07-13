<template>
    <div class="mysubject">
        <el-tabs v-model="activeName" @tab-click="handleClick">
            <el-tab-pane label="我的词条" name="first">
                <entryManagement></entryManagement>
            </el-tab-pane>
            <el-tab-pane label="我的收藏" name="second">
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script>
import entryManagement from "./entryManagement"
//import myJoinSubject from "./joinedSubject/myJoinSubject"

export default {
    name:"allMyEntry",
    components:{
        entryManagement
    },
    data() {
      return {
        activeName: 'first'
      };
    },
    methods: {
      handleClick(tab, event) {
        console.log(tab, event);
      }
    }
}
</script>


