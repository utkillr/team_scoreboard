package com.localhost.scoreboard.service;

import com.localhost.scoreboard.model.Game;
import com.localhost.scoreboard.model.Word;
import com.localhost.scoreboard.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WordService {
    private WordRepository wordRepository;

    @Autowired
    public void setWordRepository(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public List<Word> findAll() {
        return wordRepository.findAll();
    }

    public Word findById(int id) {
        return wordRepository.findById(id);
    }

    public List<Word> findAllByIds(List<Integer> ids) {
        return wordRepository.findByIds(ids);
    }

    public List<Word> findNotUsedByGame(Game game) {
        return wordRepository.findNotUsedByGame(game.getId());
    }

    public List<Word> save(List<Word> words) {
        return wordRepository.saveAll(words);
    }






    public void initWords() {
        if (! findAll().isEmpty()) return;
        List<String> words = Arrays.asList(
                "Авангард",
                "Авария",
                "Авиация",
                "Автобус",
                "Автоматизация",
                "Автомобиль",
                "Автор",
                "Агент",
                "Ад",
                "Адвокат",
                "Администратор",
                "Адрес",
                "Акцент",
                "Алгоритм",
                "Аллах",
                "Алмаз",
                "Алтарь",
                "Альбом",
                "Анализ",
                "Анархист",
                "Анатомия",
                "Ангел",
                "Анекдот",
                "Ансамбль",
                "Антенна",
                "Аплодисменты",
                "Аппетит",
                "Аптека",
                "Арбуз",
                "Арена",
                "Аренда",
                "Аристократия",
                "Арка",
                "Армия",
                "Аромат",
                "Артиллерия",
                "Архив",
                "Ассоциация",
                "Астрономия",
                "Асфальт",
                "Атака",
                "Атмосфера",
                "Атом",
                "Аудитория",
                "Аэропорт",
                "Багаж",
                "База",
                "Баклан",
                "Баланс",
                "Балет",
                "Балкон",
                "Банда",
                "Банк",
                "Банкет",
                "Бар",
                "Барабан",
                "Барак",
                "Бассейн",
                "Батарея",
                "Башня",
                "Бездна",
                "Безопасность",
                "Безумие",
                "Белка",
                "Белок",
                "Бензин",
                "Берег",
                "Береза",
                "Бесконечность",
                "Библиотека",
                "Биология",
                "Биржа",
                "Блеск",
                "Блин",
                "Блокнот",
                "Богатырь",
                "Бокал",
                "Болото",
                "Бомба",
                "Борода",
                "Борт",
                "Борщ",
                "Борьба",
                "Ботинок",
                "Бочка",
                "Брак",
                "Бревно",
                "Бровь",
                "Бронза",
                "Будда",
                "Буква",
                "Букет",
                "Бульвар",
                "Бульон",
                "Бумага",
                "Бумажник",
                "Бунт",
                "Бутерброд",
                "Бутылка",
                "Буфет",
                "Бухгалтер",
                "Бык",
                "Бюджет",
                "Бюро",
                "Бюст",
                "Вагон",
                "Ваза",
                "Вакцина",
                "Валенок",
                "Вальс",
                "Варвар",
                "Варенье",
                "Введение",
                "Ведро",
                "Ведьма",
                "Веер",
                "Век",
                "Веко",
                "Велосипед",
                "Вена",
                "Вера",
                "Верблюд",
                "Веревка",
                "Вероятность",
                "Вертолет",
                "Ветеран",
                "Вечер",
                "Взрыв",
                "Виза",
                "Вилка",
                "Виноград",
                "Винт",
                "Вирус",
                "Вишня",
                "Водитель",
                "Вождь",
                "Воздух",
                "Возраст",
                "Вокзал",
                "Волшебник",
                "Вопрос",
                "Воробей",
                "Воронка",
                "Воротник",
                "Воск",
                "Восстание",
                "Враг",
                "Врач",
                "Вращение",
                "Время",
                "Всадник",
                "Вселенная",
                "Вспышка",
                "Вступление",
                "Вторжение",
                "Вывеска",
                "Выдача",
                "Выдержка",
                "Вызов",
                "Выражение",
                "Высота",
                "Выставка",
                "Выстрел",
                "Вычисление",
                "Вышка",
                "Гавань",
                "Газ",
                "Газета",
                "Галерея",
                "Галстук",
                "Гараж",
                "Гвардия",
                "Гвоздь",
                "Гений",
                "Герб",
                "Герой",
                "Гигант",
                "Гимн",
                "Гимназия",
                "Гитара",
                "Глагол",
                "Глубина",
                "Гнев",
                "Год",
                "Гол",
                "Голод",
                "Голос",
                "Голосование",
                "Голубь",
                "Гонка",
                "Гора",
                "Гордость",
                "Горизонт",
                "Горничная",
                "Город",
                "Горох",
                "Госпиталь",
                "Град",
                "Градус",
                "Грамматика",
                "Гранат",
                "Граната",
                "Граница",
                "Грань",
                "График",
                "Графин",
                "Грех",
                "Гриб",
                "Грипп",
                "Гроза",
                "Грубость",
                "Группа",
                "Груша",
                "Губа",
                "Губернатор",
                "Губка",
                "Гусар",
                "Гусь",
                "Дань",
                "Дата",
                "Дверь",
                "Двигатель",
                "Двор",
                "Действие",
                "Дело",
                "Демон",
                "Деревня",
                "Дерево",
                "Десант",
                "Джентльмен",
                "Диалог",
                "Диаметр",
                "Диван",
                "Диплом",
                "Дирижер",
                "Диск",
                "Дичь",
                "Длина",
                "Дневник",
                "Дно",
                "Добыча",
                "Доверенность",
                "Договор",
                "Дождь",
                "Доза",
                "Доклад",
                "Должность",
                "Долина",
                "Дом",
                "Домино",
                "Допрос",
                "Доска",
                "Доставка",
                "Доступ",
                "Доцент",
                "Дракон",
                "Дробь",
                "Дрова",
                "Дружба",
                "Дуло",
                "Дума",
                "Дуэль",
                "Дыхание",
                "Дьявол",
                "Емкость",
                "Жадность",
                "Жажда",
                "Жандарм",
                "Жгут",
                "Желудок",
                "Жених",
                "Жест",
                "Жестокость",
                "Животное",
                "Жизнь",
                "Жилет",
                "Жребий",
                "Забастовка",
                "Заболевание",
                "Забор",
                "Забрало",
                "Завет",
                "Завещание",
                "Заговор",
                "Заказ",
                "Закат",
                "Заклинание",
                "Заключение",
                "Закуска",
                "Залив",
                "Залог",
                "Заложник",
                "Залп",
                "Замена",
                "Запах",
                "Запись",
                "Запрос",
                "Запястье",
                "Зарплата",
                "Заря",
                "Заряд",
                "Защита",
                "Заявление",
                "Звено",
                "Зверь",
                "Звон",
                "Здание",
                "Землетрясение",
                "Зеркало",
                "Зерно",
                "Змей",
                "Знак",
                "Знамя",
                "Зола",
                "Игла",
                "Игра",
                "Извинение",
                "Излучение",
                "Измена",
                "Изоляция",
                "Изречение",
                "Изумление",
                "Иллюзия",
                "Имидж",
                "Иммунитет",
                "Империя",
                "Импорт",
                "Инспекция",
                "Инстинкт",
                "Институт",
                "Интеллект",
                "Интервью",
                "Интерпретация",
                "Ион",
                "Ирония",
                "Исключение",
                "Искра",
                "Истина",
                "Истощение",
                "Истребитель",
                "Кабина",
                "Кабинет",
                "Кадр",
                "Казино",
                "Казнь",
                "Календарь",
                "Калибр",
                "Калина",
                "Калитка",
                "Камера",
                "Камин",
                "Кампания",
                "Канон",
                "Кант",
                "Капель",
                "Капитал",
                "Кара",
                "Караван",
                "Карета",
                "Картофель",
                "Карточка",
                "Кассета",
                "Кастрюля",
                "Катер",
                "Кафедра",
                "Каша",
                "Каюта",
                "Квартал",
                "Кепка",
                "Керосин",
                "Кий",
                "Кинематограф",
                "Кинжал",
                "Кипяток",
                "Кирпич",
                "Кислород",
                "Кислота",
                "Кисть",
                "Клад",
                "Кладбище",
                "Клевета",
                "Клей",
                "Клетка",
                "Клиент",
                "Климат",
                "Клин",
                "Клиника",
                "Кличка",
                "Клоун",
                "Клуб",
                "Клубок",
                "Ключ",
                "Клятва",
                "Кнопка",
                "Кнут",
                "Ковер",
                "Коготь",
                "Код",
                "Койка",
                "Колесо",
                "Коллега",
                "Коллекция",
                "Колода",
                "Колокол",
                "Колония",
                "Колонна",
                "Кольцо",
                "Кома",
                "Команда",
                "Командировка",
                "Комбинация",
                "Комедия",
                "Комментарий",
                "Компания",
                "Компас",
                "Комплект",
                "Композитор",
                "Компромисс",
                "Компьютер",
                "Комфорт",
                "Конверт",
                "Конечность",
                "Консерватория",
                "Конструктор",
                "Консультация",
                "Контур",
                "Конференция",
                "Конфета",
                "Концерт",
                "Коньяк",
                "Копия",
                "Корабль",
                "Корень",
                "Корзина",
                "Корм",
                "Коробка",
                "Корова",
                "Корона",
                "Корпорация",
                "Коррозия",
                "Коса",
                "Космос",
                "Костер",
                "Костюм",
                "Кость",
                "Кошелек",
                "Кошмар",
                "Крайность",
                "Кран",
                "Крейсер",
                "Крепость",
                "Кресло",
                "Кристалл",
                "Критика",
                "Кровать",
                "Кровь",
                "Кружево",
                "Кружка",
                "Кузнец",
                "Кукуруза",
                "Кулак",
                "Культура",
                "Купе",
                "Купол",
                "Курьер",
                "Кушетка",
                "Лаборатория",
                "Лагерь",
                "Лад",
                "Лазарет",
                "Лак",
                "Лампочка",
                "Ландшафт",
                "Легенда",
                "Легкость",
                "Ледник",
                "Лезвие",
                "Лекция",
                "Лен",
                "Лента",
                "Лень",
                "Лестница",
                "Летчик",
                "Лифт",
                "Ловкость",
                "Ловушка",
                "Лодка",
                "Ложка",
                "Лом",
                "Лопата",
                "Луг",
                "Лыжа",
                "Любовь",
                "Люк",
                "Магазин",
                "Магия",
                "Мантия",
                "Марка",
                "Маска",
                "Массаж",
                "Массив",
                "Матрица",
                "Мачта",
                "Медаль",
                "Мелочь",
                "Мельница",
                "Метель",
                "Метро",
                "Мешок",
                "Миграция",
                "Микрофон",
                "Миска",
                "Множество",
                "Могила",
                "Моделирование",
                "Модель",
                "Модификация",
                "Молекула",
                "Молния",
                "Молчание",
                "Момент",
                "Монополия",
                "Море",
                "Мороз",
                "Мост",
                "Мотоцикл",
                "Мох",
                "Мощь",
                "Мрак",
                "Мрамор",
                "Мудрец",
                "Муза",
                "Музей",
                "Музыкант",
                "Мяч",
                "Набережная",
                "Надпись",
                "Назначение",
                "Налет",
                "Налог",
                "Намерение",
                "Нападение",
                "Написание",
                "Напиток",
                "Напор",
                "Наследие",
                "Наступление",
                "Насыпь",
                "Наука",
                "Находка",
                "Начало",
                "Нашествие",
                "Независимость",
                "Нефть",
                "Ниша",
                "Ножницы",
                "Номер",
                "Носок",
                "Нота",
                "Ночь",
                "Ноша",
                "Обет",
                "Обещание",
                "Обзор",
                "Обновление",
                "Объем",
                "Объявление",
                "Объяснение",
                "Объятие",
                "Обыск",
                "Обычай",
                "Огонь",
                "Ограда",
                "Одежда",
                "Одеяло",
                "Одобрение",
                "Одолжение",
                "Оживление",
                "Ожидание",
                "Озеро",
                "Ознакомление",
                "Озноб",
                "Оклад",
                "Окончание",
                "Окоп",
                "Окраина",
                "Окружение",
                "Олимпиада",
                "Опера",
                "Описание",
                "Оплата",
                "Опоздание",
                "Опора",
                "Оправдание",
                "Опыт",
                "Орбита",
                "Орден",
                "Орел",
                "Орех",
                "Оркестр",
                "Оружие",
                "Освобождение",
                "Осколок",
                "Осмотр",
                "Основатель",
                "Особа",
                "Особняк",
                "Остановка",
                "Остаток",
                "Осторожность",
                "Остров",
                "Осужденный",
                "Ось",
                "Отбор",
                "Отверстие",
                "Отдача",
                "Отзыв",
                "Отказ",
                "Отклонение",
                "Открытие",
                "Отличие",
                "Отметка",
                "Отпечаток",
                "Отпор",
                "Отправление",
                "Отпуск",
                "Отражение",
                "Отрезок",
                "Отрицание",
                "Отчет",
                "Отчуждение",
                "Офис",
                "Официант",
                "Оценка",
                "Очаг",
                "Очки",
                "Павильон",
                "Пазуха",
                "Памятник",
                "Паника",
                "Панихида",
                "Паперть",
                "Папка",
                "Парад",
                "Параллель",
                "Парик",
                "Парк",
                "Паркет",
                "Парламент",
                "Паровоз",
                "Паром",
                "Пароход",
                "Партизан",
                "Парус",
                "Паспорт",
                "Пассажир",
                "Пасть",
                "Патент",
                "Пауза",
                "Пена",
                "Пенсионер",
                "Пень",
                "Пепел",
                "Перевал",
                "Передача",
                "Перелом",
                "Перемена",
                "Переменная",
                "Перепись",
                "Переполох",
                "Переулок",
                "Переход",
                "Период",
                "Периферия",
                "Перо",
                "Перрон",
                "Перстень",
                "Перчатка",
                "Песок",
                "Петля",
                "Печать",
                "Печенье",
                "Печь",
                "Пещера",
                "Пионер",
                "Пир",
                "Пирамида",
                "Пистолет",
                "Письмо",
                "Плавание",
                "Плакат",
                "Пламя",
                "План",
                "Планета",
                "Планирование",
                "Пласт",
                "Пластинка",
                "Плата",
                "Платеж",
                "Платформа",
                "Плен",
                "Пленка",
                "Плечо",
                "Плита",
                "Плоскость",
                "Плотина",
                "Пляж",
                "Побег",
                "Побережье",
                "Повар",
                "Повесть",
                "Повозка",
                "Поворот",
                "Повреждение",
                "Повышение",
                "Повязка",
                "Поговорка",
                "Погода",
                "Погоня",
                "Подарок",
                "Подбородок",
                "Подошва",
                "Подписка",
                "Подражание",
                "Поездка",
                "Пожар",
                "Пожелание",
                "Поза",
                "Поздравление",
                "Позиция",
                "Позор",
                "Поиск",
                "Показание",
                "Покой",
                "Покрытие",
                "Покупка",
                "Покушение",
                "Полет",
                "Полотно",
                "Полуостров",
                "Полюс",
                "Польза",
                "Пользователь",
                "Помеха",
                "Помещение",
                "Помощь",
                "Попытка",
                "Поражение",
                "Порог",
                "Порода",
                "Порох",
                "Порт",
                "Портной",
                "Портрет",
                "Портфель",
                "Поручение",
                "Порция",
                "Порыв",
                "Порядок",
                "Последователь",
                "Посол",
                "Пост",
                "Поставщик",
                "Постановка",
                "Постановление",
                "Постель",
                "Постройка",
                "Потенциал",
                "Поток",
                "Похвала",
                "Поход",
                "Похороны",
                "Почва",
                "Почерк",
                "Почта",
                "Пощада",
                "Поэзия",
                "Поэт",
                "Пояс",
                "Правда",
                "Правило",
                "Праздник",
                "Прах",
                "Преграда",
                "Предатель",
                "Предлог",
                "Предок",
                "Премьера",
                "Преступник",
                "Претендент",
                "Привычка",
                "Приговор",
                "Призрак",
                "Призыв",
                "Приключение",
                "Прикосновение",
                "Прикрытие",
                "Прилавок",
                "Прилив",
                "Приложение",
                "Примета",
                "Примирение",
                "Принадлежность",
                "Приоритет",
                "Припадок",
                "Прирост",
                "Притяжение",
                "Причина",
                "Приют",
                "Проба",
                "Пробка",
                "Проблема",
                "Провал",
                "Провокация",
                "Программа",
                "Прогресс",
                "Проза",
                "Производная",
                "Прокат",
                "Проклятие",
                "Пропорция",
                "Пропуск",
                "Пророчество",
                "Прорыв",
                "Просмотр",
                "Проспект",
                "Простор",
                "Простота",
                "Пространство",
                "Простыня",
                "Протест",
                "Противник",
                "Противоречие",
                "Протокол",
                "Профессор",
                "Профилактика",
                "Процедура",
                "Процесс",
                "Прочность",
                "Прошлое",
                "Проявление",
                "Пружина",
                "Прыжок",
                "Псевдоним",
                "Публикация",
                "Пуля",
                "Пульс",
                "Пункт",
                "Пустота",
                "Пустыня",
                "Пустырь",
                "Пустяк",
                "Путаница",
                "Путешествие",
                "Путь",
                "Пучок",
                "Пушка",
                "Пыль",
                "Пытка",
                "Пятно",
                "Равнодушие",
                "Радость",
                "Развитие",
                "Развлечение",
                "Развод",
                "Разговор",
                "Разделение",
                "Раздражение",
                "Разлука",
                "Размах",
                "Размер",
                "Размышление",
                "Разногласие",
                "Разность",
                "Разработка",
                "Разрез",
                "Разрешение",
                "Разрыв",
                "Разряд",
                "Разъяснение",
                "Ракета",
                "Распад",
                "Расписание",
                "Расправа",
                "Распределение",
                "Рассвет",
                "Расследование",
                "Расстрел",
                "Растерянность",
                "Реакция",
                "Реализация",
                "Реализм",
                "Режим",
                "Режиссер",
                "Резина",
                "Рейс",
                "Ремень",
                "Репетиция",
                "Репутация",
                "Ресница",
                "Реставрация",
                "Рецензия",
                "Рецепт",
                "Рисунок",
                "Ритм",
                "Рой",
                "Рубин",
                "Ругань",
                "Руда",
                "Рукопись",
                "Руль",
                "Русло",
                "Рыцарь",
                "Рычаг",
                "Рюкзак",
                "Сабля",
                "Салат",
                "Сало",
                "Салон",
                "Самолет",
                "Самолюбие",
                "Самоубийство",
                "Санаторий",
                "Сани",
                "Санкция",
                "Сантиметр",
                "Сарай",
                "Сатана",
                "Сатира",
                "Сверстник",
                "Свидание",
                "Свидетельство",
                "Свисток",
                "Свитер",
                "Свобода",
                "Свод",
                "Свойство",
                "Связь",
                "Священник",
                "Сдача",
                "Сделка",
                "Сеанс",
                "Седло",
                "Сезон",
                "Секрет",
                "Секта",
                "Сектор",
                "Семья",
                "Сенат",
                "Сено",
                "Сердце",
                "Сережка",
                "Серия",
                "Серьезность",
                "Сессия",
                "Сигнал",
                "Силуэт",
                "Символ",
                "Симметрия",
                "Симптом",
                "Система",
                "Сияние",
                "Сказка",
                "Скала",
                "Скатерть",
                "Скачок",
                "Скелет",
                "Скобка",
                "Скорбь",
                "Скорость",
                "Скука",
                "Скульптура",
                "Слабость",
                "Следствие",
                "Слеза",
                "Сложение",
                "Сложность",
                "Слой",
                "Слуга",
                "Смелость",
                "Смена",
                "Смерть",
                "Смех",
                "Смешение",
                "Смотритель",
                "Смущение",
                "Снимок",
                "Собеседник",
                "Собор",
                "Событие",
                "Создатель",
                "Созерцание",
                "Сознание",
                "Сокращение",
                "Сокровище",
                "Солидарность",
                "Солнце",
                "Солома",
                "Сомнение",
                "Сон",
                "Соображение",
                "Сообщение",
                "Сообщество",
                "Сооружение",
                "Соотечественник",
                "Сопротивление",
                "Сослуживец",
                "Состояние",
                "Сосуд",
                "Соты",
                "Сочинение",
                "Союзник",
                "Спаситель",
                "Спектакль",
                "Специальность",
                "Спираль",
                "Спирт",
                "Список",
                "Спичка",
                "Спокойствие",
                "Спор",
                "Спорт",
                "Спортсмен",
                "Справка",
                "Срок",
                "Ссылка",
                "Стабильность",
                "Стадия",
                "Стаж",
                "Стандарт",
                "Старость",
                "Статистика",
                "Статус",
                "Статуя",
                "Стать",
                "Ствол",
                "Стекло",
                "Стена",
                "Степень",
                "Стержень",
                "Столб",
                "Столовая",
                "Стопа",
                "Стопка",
                "Сторож",
                "Сторона",
                "Стоянка",
                "Страдание",
                "Страница",
                "Странность",
                "Страсть",
                "Стратегия",
                "Страх",
                "Страхование",
                "Стрела",
                "Строгость",
                "Стройка",
                "Струна",
                "Струя",
                "Студия",
                "Стул",
                "Ступня",
                "Стыд",
                "Субъект",
                "Сугроб",
                "Судья",
                "Суждение",
                "Сумерки",
                "Сумрак",
                "Существование",
                "Сфера",
                "Схватка",
                "Схема",
                "Сцена",
                "Сценарий",
                "Счастье",
                "Счеты",
                "Съезд",
                "Съемка",
                "Сыворотка",
                "Сюжет",
                "Сюрприз",
                "Таблетка",
                "Таблица",
                "Тайна",
                "Такси",
                "Тактика",
                "Талант",
                "Талия",
                "Таможня",
                "Театр",
                "Тезис",
                "Текст",
                "Телеграмма",
                "Тема",
                "Темнота",
                "Темп",
                "Темперамент",
                "Температура",
                "Тень",
                "Терраса",
                "Территория",
                "Террорист",
                "Теснота",
                "Техника",
                "Технология",
                "Течение",
                "Тигр",
                "Типография",
                "Тираж",
                "Тишина",
                "Ткань",
                "Товар",
                "Толстяк",
                "Толчок",
                "Тополь",
                "Топор",
                "Топот",
                "Торг",
                "Тормоз",
                "Тоска",
                "Точка",
                "Точность",
                "Травма",
                "Трагедия",
                "Традиция",
                "Трактир",
                "Трактор",
                "Трамвай",
                "Транспорт",
                "Трасса",
                "Требование",
                "Тревога",
                "Тренер",
                "Тренировка",
                "Треск",
                "Трещина",
                "Трибуна",
                "Тротуар",
                "Трудность",
                "Труп",
                "Трус",
                "Тряпка",
                "Туман",
                "Тупик",
                "Турка",
                "Тыл",
                "Тюрьма",
                "Тяжесть",
                "Тьма",
                "Убежище",
                "Убийство",
                "Убийца",
                "Уборка",
                "Уважение",
                "Угроза",
                "Удаление",
                "Удача",
                "Удивление",
                "Удобрение",
                "Удобство",
                "Удовлетворение",
                "Удостоверение",
                "Ужас",
                "Узел",
                "Указание",
                "Укол",
                "Украшение",
                "Улыбка",
                "Ум",
                "Умение",
                "Умиление",
                "Университет",
                "Унижение",
                "Упадок",
                "Употребление",
                "Ураган",
                "Урон",
                "Усердие",
                "Усилие",
                "Ускорение",
                "Успех",
                "Устав",
                "Усталость",
                "Устранение",
                "Утверждение",
                "Утрата",
                "Уход",
                "Участие",
                "Участник",
                "Ученый",
                "Учет",
                "Училище",
                "Ущелье",
                "Уют",
                "Фабрика",
                "Фаза",
                "Факс",
                "Факт",
                "Факультет",
                "Федерация",
                "Феномен",
                "Ферма",
                "Фермент",
                "Фестиваль",
                "Фигура",
                "Физика",
                "Филиал",
                "Философ",
                "Фильм",
                "Фирма",
                "Фишка",
                "Флот",
                "Фокус",
                "Фольклор",
                "Фон",
                "Фонарь",
                "Фонд",
                "Фонтан",
                "Форма",
                "Формула",
                "Фортепиано",
                "Форточка",
                "Форум",
                "Фотография",
                "Фраза",
                "Фрак",
                "Фракция",
                "Фронт",
                "Фундамент",
                "Функция",
                "Фуражка",
                "Футляр",
                "Хаос",
                "Характер",
                "Хата",
                "Химия",
                "Хирург",
                "Хитрость",
                "Хищник",
                "Ход",
                "Хозяйство",
                "Холм",
                "Хор",
                "Хоромы",
                "Храбрость",
                "Храм",
                "Хранитель",
                "Хроника",
                "Хутор",
                "Цель",
                "Цензура",
                "Ценность",
                "Центр",
                "Цепочка",
                "Цепь",
                "Церемония",
                "Церковь",
                "Цех",
                "Цивилизация",
                "Цикл",
                "Цилиндр",
                "Цирк",
                "Цитата",
                "Цифра",
                "Цыган",
                "Цыпленок",
                "Чайка",
                "Часовня",
                "Частица",
                "Частота",
                "Чаща",
                "Человек",
                "Челюсть",
                "Чемодан",
                "Чемпионат",
                "Чердак",
                "Чернила",
                "Черт",
                "Черта",
                "Чертеж",
                "Честь",
                "Чугун",
                "Чудовище",
                "Чума",
                "Чутье",
                "Чучело",
                "Чушь",
                "Шабаш",
                "Шаг",
                "Шайка",
                "Шаль",
                "Шампанское",
                "Шанс",
                "Шапка",
                "Шарф",
                "Шасси",
                "Шах",
                "Шахматы",
                "Шахта",
                "Шашка",
                "Швейцар",
                "Шелест",
                "Шелк",
                "Шепот",
                "Шест",
                "Шествие",
                "Шеф",
                "Шина",
                "Шлем",
                "Шлюпка",
                "Шляпа",
                "Шнур",
                "Шов",
                "Шок",
                "Шоколад",
                "Шорох",
                "Шоссе",
                "Шоу",
                "Шофер",
                "Шпага",
                "Шпион",
                "Шпора",
                "Шрам",
                "Штаб",
                "Штаны",
                "Штат",
                "Штора",
                "Шторм",
                "Штраф",
                "Штурм",
                "Штурман",
                "Штык",
                "Шум",
                "Шут",
                "Щель",
                "Щенок",
                "Щепка",
                "Щи",
                "Щит",
                "Эвакуация",
                "Эволюция",
                "Экзамен",
                "Экипаж",
                "Экскурсия",
                "Экспедиция",
                "Эксперимент",
                "Эксперт",
                "Электричество",
                "Электрон",
                "Элемент",
                "Элита",
                "Эмоция",
                "Энергия",
                "Энтузиазм",
                "Энциклопедия",
                "Эпидемия",
                "Эпизод",
                "Эпоха",
                "Эра",
                "Эрмитаж",
                "Этаж",
                "Эфир",
                "Эффект",
                "Эхо",
                "Эшелон",
                "Юбилей",
                "Юбка",
                "Юмор",
                "Юность",
                "Юрист",
                "Юрта",
                "Явление",
                "Яд",
                "Ядро",
                "Язва",
                "Язык",
                "Як",
                "Якорь",
                "Яма",
                "Ямщик",
                "Японец",
                "Яркость",
                "Ярмарка",
                "Ярость",
                "Ясность",
                "Ячейка",
                "Ящик");
        save(words.stream().map(w -> {
            Word word = new Word();
            word.setUsedInGames(new ArrayList<>());
            word.setWord(w);
            return word;
        }).collect(Collectors.toList()));
    }
}
