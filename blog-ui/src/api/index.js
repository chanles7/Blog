import request from '@/utils/request'

export function fetchList(params) {
  return request({
    url: '/web/article/list',
    method: 'get',
    params: params
  })
}
export function getArticleInfo(id) {
  return request({
    url: '/web/article/info',
    method: 'get',
    params: {
      id: id
    }
  })
}
export function searchArticle(keywords) {
  return request({
    url: '/web/article/searchArticle',
    method: 'get',
    params: {
      keywords: keywords
    }
  })
}
//分类标签id获取文章
export function queryIdToArticles(params) {
  return request({
    url: '/web/article/condition',
    method: 'get',
    params: params
  })
}
export function like(id) {
  return request({
    url: '/web/article/articleLike',
    method: 'get',
    params: { articleId: id }
  })
}
export function checkSecret(params) {
  return request({
    url: '/web/article/checkSecret',
    method: 'get',
    params: params
  })
}
//友链页面请求接口
export function addLink(data) {
  return request({
    url: '/web/friend/add',
    method: 'POST',
    data
  })
}
export function fetchFriend() {
  return request({
    url: '/web/friend/list',
    method: 'post',
    params: {}
  })
}
export function getWebSiteInfo() {
  return request({
    url: '/web/home/webSiteInfo',
    method: 'get',
    params: {}
  })
}

//留言接口
export function addMessage(data) {
  return request({
    url: '/web/message/add',
    method: 'post',
    data
  })
}

export function listMessage() {
  return request({
    url: '/web/message/webMessage',
    method: 'get',
    params: {}
  })
}
//添加评论
export function addComment(data) {
  return request({
    url: '/web/comment/addComment',
    method: 'post',
    data
  })
}
//评论列表
export function fetchComments(params) {
  return request({
    url: '/web/comment/comments',
    method: 'get',
    params: params
  })
}
//查询回复评论
export function repliesByComId(params) {
  return request({
    url: '/web/comment/repliesByComId',
    method: 'get',
    params: params
  })
}

//标签接口
export function getTags() {
  return request({
    url: '/web/tags/list',
    method: 'get',
    params: {}
  })
}
//分类接口
export function getCategory() {
  return request({
    url: '/web/tags/categoryList',
    method: 'get',
    params: {}
  })
}
//归档接口
export function getArchive(params) {
  return request({
    url: '/web/article/archive',
    method: 'get',
    params: params
  })
}
//qq退出
export function logout() {
  return request({
    url: '/logout',
    method: 'get'
  })
}
//添加访客信息
export function report() {
  return request({
    url: '/web/home/report',
    method: 'get',
    params: {}
  })
}
//相册接口
export function getAlbum() {
  return request({
    url: '/web/album/list',
    method: 'get',
    params: {}
  })
}
export function getPhotos(params) {
  return request({
    url: '/web/album/listPhotos',
    method: 'get',
    params: params
  })
}
export function addFeedback(data) {
  return request({
    url: '/web/feedback/add',
    method: 'post',
    data
  })
}

export function updatePassword(data) {
  return request({
    url: '/user/updatePassword',
    method: 'post',
    data
  })
}
export function upload(data) {
  return request({
    url: '/file/upload',
    method: 'POST',
    headers: { 'Content-Type': 'multipart/articles-data' },
    data
  })
}
export function updateUser(data) {
  return request({
    url: '/user/updateUser',
    method: 'post',
    data
  })
}
