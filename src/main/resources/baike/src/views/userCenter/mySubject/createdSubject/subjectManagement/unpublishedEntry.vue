<template>
  <div>
    <el-table
      ref="multipleTable"
      :data="tableData"
      tooltip-effect="dark"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="120"></el-table-column>
      <el-table-column label="名称" width="120">
        <template slot-scope="scope">{{ scope.row.name }}</template>
      </el-table-column>
      <el-table-column label="分类" width="240">
        <template slot-scope="scope">{{ scope.row.field }}</template>
      </el-table-column>
    </el-table>
    <div style="margin-top: 20px">
      <el-button @click="dialogFormVisible = true">新建词条</el-button>
      <el-button @click="publishFlag = true">发布词条</el-button>
    </div>
    <el-dialog title="新建词条" :visible.sync="dialogFormVisible" center width="400px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="名称" label-width="50px">
          <el-input v-model="form.name" autocomplete="on"></el-input>
        </el-form-item>
        <el-form-item label="分类" label-width="50px">
          <el-cascader
            placeholder="请选择领域"
            :options="options"
            v-model="form.field"
            :props="{ multiple: true, checkStrictly: true, emitPath: false }"
            :show-all-levels="false"
            style="width:100%"
            filterable
            clearable
          ></el-cascader>
        </el-form-item>
      </el-form>
      <div class="dialog-footer">
        <el-button style="float:right;" @click="dialogFormVisible = false">取 消</el-button>
        <el-button style="float:right;margin-right:20px" type="primary" @click="createEntry">确 定</el-button>
        <div class="clear"></div>
      </div>
    </el-dialog>
    <el-dialog title="发布原因" :visible.sync="publishFlag" width="650px">
      <span>
        <el-button
          style="margin-bottom:10px"
          size="mini"
          @click="inputValue='词条新建';handleInputConfirm()"
          plain
        >词条新建</el-button>
        <el-button size="mini" @click="inputValue='正文缺少图片';handleInputConfirm()" plain>正文缺少图片</el-button>
        <el-button size="mini" @click="inputValue='参考资料缺失';handleInputConfirm()" plain>参考资料缺失</el-button>
        <el-button size="mini" @click="inputValue='基本信息栏缺失';handleInputConfirm()" plain>基本信息栏缺失</el-button>
        <el-button size="mini" @click="inputValue='概述图不清晰';handleInputConfirm()" plain>概述图不清晰</el-button>
        <el-button
          style="margin-left: -1px"
          size="mini"
          @click="inputValue='概述缺失或过短';handleInputConfirm()"
          plain
        >概述缺失或过短</el-button>
        <div style="margin: 15px 0;"></div>
        <div class="publish-tag">
          <el-tag
            style="font-size: 15px"
            :key="tag"
            v-for="tag in dynamicTags"
            closable
            :disable-transitions="false"
            @close="handleClose(tag)"
          >{{tag}}</el-tag>
        </div>
        <div style="margin: 10px 0;"></div>
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="publishFlag = false">返 回</el-button>
        <el-button type="primary" @click="publishEntry">提 交</el-button>
      </span>
    </el-dialog>
  </div>
</template>


<script>
import subjectIndexVue from "../../../../subject/subjectIndex.vue";
import { log } from "util";
export default {
  name: "unpublishedEntry",
  props: ["subjectId"],
  data() {
    return {
      reason: "",
      tableData: [],
      multipleSelection: [],
      form: {
        name: "",
        field: ""
      },
      dialogFormVisible: false,
      publishFlag: false,
      dynamicTags: [],
      inputValue: "",
      options: [
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
      ]
    };
  },
  mounted() {
    this.init();
  },
  methods: {
    init() {
      this.$axios
        .post("http://localhost:8081/api/subjectMaker/getAssignment", {
          subjectId: new Number(this.subjectId),
          type: 1
        })
        .then(res => {
          if (res.data.data) {
            window.console.log(res.data.data);
            this.tableData = res.data.data.assignments;
          } else {
            //this.$message({
            //message: res.data.msg
            //});
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
    toggleSelection(rows) {
      if (rows) {
        rows.forEach(row => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    createEntry() {
      this.$axios
        .post("http://localhost:8081/api/subjectMaker/createEntry", {
          subjectId: new Number(this.subjectId),
          name: this.form.name,
          field: this.form.field
        })
        .then(res => {
          if (res.data) {
            this.dialogFormVisible = false;
            this.init();
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
    publishEntry() {
      var array = new Array();
      for (var i = 0; i < this.multipleSelection.length; i++) {
        array.push(this.multipleSelection[i].id);
      }
      this.$axios
        .post("http://localhost:8081/api/subjectMaker/publishAssignment", {
          entryIds: array,
          subjectId: new Number(this.subjectId)
        })
        .then(res => {
          if (res.data) {
            this.dialogFormVisible = false;
            this.init();
            this.stateChange(2);
            this.publishFlag = false
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
    stateChange(state) {
      window.console.log(state);
      this.$emit("stateChange", state);
    },
    handleInputConfirm() {
      let inputValue = this.inputValue;
      if (inputValue && this.dynamicTags.indexOf(inputValue) === -1) {
        this.dynamicTags.push(inputValue);
      }
      this.inputValue = "";
    },
    handleClose(tag) {
      this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1);
    }
  }
};
</script>

<style>
.el-tag + .el-tag {
  margin-left: 10px;
  margin-top: 5px;
}
.publish-tag {
  width: 610px;
  height: 80px;
  border: solid 1px #cdcdcd;
}
</style>
