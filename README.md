# AdolfServer

# Задание на автомат

Создать API, которая принимает регистрацию пользователя и всю информацию, которую пользователь заносит, а также выдает по запросу обратно.

Функционал программы, 

1. Запись дел по дням и сохранение файлов в каждый день.
2. Учет финансов.
3. Учет пар по группам
 
В самом начале мы регистрируемся или авторизируемся в приложении. После нас встречает главный экран с 3 вкладками, на которые мы можем нажать и тем самым перемещаться по приложению.
1. Простой список день, в который мы можем добавлять какие-либо пункты, изменять и удалять. Списки разбиты на дни. <br />
При начальном переходе на эту вкладку нас встречают пункты с датами, при нажатии на которые мы раскрываем их и можем увидеть список дел, так же нужно добавить возможность загрузок на отдельные дни файлов. В поле создать новый список дел у нас есть поля:
- Дата;
- Название;


2. Функционал, учет финансов. При заходе на эту вкладку мы можем создать учет финансов. Форма принимает от пользователя данные о зарплате, и дате (главное месяц и год). После создается таблица с 3 столбцами <br />
- столбец квота траты
- стоимость траты
- остаток <br />
Внизу списка должно выводиться информация об остатке. И перенос остатка на следующий месяц.
Все таблицы доступны пользователю и так же имеют возможность редактироваться, удаляться.


3. Учет часов пар. В этой вкладе нас в начале встречает форма, в которую мы должны заполнить следующие поля. Группа, название дисциплины, количество часов.
По учету часов у нас создается таблица с количеством строк (всего часов/2 +1)
+1 это строка для заголовка.
И 3 столбца:
- дата пары,
- название темы,
- какая пара (вывод 2/2, следующая пара 2/4) <br />
3 столбец должен автоматически при заполнении даты должен заполняться <br />
Все данные должны корректно выводиться в 3х вкладках