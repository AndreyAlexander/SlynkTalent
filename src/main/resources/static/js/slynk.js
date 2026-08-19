(() => {
    "use strict";

    const body = document.body;
    const menuButton = document.querySelector("[data-mobile-menu]");
    const backdrop = document.querySelector("[data-mobile-backdrop]");

    const closeMobileMenu = () => body.classList.remove("mobile-menu-open");

    menuButton?.addEventListener("click", () => {
        body.classList.toggle("mobile-menu-open");
    });

    backdrop?.addEventListener("click", closeMobileMenu);

    document.querySelectorAll(".app-sidebar a").forEach(link => {
        link.addEventListener("click", closeMobileMenu);
    });

    document.querySelectorAll(".needs-validation").forEach(form => {
        form.addEventListener("submit", event => {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add("was-validated");
        });
    });

    document.querySelectorAll("[data-confirm-delete]").forEach(link => {
        link.addEventListener("click", event => {
            const message = link.dataset.confirmDelete || "Tem certeza que deseja excluir?";
            if (!window.confirm(message)) {
                event.preventDefault();
            }
        });
    });

    document.querySelectorAll("[data-table-search]").forEach(input => {
        const scope = input.closest(".app-card");
        const table = scope?.querySelector("[data-search-table]");
        const emptyRow = table?.querySelector("[data-search-empty]");

        if (!table) return;

        input.addEventListener("input", () => {
            const term = input.value.trim().toLocaleLowerCase("pt-BR");
            const rows = [...table.querySelectorAll("tbody tr")]
                .filter(row => !row.hasAttribute("data-search-empty"));

            let visible = 0;

            rows.forEach(row => {
                const isDynamicEmptyState = row.querySelector(".empty-state") && !row.querySelector("[th\\:each]");
                const matches = row.textContent.toLocaleLowerCase("pt-BR").includes(term);
                const shouldShow = !term || matches;

                if (!isDynamicEmptyState) {
                    row.classList.toggle("d-none", !shouldShow);
                    if (shouldShow) visible++;
                }
            });

            if (emptyRow) {
                emptyRow.classList.toggle("d-none", visible !== 0 || !term);
            }
        });
    });
})();
