
document.getElementById("registerConferenceBtn")?.addEventListener("click", async function () {
    const conferenceId = this.dataset.conferenceId;

    try {
        const response = await fetch("/api/registrations/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            credentials: "include",
            body: new URLSearchParams({
                conferenceId: conferenceId
            })
        });

        if (!response.ok) {
            const msg = await response.text();
            alert("Hey sth when wrong"+msg);
            return;
        }

        alert("Successfully registered for conference!");
        location.reload();

    } catch (err) {
        alert("Network error");
    }
});

// Cancel conference registration
document.getElementById("cancelConferenceBtn")?.addEventListener("click", async function () {
    const registrationId = this.dataset.registrationId;

    if (!registrationId) {
        alert("Cancel failed: missing registration id");
        return;
    }

    try {
        const response = await fetch(`/api/registrations/${registrationId}/cancel`, {
            method: "PUT",
            credentials: "include"
        });

        if (!response.ok) {
            const msg = await response.text();
            alert("Cancel failed: " + msg);
            return;
        }

        alert("Registration cancelled successfully");
        location.reload();

    } catch (err) {
        alert("Network error");
    }
});

