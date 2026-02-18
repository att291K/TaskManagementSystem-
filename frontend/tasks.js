const tasksContainer = document.getElementById("tasksContainer");
const reloadBtn = document.getElementById("reloadBtn");
const loadingStatus = document.getElementById("loadingStatus");
const modeStatus = document.getElementById("modeStatus");
const errorBox = document.getElementById("errorBox");

const newTitle = document.getElementById("newTitle");
const newDescription = document.getElementById("newDescription");
const newStatus = document.getElementById("newStatus");
const createTaskBtn = document.getElementById("createTaskBtn");
const clearFormBtn = document.getElementById("clearFormBtn");

const loadingOverlay = document.getElementById("loadingOverlay");
const loadingOverlayText = document.getElementById("loadingOverlayText");

const USE_MOCK = false;
const ASSIGNMENT_API = 'http://localhost:8082';
const TASKS_API = 'http://localhost:8081';
const AUTH_API = 'http://localhost:8080';
const NGINX = 'http://localhost:8084';

const STATUS_OPTIONS = ["OPEN", "IN_PROGRESS", "DONE", "BLOCKED"];


// ------------------ МОКОВЫЕ ДАННЫЕ ------------------
let mockTasks = [
    { id: 5, title: "Сделать отчет", description: "Подготовить отчет по проекту X", status: "OPEN" },
    { id: 6, title: "Проверить данные", description: "Провести ревью входящих данных", status: "IN_PROGRESS" }
];

let mockAssignments = [
    { taskId: 5, employeeId: 10 },
    { taskId: 6, employeeId: 11 }
];

// /users
let mockUsers = [
    { id: 10, name: "Иван Петров" },
    { id: 11, name: "Анна Смирнова" },
    { id: 12, name: "Олег Кузнецов" }
];
// ----------------------------------------------------

function setLoading(isLoading, text = "") {
    reloadBtn.disabled = isLoading;
    createTaskBtn.disabled = isLoading;
    clearFormBtn.disabled = isLoading;

    loadingStatus.textContent = text || (isLoading ? "Загрузка..." : "Готово");

    // overlay
    if (isLoading) {
        loadingOverlay.style.display = "flex";
        loadingOverlayText.textContent = text || "Загрузка...";
    } else {
        loadingOverlay.style.display = "none";
        loadingOverlayText.textContent = "";
    }
}

function showError(message) {
    errorBox.style.display = "block";
    errorBox.textContent = message;
}

function clearError() {
    errorBox.style.display = "none";
    errorBox.textContent = "";
}

function escapeHtml(str) {
    return String(str ?? "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}

async function fetchJson(url, options = {}) {
    const res = await fetch(url, {
        headers: { "Accept": "application/json", ...(options.headers || {}) },
        ...options
    });

    if (!res.ok) {
        const text = await res.text().catch(() => "");
        throw new Error(`Ошибка ${res.status} при запросе ${url}\n${text}`);
    }

    const contentType = res.headers.get("content-type") || "";
    if (contentType.includes("application/json")) return res.json();
    return null;
}

// ------------------ РЕАЛЬНЫЕ API ФУНКЦИИ (TODO включить) ------------------

async function fetchTasks() {
    const jwt = localStorage.getItem('jwt');

    return fetchJson(`${TASKS_API}/tasks/getAllTasks`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${jwt}`
        }
    });
}

async function fetchAssignments(taskIds) {
    const jwt = localStorage.getItem('jwt');

    return fetchJson(`${ASSIGNMENT_API}/assignments`, {
        method: "POST",
        headers: { "Content-Type": "application/json", "Authorization": `Bearer ${jwt}` },
        body: JSON.stringify({ taskIds }),
    });
}

async function fetchUsers() {
    const token = localStorage.getItem('jwt');

    // Если токен есть, идем в API, если нет — на логин
    if (!token) {
        window.location.href = '/login.html';
        return [];
    }

    try {
        const users = await fetchJson(`${AUTH_API}/users`, {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });
        return users || [];
    } catch (e) {
        console.error("Не удалось загрузить пользователей:", e);
        return [];
    }
}

async function getRoles() {
    const jwt = localStorage.getItem('jwt');

    return fetchJson(`${AUTH_API}/getRoles`, {
        method: "GET",
        headers: { "Content-Type": "application/json", "Authorization": `Bearer ${jwt}` },
    });
}

// PATCH /tasks  { id: 5, status: "OPEN" }
// async function patchTaskStatus(taskId, status) {
//     return fetchJson(`/tasks`, {
//         method: "PATCH",
//         headers: { "Content-Type": "application/json" },
//         body: JSON.stringify({ id: taskId, status })
//     });
// }

async function patchTaskStatus(taskId, status) {
    const jwt = localStorage.getItem('jwt');
    return fetchJson(`${TASKS_API}/tasks`, { // Добавлен URL и JWT

        method: "PATCH",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${jwt}`
        },
        body: JSON.stringify({ id: taskId, status })
    });
}

