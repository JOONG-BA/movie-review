import { createContext, useEffect, useState } from "react"

export const AuthContext = createContext()

export const AuthProvider = ({ children }) => {
  const [isLoggedIn, setIsLoggedIn] = useState(false)
  const [user, setUser] = useState(null)

  useEffect(() => {
    const token = localStorage.getItem("token")
    if (token) {
      setIsLoggedIn(true)
      // 나중에 사용자 정보도 불러오면 여기서 setUser() 하면 됨
    } else {
      setIsLoggedIn(false)
      setUser(null)
    }
  }, [])

  const logout = () => {
    localStorage.removeItem("token")
    setIsLoggedIn(false)
    setUser(null)
  }

  return (
      <AuthContext.Provider value={{ isLoggedIn, setIsLoggedIn, user, setUser, logout }}>
        {children}
      </AuthContext.Provider>
  )
}

export async function authFetch(url, options = {}) {
  const token = localStorage.getItem("token");

  const headers = {
    ...options.headers,
    Authorization: `Bearer ${token}`,
    "Content-Type": "application/json",
  };

  return fetch(url, {
    ...options,
    headers,
  });
}