<template>
    <div>
        <myNavMenu></myNavMenu>
        <!-- <mySubjectMaker></mySubjectMaker> -->
        <!-- <myAssignmentCard v-bind:assignment="data" v-bind:state="true"></myAssignmentCard> -->
        <!-- <myEmptyCard cardName="创建词条"></myEmptyCard> -->
    </div>
</template>

<script>

import myNavMenu from "../components/myNavMenu"

import mySubjectMaker from "../components/mySubjectMaker"

import myAssignmentCard from "../components/myAssignmentCard"


export default {
    name:'index',
    components:{
        myNavMenu,
        mySubjectMaker,
        myAssignmentCard,
    },
    data(){
        return {
            data:{
                name:"test",
                id:1
            }
        }
    },
    mounted(){
        
    },
    methods:{
        
    }
}
</script>