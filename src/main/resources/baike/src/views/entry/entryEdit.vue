<template>
  <div class="content">
    <mavon-editor
      :editable="true"
      :toolbarsFlag="true"
      :subfield="true"
      defaultOpen="preview"
	    @imgAdd="imgAdd"
	    @imgDel="imgDel"
	    @save="saveFlag = true"
      v-model="content"
    />
    <el-dialog title="提示" :visible.sync="saveFlag" width="30%">
      <span>保存词条内容？</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="saveFlag = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "entryEdit",
  data() {
    return {
      content: "",
      entryId: this.$route.query.id,
      saveFlag: false,
      toolbars: {
        bold: true, // 粗体
        italic: true, // 斜体
        header: true, // 标题
        underline: true, // 下划线
        strikethrough: true, // 中划线
        mark: true, // 标记
        superscript: true, // 上角标
        subscript: true, // 下角标
        quote: true, // 引用
        ol: true, // 有序列表
        ul: true, // 无序列表
        link: true, // 链接
        imagelink: true, // 图片链接
        code: true, // code
        table: true, // 表格
        fullscreen: true, // 全屏编辑
        readmodel: true, // 沉浸式阅读
        htmlcode: true, // 展示html源码
        help: true, // 帮助
        /* 1.3.5 */
        undo: true, // 上一步
        redo: true, // 下一步
        trash: true, // 清空
        save: true, // 保存（触发events中的save事件）
        /* 1.4.2 */
        navigation: true, // 导航目录
        /* 2.1.8 */
        alignleft: true, // 左对齐
        aligncenter: true, // 居中
        alignright: true, // 右对齐
        /* 2.2.1 */
        subfield: true, // 单双栏模式
        preview: true // 预览
      },
    };
  },
  mounted() {
  },
  methods: {
    init(){
      this.$axios
        .post("http://localhost:8081/api/user/getEntry", {
          subjectId: new Number(this.subjectId),
          type: 3
        })
        .then(res => {
          if (res.data.data) {
            this.tableData = res.data.data.assignments;
          } else {
            this.$message({
              message: res.data.msg
            });
          }
        })
        .catch(error => {
          if (error.response) {
            this.$message({
              message: error.response.data.msg,
              type: "warning"
            });
          }
        });
    },
	  save(){
      window.console.log(this.entryId)
      this.$axios
        .post("http://localhost:8081/api/user/editEntry", {
          entryId: this.entryId,
          content: this.content
        })
        .then(res => {
          this.$message({
            message: res.data.msg
          });
          this.$router.push("/usercenter/allmyentry");
        })
        .catch(error => {
          if (error.response) {
            this.$message({
              message: error.response.data.msg,
              type: "warning"
            });
          }
        });
	  },
	  imgAdd(){

	  },
	  imgDel(){

	  }
  }
};
</script>

<style>
.content {
  width: 80%;
  margin: 20px auto;
}
</style>

