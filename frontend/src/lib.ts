import { SessionOptions } from "iron-session";

export interface SessionData {
    userId?:string;
    userName?:string;
    isLogged?:boolean;
    isChef?:boolean;
}

export const defaultSession:SessionData = {
    isLogged:false
}

export const sessionOptions:SessionOptions = {
    password: process.env.COOKIE_SECRET_KEY! ,
    cookieName : 'current_user',
    cookieOptions :{
        httpOnly : true,
        secure: process.env.NEXT_ENV === "production"
    }
}