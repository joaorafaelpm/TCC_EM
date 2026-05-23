import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './RestaurantHero.css';
import semImagemPng from '../../../assets/sem-foto.png';
import PhotoUploadTrigger from '../../PhotoHandler/PhotoUploadTrigger/PhotoUploadTrigger';

const RestaurantHero = ({ restaurant, canEdit, onEditClick, authorities = null, user = null, authToken = null }) => {
  const { name, open, address, shippingFee } = restaurant;
  const [photoKey, setPhotoKey] = useState(0);
  const [modalOpen, setModalOpen] = useState(false);
  const [loadingAction, setLoadingAction] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [infoMessage, setInfoMessage] = useState('');
  const [localOpenState, setLocalOpenState] = useState(open);
  const [localActiveState, setLocalActiveState] = useState(restaurant.active ?? true);

  const navigate = useNavigate();

  const formattedShipping = shippingFee > 0
    ? new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(shippingFee)
    : 'Grátis';

  const photo = (
    <img
      key={photoKey}
      src={`/v1/restaurants/${restaurant.id}/photo?v=${photoKey}`}
      alt={name}
      onError={(e) => {
        e.target.onerror = null;
        e.target.src = semImagemPng;
      }}
      className={`status-image-restaurant ${localOpenState ? 'open' : 'closed'}`}
    />
  );

  const normalizedAuthorities = React.useMemo(() => {
    let arr = [];
    if (Array.isArray(authorities)) arr = authorities;
    else if (user && Array.isArray(user.authorities)) arr = user.authorities;
    return arr.map(a => (typeof a === 'string' ? a.trim().toUpperCase() : a)).filter(Boolean);
  }, [authorities, user]);

  const hasAuthority = (auth) => normalizedAuthorities.includes(auth.trim().toUpperCase());

  const canEditLogic = hasAuthority('EDITAR_LOGICA_RESTAURANTES');
  const canManage    = hasAuthority('GERENCIAR_RESTAURANTE');

  const canCloseAvailable      = canEditLogic && localOpenState && !loadingAction;
  const canOpenAvailable       = canEditLogic && !localOpenState && !loadingAction;
  const canDeactivateAvailable = canManage && localActiveState && !loadingAction;
  const canActivateAvailable   = canManage && !localActiveState && !loadingAction;

  const fetchWithAuth = (url, options = {}) => {
    const headers = options.headers ? { ...options.headers } : {};
    if (authToken) headers['Authorization'] = `Bearer ${authToken}`;
    headers['Content-Type'] = headers['Content-Type'] || 'application/json';
    return fetch(url, { ...options, headers });
  };

  const handleAction = async (type) => {
    setErrorMessage('');
    setInfoMessage('');
    const needsEditAuth   = (type === 'open' || type === 'close');
    const needsManageAuth = (type === 'activate' || type === 'deactivate');

    if (needsEditAuth && !hasAuthority('EDITAR_LOGICA_RESTAURANTES')) {
      setErrorMessage('Você não tem permissão necessária.');
      return;
    }
    if (needsManageAuth && !hasAuthority('GERENCIAR_RESTAURANTE')) {
      setErrorMessage('Você não tem permissão necessária.');
      return;
    }

    setLoadingAction(true);
    try {
      let res;
      const base = `/v1/restaurants/${restaurant.id}`;
      if (type === 'open')       res = await fetchWithAuth(`${base}/opening`, { method: 'PUT' });
      else if (type === 'close') res = await fetchWithAuth(`${base}/opening`, { method: 'DELETE' });
      else if (type === 'activate')   res = await fetchWithAuth(`${base}/active`, { method: 'PUT' });
      else if (type === 'deactivate') res = await fetchWithAuth(`${base}/active`, { method: 'DELETE' });

      if (!res.ok) {
        const text = await res.text().catch(() => null);
        throw new Error(text || `Erro ${res.status}`);
      }

      if (type === 'open')       setLocalOpenState(true);
      if (type === 'close')      setLocalOpenState(false);
      if (type === 'activate')   setLocalActiveState(true);
      if (type === 'deactivate') setLocalActiveState(false);

      setInfoMessage('Operação realizada com sucesso.');
    } catch (err) {
      console.error(err);
      setErrorMessage(err.userMessage || 'Ocorreu um erro ao realizar a operação.');
    } finally {
      setLoadingAction(false);
    }
  };

  return (
    <section className="restaurant-hero">
      <div className="hero-content">
        <div className="img-wrapper">
          {canEdit ? (
            <PhotoUploadTrigger
              uploadUrl={`http://localhost/v1/restaurants/${restaurant.id}/photo`}
              deleteUrl={`http://localhost/v1/restaurants/${restaurant.id}/photo`}
              triggerVariant="pencil-overlay"
              cropShape="circle"
              label="Alterar foto do restaurante"
              onSuccess={() => setPhotoKey(k => k + 1)}
            >
              {photo}
            </PhotoUploadTrigger>
          ) : photo}
        </div>

        <div className="info-main">
          <div className="title-group">
            <h1>{name}</h1>
            <span className={`status-badge ${localOpenState ? 'open' : 'closed'}`}>
              {localOpenState ? 'Aberto' : 'Fechado'}
            </span>
          </div>

          <p className="address">
            {address?.street}, {address?.number} - {address?.neighborhood}
            <br />
            {address?.city?.name} / {address?.city?.state}
          </p>
        </div>

        <div className="delivery-info">
          <div className="info-item">
            <span className="label">Frete: </span>
            <span className="value"><strong>{formattedShipping}</strong></span>
          </div>

          <div style={{ marginTop: 12, display: 'flex', gap: 8, flexDirection: 'column', alignItems: 'flex-end' }}>
            {canEdit && (
              <div style={{ display: 'flex', gap: 8 }}>
                <button className="hero-edit-btn" onClick={onEditClick}>
                  <svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" viewBox="0 0 24 24"
                    fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                    <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7" />
                    <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z" />
                  </svg>
                  Editar restaurante
                </button>
                <button className="hero-edit-btn" onClick={() => setModalOpen(true)}>
                  Gerenciar status
                </button>
              </div>
            )}

            {/* Botão de pedidos — visível apenas para quem tem EDITAR_LOGICA_RESTAURANTES */}
            {canEditLogic && (
              <button
                className="hero-orders-btn"
                onClick={() => navigate(`/restaurant/${restaurant.id}/orders`)}
              >
                <svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" viewBox="0 0 24 24"
                  fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                  <path d="M9 5H7a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h10a2 2 0 0 0 2-2V7a2 2 0 0 0-2-2h-2"/>
                  <rect x="9" y="3" width="6" height="4" rx="1"/>
                  <path d="M9 12h6M9 16h4"/>
                </svg>
                Ver pedidos
              </button>
            )}
          </div>
        </div>
      </div>

      {modalOpen && (
        <div
          className="status-modal-overlay"
          onClick={() => { if (!loadingAction) { setModalOpen(false); setErrorMessage(''); setInfoMessage(''); } }}
        >
          <div className="status-modal" onClick={(e) => e.stopPropagation()}>
            <h3>Gerenciar Restaurante</h3>

            <div className="modal-section">
              <h4>Aberto / Fechado</h4>
              <div className="modal-actions">
                <button
                  className={`action-btn ${canOpenAvailable ? 'available-primary' : 'muted'}`}
                  disabled={!canOpenAvailable}
                  onClick={() => {
                    if (!canEditLogic) { setErrorMessage('Você não tem permissão necessária (EDITAR_LOGICA_RESTAURANTES).'); return; }
                    handleAction('open');
                  }}
                  title={!canEditLogic ? 'Permissão necessária: EDITAR_LOGICA_RESTAURANTES' : ''}
                >
                  Abrir restaurante
                </button>
                <button
                  className={`action-btn ${canCloseAvailable ? 'available-danger' : 'muted'}`}
                  disabled={!canCloseAvailable}
                  onClick={() => {
                    if (!canEditLogic) { setErrorMessage('Você não tem permissão necessária (EDITAR_LOGICA_RESTAURANTES).'); return; }
                    handleAction('close');
                  }}
                  title={!canEditLogic ? 'Permissão necessária: EDITAR_LOGICA_RESTAURANTES' : ''}
                >
                  Fechar restaurante
                </button>
              </div>
            </div>

            <div className="modal-section">
              <h4>Ativar / Desativar</h4>
              <div className="modal-actions">
                <button
                  className={`action-btn ${canActivateAvailable ? 'available-primary' : 'muted'}`}
                  disabled={!canActivateAvailable}
                  onClick={() => {
                    if (!canManage) { setErrorMessage('Você não tem permissão necessária.'); return; }
                    handleAction('activate');
                  }}
                  title={!canManage ? 'Permissão necessária: GERENCIAR_RESTAURANTE' : ''}
                >
                  Ativar
                </button>
                <button
                  className={`action-btn ${canDeactivateAvailable ? 'available-danger' : 'muted'}`}
                  disabled={!canDeactivateAvailable}
                  onClick={() => {
                    if (!canManage) { setErrorMessage('Você não tem permissão necessária.'); return; }
                    handleAction('deactivate');
                  }}
                  title={!canManage ? 'Permissão necessária: GERENCIAR_RESTAURANTE' : ''}
                >
                  Desativar
                </button>
              </div>
            </div>

            <div className="modal-footer">
              {loadingAction && <span className="muted">Processando...</span>}
              {infoMessage  && <span className="info-message">{infoMessage}</span>}
              {errorMessage && <span className="error-message">{errorMessage}</span>}
              <div className="hero-edit-close-div">
                <button
                  className="hero-edit-close-btn"
                  onClick={() => { if (!loadingAction) setModalOpen(false); }}
                >
                  Fechar
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </section>
  );
};

export default RestaurantHero;