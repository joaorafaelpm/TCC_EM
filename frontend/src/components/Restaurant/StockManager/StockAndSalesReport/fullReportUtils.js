import { formatQty } from '../stockUtils';

/**
 * Recorte de datas igual ao do relatório de vendas (dia inteiro em UTC),
 * para que vendas e estoque cubram exatamente o mesmo período.
 */
export function periodParams(startDate, endDate) {
  return {
    start: `${startDate}T00:00:00+00:00`,
    end: `${endDate}T23:59:59.999+00:00`,
  };
}

export const formatBRL = (n) =>
  new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(Number(n) || 0);

/** "2026-11-09" -> "09/11/2026" */
export function formatDateBR(iso) {
  if (!iso) return '—';
  const [y, m, d] = String(iso).slice(0, 10).split('-');
  return `${d}/${m}/${y}`;
}

export function formatDateTimeBR(date) {
  return date.toLocaleString('pt-BR', { dateStyle: 'short', timeStyle: 'short' });
}

/** Com sinal explícito: +500 g, −500 g, 0 g. */
export function formatSigned(value, unit) {
  const v = Number(value) || 0;
  if (v === 0) return formatQty(0, unit);
  return `${v > 0 ? '+' : '−'}${formatQty(Math.abs(v), unit)}`;
}
