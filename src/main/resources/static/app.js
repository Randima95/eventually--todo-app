document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll(".alert.success").forEach((alert) => {
        window.setTimeout(() => {
            alert.style.transition = "opacity 0.35s ease";
            alert.style.opacity = "0";
            window.setTimeout(() => alert.remove(), 350);
        }, 3500);
    });
});