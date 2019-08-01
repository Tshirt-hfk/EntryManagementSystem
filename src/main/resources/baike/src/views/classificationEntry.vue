<template>
  <div class="classification-content">
    <ul>
        <li v-for="entry in entrys" :key="entry.id">
            <a class="classification-content-img" @click="toEntryExhibition(entry.name)">
                <img :src="entry.img_url" style="width: 88px;height: 74px">
            </a>
            <div class="classification-lemma-content">
                <h4 @click="toEntryExhibition(entry.name)">{{entry.name}}</h4>
                <p>{{entry.intro}}</p>
            </div>
        </li>      
    </ul>
  </div>
</template>

<script>

export default {
  name: "classificationEntry",
  props: ["field"],
  watch :{
      field:{
        handler(n, o){
          this.fieldName = n;
          this.init();
        }
      }
    },
  data() {
    return {
        fieldName: '',
        entrys: [
            {
                id: '1',
                name: '李鹏',
                intro: '李鹏同志遗体在北京被火化，习近平总书记亲自到场送别',
                img_url: '/static/image/李鹏.jpg'
            },{
                id: '2',
                name: '蔡徐坤',
                intro: 'UCAA艺术公益大使蔡徐坤帮助孩子走进艺术展',
                img_url: '/static/image/蔡徐坤.jpg'
            },{
                id: '3',
                name: '马思纯',
                intro: '马思纯笑对发胖，自侃是心灵美',
                img_url: '/static/image/马思纯.jpg'
            },{
                id: '4',
                name: '石原里美',
                intro: '石原里美的女神蜕变记，靠自己闯出一条爆红路',
                img_url: '/static/image/石原里美.jpg'
            }         
        ],
    };
  },
  mounted() {
    this.init();
  },
  methods: {
    init() {
      // 初始化数据
      this.$axios
        .post("/user/getClassificationEntry",{
            field: this.fieldName
        })
        .then(res => {
          if (res.data.data) 
            this.entrys = res.data.data.entrys.splice(0, 4);
        })
        .catch(error => {
          if (error.response) {
          }
        });
    },
    toEntryExhibition(entryName){
      this.$emit('toEntryExhibition', entryName);
    }
  }
};
</script>

<style scope>
h4,p{
    margin: 0;
    padding: 0;
}
.classification-content li{
    float: left;
    margin: 10px 0 0 30px;
    width: 293px;
    height: 89px;
    border-bottom: 1px solid #e9e7e6;
    zoom: 1;
    list-style: none;
}
.classification-content-img{
    float: left;
    width: 88px;
    height: 80%;
    margin-right: 10px;
    border: 1px solid #cacaca;
}
.classification-content-img:hover{
    cursor: pointer;
}
.classification-lemma-content{
    float: left;
    width: 193px;
}
.classification-lemma-content h4{
    float: left;
    font-size: 15px;
    margin-bottom: 18px;
}
.classification-lemma-content h4:hover{
    color: #459df5;
    text-decoration: underline;
    cursor: pointer;
}
.classification-lemma-content p{
    float: left;
    height: 36px;
    overflow: hidden;
    font-size: 13px;
    color: #888;
    word-break: break-word;
    text-overflow: ellipsis;
}
</style>