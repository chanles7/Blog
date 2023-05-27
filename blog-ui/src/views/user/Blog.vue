<template>
  <div>


    <v-row class="blog-container">


    </v-row>


    <!-- 主页文章 -->
    <v-row class="home-container">
      <!-- 博主信息 -->
      <v-col :md="12" :cols="12" class="d-md-block d-none">
        <div class="blog-wrapper">
          <v-card class="animated zoomIn blog-card mt-5">
            <div class="author-wrapper">
              <!-- 博主头像 -->
              <v-avatar size="110">
                <img class="author-avatar" :src="blogInfo.webSite.authorAvatar" />
              </v-avatar>
              <div style="font-size: 1.375rem">
                {{ blogInfo.webSite.author }}
              </div>
              <div style="font-size: 0.875rem;">
                {{ blogInfo.webSite.authorInfo }}
              </div>
            </div>
            <!-- 博客信息 -->
            <div class="blog-info-wrapper">
              <div class="blog-info-data">
                <router-link to="/archives">
                  <div style="font-size: 0.875rem">文章</div>
                  <div style="font-size: 1.25rem">
                    {{ blogInfo.count.articleCount }}
                  </div>
                </router-link>
              </div>
              <div class="blog-info-data">
                <router-link to="/categories">
                  <div style="font-size: 0.875rem">分类</div>
                  <div style="font-size: 1.25rem">
                    {{ blogInfo.count.categoryCount }}
                  </div>
                </router-link>
              </div>
              <div class="blog-info-data">
                <router-link to="/tags">
                  <div style="font-size: 0.875rem">标签</div>
                  <div style="font-size: 1.25rem"> {{ blogInfo.count.tagCount }}</div>
                </router-link>
              </div>
            </div>
          </v-card>
        </div>
      </v-col>

      <v-col :md="12" :cols="12">
        <v-card class="animated zoomIn article-card" style="border-radius: 12px 8px 8px 12px" v-for="(item, index) of articleList" :key="item.id">
          <!-- 文章信息 -->
          <div class="article-wrapper">
            <div style="line-height:1.">
              <div>
                <router-link :to="'/articles/' + item.id">
                  {{ item.title }}
                </router-link>
                <span v-if="item.quantity >= 1000 && item.quantity < 10000" style="font-size:12px;border-radius:3px;border: 1px solid  #f70;text-align: center">
                  <font style="color: #f70;">千次阅读</font>
                </span>
                <span v-if="item.quantity >= 10000" style="font-size:12px;border-radius:3px;border: 1px solid  #f70;text-align: center">
                  <font style="color: #f70;">万次阅读</font>
                </span>
              </div>
            </div>
            <div class="article-info">
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
              <router-link style="display:inline-block" :to="'/tags/' + tag.id" class="mr-1" v-for="tag of item.tagDTOList" :key="tag.id">
                <v-icon size="14">mdi-tag-multiple</v-icon>
                {{ tag.name }}
              </router-link>
            </div>
            <!-- 文章内容 -->
            <div class="article-content">
              {{ item.content }}
            </div>
          </div>
        </v-card>
      </v-col>

      <v-col>
        <div style="text-align: center;margin-top: 20px">
          <el-pagination background @current-change="handleCurrentChange" :current-page="params.pageNo" :page-size="params.pageSize" layout="total, prev, pager, next, jumper" :total="params.total" />
        </div>
      </v-col>
    </v-row>
  </div>
</template>

<script>
import { fetchList } from "@/api";

export default {
  mounted: function () {
    let script = document.createElement("script");
    script.type = "text/javascript";
    script.src =
      "https://widget.qweather.net/standard/static/js/he-standard-common.js?v=2.0";
    document.getElementsByTagName("head")[0].appendChild(script);

    this.getBlogList()
  },
  data: function () {
    return {
      articleList: [],
      params: {
        pageNo: 1,
        pageSize: 9,
        total: 0
      },
    };
  },

  methods: {
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
    }
  },
  computed: {
    blogInfo() {
      return this.$store.state.blogInfo;
    },
  },
};
</script>

<style lang="stylus">
.typed-cursor {
  opacity: 1;
  animation: blink 0.7s infinite;
}

@keyframes blink {
  0% {
    opacity: 1;
  }

  50% {
    opacity: 0;
  }

  100% {
    opacity: 1;
  }
}
</style>

<style scoped lang="scss">
.article-wrapper {
  font-size: 14px;
}

@media (min-width: 760px) {
  .blog-title {
    font-size: 2.5rem;
  }

  .blog-intro {
    font-size: 1.5rem;
  }

  .blog-contact {
    display: none;
  }

  .home-container {
    max-width: 1200px;
    margin: calc(5vh - 48px) auto 28px auto;
    padding: 0 5px;
  }

  .article-card {
    // display: inline-block;
    align-items: center;
    height: 250px;
    width: 32%;
    margin-top: 20px;
    margin-left: 15px;
    float: left;
  }

  .article-cover {
    overflow: hidden;
    height: 100%;
    width: 45%;
  }

  .on-hover {
    transition: all 0.6s;
  }

  .article-card:hover .on-hover {
    transform: scale(1.1);
  }

  .article-wrapper {
    padding: 2.5rem 2.5rem;
  }

  .article-wrapper a {
    font-size: 1.5rem;
    transition: all 0.3s;
  }
}

@media (max-width: 759px) {
  .blog-title {
    font-size: 26px;
  }

  .blog-contact {
    font-size: 1.25rem;
    line-height: 2;
  }

  .home-container {
    width: 100%;
    margin: calc(100vh - 66px) auto 0 auto;
  }

  .article-card {
    margin-top: 1rem;
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
    font-size: 1.25rem;
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

.article-content {
  line-height: 2;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.blog-wrapper {
  position: sticky;
  top: 10px;
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
  text-align: center;
}

.blog-info-wrapper {
  display: flex;
  justify-self: center;
  padding: 0.875rem 0;
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
