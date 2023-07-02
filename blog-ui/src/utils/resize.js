export const judgeIsLargeWidth = () => {
  const screenWidth = window.innerWidth
  if (screenWidth >= 768) {
    return true
  } else {
    return false
  }
}
