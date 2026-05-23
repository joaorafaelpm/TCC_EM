import React, { useRef, useState } from 'react';
import ReactDOM from 'react-dom';
import PhotoUploadModal from '../PhotoUploadModal/PhotoUploadModal.jsx';
import PhotoCropModal from '../PhotoCropModal/PhotoCropModal.jsx';
import './PhotoUploadTrigger.css';

const MAX_FILE_SIZE = 500 * 1024; // 500 KB
const ALLOWED_TYPES = ['image/jpeg', 'image/png'];

/**
 * Componente reutilizável de upload de foto.
 * Fluxo com crop:    arquivo → PhotoCropModal → PhotoUploadModal → PUT
 * Fluxo sem crop:    arquivo → PhotoUploadModal → PUT
 *
 * Os modais são renderizados via Portal em document.body para escapar de
 * qualquer ancestral com overflow:hidden ou z-index limitado.
 */
const PhotoUploadTrigger = ({
  uploadUrl,
  deleteUrl,
  onSuccess,
  cropShape,
  triggerVariant = 'pencil-corner',
  label = 'Alterar foto',
  className = '',
  children,
}) => {
  const inputRef = useRef(null);
  const [fileError,   setFileError]   = useState(null);
  const [pendingFile, setPendingFile] = useState(null);
  const [croppedBlob, setCroppedBlob] = useState(null);

  const openPicker = () => {
    if (inputRef.current) {
      inputRef.current.value = '';
      inputRef.current.click();
    }
  };

  const handleFileChange = (e) => {
    setFileError(null);
    const file = e.target.files?.[0];
    if (!file) return;

    if (!ALLOWED_TYPES.includes(file.type)) {
      setFileError('Formato inválido. Apenas JPG e PNG são aceitos.');
      return;
    }
    if (file.size > MAX_FILE_SIZE) {
      setFileError(`Arquivo muito grande: ${(file.size / 1024).toFixed(0)} KB. Máximo: 500 KB.`);
      return;
    }

    setPendingFile(file);
  };

  const handleCropDone = (blob) => {
    const croppedFile = new File([blob], pendingFile.name, { type: pendingFile.type });
    setCroppedBlob(croppedFile);
  };

  const handleCropCancel = () => {
    setPendingFile(null);
    setCroppedBlob(null);
  };

  const handleUploadSuccess = () => {
    setPendingFile(null);
    setCroppedBlob(null);
    onSuccess?.();
  };

  const handleUploadClose = () => {
    if (cropShape) {
      setCroppedBlob(null);
    } else {
      setPendingFile(null);
    }
  };

  const fileForUpload = cropShape ? croppedBlob : pendingFile;

  return (
    <div className={`put-root put-root--${triggerVariant} ${className}`}>
      {children}

      {triggerVariant === 'button' ? (
        <button className="put-btn" onClick={openPicker} type="button">
          <PencilIcon />
          {label}
        </button>
      ) : triggerVariant === 'pencil-overlay' ? (
        <button className="put-overlay-btn" onClick={openPicker} type="button" aria-label={label} title={label}>
          <PencilIcon size={16} />
          <span>{label}</span>
        </button>
      ) : (
        <button className="put-corner-btn" onClick={openPicker} type="button" aria-label={label} title={label}>
          <PencilIcon size={14} />
        </button>
      )}

      <input
        ref={inputRef}
        type="file"
        accept="image/jpeg,image/png"
        style={{
          position: 'absolute',
          width: '1px',
          height: '1px',
          opacity: 0,
          pointerEvents: 'none',
          overflow: 'hidden',
        }}
        onChange={handleFileChange}
        aria-hidden="true"
        tabIndex={-1}
      />

      {fileError && <p className="put-error" role="alert">{fileError}</p>}

      {/* Portal: renderiza os modais direto no body, escapando de overflow:hidden */}
      {cropShape && pendingFile && !croppedBlob && ReactDOM.createPortal(
        <PhotoCropModal
          file={pendingFile}
          shape={cropShape}
          onCrop={handleCropDone}
          onClose={handleCropCancel}
        />,
        document.body
      )}

      {fileForUpload && ReactDOM.createPortal(
        <PhotoUploadModal
          file={fileForUpload}
          uploadUrl={uploadUrl}
          deleteUrl={deleteUrl}
          onSuccess={handleUploadSuccess}
          onClose={handleUploadClose}
        />,
        document.body
      )}
    </div>
  );
};

const PencilIcon = ({ size = 14 }) => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    width={size}
    height={size}
    viewBox="0 0 24 24"
    fill="none"
    stroke="currentColor"
    strokeWidth="2.5"
    strokeLinecap="round"
    strokeLinejoin="round"
    aria-hidden="true"
  >
    <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7" />
    <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z" />
  </svg>
);

export default PhotoUploadTrigger;