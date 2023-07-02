<template>
  <div :style="background">
    <div class="blog-banner" :style="background" />
    <v-row class="blog-container">
      <v-col :md="3" :cols="12" class="hidden-xs-only">
        <v-card class="blog-sider">
          <v-card class="blog-sider-function">
            <div>
              <router-link to="/">
                <span>首页</span>
              </router-link>
            </div>
            <div style="padding:50px 0">
              <span @click="handleAdd">新博客</span>
            </div>
          </v-card>
          <v-card style="height:calc(100% - 500px)">
            <el-calendar />
          </v-card>
        </v-card>
      </v-col>

      <v-col :xs="12" :md="9" :cols="12">
        <div>
          <v-row class="home-container">
            <!-- 博主信息 -->
            <v-col :md="12" :cols="12" class="d-md-block d-none">
              <v-card class="blog-wrapper">
                <v-row>
                  <v-col :md="2" :cols="12">
                    <div class="author-wrapper">
                      <!-- 博主头像 -->
                      <v-avatar size="110">
                        <img class="author-avatar" :src="userInfo.avatar" />
                      </v-avatar>
                    </div>
                  </v-col>
                  <v-col :md="10" :cols="12" style="margin-top:240px">
                    <div style="font-size: 1.375rem">
                      {{ userInfo.author }}
                    </div>
                    <div style="font-size: 0.875rem;">
                      {{ userInfo.authorInfo }}
                    </div>
                    <!-- 博客信息 -->
                    <div class="blog-info-wrapper">
                      <div class="blog-info-data">
                        <router-link to="/archives">
                          <div style="font-size: 0.875rem">文章</div>
                          <div style="font-size: 1.25rem">
                            {{ userInfo.blogCount }}
                          </div>
                        </router-link>
                      </div>
                      <div class="blog-info-data">
                        <router-link to="/categories">
                          <div style="font-size: 0.875rem">分类</div>
                          <div style="font-size: 1.25rem">
                            {{ userInfo.categoryCount }}
                          </div>
                        </router-link>
                      </div>
                      <div class="blog-info-data">
                        <div style="font-size: 0.875rem">阅读量</div>
                        <div style="font-size: 1.25rem">
                          {{ userInfo.blogViews }}
                        </div>
                      </div>
                    </div>
                  </v-col>
                </v-row>
              </v-card>
            </v-col>

            <v-col :md="12" :cols="12" class="article-list">
              <v-card v-for="(item, index) of articleList" :key="item.id" class="article-card" style="border-radius: 12px 8px 8px 12px;">
                <div class="article-card-height">
                  <!-- 文章信息 -->
                  <div class="article-wrapper">
                    <div>
                      <router-link :to="'/articles/' + item.id" class="article-title-overflow">
                        {{ item.title }}
                      </router-link>
                    </div>
                    <div class="article-info article-tag-overflow">
                      <!-- 是否置顶 -->
                      <span v-if="item.isStick === 1">
                        <span style="color:#ff7242">
                          <i class="iconfont iconzhiding" /> 置顶
                        </span>
                        <span class="separator">|</span>
                      </span>
                      <!-- 发表时间 -->
                      <v-icon size="14">mdi-calendar-month-outline</v-icon>
                      {{ item.createTime | date }}
                      <span class="separator">|</span>
                      <!-- 文章分类 -->
                      <router-link :to="'/categories/' + item.categoryId">
                        <v-icon size="14">mdi-inbox-full</v-icon>
                        {{ item.categoryName }}
                      </router-link>
                      <span class="separator">|</span>
                      <!-- 文章标签 -->
                      <router-link style="display:inline-block" :to="'/tags/' + tag.id" class="mr-1" v-for="tag of item.tagVOList" :key="tag.id">
                        <v-icon size="14">mdi-tag-multiple</v-icon>
                        {{ tag.name }}
                      </router-link>
                    </div>
                    <!-- 文章内容 -->
                    <router-link :to="'/articles/' + item.id" class="article-content-overflow">
                      {{ item.content }}
                    </router-link>
                  </div>
                </div>
                <div style="float: right;">
                  <el-button type="success" @click="handleUpdate(item.id)">编辑</el-button>
                  <router-link :to="'/articles/' + item.id">
                    <el-button type="primary">阅读</el-button>
                  </router-link>
                </div>
              </v-card>
            </v-col>
          </v-row>
        </div>
        <v-col>
          <div style="text-align: center;margin-top: 20px">
            <el-pagination background @current-change="handleCurrentChange" :current-page="params.pageNo" :page-size="params.pageSize" layout="total, prev, pager, next, jumper" :total="params.total" />
          </div>
        </v-col>

      </v-col>
    </v-row>

    <!--添加or修改区域-->
    <el-dialog :title="title" :visible.sync="centerDialogVisible" :before-close="closeDialogForm" fullscreen center>
      <el-form :rules="rules" ref="dataForm" :model="article" style="margin-top: 10px;" label-position="left">
        <el-row>
          <el-col :span="14">
            <el-form-item label="文章名称" :label-width="formLabelWidth" prop="title">
              <el-input v-model="article.title" auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item label="文章简介" :label-width="formLabelWidth" prop="summary">
              <el-input auto-complete="off" v-model="article.summary"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6.5">
            <el-form-item label="标签" :label-width="formLabelWidth" prop="tags">
              <el-tag v-for="(item, index) of article.tags" :key="index" style="margin:0 1rem 0 0" :closable="true" @close="removeTag(item)">
                {{ item }}
              </el-tag>
              <!-- 标签选项 -->
              <el-popover placement="bottom-start" width="460" trigger="click" v-if="article.tags && article.tags.length < 3">
                <div class="popover-title">标签</div>
                <!-- 搜索框 -->
                <el-input style="width:100%" v-model="tagName" placeholder="请输入标签名,enter添加自定义标签" @keyup.enter.native="saveTag" />
                <!-- 标签 -->
                <div class="popover-container">
                  <div>添加标签</div>
                  <el-tag v-for="(item, index) of tags" :key="index" style="margin-left: 3px;margin-top: 2px" :class="tagClass(item)" @click="addTag(item)">
                    {{ item.name }}
                  </el-tag>
                </div>
                <el-button type="primary" plain slot="reference" size="small">
                  添加标签
                </el-button>
              </el-popover>
            </el-form-item>
          </el-col>
          <el-col :span="6.5">
            <el-form-item label="分类" :label-width="formLabelWidth" prop="categoryName">
              <el-tag type="success" v-show="article.categoryName" style="margin:0 1rem 0 0" :closable="true" @close="removeCategory">
                {{ article.categoryName }}
              </el-tag>
              <!-- 分类选项 -->
              <el-popover placement="bottom-start" width="460" trigger="click" v-if="!article.categoryName">
                <div class="popover-title">分类</div>
                <!-- 输入框 -->
                <el-input style="width:100%" v-model="categoryName" placeholder="请输入分类名,enter添加自定义分类" @keyup.enter.native="saveCategory" />
                <!-- 分类 -->
                <div class="popover-container">
                  <div>添加分类</div>
                  <el-tag v-for="(item, index) of category" :key="index" style="margin-left: 3px;margin-top: 2px" class="category-item" @click="addCategory(item)">
                    {{ item.name }}
                  </el-tag>
                </div>
                <el-button type="success" plain slot="reference" size="small">
                  添加分类
                </el-button>
              </el-popover>
            </el-form-item>
          </el-col>
          <el-col :span="6.5">
            <el-form-item :label-width="formLabelWidth" label="是否置顶" prop="isStick">
              <el-radio-group v-model="article.isStick" size="small">
                <el-radio v-for="(item,index) in yesOrNoList" :key="index" :label="index" border>{{ item }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="6.5">
            <el-form-item :label-width="formLabelWidth" label="是否发布" prop="isPublish">
              <el-radio-group v-model="article.isPublish" size="small">
                <el-radio v-for="(item,index) in publishList" :key="index" :label="index" border>{{ item }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6.5">
            <el-form-item :label-width="formLabelWidth" label="是否私密" prop="isSecret">
              <el-radio-group v-model="article.isSecret" size="small">
                <el-radio v-for="(item,index) in secretList" :key="index" :label="index" border>{{ item }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="6.5">
            <el-form-item :label-width="formLabelWidth" label="创作类型" prop="isOriginal">
              <el-radio-group v-model="article.isOriginal" size="small">
                <el-radio v-for="(item,index) in isOriginalList" :key="index" :label="index" border>{{ item }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col v-if="article.isOriginal === 0" :span="5">
            <el-form-item :label-width="formLabelWidth" label="原文链接" prop="originalUrl">
              <el-input v-model="article.originalUrl"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6.5">
            <el-form-item :label-width="formLabelWidth" label="关键词">
              <el-input v-model="article.keywords"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6.5">
            <el-form-item :label-width="formLabelWidth" label="备注">
              <el-input v-model="article.remark"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label-width="formLabelWidth" label="内容" prop="contentMd">
              <mavon-editor placeholder="输入文章内容..." style="height: 500px" ref=md v-model="article.contentMd" @imgDel="imgDel" @change="" @imgAdd="imgAdd" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeDialogForm">取 消</el-button>
        <el-button v-if="canAdd || canUpdate" type="primary" @click="submit">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { update, save, info, } from '@/api/articles'
import { upload, delBatchFile } from '@/api/imgUpload'
import { fetchCategory } from '@/api/category'
import { fetchTags } from '@/api/tags'
import { fetchList } from "@/api";
import { getUserInfo } from '@/api/user'
import { hasAuth } from '@/utils/auth'
import { judgeIsLargeWidth } from '@/utils/resize'
export default {
  mounted() {
    if (this.$route.query) {
      this.centerDialogVisible = JSON.parse(this.$route.query.showDialog)
    };
    this.updateFormLabelWidth()
    this.getUserInfo()
    this.getBlogList()
    this.getCategoryList()
    fetchTags({ pageNo: 1, pageSize: 100 }).then(res => {
      this.tags = res.data.records
    })
  },
  data() {
    return {
      articleList: [],
      params: {
        strategy: 'look',
        pageNo: 1,
        pageSize: 9,
        total: 0
      },
      rules: {
        'title': [{ required: true, message: '必填字段', trigger: 'blur' }],
        'summary': [{ required: true, message: '必填字段', trigger: 'blur' }],
        'tags': [{ required: true, message: '必填字段', trigger: 'blur' }],
        'categoryName': [{ required: true, message: '必填字段', trigger: 'blur' }],
        'isSecret': [{ required: true, message: '必填字段', trigger: 'change' }],
        'isStick': [{ required: true, message: '必填字段', trigger: 'change' }],
        'isPublish': [{ required: true, message: '必填字段', trigger: 'change' }],
        'isOriginal': [{ required: true, message: '必填字段', trigger: 'change' }],
        'originalUrl': [{ required: true, message: '必填字段', trigger: 'blur' }],
        'contentMd': [{ required: true, message: '必填字段', trigger: 'change' }]
      },
      secretList: ["公开", "私密"],
      isOriginalList: ["转载", "原创"],
      yesOrNoList: ["否", "是"],
      yesOrNoStyle: ['danger', 'success'],
      publishList: ['下架', '发布'],
      formLabelWidth: "50px",
      uploadPictureHost: process.env.VUE_APP_BASE_API + "/file/upload",
      centerDialogVisible: false,
      visible: false,
      title: "",
      isEditForm: 0,
      categoryName: "",
      tagName: "",
      article: {},
      category: [],
      tags: [],
      loading: [],

      userInfo: {
        avatar: '',
        author: '',
        authorInfo: '',
        blogCount: '',
        blogViews: '',
        categoryCount: ''
      }
    };
  },
  methods: {
    updateFormLabelWidth() {
      // 根据屏幕宽度设置头像尺寸
      if (judgeIsLargeWidth()) {
        this.formLabelWidth = "120px";
      }
    },
    getUserInfo() {
      getUserInfo().then((res) => {
        const user = res.data
        this.userInfo.avatar = user.avatar
        this.userInfo.author = user.nickname
        this.userInfo.authorInfo = user.authorInfo
        this.userInfo.blogCount = user.blogCount || 0
        this.userInfo.blogViews = user.blogViews || 0
        this.userInfo.categoryCount = user.categoryCount || 0
      })
    },
    getBlogList() {
      let md = require("markdown-it")();
      fetchList(this.params).then((res) => {
        this.articleList = []
        if (res.data.records.length) {
          // 去除markdown标签
          res.data.records.forEach((item) => {
            if (item.contentMd != null) {
              item.content = md
                .render(item.contentMd)
                .replace(/<\/?[^>]*>/g, "")
                .replace(/[|]*\n/, "")
                .replace(/&npsp;/gi, "");
            }
            this.articleList.push(item);
          });
        }
        this.params.total = res.data.total
      })
    },
    handleCurrentChange(pageNo) {
      this.params.pageNo = pageNo
      this.getBlogList()
    },
    submit: function () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.article.content = this.$refs.md.d_render
          if (this.isEditForm) {
            update(this.article).then(res => {
              this.$message.success("修改文章成功")
              this.closeDialogForm()
              this.getBlogList()
            }).catch(err => {
              console.log(err)
            })
          } else {
            save(this.article).then(res => {
              this.$message.success("添加文章成功")
              this.closeDialogForm()
              this.getBlogList()
            }).catch(err => {
              console.log(err)
            })
          }
        } else {
          this.$message.error("存在必填字段未填")
        }
      })
    },
    imgAdd: function (pos, $file) {
      var formdata = new FormData();
      formdata.append('multipartFile', $file);
      upload(formdata).then(res => {
        this.$refs.md.$img2Url(pos, res.data);
      }).catch(err => {
        console.log(err)
      })
    },
    imgDel: function (filename) {
      delBatchFile(filename[0].split(this.img)[1])
    },
    getCategoryList: function () {
      fetchCategory({ pageNo: 1, pageSize: 100 }).then(res => {
        this.category = res.data.records
      })
    },
    addCategory: function (item) {
      this.article.categoryName = item.name
    },
    saveCategory: function () {
      if (this.categoryName.trim() !== "") {
        this.article.categoryName = this.categoryName
      }
    },
    removeCategory: function () {
      this.article.categoryName = null
    },
    addTag: function (item) {
      if (this.article.tags.length > 2) {
        this.$message.error("最多添加三个标签,如需继续添加,请先删除一个!")
        return false;
      }
      if (this.article.tags.indexOf(item.name) === -1) {
        this.article.tags.push(item.name);
      }
    },
    saveTag: function () {
      if (this.tagName.trim() !== "") {
        this.addTag({
          name: this.tagName
        });
        this.tagName = "";
      }
    },
    removeTag: function (item) {
      const index = this.article.tags.indexOf(item);
      this.article.tags.splice(index, 1);
    },
    uploadBefore: function () {
      this.openLoading()
    },
    // 打开加载层
    openLoading: function () {
      this.loading = this.$loading({
        lock: true,
        text: "正在加载中~",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
    },
    uploadSectionFile: function (param) {
      this.files = param.file
      // FormData 对象
      var formData = new FormData()
      // 文件对象
      formData.append('multipartFile', this.files)
      upload(formData).then(res => {
        this.article.avatar = res.data
      })
      this.loading.close()
    },
    handleAdd: function () {
      this.fileList = []
      let article = sessionStorage.getItem("article");
      if (article != null) {
        this.$confirm('还有上次未完成的博客编辑，是否继续编辑?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.initModel(JSON.parse(article))
        }).catch(() => {
          this.initModel(this.getFormObject())
          sessionStorage.removeItem("article")
        })
      } else {
        this.initModel(this.getFormObject())
      }
    },
    initModel: function (article) {
      this.article = article
      if (this.article.id != null) {
        this.beforeShow('修改文章', 1)
      } else {
        this.beforeShow('新增文章', 0)
      }
    },
    getFormObject: function () {
      return {
        id: null,
        title: '',
        avatar: '',
        summary: '',
        quantity: 0,
        content: '',
        contentMd: '',
        isSecret: 0,
        isStick: 0,
        isOriginal: 1,
        remark: '',
        keywords: '',
        categoryName: null,
        isPublish: 1,
        tags: []
      };
    },
    beforeShow: function (title, isEditForm) {
      this.title = title
      this.isEditForm = isEditForm
      this.centerDialogVisible = true
    },
    handleUpdate: function (blogId) {
      info(blogId).then(res => {
        this.article = res.data
        this.beforeShow("修改文章", 1)
      })
    },
    tagClass() {
      return function (item) {
        const index = this.article.tags.indexOf(item.name);
        return index !== -1 ? "tag-item-select" : "tag-item";
      };
    },
    closeDialogForm() {
      this.centerDialogVisible = false
      if (JSON.parse(this.$route.query.showDialog)) {
        this.$router.replace({ path: '/blog', query: { showDialog: false } });
      }
    }
  },
  computed: {
    canAdd: function () {
      return hasAuth(this.pres, '/system/article/add')
    },
    canUpdate: function () {
      return hasAuth(this.pres, '/system/article/update')
    },
    background() {
      return "background: url(' https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg ') center center / cover no-repeat";
    },
  },
};
</script>

<style lang="stylus"></style>

<style scoped lang="scss">
.blog-banner {
  position: absolute;
  top: -60px;
  left: 0;
  right: 0;
  height: 60px;
}

.blog-container {
  opacity: 0.8;
}

.blog-sider {
  height: 100%;
}

.blog-sider-function {
  height: 500px;
  padding: 150px 50px;
  text-align: center;
}

.blog-wrapper {
  margin-top: 15px;
  height: 400px;
}

.article-list {
  border: 2px solid white;
  box-shadow: 0 0 5px white;
}

@media (min-width: 760px) {
  .blog-container {
    animation: header-effect 1s;
    max-width: 2000px;
    margin: 0 auto 0 auto;
    padding: 0 5px;
  }

  .article-card {
    height: 230px;
    width: 32.83%;
    float: left;
    margin: 5px 0.25%;
  }

  .article-card-height {
    height: 180px;
  }

  .article-cover {
    overflow: hidden;
    height: 100%;
    width: 45%;
  }

  .on-hover {
    transition: all 0.6s;
  }

  .article-wrapper {
    padding: 0 1rem;
  }

  .article-wrapper a {
    font-size: 1rem;
    transition: all 0.3s;
  }

  .article-list {
    height: 740px;
  }
}

@media (max-width: 759px) {
  .blog-container {
    width: 100%;
    margin: 0 auto 0 auto;
    padding: 0 5px;
  }

  .home-container {
    min-height: 600px;
  }

  .article-card {
    height: 250px;
    margin-top: 1rem;
  }

  .article-card-height {
    height: 200px;
  }

  .article-cover {
    border-radius: 8px 8px 0 0 !important;
    height: 230px !important;
    width: 100%;
  }

  .article-cover div {
    border-radius: 8px 8px 0 0 !important;
  }

  .article-wrapper {
    padding: 1.25rem 1.25rem 1.875rem;
  }

  .article-wrapper a {
    font-size: 0.9rem;
    transition: all 0.3s;
  }
}

.article-wrapper a:hover {
  color: #8e8cd8;
}

.article-info {
  font-size: 95%;
  color: #858585;
  line-height: 2;
  margin: 1rem 0;
}

.article-info a {
  font-size: 95%;
  color: #858585 !important;
}

.blog-card {
  line-height: 2;
  padding: 1.25rem 1.5rem;

  .web-info-title {
    svg {
      vertical-align: middle;
      margin-bottom: 3px;
    }
  }
}

.author-wrapper {
  // text-align: center;
  position: absolute;
  bottom: 50px;
  left: 50px;
}

.blog-info-wrapper {
  display: flex;
  // justify-self: center;
  // padding: 0.875rem 0;
}

.blog-info-data {
  flex: 1;
  text-align: center;
}

.blog-info-data a {
  text-decoration: none;
}

.collection-btn {
  text-align: center;
  z-index: 1;
  font-size: 14px;
  position: relative;
  display: block;
  background-color: #49b1f5;
  color: #fff !important;
  height: 32px;
  line-height: 32px;
  transition-duration: 1s;
  transition-property: color;
}

.collection-btn:before {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  z-index: -1;
  background: #ff7242;
  content: "";
  transition-timing-function: ease-out;
  transition-duration: 0.5s;
  transition-property: transform;
  transform: scaleX(0);
  transform-origin: 0 50%;
}

.collection-btn:hover:before {
  transition-timing-function: cubic-bezier(0.45, 1.64, 0.47, 0.66);
  transform: scaleX(1);
}

.author-avatar {
  transition: all 0.5s;
}

.author-avatar:hover {
  transform: rotate(360deg);
}

.scroll-down-effects {
  color: #eee !important;
  text-align: center;
  text-shadow: 0.1rem 0.1rem 0.2rem rgba(0, 0, 0, 0.15);
  line-height: 1.5;
  display: inline-block;
  text-rendering: auto;
  -webkit-font-smoothing: antialiased;
  animation: scroll-down-effect 1.5s infinite;
}

@keyframes scroll-down-effect {
  0% {
    top: 0;
    opacity: 0.4;
    filter: alpha(opacity=40);
  }

  50% {
    top: -16px;
    opacity: 1;
    filter: none;
  }

  100% {
    top: 0;
    opacity: 0.4;
    filter: alpha(opacity=40);
  }
}

.big i {
  color: #f00;
  animation: big 0.8s linear infinite;
}

@keyframes big {
  0%,
  100% {
    transform: scale(1);
  }

  50% {
    transform: scale(1.2);
  }
}
</style>
