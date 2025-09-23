import { env } from 'process';


/** @type {import('next').NextConfig} */
const nextConfig = {
  
  

  webpack(config) {
    config.module.rules.push({
      test: /\.svg$/,
      use: ['@svgr/webpack'],
    });
    config.module.rules.push({
      test: /\.(png|jpe?g|gif)$/i,
      use: [{
          loader:'file-loader',
        }],
    });
    

    return config;
  },
  env : {
    SUPABASE_URL : 'https://jgypwhjjlrvxloydjano.supabase.co',
    SUPABASE_KEY : 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImpneXB3aGpqbHJ2eGxveWRqYW5vIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzU1ODk0NzcsImV4cCI6MjA1MTE2NTQ3N30.ZZDSXk0xJ7W-mqrrHKkmfzzge9m4PHWsErsIZxwRD1Y',
    COOKIE_SECRET_KEY : 'nPwoDCDEFRzL7rOz28t6b7F+Ikd3s0HtMWsXUH9fbIA=',
    NEXT_ENV : 'development'
  },
};




export default nextConfig
