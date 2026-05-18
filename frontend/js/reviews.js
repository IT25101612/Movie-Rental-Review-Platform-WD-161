/* reviews.js — CRUD for Review Management */
let editingReviewId = null;

document.addEventListener('DOMContentLoaded', () => { loadReviews(); loadReviewDropdowns(); setupSearch('reviewSearch','reviewsTableBody'); });

async function loadReviewDropdowns() {
  try {
    const [users, movies] = await Promise.all([apiFetch('/users'), apiFetch('/movies')]);
    const us = document.getElementById('reviewUserSelect');
    us.innerHTML = '<option value="">— Select Customer —</option>' + users.map(u => `<option value="${u.id}">${u.fullName}</option>`).join('');
    const ms = document.getElementById('reviewMovieSelect');
    ms.innerHTML = '<option value="">— Select Movie —</option>' + movies.map(m => `<option value="${m.id}">${m.title}</option>`).join('');
  } catch(e) {}
}

async function loadReviews() {
  try {
    const reviews = await apiFetch('/reviews');
    document.getElementById('reviewCount').textContent = reviews.length;
    const tbody = document.getElementById('reviewsTableBody');
    if (!reviews.length) { tbody.innerHTML = '<tr class="empty-row"><td colspan="7">No reviews found.</td></tr>'; return; }
    tbody.innerHTML = reviews.map(r => {
      const stars = '⭐'.repeat(r.rating) + '☆'.repeat(5-r.rating);
      return `<tr>
      <td>${r.id}</td><td>${r.user?.fullName||'—'}</td><td>${r.movie?.title||'—'}</td>
      <td class="stars">${stars}</td><td>${r.comment||'—'}</td><td>${formatDate(r.reviewDate)}</td>
      <td class="action-btns">
        <button class="btn btn-sm btn-secondary" onclick="editReview(${r.id})">✏️</button>
        <button class="btn btn-sm btn-danger" onclick="deleteReview(${r.id})">🗑️</button>
      </td></tr>`;
    }).join('');
  } catch(e) { console.error(e); }
}

function openAddReview() { editingReviewId = null; resetForm('reviewForm'); document.getElementById('reviewModalTitle').textContent = 'Add Review'; openModal('reviewModal'); }

async function editReview(id) {
  try {
    const r = await apiFetch(`/reviews/${id}`);
    editingReviewId = id;
    document.getElementById('reviewModalTitle').textContent = 'Edit Review';
    document.getElementById('reviewUserSelect').value = r.user?.id||'';
    document.getElementById('reviewMovieSelect').value = r.movie?.id||'';
    setFormData('reviewForm', { rating:r.rating, comment:r.comment });
    openModal('reviewModal');
  } catch(e) {}
}

async function saveReview() {
  const d = getFormData('reviewForm');
  const userId = document.getElementById('reviewUserSelect').value;
  const movieId = document.getElementById('reviewMovieSelect').value;
  if (!userId || !movieId || !d.rating) { showToast('Fill all required fields','error'); return; }
  const body = { user:{id:parseInt(userId)}, movie:{id:parseInt(movieId)}, rating:parseInt(d.rating), comment:d.comment||null };
  try {
    if (editingReviewId) await apiFetch(`/reviews/${editingReviewId}`, { method:'PUT', body:JSON.stringify(body) });
    else await apiFetch('/reviews', { method:'POST', body:JSON.stringify(body) });
    showToast(editingReviewId ? 'Review updated!' : 'Review added!');
    closeModal('reviewModal'); loadReviews();
  } catch(e) {}
}

function deleteReview(id) {
  showConfirm('Delete this review?', async () => {
    try { await apiFetch(`/reviews/${id}`, { method:'DELETE' }); showToast('Review deleted!'); loadReviews(); } catch(e) {}
  });
}
