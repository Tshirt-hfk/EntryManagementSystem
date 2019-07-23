<template>
<div class="preview-layout">
	<div class="preview-searchbar">
        <div class="preview-logo-headl">
            <a title="首页" href="/">
            <img src="https://wx3.sinaimg.cn/mw690/ad5be381gy1g51mw04nn2j206t01mgli.jpg">
            </a>
        </div>
        <div class="preview-search">
            <mySearch style="width: 440px;"></mySearch>
            <el-button type="primary" @click="search" style="margin-left: 10px">搜索词条</el-button>
        </div>
		
    </div>
	<div class="preview-main">
		<div class="preview-divideline">
			<span class="preview-tit" style="font-size: 25px;">词条内容</span>
		</div>
		<el-card class="preview-intro">
			<div class="preview-main-wrap">
				<div class="preview-tool-bar">
					<div class="tool-bar-collect">
						<i class="el-icon-star-off"></i>
						<span>收藏</span>
					</div>
					<div class="tool-bar-line"> </div>
					<div class="tool-bar-like">
						<i class="el-icon-thumb"></i>
						<span>{{likeNum}}</span>
					</div>
				</div>
				<div class="clearfloat"></div>
				<div class="preview-entryName">
					<h1>{{entryName}}</h1>
					<h2>({{field}})</h2>
					<el-button size="mini" @click="toEntryEdit">编辑</el-button>
				</div>
				<div class="preview-entryintro">
					{{form.intro}}
				</div>
				<div class="preview-entry-attribute">
					<div class="preview-basicinfo-title">
						<h2 class="preview-basicinfo-h2">基本信息</h2>
					</div>
					<dl class="preview-attribute-left">
						<div v-for="(prop, index) in form.properties" :key="prop[0]">
							<div v-if="index % 2 == 0">
							<dt>{{prop[0]}}</dt>
							<span style="float:left">:</span>
							<dd>{{prop[1]}}</dd>
							</div>
						</div>
					</dl>
					<dl class="preview-attribute-right">
						<div v-for="(prop, index) in form.properties" :key="prop[0]">
							<div v-if="index % 2 !== 0">
							<dt>{{prop[0]}}</dt>
							<span style="float:left">:</span>
							<dd>{{prop[1]}}</dd>
							</div>
						</div>
					</dl>
				</div>
				<div class="preview-catalog">
					<div class="preview-catalog-title">
						<h2 class="preview-title-h2">目录</h2>
					</div>
					<div class="preview-cataloglist">
						<ol v-for="n in columns" :key="n">
							<li style="line-height: 28px" v-for="(cata, index) in form.catalog.slice((n-1)*12)" :key="index">
								<div v-if="index < 12">
								<template v-if="cata.type == 1">
									<span class="catalog-index1">{{indexNum++}}</span>
									<span class="catalog-text1">
										<a :href="cata.url">{{cata.title}}</a>
									</span>
								</template>
								<template v-else>
									<span class="catalog-index2">▪</span>
									<span class="catalog-text2">
										<a :href="cata.url">{{cata.title}}</a>
									</span>
								</template>
								</div>
							</li>
						</ol>
					</div>
				</div>
				<div class="preview-content" id="mainContent">
					<div v-html="form.content" class="ql-editor ql-snow"></div>
				</div>
			</div>
			<div class="preview-side-wrap">
				<div class="preview-picture">
					<img style="margin: 0 auto" :src="form.imageUrl">
				</div>
				<div class="preview-side-catalog" style="visibility: hidden;bottom: 10px;" id="sideRoller">
					<div class="preview-side-bar">
						<i class="el-icon-help circle-start"></i>
						<i class="el-icon-help circle-end"></i>
					</div>
					<div class="preview-side-roller">
						<div class="inner-container">
						<ol class="preview-side-catalist">
							<li style="line-height: 28px" v-for="(cata, index) in form.catalog" :key="index">
								<template v-if="cata.type == 1">
									<a class="side-cata-pointer"></a>
									<span class="catalog-index1">{{sideIndex++}}</span>
									<span class="catalog-text1">
										<a :href="cata.url">{{cata.title}}</a>
									</span>
								</template>
								<template v-else>
									<span class="catalog-index2">▪</span>
									<span class="catalog-text2">
										<a :href="cata.url">{{cata.title}}</a>
									</span>
								</template>
							</li>
						</ol>
						</div>
					</div>
					<div class="preview-side-button">
						<div class="preview-side-up" @click="toPageTop">UP</div>
					</div>
				</div>
			</div>
		</el-card>
    </div>
</div>
</template>

<script>

import mySearch from "../components/mySearch";

