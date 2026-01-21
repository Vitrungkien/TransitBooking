
// Utility Functions
// Utility Functions
function calculateTimeDifference(startTime, endTime) {
    if (!startTime || !endTime) return '';

    function parseToSeconds(timeInput) {
        if (!timeInput) return 0;
        let h = 0, m = 0, s = 0;

        if (Array.isArray(timeInput)) {
            // [hour, minute, second]
            h = timeInput[0] || 0;
            m = timeInput[1] || 0;
            s = timeInput[2] || 0;
        } else if (typeof timeInput === 'string') {
            const parts = timeInput.split(':').map(Number);
            h = parts[0] || 0;
            m = parts[1] || 0;
            s = parts[2] || 0;
        }
        return h * 3600 + m * 60 + s;
    }

    const startTimeInSeconds = parseToSeconds(startTime);
    const endTimeInSeconds = parseToSeconds(endTime);

    let timeDifferenceInSeconds = endTimeInSeconds - startTimeInSeconds;

    if (timeDifferenceInSeconds < 0) {
        timeDifferenceInSeconds += 24 * 3600;
    }

    const hours = Math.floor(timeDifferenceInSeconds / 3600);
    const minutes = Math.floor((timeDifferenceInSeconds % 3600) / 60);

    return `${hours} Giờ ${minutes} phút`;
}

function formatTime(timeInput) {
    if (!timeInput) return '';
    let h = 0, m = 0;
    if (Array.isArray(timeInput)) {
        h = timeInput[0];
        m = timeInput[1];
    } else if (typeof timeInput === 'string') {
        const parts = timeInput.split(':');
        h = parseInt(parts[0]);
        m = parseInt(parts[1]);
    } else {
        return '';
    }
    return (h < 10 ? '0' + h : h) + ':' + (m < 10 ? '0' + m : m);
}

function displayValue(value) {
    return (value !== null && value !== undefined) ? value : 'null';
}

// Global UI Helper Functions (can be used inline or via event delegation)
// Used in main.html and management.html text generation

function showNoticeList(id) {
    const el = document.getElementById('noticeList' + id);
    if (el) {
        // Toggle or Show?
        // main.html line 217 says: if block then none, else block. (Toggle)
        // management.html line 830 says: display = block. (Show)
        // main.html uses onclick="showNoticeList" (Toggle)
        // management.html uses onmouseover="showNoticeList" (Show) and onmouseout="hideNoticeList".

        // We need to support both or split.
        // If it's a hover, show. If Click, toggle.
        // Check if event is passed? No.

        // Let's implement generic toggle if forced, or stick to simple show/hide and let logic decide.
        // Since main.html line 217 toggles:
        /*
        if (noticeList.style.display == 'block') { noticeList.style.display = 'none'; }
        else { noticeList.style.display = 'block'; }
        */

        // If I make showNoticeList JUST SHOW, main.html Click will break (it will only open).
        // I will rename main.html function to toggleNoticeList() later?
        // Or check element style here.

        // But management.html also calls it.
        // management.html: onmouseover="showNoticeList()" -> Wants SHOW.

        // So main.html definition is conflicting with management.html definition if we merge.
        // I will implement `showNoticeList` as SHOW, and `toggleNoticeList` as TOGGLE.
        // And update main.html to use `toggleNoticeList`.
        el.style.display = 'block';
    }
}

function hideNoticeList(id) {
    const el = document.getElementById('noticeList' + id);
    if (el) el.style.display = 'none';
}

function toggleNoticeList(id) {
    const el = document.getElementById('noticeList' + id);
    if (el) {
        if (el.style.display === 'block') {
            el.style.display = 'none';
        } else {
            el.style.display = 'block';
        }
    }
}

// Cookie & Auth Utilities
function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}

function handleLogout() {
    fetch('/logout', {
        method: 'POST',
        credentials: 'same-origin'
    })
        .then(response => {
            if (response.ok) {
                window.location.href = '/';
            } else {
                console.error('Logout failed:', response.status);
                if (typeof ToastService !== 'undefined') ToastService.error("Đăng xuất thất bại");
            }
        })
        .catch(error => {
            console.error('Error during logout:', error);
            if (typeof ToastService !== 'undefined') ToastService.error("Lỗi khi đăng xuất");
        });
}

function checkAuthUI() {
    const authToken = getCookie('Authorization');
    const meElement = document.getElementById('me');
    const loginElement = document.getElementById('login');

    if (!authToken) {
        if (meElement) meElement.style.display = 'none';
        if (loginElement) loginElement.style.display = 'block';
    } else {
        if (meElement) meElement.style.display = 'block';
        if (loginElement) loginElement.style.display = 'none';
    }
}

$(document).ready(function () {
    // Setup generic logout button listener
    const logoutBtn = document.getElementById('logoutButton');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', function (e) {
            e.preventDefault();
            handleLogout();
        });
    }

    // Check Auth UI on load
    checkAuthUI();
});