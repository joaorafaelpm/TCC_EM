import { createContext, useContext, useState } from 'react'

const SearchContext = createContext(null)

export function SearchProvider({ children }) {
  const [query, setQuery] = useState('')
  const [isOpen, setIsOpen] = useState(false)

  const openSearch = () => setIsOpen(true)
  const closeSearch = () => { setIsOpen(false); setQuery('') }

  return (
    <SearchContext.Provider value={{ query, setQuery, isOpen, openSearch, closeSearch }}>
      {children}
    </SearchContext.Provider>
  )
}

export const useSearch = () => useContext(SearchContext)