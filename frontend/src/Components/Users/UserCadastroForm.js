import { useState } from 'react'

import styles from '../../../styles/UserForm.module.css'

import Input from '../Forms/Input'
import SubmitButton from '../Forms/SubmitButton'

export default function UserCadastroForm ({handleSubmit ,userData , handleChange}) {

    function submit (e) {
        e.preventDefault()
        handleSubmit(userData)
    }
    function verifyPassword () {
        
    }


    return (
        <div className={styles.container_form}>
            <form className={styles.form} onSubmit={submit}>
                <h2> Cadastre-se no nosso site! </h2>
                <div className={styles.form_itens}>
                <Input 
                    type='text'
                    placeholder='Insira seu CPF'
                    name='cpf'
                    id='cpf'
                    // value={userData.complete_name}
                    handleOnChange={handleChange}
                />

                </div>

                <div className={styles.form_itens}>
                
                
                <Input 
                    type='email'
                    placeholder='Email'
                    name='email'
                    id='email'
                    // value={userData.email}
                    handleOnChange={handleChange}
                />
                </div>
                
                <div className={styles.form_itens}>
                
                <Input 
                    type='text'
                    placeholder='Nome Completo'
                    name='complete_name'
                    id='complete_name'
                    // value={userData.complete_name}
                    handleOnChange={handleChange}
                    />
                </div>

                <div className={styles.form_itens}>
                <Input 
                    type='password'
                    placeholder='Senha'
                    name='password'
                    id='password'
                    // value={userData.password}
                    handleOnChange={handleChange}
                    />
                </div>

                <div className={styles.form_itens}>
                <Input 
                    type='password'
                    placeholder='Confirme sua Senha'
                    name='password_check'
                    id='password_check'
                    handleOnChange={verifyPassword}
                    />
                </div>
                <div className={styles.form_itens}>
                <Input 
                type='date'
                name='born_date'
                id='born_date'
                // value={userData.born_date}
                handleOnChange={handleChange}
                />

                </div>

                <SubmitButton customClass='container_btn_middle' text='Entrar'/>
            </form>
        </div>
        
    )
}