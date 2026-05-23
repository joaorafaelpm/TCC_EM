import React, { useCallback, useEffect, useRef, useState } from 'react';
import './PhotoCropModal.css';

const CANVAS_SIZE = 320;
const MIN_SCALE  = 0.5;
const MAX_SCALE  = 4;

const PhotoCropModal = ({ file, shape = 'circle', onCrop, onClose }) => {
  const canvasRef  = useRef(null);
  const imageRef   = useRef(null);
  const dragging   = useRef(false);
  const lastPos    = useRef({ x: 0, y: 0 });

  const [offset, setOffset] = useState({ x: 0, y: 0 });
  const [scale,  setScale]  = useState(1);
  const [ready,  setReady]  = useState(false);

  useEffect(() => {
    if (!file) return;
    const url = URL.createObjectURL(file);
    const img = new Image();
    img.onload = () => {
      const initialScale = Math.max(
        CANVAS_SIZE / img.naturalWidth,
        CANVAS_SIZE / img.naturalHeight,
      );
      imageRef.current = img;
      setScale(initialScale);
      setOffset({ x: 0, y: 0 });
      setReady(true);
    };
    img.src = url;
    return () => URL.revokeObjectURL(url);
  }, [file]);

  const draw = useCallback(() => {
    const canvas = canvasRef.current;
    const img    = imageRef.current;
    if (!canvas || !img) return;

    const ctx = canvas.getContext('2d');
    const cx  = CANVAS_SIZE / 2;
    const cy  = CANVAS_SIZE / 2;

    ctx.clearRect(0, 0, CANVAS_SIZE, CANVAS_SIZE);

    const drawW = img.naturalWidth  * scale;
    const drawH = img.naturalHeight * scale;
    const drawX = cx + offset.x - drawW / 2;
    const drawY = cy + offset.y - drawH / 2;
    ctx.drawImage(img, drawX, drawY, drawW, drawH);

    ctx.save();
    ctx.fillStyle = 'rgba(0,0,0,0.52)';
    ctx.fillRect(0, 0, CANVAS_SIZE, CANVAS_SIZE);
    ctx.globalCompositeOperation = 'destination-out';
    ctx.beginPath();
    if (shape === 'circle') {
      ctx.arc(cx, cy, CANVAS_SIZE / 2 - 4, 0, Math.PI * 2);
    } else {
      const pad = 4;
      ctx.roundRect(pad, pad, CANVAS_SIZE - pad * 2, CANVAS_SIZE - pad * 2, 6);
    }
    ctx.fill();
    ctx.restore();

    ctx.save();
    ctx.strokeStyle = 'rgba(255,255,255,0.85)';
    ctx.lineWidth   = 2;
    ctx.beginPath();
    if (shape === 'circle') {
      ctx.arc(cx, cy, CANVAS_SIZE / 2 - 4, 0, Math.PI * 2);
    } else {
      const pad = 4;
      ctx.roundRect(pad, pad, CANVAS_SIZE - pad * 2, CANVAS_SIZE - pad * 2, 6);
    }
    ctx.stroke();
    ctx.restore();
  }, [offset, scale, shape]);

  useEffect(() => { if (ready) draw(); }, [ready, draw]);

  // ── Mouse: listeners no window para não perder o drag ao sair do canvas ──
  const onMouseDown = (e) => {
    dragging.current = true;
    lastPos.current  = { x: e.clientX, y: e.clientY };
  };

  useEffect(() => {
    const onMouseMove = (e) => {
      if (!dragging.current) return;
      const dx = e.clientX - lastPos.current.x;
      const dy = e.clientY - lastPos.current.y;
      lastPos.current = { x: e.clientX, y: e.clientY };
      setOffset(prev => ({ x: prev.x + dx, y: prev.y + dy }));
    };

    const onMouseUp = () => { dragging.current = false; };

    window.addEventListener('mousemove', onMouseMove);
    window.addEventListener('mouseup',   onMouseUp);
    return () => {
      window.removeEventListener('mousemove', onMouseMove);
      window.removeEventListener('mouseup',   onMouseUp);
    };
  }, []);

  // ── Touch ──
  const onTouchStart = (e) => {
    dragging.current = true;
    lastPos.current  = { x: e.touches[0].clientX, y: e.touches[0].clientY };
  };

  const onTouchMove = useCallback((e) => {
    if (!dragging.current) return;
    e.preventDefault();
    const dx = e.touches[0].clientX - lastPos.current.x;
    const dy = e.touches[0].clientY - lastPos.current.y;
    lastPos.current = { x: e.touches[0].clientX, y: e.touches[0].clientY };
    setOffset(prev => ({ x: prev.x + dx, y: prev.y + dy }));
  }, []);

  const onTouchEnd = () => { dragging.current = false; };

  // ── Zoom ──
  const onWheel = useCallback((e) => {
    e.preventDefault();
    const delta = e.deltaY > 0 ? -0.08 : 0.08;
    setScale(prev => Math.min(MAX_SCALE, Math.max(MIN_SCALE, prev + delta)));
  }, []);

  // ── Exporta o recorte ──
  const handleConfirm = () => {
    const img  = imageRef.current;
    if (!img) return;

    const out   = document.createElement('canvas');
    const size  = CANVAS_SIZE - 8;
    out.width   = size;
    out.height  = size;
    const ctx   = out.getContext('2d');
    const cx    = CANVAS_SIZE / 2;
    const cy    = CANVAS_SIZE / 2;

    if (shape === 'circle') {
      ctx.beginPath();
      ctx.arc(size / 2, size / 2, size / 2, 0, Math.PI * 2);
      ctx.clip();
    }

    const drawW = img.naturalWidth  * scale;
    const drawH = img.naturalHeight * scale;
    const srcX  = (cx + offset.x - drawW / 2) - 4;
    const srcY  = (cy + offset.y - drawH / 2) - 4;

    ctx.drawImage(img, srcX, srcY, drawW, drawH);

    out.toBlob(
      (blob) => { if (blob) onCrop(blob); },
      file.type,
      0.92,
    );
  };

  const shapeLabel = shape === 'circle' ? 'circular' : 'quadrado';

  return (
    <div className="crop-overlay">
      <div className="crop-modal">

        <div className="crop-modal__header">
          <h3>Reposicionar foto</h3>
          <button className="crop-modal__close" onClick={onClose} aria-label="Fechar">
            <CloseIcon />
          </button>
        </div>

        <p className="crop-modal__hint">
          Arraste para posicionar · scroll para aproximar/afastar
        </p>

        <div className="crop-modal__canvas-wrapper">
          <canvas
            ref={canvasRef}
            width={CANVAS_SIZE}
            height={CANVAS_SIZE}
            className={`crop-canvas crop-canvas--${shape}`}
            onMouseDown={onMouseDown}
            onTouchStart={onTouchStart}
            onTouchMove={onTouchMove}
            onTouchEnd={onTouchEnd}
            onWheel={onWheel}
            style={{ cursor: dragging.current ? 'grabbing' : 'grab' }}
          />
          {!ready && <div className="crop-modal__loading">Carregando imagem...</div>}
        </div>

        <div className="crop-modal__zoom">
          <ZoomOutIcon />
          <input
            type="range"
            min={MIN_SCALE * 100}
            max={MAX_SCALE * 100}
            value={Math.round(scale * 100)}
            onChange={e => setScale(Number(e.target.value) / 100)}
            className="crop-modal__zoom-slider"
            aria-label="Zoom"
          />
          <ZoomInIcon />
        </div>

        <div className="crop-modal__actions">
          <button className="crop-modal__btn crop-modal__btn--cancel" onClick={onClose}>
            Cancelar
          </button>
          <button
            className="crop-modal__btn crop-modal__btn--confirm"
            onClick={handleConfirm}
            disabled={!ready}
          >
            Usar recorte {shapeLabel}
          </button>
        </div>
      </div>
    </div>
  );
};

const CloseIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24"
    fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round"
    aria-hidden="true">
    <line x1="18" y1="6" x2="6" y2="18" /><line x1="6" y1="6" x2="18" y2="18" />
  </svg>
);

const ZoomInIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24"
    fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"
    aria-hidden="true">
    <circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/>
    <line x1="11" y1="8" x2="11" y2="14"/><line x1="8" y1="11" x2="14" y2="11"/>
  </svg>
);

const ZoomOutIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24"
    fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"
    aria-hidden="true">
    <circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/>
    <line x1="8" y1="11" x2="14" y2="11"/>
  </svg>
);

export default PhotoCropModal;