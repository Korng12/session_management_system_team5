const api = {
    sessions: '/admin/api/sessions',
    createSession: '/admin/sessions',
    users: '/admin/api/users',
    chairs: '/admin/api/users/chairs',
    rooms: '/admin/api/rooms',
    conferences: '/admin/api/conferences'
};

const form = document.getElementById('createSessionForm');
const sessionsBody = document.getElementById('sessions-body');
const formError = document.getElementById('formError');
const formSuccess = document.getElementById('formSuccess');

const chairSelect = document.getElementById('chairId');
const roomSelect = document.getElementById('roomId');
const conferenceSelect = document.getElementById('conferenceId');

const editChairSelect = document.getElementById('editChairId');
const editRoomSelect = document.getElementById('editRoomId');
const editConferenceSelect = document.getElementById('editConferenceId');

const assignChairSelect = document.getElementById('assignChairSelect');

async function loadOptions() {
    await Promise.all([
        fetchOptions(api.users, [chairSelect, editChairSelect], u => ({ value: u.id, label: `${u.name || 'User'} (${u.email})` })),
        fetchOptions(api.chairs, [assignChairSelect], u => ({ value: u.id, label: `${u.name || 'User'} (${u.email})` })),
        fetchOptions(api.rooms, [roomSelect, editRoomSelect], r => ({ value: r.id, label: `${r.name} (cap ${r.capacity})` })),
        fetchOptions(api.conferences, [conferenceSelect, editConferenceSelect], c => ({ value: c.id, label: c.title }))
    ]);
}

async function fetchOptions(url, selectEls, mapFn) {
    const res = await fetch(url);
    if (!res.ok) return;
    const data = await res.json();
    const targets = Array.isArray(selectEls) ? selectEls : [selectEls];
    targets.forEach(selectEl => {
        if (!selectEl) return;
        selectEl.innerHTML = '<option value="" disabled selected>Select...</option>';
        data.forEach(item => {
            const opt = document.createElement('option');
            const mapped = mapFn(item);
            opt.value = mapped.value;
            opt.textContent = mapped.label;
            selectEl.appendChild(opt);
        });
    });
}

