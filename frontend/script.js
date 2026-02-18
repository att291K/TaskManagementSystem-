// Проверка авторизации при загрузке страницы
document.addEventListener('DOMContentLoaded', function() {
    checkAuth();
    setupEventListeners();
    loadTasks();
    updateStatistics();
});

// Проверка авторизации
function checkAuth() {
    const isAuthenticated = localStorage.getItem('isAuthenticated') === 'true';
    
    if (!isAuthenticated) {
        // Сохраняем текущий URL для возврата после авторизации
        sessionStorage.setItem('redirectAfterLogin', window.location.href);
        window.location.href = 'custom-login.html';
    }
}

// Настройка обработчиков событий
function setupEventListeners() {
    // Навигация между страницами
    document.querySelectorAll('.nav-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            const page = this.dataset.page;
            switchPage(page);
        });
    });

    // Выход из системы
    document.getElementById('logoutBtn').addEventListener('click', logout);

    // Добавление задачи
    document.getElementById('addTaskBtn').addEventListener('click', showTaskModal);
    
    // Закрытие модального окна
    document.querySelector('.close').addEventListener('click', hideTaskModal);
    
    // Отправка формы задачи
    document.getElementById('taskForm').addEventListener('submit', createTask);
    
    // Фильтрация задач
    document.querySelectorAll('.filter-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            const filter = this.dataset.filter;
            filterTasks(filter);
        });
    });
    
    // Закрытие модального окна по клику вне его
    window.addEventListener('click', function(e) {
        const modal = document.getElementById('taskModal');
        if (e.target === modal) {
            hideTaskModal();
        }
    });
}

// Переключение страниц
function switchPage(page) {
    // Обновляем активную кнопку
    document.querySelectorAll('.nav-btn').forEach(btn => {
        btn.classList.remove('active');
        if (btn.dataset.page === page) {
            btn.classList.add('active');
        }
    });
    
    // Показываем соответствующую страницу
    document.querySelectorAll('.page').forEach(p => {
        p.classList.remove('active-page');
    });
    
    if (page === 'statistics') {
        document.querySelector('.statistics-page').classList.add('active-page');
        updateStatistics();
    } else {
        document.querySelector('.tasks-page').classList.add('active-page');
        loadTasks();
    }
}

// Работа с задачами
let tasks = JSON.parse(localStorage.getItem('tasks')) || [];

function loadTasks(filter = 'all') {
    const tasksList = document.getElementById('tasksList');
    let filteredTasks = tasks;
    
    if (filter === 'active') {
        filteredTasks = tasks.filter(task => !task.completed);
    } else if (filter === 'completed') {
        filteredTasks = tasks.filter(task => task.completed);
    }
    
    if (filteredTasks.length === 0) {
        tasksList.innerHTML = '<p class="no-tasks">Нет задач</p>';
        return;
    }
    
    tasksList.innerHTML = filteredTasks.map(task => `
        <div class="task-item ${task.completed ? 'completed' : ''}" data-id="${task.id}">
            <input type="checkbox" class="task-checkbox" ${task.completed ? 'checked' : ''}>
            <div class="task-content">
                <div class="task-title">${task.title}</div>
                <div class="task-description">${task.description || 'Нет описания'}</div>
            </div>
            <span class="delete-task" onclick="deleteTask('${task.id}')">✕</span>
        </div>
    `).join('');
    
    // Добавляем обработчики для чекбоксов
    document.querySelectorAll('.task-checkbox').forEach((checkbox, index) => {
        checkbox.addEventListener('change', function() {
            const taskId = this.closest('.task-item').dataset.id;
            toggleTaskComplete(taskId);
        });
    });
}

function createTask(e) {
    e.preventDefault();
    
    const form = e.target;
    const title = form.querySelector('input').value;
    const description = form.querySelector('textarea').value;
    
    if (!title.trim()) return;
    
    const newTask = {
        id: Date.now().toString(),
        title: title,
        description: description,
        completed: false,
        createdAt: new Date().toISOString()
    };
    
    tasks.push(newTask);
    localStorage.setItem('tasks', JSON.stringify(tasks));
    
    form.reset();
    hideTaskModal();
    loadTasks();
    updateStatistics();
}

function toggleTaskComplete(taskId) {
    tasks = tasks.map(task => {
        if (task.id === taskId) {
            return { ...task, completed: !task.completed };
        }
        return task;
    });
    
    localStorage.setItem('tasks', JSON.stringify(tasks));
    
    const activeFilter = document.querySelector('.filter-btn.active').dataset.filter;
    loadTasks(activeFilter);
    updateStatistics();
}

function deleteTask(taskId) {
    if (confirm('Вы уверены, что хотите удалить задачу?')) {
        tasks = tasks.filter(task => task.id !== taskId);
        localStorage.setItem('tasks', JSON.stringify(tasks));
        
        const activeFilter = document.querySelector('.filter-btn.active').dataset.filter;
        loadTasks(activeFilter);
        updateStatistics();
    }
}

function filterTasks(filter) {
    document.querySelectorAll('.filter-btn').forEach(btn => {
        btn.classList.remove('active');
        if (btn.dataset.filter === filter) {
            btn.classList.add('active');
        }
    });
    
    loadTasks(filter);
}

// Управление модальным окном
function showTaskModal() {
    document.getElementById('taskModal').classList.add('show');
}

function hideTaskModal() {
    document.getElementById('taskModal').classList.remove('show');
    document.getElementById('taskForm').reset();
}

// Обновление статистики
function updateStatistics() {
    const totalTasks = tasks.length;
    const completedTasks = tasks.filter(task => task.completed).length;
    const inProgressTasks = totalTasks - completedTasks;
    const activity = totalTasks ? Math.round((completedTasks / totalTasks) * 100) : 0;
    
    document.getElementById('totalTasks').textContent = totalTasks;
    document.getElementById('completedTasks').textContent = completedTasks;
    document.getElementById('inProgressTasks').textContent = inProgressTasks;
    document.getElementById('activity').textContent = activity + '%';
}

// Выход из системы
function logout() {
    localStorage.removeItem('isAuthenticated');
    window.location.href = 'custom-login.html';
}

// Глобальная функция для удаления задачи (для onclick в HTML)
window.deleteTask = deleteTask;