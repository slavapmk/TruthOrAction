# Truth Or Action
[🇬🇧 English](/readme.md) [🇷🇺 Russian](/readme.ru.md)

**Truth Or Action** — это игра «Правда или действие», в которой вопросы и задания создаются на ходу с помощью настраиваемого ИИ (Google Gemini), вместо использования стандартных шаблонов.

---

## 🎯 Основные особенности
- **Динамичный контент**: Каждый вопрос или задание генерируется в реальном времени Google Gemini AI.
- **Настраиваемость**: Игроки могут добавлять свои настройки, чтобы адаптировать игровой процесс.
- **Без повторов**: Gemini гарантирует отсутствие повторяющихся вопросов в ходе игры.
- **Для компании**: Идеально подходит для вечеров с друзьями.

---

## 📋 Технические детали
- **Платформа**: Нативное Android-приложение, написанное на Kotlin.
- **Зависимости**:
    - Retrofit
    - Gson
    - OkHttp
    - Kotlin Coroutines (опционально)
- **Лицензия**: BSD 3-Clause License.

---

## ⚙️ Руководство по установке
1. **Необходимые инструменты**:
    - Установите Android Studio с Android SDK.
2. **Сборка приложения**:
    - Клонируйте репозиторий:
      ```bash
      git clone <repository_url>
      ```
    - Откройте проект в Android Studio.
    - Нажмите на значок молоточка, чтобы собрать проект.

3. **Где находится APK**:
    - Скомпилированный APK файл находится по пути:
      ```
      app/build/outputs/apk/
      ```

---

## 🛠 Настройка
1. **Установите APK** на своё Android-устройство.
2. **Добавьте API-ключ**:
    - Получите API-ключ Google Gemini [по этой ссылке](https://aistudio.google.com/app/apikey).
    - Введите ключ в настройках приложения.
3. **VPN для России**:
    - Для работы в России используйте VPN (даже слабый VPN подойдёт, так как передаётся только текст).
4. **Настройки игры**:
    - По желанию: настройте игру, добавив текстовые инструкции для ИИ.

---

## 🚀 Как играть
1. Добавьте игроков в разделе «Игроки».
2. Начните игру, перейдя на главный экран.
3. Наслаждайтесь уникальным опытом каждый раз!

---

## 🛡 Поддержка
- Сообщайте об ошибках через [GitHub Issues](#).
- Контрибьюторы: проект создан самостоятельно.

---

## 📦 Дополнительные сведения
- **Скриншоты**:  
  ![Скриншот](https://i.imgur.com/gAIgReP.png)
- **Релизы**: Предварительно собранные APK-файлы доступны в [GitHub Releases](#).

---

## 🌐 Будущие обновления
- Автоматические тесты скоро будут интегрированы (они будут выполняться при сборке Gradle).
- Документация и подробные инструкции по настройке появятся в будущем.

---

**Наслаждайтесь Truth Or Action и делайте ваши встречи незабываемыми!**