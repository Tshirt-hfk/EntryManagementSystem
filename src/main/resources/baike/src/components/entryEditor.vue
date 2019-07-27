<template>
  <div class="my-ck-editor">
    <div class="editor-toolbar" ref="toolbar"></div>
    <div class="editor-content">
      <!-- 目录 -->
      <div class="catalog-side" style="display: block;">
        <div class="catalog-header">
          <h5>目录</h5>
        </div>
        <div class="catalog-holder">
          <template v-for="(item,index) in others.catalog">
            <div :key="index+3000">
              <template v-if="item.type==1">
                <h2>
                  <a class="catalog-title" :href="item.url">{{item.title}}</a>
                </h2>
              </template>
              <template v-else-if="item.type==2">
                <h3>
                  <a class="catalog-title" :href="item.url">{{item.title}}</a>
                </h3>
              </template>
            </div>
          </template>
        </div>
      </div>
      <div class="editor-main">
        <div class="basic-info">
          <h1 class="basic-info-title">{{form.entryName}}</h1>
          <el-form class="basic-info-form">
            <div class="basic-info-intro">
              <div class="basic-info-intro-header">
                <h2>概述</h2>
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
                <div class="basic-info-intro-text">
                  <ckeditor
                    class="my-editor"
                    :editor="basicInfoEditor.editor"
                    :config="basicInfoEditor.editorConfig"
                    :value="basicInfoEditor.intro"
                    @onEditorInput="onIntroEditorInput"
                  ></ckeditor>
                </div>
              </div>
            </div>
            <div class="basic-info-property">
              <div class="basic-info-property-header">
                <h2>属性</h2>
              </div>
              <div>
                <template v-for="(item,index) in others.properties">
                  <div :key="index" class="basic-info-property-item">
                    <el-form-item :label="item[0]" label-width="75px" style="width:350px">
                      <el-input
                        type="text"
                        size="mini"
                        maxlength="20"
                        show-word-limit
                        v-model="others.properties[index][1]"
                      ></el-input>
                    </el-form-item>
                  </div>
                </template>
                <template v-for="(item,index) in others.editableProperties">
                  <div :key="index+1000" class="basic-info-property-item">
                    <el-form-item style="width:65px;float:left">
                      <el-input
                        class="basic-info-property-label"
                        type="text"
                        size="mini"
                        maxlength="5"
                        v-model="others.editableProperties[index][0]"
                      ></el-input>
                    </el-form-item>
                    <el-form-item style="width:275px;float:left;margin-left:10px">
                      <el-input
                        type="text"
                        size="mini"
                        maxlength="20"
                        show-word-limit
                        v-model="others.editableProperties[index][1]"
                      ></el-input>
                    </el-form-item>
                    <div class="clear"></div>
                  </div>
                </template>
                <div class="basic-info-property-item">
                  <a
                    style="margin-left:230px;cursor:pointer;color: #3b7cff;"
                    @click="form.editableProperties.push(['',''])"
                  >
                    <i class="el-icon-edit"></i> 添加自定义项
                  </a>
                </div>
                <div class="clear"></div>
              </div>
            </div>
          </el-form>
        </div>
        <div class="main-content">
          <div class="main-content-header">正文</div>
          <div class="main-content-body">
            <ckeditor
              class="my-editor"
              :editor="contentEditor.editor"
              :config="contentEditor.editorConfig"
              :value="contentEditor.content"
              @ready="onReady"
              @input="onContentEditorInput"
            >></ckeditor>
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
    </div>
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
  </div>
</template>

<script>
import {
  DecoupledEditor,
  InlineEditor
} from "@ckeditor/ckeditor5-build-decoupled-document";
import "@ckeditor/ckeditor5-build-decoupled-document/build/translations/zh-cn.js";
import MyUploadAdapter from "./myUploadAdapter.js";

