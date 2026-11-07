UPDATE oauth2_registered_client
SET redirect_uris = 'http://localhost:5173/redirect,http://localhost:80/redirect,https://tcc-em.vercel.app/redirect'
WHERE client_id IN ('pendezzapizza-web', 'pendezzapizza-end', 'pendezzapizza-commom-client');