export default {
	name:"entryExhibition",
	components:{
		mySearch
	},
	computed:{
		// columns:function(){		
			
		// }
	},
	data(){
		return {
			name:this.$route.query.name,
			value:"",
			likeNum: '0',
			indexNum: 1,
			sideIndex: 1,
			columns: 4,
			entryId: 1,
			entryName: '方磊',
			field: '动物',
			form: {
				imageUrl: "https://gss3.bdstatic.com/-Po3dSag_xI4khGkpoWK1HF6hhy/baike/w%3D268%3Bg%3D0/sign=6841e3a2464a20a4311e3bc1a869ff1f/71cf3bc79f3df8dcfa573421c411728b47102813.jpg",
				intro: "黑龙江省，简称“黑”，中华人民共和国省级行政区，省会哈尔滨。位于中国东北地区北部，界于北纬43°26′—53°33′，东经121°11′—135°05′之间，北、东部与俄罗斯相望，西部与内蒙古相邻，南部与吉林接壤，是中国最北端以及陆地最东端的省级行政区。总面积47.3万平方千米。",
				filed: "",
				properties: [
				["中文名称", "方磊"],
				["身高", "150cm"],
				["体重", "180kg"],
				["爱好", "搞黄色"]
				],
				catalog: [
					{
						title: '历史沿革',
						url: '#t1',
						type: 1
					},{
						title: '行政区划',
						url: '#t2',
						type: 1
					},{
						title: '地理环境',
						url: '#t1',
						type: 1
					},{
						title: '位置境遇',
						url: '#t1',
						type: 2
					},{
						title: 'hh',
						url: '#t1',
						type: 2
					},{
						title: 'hh',
						url: '#t1',
						type: 2
					},{
						title: 'hh',
						url: '#t1',
						type: 2
					},{
						title: 'hh',
						url: '#t1',
						type: 2
					},{
						title: 'hh',
						url: '#t1',
						type: 2
					},{
						title: 'hh',
						url: '#t1',
						type: 2
					},{
						title: 'hh',
						url: '#t1',
						type: 2
					},{
						title: '自然资源',
						url: '#t1',
						type: 1
					},{
						title: 'hh',
						url: '#t1',
						type: 2
					},{
						title: 'hh',
						url: '#t1',
						type: 2
					},{
						title: 'hh',
						url: '#t1',
						type: 2
					},{
						title: 'hh',
						url: '#t1',
						type: 2
					},{
						title: 'hh',
						url: '#t1',
						type: 2
					},
				],
				content: '<h1 id="t1">方磊</h1> <h2 id="t2">色情</h2> <p>黑龙江省是中国位置最北、最东，纬度最高，经度最东的省份，西起121°11′E，东至135°05′E，南起43°26′N，北至53°33′N，东西跨14个经度，南北跨10个纬度，2个热量带；东西跨14个经度，3个湿润区。面积47.3万平方千米（含加格达奇区和松岭区）。北部和东部与俄罗斯相邻，边境线长3045千米，是亚洲与太平洋地区陆路通往俄罗斯远东和欧洲大陆的重要通道，西部与南部分别与内蒙古和吉林省相邻，东部近日本海。</p>',
				references: []
			},
		}
	},
	mounted(){
		this.init()
		window.addEventListener('scroll', this.handleScroll)
	},
	methods:{
		init(){
			window.console.log(this.name)
			// 获取词条数据 
			// this.$axios.get(
            //     "http://127.0.0.1:5000/fetchPageByName",{params:{
			// 		name:this.name
			// 	}}
            // ).then(res => {
            //     if(res.data){
			// 		window.console.log("test")
            //         this.value = res.data.page_content
            //     } else {
            //     this.$message({
            //         message:res.data.msg,
            //         type:"warning"
            //     });
            //     }
            // }).catch(error => {
            //     if(error.response){
            //         this.$message({
            //             message:error.response.data.msg,
            //             type:"warning"
            //         });
            //     }
			// });	
		},

		toEntryEdit(){

		},
		search() {
        //TODO
		},
		toPageTop(){
			header.scrollIntoView();
		},
		handleScroll(){
			var scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop
			var offsetTop = document.querySelector('#mainContent').offsetTop
			if (scrollTop > offsetTop) {
				var fbox = document.getElementById("sideRoller");
				fbox.style.visibility = "visible";
			}else{
				var fbox = document.getElementById("sideRoller");
				fbox.style.visibility = "hidden";
			}
		}
	}
}
</script>

<style scoped>
@import "quill/dist/quill.core.css";
@import "quill/dist/quill.snow.css";
@import "katex/dist/katex.min.css";

