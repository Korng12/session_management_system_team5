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
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    await Promise.all([
        fetchOptions(api.chairs, [chairSelect, editChairSelect], u => ({ value: u.id, label: `${u.name || 'User'} (${u.email})` })),
        fetchOptions(api.chairs, [assignChairSelect], u => ({ value: u.id, label: `${u.name || 'User'} (${u.email})` })),
        fetchOptions(api.rooms, [roomSelect, editRoomSelect], r => ({ value: r.id, label: `${r.name} (cap ${r.capacity})` })),
        fetchOptions(
            api.conferences,
            [conferenceSelect, editConferenceSelect],
            c => ({ value: c.id, label: c.title }),
            c => {
                if (!c) return false;
                if (!c.endDate) return true; // keep if no end date
                const end = new Date(c.endDate);
                if (Number.isNaN(end.getTime())) return true; // keep unparseable dates
                end.setHours(23, 59, 59, 999);
                return end >= today;
            }
        )
    ]);
}

async function fetchOptions(url, selectEls, mapFn, filterFn = () => true) {
    try {
        console.log('Fetching options from:', url);
        const res = await fetch(url, { credentials: 'include' });
        console.log('Response status:', res.status);
        
        if (!res.ok) {
            console.error('Failed to fetch options from', url, 'Status:', res.status);
            return;
        }
        
        const data = await res.json();
        const filtered = Array.isArray(data) ? data.filter(filterFn) : [];
        console.log('Data received from', url, ':', data);
        console.log('Data after filtering', url, ':', filtered);
        
        const targets = Array.isArray(selectEls) ? selectEls : [selectEls];
        targets.forEach(selectEl => {
            if (!selectEl) {
                console.warn('Select element not found');
                return;
            }
            
            // For chair selects in create and edit forms, start with "No chair assigned" option
            if (selectEl.id === 'chairId' || selectEl.id === 'editChairId') {
                selectEl.innerHTML = '<option value="">No chair assigned</option>';
            } else {
                selectEl.innerHTML = '<option value="" disabled selected>Select...</option>';
            }
            
            filtered.forEach(item => {
                const opt = document.createElement('option');
                const mapped = mapFn(item);
                opt.value = mapped.value;
                opt.textContent = mapped.label;
                selectEl.appendChild(opt);
            });
            
            console.log('Populated', selectEl.id, 'with', filtered.length, 'options');
        });
    } catch (error) {
        console.error('Error fetching options from', url, ':', error);
    }
}

