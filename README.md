Клиент-серверное Android приложение для поиска, просмотра и сохранения товаров на сервисе Etsy.
(приложение в процессе доработки визуальной части и оптимизации бизнес-логики)

Основные функции:

- поиск товаров по категориям и ключевым словам, введенным пользователем

  <img src="https://github.com/VadimChubarov/Screenshots-repo/blob/master/screen1.png" width="200"/>
  <img src="https://github.com/VadimChubarov/Screenshots-repo/blob/master/screen2.png" width="200"/>
  
- просмотр детальной информации о товаре

  <img src="https://github.com/VadimChubarov/Screenshots-repo/blob/master/screen4.png" width="200"/>
  <img src="https://github.com/VadimChubarov/Screenshots-repo/blob/master/screen5.png" width="200"/>
  <img src="https://github.com/VadimChubarov/Screenshots-repo/blob/master/screen3.png" width="200"/>
  
- сохранение товаров в "избранное"/ выбор и удаление

  <img src="https://github.com/VadimChubarov/Screenshots-repo/blob/master/screen7.png" width="200"/>
  <img src="https://github.com/VadimChubarov/Screenshots-repo/blob/master/screen6.png" width="200"/>
  
  Навигация и просмотр товаров выполнены с помощью ToolBar/ViewPager, RecyclerView+swipe to refresh/pagination,
  взаимодействие c RESTful API - Retrofit,Picasso, формат - JSON, архитектура - MVP
  
  Технологии:
  - MVP
  - Retrofit2
  - Picasso
  - GsonConverterFactory
  - Хранение данных SQLite
