  /**
   * validators.js
   * Porta todos os validators do backend (Java Bean Validation + customs) para o frontend.
   *
   * Cada função retorna:
   *   - null    → válido
   *   - string  → mensagem de erro
   *
   * Uso com useFormValidation:
   *   const schema = {
   *     nome:  [notBlank('Nome'), validationName()],
   *     email: [notBlank('E-mail'), email],
   *     cpf:   [notNull('CPF'), validCpf],
   *   };
   */

  /* ─────────────────────────────────────────────────────────────────────────────
    JAVA BEAN VALIDATION NATIVO
    ───────────────────────────────────────────────────────────────────────────── */

  /**
   * @NotNull — valor não pode ser null/undefined
   * Equivalente direto de @NotNull (não verifica string vazia, só null/undefined).
   */
  export const notNull =
    (label = "Campo") =>
    (value) =>
      value == null ? `${label} é obrigatório` : null;

  /**
   * @NotEmpty — não nulo e não string vazia ("")
   * Equivalente de @NotEmpty: rejeita null, undefined e "".
   * Não considera espaços em branco — para isso use notBlank.
   */
  export const notEmpty =
    (label = "Campo") =>
    (value) =>
      value == null || value === "" ? `${label} não pode ser vazio` : null;

  /**
   * @NotBlank — não nulo, não vazio e não composto só de espaços
   * Equivalente de @NotBlank: o mais restritivo dos três para strings.
   */
  export const notBlank =
    (label = "Campo") =>
    (value) =>
      !value?.toString().trim() ? `${label} é obrigatório` : null;

  /**
   * @Email — formato de e-mail válido
   * Obs: não verifica se o domínio existe, assim como o @Email do Bean Validation.
   */
  export const validationEmail = (value) =>
    value && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)
      ? "Insira um e-mail válido"
      : null;

  /**
   * @PositiveOrZero — número maior ou igual a zero
   */
  export const positiveOrZero =
    (label = "Campo") =>
    (value) => {
      const num = Number(value);
      return isNaN(num) || num < 0
        ? `${label} deve ser zero ou um valor positivo`
        : null;
    };

  /**
   * Torna qualquer regra opcional — só valida se o campo tiver valor.
   * Use para campos que têm @Positive, @Email etc. mas não têm @NotBlank no DTO.
   *
   * @example
   *   averageDeliveryTimeMinutes: [optional(positive('Tempo de entrega'))]
   */
  export const optional = (rule) => (value) =>
    value === '' || value == null ? null : rule(value);

  /**
   * @Positive — número maior que zero
   */
  export const positive =
    (label = "Campo") =>
    (value) => {
      const num = Number(value);
      return isNaN(num) || num <= 0
        ? `${label} deve ser um valor positivo`
        : null;
    };

  /* ─────────────────────────────────────────────────────────────────────────────
    VALIDATORS CUSTOMIZADOS — porta fiel das implementações Java
    ───────────────────────────────────────────────────────────────────────────── */

  /**
   * @ValidCpf
   * Porta de CpfValidator.java:
   *   - remove máscara (pontos e traços)
   *   - rejeita sequências repetidas (111.111.111-11)
   *   - valida os dois dígitos verificadores pelo algoritmo oficial
   *   - retorna null (válido) se o valor for null (deixa @NotNull cuidar disso)
   */
  export const validCpf = (value) => {
    if (!value) return null; // @NotNull cuida de null

    const cpf = value.replace(/[^0-9]/g, "");

    if (cpf.length !== 11) return "CPF inválido";
    if (/^(\d)\1{10}$/.test(cpf)) return "CPF inválido"; // sequências repetidas

    // Primeiro dígito verificador
    let sum = 0;
    for (let i = 0; i < 9; i++) sum += parseInt(cpf[i]) * (10 - i);
    let first = 11 - (sum % 11);
    if (first >= 10) first = 0;
    if (first !== parseInt(cpf[9])) return "CPF inválido";

    // Segundo dígito verificador
    sum = 0;
    for (let i = 0; i < 10; i++) sum += parseInt(cpf[i]) * (11 - i);
    let second = 11 - (sum % 11);
    if (second >= 10) second = 0;
    if (second !== parseInt(cpf[10])) return "CPF inválido";

    return null;
  };

  /**
   * @ValidationName
   * Porta de ValidationNameConfig.java:
   *   - min/max de tamanho (após trim)
   *   - bloqueia placeholders tipo {nome}
   *   - aceita apenas letras (incluindo acentuadas), espaço,
   *     hífen (se allowHyphen) e apóstrofo (se allowApostrophe)
   *
   * @param {object} options
   * @param {number}  options.min            — mínimo de caracteres (default: 2)
   * @param {number}  options.max            — máximo de caracteres (default: 100)
   * @param {boolean} options.allowHyphen    — permite hífen (default: true)
   * @param {boolean} options.allowApostrophe — permite apóstrofo (default: true)
   */
  export const validationName =
    ({ min = 2, max = 100, allowHyphen = true, allowApostrophe = true } = {}) =>
    (value) => {
      if (value == null) return null; // @NotNull cuida disso

      const cleaned = value.trim();

      if (cleaned.length === 0) return "Nome é obrigatório";
      if (cleaned.length < min)
        return `Nome deve ter no mínimo ${min} caracteres`;
      if (cleaned.length > max)
        return `Nome deve ter no máximo ${max} caracteres`;

      // Bloqueia placeholders tipo {nome}
      if (/^\{.*\}$/.test(cleaned)) return "Nome inválido";

      // Monta regex dinamicamente, igual ao Java
      let chars = "A-Za-zÀ-ÖØ-öø-ÿ ";
      if (allowHyphen) chars += "\\-";
      if (allowApostrophe) chars += "'";

      const pattern = new RegExp(`^[${chars}]+$`);

      return pattern.test(cleaned) ? null : "Nome contém caracteres inválidos";
    };

    /**
   * @ValidDisplayName
   * Para nomes "de exibição" — nome de usuário, nome de restaurante, etc.
   * Não representa identidade real, então é mais permissivo que validationName:
   * aceita letras, números, espaços e pontuação comum (evita só controle/spam).
   * Ainda assim bloqueia strings vazias e limita tamanho.
   */
  export const validDisplayName =
    ({ min = 2, max = 100 } = {}) =>
    (value) => {
      if (value == null) return null;
      const cleaned = value.trim();
      if (cleaned.length === 0) return "Nome é obrigatório";
      if (cleaned.length < min) return `Nome deve ter no mínimo ${min} caracteres`;
      if (cleaned.length > max) return `Nome deve ter no máximo ${max} caracteres`;
      // bloqueia caracteres de controle e emojis problemáticos, mas libera o resto
      if (/[\x00-\x1F\x7F]/.test(cleaned)) return "Nome contém caracteres inválidos";
      return null;
    };

  /**
   * @ValidPhone
   * Porta de PhoneValidator.java:
   *   - remove máscara (parênteses, traços, espaços)
   *   - aceita 10 dígitos (fixo) ou 11 dígitos (celular)
   *   - celular de 11 dígitos: terceiro caractere (após DDD) deve ser '9'
   *   - retorna null se o valor for null (deixa @NotNull cuidar disso)
   */
  export const validPhone = (value) => {
    if (!value) return null; // @NotNull cuida disso

    const phone = value.replace(/[^0-9]/g, "");

    if (phone.length !== 10 && phone.length !== 11)
      return "Telefone inválido. Use (XX) XXXX-XXXX ou (XX) 9XXXX-XXXX";

    if (phone.length === 11 && phone[2] !== "9")
      return "Número de celular inválido (deve começar com 9 após o DDD)";

    return null;
  };

  /**
   * @FileSize
   * Porta de FileSizeValidator.java:
   *   - aceita notação do Spring DataSize: "5MB", "500KB", "1GB", etc.
   *   - retorna null para arquivo null (comportamento idêntico ao Java)
   *
   * @param {string} max — tamanho máximo no formato "5MB", "500KB", "1GB"
   */
  export const fileSize = (max) => (file) => {
    if (!file) return null;

    const parseDataSize = (str) => {
      const match = str.trim().match(/^(\d+(?:\.\d+)?)\s*(B|KB|MB|GB)$/i);
      if (!match) throw new Error(`Tamanho inválido: "${str}"`);
      const units = { B: 1, KB: 1024, MB: 1024 ** 2, GB: 1024 ** 3 };
      return parseFloat(match[1]) * units[match[2].toUpperCase()];
    };

    const maxBytes = parseDataSize(max);
    return file.size > maxBytes ? `O arquivo deve ter no máximo ${max}` : null;
  };

  /**
   * @ValidPassword
   * Porta de PasswordValidator.java:
   *   - mínimo 8 caracteres
   *   - pelo menos 1 letra maiúscula
   *   - pelo menos 1 letra minúscula
   *   - pelo menos 1 caractere especial
   *   - retorna null se o valor for null/vazio (deixa @NotBlank cuidar disso)
   */
  export const validPassword = (value) => {
    if (!value) return null;

    if (value.length < 8) return "Senha deve ter no mínimo 8 caracteres";
    if (!/[A-Z]/.test(value)) return "Senha deve ter ao menos uma letra maiúscula";
    if (!/[a-z]/.test(value)) return "Senha deve ter ao menos uma letra minúscula";
    if (!/[^A-Za-z0-9]/.test(value)) return "Senha deve ter ao menos um caractere especial";

    return null;
  };

  /**
   * @FileType
   * Porta de FileTypeValidator.java:
   *   - recebe lista de MIME types permitidos
   *   - retorna null para arquivo null (comportamento idêntico ao Java)
   *
   * @param {string[]} allowed — ex: ['image/jpeg', 'image/png', 'image/webp']
   */
  export const fileType = (allowed) => (file) => {
    if (!file) return null;

    return allowed.includes(file.type)
      ? null
      : `Tipo de arquivo não permitido. Tipos aceitos: ${allowed.map((t) => t.split("/")[1].toUpperCase()).join(", ")}`;
  };

  /* ─────────────────────────────────────────────────────────────────────────────
    UTILITÁRIO — combina várias regras em sequência (retorna o primeiro erro)
    ───────────────────────────────────────────────────────────────────────────── */

  /**
   * Executa uma lista de regras em sequência e retorna o primeiro erro encontrado.
   * Útil fora do useFormValidation quando você quer validar um campo pontualmente.
   *
   * @example
   *   const erro = validate('   ', [notBlank('E-mail'), email]);
   *   // → 'E-mail é obrigatório'
   */
  export const validate = (value, rules) => {
    for (const rule of rules) {
      const error = rule(value);
      if (error) return error;
    }
    return null;
  };
