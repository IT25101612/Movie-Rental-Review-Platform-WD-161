/* users.js — CRUD for User Management */
let editingUserId = null;

document.addEventListener('DOMContentLoaded', () => { loadUsers(); setupSearch('userSearch','usersTableBody'); });

async function loadUsers() {
  try {
    const users = await apiFetch('/users');
    document.getElementById('userCount').textContent = users.length;
    const tbody = document.getElementById('usersTableBody');
    if (!users.length) { tbody.innerHTML = '<tr class="empty-row"><td colspan="7">No users found.</td></tr>'; return; }
    tbody.innerHTML = users.map(u => `<tr>
      <td>${u.id}</td><td><strong>${u.fullName}</strong></td><td>${u.email}</td><td>${u.phone||'—'}</td>
      <td>${statusBadge(u.role)}</td><td>${formatDate(u.createdAt)}</td>
      <td class="action-btns">
        <button class="btn btn-sm btn-secondary" onclick="editUser(${u.id})">✏️</button>
        <button class="btn btn-sm btn-danger" onclick="deleteUser(${u.id},'${u.fullName}')">🗑️</button>
      </td></tr>`).join('');
  } catch(e) { console.error(e); }
}

function openAddUser() { editingUserId = null; resetForm('userForm'); document.getElementById('userModalTitle').textContent = 'Add User'; openModal('userModal'); }

async function editUser(id) {
  try {
    const u = await apiFetch(`/users/${id}`);
    editingUserId = id;
    document.getElementById('userModalTitle').textContent = 'Edit User';
    setFormData('userForm', { fullName: u.fullName, email: u.email, phone: u.phone, password: '', role: u.role });
    openModal('userModal');
  } catch(e) {}
}

async function saveUser() {
  const d = getFormData('userForm');
  if (!d.fullName || !d.email) { showToast('Name and Email are required','error'); return; }
  if (!editingUserId && !d.password) { showToast('Password is required for new users','error'); return; }
  const body = { fullName:d.fullName, email:d.email, phone:d.phone, password:d.password||'default123', role:d.role||'CUSTOMER' };
  try {
    if (editingUserId) await apiFetch(`/users/${editingUserId}`, { method:'PUT', body:JSON.stringify(body) });
    else await apiFetch('/users', { method:'POST', body:JSON.stringify(body) });
    showToast(editingUserId ? 'User updated!' : 'User created!');
    closeModal('userModal'); loadUsers();
  } catch(e) {}
}

function deleteUser(id, name) {
  showConfirm(`Delete user <strong>${name}</strong>?`, async () => {
    try { await apiFetch(`/users/${id}`, { method:'DELETE' }); showToast('User deleted!'); loadUsers(); } catch(e) {}
  });
}