async function loadSessions() {
    console.log('loadSessions called');
    sessionsBody.innerHTML = '<tr><td colspan="10" class="text-center text-muted">Loading...</td></tr>';
    try {
        console.log('Fetching sessions from:', api.sessions);
        const res = await fetch(api.sessions);
        console.log('Response status:', res.status);
        if (!res.ok) throw new Error('Failed to load sessions');
        const sessions = await res.json();
        console.log('Sessions loaded:', sessions.length, sessions);
        if (!sessions.length) {
            sessionsBody.innerHTML = '<tr><td colspan="10" class="text-center text-muted">No sessions yet</td></tr>';
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
                removeChairBtn.addEventListener('click', () => removeChairFromSession(s.id, s.chairName || '', s.title || ''));
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

// Create session button handler
document.getElementById('createSubmitBtn')?.addEventListener('click', async () => {
    hideAlerts();
    
    // Validate form
    const titleInput = document.getElementById('title');
    const roomInput = document.getElementById('roomId');
    const conferenceInput = document.getElementById('conferenceId');
    const startInput = document.getElementById('startTime');
    const endInput = document.getElementById('endTime');
    
    if (!titleInput.value.trim() || !roomInput.value || !conferenceInput.value || !startInput.value || !endInput.value) {
        showError('Please fill in all required fields');
        return;
    }
    
    const chairValue = chairSelect.value;
    const payload = {
        title: titleInput.value.trim(),
        chairId: chairValue ? parseInt(chairValue, 10) : null,
        roomId: parseInt(roomInput.value, 10),
        conferenceId: parseInt(conferenceInput.value, 10),
        startTime: startInput.value + ':00',
        endTime: endInput.value + ':00',
        status: document.getElementById('status').value
    };

    try {
        const res = await fetch(api.createSession, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify(payload)
        });

        if (!res.ok) {
            const err = await res.json().catch(() => ({}));
            const errorMsg = err.message || err.errors?.[Object.keys(err.errors)[0]]?.[0] || 'Failed to create session';
            showError(errorMsg);
            return;
        }

        showSuccess('Session created successfully');
        form.reset();
        const createModal = bootstrap.Modal.getInstance(document.getElementById('createSessionModal'));
        createModal?.hide();
        loadSessionsWithFilter();
    } catch (error) {
        showError('Network error while creating session');
    }
});

form.addEventListener('submit', async (e) => {
    e.preventDefault();
    document.getElementById('createSubmitBtn').click();
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
    try {
        const editModalEl = document.getElementById('editSessionModal');
        if (!editModalEl) {
            console.error('Edit modal element not found');
            showError('Error: Edit modal not found');
            return;
        }
        
        document.getElementById('editSessionId').value = sessionId;
        document.getElementById('editTitle').value = title;
        document.getElementById('editStartTime').value = startTime.substring(0, 16);
        document.getElementById('editEndTime').value = endTime.substring(0, 16);
        document.getElementById('editStatus').value = status;
        
        if (editChairSelect) {
            editChairSelect.value = chairId ? String(chairId) : '';
        }
        if (editRoomSelect) {
            editRoomSelect.value = roomId ? String(roomId) : '';
        }
        if (editConferenceSelect) {
            editConferenceSelect.value = conferenceId ? String(conferenceId) : '';
        }
        
        document.getElementById('editError').classList.add('d-none');
        
        if (typeof bootstrap === 'undefined') {
            console.error('Bootstrap is not loaded');
            showError('Error: Bootstrap library not loaded');
            return;
        }
        
        editModal = new bootstrap.Modal(editModalEl);
        editModal.show();
    } catch (error) {
        console.error('Error opening edit modal:', error);
        showError('Error opening edit modal: ' + error.message);
    }
}

function openDeleteModal(sessionId, title) {
    try {
        const deleteModalEl = document.getElementById('deleteSessionModal');
        if (!deleteModalEl) {
            console.error('Delete modal element not found');
            showError('Error: Delete modal not found');
            return;
        }
        
        const titleElement = document.getElementById('deleteSessionTitle');
        if (titleElement) {
            titleElement.textContent = title;
        }
        
        if (typeof bootstrap === 'undefined') {
            console.error('Bootstrap is not loaded');
            showError('Error: Bootstrap library not loaded');
            return;
        }
        
        deleteModal = new bootstrap.Modal(deleteModalEl);
        deleteModal._sessionId = sessionId;
        deleteModal.show();
    } catch (error) {
        console.error('Error opening delete modal:', error);
        showError('Error opening delete modal: ' + error.message);
    }
}



document.getElementById('saveEditBtn')?.addEventListener('click', async () => {
    const sessionId = document.getElementById('editSessionId').value;
    const editError = document.getElementById('editError');
    
    // Validate required fields
    const titleInput = document.getElementById('editTitle');
    const roomInput = document.getElementById('editRoomId');
    const conferenceInput = document.getElementById('editConferenceId');
    const startInput = document.getElementById('editStartTime');
    const endInput = document.getElementById('editEndTime');
    
    if (!titleInput.value.trim() || !roomInput.value || !conferenceInput.value || !startInput.value || !endInput.value) {
        editError.textContent = 'Please fill in all required fields';
        editError.classList.remove('d-none');
        return;
    }
    
    const payload = {
        title: titleInput.value.trim(),
        chairId: editChairSelect.value ? Number(editChairSelect.value) : null,
        roomId: Number(roomInput.value),
        conferenceId: Number(conferenceInput.value),
        startTime: startInput.value + ':00',
        endTime: endInput.value + ':00',
        status: document.getElementById('editStatus').value
    };
    
    try {
        const res = await fetch(`${api.createSession}/${sessionId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
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
            editError.textContent = msg;
            editError.classList.remove('d-none');
            return;
        }
        
        editModal.hide();
        loadSessionsWithFilter();
        showSuccess('Session updated successfully');
    } catch (error) {
        editError.textContent = 'Network error';
        editError.classList.remove('d-none');
    }
});

document.getElementById('confirmDeleteBtn')?.addEventListener('click', async () => {
    const sessionId = deleteModal?._sessionId;
    if (!sessionId) {
        showError('Invalid session ID');
        return;
    }
    
    try {
        const res = await fetch(`${api.createSession}/${sessionId}`, {
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include'
        });
        
        if (!res.ok) {
            const errJson = await res.json().catch(() => ({}));
            const errText = !Object.keys(errJson).length ? await res.text().catch(() => '') : '';
            const msg = errJson.message || errText || `Failed to delete (status ${res.status})`;
            showError(msg);
            return;
        }
        
        // Close modal and remove backdrop
        if (deleteModal) {
            deleteModal.hide();
            // Remove any lingering backdrops
            setTimeout(() => {
                const backdrops = document.querySelectorAll('.modal-backdrop');
                backdrops.forEach(backdrop => backdrop.remove());
                document.body.classList.remove('modal-open');
                document.body.style.removeProperty('overflow');
                document.body.style.removeProperty('padding-right');
            }, 100);
        }
        
        loadSessionsWithFilter();
        showSuccess('Session deleted successfully');
    } catch (error) {
        showError('Network error while deleting');
    }
});



// Search and Filter functionality
let allSessions = [];

function filterSessions() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();
    const statusFilter = document.getElementById('filterStatus').value;

    const filtered = allSessions.filter(s => {
        // Exclude deleted sessions
        if (s.deleted) {
            return false;
        }
        
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
        sessionsBody.innerHTML = '<tr><td colspan="10" class="text-center text-muted">No sessions found</td></tr>';
        return;
    }

    sessionsBody.innerHTML = '';
    sessions.forEach(s => {
        const tr = document.createElement('tr');
        const deletedBadge = s.deleted 
            ? '<span class="badge bg-danger">Deleted</span>' 
            : '<span class="badge bg-success">Active</span>';
        
        tr.innerHTML = `
            <td>${s.id || ''}</td>
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
                ${!s.deleted ? '<button class="btn btn-sm btn-danger delete-btn">Delete</button>' : ''}
            </td>
        `;

        const editBtn = tr.querySelector('.edit-btn');
        const deleteBtn = tr.querySelector('.delete-btn');

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

        sessionsBody.appendChild(tr);
    });
}

// Update loadSessions to use new rendering
const originalLoadSessions = loadSessions;
async function loadSessionsWithFilter() {
    console.log('loadSessionsWithFilter called');
    sessionsBody.innerHTML = '<tr><td colspan="10" class="text-center text-muted">Loading...</td></tr>';
    try {
        console.log('Fetching sessions from:', api.sessions);
        const res = await fetch(api.sessions);
        console.log('Response status:', res.status);
        if (!res.ok) throw new Error('Failed to load sessions');
        allSessions = await res.json();
        console.log('Sessions fetched:', allSessions.length, allSessions);
        filterSessions();
    } catch (e) {
        console.error('Error loading sessions:', e);
        sessionsBody.innerHTML = '<tr><td colspan="10" class="text-danger text-center">Could not load sessions</td></tr>';
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

// One-click chair removal with confirmation
async function removeChairFromSession(sessionId, chairName, sessionTitle) {
    if (!confirm(`Remove ${chairName} from "${sessionTitle}"?`)) {
        return;
    }
    
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
        
        loadSessions();
        showSuccess('Chair removed successfully');
    } catch (error) {
        showError('Network error while removing chair');
    }
}

document.addEventListener('DOMContentLoaded', () => {
    loadOptions().then(loadSessionsWithFilter);
    
    // Add search and filter event listeners
    const searchInput = document.getElementById('searchInput');
    const filterStatus = document.getElementById('filterStatus');
    
    if (searchInput) {
        searchInput.addEventListener('keyup', filterSessions);
    }
    if (filterStatus) {
        filterStatus.addEventListener('change', filterSessions);
    }
});
