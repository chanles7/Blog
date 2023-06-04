export function hasAuth() {
  return true
}

export const setToken = (token) => localStorage.setItem('TOKEN', token)

export const getToken = () => localStorage.getItem('TOKEN')

export const removeToken = () => localStorage.removeItem('TOKEN')

export const hasToken = () => {
  var token = localStorage.getItem('TOKEN')
  if (token) return true
  return false
}