async function loadSessions() {
    sessionsBody.innerHTML = '<tr><td colspan="8" class="text-center text-muted">Loading...</td></tr>';
    try {
        const res = await fetch(api.sessions);
        if (!res.ok) throw new Error('Failed to load sessions');
        const sessions = await res.json();
        if (!sessions.length) {
            sessionsBody.innerHTML = '<tr><td colspan="8" class="text-center text-muted">No sessions yet</td></tr>';
            return;
        }
        sessionsBody.innerHTML = '';
        sessions.forEach(s => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${s.title || ''}</td>
                <td>
                    <div class="d-flex gap-2 align-items-center">
                        <span>${s.chairName || '<em class="text-muted">No chair</em>'}</span>
                        ${s.chairId ? '<button class="btn btn-sm btn-outline-danger remove-chair-btn" title="Remove chair">✕</button>' : ''}
                    </div>
                </td>
                <td>${s.roomName || ''}</td>
                <td>${s.conferenceName || ''}</td>
                <td>${formatDateTime(s.startTime)}</td>
                <td>${formatDateTime(s.endTime)}</td>
                <td><span class="badge bg-secondary">${s.status || 'N/A'}</span></td>
                <td class="d-flex gap-1">
                    <button class="btn btn-sm btn-info assign-chair-btn">Assign Chair</button>
                    <button class="btn btn-sm btn-warning">Edit</button>
                    <button class="btn btn-sm btn-danger">Delete</button>
                </td>
            `;

            const assignChairBtn = tr.querySelector('.assign-chair-btn');
            const removeChairBtn = tr.querySelector('.remove-chair-btn');
            const [editBtn, deleteBtn] = tr.querySelectorAll('button:not(.assign-chair-btn):not(.remove-chair-btn)');
            
            assignChairBtn.addEventListener('click', () => openAssignChairModal(s.id, s.title || '', s.startTime, s.endTime));
            
            if (removeChairBtn) {
                removeChairBtn.addEventListener('click', () => openRemoveChairModal(s.id, s.title || '', s.chairName || ''));
            }
            
            editBtn.addEventListener('click', () => openEditModal(
                s.id,
                s.title || '',
                s.startTime,
                s.endTime,
                s.status || '',
                s.chairId || null,
                s.roomId || null,
                s.conferenceId || null
            ));
            deleteBtn.addEventListener('click', () => openDeleteModal(s.id, s.title || ''));

            sessionsBody.appendChild(tr);
        });

    } catch (e) {
        sessionsBody.innerHTML = '<tr><td colspan="8" class="text-danger text-center">Could not load sessions</td></tr>';
    }
}

function formatDateTime(val) {
    if (!val) return '';
    try {
        return new Date(val).toLocaleString();
    } catch (e) {
        return val;
    }
}

form.addEventListener('submit', async (e) => {
    e.preventDefault();
    hideAlerts();
    const payload = {
        title: document.getElementById('title').value.trim(),
        chairId: parseInt(chairSelect.value, 10),
        roomId: parseInt(roomSelect.value, 10),
        conferenceId: parseInt(conferenceSelect.value, 10),
        startTime: document.getElementById('startTime').value + ':00',
        endTime: document.getElementById('endTime').value + ':00',
        status: document.getElementById('status').value
    };

    try {
        const res = await fetch(api.createSession, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (!res.ok) {
            const err = await res.json().catch(() => ({}));
            showError(err.message || 'Failed to create session');
            return;
        }

        showSuccess('Session created');
        form.reset();
        loadSessions();
    } catch (error) {
        showError('Network error while creating session');
    }
});

function showError(msg) {
    formError.textContent = msg;
    formError.classList.remove('d-none');
}

function showSuccess(msg) {
    formSuccess.textContent = msg;
    formSuccess.classList.remove('d-none');
    setTimeout(() => formSuccess.classList.add('d-none'), 2500);
}

function hideAlerts() {
    formError.classList.add('d-none');
    formSuccess.classList.add('d-none');
}

let editModal, deleteModal;

function openEditModal(sessionId, title, startTime, endTime, status, chairId, roomId, conferenceId) {
    document.getElementById('editSessionId').value = sessionId;
    document.getElementById('editTitle').value = title;
    document.getElementById('editStartTime').value = startTime.substring(0, 16);
    document.getElementById('editEndTime').value = endTime.substring(0, 16);
    document.getElementById('editStatus').value = status;
    editChairSelect.value = chairId ? String(chairId) : '';
    editRoomSelect.value = roomId ? String(roomId) : '';
    editConferenceSelect.value = conferenceId ? String(conferenceId) : '';
    document.getElementById('editError').classList.add('d-none');
    editModal = new bootstrap.Modal(document.getElementById('editSessionModal'));
    editModal.show();
}

function openDeleteModal(sessionId, title) {
    document.getElementById('deleteSessionTitle').textContent = title;
    deleteModal = new bootstrap.Modal(document.getElementById('deleteSessionModal'));
    deleteModal._sessionId = sessionId;
    deleteModal.show();
}

document.getElementById('saveEditBtn')?.addEventListener('click', async () => {
    const sessionId = document.getElementById('editSessionId').value;
    const payload = {
        title: document.getElementById('editTitle').value.trim(),
        chairId: editChairSelect.value ? Number(editChairSelect.value) : null,
        roomId: editRoomSelect.value ? Number(editRoomSelect.value) : null,
        conferenceId: editConferenceSelect.value ? Number(editConferenceSelect.value) : null,
        startTime: document.getElementById('editStartTime').value + ':00',
        endTime: document.getElementById('editEndTime').value + ':00',
        status: document.getElementById('editStatus').value
    };
    try {
        const res = await fetch(`${api.createSession}/${sessionId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        if (!res.ok) {
            const errJson = await res.json().catch(() => ({}));
            const errText = !Object.keys(errJson).length ? await res.text().catch(() => '') : '';
            let details = '';
            if (errJson.errors && typeof errJson.errors === 'object') {
                const first = Object.values(errJson.errors)[0];
                details = Array.isArray(first) ? first[0] : String(first);
            }
            const msg = errJson.message || details || errText || `Failed to update (status ${res.status})`;
            document.getElementById('editError').textContent = msg;
            document.getElementById('editError').classList.remove('d-none');
            return;
        }
        editModal.hide();
        loadSessions();
        showSuccess('Session updated');
    } catch (error) {
        document.getElementById('editError').textContent = 'Network error';
        document.getElementById('editError').classList.remove('d-none');
    }
});

document.getElementById('confirmDeleteBtn')?.addEventListener('click', async () => {
    const sessionId = deleteModal?._sessionId;
    try {
        const res = await fetch(`${api.createSession}/${sessionId}`, {
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json' }
        });
        if (!res.ok) {
            const errJson = await res.json().catch(() => ({}));
            const errText = !Object.keys(errJson).length ? await res.text().catch(() => '') : '';
            const msg = errJson.message || errText || `Failed to delete (status ${res.status})`;
            showError(msg);
            return;
        }
        deleteModal.hide();
        loadSessions();
        showSuccess('Session deleted');
    } catch (error) {
        showError('Network error while deleting');
    }
});

function restoreSessionConfirm(sessionId, sessionTitle) {
    if (confirm(`Restore session "${sessionTitle}"?`)) {
        restoreSession(sessionId);
    }
}

async function restoreSession(sessionId) {
    try {
        const res = await fetch(`${api.createSession}/${sessionId}/restore`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' }
        });
        if (!res.ok) {
            const errJson = await res.json().catch(() => ({}));
            const errText = !Object.keys(errJson).length ? await res.text().catch(() => '') : '';
            const msg = errJson.message || errText || `Failed to restore (status ${res.status})`;
            showError(msg);
            return;
        }
        loadSessions();
        showSuccess('Session restored');
    } catch (error) {
        showError('Network error while restoring');
    }
}

