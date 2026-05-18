/* ============================================
   app.js — Shared Utilities (API, Toast, Confirm)
   ============================================ */
const API_BASE = 'http://localhost:8080/api';

// ── API Fetch Wrapper ──
async function apiFetch(endpoint, options = {}) {
  try {
    const url     = endpoint.startsWith('http') ? endpoint : `${API_BASE}${endpoint}`;
    const token   = typeof getToken === 'function' ? getToken() : null;
    const headers = { 'Content-Type': 'application/json', ...(options.headers || {}) };
    if (token) headers['Authorization'] = `Bearer ${token}`;
    const config  = { ...options, headers };
    const res     = await fetch(url, config);
    if (res.status === 401) { if (typeof logout === 'function') logout(); return; }
    if (res.status === 204) return null;
    const data = await res.json().catch(() => null);
    if (!res.ok) throw new Error(data?.message || data?.error || `Error ${res.status}`);
    return data;
  } catch (err) {
    if (err.message === 'Failed to fetch') showToast('Cannot connect to server. Is the backend running?', 'error');
    else showToast(err.message, 'error');
    throw err;
  }
}

// ── Toast Notifications ──
function ensureToastContainer() {
  let c = document.querySelector('.toast-container');
  if (!c) { c = document.createElement('div'); c.className = 'toast-container'; document.body.appendChild(c); }
  return c;
}
function showToast(msg, type = 'success') {
  const c = ensureToastContainer();
  const t = document.createElement('div');
  t.className = `toast toast-${type}`;
  t.innerHTML = `<span>${type === 'success' ? '✅' : '❌'}</span> ${msg}`;
  c.appendChild(t);
  setTimeout(() => { t.style.animation = 'toastOut .4s ease forwards'; setTimeout(() => t.remove(), 400); }, 3500);
}

// ── Confirm Dialog ──
function showConfirm(message, onConfirm) {
  const existing = document.querySelector('.confirm-overlay');
  if (existing) existing.remove();
  const overlay = document.createElement('div');
  overlay.className = 'confirm-overlay show';
  overlay.innerHTML = `
    <div class="confirm-box">
      <h3>⚠️ Confirm Action</h3>
      <p>${message}</p>
      <div class="confirm-actions">
        <button class="btn btn-secondary" id="confirmNo">Cancel</button>
        <button class="btn btn-danger" id="confirmYes">Delete</button>
      </div>
    </div>`;
  document.body.appendChild(overlay);
  overlay.querySelector('#confirmNo').onclick = () => overlay.remove();
  overlay.querySelector('#confirmYes').onclick = () => { overlay.remove(); onConfirm(); };
}

// ── Modal Helpers ──
function openModal(id) { document.getElementById(id).classList.add('show'); }
function closeModal(id) { document.getElementById(id).classList.remove('show'); }

// ── Form Helpers ──
function getFormData(formId) {
  const form = document.getElementById(formId);
  const data = {};
  const inputs = form.querySelectorAll('input, select, textarea');
  inputs.forEach(el => { if (el.name) data[el.name] = el.value; });
  return data;
}
function setFormData(formId, data) {
  const form = document.getElementById(formId);
  Object.entries(data).forEach(([key, value]) => {
    const el = form.querySelector(`[name="${key}"]`);
    if (el) el.value = value ?? '';
  });
}
function resetForm(formId) {
  document.getElementById(formId).reset();
}

// ── Animated Counter ──
function animateCounter(el, target) {
  let current = 0;
  const step = Math.max(1, Math.ceil(target / 40));
  const timer = setInterval(() => {
    current += step;
    if (current >= target) { current = target; clearInterval(timer); }
    el.textContent = current.toLocaleString();
  }, 30);
}

// ── Search Helper ──
function setupSearch(inputId, tableBodyId, columns) {
  const input = document.getElementById(inputId);
  if (!input) return;
  input.addEventListener('input', () => {
    const query = input.value.toLowerCase();
    const rows = document.querySelectorAll(`#${tableBodyId} tr`);
    rows.forEach(row => {
      const text = Array.from(row.querySelectorAll('td')).map(td => td.textContent.toLowerCase()).join(' ');
      row.style.display = text.includes(query) ? '' : 'none';
    });
  });
}

// ── Format helpers ──
function formatDate(d) { if (!d) return '—'; return new Date(d).toLocaleDateString('en-US', { year:'numeric', month:'short', day:'numeric' }); }
function formatMoney(n) { return '$' + parseFloat(n || 0).toFixed(2); }
function statusBadge(status) {
  const map = { ADMIN:'badge-primary', CUSTOMER:'badge-info', ACTIVE:'badge-success', RETURNED:'badge-info', OVERDUE:'badge-danger',
    COMPLETED:'badge-success', PENDING:'badge-warning', FAILED:'badge-danger', REFUNDED:'badge-info',
    WANT_TO_WATCH:'badge-warning', WATCHING:'badge-info', WATCHED:'badge-success', true:'badge-success', false:'badge-danger' };
  const cls = map[status] || 'badge-info';
  const label = String(status).replace(/_/g,' ');
  return `<span class="badge ${cls}">${label}</span>`;
}
