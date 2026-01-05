document.addEventListener("DOMContentLoaded", function () {

    const logoutForm = document.getElementById("logoutForm");

    if (logoutForm) {
        logoutForm.addEventListener("submit", async function (e) {
            e.preventDefault();
            console.log("Logging out...");

            await fetch("/api/auth/logout", {
                method: "POST"
            });

            window.location.href = "/";
        });
    }

});
