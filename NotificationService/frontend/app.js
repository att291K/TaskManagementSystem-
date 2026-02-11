const API_BASE_URL = 'http://localhost:8080/api';

// DOM элементы
let eventsList = document.getElementById('eventsList');
let loading = document.getElementById('loading');
let errorDiv = document.getElementById('error');
let totalEvents = document.getElementById('totalEvents');
let serviceStatus = document.getElementById('serviceStatus');
let lastUpdate = document.getElementById('lastUpdate');
let lastRefresh = document.getElementById('lastRefresh');

// Состояние приложения
let currentPage = 0;
const pageSize = 20;
let autoRefreshInterval = null;

// Форматирование времени
function formatDateTime(date) {
    return new Date(date).toLocaleString('ru-RU', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
    });
}

// Форматирование типа события
function formatEventType(eventType) {
    const types = {
        'TASK_CREATED': 'Создание',
        'TASK_ASSIGNED': 'Назначение',
        'STATUS_CHANGED': 'Изменение статуса',
        'TASK_COMPLETED': 'Завершение',
        'TEST_EVENT': 'Тест'
    };
    return types[eventType] || eventType;
}

// Получение цвета для типа события
function getEventTypeColor(eventType) {
    const colors = {
        'TASK_CREATED': '#4CAF50',
        'TASK_ASSIGNED': '#2196F3',
        'STATUS_CHANGED': '#FF9800',
        'TASK_COMPLETED': '#9C27B0',
        'TEST_EVENT': '#607D8B'
    };
    return colors[eventType] || '#667eea';
}

// Отображение события
function renderEvent(event) {
    return `
        <div class="event-item">
            <div>
                <span class="event-type" style="background-color: ${getEventTypeColor(event.eventType)}">
                    ${formatEventType(event.eventType)}
                </span>
                <span class="event-time">${formatDateTime(event.createdAt)}</span>
            </div>
            <div class="event-message">${event.message}</div>
            <div class="event-meta">
                <span class="event-task">
                    ${event.taskId ? `Задача #${event.taskId}` : 'Системное событие'}
                </span>
                <span>
                    ${event.employeeId ? `Сотрудник #${event.employeeId}` : ''}
                </span>
            </div>
        </div>
    `;
}

// ... существующий код ...

async function loadEvents() {
    try {
        loading.style.display = 'block';
        errorDiv.style.display = 'none';

        // Загрузка статистики
        const statsResponse = await fetch(`${API_BASE_URL}/events/stats`);
        const statsData = await statsResponse.json();

        if (statsData.status === "success" && statsData.data) {
            const stats = statsData.data;
            totalEvents.textContent = stats.totalEvents || 0;
            serviceStatus.textContent = stats.status === 'running' ? '✓ Активен' : '✗ Ошибка';
            serviceStatus.style.color = stats.status === 'running' ? '#4CAF50' : '#d32f2f';
        } else {
            throw new Error(statsData.message || 'Неизвестная ошибка при загрузке статистики');
        }

        // Загрузка последних событий
        const eventsResponse = await fetch(`${API_BASE_URL}/events/recent?limit=${pageSize}`);
        const eventsData = await eventsResponse.json();

        let events = [];
        if (eventsData.status === "success" && Array.isArray(eventsData.data)) {
            events = eventsData.data;
        } else {
            console.warn('Ожидался массив событий в поле data, получено:', eventsData);
        }

        if (events.length === 0) {
            eventsList.innerHTML = '<div class="loading">Событий пока нет</div>';
        } else {
            eventsList.innerHTML = events.map(renderEvent).join('');
        }

        // Обновляем время
        const now = new Date();
        lastUpdate.textContent = formatDateTime(now);
        lastRefresh.textContent = now.toLocaleTimeString('ru-RU');

    } catch (err) {
        console.error('Ошибка соединения:', err);
        errorDiv.style.display = 'block';
        errorDiv.textContent = `Ошибка соединения: ${err.message}`;
    } finally {
        loading.style.display = 'none';
    }
}

// ... остальной код ...

// Очистка фильтров
function clearEvents() {
    currentPage = 0;
    loadEvents();
}

// Создание тестового события
async function createTestEvent() {
    try {
        const response = await fetch(`${API_BASE_URL}/events/test`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            alert('Тестовое событие создано!');
            loadEvents();
        }
    } catch (err) {
        console.error('Ошибка создания тестового события:', err);
    }
}

// Запуск автообновления
function startAutoRefresh() {
    if (autoRefreshInterval) {
        clearInterval(autoRefreshInterval);
    }

    autoRefreshInterval = setInterval(() => {
        loadEvents();
    }, 10000); // Каждые 10 секунд

    console.log('Автообновление запущено');
}

// Инициализация приложения
function init() {
    // Загружаем события при загрузке страницы
    loadEvents();

    // Запускаем автообновление
    startAutoRefresh();

    // Добавляем кнопку тестирования в консоль для разработчиков
    console.log('Для создания тестового события выполните: createTestEvent()');

    // Экспортируем функции в глобальную область видимости
    window.createTestEvent = createTestEvent;
    window.loadEvents = loadEvents;
    window.clearEvents = clearEvents;
}

// Запускаем приложение при загрузке страницы
document.addEventListener('DOMContentLoaded', init);

// Останавливаем автообновление при скрытии страницы
document.addEventListener('visibilitychange', function() {
    if (document.hidden) {
        if (autoRefreshInterval) {
            clearInterval(autoRefreshInterval);
            autoRefreshInterval = null;
            console.log('Автообновление приостановлено');
        }
    } else {
        startAutoRefresh();
    }
});