export default {
  name: "entryEditor",
  data() {
    return {
      id: 0,
      form: {
        entryName: "",
        imageUrl: "",
        field: [],
        intro: "",
        infoBox: [],
        content: "",
        reference: []
      },
      basicInfoEditor: {
        intro:"",
        editorObject: null,
        editor: InlineEditor,
        editorConfig: {
          language: "zh-cn",
          toolbar: [
            "bold",
            "italic",
            "underline",
            "strikethrough",
            "|",
            "fontColor",
            "fontBackgroundColor",
            "|",
            "link",
            "blockquote",
            "|",
            "removeFormat",
            "|",
            "undo",
            "redo"
          ]
        }
      },
      contentEditor: {
        content:"",
        editorObject: null,
        editor: DecoupledEditor,
        editorConfig: {
          language: "zh-cn",
          toolbar: [
            "heading",
            "|",
            "bold",
            "italic",
            "underline",
            "strikethrough",
            "|",
            "fontColor",
            "fontBackgroundColor",
            "|",
            "numberedList",
            "bulletedList",
            "|",
            "link",
            "blockquote",
            "imageUpload",
            "insertTable",
            "|",
            "removeFormat",
            "|",
            "undo",
            "redo"
          ],
          heading: {
            options: [
              {
                model: "paragraph",
                title: "Paragraph",
                class: "ck-heading_paragraph"
              },
              {
                model: "heading1",
                view: "h2",
                title: "Heading 1",
                class: "ck-heading_heading1"
              },
              {
                model: "heading2",
                view: "h3",
                title: "Heading 2",
                class: "ck-heading_heading2"
              }
            ]
          }
        }
      },
      others: {
        dialogFormVisible: false,
        referenceForm: {
          title: "",
          author: "",
          url: "",
          type: 1,
          aim: 1
        },
        serverUrl: "http://localhost:8081/resource/image",
        catalog: [],
        properties: [
          ["hel", "ehl"],
          ["hel", "ehl"],
          ["hel", "ehl"],
          ["hel", "ehl"]
        ],
        editableProperties: [["", ""], ["", ""], ["", ""]]
      }
    };
  },
  methods: {
    onReady(editor) {
      // Insert the toolbar
      this.$refs.toolbar.appendChild(editor.ui.view.toolbar.element);
      editor.plugins.get("FileRepository").createUploadAdapter = loader => {
        return new MyUploadAdapter(loader);
      };
      this.contentEditor.editorObject = editor;
    },
    // 词条图片上传
    handleAvatarSuccess(res, file) {
      this.form.imageUrl = res.data.url;
    },
    // 词条图片上传限制
    beforeAvatarUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
      }
      return isLt2M;
    },
    // 绑定词条内容
    onContentEditorInput(editor) {
      this.form.content = editor;
      this.refreshCatalog();
    },
    // 绑定词条介绍
    onIntroEditorInput(editor) {
      this.form.intro = editor;
    },
    // 保存
    save() {},
    // 更新目录
    refreshCatalog() {
      var leafs = this.contentEditor.editorObject.sourceElement.childNodes;
      this.others.catalog.splice(0, this.others.catalog.length);
      var i = 1;
      for (var leaf of leafs) {
        var dom = leaf;
        var type;
        if (dom.tagName == "H2") {
          type = 1;
        } else if (dom.tagName == "H3") {
          type = 2;
        } else {
          continue;
        }
        dom.id = "t" + i;
        var title = dom.textContent;
        this.others.catalog.push({
          title: title,
          url: "#t" + i,
          type: type
        });
        i = i + 1;
      }
    },
    // 参考资料
    addReference() {
      this.others.referenceForm.type = 1;
      this.others.dialogFormVisible = true;
    },
    editReference(id) {
      // 修改参考资料
      this.others.referenceForm.type = 2;
      this.others.referenceForm.aim = id;
      this.others.referenceForm.title = this.form.reference[id].title;
      this.others.referenceForm.author = this.form.reference[id].author;
      this.others.referenceForm.url = this.form.reference[id].url;
      this.others.dialogFormVisible = true;
    },
    deleteReference(id) {
      // 删除参考资料
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
    }
  }
};
</script>

<style>
.my-ck-editor {
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
  height: 40px;
}

/* 编辑器 */
.editor-content {
  position: absolute;
  width: 100%;
  overflow-y: auto;
  top: 40px;
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
.main-content,
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
.basic-info-property {
  min-height: 200px;
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
.main-content-header,
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

.main-content-body,
.reference-body {
  font-family: sans-serif;
  font-size: 14px;
  overflow: hidden;
  padding: 0 21px;
}

/* 正文 */
.my-editor {
  border: 0px;
  background: #fff;
  color: #000;
  padding: 0px;
}
.my-editor p {
  text-indent: 2em;
  word-wrap: break-word;
  line-height: 24px;
  font-size: 14px;
}
.my-editor h2 {
  font-size: 16px;
  font-weight: bold;
  line-height: 24px;
  border-bottom: 1px solid #dedfe1;
  padding-bottom: 5px;
  margin: 35px 0 15px;
  clear: both;
}
.my-editor h3 {
  font-size: 15px;
  font-family: Arial;
  line-height: 22px;
  margin: 22px 0 12px;
}

/* 菜单栏 */
.catalog-side {
  position: fixed;
  top: 82px;
  left: 0px;
  bottom: 7px;
  width: 240px;
  background: rgb(255, 255, 255);
}
.catalog-header {
  background-color: #fafafa;
}
.catalog-header h5 {
  height: 38px;
  line-height: 38px;
  font-size: 18px;
  font-weight: bold;
  padding-left: 20px;
  padding-bottom: 10px;
  margin-top: 12px;
  border-bottom: 1px solid #e3e3e6;
  overflow: hidden;
}
.catalog-holder {
  margin-left: 18px;
  margin-top: 10px;
  color: #333;
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

.clear {
  clear: both;
  width: 0;
  height: 0;
  margin: 0;
  padding: 0;
  border: 0;
}
</style>
