import { useState } from "react";

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
    const msg =
      err.response?.data?.message ||
      err.response?.data?.error ||
      "Erro inesperado. Tente novamente.";
    setErrors((prev) => ({ ...prev, general: msg }));
  };

  const clearErrors = () => setErrors({});

  return { errors, validateAll, setBackendError, clearErrors };
}
