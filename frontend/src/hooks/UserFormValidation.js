import { useState } from "react";

// Mapeia trechos do `detail` retornado pelo backend para o campo do form
// e a mensagem amigável que deve aparecer. Adicione novas entradas aqui
// conforme surgirem outras regras de negócio ligadas a campos específicos.
const BUSINESS_ERROR_FIELD_MAP = [
  {
    match: /email.*already in use/i,
    field: "email",
    message: "E-mail já cadastrado",
  },
  // exemplo futuro:
  // { match: /cpf.*already in use/i, field: "cpf", message: "CPF já cadastrado" },
];

export function useFormValidation(schema) {
  const [errors, setErrors] = useState({});

  // Valida todos os campos contra o schema
  const validateAll = (data) => {
    const newErrors = {};
    for (const [field, rules] of Object.entries(schema)) {
      const error = rules.reduce((acc, rule) => acc || rule(data[field]), null);
      if (error) newErrors[field] = error;
    }
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  // Adiciona um erro vindo do backend
  const setBackendError = (err) => {
    const data = err.response?.data;

    // RFC7807 problem detail (erro de negócio, validação, etc.)
    const detail = data?.detail;
    const isBusinessError = data?.type?.endsWith("erro-de-negocio");

    if (isBusinessError && detail) {
      const mapped = BUSINESS_ERROR_FIELD_MAP.find(({ match }) =>
        match.test(detail),
      );
      if (mapped) {
        setErrors((prev) => ({ ...prev, [mapped.field]: mapped.message }));
        return;
      }
    }

    // Fallback: mensagem genérica (título do problem detail, mensagem
    // legada, ou texto padrão) — nunca expõe o userMessage genérico do
    // backend pro campo específico, só pro erro geral do form.
    const msg =
      detail ||
      data?.title ||
      data?.message ||
      data?.error ||
      "Erro inesperado. Tente novamente.";
    setErrors((prev) => ({ ...prev, general: msg }));
  };

  const clearErrors = () => setErrors({});

  return { errors, validateAll, setBackendError, clearErrors };
}
