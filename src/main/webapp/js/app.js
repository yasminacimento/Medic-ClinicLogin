"use strict";
document.documentElement.classList.add("js");
document.addEventListener("DOMContentLoaded", () => {
    const normalize = value => value.normalize("NFD").replace(/[\u0300-\u036f]/g, "").toLocaleLowerCase("pt-BR");
    const nav = document.querySelector(".app-navigation");
    const toggle = document.querySelector(".nav-toggle");
    toggle?.addEventListener("click", () => {
        const opened = nav.classList.toggle("is-open");
        toggle.setAttribute("aria-expanded", String(opened));
    });
    document.querySelectorAll("[data-nav-link]").forEach(link => {
        if (new URL(link.href).pathname === location.pathname) {
            link.classList.add("active");
            link.setAttribute("aria-current", "page");
            link.closest(".nav-group")?.classList.add("active");
        }
    });
    document.querySelectorAll(".nav-group").forEach(group => group.addEventListener("toggle", () => {
        if (group.open) document.querySelectorAll(".nav-group").forEach(other => { if (other !== group) other.open = false; });
    }));
    document.addEventListener("click", event => {
        document.querySelectorAll(".nav-group[open]").forEach(group => { if (!group.contains(event.target)) group.open = false; });
    });
    document.addEventListener("keydown", event => {
        if (event.key === "Escape") document.querySelectorAll(".nav-group[open]").forEach(group => { group.open = false; group.querySelector("summary").focus(); });
    });
    document.querySelectorAll(".form-group").forEach((group, index) => {
        const field = group.querySelector("select") || group.querySelector("input:not([type=hidden]), textarea");
        const label = group.querySelector("label");
        if (!field || !label) return;
        if (!field.id) field.id = "campo-" + (field.name || "form") + "-" + index;
        label.htmlFor = field.id;
        if (field.required) {
            const mark = document.createElement("span");
            mark.className = "required-mark";
            mark.textContent = " *";
            mark.setAttribute("aria-hidden", "true");
            label.append(mark);
        }
    });
    document.querySelectorAll(".mensagem").forEach(message => {
        message.setAttribute("role", "status");
        if (/confirme|edite|dados definidos|edição cancelada|atualizados.*finalize/i.test(message.textContent)) {
            message.classList.remove("erro", "sucesso"); message.classList.add("info");
        }
    });
    document.querySelectorAll("[data-confirm]").forEach(control => control.addEventListener("click", event => {
        if (!window.confirm(control.dataset.confirm)) event.preventDefault();
    }));
    document.querySelectorAll(".form-crud > form").forEach(form => {
        const action = form.querySelector("[name=opcao]")?.value || "";
        if (/confirmarExcluir|^excluir$/.test(action)) {
            form.querySelectorAll("[required]").forEach(field => field.required = false);
        }
    });
    document.querySelectorAll("input[type=password]").forEach(input => {
        const wrapper = document.createElement("div");
        wrapper.className = "password-field";
        input.before(wrapper); wrapper.append(input);
        const button = document.createElement("button");
        button.type = "button"; button.className = "password-toggle"; button.textContent = "Mostrar";
        button.setAttribute("aria-label", "Mostrar " + (input.id === "confirmacao" ? "confirmação da senha" : "senha"));
        button.setAttribute("aria-pressed", "false");
        button.addEventListener("click", () => {
            const visible = input.type === "password";
            input.type = visible ? "text" : "password"; button.textContent = visible ? "Ocultar" : "Mostrar";
            button.setAttribute("aria-pressed", String(visible));
            button.setAttribute("aria-label", (visible ? "Ocultar " : "Mostrar ") + (input.id === "confirmacao" ? "confirmação da senha" : "senha"));
        });
        wrapper.append(button);
    });
    const moduleSearch = document.querySelector("[data-module-search]");
    moduleSearch?.addEventListener("input", () => {
        let visible = 0;
        document.querySelectorAll(".module-card").forEach(card => {
            card.hidden = !normalize(card.textContent).includes(normalize(moduleSearch.value));
            if (!card.hidden) visible++;
        });
        const empty = document.querySelector("[data-module-empty]");
        if (empty) empty.hidden = visible > 0;
    });
    document.querySelectorAll("[data-catalog-search]").forEach(search => {
        const select = document.getElementById(search.dataset.catalogSearch);
        if (!select) return;
        const options = Array.from(select.options).map(option => option.cloneNode(true));
        search.addEventListener("input", () => {
            const value = select.value;
            const matches = options.filter(option => !option.value || normalize(option.textContent).includes(normalize(search.value)));
            select.replaceChildren(...matches.map(option => option.cloneNode(true)));
            if (matches.some(option => option.value === value)) select.value = value;
            else select.value = "";
        });
    });
    document.querySelectorAll(".table-crud:not([data-static])").forEach((table, tableIndex) => {
        let wrapper = table.parentElement;
        if (!wrapper.classList.contains("table-responsive")) {
            wrapper = document.createElement("div"); wrapper.className = "table-responsive";
            table.before(wrapper); wrapper.append(table);
        }
        wrapper.tabIndex = 0; wrapper.setAttribute("aria-label", "Listagem com rolagem horizontal");
        const body = table.tBodies[0];
        if (!body) return;
        const rows = Array.from(body.rows);
        const columns = Array.from(table.tHead?.rows[0]?.cells || []).reduce((sum, cell) => sum + cell.colSpan, 0) || 1;
        const emptyRow = document.createElement("tr"); const emptyCell = document.createElement("td");
        emptyCell.colSpan = columns; emptyCell.className = "empty-state"; emptyRow.append(emptyCell); body.append(emptyRow);
        const toolbar = document.createElement("div"); toolbar.className = "table-toolbar";
        const search = document.createElement("input"); search.type = "search"; search.placeholder = "Buscar nesta lista...";
        search.setAttribute("aria-label", "Buscar registros nesta lista"); search.id = "busca-lista-" + tableIndex;
        const count = document.createElement("span"); count.setAttribute("aria-live", "polite");
        toolbar.append(search, count); wrapper.before(toolbar);
        const pagination = document.createElement("div"); pagination.className = "table-pagination";
        const sizeLabel = document.createElement("label"); sizeLabel.textContent = "Por página ";
        const size = document.createElement("select"); size.setAttribute("aria-label", "Registros por página");
        [10, 20, 50].forEach(value => { const option = document.createElement("option"); option.value = value; option.textContent = value; size.append(option); });
        sizeLabel.append(size);
        const previous = document.createElement("button"); previous.type = "button"; previous.textContent = "Anterior";
        const position = document.createElement("span");
        const next = document.createElement("button"); next.type = "button"; next.textContent = "Próxima";
        pagination.append(sizeLabel, previous, position, next); wrapper.after(pagination);
        let page = 1; let direction = 1; let lastSort = -1;
        function render() {
            const matches = rows.filter(row => normalize(row.textContent).includes(normalize(search.value)));
            const pages = Math.max(1, Math.ceil(matches.length / Number(size.value)));
            page = Math.max(1, Math.min(page, pages));
            const visible = new Set(matches.slice((page - 1) * Number(size.value), page * Number(size.value)));
            rows.forEach(row => row.hidden = !visible.has(row));
            emptyRow.hidden = matches.length > 0;
            emptyCell.textContent = rows.length ? "Nenhum resultado para esta busca." : "Nenhum registro cadastrado.";
            count.textContent = matches.length + (matches.length === 1 ? " registro" : " registros");
            position.textContent = page + " de " + pages;
            previous.disabled = page === 1; next.disabled = page === pages;
            pagination.hidden = matches.length <= 10 && rows.length <= 10;
        }
        search.addEventListener("input", () => { page = 1; render(); });
        size.addEventListener("change", () => { page = 1; render(); });
        previous.addEventListener("click", () => { page--; render(); });
        next.addEventListener("click", () => { page++; render(); });
        Array.from(table.tHead?.rows[0]?.cells || []).forEach((header, index) => {
            if (header.colSpan > 1 || /aç[ãõ]|a[cç][aã]o/i.test(header.textContent)) return;
            const button = document.createElement("button"); button.type = "button"; button.className = "sort-button";
            button.textContent = header.textContent; button.setAttribute("aria-label", "Ordenar por " + header.textContent.trim());
            header.replaceChildren(button);
            button.addEventListener("click", () => {
                direction = lastSort === index ? -direction : 1; lastSort = index;
                const collator = new Intl.Collator("pt-BR", { numeric: true, sensitivity: "base" });
                const value = row => {
                    const cell = row.cells[index];
                    const text = cell?.dataset.sort || cell?.textContent.trim() || "";
                    const date = text.match(/^(\d{2})\/(\d{2})\/(\d{4})$/);
                    return date ? date[3] + "-" + date[2] + "-" + date[1] : text;
                };
                rows.sort((a, b) => direction * collator.compare(value(a), value(b)));
                rows.forEach(row => body.append(row)); body.append(emptyRow);
                table.querySelectorAll("th[aria-sort]").forEach(cell => cell.removeAttribute("aria-sort"));
                header.setAttribute("aria-sort", direction === 1 ? "ascending" : "descending"); page = 1; render();
            });
        });
        render();
    });
    const main = document.getElementById("conteudo");
    if (main) { main.tabIndex = -1; if (main.tagName !== "MAIN") main.setAttribute("role", "main"); }
    if (document.querySelector(".app-header") && !document.querySelector(".app-footer")) {
        const footer = document.createElement("footer"); footer.className = "app-footer";
        footer.textContent = "Clínica Médica · IFSULDEMINAS · Projeto acadêmico"; document.body.append(footer);
    }
});
