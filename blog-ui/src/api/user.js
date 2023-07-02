import request from '@/utils/request'

/**
 *
 * @param {用户相关请求}
 * @returns
 */
/**
 * 简单注册
 */
export const simpleRegister = (data) => request.post('/user/register', data)

/**
 * 简单登录
 */
export const simpleLogin = (data) => request.post('/user/login', data)

/**
 * 获取用户信息
 */
export const getUserInfo = () => request.get('/user/info')

/**
 * 获取作者信息
 */
export const getAuthorInfo = () => request.get('/user/author')
/**
 * 续费token
 */
export const tokenRecharge = () => request.post('/user/token')
/**
 * 更新用户信息
 */
export const updateUserInfo = (data) => request.post('/user/update', data)
