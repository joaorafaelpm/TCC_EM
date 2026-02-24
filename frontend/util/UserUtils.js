import axios from "axios";

// Gera o code_verifier usando a Web Crypto API nativa do browser
function generateCodeVerifier() {
  const array = new Uint8Array(32);
  window.crypto.getRandomValues(array);
  return btoa(String.fromCharCode(...array))
    .replace(/=/g, "")
    .replace(/\+/g, "-")
    .replace(/\//g, "_");
}

async function generateCodeChallenge(verifier) {
  const encoder = new TextEncoder();
  const data = encoder.encode(verifier);
  const digest = await window.crypto.subtle.digest("SHA-256", data);
  return btoa(String.fromCharCode(...new Uint8Array(digest)))
    .replace(/=/g, "")
    .replace(/\+/g, "-")
    .replace(/\//g, "_");
}

const CLIENT_ID = import.meta.env.VITE_CLIENT_ID;
const SCOPES = import.meta.env.VITE_SCOPES;
const API_URL = import.meta.env.VITE_API_URL;
const CLIENT_SECRET = import.meta.env.VITE_CLIENT_SECRET;
const API_REDIRECT_URL = import.meta.env.VITE_API_REDIRECT_URL;

export async function visitAuthorizationUrl() {
  const verifier = generateCodeVerifier();
  const challenge = await generateCodeChallenge(verifier);
  
  sessionStorage.setItem("code_verifier", verifier);

  const params = new URLSearchParams({
    response_type: "code",
    client_id: CLIENT_ID,
    redirect_uri: API_REDIRECT_URL,
    scope: SCOPES,
    code_challenge: challenge,
    code_challenge_method: "S256",
  });

  window.location.href = `http://localhost:80/oauth2/authorize?${params}`;
}

export async function getAccessToken(capturedCode) {
  const verifier = sessionStorage.getItem("code_verifier");

  const response = await axios.post(
    `${API_URL}/oauth2/token`,
    new URLSearchParams({
      grant_type: "authorization_code",
      code: capturedCode,
      redirect_uri: API_REDIRECT_URL,
      code_verifier: verifier,
    }),
    {
      auth: {
        username: CLIENT_ID,
        password: CLIENT_SECRET,
      },
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      },
    },
  );

  return response.data;
}
