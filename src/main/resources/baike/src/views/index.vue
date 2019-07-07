<template>
    <div>
        <!-- <myNavMenu></myNavMenu> -->
        <!-- <mySubjectMaker></mySubjectMaker> -->
        <myAssignmentCard v-bind:assignment="data"></myAssignmentCard>
        <myEmptyCard cardName="创建词条"></myEmptyCard>
    </div>
</template>

<script>

import myNavMenu from "../components/myNavMenu"

import mySubjectMaker from "../components/mySubjectMaker"

import myAssignmentCard from "../components/myAssignmentCard"

import myEmptyCard from "../components/myEmptyCard"

export default {
    name:'index',
    components:{
        myNavMenu,
        mySubjectMaker,
        myAssignmentCard,
        myEmptyCard
    },
    data(){
        return {
            data:{
                name:"test",
                reasons:["没有目录","没有参考资料","aaa"]
            }
        }
    },
    mounted(){
        
    },
    methods:{
        
    }
}
</script>