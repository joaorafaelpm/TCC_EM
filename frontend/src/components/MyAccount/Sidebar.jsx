import React from 'react';
import { useAuth } from '../context/AuthProvider.jsx';
import './Sidebar.css';

const NAV_ITEMS = [
  {
    key: 'info',
    label: 'Minhas informações',
    icon: (
      <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24"
        fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" />
        <circle cx="12" cy="7" r="4" />
      </svg>
    ),
  },
  {
    key: 'restaurants',
    label: 'Meus restaurantes',
    icon: (
      <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24"
        fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <path d="M3 11l19-9-9 19-2-8-8-2z" />
      </svg>
    ),
  },
  {
    key: 'sales-report',
    label: 'Relatório de vendas',
    icon: (
      <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24"
        fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <line x1="18" y1="20" x2="18" y2="10" />
        <line x1="12" y1="20" x2="12" y2="4" />
        <line x1="6"  y1="20" x2="6"  y2="14" />
      </svg>
    ),
  },
];

const Sidebar = ({ activeSection, onNavigate, user }) => {
  const { logout } = useAuth();

  const initials = user?.name
    ? user.name.split(' ').slice(0, 2).map((n) => n[0]).join('').toUpperCase()
    : '?';

  return (
    <aside className="sidebar">
      <div className="sidebar__brand">
        <span className="sidebar__brand-logo">🍽️</span>
        <span className="sidebar__brand-name">Minha Conta</span>
      </div>

      <div className="sidebar__profile">
        <div className="sidebar__avatar">{initials}</div>
        <div className="sidebar__profile-info">
          <span className="sidebar__profile-name">{user?.name?.split(' ')[0]}</span>
          <span className="sidebar__profile-email">{user?.email}</span>
        </div>
      </div>

      <nav className="sidebar__nav">
        {NAV_ITEMS.map((item) => (
          <button
            key={item.key}
            className={`sidebar__nav-item ${activeSection === item.key ? 'sidebar__nav-item--active' : ''}`}
            onClick={() => onNavigate(item.key)}
          >
            <span className="sidebar__nav-icon">{item.icon}</span>
            <span>{item.label}</span>
          </button>
        ))}
      </nav>

      <div className="sidebar__footer">
        <button className="sidebar__logout" onClick={logout}>
          <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24"
            fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
            <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4" />
            <polyline points="16 17 21 12 16 7" />
            <line x1="21" y1="12" x2="9" y2="12" />
          </svg>
          <span>Sair</span>
        </button>
      </div>
    </aside>
  );
};

export default Sidebar;