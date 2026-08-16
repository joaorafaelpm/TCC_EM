
import { AuthProvider, useAuth } from './components/context/AuthProvider.jsx'
import Navbar from "./components/Navbar/Navbar.jsx"
import Footer from "./components/Footer/Footer.jsx"
import Home from "./pages/Home/Home.jsx"
import PlaceOrder from "./pages/PlaceOrder/PlaceOrder.jsx"
import Cart from "./pages/Cart/Cart.jsx"
import TermsOfUser from "./pages/TermsOfUser/TermsOfUser.jsx"
import { Route, Routes, useLocation } from 'react-router-dom'
import Cadastro from './pages/Cadastro/Cadastro.jsx'
import User from './pages/Restaurant/Restaurant.jsx'
import StoreContextProvider from './components/context/StoreContext.jsx';
import RestaurantForm from './pages/RestaurantForm/RestaurantForm.jsx'
import MyAccount from './pages/MyAccount/MyAccount.jsx'
import Restaurant from './pages/Restaurant/Restaurant.jsx'
import OrderHandler from './pages/OrderHandler/OrderHandler.jsx'
import { SearchProvider } from './components/context/SearchContext.jsx'
import NotFound from "./pages/NotFound/NotFound.jsx"

function AppContent() {
  const { user, isLoading } = useAuth()
  const location = useLocation()

  const noCadastro = location.pathname.includes('/cadastro')

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
            <Route path='/register-restaurant' element={<RestaurantForm />} />
            <Route path='/restaurant/:id' element={<Restaurant />} />
            <Route path='/restaurant/:id/orders' element={<OrderHandler />} />
            <Route path='/my-account' element={<MyAccount />} />
            <Route path='*' element={<NotFound />} />
          </Routes>
        </div>
        <Footer />
    </div>
  )
}

function App() {
  return (
    <AuthProvider>
      <SearchProvider>
        <StoreContextProvider>
          <AppContent />
        </StoreContextProvider>
      </SearchProvider>
    </AuthProvider>
  )
}

export default App;