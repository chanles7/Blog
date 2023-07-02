import router from './router'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth'
import { tokenRecharge } from '@/api/user'
NProgress.configure({ showSpinner: false }) // NProgress Configuration

const checkList = ['/blog'] // no redirect whitelist

router.beforeEach(async (to, from, next) => {
  if (to.path) {
    if (window._hmt) {
      window._hmt.push(['_trackPageview', to.fullPath])
    }
  }
  NProgress.start()
  if (checkList.indexOf(to.path) == -1) {
    next()
  } else {
    const hasToken = getToken()
    if (hasToken) {
      tokenRecharge()
        .then((res) => {})
        .catch((err) => {})
      next()
    } else {
      next(`/home`)
    }
  }

  NProgress.done()
})

router.afterEach(() => {
  window.scrollTo({
    top: 0,
    behavior: 'instant'
  })
  NProgress.done()
})
