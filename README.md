# PianoTestApp
Сделал проект с помощью Spring Boot.
Я немного затупил и сделал так, чтобы создавать нового пользователя при запросе.
Тестировал с помощью Postman.
http://localhost:8080/create  (POST) создается новый пользователь и запоминается его id, создается каждый раз новый при вызове данного метода
http://localhost:8080/users   (GET) посмотреть пользователя и в какой комнате находится
http://localhost:8080/helper?roomId=2&entrance=true   (POST) вводится этот запрос и перенаправляется на URL check и подставляется id пользователя, итоговый интерфейс :
http://localhost:8080/check?roomId=2&entrance=true&keyId=8 

Проверка:
1) http://localhost:8080/create - создается пользователь (POST)
2) http://localhost:8080/users  - проверяем что пользователь (GET) с id=1 создан и у него нет комнаты (реализовал с помощью HashMap) ("1": null - результат)
3) http://localhost:8080/helper?roomId=1&entrance=true  -  пользователь с id=1 в комнате 2  (POST),  ("1":1  -  результат)
4) http://localhost:8080/users  - проверяем
5) http://localhost:8080/helper?roomId=1&entrance=false  - пользователь с id=1 вышел из комнаты 1  (POST),  ("1":null  -  результат)
6) http://localhost:8080/users  - проверяем и т.д.
Вроде все учел, в логах выводится
