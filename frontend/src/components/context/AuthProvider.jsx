import { createContext, useContext, useEffect, useState } from 'react'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser] = useState(undefined)
  const [redirecting, setRedirecting] = useState(false)

  const redirectToLogin = () => {
    if (!globalThis.location.pathname.includes('/cadastro')) {
      setRedirecting(true)
      globalThis.location.href = '/oauth2/iniciar-login'
    }
  }

  // Lê as authorities atuais da sessão Redis via /auth/me
  const loadUser = async () => {
    const res = await fetch('/auth/me', { credentials: 'include' })
    if (!res.ok) return null
    return res.json()
  }

  useEffect(() => {
    let cancelled = false

    fetch('/auth/me', { credentials: 'include' })
      .then(res => res.ok ? res.json() : null)
      .then(data => {
        if (cancelled) return
        setUser(data)

        const noCadastro = globalThis.location.pathname.includes('/cadastro')
        if (data && noCadastro) { globalThis.location.href = '/'; return }
        if (!data && !noCadastro) redirectToLogin()
      })
      .catch(() => {
        if (cancelled) return
        setUser(null)
        redirectToLogin()
      })

    return () => { cancelled = true }
  }, [])

  // Chame após qualquer ação que promova o usuário no backend
  // (ex: cadastrar restaurante → grupo "Dono de Restaurante" atribuído).
  // Fluxo: POST /auth/refresh → Authorization Server emite novo JWT com authorities atualizadas
  //        → sessão Redis é atualizada → GET /auth/me devolve o novo payload
  const refreshUser = async () => {
    try {
      const refreshRes = await fetch('/auth/refresh', {
        method: 'POST',
        credentials: 'include',
      })

      // Se o refresh_token também expirou, manda para o login
      if (refreshRes.status === 401) {
        redirectToLogin()
        return
      }

      if (!refreshRes.ok) {
        console.error('Falha ao renovar sessão — status', refreshRes.status)
        return
      }

      // Sessão atualizada no Redis — agora /auth/me retorna as authorities novas
      const data = await loadUser()
      setUser(data)
    } catch (err) {
      console.error('Erro no refreshUser', err)
    }
  }

  // Intercepta respostas 401 globalmente para renovar a sessão automaticamente.
  // Cobre o caso de o access_token na sessão expirar enquanto o usuário está navegando.
  useEffect(() => {
    const originalFetch = window.fetch

    window.fetch = async (...args) => {
      const response = await originalFetch(...args)

      const url = typeof args[0] === 'string' ? args[0] : args[0]?.url ?? ''
      const isAuthEndpoint = url.includes('/auth/me') ||
        url.includes('/auth/refresh') ||
        url.includes('/auth/logout') ||
        url.includes('/oauth2/')

      if (response.status === 401 && !isAuthEndpoint) {
        // Tenta renovar silenciosamente
        const refreshRes = await originalFetch('/auth/refresh', {
          method: 'POST',
          credentials: 'include',
        })

        if (refreshRes.ok) {
          // Sessão renovada — repete a requisição original
          return originalFetch(...args)
        } else {
          // Refresh também falhou — sessão morta, manda para login
          redirectToLogin()
        }
      }

      return response
    }

    return () => { window.fetch = originalFetch }
  }, [])

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