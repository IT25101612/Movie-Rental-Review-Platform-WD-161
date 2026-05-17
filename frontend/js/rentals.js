/* rentals.js — CRUD for Rental Management */
let editingRentalId = null;

document.addEventListener('DOMContentLoaded', () => { loadRentals(); loadDropdowns(); setupSearch('rentalSearch','rentalsTableBody'); });

async function loadDropdowns() {
  try {
    const [users, movies] = await Promise.all([apiFetch('/users'), apiFetch('/movies')]);
    const us = document.getElementById('rentalUserSelect');
    us.innerHTML = '<option value="">— Select Customer —</option>' + users.map(u => `<option value="${u.id}">${u.fullName} (${u.email})</option>`).join('');
    const ms = document.getElementById('rentalMovieSelect');
    ms.innerHTML = '<option value="">— Select Movie —</option>' + movies.map(m => `<option value="${m.id}">${m.title} (${m.releaseYear||''})</option>`).join('');
  } catch(e) {}
}

async function loadRentals() {
  try {
    const rentals = await apiFetch('/rentals');
    document.getElementById('rentalCount').textContent = rentals.length;
    const tbody = document.getElementById('rentalsTableBody');
    if (!rentals.length) { tbody.innerHTML = '<tr class="empty-row"><td colspan="8">No rentals found.</td></tr>'; return; }
    tbody.innerHTML = rentals.map(r => `<tr>
      <td>${r.id}</td><td>${r.user?.fullName||'—'}</td><td>${r.movie?.title||'—'}</td>
      <td>${formatDate(r.rentalDate)}</td><td>${formatDate(r.returnDate)}</td>
      <td>${statusBadge(r.status)}</td><td>${formatMoney(r.totalAmount)}</td>
      <td class="action-btns">
        <button class="btn btn-sm btn-secondary" onclick="editRental(${r.id})">✏️</button>
        <button class="btn btn-sm btn-danger" onclick="deleteRental(${r.id})">🗑️</button>
      </td></tr>`).join('');
  } catch(e) { console.error(e); }
}

function openAddRental() { editingRentalId = null; resetForm('rentalForm'); document.getElementById('rentalModalTitle').textContent = 'New Rental'; openModal('rentalModal'); }

async function editRental(id) {
  try {
    const r = await apiFetch(`/rentals/${id}`);
    editingRentalId = id;
    document.getElementById('rentalModalTitle').textContent = 'Edit Rental';
    document.getElementById('rentalUserSelect').value = r.user?.id||'';
    document.getElementById('rentalMovieSelect').value = r.movie?.id||'';
    setFormData('rentalForm', { rentalDate:r.rentalDate, returnDate:r.returnDate||'', status:r.status, totalAmount:r.totalAmount });
    openModal('rentalModal');
  } catch(e) {}
}

async function saveRental() {
  const d = getFormData('rentalForm');
  const userId = document.getElementById('rentalUserSelect').value;
  const movieId = document.getElementById('rentalMovieSelect').value;
  if (!userId || !movieId || !d.rentalDate || !d.totalAmount) { showToast('Fill all required fields','error'); return; }
  const body = { user:{id:parseInt(userId)}, movie:{id:parseInt(movieId)}, rentalDate:d.rentalDate, returnDate:d.returnDate||null, status:d.status||'ACTIVE', totalAmount:parseFloat(d.totalAmount) };
  try {
    if (editingRentalId) await apiFetch(`/rentals/${editingRentalId}`, { method:'PUT', body:JSON.stringify(body) });
    else await apiFetch('/rentals', { method:'POST', body:JSON.stringify(body) });
    showToast(editingRentalId ? 'Rental updated!' : 'Rental created!');
    closeModal('rentalModal'); loadRentals();
  } catch(e) {}
}

function deleteRental(id) {
  showConfirm('Delete this rental record?', async () => {
    try { await apiFetch(`/rentals/${id}`, { method:'DELETE' }); showToast('Rental deleted!'); loadRentals(); } catch(e) {}
  });
}
