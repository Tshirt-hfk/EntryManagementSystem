<template>
    <el-card style="width:81%; margin:40px auto 0">
        <el-table width="80%" :data="data" border>
            <el-table-column type='index' width="50" align="center"></el-table-column>
            <el-table-column label="标题" align="center">
                <template slot-scope="scope">
                    <el-button type="text" @click="handleTitle(scope.row.id)">{{scope.row.topic}}</el-button>
                </template>
            </el-table-column>
            <el-table-column prop="likeNums" label="点赞数" align="center"></el-table-column>
            <el-table-column prop="topicTime" label="发表时间" align="center"></el-table-column>
            <el-table-column prop="createUser" label="发表人" align="center"></el-table-column>
            <el-table-column prop="topicReplyCount" label="评论数" align="center"></el-table-column>
        </el-table>
    </el-card>
</template>

<script>
    export default {
        name:'index',
        data(){
            return {
                data:[{
                    id:1,
                    topic:'题目',
                    likeNums:102,
                    topicTime:'2019.1.14',
                    createUser:'小明',
                    topicReplyCount:20
                }],
                searchData:'', //搜索内容
            }
        },
        mounted(){
            
        },
        methods:{
            
        }
    }
</script>