// Search and Filter functionality
let allSessions = [];

function filterSessions() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();
    const statusFilter = document.getElementById('filterStatus').value;

    const filtered = allSessions.filter(s => {
        const matchesSearch = !searchTerm || 
            s.title.toLowerCase().includes(searchTerm) ||
            (s.chairName && s.chairName.toLowerCase().includes(searchTerm)) ||
            (s.roomName && s.roomName.toLowerCase().includes(searchTerm));
        
        const matchesStatus = !statusFilter || s.status === statusFilter;
        
        return matchesSearch && matchesStatus;
    });

    renderSessions(filtered);
}

function renderSessions(sessions) {
    if (!sessions.length) {
        sessionsBody.innerHTML = '<tr><td colspan="9" class="text-center text-muted">No sessions found</td></tr>';
        return;
    }

    sessionsBody.innerHTML = '';
    sessions.forEach(s => {
        const tr = document.createElement('tr');
        const deletedBadge = s.deleted 
            ? '<span class="badge bg-danger">Deleted</span>' 
            : '<span class="badge bg-success">Active</span>';
        
        tr.innerHTML = `
            <td>${s.title || ''}</td>
            <td>${s.chairName || ''}</td>
            <td>${s.roomName || ''}</td>
            <td>${s.conferenceName || ''}</td>
            <td>${formatDateTime(s.startTime)}</td>
            <td>${formatDateTime(s.endTime)}</td>
            <td><span class="badge bg-secondary">${s.status || 'N/A'}</span></td>
            <td>${deletedBadge}</td>
            <td class="d-flex gap-1">
                ${!s.deleted ? '<button class="btn btn-sm btn-warning edit-btn">Edit</button>' : ''}
                ${!s.deleted ? '<button class="btn btn-sm btn-danger delete-btn">Delete</button>' : '<button class="btn btn-sm btn-info restore-btn">Restore</button>'}
            </td>
        `;

        const editBtn = tr.querySelector('.edit-btn');
        const deleteBtn = tr.querySelector('.delete-btn');
        const restoreBtn = tr.querySelector('.restore-btn');

        if (editBtn) {
            editBtn.addEventListener('click', () => openEditModal(
                s.id,
                s.title || '',
                s.startTime,
                s.endTime,
                s.status || '',
                s.chairId || null,
                s.roomId || null,
                s.conferenceId || null
            ));
        }

        if (deleteBtn) {
            deleteBtn.addEventListener('click', () => openDeleteModal(s.id, s.title || ''));
        }

        if (restoreBtn) {
            restoreBtn.addEventListener('click', () => restoreSessionConfirm(s.id, s.title || ''));
        }

        sessionsBody.appendChild(tr);
    });
}

