import { createContext, useContext, useEffect, useState } from 'react'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser] = useState(undefined)
  const [redirecting, setRedirecting] = useState(false) // evita múltiplos redirects

  useEffect(() => {
    fetch('/auth/me', { credentials: 'include' })
      .then(res => res.ok ? res.json() : null)
      .then(data => {
        setUser(data)

        const noCadastro = globalThis.location.pathname.includes('/cadastro')

        if (data && noCadastro) {
          globalThis.location.href = '/'
          return
        }

        if (!data && !redirecting && !noCadastro) {
          setRedirecting(true)
          globalThis.location.href = '/oauth2/iniciar-login'
        }
      })
      .catch(() => {
        setUser(null)
        if (!redirecting && !globalThis.location.pathname.includes('/cadastro')) {
          setRedirecting(true)
          globalThis.location.href = '/oauth2/iniciar-login'
        }
      })
  }, [])

  const logout = async () => {
    await fetch('/auth/logout', { method: 'POST', credentials: 'include' })
    setUser(null)
    window.location.href = '/'
  }

  return (
    <AuthContext.Provider value={{ user, logout, isLoading: user === undefined }}>
      {children}
    </AuthContext.Provider>
  )
}

export const useAuth = () => useContext(AuthContext)