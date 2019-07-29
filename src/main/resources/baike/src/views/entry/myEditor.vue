<template>
  <div class="myEditor">
    <!-- 工具栏 -->
    <div class="editor-toolbar">
      <div ref="toolbar" id="toolbar">
        <span class="ql-formats">
          <button
            ref="catalogButton"
            class="custom-buttom"
            style="width:45px;color:#06c"
            @click="catalogHanlder"
          >目录</button>
          <button class="custom-buttom" style="width:45px" @click="save">保存</button>
          <button class="custom-buttom" style="width:45px" @click="drawerFlag = true">预览</button>
        </span>
        <span class="ql-formats">
          <button class="ql-clean"></button>
        </span>
        <span class="ql-formats">
          <button class="ql-bold"></button>
          <button class="ql-italic"></button>
          <button class="ql-underline"></button>
        </span>
        <span class="ql-formats">
          <select class="ql-color">
            <option selected></option>
            <option value="red"></option>
            <option value="orange"></option>
            <option value="yellow"></option>
            <option value="green"></option>
            <option value="blue"></option>
            <option value="purple"></option>
          </select>
          <select class="ql-background">
            <option selected></option>
            <option value="red"></option>
            <option value="orange"></option>
            <option value="yellow"></option>
            <option value="green"></option>
            <option value="blue"></option>
            <option value="purple"></option>
          </select>
        </span>
        <span class="ql-formats">
          <button class="ql-header" value="1"></button>
          <button class="ql-header" value="2"></button>
        </span>
        <span class="ql-formats">
          <button class="ql-list" value="ordered"></button>
          <button class="ql-list" value="bullet"></button>
          <!-- <button class="ql-table"></button> -->
        </span>
        <span class="ql-formats">
          <button class="ql-link"></button>
          <button class="ql-image"></button>
          <button class="ql-formula"></button>
        </span>
      </div>
    </div>
    <!-- 编辑区域 -->
    <div class="editor-content">
      <!-- 目录 -->
      <div ref="catalogSide" class="catalog-side" style="display: block;">
        <el-tabs type="border-card" class="catalog-container" stretch>
          <el-tab-pane label="目录" class="catalog-holder">
            <template v-for="(item,index) in others.catalog">
              <div :key="index+3000">
                <template v-if="item.type==1">
                  <h2>
                    <a class="catalog-title" :href="'#t'+item.index">{{item.title}}</a>
                  </h2>
                </template>
                <template v-else-if="item.type==2">
                  <h3>
                    <a class="catalog-title" :href="'#t'+item.index">{{item.title}}</a>
                  </h3>
                </template>
              </div>
            </template>
          </el-tab-pane>
          <el-tab-pane label="推荐目录">推荐目录</el-tab-pane>
        </el-tabs>
      </div>
      <div class="editor-main">
        <div class="basic-info">
          <h1 class="basic-info-title">{{form.entryName}}</h1>
          <el-form class="basic-info-form">
            <div class="basic-info-intro">
              <div class="basic-info-intro-header">
                <h2>概叙</h2>
              </div>
              <div class="basic-info-intro-content">
                <el-upload
                  class="avatar-uploader basic-info-intro-image"
                  :action="others.serverUrl"
                  :show-file-list="false"
                  :on-success="handleAvatarSuccess"
                  :before-upload="beforeAvatarUpload"
                >
                  <img v-if="form.imageUrl" :src="form.imageUrl" class="avatar" />
                  <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
                <div class="basic-info-intro-text" id="introEditor"></div>
              </div>
            </div>
            <div class="basic-info-property">
              <div class="basic-info-property-header">
                <h2>属性</h2>
              </div>
              <div>
                <template v-for="(item,index) in form.infoBox">
                  <div :key="index" class="basic-info-property-item" v-if="item.type==1">
                    <el-form-item :label="item.key" label-width="75px" style="width:350px">
                      <el-input
                        type="text"
                        size="mini"
                        maxlength="20"
                        show-word-limit
                        v-model="item.value"
                      ></el-input>
                    </el-form-item>
                  </div>
                  <div :key="index" class="basic-info-property-item" v-else>
                    <el-form-item style="width:65px;float:left">
                      <el-input
                        class="basic-info-property-label"
                        type="text"
                        size="mini"
                        maxlength="5"
                        v-model="item.key"
                      ></el-input>
                    </el-form-item>
                    <el-form-item style="width:275px;float:left;margin-left:10px">
                      <el-input
                        type="text"
                        size="mini"
                        maxlength="20"
                        show-word-limit
                        v-model="item.value"
                      ></el-input>
                    </el-form-item>
                    <div class="clear"></div>
                  </div>
                </template>
                <div class="basic-info-property-item">
                  <a
                    style="margin-left:230px;cursor:pointer;color: #3b7cff;"
                    @click="form.infoBox.push({key:'',value:'',type:2})"
                  >
                    <i class="el-icon-edit"></i> 添加自定义项
                  </a>
                </div>
                <div class="clear"></div>
              </div>
            </div>
          </el-form>
        </div>
        <div class="maincontent">
          <div class="maincontent-header">正文</div>
          <div class="maincontent-body">
            <div id="editor"></div>
          </div>
        </div>
        <div class="reference">
          <div class="reference-header">
            参考资料
            <a @click="addReference">
              <i class="el-icon-edit"></i> 添加新的参考资料
            </a>
          </div>
          <div class="reference-body">
            <div style="margin:0px 20px;padding-top:20px">
              <template v-for="(reference, index) in form.reference">
                <el-row :key="index+2000" style="margin-bottom:10px">
                  <el-col :span="6">{{reference.title+" . "+reference.author}}</el-col>
                  <el-col :span="10">
                    <a :href="reference.url" style="outline: none;">{{reference.url}}</a>
                  </el-col>
                  <el-col :span="8" style="height:100%">
                    <el-button @click="editReference(index)" size="mini">编辑</el-button>
                    <el-button @click="deleteReference(index)" size="mini" type="danger">删除</el-button>
                    <el-button size="mini">插入正文</el-button>
                  </el-col>
                </el-row>
              </template>
            </div>
          </div>
        </div>
      </div>
      <!-- 上下位关系 -->
      <div class="relationship-side">
        <div class="relationship-side-title">
          <h5>词条关系</h5>
        </div>
        <div class="relationship-side-operation">
          <el-alert
            title="请谨慎选择词条关系"
            type="warning"
            show-icon
            :closable="false"
            style="width: 250px;margin: 0 auto;margin-bottom: 10px;"
            center
          ></el-alert>
          <mySearch
            style="margin-left: 15px;width: 210px;"
            v-on:remoteMethod="remoteMethod"
            :options="options"
            :value="value"
            :loading="loading"
          ></mySearch>
          <el-select v-model="relation" placeholder="关系选择" style="width: 150px">
            <el-option v-for="item in optionInRelation" :key="item" :label="item" :value="item"></el-option>
          </el-select>
          <el-button class="relation-button-add" type="primary" @click="toAddRelation">添加</el-button>
        </div>
        <div class="relation-table">
          <el-table :data="relationData" style="width: 100%">
            <el-table-column prop="name" label="词条名" width="150px"></el-table-column>
            <el-table-column prop="relation" label="关系" width="150px"></el-table-column>
            <el-table-column label="操作">
              <template slot-scope="scope">
                <el-button size="mini" type="danger" @click="toDeleteRelation(scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>
    <!-- 图片上传 -->
    <el-upload
      id="inserted-image"
      :action="others.serverUrl"
      name="file"
      :show-file-list="false"
      :on-success="uploadSuccess"
      :on-error="uploadError"
    ></el-upload>
    <!-- 添加参考资料 -->
    <el-dialog title="添加参考资料" :visible.sync="others.dialogFormVisible" width="500px">
      <el-form :model="others.referenceForm">
        <el-form-item label="标题" label-width="60px">
          <el-input v-model="others.referenceForm.title" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="作者" label-width="60px">
          <el-input v-model="others.referenceForm.author" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="URL" label-width="60px">
          <el-input v-model="others.referenceForm.url" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="others.dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleReference">确 定</el-button>
      </div>
    </el-dialog>
    <entryReview
      :relationData="relationData"
      :form="form"
      :drawerFlag="drawerFlag"
      v-on:handleClose="handleClose"
    ></entryReview>
  </div>