// Update loadSessions to use new rendering
const originalLoadSessions = loadSessions;
async function loadSessionsWithFilter() {
    sessionsBody.innerHTML = '<tr><td colspan="8" class="text-center text-muted">Loading...</td></tr>';
    try {
        const res = await fetch(api.sessions);
        if (!res.ok) throw new Error('Failed to load sessions');
        allSessions = await res.json();
        filterSessions();
    } catch (e) {
        sessionsBody.innerHTML = '<tr><td colspan="8" class="text-danger text-center">Could not load sessions</td></tr>';
    }
}

loadSessions = loadSessionsWithFilter;

// Assign Chair Modal
let assignChairModal, chairConflictDetailsModal;
let currentSessionStartTime, currentSessionEndTime;

function openAssignChairModal(sessionId, sessionTitle, startTime, endTime) {
    document.getElementById('assignSessionId').value = sessionId;
    document.getElementById('assignSessionTitle').textContent = sessionTitle;
    document.getElementById('assignSessionTime').textContent = `${formatDateTime(startTime)} - ${formatDateTime(endTime)}`;
    document.getElementById('assignChairError').classList.add('d-none');
    document.getElementById('chairConflictWarning').classList.add('d-none');
    document.getElementById('assignChairSelect').value = '';
    
    currentSessionStartTime = startTime;
    currentSessionEndTime = endTime;
    
    assignChairModal = new bootstrap.Modal(document.getElementById('assignChairModal'));
    assignChairModal.show();
}

// Real-time chair availability checking
document.getElementById('assignChairSelect')?.addEventListener('change', async (e) => {
    const chairId = e.target.value;
    const assignBtn = document.getElementById('confirmAssignChairBtn');
    
    if (!chairId) {
        document.getElementById('chairConflictWarning').classList.add('d-none');
        assignBtn.disabled = false;
        assignBtn.classList.remove('disabled');
        return;
    }
    
    try {
        const res = await fetch(`/admin/users/${chairId}/availability?startTime=${currentSessionStartTime}&endTime=${currentSessionEndTime}`);
        if (!res.ok) return;
        
        const data = await res.json();
        
        // If there are conflicts, show warning and disable assignment
        if (data.conflicts && data.conflicts.length > 0) {
            document.getElementById('chairConflictWarning').classList.remove('d-none');
            assignBtn.disabled = true;
            assignBtn.classList.add('disabled');
            assignBtn.innerHTML = '❌ Cannot Assign (Conflicts)';
            
            // Store conflict data for modal
            const chairName = document.getElementById('assignChairSelect').options[document.getElementById('assignChairSelect').selectedIndex].text;
            window.currentConflicts = {
                chairId: chairId,
                chairName: chairName,
                startTime: currentSessionStartTime,
                endTime: currentSessionEndTime,
                conflicts: data.conflicts
            };
        } else {
            document.getElementById('chairConflictWarning').classList.add('d-none');
            assignBtn.disabled = false;
            assignBtn.classList.remove('disabled');
            assignBtn.innerHTML = 'Assign Chair';
        }
    } catch (error) {
        console.error('Error checking chair availability:', error);
    }
});

