import React, { useState, useEffect } from 'react';
import { useAuth } from '../../components/context/AuthProvider.jsx';
import Sidebar from '../../components/MyAccount/SideBar/Sidebar.jsx';
import MyInfo from '../../components/MyAccount/MyInfo/MyInfo.jsx';
import MyRestaurants from '../../components/MyAccount/MyRestaurants/MyRestaurants.jsx';
import SalesReport from '../../components/SalesReport/SalesReport.jsx';
import './MyAccount.css';
import MyOrders from '../../components/MyAccount/MyOrders/MyOrders.jsx';

const MyAccount = () => {
  const { user: tokenData, isLoading: authLoading } = useAuth();
  const [activeSection, setActiveSection] = useState('info');
  const [userData, setUserData] = useState(null);
  const [userLoading, setUserLoading] = useState(true);
  const [userError, setUserError] = useState(null);

  useEffect(() => {
    if (authLoading || !tokenData?.userId) return;

    const fetchUser = async () => {
      try {
        const res = await fetch(`/v1/users/${tokenData.userId}`, {
          credentials: 'include',
        });

        if (!res.ok) throw new Error('Erro ao carregar dados do usuário.');

        const data = await res.json();
        setUserData(data);
      } catch (err) {
        setUserError(err.message);
      } finally {
        setUserLoading(false);
      }
    };

    fetchUser();
  }, [authLoading, tokenData]);

  const isLoading = authLoading || userLoading;

  if (isLoading) {
    return (
      <div className="account-loading">
        <div className="account-loading__spinner" />
        <p>Carregando...</p>
      </div>
    );
  }

  if (userError) {
    return (
      <div className="account-loading">
        <p style={{ color: '#a8222e' }}>⚠️ {userError}</p>
      </div>
    );
  }

  if (!userData) return null;

  const renderSection = () => {
    switch (activeSection) {
      case 'orders':
        return <MyOrders userId={tokenData.userId} />;
      case 'info':
        return <MyInfo user={userData} />;
      case 'restaurants':
        return <MyRestaurants userId={tokenData.userId} />;
      case 'sales-report':
        return <SalesReport userId={tokenData.userId} />;
      default:
        return <MyInfo user={userData} userId={tokenData.userId} />;
    }
  };

  return (
    <div className="account-page">
      <Sidebar activeSection={activeSection} onNavigate={setActiveSection} user={userData} />
      <main className="account-main">
        {renderSection()}
      </main>
    </div>
  );
};

export default MyAccount;