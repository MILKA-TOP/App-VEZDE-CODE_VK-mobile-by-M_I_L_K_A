# App-VEZDEKOD_MOBILE[2022]

## Данная версия является "спортивный", написанная в сроки проведения хакатона. В скором времени код будет переписан под архитектуру MVVM с доработкой отдельных пунктов.

## Было набрано 120/150 баллов;

## 10 ✔
На главном экране генерируется карточка с названием магазина, адресом и временем доставки. Названия 
магазинов выбираются произвольно из предварительно захардкоженного массива. Время генерируется произвольным
образом, но в диапазоне сегодняшнего дня. Внизу карточки расположены две кнопки: «Отклонить» и «Принять». 
Нажатие «Отклонить» означает отказ от заказа, на место отклонённой карточки должна встать следующая. 
Нажатие «Принять» должно приводить к принятию заказа и показу алерта «Заказ принят!». После этого с экрана 
пропадают карточки, в центре экрана остаётся надпись «У вас есть активный заказ», а под ней — кнопка «Завершить заказ».
При нажатии на эту кнопку процесс принятия и отклонения заказа запускается заново.

## 20
Добавим возможность принять или отклонить заказ с помощью свайпа — на манер «Тиндера». Свайп карточки влево 
означает отказ от заказа, свайп вправо — принятие. При свайпе влево границы карточки должны окрашиваться в красный цвет, 
а сама карточка — смещаться к левому краю (не сразу, а по нарастанию: чем ближе карточка к левому краю, тем ярче красный). 
Таким же образом при свайпе вправо границы карточки должны окрашиваться в зелёный цвет. Результат аналогичен нажатию на кнопки.

## 30 ✔
Добавим в приложение таббар. Сделаем две вкладки: «Активный заказ» и «История заказов». На вкладке «Активный заказ» 
остаётся уже разработанный экран. На вкладке «История заказов» отображается список заказов, с которыми работал пользователь. 
Заказы могут быть в одном из трёх статусов: «Активный» (может быть только один такой заказ), «Отклонён», «Завершён». 
Цвет текста для отображения статуса должен различаться: жёлтый для активного заказа, зелёный для завершённого, красный для отклонённого.

## 40 ✔
Хранение данных. Необходимо, чтобы список заказов сохранялся между сессиями. Для этого нужно использовать некоторую 
локальную базу данных (Realm, CoreData) на усмотрение команды. При запуске приложения вкладка «История заказов» должна 
показывать данные из этой базы.

## 50 ✔
Добавьте в проект карту. Выбор библиотеки — на ваше усмотрение: Apple MapKit, Google Maps, Яндекс Карты и другие. 
Экран с картой помещается в третью вкладку — «Карта». На карте нужно показать два пина: пин курьера (координаты ставятся 
по геолокации устройства) и пин магазина в случае наличия активного заказа (координаты выбираются из захардкоженного магазина). 
Пример получения координат: заходим на карты Google, нажимаем на произвольную точку (магазин), из URL в строке браузера:

```https://www.google.ru/maps/place/Радуга/@55.1642157,61.3993166,14z/data=!4m13!1m7!3m6!1s0x43c592cb104a3a8d:0xef224a2a6d1711bf!2z0KfQtdC70Y_QsdC40L3RgdC6LCDQp9C10LvRj9Cx0LjQvdGB0LrQsNGPINC-0LHQuy4!3b1!8m2!3d55.1644419!4d61.4368432!3m4!1s0x43c5ed4f136639d7:0x2774b3ae48862179!8m2!3d55.1635933!4d61.4157103?hl=ru ```
выделяем широту (55.1642157) и долготу (61.3993166). Пин курьера стандартный, предлагаемый картой по умолчанию для отображения пользователя. 
Пин магазина — на усмотрение участника. При открытии экрана масштаб карты должен вычисляться таким образом, чтобы оба пина были видны на экране.

Реализуйте обратный геокодинг адреса курьера, чтобы на карте выводился человекочитаемый адрес точки, где сейчас стоит курьер. 
В качестве сервиса геокодинга используйте DaData.