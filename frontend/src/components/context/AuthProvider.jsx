import { createContext, useContext, useEffect, useState } from 'react'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser] = useState(undefined)
  const [redirecting, setRedirecting] = useState(false)

  useEffect(() => {
    let cancelled = false

    fetch('/auth/me', { credentials: 'include' })
      .then(res => res.ok ? res.json() : null)
      .then(data => {
        if (cancelled) return
        setUser(data)

        const noCadastro = globalThis.location.pathname.includes('/cadastro')

        if (data && noCadastro) {
          globalThis.location.href = '/'
          return
        }

        if (!data && !noCadastro) {
          setRedirecting(true)
          globalThis.location.href = '/oauth2/iniciar-login'
        }
      })
      .catch(() => {
        if (cancelled) return
        setUser(null)
        if (!globalThis.location.pathname.includes('/cadastro')) {
          setRedirecting(true)
          globalThis.location.href = '/oauth2/iniciar-login'
        }
      })

    return () => { cancelled = true }
  }, [])

  // Chame isso após qualquer ação que mude as authorities do usuário no backend
  // (ex: cadastrar restaurante → usuário é promovido para "Dono de Restaurante").
  // O /auth/me relê o token da sessão atual e retorna as authorities atualizadas.
  const refreshUser = async () => {
    try {
      const res = await fetch('/auth/me', { credentials: 'include' })
      const data = res.ok ? await res.json() : null
      setUser(data)
    } catch {
      setUser(null)
    }
  }

  const logout = async () => {
    await fetch('/auth/logout', { method: 'POST', credentials: 'include' })
    setUser(null)
    globalThis.location.href = '/'
  }

  return (
    <AuthContext.Provider value={{ user, logout, refreshUser, isLoading: user === undefined }}>
      {children}
    </AuthContext.Provider>
  )
}

export const useAuth = () => useContext(AuthContext)