/* watchlist.js — CRUD for Watchlist Management */
let editingWatchlistId = null;

document.addEventListener('DOMContentLoaded', () => { loadWatchlist(); loadWatchlistDropdowns(); setupSearch('watchlistSearch','watchlistTableBody'); });

async function loadWatchlistDropdowns() {
  try {
    const [users, movies] = await Promise.all([apiFetch('/users'), apiFetch('/movies')]);
    const us = document.getElementById('watchlistUserSelect');
    us.innerHTML = '<option value="">— Select Customer —</option>' + users.map(u => `<option value="${u.id}">${u.fullName}</option>`).join('');
    const ms = document.getElementById('watchlistMovieSelect');
    ms.innerHTML = '<option value="">— Select Movie —</option>' + movies.map(m => `<option value="${m.id}">${m.title}</option>`).join('');
  } catch(e) {}
}

async function loadWatchlist() {
  try {
    const items = await apiFetch('/watchlist');
    document.getElementById('watchlistCount').textContent = items.length;
    const tbody = document.getElementById('watchlistTableBody');
    if (!items.length) { tbody.innerHTML = '<tr class="empty-row"><td colspan="6">Watchlist is empty.</td></tr>'; return; }
    tbody.innerHTML = items.map(w => `<tr>
      <td>${w.id}</td><td>${w.user?.fullName||'—'}</td><td>${w.movie?.title||'—'}</td>
      <td>${formatDate(w.addedDate)}</td><td>${statusBadge(w.status)}</td>
      <td class="action-btns">
        <button class="btn btn-sm btn-secondary" onclick="editWatchlist(${w.id})">✏️</button>
        <button class="btn btn-sm btn-danger" onclick="deleteWatchlist(${w.id})">🗑️</button>
      </td></tr>`).join('');
  } catch(e) { console.error(e); }
}

function openAddWatchlist() { editingWatchlistId = null; resetForm('watchlistForm'); document.getElementById('watchlistModalTitle').textContent = 'Add to Watchlist'; openModal('watchlistModal'); }

async function editWatchlist(id) {
  try {
    const w = await apiFetch(`/watchlist/${id}`);
    editingWatchlistId = id;
    document.getElementById('watchlistModalTitle').textContent = 'Edit Watchlist';
    document.getElementById('watchlistUserSelect').value = w.user?.id||'';
    document.getElementById('watchlistMovieSelect').value = w.movie?.id||'';
    setFormData('watchlistForm', { status:w.status });
    openModal('watchlistModal');
  } catch(e) {}
}

async function saveWatchlist() {
  const d = getFormData('watchlistForm');
  const userId = document.getElementById('watchlistUserSelect').value;
  const movieId = document.getElementById('watchlistMovieSelect').value;
  if (!userId || !movieId) { showToast('Customer and Movie are required','error'); return; }
  const body = { user:{id:parseInt(userId)}, movie:{id:parseInt(movieId)}, status:d.status||'WANT_TO_WATCH' };
  try {
    if (editingWatchlistId) await apiFetch(`/watchlist/${editingWatchlistId}`, { method:'PUT', body:JSON.stringify(body) });
    else await apiFetch('/watchlist', { method:'POST', body:JSON.stringify(body) });
    showToast(editingWatchlistId ? 'Watchlist updated!' : 'Added to watchlist!');
    closeModal('watchlistModal'); loadWatchlist();
  } catch(e) {}
}

function deleteWatchlist(id) {
  showConfirm('Remove from watchlist?', async () => {
    try { await apiFetch(`/watchlist/${id}`, { method:'DELETE' }); showToast('Removed from watchlist!'); loadWatchlist(); } catch(e) {}
  });
}
