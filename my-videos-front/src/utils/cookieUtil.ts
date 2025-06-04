export const setCookie = (name: string, value: string, now: Date) => {
  const expires = new Date(now.getTime() + 1000 * 60 * 60 * 3) // 3시간 뒤
  document.cookie = `${name}=${value};expires=${expires.toUTCString()};path=/`
}

export const getCookie = (name: string): string | null => {
  const cookieArr = document.cookie.split('; ')
  for (const cookie of cookieArr) {
    const [key, val] = cookie.split('=')
    if (key === name) {
      return val
    }
  }
  return null
}

export const deleteCookie = (name: string) => {
  document.cookie = `${name}=;expires=Thu, 01 Jan 1970 00:00:01 GMT;path=/`
}
