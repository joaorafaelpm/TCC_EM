import { useEffect, useState } from 'react'
import { useSearch } from '../context/SearchContext.jsx'
import ProductCardDisplay from '../FoodDisplay/ProductCardDisplay/ProductCardDisplay.jsx'
import './SearchResults.css'

const PAGE_SIZE_RESTAURANTS = 8
const PAGE_SIZE_PRODUCTS = 10

function useDebounce(value, delay = 350) {
  const [debounced, setDebounced] = useState(value)
  useEffect(() => {
    const t = setTimeout(() => setDebounced(value), delay)
    return () => clearTimeout(t)
  }, [value, delay])
  return debounced
}

const SearchResults = () => {
  const { query } = useSearch()
  const debouncedQuery = useDebounce(query)

  const [activeTab, setActiveTab] = useState('restaurants')

  const [restaurants, setRestaurants] = useState([])
  const [restPage, setRestPage] = useState(0)
  const [restTotalPages, setRestTotalPages] = useState(1)
  const [restTotal, setRestTotal] = useState(0)
  const [restLoading, setRestLoading] = useState(false)

  const [products, setProducts] = useState([])
  const [prodPage, setProdPage] = useState(0)
  const [prodTotalPages, setProdTotalPages] = useState(1)
  const [prodTotal, setProdTotal] = useState(0)
  const [prodLoading, setProdLoading] = useState(false)

  // Reset de página quando a query muda
  useEffect(() => {
    setRestPage(0)
    setProdPage(0)
  }, [debouncedQuery])

  // Busca restaurantes — parâmetro correto: restaurantName
  useEffect(() => {
    if (!debouncedQuery.trim()) return
    setRestLoading(true)

    const params = new URLSearchParams({
      restaurantName: debouncedQuery,
      page: restPage,
      size: PAGE_SIZE_RESTAURANTS,
    })

    fetch(`/v1/restaurants?${params}`, { credentials: 'include' })
      .then(r => r.json())
      .then(data => {
        setRestaurants(data.content || [])
        setRestTotal(data.totalElements || 0)
        setRestTotalPages(data.totalPage || 1)
      })
      .catch(() => setRestaurants([]))
      .finally(() => setRestLoading(false))
  }, [debouncedQuery, restPage])

  // Busca produtos — parâmetro correto: productName
  useEffect(() => {
    if (!debouncedQuery.trim()) return
    setProdLoading(true)

    const params = new URLSearchParams({
      productName: debouncedQuery,
      page: prodPage,
      size: PAGE_SIZE_PRODUCTS,
    })

    fetch(`/v1/products?${params}`, { credentials: 'include' })
      .then(r => r.json())
      .then(data => {
        setProducts(data.content || [])
        setProdTotal(data.totalElements || 0)
        setProdTotalPages(data.totalPage || 1)
      })
      .catch(() => setProducts([]))
      .finally(() => setProdLoading(false))
  }, [debouncedQuery, prodPage])

  return (
    <div className="search-results">

      <div className="search-tabs">
        <button
          className={`search-tab ${activeTab === 'restaurants' ? 'active' : ''}`}
          onClick={() => setActiveTab('restaurants')}
        >
          Restaurantes
          {restTotal > 0 && <span className="search-tab-count">{restTotal}</span>}
        </button>
        <button
          className={`search-tab ${activeTab === 'products' ? 'active' : ''}`}
          onClick={() => setActiveTab('products')}
        >
          Produtos
          {prodTotal > 0 && <span className="search-tab-count">{prodTotal}</span>}
        </button>
      </div>

      {activeTab === 'restaurants' && (
        <div className="search-section">
          {restLoading ? (
            <p className="search-loading">Buscando restaurantes...</p>
          ) : restaurants.length === 0 ? (
            <p className="search-empty">Nenhum restaurante encontrado para "{debouncedQuery}"</p>
          ) : (
            <>
              <div className="search-restaurants-grid">
                {restaurants.map(r => (
                  <a key={r.id} href={`/restaurants/${r.id}`} className="search-restaurant-card">
                    {r.imageUrl && <img src={r.imageUrl} alt={r.name} />}
                    <span>{r.name}</span>
                  </a>
                ))}
              </div>
              <Pagination current={restPage} total={restTotalPages} onChange={setRestPage} />
            </>
          )}
        </div>
      )}

      {activeTab === 'products' && (
        <div className="search-section">
          {prodLoading ? (
            <p className="search-loading">Buscando produtos...</p>
          ) : products.length === 0 ? (
            <p className="search-empty">Nenhum produto encontrado para "{debouncedQuery}"</p>
          ) : (
            <>
              <div className="search-products-grid">
                {products.map(p => (
                  <ProductCardDisplay key={p.id} product={p} />
                ))}
              </div>
              <Pagination current={prodPage} total={prodTotalPages} onChange={setProdPage} />
            </>
          )}
        </div>
      )}

    </div>
  )
}

const Pagination = ({ current, total, onChange }) => {
  if (total <= 1) return null
  return (
    <div className="search-pagination">
      <button className="page-btn" disabled={current === 0} onClick={() => onChange(current - 1)}>
        ← Anterior
      </button>
      <span className="page-info">{current + 1} de {total}</span>
      <button className="page-btn" disabled={current >= total - 1} onClick={() => onChange(current + 1)}>
        Próxima →
      </button>
    </div>
  )
}

export default SearchResults