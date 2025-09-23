import styles from '../../../styles/Input.module.css'

export default function Input ({ type, text, name, placeholder, handleOnChange, value}) {
    return (
      <div className={styles.form_control}>
        <label htmlFor={name}>{text}</label>
        <input
          type={type}
          placeholder={placeholder}
          name={name}
          id={name}
          onChange={handleOnChange}
          value={value}
          required
        />
      </div>
    )}
