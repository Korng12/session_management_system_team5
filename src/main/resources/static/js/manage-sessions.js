const api = {
    sessions: '/admin/api/sessions',
    createSession: '/admin/sessions',
    users: '/admin/api/users',
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

async function loadOptions() {
    await Promise.all([
        fetchOptions(api.users, [chairSelect, editChairSelect], u => ({ value: u.id, label: `${u.name || 'User'} (${u.email})` })),
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
                <td>${s.chairName || ''}</td>
                <td>${s.roomName || ''}</td>
                <td>${s.conferenceName || ''}</td>
                <td>${formatDateTime(s.startTime)}</td>
                <td>${formatDateTime(s.endTime)}</td>
                <td><span class="badge bg-secondary">${s.status || 'N/A'}</span></td>
                <td class="d-flex gap-1">
                    <button class="btn btn-sm btn-warning">Edit</button>
                    <button class="btn btn-sm btn-danger">Delete</button>
                </td>
            `;

            const [editBtn, deleteBtn] = tr.querySelectorAll('button');
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

document.addEventListener('DOMContentLoaded', () => {
    loadOptions().then(loadSessions);
});
