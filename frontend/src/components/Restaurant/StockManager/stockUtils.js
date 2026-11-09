// Utilitários do módulo de estoque.
// Regra: o backend trabalha SEMPRE na unidade base (g, ml ou un).
// A tela só converte (kg -> g, L -> ml) na digitação e na exibição.

export const UNITS = {
  G: { label: 'g', big: { label: 'kg', factor: 1000 } },
  ML: { label: 'ml', big: { label: 'L', factor: 1000 } },
  UN: { label: 'un', big: null },
};

export const UNIT_OPTIONS = [
  { value: 'G', label: 'Peso (g / kg)' },
  { value: 'ML', label: 'Volume (ml / L)' },
  { value: 'UN', label: 'Unidade (un)' },
];

/** 5000 + 'G' -> "5 kg" | 500 + 'G' -> "500 g" | 12 + 'UN' -> "12 un" */
export function formatQty(value, unit) {
  const v = Number(value) || 0;
  const u = UNITS[unit] || UNITS.UN;
  if (u.big && Math.abs(v) >= u.big.factor) {
    return `${(v / u.big.factor).toLocaleString('pt-BR', { maximumFractionDigits: 3 })} ${u.big.label}`;
  }
  return `${v.toLocaleString('pt-BR')} ${u.label}`;
}

/** Digitado (ex.: "2,5") x fator (1000 para kg) -> inteiro na unidade base. Retorna null se inválido. */
export function toBase(amount, factor) {
  const n = parseFloat(String(amount).replace(',', '.'));
  if (!Number.isFinite(n) || n < 0) return null;
  return Math.round(n * factor);
}

/** Valor base -> { amount, factor } para preencher o campo ao editar (2000 g -> 2 kg). */
export function splitBase(base, unit) {
  const u = UNITS[unit] || UNITS.UN;
  const n = Number(base);
  if (!n) return { amount: '', factor: 1 };
  if (u.big && n % u.big.factor === 0) {
    return { amount: String(n / u.big.factor), factor: u.big.factor };
  }
  return { amount: String(n), factor: 1 };
}

/**
 * Mensagem de erro para exibir ao usuário.
 * O backend devolve a mensagem de negócio em `detail` (o `userMessage` vem genérico).
 * Se o seu interceptor do axios já normaliza o erro, ajuste só esta função.
 */
export function getErrorMessage(err, fallback = 'Ocorreu um erro. Tente novamente.') {
  const data = err?.response?.data ?? err;
  return data?.detail || err?.detail || data?.userMessage || err?.userMessage || fallback;
}

/** Date -> "yyyy-mm-dd" (data local, para <input type="date">). */
export function toInputDate(date) {
  const pad = (n) => String(n).padStart(2, '0');
  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}`;
}
