
document.getElementById("registerConferenceBtn")?.addEventListener("click", async function () {
    const conferenceId = this.dataset.conferenceId;

    try {
        const response = await fetch("/api/registrations/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
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