// View conflict details
document.getElementById('viewConflictDetailsLink')?.addEventListener('click', (e) => {
    e.preventDefault();
    if (window.currentConflicts) {
        document.getElementById('conflictChairName').textContent = window.currentConflicts.chairName;
        document.getElementById('conflictSessionTime').textContent = 
            `${formatDateTime(window.currentConflicts.startTime)} - ${formatDateTime(window.currentConflicts.endTime)}`;
        
        const conflictList = document.getElementById('conflictSessionsList');
        conflictList.innerHTML = '';
        window.currentConflicts.conflicts.forEach(conflict => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${conflict.title || 'N/A'}</td>
                <td>${conflict.roomName || 'N/A'}</td>
                <td>${formatDateTime(conflict.startTime)}</td>
                <td>${formatDateTime(conflict.endTime)}</td>
            `;
            conflictList.appendChild(row);
        });
        
        chairConflictDetailsModal = new bootstrap.Modal(document.getElementById('chairConflictDetailsModal'));
        chairConflictDetailsModal.show();
    }
});

// Handle chair assignment
document.getElementById('confirmAssignChairBtn')?.addEventListener('click', async () => {
    const sessionId = document.getElementById('assignSessionId').value;
    const chairId = document.getElementById('assignChairSelect').value;
    
    if (!chairId) {
        document.getElementById('assignChairError').textContent = 'Please select a chair';
        document.getElementById('assignChairError').classList.remove('d-none');
        return;
    }
    
    try {
        const res = await fetch(`/admin/sessions/${sessionId}/assign-chair/${chairId}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' }
        });
        
        if (!res.ok) {
            const errJson = await res.json().catch(() => ({}));
            const msg = errJson.message || `Failed to assign chair (status ${res.status})`;
            document.getElementById('assignChairError').textContent = msg;
            document.getElementById('assignChairError').classList.remove('d-none');
            return;
        }
        
        assignChairModal.hide();
        loadSessions();
        showSuccess('Chair assigned successfully');
    } catch (error) {
        document.getElementById('assignChairError').textContent = 'Network error while assigning chair';
        document.getElementById('assignChairError').classList.remove('d-none');
    }
});

// Remove Chair Modal
let removeChairModal;
function openRemoveChairModal(sessionId, sessionTitle, chairName) {
    document.getElementById('removeChairSessionTitle').textContent = sessionTitle;
    document.getElementById('removeChairName').textContent = chairName;
    removeChairModal = new bootstrap.Modal(document.getElementById('removeChairModal'));
    removeChairModal._sessionId = sessionId;
    removeChairModal.show();
}

// Handle chair removal
document.getElementById('confirmRemoveChairBtn')?.addEventListener('click', async () => {
    const sessionId = removeChairModal?._sessionId;
    
    try {
        const res = await fetch(`/admin/sessions/${sessionId}/remove-chair`, {
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json' }
        });
        
        if (!res.ok) {
            const errJson = await res.json().catch(() => ({}));
            const msg = errJson.message || `Failed to remove chair (status ${res.status})`;
            showError(msg);
            return;
        }
        
        removeChairModal.hide();
        loadSessions();
        showSuccess('Chair removed successfully');
    } catch (error) {
        showError('Network error while removing chair');
    }
});

document.addEventListener('DOMContentLoaded', () => {
    loadOptions().then(loadSessions);
    
    // Add search and filter event listeners
    document.getElementById('searchInput').addEventListener('keyup', filterSessions);
    document.getElementById('filterStatus').addEventListener('change', filterSessions);
    document.getElementById('resetBtn').addEventListener('click', () => {
        document.getElementById('searchInput').value = '';
        document.getElementById('filterStatus').value = '';
        filterSessions();
    });
});