</template>

<script>
import mySearch from "../../components/mySearch";
import entryReview from "../../components/entryReview";

import "quill/dist/quill.core.css";
import "quill/dist/quill.snow.css";
import "quill/dist/quill.Bubble.css";
import Quill from "quill";
import katex from "katex";
import ImageResize from "quill-image-resize-module";
import "katex/dist/katex.min.css";
window.Quill.register("modules/imageResize", ImageResize);

export default {
  name: "myEditor",
  components: {
    mySearch,
    entryReview
  },
  data() {
    return {
      drawerFlag: false,
      options: [],
      loading: false,
      value: [],
      relation: "",
      optionInRelation: ["上位词", "下位词", "同位词"],
      relationData: [
        {
          name: "哈哈",
          relation: "上位词"
        },
        {
          name: "嘻嘻",
          relation: "下位词"
        },
        {
          name: "呵呵",
          relation: "同位词"
        }
      ],
      taskId: this.$route.query.id,
      form: {
        entryName: "哈哈",
        field: [],
        imageUrl: "",
        intro: "",
        infoBox: [],
        content: "",
        reference: []
      },
      introEditor: {
        editor: null,
        intro: ""
      },
      contenteditor: {
        editor: null,
        content: ""
      },
      recommend: {},
      others: {
        catalog: [],
        catalogOpen: true, // 目录显示控制
        serverUrl: "/resource/image",
        dialogFormVisible: false,
        referenceForm: {
          title: "",
          author: "",
          url: "",
          type: 1,
          aim: 1
        }
      }
    };
  },
  mounted() {
    this.initData();
  },
  methods: {
    initData() {
      this.$axios
        .post("/api/user/getTaskContent", {
          taskId: new Number(this.taskId)
        })
        .then(res => {
          if (res.data.data) {
            this.form.entryName = res.data.data.entryName;
            this.form.imageUrl = res.data.data.imageUrl;
            this.form.intro = res.data.data.intro;
            for (var field of res.data.data.field) {
              this.form.field.push(field);
            }
            for (var info of res.data.data.infoBox) {
              this.form.infoBox.push(info);
            }
            this.form.content = res.data.data.content;
            this.introEditor.intro = this.form.intro;
            this.contenteditor.content = this.form.content;
            this.initIntroEditor();
            this.initContentEditor();
          } else {
            this.$message({
              message: res.data.msg,
              type: "warning"
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
    initIntroEditor() {
      this.introEditor.editor = new Quill("#introEditor", {
        theme: "bubble",
        modules: {
          toolbar: ["bold", "italic", "underline", "strike", "link"]
        }
      });
      this.introEditor.editor.root.innerHTML = this.introEditor.intro;
      this.introEditor.editor.on("text-change", (delta, oldDelta, source) => {
        if (source == "api") {
          console.log("An API call triggered this change.");
        } else if (source == "user") {
          this.form.intro = this.introEditor.editor.root.innerHTML;
        }
      });
    },
    initContentEditor() {
      window.katex = katex;
      this.contenteditor.editor = new Quill("#editor", {
        theme: "snow",
        modules: {
          formula: true,
          imageResize: {},
          toolbar: {
            container: "#toolbar",
            handlers: {
              link: value => {
                if (value) {
                  var href = prompt("请输入链接：");
                  this.contenteditor.editor.format("link", href);
                } else {
                  this.contenteditor.editor.format("link", false);
                }
              },
              image: value => {
                if (value) {
                  // 触发input框选择图片文件
                  document.querySelector("#inserted-image input").click();
                } else {
                  this.contenteditor.editor.format("image", false);
                }
              },
              formula: value => {
                if (value) {
                  var href = prompt("请输入公式：");
                  this.contenteditor.editor.format("formula", href);
                } else {
                  this.contenteditor.editor.format("formula", false);
                }
              }
            }
          }
        }
      });
      this.contenteditor.editor.root.innerHTML = this.contenteditor.content;
      this.contenteditor.editor.on("text-change", (delta, oldDelta, source) => {
        if (source == "api") {
          console.log("An API call triggered this change.");
        } else if (source == "user") {
          this.form.content = this.contenteditor.editor.root.innerHTML;
          this.refreshCatalog();
        }
      });
    },
    // 词条图片上传限制
    beforeAvatarUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
      }
      return isLt2M;
    },
    save() {
      // 未完成， 需要把关系写入数据库
      this.$axios
        .post("/api/user/saveTaskContent", {
          taskId: new Number(this.taskId),
          form: this.form
        })
        .then(res => {
          if (res.data) {
            this.$message({
              message: res.data.msg
            });
            this.$router.push("/usercenter/myentry");
          } else {
            this.$message({
              message: res.data.msg,
              type: "warning"
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
    // 目录显示控制
    catalogHanlder() {
      if (this.others.catalogOpen == false) {
        this.$refs.catalogSide.style.display = "block";
        this.others.catalogOpen = true;
        this.$refs.catalogButton.style.color = "#06c";
      } else {
        this.$refs.catalogSide.style.display = "none";
        this.others.catalogOpen = false;
        this.$refs.catalogButton.style.color = "";
      }
    },
    // 更新目录
    refreshCatalog() {
      var nodes = this.contenteditor.editor.root.childNodes;
      this.others.catalog.splice(0, this.others.catalog.length);
      var i1 = 0;
      var i2 = 0;
      for (var node of nodes) {
        var type;
        if (node.tagName == "H1") {
          type = 1;
          i1 = i1 + 1;
          i2 = 0;
        } else if (node.tagName == "H2") {
          type = 2;
          i2 = i2 + 1;
        } else {
          continue;
        }
        var index = i1.toString();
        if (i2 != 0) index = index + "." + i2.toString();
        node.id = "t" + index;
        var title = node.textContent;
        this.others.catalog.push({
          title: title,
          index: index,
          type: type
        });
      }
    },
    uploadSuccess(res, file) {
      // 如果上传成功
      if (res.data && res.data.url) {
        // 获取光标所在位置
        let length = this.contenteditor.editor.getSelection().index;
        // 插入图片  res.url为服务器返回的图片地址
        this.contenteditor.editor.insertEmbed(length, "image", res.data.url);
        // 调整光标到最后
        this.contenteditor.editor.setSelection(length + 1);
      } else {
        this.$message.error("图片插入失败");
      }
    },
    // 富文本图片上传失败
    uploadError() {
      this.$message.error("图片插入失败");
    },
    // 词条图片上传
    handleAvatarSuccess(res, file) {
      this.form.imageUrl = res.data.url;
    },
    // 参考资料
    addReference() {
      this.others.referenceForm.type = 1;
      this.others.dialogFormVisible = true;
    },
    editReference(id) {
      this.others.referenceForm.type = 2;
      this.others.referenceForm.aim = id;
      this.others.referenceForm.title = this.form.reference[id].title;
      this.others.referenceForm.author = this.form.reference[id].author;
      this.others.referenceForm.url = this.form.reference[id].url;
      this.others.dialogFormVisible = true;
    },
    deleteReference(id) {
      if (this.form.reference.length > id) {
        this.form.reference.splice(id, 1);
      }
    },
    handleReference() {
      // 处理参考资料
      if (this.others.referenceForm.type == 1) {
        this.form.reference.push({
          title: this.others.referenceForm.title,
          author: this.others.referenceForm.author,
          url: this.others.referenceForm.url
        });
      } else if (this.others.referenceForm.type == 2) {
        this.form.reference[
          this.others.referenceForm.aim
        ].title = this.others.referenceForm.title;
        this.form.reference[
          this.others.referenceForm.aim
        ].author = this.others.referenceForm.author;
        this.form.reference[
          this.others.referenceForm.aim
        ].url = this.others.referenceForm.url;
      }
      this.others.referenceForm.title = "";
      this.others.referenceForm.author = "";
      this.others.referenceForm.url = "";
      this.others.dialogFormVisible = false;
    },
    remoteMethod(query) {
      if (query !== "") {
        this.loading = true;
        this.value = query;
        this.$axios
          .post("http://192.168.1.121:9000/", { keyword: query }) //向远程服务器模糊搜索
          .then(res => {
            if (res.data.data) {
              this.options = res.data.data.entrys;
            }
            this.loading = false;
          })
          .catch(error => {});
      } else {
        this.options = [];
      }
    },
    toAddRelation() {
      let arr = [
        {
          name: this.aimEntry,
          relation: this.relation
        }
      ];
      tableData.push(arr);
    },
    toDeleteRelation(index) {
      this.tableData.splice(index, 1);
      window.console.log("nmh");
    },
    handleClose(done) {
      this.drawerFlag = false;
    }
  }
};
</script>

<style>
.myEditor {
  position: absolute;
  left: 0px;
  right: 0px;
  bottom: 0px;
  top: 35px;
  font-family: SimSun, Arial;
  text-align: left;
  word-break: break-all;
}
/* 编辑工具栏 */
.editor-toolbar {
  position: absolute;
  z-index: 999;
  width: 100%;
  height: 42px;
}
.editor-toolbar .custom-buttom {
  outline: none;
}

/* 编辑器 */
.editor-content {
  position: absolute;
  width: 100%;
  overflow-y: auto;
  top: 42px;
  bottom: 0px;
  background: rgb(240, 240, 240);
}
.editor-main {
  position: relative;
  z-index: 998;
  width: 880px;
  margin: 0 auto;
  padding-top: 7px;
}

.basic-info,
.maincontent,
.reference {
  margin-bottom: 7px;
  min-height: 800px;
  width: 100%;
  border: 1px solid #c8c9cc;
  background: rgb(255, 255, 255);
}

/* 基本信息 */
.basic-info-title {
  font-weight: normal;
  font-family: 微软雅黑;
  font-size: 30px;
  position: relative;
  color: rgb(178, 178, 178);
  padding: 19px 21px 0px;
}
.basic-info-form {
  margin: 20px 20px 0;
  padding-bottom: 15px;
  height: 100%;
}
.basic-info-property,
.basic-info-intro {
  min-height: 300px;
  border-bottom: 1px dotted #dbdbdb;
}

.basic-info-intro-header,
.basic-info-property-header {
  margin-top: 7px;
  border-bottom: 2px solid #ccc;
  background-color: #fff;
  padding-left: 12px;
  overflow: hidden;
}
.basic-info-intro-header h2,
.basic-info-property-header h2 {
  font-size: 20px;
  color: #333;
  float: left;
  line-height: 22px;
}
.basic-info-intro-content {
  padding-top: 25px;
}
.basic-info-intro-image {
  float: left;
  margin-left: 25px;
}
.basic-info-intro-text {
  float: left;
  width: 600px;
  height: 178px;
  margin-left: 25px;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
}
.basic-info-intro-text p {
  margin-bottom: 5px;
  text-indent: 2em;
  word-wrap: break-word;
  line-height: 24px;
}
.basic-info-property {
  min-height: 300px;
  padding-bottom: 15px;
}
.basic-info-property-item {
  float: left;
  margin-top: 10px;
  margin-right: 40px;
  margin-left: 20px;
}
.basic-info-property-item .el-form-item {
  margin-bottom: 0;
}
.basic-info-property-label input {
  padding: 0 0;
  text-align: right;
}

.reference {
  min-height: 200px;
}
.maincontent-header,
.reference-header {
  font-size: 20px;
  color: #333;
  font-weight: bold;
  margin: 30px 30px 0 30px;
  border-bottom: 2px solid #ccc;
  padding-left: 12px;
  padding-bottom: 6px;
}
.reference-header a {
  outline: none;
  font-size: 12px;
  color: #3b7cff;
  float: right;
  cursor: pointer;
}
.maincontent-body,
.reference-body {
  font-family: sans-serif;
  font-size: 16px;
  overflow: hidden;
  padding: 0 16px;
}

.ql-container.ql-snow {
  border: 0px;
}

.ql-snow .ql-editor {
  margin: 0 15px;
  background: #fff;
  color: #000;
  padding: 0px;
}
.ql-snow .ql-editor p {
  margin-bottom: 5px;
  text-indent: 2em;
  word-wrap: break-word;
  line-height: 18px;
}
.ql-snow .ql-editor h1 {
  font-size: 16px;
  font-weight: bold;
  line-height: 24px;
  border-bottom: 1px solid #dedfe1;
  padding-bottom: 5px;
  margin: 35px 0 15px;
  clear: both;
}
.ql-snow .ql-editor h2 {
  font-size: 15px;
  font-family: Arial;
  line-height: 22px;
  margin: 22px 0 12px;
}

/* 菜单栏 */
.catalog-side {
  position: fixed;
  top: 84px;
  left: 0px;
  bottom: 7px;
  width: 400px;
  background: rgb(255, 255, 255);
}
.catalog-container {
  width: 100%;
  height: 100%;
}
.catalog-holder {
  padding-left: 10px;
}
.catalog-holder h2,
.catalog-holder h3 {
  font-size: 14px;
  font-weight: normal;
  line-height: 24px;
  margin: 0;
}
.catalog-holder h2 {
  background: url(https://baike.baidu.com/static/editor/img/catalog-h1-icon_72e0d273.png)
    0 9px no-repeat;
  margin-bottom: 5px;
  margin-top: 5px;
  padding-left: 16px;
}
.catalog-holder h3 {
  padding: 0 8px 0 25px;
  background: url(https://baike.baidu.com/static/editor/img/catalog-h2-icon_ddc25a3d.png)
    12px 11px no-repeat;
}
.catalog-holder .catalog-title {
  text-decoration: none;
  display: inline-block;
  color: #4c6c99;
}

/* 图片上传 */
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
/* 上下位关系 */
.relationship-side {
  display: block;
  position: fixed;
  top: 84px;
  right: 0px;
  bottom: 7px;
  width: 400px;
  background: rgb(255, 255, 255);
}
.relationship-side-title {
  height: 38px;
  border-bottom: 2px solid #e3e3e6;
  text-align: center;
}
.relationship-side-title h5 {
  height: 38px;
  line-height: 38px;
  font-size: 18px;
  font-weight: bold;
  margin-top: 6px;
  color: #409EFF;
}
.relationship-side-operation {
  margin-top: 15px;
  border-bottom: 2px dashed #e3e3e6;
  padding-bottom: 5px;
}
.relation-button-add {
  margin: 0 auto;
  margin-top: 8px;
  width: 80px;
  margin-left: 305px;
}
.relation-table {
  font-family: Arial, Helvetica, sans-serif;
}

.clear {
  clear: both;
  width: 0;
  height: 0;
  margin: 0;
  padding: 0;
  border: 0;
}
</style>
