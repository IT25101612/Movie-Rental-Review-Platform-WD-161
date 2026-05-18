/* ============================================================
   auth.js — JWT Auth Utilities & Route Guards
   ============================================================ */

const TOKEN_KEY = 'mr_token';
const USER_KEY  = 'mr_user';

// ── Token Storage ──────────────────────────────────────────
function getToken()        { return localStorage.getItem(TOKEN_KEY); }
function setToken(token)   { localStorage.setItem(TOKEN_KEY, token); }
function removeToken()     { localStorage.removeItem(TOKEN_KEY); localStorage.removeItem(USER_KEY); }

// ── JWT Decode (no library needed) ────────────────────────
function parseJwt(token) {
  try {
    const base64Url = token.split('.')[1];
    const base64    = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const json      = decodeURIComponent(
      window.atob(base64).split('').map(c =>
        '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
      ).join('')
    );
    return JSON.parse(json);
  } catch (e) { return null; }
}

// ── Get current user from token ────────────────────────────
function getCurrentUser() {
  const cached = localStorage.getItem(USER_KEY);
  if (cached) { try { return JSON.parse(cached); } catch(_) {} }
  const token = getToken();
  if (!token) return null;
  const payload = parseJwt(token);
  if (!payload) return null;
  if (payload.exp && Date.now() / 1000 > payload.exp) { removeToken(); return null; }
  const user = { id: payload.id, email: payload.sub, name: payload.name, role: payload.role };
  localStorage.setItem(USER_KEY, JSON.stringify(user));
  return user;
}

function isAuthenticated() { return getCurrentUser() !== null; }
function hasRole(role)      { const u = getCurrentUser(); return u && u.role === role; }

// ── Logout ─────────────────────────────────────────────────
function logout() {
  removeToken();
  const root = _detectRoot();
  window.location.href = root + 'login.html';
}

// ── Route Guard ────────────────────────────────────────────
// Call at the top of any protected page.
// role: 'ADMIN' | 'CUSTOMER' | null (any authenticated user)
function requireAuth(role) {
  const user = getCurrentUser();
  const root = _detectRoot();
  if (!user) { window.location.replace(root + 'login.html'); return; }
  if (role && user.role !== role) {
    // Wrong role — redirect to their own dashboard
    if (user.role === 'ADMIN')    window.location.replace(root + 'admin-dashboard.html');
    else                          window.location.replace(root + 'customer-dashboard.html');
  }
}

// Detect the root path relative to the current page (handles /pages/ sub-folder)
function _detectRoot() {
  return window.location.pathname.includes('/pages/') ? '../' : './';
}

// ── Render logged-in user info in header ──────────────────
function renderUserInfo(containerId) {
  const user = getCurrentUser();
  const el   = document.getElementById(containerId);
  if (!el || !user) return;
  el.innerHTML = `
    <div class="user-info">
      <div class="user-avatar">${user.name.charAt(0).toUpperCase()}</div>
      <div class="user-meta">
        <div class="user-name">${user.name}</div>
        <div class="user-role">${user.role}</div>
      </div>
    </div>
    <button class="btn btn-secondary btn-sm" onclick="logout()">Logout</button>
  `;
}
