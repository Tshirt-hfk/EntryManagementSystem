<template>
  <div class="myEditor">
    <!-- 工具栏 -->
    <div class="editor-toolbar">
      <div ref="toolbar" id="toolbar">
        <span class="ql-formats">
          <button class="custom-buttom" style="width:45px" @click="save">保存</button>
          <button
            ref="catalogButton"
            class="custom-buttom"
            style="width:45px;color:#06c"
            @click="catalogHanlder"
          >目录</button>
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
          <button class="ql-code"></button>
        </span>
      </div>
    </div>
    <!-- 编辑区域 -->
    <div class="editor-content">
      <!-- 目录 -->
      <div ref="catalogSide" class="catalog-side" style="display: block;">
        <div class="catalog-header">
          <h5>目录</h5>
        </div>
        <div class="catalog-holder">
          <template v-for="(item,index) in form.catalog">
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
          <h1 class="basic-info-title">{{entryName}}</h1>
          <el-form class="basic-info-form">
            <div class="basic-info-intro">
              <div class="basic-info-intro-header">
                <h2>概叙</h2>
              </div>
              <div class="basic-info-intro-content">
                <el-upload
                  class="avatar-uploader basic-info-intro-image"
                  :action="serverUrl"
                  :show-file-list="false"
                  :on-success="handleAvatarSuccess"
                  :before-upload="beforeAvatarUpload"
                >
                  <img v-if="form.imageUrl" :src="form.imageUrl" class="avatar" />
                  <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
                <el-input
                  class="basic-info-intro-text"
                  type="textarea"
                  placeholder="请输入内容"
                  v-model="form.intro"
                  maxlength="600"
                  show-word-limit
                  :rows="8"
                ></el-input>
              </div>
            </div>
            <div class="basic-info-property">
              <div class="basic-info-property-header">
                <h2>属性</h2>
              </div>
              <div>
                <template v-for="(item,index) in form.properties">
                  <div :key="index" class="basic-info-property-item">
                    <el-form-item :label="item[0]" label-width="75px" style="width:350px">
                      <el-input
                        type="text"
                        size="mini"
                        maxlength="20"
                        show-word-limit
                        v-model="form.properties[index][1]"
                      ></el-input>
                    </el-form-item>
                  </div>
                </template>
                <template v-for="(item,index) in form.editableProperties">
                  <div :key="index+1000" class="basic-info-property-item">
                    <el-form-item style="width:65px;float:left">
                      <el-input
                        class="basic-info-property-label"
                        type="text"
                        size="mini"
                        maxlength="5"
                        v-model="form.editableProperties[index][0]"
                      ></el-input>
                    </el-form-item>
                    <el-form-item style="width:275px;float:left;margin-left:10px">
                      <el-input
                        type="text"
                        size="mini"
                        maxlength="20"
                        show-word-limit
                        v-model="form.editableProperties[index][1]"
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
            <div style="margin:0px 20px">
              <template v-for="(reference, index) in form.references">
                <el-row :key="index+2000" :gutter="20">
                  <el-col :span="20">
                    <p>{{reference.title+" . "+reference.author}}</p>
                    <p>
                      <a :href="reference.url">{{reference.url}}</a>
                    </p>
                  </el-col>
                  <el-col :span="4">
                    <p>
                      <a @click="editReference(index)">编辑</a>
                      <a @click="deleteReference(index)">删除</a>
                    </p>
                    <p>
                      <a>插入正文</a>
                    </p>
                  </el-col>
                </el-row>
              </template>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- 图片上传 -->
    <el-upload
      id="inserted-image"
      :action="serverUrl"
      name="file"
      :show-file-list="false"
      :on-success="uploadSuccess"
      :on-error="uploadError"
      :before-upload="beforeUpload"
    ></el-upload>
    <!-- 添加参考资料 -->
    <el-dialog title="添加参考资料" :visible.sync="dialogFormVisible">
      <el-form :model="referenceForm">
        <el-form-item label="标题" label-width="60px">
          <el-input v-model="referenceForm.title" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="作者" label-width="60px">
          <el-input v-model="referenceForm.author" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="URL" label-width="60px">
          <el-input v-model="referenceForm.url" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleReference">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import "quill/dist/quill.core.css";
import "quill/dist/quill.snow.css";
import Quill from "quill";
import katex from "katex";
import ImageResize from "quill-image-resize-module";
import "katex/dist/katex.min.css";
window.console.log(ImageResize)
window.Quill.register("modules/imageResize", ImageResize);

