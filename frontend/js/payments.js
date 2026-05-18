/* payments.js — CRUD for Payment Management */
let editingPaymentId = null;

document.addEventListener('DOMContentLoaded', () => { loadPayments(); loadPaymentDropdowns(); setupSearch('paymentSearch','paymentsTableBody'); });

async function loadPaymentDropdowns() {
  try {
    const rentals = await apiFetch('/rentals');
    const sel = document.getElementById('paymentRentalSelect');
    sel.innerHTML = '<option value="">— Select Rental —</option>' + rentals.map(r => `<option value="${r.id}">Rental #${r.id} — ${r.user?.fullName||'?'} → ${r.movie?.title||'?'}</option>`).join('');
  } catch(e) {}
}

async function loadPayments() {
  try {
    const payments = await apiFetch('/payments');
    document.getElementById('paymentCount').textContent = payments.length;
    const tbody = document.getElementById('paymentsTableBody');
    if (!payments.length) { tbody.innerHTML = '<tr class="empty-row"><td colspan="8">No payments found.</td></tr>'; return; }
    tbody.innerHTML = payments.map(p => `<tr>
      <td>${p.id}</td><td>Rental #${p.rental?.id||'—'}</td><td>${formatMoney(p.amount)}</td>
      <td>${(p.paymentMethod||'').replace(/_/g,' ')}</td><td>${statusBadge(p.paymentStatus)}</td>
      <td>${formatDate(p.paymentDate)}</td><td><code>${p.transactionId||'—'}</code></td>
      <td class="action-btns">
        <button class="btn btn-sm btn-secondary" onclick="editPayment(${p.id})">✏️</button>
        <button class="btn btn-sm btn-danger" onclick="deletePayment(${p.id})">🗑️</button>
      </td></tr>`).join('');
  } catch(e) { console.error(e); }
}

function openAddPayment() { editingPaymentId = null; resetForm('paymentForm'); document.getElementById('paymentModalTitle').textContent = 'Record Payment'; openModal('paymentModal'); }

async function editPayment(id) {
  try {
    const p = await apiFetch(`/payments/${id}`);
    editingPaymentId = id;
    document.getElementById('paymentModalTitle').textContent = 'Edit Payment';
    document.getElementById('paymentRentalSelect').value = p.rental?.id||'';
    setFormData('paymentForm', { amount:p.amount, paymentMethod:p.paymentMethod, paymentStatus:p.paymentStatus, transactionId:p.transactionId });
    openModal('paymentModal');
  } catch(e) {}
}

async function savePayment() {
  const d = getFormData('paymentForm');
  const rentalId = document.getElementById('paymentRentalSelect').value;
  if (!rentalId || !d.amount) { showToast('Rental and Amount are required','error'); return; }
  const body = { rental:{id:parseInt(rentalId)}, amount:parseFloat(d.amount), paymentMethod:d.paymentMethod, paymentStatus:d.paymentStatus||'PENDING', transactionId:d.transactionId||null };
  try {
    if (editingPaymentId) await apiFetch(`/payments/${editingPaymentId}`, { method:'PUT', body:JSON.stringify(body) });
    else await apiFetch('/payments', { method:'POST', body:JSON.stringify(body) });
    showToast(editingPaymentId ? 'Payment updated!' : 'Payment recorded!');
    closeModal('paymentModal'); loadPayments();
  } catch(e) {}
}

function deletePayment(id) {
  showConfirm('Delete this payment record?', async () => {
    try { await apiFetch(`/payments/${id}`, { method:'DELETE' }); showToast('Payment deleted!'); loadPayments(); } catch(e) {}
  });
}
