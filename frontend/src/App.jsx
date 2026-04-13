
import { AuthProvider, useAuth } from './context/AuthContext'
import Navbar from "./components/Navbar/Navbar.jsx"
import Footer from "./components/Footer/Footer.jsx"
import Home from "./pages/Home/Home.jsx"
import PlaceOrder from "./pages/PlaceOrder/PlaceOrder.jsx"
import Cart from "./pages/Cart/Cart.jsx"
import TermsOfUser from "./pages/TermsOfUser/TermsOfUser.jsx"
import { Route, Routes, useLocation } from 'react-router-dom'
import Cadastro from './pages/Cadastro/Cadastro.jsx'

function AppContent() {
  const { user, isLoading } = useAuth()
  const location = useLocation()

  const noCadastro = location.pathname.includes('/cadastro')

  // Sempre renderiza o cadastro, independente do estado de auth
  if (noCadastro) {
    return (
      <Routes>
        <Route path='/cadastro' element={<Cadastro />} />
      </Routes>
    )
  }

  if (isLoading) return <div>Carregando...</div>
  if (!user) return null

  return (
    <div className="app">
      <Navbar />
      <div className="page-content">
        <Routes>
          <Route path='/' element={<Home />} />
          <Route path='/cart' element={<Cart />} />
          <Route path='/order' element={<PlaceOrder />} />
          <Route path="/terms_of_user" element={<TermsOfUser />} />
        </Routes>
      </div>
      <Footer />
    </div>
  )
}

function App() {
  return (
    <AuthProvider>
      <AppContent />
    </AuthProvider>
  )
}

export default App;