export default {
  name: "myEditor",
  data() {
    return {
      entryId: 1,
      entryName: "哈哈",
      form: {
        imageUrl: "",
        intro: "",
        filed: "",
        properties: [
          ["hel", "ehl"],
          ["hel", "ehl"],
          ["hel", "ehl"],
          ["hel", "ehl"]
        ],
        editableProperties: [["", ""], ["", ""], ["", ""]],
        catalog: [],
        content: "",
        references: []
      },
      catalogOpen: true, // 目录显示控制
      serverUrl: "http://localhost:8081/resource/image",
      quillUpdateImg: false,
      referenceForm: {
        title: "",
        author: "",
        url: "",
        type: 1,
        aim: 1
      },
      dialogFormVisible: false
    };
  },
  mounted() {
    this.init();
  },
  methods: {
    init() {
      window.katex = katex;
      this.quill = new Quill("#editor", {
        theme: "snow",
        placeholder: "",
        modules: {
          formula: true,
          imageResize: {},
          toolbar: {
            container: "#toolbar",
            handlers: {
              link: function(value) {
                if (value) {
                  var href = prompt("请输入链接：");
                  this.quill.format("link", href);
                } else {
                  this.quill.format("link", false);
                }
              },
              image: function(value) {
                if (value) {
                  // 触发input框选择图片文件
                  document.querySelector("#inserted-image input").click();
                } else {
                  this.quill.format("image", false);
                }
              },
              formula: function(value) {
                if (value) {
                  var href = prompt("请输入公式：");
                  window.console.log(href);
                  this.quill.format("formula", href);
                } else {
                  this.quill.format("formula", false);
                }
              }
            }
          }
        }
      });
      this.quill.on("text-change", (delta, oldDelta, source) => {
        if (source == "api") {
          console.log("An API call triggered this change.");
        } else if (source == "user") {
          this.refreshCatalog();
          window.console.log(delta);
        }
      });
    },
    save() {
      // TODO 上传数据
      //window.console.log(this.form.content);
    },
    // 目录显示控制
    catalogHanlder() {
      if (this.catalogOpen == false) {
        this.$refs.catalogSide.style.display = "block";
        this.catalogOpen = true;
        this.$refs.catalogButton.style.color = "#06c";
      } else {
        this.$refs.catalogSide.style.display = "none";
        this.catalogOpen = false;
        this.$refs.catalogButton.style.color = "";
      }
    },
    // 更新目录
    refreshCatalog() {
      var leaf = this.quill.getLine(0)[0];
      this.form.catalog.splice(0, this.form.catalog.length);
      var i = 1;
      while (leaf != null) {
        var dom = leaf.domNode;
        leaf = leaf.next;
        var type;
        if (dom.tagName == "H1") {
          type = 1;
        } else if (dom.tagName == "H2") {
          type = 2;
        } else if (leaf == null) {
          break;
        } else {
          continue;
        }
        dom.id = "t" + i;
        var title = dom.textContent;
        this.form.catalog.push({
          title: title,
          url: "#t" + i,
          type: type
        });
        i = i + 1;
      }
    },
    // 图片上传
    beforeUpload() {
      // 显示loading动画
      this.quillUpdateImg = true;
    },
    uploadSuccess(res, file) {
      // 如果上传成功
      window.console.log(res.data);
      if (res.data && res.data.url) {
        // 获取光标所在位置
        let length = this.quill.getSelection().index;
        // 插入图片  res.url为服务器返回的图片地址
        this.quill.insertEmbed(length, "image", res.data.url);
        // 调整光标到最后
        this.quill.setSelection(length + 1);
      } else {
        this.$message.error("图片插入失败");
      }
      // loading动画消失
      this.quillUpdateImg = false;
    },
    // 富文本图片上传失败
    uploadError() {
      // loading动画消失
      this.quillUpdateImg = false;
      this.$message.error("图片插入失败");
    },
    addReference() {
      this.referenceForm.type = 1;
      this.dialogFormVisible = true;
    },
    editReference(id) {
      // 修改参考资料
      this.referenceForm.type = 2;
      this.referenceForm.aim = id;
      this.referenceForm.title = this.form.references[id].title;
      this.referenceForm.author = this.form.references[id].author;
      this.referenceForm.url = this.form.references[id].url;
      this.dialogFormVisible = true;
    },
    deleteReference(id) {
      // 删除参考资料
      if (this.form.references.length > id) {
        this.form.references.splice(id, 1);
      }
    },
    handleReference() {
      // 处理参考资料
      if (this.referenceForm.type == 1) {
        this.form.references.push({
          title: this.referenceForm.title,
          author: this.referenceForm.author,
          url: this.referenceForm.url
        });
      } else if (this.referenceForm.type == 2) {
        this.form.references[
          this.referenceForm.aim
        ].title = this.referenceForm.title;
        this.form.references[
          this.referenceForm.aim
        ].author = this.referenceForm.author;
        this.form.references[
          this.referenceForm.aim
        ].url = this.referenceForm.url;
      }
      this.referenceForm.title = "";
      this.referenceForm.author = "";
      this.referenceForm.url = "";
      this.dialogFormVisible = false;
    },
    // 词条图片上传
    handleAvatarSuccess(res, file) {
      this.form.imageUrl = res.data.url;
      window.console.log(this.form.imageUrl);
    },
    // 词条图片上传限制
    beforeAvatarUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
      }
      return isLt2M;
    }
  }
};
</script>

<style scope>
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
  margin-left: 25px;
}
.basic-info-property {
  min-height: 200px;
  padding-bottom: 15px;
}
.basic-info-property-item {
  float: left;
  margin-top: 10px;
  margin-right: 70px;
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
  line-height: 24px;
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
