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
                    {{item.index}}
                    <a class="catalog-title" :href="'#t'+item.index" style="color: #3f81c1;">{{item.title}}</a>
                  </h2>
                </template>
                <template v-else-if="item.type==2">
                  <h3>
                    {{item.index}}
                    <a class="catalog-title" :href="'#t'+item.index" style="color: #000;">{{item.title}}</a>
                  </h3>
                </template>
              </div>
            </template>
          </el-tab-pane>
          <el-tab-pane label="推荐目录" class="recata-holder">
            <a class="recata-btn-apply" @click="applyRecommendCatalog">应用</a>
            <template v-for="(item,index) in others.recommendcatalog">
              <div :key="index+3000">
                <template v-if="item.type==1">
                  <h2>
                    {{item.index}}
                    <a class="catalog-title">{{item.title}}</a>
                  </h2>
                </template>
                <template v-else-if="item.type==2">
                  <h3>
                    {{item.index}}
                    <a class="catalog-title">{{item.title}}</a>
                  </h3>
                </template>
              </div>
            </template>
          </el-tab-pane>
        </el-tabs>
      </div>
      <div class="editor-main">
        <div class="basic-info">
          <div class="basic-info-head">
            <h1 class="basic-info-title">{{form.entryName}}</h1>
            <el-tag
              :key="tag"
              v-for="tag in form.field"
              closable
              :disable-transitions="false"
              @close="handleFieldDelete(tag)"
              style="font-size: 16px;margin-left:8px;">
              {{tag}}
            </el-tag>
            <el-cascader
              class="select-new-tag"
              v-if="others.selectVisible"
              placeholder="请选择领域"
              ref="saveTagSelect"
              :options="others.fieldOptions"
              v-model="others.selectValue"
              :props="{ multiple: true, checkStrictly: true, emitPath: false }"
              :show-all-levels="false"
              filterable
              clearable
              @change="addField"
            ></el-cascader>
            <el-button v-else class="button-new-tag" size="small" @click="showSelect">添加领域</el-button>
          </div>
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
                <div style="float:right;margin-top:5px">
                  <el-select v-model="others.selectAttribute" placeholder="请选择导入的基本属性" style="margin-bottom: 10px;">
                    <el-option
                      v-for="item in attributeOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                      @change="refreshAttribute">
                    </el-option>
                  </el-select>
                  <a
                    style="cursor:pointer;color:#3b7cff;"
                    @click="form.infoBox.push({key:'',value:''})"
                  >
                    <i class="el-icon-edit"></i> 添加自定义项
                  </a>
                </div>
              </div>
              <div>
                <template v-for="(item,index) in form.infoBox">
                  <div :key="index" class="basic-info-property-item">
                    <el-form-item style="width:65px;float:left">
                      <el-input
                        class="basic-info-property-label"
                        type="text"
                        size="mini"
                        maxlength="10"
                        v-model="item.key"
                      ></el-input>
                    </el-form-item>
                    <el-form-item style="width:255px;float:left;margin-left:10px">
                      <el-input
                        type="text"
                        size="mini"
                        maxlength="20"
                        v-model="item.value"
                      ></el-input>
                    </el-form-item>
                    <div style="float:left;margin-left:8px;line-height:40px;">
                      <a
                        style="cursor:pointer"
                        class="el-icon-delete"
                        @click="form.infoBox.splice(i,1)"
                      ></a>
                    </div>
                    <div class="clear"></div>
                  </div>
                </template>
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
        fieldOptions: [
          {
            value: "人物",
            label: "人物",
            children: [
              {
                value: "各职业人物",
                label: "各职业人物",
                children: [
                  { value: "人类学家", label: "人类学家" },
                  { value: "哲学家", label: "哲学家" },
                  { value: "地理学家", label: "地理学家" },
                  { value: "思想家", label: "思想家" },
                  { value: "性学家", label: "性学家" },
                  { value: "民俗学家", label: "民俗学家" },
                  { value: "美学家", label: "美学家" },
                  { value: "艺术史学家", label: "艺术史学家" },
                  { value: "语言学家", label: "语言学家" },
                  { value: "宗教人物", label: "宗教人物" },
                  { value: "神学家", label: "神学家" },
                  { value: "宇航员", label: "宇航员" },
                  { value: "工程师", label: "工程师" },
                  { value: "建筑师", label: "建筑师" },
                  { value: "机械学家", label: "机械学家" },
                  { value: "水利学家", label: "水利学家" },
                  { value: "发明家", label: "发明家" },
                  { value: "程序员", label: "程序员" },
                  { value: "飞行员", label: "飞行员" },
                  { value: "君主", label: "君主" },
                  { value: "外交官", label: "外交官" },
                  { value: "政治人物", label: "政治人物" },
                  { value: "社会运动者", label: "社会运动者" },
                  { value: "军事人物", label: "军事人物" },
                  { value: "革命家", label: "革命家" },
                  { value: "作家", label: "作家" },
                  { value: "翻译家", label: "翻译家" },
                  { value: "词人", label: "词人" },
                  { value: "诗人", label: "诗人" },
                  { value: "服装设计师", label: "服装设计师" },
                  { value: "模特", label: "模特" },
                  { value: "动画师", label: "动画师" },
                  { value: "漫画家", label: "漫画家" },
                  { value: "游戏设计师", label: "游戏设计师" },
                  { value: "艺术总监", label: "艺术总监" },
                  { value: "陶艺家", label: "陶艺家" },
                  { value: "雕刻家", label: "雕刻家" },
                  { value: "魔术师", label: "魔术师" },
                  { value: "厨师", label: "厨师" },
                  { value: "调酒师", label: "调酒师" },
                  { value: "商人", label: "商人" },
                  { value: "收藏家", label: "收藏家" },
                  { value: "工业设计师", label: "工业设计师" },
                  { value: "书法家", label: "书法家" },
                  { value: "画家", label: "画家" },
                  { value: "雕塑家", label: "雕塑家" },
                  { value: "摄影师", label: "摄影师" },
                  { value: "作曲家", label: "作曲家" },
                  { value: "指挥家", label: "指挥家" },
                  { value: "歌手", label: "歌手" },
                  { value: "音乐制作人", label: "音乐制作人" },
                  { value: "音乐家", label: "音乐家" },
                  { value: "演员", label: "演员" },
                  { value: "舞者", label: "舞者" },
                  { value: "导演", label: "导演" },
                  { value: "音效师", label: "音效师" },
                  { value: "主持人", label: "主持人" },
                  { value: "主播", label: "主播" },
                  { value: "编辑", label: "编辑" },
                  { value: "配音员", label: "配音员" },
                  { value: "历史学家", label: "历史学家" },
                  { value: "图书馆学家", label: "图书馆学家" },
                  { value: "教育家", label: "教育家" },
                  { value: "社会学家", label: "社会学家" },
                  { value: "经济学家", label: "经济学家" },
                  { value: "考古学家", label: "考古学家" },
                  { value: "金融家", label: "金融家" },
                  { value: "农学家", label: "农学家" },
                  { value: "化学家", label: "化学家" },
                  { value: "地质学家", label: "地质学家" },
                  { value: "天文学家", label: "天文学家" },
                  { value: "数学家", label: "数学家" },
                  { value: "物理学家", label: "物理学家" },
                  { value: "生物学家", label: "生物学家" },
                  { value: "计算机科学家", label: "计算机科学家" },
                  { value: "医学家", label: "医学家" },
                  { value: "医生", label: "医生" },
                  { value: "运动员", label: "运动员" },
                  { value: "裁判员", label: "裁判员" },
                  { value: "电影人", label: "电影人" },
                  { value: "律师", label: "律师" },
                  { value: "法官", label: "法官" }
                ]
              },
              {
                value: "特殊人物",
                label: "特殊人物",
                children: [
                  { value: "罪犯", label: "罪犯" },
                  { value: "烈士", label: "烈士" },
                  { value: "司法人物", label: "司法人物" },
                  { value: "神话人物", label: "神话人物" },
                  { value: "虚构角色", label: "虚构角色" }
                ]
              }
            ]
          },
          {
            value: "休闲",
            label: "休闲",
            children: [
              {
                value: "活动",
                label: "活动",
                children: [
                  { value: "旅游", label: "旅游" },
                  { value: "漫画", label: "漫画" },
                  { value: "锻练", label: "锻练" },
                  { value: "烹饪", label: "烹饪" },
                  { value: "收藏", label: "收藏" },
                  { value: "园艺", label: "园艺" },
                  { value: "摄影", label: "摄影" },
                  { value: "宠物", label: "宠物" },
                  { value: "展览", label: "展览" }
                ]
              },
              {
                value: "娱乐",
                label: "娱乐",
                children: [
                  { value: "电影", label: "电影" },
                  { value: "电视剧", label: "电视剧" },
                  { value: "综艺节目", label: "综艺节目" },
                  { value: "广播", label: "广播" }
                ]
              },
              {
                value: "制品",
                label: "制品",
                children: [{ value: "玩具", label: "玩具" }]
              }
            ]
          },
          {
            value: "历史",
            label: "历史",
            children: [
              {
                value: "历史著作",
                label: "历史著作",
                children: [{ value: "历史书籍", label: "历史书籍" }]
              },
              {
                value: "历史概念",
                label: "历史概念",
                children: [
                  { value: "历史学", label: "历史学" },
                  { value: "年代", label: "年代" }
                ]
              },
              { value: "考古学", label: "考古学" },
              { value: "世界史", label: "世界史" },
              { value: "传说", label: "传说" }
            ]
          },
          {
            value: "司法",
            label: "司法",
            children: [
              { value: "法律", label: "法律" },
              { value: "司法制度", label: "司法制度" },
              { value: "司法组织", label: "司法组织" },
              { value: "案件", label: "案件" }
            ]
          },
          {
            value: "哲学",
            label: "哲学",
            children: [
              { value: "逻辑", label: "逻辑" },
              { value: "伦理学", label: "伦理学" },
              { value: "哲学问题", label: "哲学问题" },
              { value: "哲学概念", label: "哲学概念" }
            ]
          },
          {
            value: "地理",
            label: "地理",
            children: [
              { value: "地理学", label: "地理学" },
              { value: "国家", label: "国家" },
              { value: "城市", label: "城市" },
              { value: "地图", label: "地图" },
              { value: "时区", label: "时区" },
              { value: "方位", label: "方位" },
              { value: "地区", label: "地区" },
              { value: "地形", label: "地形" },
              { value: "气候", label: "气候" },
              { value: "土壤", label: "土壤" },
              { value: "河流", label: "河流" },
              { value: "山", label: "山" }
            ]
          },
          {
            value: "宗教",
            label: "宗教",
            children: [
              { value: "佛教", label: "佛教" },
              { value: "基督教", label: "基督教" },
              { value: "伊斯兰教", label: "伊斯兰教" },
              { value: "道教", label: "道教" }
            ]
          },
          { value: "心理学", label: "心理学" },
          {
            value: "文学",
            label: "文学",
            children: [
              { value: "修辞", label: "修辞" },
              {
                value: "文学体裁",
                label: "文学体裁",
                children: [
                  { value: "小说", label: "小说" },
                  { value: "散文", label: "散文" },
                  { value: "论文", label: "论文" },
                  { value: "文言文", label: "文言文" },
                  { value: "白话文", label: "白话文" },
                  { value: "戏剧", label: "戏剧" },
                  { value: "诗歌", label: "诗歌" },
                  { value: "儿童文学", label: "儿童文学" },
                  { value: "传记", label: "传记" }
                ]
              },
              { value: "文学奖", label: "文学奖" },
              { value: "文学组织", label: "文学组织" },
              { value: "作家协会", label: "作家协会" },
              {
                value: "传播类型",
                label: "传播类型",
                children: [
                  { value: "杂志", label: "杂志" },
                  { value: "报纸", label: "报纸" }
                ]
              }
            ]
          },
          {
            value: "物质",
            label: "物质",
            children: [
              { value: "化学元素", label: "化学元素" },
              {
                value: "混合物",
                label: "混合物",
                children: [
                  { value: "合金", label: "合金" },
                  { value: "溶液", label: "溶液" },
                  { value: "玻璃", label: "玻璃" }
                ]
              },
              {
                value: "纯净物",
                label: "纯净物",
                children: [
                  { value: "酸", label: "酸" },
                  { value: "碱", label: "碱" },
                  { value: "盐", label: "盐" }
                ]
              },
              { value: "金属", label: "金属" },
              { value: "暗物质", label: "暗物质" },
              { value: "黑洞", label: "黑洞" }
            ]
          },
          {
            value: "社会",
            label: "社会",
            children: [
              {
                value: "交通",
                label: "交通",
                children: [
                  { value: "交通工具", label: "交通工具" },
                  {
                    value: "航空",
                    label: "航空",
                    children: [
                      { value: "航空器", label: "航空器" },
                      { value: "航天器", label: "航天器" },
                      { value: "航空公司", label: "航空公司" },
                      { value: "机场", label: "机场" }
                    ]
                  },
                  {
                    value: "公路",
                    label: "公路",
                    children: [
                      { value: "汽车", label: "汽车" },
                      { value: "汽车品牌", label: "汽车品牌" }
                    ]
                  },
                  {
                    value: "铁路",
                    label: "铁路",
                    children: [
                      { value: "铁路公司", label: "铁路公司" },
                      { value: "铁路车站", label: "铁路车站" },
                      { value: "铁路线", label: "铁路线" }
                    ]
                  }
                ]
              },
              {
                value: "军事",
                label: "军事",
                children: [
                  { value: "军事组织", label: "军事组织" },
                  {
                    value: "军事装备",
                    label: "军事装备",
                    children: [
                      {
                        value: "武器",
                        label: "武器",
                        children: [
                          { value: "枪械", label: "枪械" },
                          { value: "导弹", label: "导弹" },
                          { value: "坦克", label: "坦克" },
                          { value: "军舰", label: "军舰" }
                        ]
                      },
                      { value: "战争", label: "战争" }
                    ]
                  },
                  {
                    value: "教育",
                    label: "教育",
                    children: [
                      { value: "大学", label: "大学" },
                      { value: "考试", label: "考试" }
                    ]
                  },
                  {
                    value: "文化",
                    label: "文化",
                    children: [
                      { value: "节日", label: "节日" },
                      { value: "语言", label: "语言" }
                    ]
                  },
                  {
                    value: "生活",
                    label: "生活",
                    children: [
                      { value: "服装", label: "服装" },
                      { value: "美容", label: "美容" }
                    ]
                  },
                  {
                    value: "经济",
                    label: "经济",
                    children: [
                      { value: "财政", label: "财政" },
                      { value: "贸易", label: "贸易" },
                      { value: "管理学", label: "管理学" },
                      { value: "经济学", label: "经济学" }
                    ]
                  }
                ]
              },
              {
                value: "科学",
                label: "科学",
                children: [
                  { value: "科学组织", label: "科学组织" },
                  { value: "科学竞赛", label: "科学竞赛" }
                ]
              },
              { value: "技术", label: "技术" },
              {
                value: "艺术",
                label: "艺术",
                children: [
                  { value: "雕塑作品", label: "雕塑作品" },
                  { value: "绘画作品", label: "绘画作品" },
                  { value: "陶瓷", label: "陶瓷" },
                  { value: "舞蹈作品", label: "舞蹈作品" },
                  { value: "书法作品", label: "书法作品" },
                  { value: "乐器", label: "乐器" },
                  { value: "舞蹈", label: "舞蹈" },
                  { value: "歌曲", label: "歌曲" },
                  { value: "音乐奖项", label: "音乐奖项" }
                ]
              },
              {
                value: "自然",
                label: "自然",
                children: [
                  { value: "动物", label: "动物" },
                  { value: "植物", label: "植物" }
                ]
              }
            ]
          }
        ],
        selectVisible: false,
        selectValue: '',
        selectAttribute: '',
        attributeOptions: [],
        recommendcatalog: [
          {
            title: '标题1',
            index: '1',
            type: 1
          },{
            title: '标题1.1',
            index: '1.1',
            type: 2
          },{
            title: '标题1.2',
            index: '1.2',
            type: 2
          },{
            title: '标题2',
            index: '2',
            type: 1
          },{
            title: '标题2.1',
            index: '2.1',
            type: 2
          }
        ],
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
    applyRecommendCatalog(){

    },
    handleFieldDelete(tag){
      this.form.field.splice(this.form.field.indexOf(tag), 1);
    },
    showSelect() {
      this.others.selectVisible = true;
      // this.$nextTick(_ => {
      //   this.$refs.saveTagSelect.$refs.input.focus();
      // });
    },
    addField(){
      let selectValue = this.others.selectValue;
      if (selectValue) {
        this.form.field.push(selectValue[0]);
      }
      this.others.selectVisible = false;
      this.others.selectValue = '';
    },
    refreshAttribute(){

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
.basic-info-head{
  width: 100%;
  margin: 30px 0 15px;
}
.basic-info-title {
  font-weight: normal;
  font-family: 微软雅黑;
  font-size: 30px;
  position: relative;
  color: rgb(178, 178, 178);
  margin-left: 30px;
  margin-right: 15px;
  display: inline;
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
  margin-top: 15px;
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
  font-size: 15px;
}
.ql-snow .ql-editor p {
  margin-bottom: 5px;
  text-indent: 2em;
  word-wrap: break-word;
  line-height: 24px;
}
.ql-snow .ql-editor h1 {
  font-size: 20px;
  font-weight: bold;
  line-height: 24px;
  border-bottom: 1px solid #dedfe1;
  padding-bottom: 5px;
  margin: 35px 0 15px;
  clear: both;
}
.ql-snow .ql-editor h2 {
  font-size: 18px;
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
  margin-top: 32px;
}
.catalog-holder .catalog-title {
  text-decoration: none;
  display: inline-block;
}
.catalog-holder h2{
  font-size: 18px;
  font-weight: 550;
  color: #3f81c1;
}
.catalog-holder h3 {
  margin-left: 10px;
  font-size: 14px;
  font-weight: 500;
  color: #000;
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
  border: solid 1px #d5d5d5;
  box-shadow: -2px 2px 2px #e7e7e7;
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
  color: #409eff;
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
.recata-btn-apply{
  color: #fff;
  border: solid 1px #409EFF;
  border-radius: 6px;
  background: #409EFF;
  text-align: center;
  padding: 7px 0px 7px 10px;
  font-size: 15px;
  text-decoration: none;
  margin-left: 143px;
  font-family: Arial, Helvetica, sans-serif;
  cursor: pointer;
  letter-spacing: 10px;
}
.recata-btn-apply:hover{
  opacity: 0.9;
}
.recata-holder{
  margin-left: 10px;
}
.recata-holder h2{
  font-size: 18px;
  font-weight: 550;
  color: #3f81c1;
}
.recata-holder h3{
  margin-left: 10px;
  font-size: 14px;
  font-weight: 500;
  color: #000;
}
.el-tag + .el-tag {
  margin-left: 10px;
}
.button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
  width: 150px;
}
.select-new-tag {
  height: 35px;
  width: 150px;
  margin-left: 10px;
  vertical-align: bottom;
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
