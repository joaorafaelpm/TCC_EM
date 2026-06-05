import './Input.css';

export default function Input({ name, type, placeholder, value, onChange, error, onBlur }) {
  return (
    <div className="field-wrapper">
      <input
        type={type}
        id={name}
        name={name}
        placeholder={placeholder}
        value={value}
        onChange={onChange}
        className={"field-input" + (error ? " is-invalid" : "")}
        onBlur={onBlur} // Para disparar validação de CEP ao sair do campo
      />
    {error && <span className="global-field-error">{error}</span>}
      
    </div>
  );
}