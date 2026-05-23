import React, { useEffect, useState } from 'react';
import './PhotoUploadModal.css';

/**
 * Modal de preview + descrição + upload/deleção de foto.
 *
 * @param {File}     file       — arquivo já validado pelo PhotoUploadTrigger
 * @param {string}   uploadUrl  — URL do endpoint PUT multipart/form-data
 * @param {string}   [deleteUrl] — URL do endpoint DELETE (opcional)
 * @param {Function} onSuccess  — callback chamado após upload ou deleção bem-sucedida
 * @param {Function} onClose    — callback para fechar sem salvar
 */
const PhotoUploadModal = ({ file, uploadUrl, deleteUrl, onSuccess, onClose }) => {
  const [preview, setPreview] = useState(null);
  const [description, setDescription] = useState('');
  const [descError, setDescError] = useState(null);
  const [uploading, setUploading] = useState(false);
  const [uploadError, setUploadError] = useState(null);
  const [confirmingDelete, setConfirmingDelete] = useState(false);
  const [deleting, setDeleting] = useState(false);

  // URL.createObjectURL é síncrono e mais confiável que FileReader para preview
  useEffect(() => {
    if (!file) return;
    const url = URL.createObjectURL(file);
    setPreview(url);
    // revoga a URL ao desmontar para liberar memória
    return () => URL.revokeObjectURL(url);
  }, [file]);

  const fileSizeLabel = file ? `${(file.size / 1024).toFixed(0)} KB` : '';
  const fileTypeLabel = file?.type === 'image/jpeg' ? 'JPG' : 'PNG';

  const handleSubmit = async () => {
    setDescError(null);
    setUploadError(null);

    if (!description.trim()) {
      setDescError('A descrição é obrigatória.');
      return;
    }

    setUploading(true);
    try {
      const formData = new FormData();
      formData.append('file', file);
      formData.append('description', description.trim());

      const res = await fetch(uploadUrl, {
        method: 'PUT',
        credentials: 'include',
        body: formData,
      });

      if (!res.ok) {
        const text = await res.text();
        throw new Error(text || `Erro ${res.status} ao enviar a foto.`);
      }

      onSuccess?.();
    } catch (err) {
      setUploadError(err.message);
    } finally {
      setUploading(false);
    }
  };

  const handleDelete = async () => {
    setDeleting(true);
    setUploadError(null);
    try {
      const res = await fetch(deleteUrl, {
        method: 'DELETE',
        credentials: 'include',
      });
      if (!res.ok) {
        const text = await res.text();
        throw new Error(text || `Erro ${res.status} ao remover a foto.`);
      }
      onSuccess?.();
    } catch (err) {
      setUploadError(err.message);
      setConfirmingDelete(false);
    } finally {
      setDeleting(false);
    }
  };

  const isBusy = uploading || deleting;

  return (
    <div className="photo-modal-overlay" onClick={onClose}>
      <div className="photo-modal" onClick={e => e.stopPropagation()}>

        {/* ── Header ── */}
        <div className="photo-modal__header">
          <h3>Alterar foto</h3>
          <div className="photo-modal__header-actions">
            {deleteUrl && !confirmingDelete && (
              <button
                className="photo-modal__icon-btn photo-modal__icon-btn--delete"
                onClick={() => setConfirmingDelete(true)}
                disabled={isBusy}
                aria-label="Remover foto atual"
                title="Remover foto atual"
              >
                <TrashIcon />
              </button>
            )}
            <button
              className="photo-modal__close"
              onClick={onClose}
              disabled={isBusy}
              aria-label="Fechar"
            >
              <CloseIcon />
            </button>
          </div>
        </div>

        {/* ── Confirmação de deleção ── */}
        {confirmingDelete ? (
          <div className="photo-modal__delete-confirm">
            <div className="photo-modal__delete-icon" aria-hidden="true">🗑️</div>
            <p className="photo-modal__delete-title">Remover foto atual?</p>
            <p className="photo-modal__delete-msg">
              A foto será excluída permanentemente e não poderá ser recuperada.
            </p>
            {uploadError && (
              <p className="photo-modal__upload-error">{uploadError}</p>
            )}
            <div className="photo-modal__actions">
              <button
                className="photo-modal__btn photo-modal__btn--cancel"
                onClick={() => { setConfirmingDelete(false); setUploadError(null); }}
                disabled={deleting}
              >
                Cancelar
              </button>
              <button
                className="photo-modal__btn photo-modal__btn--delete"
                onClick={handleDelete}
                disabled={deleting}
              >
                {deleting ? (
                  <><span className="photo-modal__spinner" aria-hidden="true" />Removendo...</>
                ) : 'Confirmar remoção'}
              </button>
            </div>
          </div>
        ) : (
          <>
            {/* ── Preview ── */}
            <div className="photo-modal__preview-wrapper">
              {preview ? (
                <img src={preview} alt="Preview da nova foto" className="photo-modal__preview" />
              ) : (
                <div className="photo-modal__preview-placeholder">Carregando preview...</div>
              )}
            </div>

            <div className="photo-modal__file-info">
              <span className="photo-modal__pill">{fileTypeLabel}</span>
              <span className="photo-modal__pill">{fileSizeLabel}</span>
            </div>

            {/* ── Descrição ── */}
            <div className="photo-modal__field">
              <label htmlFor="photo-description">
                Descrição da foto <span aria-hidden="true">*</span>
              </label>
              <input
                id="photo-description"
                type="text"
                value={description}
                onChange={e => { setDescription(e.target.value); setDescError(null); }}
                placeholder="ex: Foto principal da pizza margherita"
                maxLength={255}
                disabled={isBusy}
              />
              {descError && <p className="photo-modal__field-error">{descError}</p>}
            </div>

            {uploadError && (
              <p className="photo-modal__upload-error">{uploadError}</p>
            )}

            {/* ── Ações ── */}
            <div className="photo-modal__actions">
              <button
                className="photo-modal__btn photo-modal__btn--cancel"
                onClick={onClose}
                disabled={isBusy}
              >
                Cancelar
              </button>
              <button
                className="photo-modal__btn photo-modal__btn--save"
                onClick={handleSubmit}
                disabled={isBusy || !preview}
              >
                {uploading ? (
                  <><span className="photo-modal__spinner" aria-hidden="true" />Enviando...</>
                ) : 'Salvar foto'}
              </button>
            </div>
          </>
        )}
      </div>
    </div>
  );
};

const TrashIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" viewBox="0 0 24 24"
    fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round"
    aria-hidden="true">
    <polyline points="3 6 5 6 21 6" />
    <path d="M19 6l-1 14a2 2 0 0 1-2 2H8a2 2 0 0 1-2-2L5 6" />
    <path d="M10 11v6M14 11v6" />
    <path d="M9 6V4h6v2" />
  </svg>
);

const CloseIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24"
    fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round"
    aria-hidden="true">
    <line x1="18" y1="6" x2="6" y2="18" />
    <line x1="6" y1="6" x2="18" y2="18" />
  </svg>
);

export default PhotoUploadModal;