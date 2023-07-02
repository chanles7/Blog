<template>
  <v-navigation-drawer app v-model="drawer" width="250" disable-resize-watcher right overlay-opacity="0.8">
    <!-- 博主介绍 -->
    <div class="blogger-info" v-if="hasToken">
      <v-avatar size="110" style="margin-bottom:0.5rem">
        <img :src="userInfo.avatar" />
      </v-avatar>
    </div>
    <!-- 博客信息 -->
    <div class="blog-info-wrapper" v-if="hasToken">
      <div class="blog-info-data">
        <router-link to="/archives">
          <div style="font-size: 0.875rem">文章</div>
          <div style="font-size: 1.125rem;">
            {{ userInfo.blogCount }}
          </div>
        </router-link>
      </div>
      <div class="blog-info-data">
        <router-link to="/categories">
          <div style="font-size: 0.875rem">分类</div>
          <div style="font-size: 1.125rem">
            {{ userInfo.categoryCount }}
          </div>
        </router-link>
      </div>
      <div class="blog-info-data">
        <router-link to="/tags">
          <div style="font-size: 0.875rem">访问量</div>
          <div style="font-size: 1.125rem">
            {{ userInfo.blogViews }}
          </div>
        </router-link>
      </div>
    </div>
    <hr />
    <!-- 页面导航 -->
    <div class="menu-container">
      <div class="menus-item">
        <router-link to="/">
          <i class="iconfont iconzhuye" /> 首页
        </router-link>
      </div>
      <div class="menus-item">
        <router-link to="/categories">
          <i class="iconfont iconfenlei" /> 分类
        </router-link>
      </div>
      <div class="menus-item">
        <router-link to="/links">
          <i class="iconfont iconlianjie" /> 友链
        </router-link>
      </div>
      <div class="menus-item">
        <router-link to="/about">
          <i class="iconfont iconzhifeiji" /> 关于开发者
        </router-link>
      </div>
      <div class="menus-item">
        <router-link to="/message">
          <i class="iconfont iconpinglunzu" /> 留言
        </router-link>
      </div>
      <div class="menus-item" v-if="hasToken && !judgeIsLargeWidth">
        <button class="menu-btn" @click="toBlog(true)">
          <i class="iconfont iconpinglunzu" /> 新博客
        </button>
      </div>
      <div class="menus-item" v-if="hasToken">
        <button class="menu-btn" @click="toBlog(false)">
          <i class="iconfont iconpinglunzu" /> 我的博客
        </button>
      </div>
      <div class="menus-item" v-if="!this.$store.state.avatar">
        <a @click="openLogin"><i class="iconfont icondenglu" /> 登录 </a>
      </div>
      <div v-else>
        <div class="menus-item">
          <router-link to="/user">
            <i class="iconfont icongerenzhongxin" /> 个人中心
          </router-link>
        </div>
        <div class="menus-item">
          <a @click="logout"><i class="iconfont icontuichu" /> 退出</a>
        </div>
      </div>
    </div>
  </v-navigation-drawer>
</template>

<script>
import { logout } from "@/api";
import { hasToken, removeToken } from "@/utils/auth"
import { getUserInfo } from '@/api/user'
import { judgeIsLargeWidth } from '@/utils/resize'
export default {
  mounted() {
    window.addEventListener("scroll", this.scroll);
    this.hasToken = hasToken()
    if (this.hasToken) {
      this.getUserInfo()
    }
  },
  data() {
    return {
      img: process.env.VUE_APP_IMG_API,
      hasToken: true,
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
  computed: {
    drawer: {
      set(value) {
        this.$store.state.drawer = value;
      },
      get() {
        return this.$store.state.drawer;
      },
    },
    isLogin() {
      return this.$store.state.userId;
    },
    judgeIsLargeWidth() {
      return judgeIsLargeWidth()
    }
  },
  methods: {
    toBlog(showDialog) {
      if (this.$route.path == '/blog') {
        const queryShowDialog = JSON.parse(this.$route.query.showDialog)
        if (queryShowDialog != showDialog) {
          this.$router.replace({ path: '/blog', query: { showDialog } });
        }
      } else {
        this.$router.push({ path: '/blog', query: { showDialog } });
      }
    },
    scroll() {
      const that = this;
      that.scrollTop = window.pageYOffset ||
        document.documentElement.scrollTop ||
        document.body.scrollTop;
      if (that.scrollTop > 60) {
        that.navClass = "nav-fixed";
      } else {
        that.navClass = "nav";
      }
    },
    openLogin() {
      this.$store.state.loginFlag = true;
    },
    logout() {
      //如果在个人中心则跳回上一页
      if (this.$route.path === "/user") {
        this.$router.go(-1);
      }
      logout().then(res => {
        this.$store.commit("logout");
        this.$message.success("注销成功");
        this.hasToken = false
        removeToken()
        this.$router.go(0);
      }).catch(err => {
        this.$message.error(err.message);
      });
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
  },
}
</script>

<style scoped>
.blogger-info {
  padding: 26px 30px 0;
  text-align: center;
}
.blog-info-wrapper {
  display: flex;
  align-items: center;
  padding: 12px 10px 0;
}
.blog-info-data {
  flex: 1;
  line-height: 2;
  text-align: center;
}
hr {
  border: 2px dashed #d2ebfd;
  margin: 20px 0;
}
.menu-container {
  padding: 0 10px 40px;
  animation: 0.8s ease 0s 1 normal none running sidebarItem;
}
.menus-item a,
button {
  padding: 6px 30px;
  display: block;
  line-height: 2;
}
.menus-item i {
  margin-right: 2rem;
}
@keyframes sidebarItem {
  0% {
    transform: translateX(200px);
  }
  100% {
    transform: translateX(0);
  }
}
</style>
