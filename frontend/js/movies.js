/* movies.js — CRUD for Movie Management */
let editingMovieId = null;

document.addEventListener('DOMContentLoaded', () => { loadMovies(); setupSearch('movieSearch','moviesTableBody'); });

async function loadMovies() {
  try {
    const movies = await apiFetch('/movies');
    document.getElementById('movieCount').textContent = movies.length;
    const tbody = document.getElementById('moviesTableBody');
    if (!movies.length) { tbody.innerHTML = '<tr class="empty-row"><td colspan="8">No movies found.</td></tr>'; return; }
    tbody.innerHTML = movies.map(m => `<tr>
      <td>${m.id}</td><td><strong>${m.title}</strong></td><td>${m.genre||'—'}</td><td>${m.releaseYear||'—'}</td>
      <td>${formatMoney(m.rentalPrice)}</td><td>${statusBadge(m.available)}</td><td>${formatDate(m.createdAt)}</td>
      <td class="action-btns">
        <button class="btn btn-sm btn-secondary" onclick="editMovie(${m.id})">✏️</button>
        <button class="btn btn-sm btn-danger" onclick="deleteMovie(${m.id},'${m.title}')">🗑️</button>
      </td></tr>`).join('');
  } catch(e) { console.error(e); }
}

function openAddMovie() { editingMovieId = null; resetForm('movieForm'); document.getElementById('movieModalTitle').textContent = 'Add Movie'; openModal('movieModal'); }

async function editMovie(id) {
  try {
    const m = await apiFetch(`/movies/${id}`);
    editingMovieId = id;
    document.getElementById('movieModalTitle').textContent = 'Edit Movie';
    setFormData('movieForm', { title:m.title, genre:m.genre, releaseYear:m.releaseYear, rentalPrice:m.rentalPrice, available:String(m.available), description:m.description, posterUrl:m.posterUrl });
    openModal('movieModal');
  } catch(e) {}
}

async function saveMovie() {
  const d = getFormData('movieForm');
  if (!d.title || !d.rentalPrice) { showToast('Title and Price are required','error'); return; }
  const body = { title:d.title, genre:d.genre, description:d.description, releaseYear:d.releaseYear?parseInt(d.releaseYear):null, rentalPrice:parseFloat(d.rentalPrice), available:d.available==='true', posterUrl:d.posterUrl };
  try {
    if (editingMovieId) await apiFetch(`/movies/${editingMovieId}`, { method:'PUT', body:JSON.stringify(body) });
    else await apiFetch('/movies', { method:'POST', body:JSON.stringify(body) });
    showToast(editingMovieId ? 'Movie updated!' : 'Movie added!');
    closeModal('movieModal'); loadMovies();
  } catch(e) {}
}

function deleteMovie(id, title) {
  showConfirm(`Delete movie <strong>${title}</strong>?`, async () => {
    try { await apiFetch(`/movies/${id}`, { method:'DELETE' }); showToast('Movie deleted!'); loadMovies(); } catch(e) {}
  });
}