dl,dd,ol,h1,h2,p{
	margin:0;   
	padding:0; 
}
.ql-editor{
	margin: 0;
	padding: 0;
}
.preview-content>>> h1 {
  font-size: 26px;
  font-weight: 400;
  line-height: 28px;
  border-bottom: 1px solid #ddd;
  padding-bottom: 8px;
  font-family: Arial;
  color: #000;
  margin: 0 0 3px 0;
  clear: both;
}
.preview-content>>> h1::before{
	content: '';
    display: inline-block;
    width: 10px;
    height: 28px;
    background-color: #4d95dc;
    position: absolute;
    margin-left: -50px;
	margin-top: 2px;
}
.preview-content>>> h2 {
  font-size: 20px;
  font-family: Arial;
  line-height: 22px;
  font-weight: 400;
  margin: 0;
  color: #333;
}
.preview-content>>> p{
	font-size: 15px;
	font-family: Arial;
    word-wrap: break-word;
    color: #333;
	margin: 0;
    text-indent: 2em;
    line-height: 24px;
    zoom: 1;
}
.preview-layout {
  width: 100%;
  margin: 0 auto;
  position: relative;
}
.preview-searchbar{
  width: 808px;
  height: 105px;
  margin: 0 auto;
  position: relative;
  font-family: arial;
}
.preview-logo-headl{
  padding: 25px 0 35px;
  float: left;
  line-height: 1;
}
.preview-search{
  float: left;
  padding: 37px 0;
}
.preview-main{
  width: 1170px;
  margin: 0 auto;
  min-height: 700px;
  margin-top: 10px;
}
.preview-intro{
	margin-top: 45px;
}
.preview-divideline{
  height: 28px;
  border-bottom: 1px solid #ddd;
  margin-bottom: 28px;
  text-align: center;
}
.preview-divideline .preview-tit{
  line-height: 56px;
  padding: 0 20px;
  margin-right: 415px;
  background: #fff;
}
.preview-main-wrap{
  float: left;
  width: 790px;
  padding: 0 29px 0;
}
.preview-tool-bar{
	float: right;
}
.tool-bar-collect{
	height: 18px;
	float: right;
	cursor: pointer;
}
.tool-bar-collect:hover{
	color: #1296db;
}
.tool-bar-line{
  float:left;
  border-right: 1px solid #707070;
  height: 18px;
  margin-left: 5px;
  margin-right:5px;
  float: right;
}
.tool-bar-like{
	height: 18px;
	float: right;
	cursor: pointer;
}
.tool-bar-like:hover{
	color: #1296db;
}
.clearfloat{
	width: 0;
    height: 0;
    clear: both;
}
.preview-entryName{
	margin: 0 0 10px;
    width: 700px;
}
.preview-entryName h1{
	margin: 0 10px 0 0;
    display: inline;
    font-size: 34px;
    line-height: 1.15;
    font-weight: 400;
    vertical-align: sub;
}
.preview-entryName h2{
	margin: 0 50px 0 0;
    display: inline;
    font-weight: 400;
    font-size: 20px;
    vertical-align: sub;
}
.preview-entryintro{
	clear: both;
    font-size: 14px;
    word-wrap: break-word;
    color: #333;
	margin-top: 20px;
    margin-bottom: 15px;
    text-indent: 2em;
    line-height: 24px;
}
.preview-picture{
	width: 268px;
    border: solid 1px #DDD;
    margin-bottom: 20px;
    box-shadow: 1px 1px 1px #ccc;
    position: relative;
}
.preview-side-wrap{
  width: 270px;
  float: right;
}
.preview-entry-attribute{
	margin: 20px 0 35px;
	clear: both;
	background: #fafcff;
	min-height: 200px;
}
.preview-basicinfo-title{
	display: block;
    clear: both;
    zoom: 1;
    overflow: hidden;
	border-bottom: 1px solid #ddd;
	padding-bottom: 8px;
	background: #fff;
	margin: 35px 0 10px 0;
}
.preview-basicinfo-h2{
	float: left;
    display: block;
    line-height: 28px;
    font-size: 26px;
    font-weight: 400;
    color: #000;
    background: #fff;
}
.preview-basicinfo-h2::before{
	content: '';
    display: inline-block;
    width: 10px;
    height: 28px;
    background-color: #4d95dc;
    position: absolute;
    margin-left: -50px;
	margin-top: 2px;
}
.preview-attribute-left{
	width: 395px;
    float: left;
}
.preview-attribute-right{
	width: 395px;
    float: right;
}
.preview-entry-attribute dt{
	display: block;
	width: 90px;
    margin: 0 5px 0 12px;
    font-weight: 700;
    color: #1296db;
	line-height: 26px;
    padding: 0;
    margin: 0;
    float: left;
	text-align: center;
}
.preview-entry-attribute dd{
	color: #333;
    width: 285px;
    float: left;
    position: relative;
    word-break: break-all;
	line-height: 26px;
    display: block;
	margin-left: 10px;
}
.preview-catalog{
	margin-bottom: 35px;
	float:left;
    border-bottom: 1px solid #ddd;
    background: #fbfbfb;
}
.preview-catalog-title{
    display: block;
    clear: both;
    zoom: 1;
    overflow: hidden;
	border-bottom: 1px solid #ddd;
	padding-bottom: 8px;
	background: #fff;
}
.preview-title-h2{
    float: left;
    display: block;
    line-height: 28px;
    font-size: 26px;
    font-weight: 400;
    color: #000;
    background: #fff;
}
.preview-title-h2::before{
	content: '';
    display: inline-block;
    width: 10px;
    height: 28px;
    background-color: #4d95dc;
    position: absolute;
    margin-left: -50px;
	margin-top: 2px;
}
.preview-cataloglist{
	display: block;
    float: left;
    width: 790px;
    position: relative;
    overflow: hidden;
    padding-top: 15px;
    padding-bottom: 20px;
    background-color: #fff;
}
.catalog-index1{
	display: inline-block;
    width: 18px;
    font-size: 16px;
    padding-left: 20px;
    padding-right: 8px;
    vertical-align: top;
    text-align: right;
    color: #63a0df;
}
.catalog-text1{
	display: inline-block;
    font-size: 16px;
    font-weight: 500;
    background-color: #fff;
    vertical-align: top;
    width: 120px;
}
.catalog-index2{
	display: inline-block;
    padding-left: 41px;
    padding-right: 5px;
    width: 5px;
    line-height: 16px;
    font-size: 12px;
    vertical-align: top;
    color: #ccc;
}
.catalog-text2{
	display: inline-block;
    line-height: 16px;
    font-size: 12px;
    width: 115px;
    vertical-align: top;
}
.preview-cataloglist a{
	color: #136ec2;
	text-decoration: none;
}
.preview-cataloglist ol{
	width: 195px;
	list-style: none;
	border-left: 1px solid #f5f5f5;
	float:left
}
.preview-cataloglist a:hover{
	text-decoration: underline;
}
.preview-content{
	clear: both;
    overflow: hidden;
    zoom: 1;
    font-size: 14px;
    line-height: 25px;
    word-wrap: break-word;
	height: 2000px;
}
.preview-side-catalog{
	width: 270px;
    height: 456px;
    position: fixed;
    bottom: 10px;
    font-size: 14px;
    font-family: 宋体;
    line-height: 19px;
}
.preview-side-bar{
	position: absolute;
    top: 0;
    width: 0;
    height: 353px;
    background-color: #eaeaea;
    border: 1px solid #eaeaea;
    border-top: 0;
    border-bottom: 0;
    left: 5px;
	font-size: 10px;
}
.circle-start{
	top: 0;
	position: absolute;
    left: -5.5px;
	top: -8px;
    width: 11px;
    height: 11px;
	color: #bfbfbf;
    zoom: 1;
    overflow: hidden;
}
.circle-end{
	margin: 0;
    bottom: 0;
    position: absolute;
    left: -5.5px;
	bottom: -8px;
    width: 11px;
    height: 11px;
	color: #bfbfbf;
    zoom: 1;
    overflow: hidden;
}
.preview-side-roller{
	padding-top: 10px;
    position: absolute;
    z-index: 1;
    top: 0;
    left: 0;
    width: 210px;
    height: 330px;
    overflow: hidden;
}
.inner-container{
	position: absolute; 
    overflow-x: hidden;
    overflow-y: scroll;
}
.preview-side-catalist{
	width: 210px;
    height: 330px;
    position: relative;
}
.preview-side-catalist a{
	color: #136ec2;
	text-decoration: none;
}
.preview-side-catalist a:hover{
	text-decoration: underline;
}
.side-cata-pointer{
	font-size: 10px;
	background: #136ec2;
	border-radius: 15px;
	position: absolute;
    display: block;
    width: 8px;
    height: 8px;
    cursor: pointer;
    margin-top: -17px;
    left: 2px;
}
.preview-side-button{
	position: absolute;
    bottom: 45px;
    height: 50px;
	width: 100%;
}
.preview-side-up{
	margin-top: 10px;
	margin-left: 5px;
	font-size: 20px;
	font-weight: bold;
	height: 50px;
	width: 50px;
	background-color: #f2f5f6;
	box-shadow: 0 0 6px rgba(0,0,0, .12);
	text-align: center;
	line-height: 50px;
	color: #1989fa;
	cursor: pointer;
}
.preview-side-up:hover{
	background:rgba(82, 163, 245,0.1)
}
</style>


