<template>
  <v-dialog v-model="registerFlag" :fullscreen="isMobile" max-width="460">
    <v-card class="login-container" style="border-radius:4px;height: 450px">
      <v-icon class="float-right" @click="registerFlag = false">
        mdi-close
      </v-icon>
      <div class="login-wrapper">
        <!-- 用户名 -->
        <v-text-field v-model="username" label="昵称" placeholder="请输入您的用户名" clearable @keyup.enter="register" />
        <!-- 密码 -->
        <v-text-field v-model="password" class="mt-7" label="密码" placeholder="请输入您的密码" @keyup.enter="register" :append-icon="show ? 'mdi-eye' : 'mdi-eye-off'" :type="show ? 'text' : 'password'" @click:append="show = !show" />
        <!-- 注册按钮 -->
        <v-btn class="mt-7" block color="red" style="color:#fff" @click="register">
          注册
        </v-btn>
        <!-- 登录 -->
        <div class="mt-10 login-tip">
          已有账号？<span @click="openLogin">登录</span>
        </div>
      </div>
    </v-card>
  </v-dialog>
</template>

<script>
import { simpleRegister } from "@/api/user.js";
export default {
  data: function () {
    return {
      username: "",
      password: "",
      flag: true,
      codeMsg: "发送",
      time: 60,
      show: false
    };
  },
  methods: {
    openLogin() {
      this.$store.state.registerFlag = false;
      this.$store.state.loginFlag = true;
    },
    sendCode() {
      //发送邮件
      this.countDown();
      sendEmailCode(this.username).then(res => {
        this.$toast({ type: "success", message: res.message });
      }).catch(err => {
        this.$toast({ type: "error", message: err.message });
      });
    },
    countDown() {
      this.flag = true;
      this.timer = setInterval(() => {
        this.time--;
        this.codeMsg = this.time + "s";
        if (this.time <= 0) {
          clearInterval(this.timer);
          this.codeMsg = "发送";
          this.time = 60;
          this.flag = false;
        }
      }, 1000);
    },
    register() {
      var reg = /^[a-zA-Z]{4,10}$/;
      if (!reg.test(this.username)) {
        this.$toast({ type: "error", message: "用户名需为4-10位字母" });
        return false;
      }
      if (this.password.trim().length < 6) {
        this.$toast({ type: "error", message: "密码不能少于6位" });
        return false;
      }
      const user = {
        username: this.username,
        password: this.password,
      };
      simpleRegister(user).then(res => {
        this.$message.success(res.data);
      }).catch(err => {
        this.$message.error(err.message);
      });
    }
  },
  computed: {
    registerFlag: {
      set(value) {
        this.$store.state.registerFlag = value;
      },
      get() {
        return this.$store.state.registerFlag;
      }
    },
    isMobile() {
      const clientWidth = document.documentElement.clientWidth;
      if (clientWidth > 960) {
        return false;
      }
      return true;
    }
  },
  watch: {
    username(value) {
      var reg = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
      if (reg.test(value)) {
        this.flag = false;
      } else {
        this.flag = true;
      }
    }
  }
};
</script>
