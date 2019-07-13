<template>
    <div class="mysubject">
        <el-tabs v-model="activeName" @tab-click="handleClick">
            <el-tab-pane label="我创建的专题" name="first">
                <myCreatedSubject></myCreatedSubject>
            </el-tab-pane>
            <el-tab-pane label="我参与的专题" name="second">
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script>
import myCreatedSubject from "./createdSubject/myCreatedSubject"
import myJoinSubject from "./joinedSubject/myJoinSubject"

export default {
    name:"allMySubject",
    components:{
        myCreatedSubject,
        myJoinSubject
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