// POST /assignment  { taskId: 5, employeeId: 10 }
async function postAssignment(taskId, employeeId) {
    const jwt = localStorage.getItem('jwt');

    return fetchJson(`${ASSIGNMENT_API}/assignment`, {
        method: "POST",
        headers: { "Content-Type": "application/json", "Authorization": `Bearer ${jwt}` },
        body: JSON.stringify({ taskId, employeeId })
    });
}

// POST /tasks  { title, description, status }
// async function createTask(payload) {
//     return fetchJson(`/tasks`, {
//         method: "POST",
//         headers: { "Content-Type": "application/json" },
//         body: JSON.stringify(payload)
//     });
// }
async function createTask(payload) {
    const jwt = localStorage.getItem('jwt');
    return fetchJson(`${TASKS_API}/tasks`, { // Добавлен URL и JWT

        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${jwt}`
        },
        body: JSON.stringify(payload)
    });
}

// ------------------ МОКОВЫЕ API ФУНКЦИИ ------------------

async function fetchTasksMock() {
    return mockTasks;
}

async function fetchAssignmentsMock() {
    return mockAssignments;
}

async function fetchUsersMock() {
    return mockUsers;
}

async function patchTaskStatusMock(taskId, status) {
    mockTasks = mockTasks.map(t => t.id === taskId ? { ...t, status } : t);
    return { ok: true };
}

async function createTaskMock(payload) {
    const nextId = Math.max(0, ...mockTasks.map(t => t.id)) + 1;

    const newTask = {
        id: nextId,
        title: payload.title,
        description: payload.description,
        status: payload.status
    };

    mockTasks = [newTask, ...mockTasks]; // добавим сверху
    return newTask;
}

// ------------------ UI / ЛОГИКА ------------------

function buildAssignmentsMap(assignments) {
    const map = new Map();

    if (!assignments) return map;

    // Если пришел один объект, упаковываем его в массив [assignments]
    const data = Array.isArray(assignments) ? assignments : [assignments];

    for (const a of data) {
        // Добавляем проверку на существование а
        if (a && a.taskId != null) {
            map.set(String(a.taskId), a.employeeId);
        }
    }
    return map;
}

function renderTasks(tasks, assignmentsMap, users, role) {
    tasksContainer.innerHTML = "";

    if (!tasks || tasks.length === 0) {
        tasksContainer.innerHTML = `<div class="empty">Тасок нет</div>`;
        return;
    }

    const html = tasks.map((t) => {
        const id = t.id;
        const title = t.title ?? "Без названия";
        const description = t.description ?? "";
        const status = t.status ?? "UNKNOWN";

        const currentEmployeeId = assignmentsMap.get(String(id)) ?? null;

        const statusOptionsHtml = STATUS_OPTIONS.map(s => `
          <option value="${escapeHtml(s)}" ${s === status ? "selected" : ""}>
            ${escapeHtml(s)}
          </option>
        `).join("");

        const employeeOptionsHtml = `
          <option value="">Не назначен</option>
          ${(users || []).map(u => `
              <option value="${u.id}">
                ${escapeHtml(u.username ?? ("User #" + u.id))}
              </option>
            `)}
        `;//.join("")

        return `
          <div class="card">
            <h2 class="task-title">${escapeHtml(title)}</h2>

            <p class="task-desc">${escapeHtml(description)}</p>

            <div class="task-meta">
              <div class="row">
                <span>Статус:</span>
                <select data-action="change-status" data-task-id="${escapeHtml(id)}">
                  ${statusOptionsHtml}
                </select>
              </div>

              <div class="row">
                <span>Назначенный сотрудник:</span>
                <select ${role !== 'MANAGER' ? 'disabled' : ''} data-action="change-employee" data-task-id="${escapeHtml(id)}">
                  ${employeeOptionsHtml}
                </select>
              </div>
            </div>
          </div>
        `;
    }).join("");

    tasksContainer.innerHTML = html;
}

function initCreateForm() {
    newStatus.innerHTML = STATUS_OPTIONS.map(s => `
        <option value="${escapeHtml(s)}" ${s === "OPEN" ? "selected" : ""}>
          ${escapeHtml(s)}
        </option>
      `).join("");
}

function clearCreateForm() {
    newTitle.value = "";
    newDescription.value = "";
    newStatus.value = "OPEN";
}

async function loadData() {
    clearError();
    modeStatus.textContent = USE_MOCK ? "Мок-режим" : "API-режим";
    setLoading(true, USE_MOCK ? "Загрузка (моковые данные)..." : "Загрузка...");

    try{
            const token = localStorage.getItem('jwt');
            if (!token) {
                window.location.href = NGINX + '/custom-login.html';
            }
    }
    catch (err) {
        showError(err?.message || String(err));
    }

    try {
        let tasks, assignments, users;

        if (USE_MOCK) {
            tasks = await fetchTasksMock();
            users = await fetchUsersMock();
        } else {
            // TODO: включить реальные запросы
            /*[tasks, assignments, users] = await Promise.all([
                fetchUsers()
            ]);*/
            tasks = await fetchTasks();
            users = await fetchUsers();
        }

        //const taskIds = tasks?.map(task => task.id)
        const taskIds = Array.isArray(tasks) ? tasks.map(t => t.id) : [];


        console.log(taskIds)

        assignments = await fetchAssignments(taskIds)

        const assignmentsMap = buildAssignmentsMap(assignments);

        let role = ''
        try {
            const roles = await getRoles()
            role = roles[0]
        } catch(err) {
            //window.location.href = '/custom-login.html';
            if (err.message.includes("401") || err.message.includes("403")) {
                window.location.href = '/custom-login.html';
            } else {
                showError("Ошибка загрузки ролей: " + err.message);
            }
        }

        renderTasks(tasks, assignmentsMap, users, role);

    } catch (err) {
        showError(err?.message || String(err));
    } finally {
        setLoading(false, USE_MOCK ? "Мок-режим (TODO подключить API)" : "Готово");
    }
}

// Изменения в тасках (статус/назначение)
tasksContainer.addEventListener("change", async (e) => {
    const target = e.target;
    if (!(target instanceof HTMLSelectElement)) return;

    const action = target.dataset.action;
    const taskId = Number(target.dataset.taskId);
    if (!action || !taskId) return;

    try {
        setLoading(true, "Сохранение...");

        if (action === "change-status") {
            const newStatusValue = target.value;

            if (USE_MOCK) {
                await patchTaskStatusMock(taskId, newStatusValue);
            } else {
                // TODO: включить реальный запрос
                // await patchTaskStatus(taskId, newStatusValue);
            }
        }

        if (action === "change-employee") {
            const raw = target.value;
            const employeeId = raw === "" ? null : Number(raw);

            await postAssignment(taskId, employeeId);
        }

// TODO: turn on when all will be ready
    await loadData();
    } catch (err) {
        showError(err?.message || String(err));
    } finally {
        setLoading(false, USE_MOCK ? "Мок-режим (TODO подключить API)" : "Готово");
    }
});

// Создание таски
createTaskBtn.addEventListener("click", async () => {
    clearError();

    const title = newTitle.value.trim();
    const description = newDescription.value.trim();
    const status = newStatus.value;

    if (!title) {
        showError("Название (title) обязательно для заполнения");
        return;
    }

    try {
        setLoading(true, "Создание задачи...");

        const payload = { title, description, status };

        if (USE_MOCK) {
            await createTaskMock(payload);
        } else {
            // TODO: включить реальный запрос
            // await createTask(payload);
        }

        clearCreateForm();
        await loadData();
    } catch (err) {
        showError(err?.message || String(err));
    } finally {
        setLoading(false, USE_MOCK ? "Мок-режим (TODO подключить API)" : "Готово");
    }
});

clearFormBtn.addEventListener("click", clearCreateForm);
reloadBtn.addEventListener("click", loadData);

initCreateForm();
loadData();