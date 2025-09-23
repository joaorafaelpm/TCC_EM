'use server'

import { getIronSession } from "iron-session";
import { sessionOptions, SessionData, defaultSession } from "./lib";
import { cookies } from "next/headers";
import supabase from '../bd'

export const getSession = async () => {
    const session = await getIronSession<SessionData>(await cookies() , sessionOptions);

    if (!session.isLogged) {
        session.isLogged = defaultSession.isLogged;
    }

    return session;
};

export const loggin = async (formData: FormData) => {
    const session = await getSession();

    const formEmail = formData.get('email') as string
    const formPassword = formData.get('password') as string


    // CHEK USER
    const {data , error} = await supabase.from('usuarios').select('*').eq('email' , formEmail).eq('password' , formPassword)
    if (error) {
        console.log(error)
        return {error: 'Erro ao encontrar usuário!'}
    }
    
    const user = data['data']

    session.userId = user.id
    session.userName = user.complete_name
    session.isLogged = true
    session.isChef = false

    return session.save();
};

export const loggout = () => {};
