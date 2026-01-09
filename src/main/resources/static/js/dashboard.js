// Dashboard.js - Admin Dashboard functionality

// API endpoints
const API = {
    sessions: '/admin/api/sessions',
    registrations: '/admin/api/registrations'
};

// Utility function to format date/time
function formatDateTime(dateString) {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    const options = { 
        year: 'numeric', 
        month: 'short', 
        day: 'numeric', 
        hour: '2-digit', 
        minute: '2-digit' 
    };
    return date.toLocaleDateString('en-US', options);
}

// Load and display upcoming sessions
async function loadUpcomingSessions() {
    try {
        const response = await fetch(API.sessions);
        if (!response.ok) throw new Error('Failed to load sessions');
        
        const sessions = await response.json();
        
        // Filter upcoming sessions (not deleted, future start time)
        const now = new Date();
        const upcomingSessions = sessions
            .filter(s => !s.deleted && new Date(s.startTime) > now)
            .sort((a, b) => new Date(a.startTime) - new Date(b.startTime))
            .slice(0, 5); // Next 5 sessions
        
        const tbody = document.getElementById('upcoming-sessions-body');
        
        if (upcomingSessions.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" class="text-center text-muted">No upcoming sessions scheduled</td></tr>';
            return;
        }
        
        tbody.innerHTML = upcomingSessions.map(s => {
            const statusClass = s.status === 'SCHEDULED' ? 'bg-info' : 
                               s.status === 'ONGOING' ? 'bg-warning' : 
                               s.status === 'COMPLETED' ? 'bg-success' : 'bg-secondary';
            
            return `
                <tr>
                    <td><strong>${s.title || 'Untitled'}</strong></td>
                    <td>${s.chairName || '<em class="text-muted">No chair assigned</em>'}</td>
                    <td>${s.roomName || '<em class="text-muted">TBA</em>'}</td>
                    <td>${formatDateTime(s.startTime)}</td>
                    <td><span class="badge ${statusClass}">${s.status || 'N/A'}</span></td>
                </tr>
            `;
        }).join('');
        
    } catch (error) {
        console.error('Error loading upcoming sessions:', error);
        document.getElementById('upcoming-sessions-body').innerHTML = 
            '<tr><td colspan="5" class="text-center text-danger">Error loading sessions</td></tr>';
    }
}

// Load and display session statistics
async function loadSessionStatistics() {
    try {
        const response = await fetch(API.sessions);
        if (!response.ok) throw new Error('Failed to load sessions');
        
        const sessions = await response.json();
        
        // Calculate statistics
        const stats = {
            total: sessions.filter(s => !s.deleted).length,
            scheduled: sessions.filter(s => !s.deleted && s.status === 'SCHEDULED').length,
            ongoing: sessions.filter(s => !s.deleted && s.status === 'ONGOING').length,
            completed: sessions.filter(s => !s.deleted && s.status === 'COMPLETED').length,
            cancelled: sessions.filter(s => !s.deleted && s.status === 'CANCELLED').length,
            deleted: sessions.filter(s => s.deleted).length
        };
        
        // Update stat cards
        document.getElementById('stat-total').textContent = stats.total;
        document.getElementById('stat-scheduled').textContent = stats.scheduled;
        document.getElementById('stat-ongoing').textContent = stats.ongoing;
        document.getElementById('stat-completed').textContent = stats.completed;
        document.getElementById('stat-cancelled').textContent = stats.cancelled;
        
        // Calculate percentages for progress bars
        if (stats.total > 0) {
            const scheduledPct = (stats.scheduled / stats.total * 100).toFixed(1);
            const ongoingPct = (stats.ongoing / stats.total * 100).toFixed(1);
            const completedPct = (stats.completed / stats.total * 100).toFixed(1);
            const cancelledPct = (stats.cancelled / stats.total * 100).toFixed(1);
            
            document.getElementById('bar-scheduled').style.width = scheduledPct + '%';
            document.getElementById('bar-scheduled').textContent = scheduledPct + '%';
            
            document.getElementById('bar-ongoing').style.width = ongoingPct + '%';
            document.getElementById('bar-ongoing').textContent = ongoingPct + '%';
            
            document.getElementById('bar-completed').style.width = completedPct + '%';
            document.getElementById('bar-completed').textContent = completedPct + '%';
            
            document.getElementById('bar-cancelled').style.width = cancelledPct + '%';
            document.getElementById('bar-cancelled').textContent = cancelledPct + '%';
        }
        
    } catch (error) {
        console.error('Error loading session statistics:', error);
        document.getElementById('stat-total').textContent = '—';
        document.getElementById('stat-scheduled').textContent = '—';
        document.getElementById('stat-ongoing').textContent = '—';
        document.getElementById('stat-completed').textContent = '—';
        document.getElementById('stat-cancelled').textContent = '—';
    }
}

// Initialize dashboard on page load
document.addEventListener('DOMContentLoaded', () => {
    loadUpcomingSessions();
    loadSessionStatistics();
    loadUserActivities();
});

// Load and display user activities
async function loadUserActivities() {
    const list = document.getElementById('user-activities-list');
    try {
        // Fetch recent registrations and session changes (simulate for now)
        // In a real app, replace with a backend API endpoint like /admin/api/activities
        const [regRes, sessRes] = await Promise.all([
            fetch(API.registrations),
            fetch(API.sessions)
        ]);
        if (!regRes.ok || !sessRes.ok) throw new Error('Failed to load activities');
        const registrations = await regRes.json();
        const sessions = await sessRes.json();

        // Recent registrations (last 5)
        const recentRegs = registrations
            .sort((a, b) => new Date(b.registeredAt) - new Date(a.registeredAt))
            .slice(0, 5)
            .map(r => ({
                type: 'registration',
                time: r.registeredAt,
                user: r.userName,
                conference: r.conferenceTitle
            }));

        // Recent session status changes (last 5)
        const recentSess = sessions
            .filter(s => s.statusHistory && Array.isArray(s.statusHistory))
            .flatMap(s => s.statusHistory.map(h => ({
                type: 'session-status',
                time: h.changedAt,
                status: h.status,
                title: s.title
            })))
            .sort((a, b) => new Date(b.time) - new Date(a.time))
            .slice(0, 5);

        // Combine and sort all activities by time (latest first)
        const activities = [...recentRegs, ...recentSess]
            .sort((a, b) => new Date(b.time) - new Date(a.time))
            .slice(0, 8);

        if (activities.length === 0) {
            list.innerHTML = '<li class="list-group-item text-muted text-center">No recent activities</li>';
            return;
        }

        list.innerHTML = activities.map(act => {
            if (act.type === 'registration') {
                return `<li class="list-group-item">
                    <span class="badge bg-primary me-2">Registration</span>
                    <strong>${act.user}</strong> registered for <strong>${act.conference}</strong>
                    <span class="float-end text-muted small">${formatDateTime(act.time)}</span>
                </li>`;
            } else if (act.type === 'session-status') {
                return `<li class="list-group-item">
                    <span class="badge bg-info me-2">Session</span>
                    Status changed to <strong>${act.status}</strong> for <strong>${act.title}</strong>
                    <span class="float-end text-muted small">${formatDateTime(act.time)}</span>
                </li>`;
            }
            return '';
        }).join('');
    } catch (error) {
        console.error('Error loading user activities:', error);
        list.innerHTML = '<li class="list-group-item text-danger text-center">Error loading activities</li>';
    }
}
