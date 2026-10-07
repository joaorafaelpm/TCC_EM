import { useEffect, useRef } from 'react'
import './SearchOverlay.css'
import { useSearch } from '../../context/SearchContext'
import SearchResults from '../../SearchResults/SearchResults'

const SearchOverlay = () => {
  const { query, setQuery, isOpen, closeSearch } = useSearch()
  const inputRef = useRef(null)
  const mouseDownTarget = useRef(null)

  useEffect(() => {
    if (isOpen) setTimeout(() => inputRef.current?.focus(), 50)
  }, [isOpen])

  useEffect(() => {
    const handleKey = (e) => { if (e.key === 'Escape') closeSearch() }
    window.addEventListener('keydown', handleKey)
    return () => window.removeEventListener('keydown', handleKey)
  }, [])

  if (!isOpen) return null

  const handleMouseDown = (e) => {
    // Guarda onde o clique começou
    mouseDownTarget.current = e.target
  }

  const handleMouseUp = (e) => {
    // Só fecha se o mousedown E o mouseup foram no backdrop (mesmo elemento)
    // Isso ignora drags que começaram dentro do painel e terminaram fora
    if (
      mouseDownTarget.current === e.currentTarget &&
      e.target === e.currentTarget
    ) {
      closeSearch()
    }
    mouseDownTarget.current = null
  }

  return (
    <div
      className="search-overlay"
      onMouseDown={handleMouseDown}
      onMouseUp={handleMouseUp}
    >
      <div className="search-overlay-panel" onMouseDown={e => e.stopPropagation()}>

        <div className="search-overlay-box">
          <svg className="search-icon" xmlns="http://www.w3.org/2000/svg" width="18" height="18"
            viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"
            strokeLinecap="round" strokeLinejoin="round">
            <circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/>
          </svg>
          <input
            ref={inputRef}
            className="search-overlay-input"
            type="text"
            placeholder="Buscar restaurantes ou produtos..."
            value={query}
            onChange={e => setQuery(e.target.value)}
          />
          {query && (
            <button className="search-clear" onClick={() => setQuery('')}>✕</button>
          )}
          <button className="search-overlay-close" onClick={closeSearch}>Fechar</button>
        </div>

        {query.trim() && (
          <div className="search-overlay-results">
            <SearchResults />
          </div>
        )}

      </div>
    </div>
  )
}

export default SearchOverlay