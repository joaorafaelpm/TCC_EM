export const fmt = (n) =>
  new Intl.NumberFormat("pt-BR", { style: "currency", currency: "BRL" }).format(
    n,
  );

export const fmtDate = (iso) => {
  const [, m, d] = iso.split("-");
  return `${d}/${m}`;
};

const toInputDate = (date) => date.toISOString().split("T")[0];

export const today = () => toInputDate(new Date());

export const daysAgo = (n) => {
  const d = new Date();
  d.setDate(d.getDate() - n);
  return toInputDate(d);
};
