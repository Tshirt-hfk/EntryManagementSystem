<template>
    <div class="layout">
        
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

<style>
.layout {
  width: 60%;
  margin: 0 auto;
  margin-top: 30px;
}
</style>
