'use server'

import { useState } from 'react'
import cookies  from 'next-cookies'  

import styles from '../../../styles/UserForm.module.css'

import Input from '../Forms/Input'
import SubmitButton from '../Forms/SubmitButton'
import Link from 'next/link'
import Email from '../../../public/email.svg'
import Password from '../../../public/password.svg'
import supabase from '../../../bd'


async function serverActions (user) {
    const email = user.email
    const password = user.password

    const resp = await supabase.from('usuarios').select('*').eq('email', email).eq('password', password)
    const userData = resp['data']
    if (!userData) {
        return {error: 'Erro ao encontrar usuário!'}
    }

    setCookie({
        name :  email,
        value : JSON.stringify(userData['email']),
        path : '/' ,
        httpOnly : true,
    }) 
}
export default function UserLoginForm () {

    const [user, setUser] = useState([]);

    function handleChange(e) {
        setUser({ ...user, [e.target.name]: e.target.value });
    }

    function handleSubmit (e) {
        e.preventDefault
        return serverActions(user)
    }

    return (
        <div className={styles.container_form}>
            <form className={styles.form} action={handleSubmit}>
                <h2> Faça Login com seu usuário </h2>
                <div className={styles.form_itens}>
                <Email 
                    width={25}
                    height={25}
                />
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
                <Password 
                        width={25}
                        height={25}
                    />
                <Input 
                    type='password'
                    placeholder='Senha'
                    name='password'
                    id='password'
                    // value={userData.password}
                    handleOnChange={handleChange}
                    />
                </div>

                <Link href='/cadastro' className={styles.link}> Não tem conta? Cadastre-se </Link>

                <SubmitButton customClass='container_btn_middle' text='Entrar'/>
            </form>
        </div>
        
    )
}