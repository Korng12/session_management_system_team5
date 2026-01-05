document.addEventListener("DOMContentLoaded", () => {
  function showError(id, message) {
    const el = document.getElementById(id);
    if (el) el.textContent = message || "";
  }

  function clearErrors(ids) {
    ids.forEach((id) => showError(id, ""));
  }

  //  Strong email validation regex
  function validateEmail(email) {
    const regex =
      /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9-]+\.(com|net|org|edu|gov|io|co|kh)$/;
    return regex.test(email);
  }

  function validatePassword(password) {
    if (password.length < 8) return "Password must be at least 8 characters";
    if (!/[A-Za-z]/.test(password))
      return "Password must contain at least 1 letter";
    if (!/\d/.test(password)) return "Password must contain at least 1 number";
    return null;
  }

  //  Real-time email validation
  const emailInput = document.getElementById("email");
  if (emailInput) {
    emailInput.addEventListener("input", () => {
      const email = emailInput.value.trim();

      // only show error if user typed something
      if (email.length > 0 && !validateEmail(email)) {
        if (document.getElementById("emailError")) {
          showError("emailError", "Invalid email format");
        }
        if (document.getElementById("loginEmailError")) {
          showError("loginEmailError", "Invalid email format");
        }
      } else {
        if (document.getElementById("emailError")) showError("emailError", "");
        if (document.getElementById("loginEmailError"))
          showError("loginEmailError", "");
      }
    });
  }

  // ================= LOGIN =================
  const loginForm = document.getElementById("loginForm");
  if (loginForm) {
    loginForm.addEventListener("submit", async (e) => {
      e.preventDefault();

      clearErrors(["loginEmailError", "loginPasswordError"]);

      const email = document.getElementById("email").value.trim();
      const password = document.getElementById("password").value.trim();

      let valid = true;

      if (!email) {
        showError("loginEmailError", "Email is required");
        valid = false;
      } else if (!validateEmail(email)) {
        showError("loginEmailError", "Invalid email format");
        valid = false;
      }

      if (!password) {
        showError("loginPasswordError", "Password is required");
        valid = false;
      }

      if (!valid) return;

      const res = await fetch("/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({ email, password }),
      });

      if (!res.ok) {
        const errorData = await res.json().catch(() => null);

        if (errorData) {
          if (errorData.email) showError("loginEmailError", errorData.email);
          if (errorData.password)
            showError("loginPasswordError", errorData.password);
          if (errorData.message) alert(errorData.message);
        } else {
          alert("Login failed (wrong credentials)");
        }
        return;
      }

      window.location.href = "/home";
    });
  }

  // ================= REGISTER =================
  const registerForm = document.getElementById("registerForm");
  if (registerForm) {
    registerForm.addEventListener("submit", async (e) => {
      e.preventDefault();

      clearErrors(["nameError", "emailError", "passwordError"]);

      const name = document.getElementById("name").value.trim();
      const email = document.getElementById("email").value.trim();
      const password = document.getElementById("password").value.trim();

      let valid = true;

      //  Validate name
      if (!name) {
        showError("nameError", "Name is required");
        valid = false;
      } else if (name.length < 3) {
        showError("nameError", "Name must be at least 3 characters");
        valid = false;
      }

      //  Strong email validation
      if (!email) {
        showError("emailError", "Email is required");
        valid = false;
      } else if (!validateEmail(email)) {
        showError("emailError", "Invalid email format");
        valid = false;
      }

      //  Validate password
      const passwordError = validatePassword(password);
      if (!password) {
        showError("passwordError", "Password is required");
        valid = false;
      } else if (passwordError) {
        showError("passwordError", passwordError);
        valid = false;
      }

      if (!valid) return;

      const res = await fetch("/api/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({ name, email, password }),
      });

      if (!res.ok) {
        const text = await res.text();
        let errorData;

        try {
          errorData = JSON.parse(text);
        } catch {
          errorData = { message: text };
        }

        if (errorData.name) showError("nameError", errorData.name);
        if (errorData.email) showError("emailError", errorData.email);
        if (errorData.password) showError("passwordError", errorData.password);

        alert(errorData.message || "Registration failed");
        return;
      }

      window.location.href = "/home";
    });
  }
});
