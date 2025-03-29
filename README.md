# ChatBot VK
## [Основное решение для Callback API](./springbot/)

Для корректного запуска необходимо изменить параметры вида `connection.*` в [application.properties](./springbot/src/main/resources/application.properties).
Либо после запуска приложения отправить POST-запрос с теми же параметрами: [Пример запроса](./springbot/src/test/http-requests/connections.http).

В целях безопасности стоит также изменить `admin.token` в [application.properties](./springbot/src/main/resources/application.properties).

[Исходный код](./springbot/src/main/kotlin/org/example/springbot)

## [Дополнительное решение для Long Poll API](./botscript/)

Помимо основного решения, я разобрался как работать с более современным Long Poll API. Однако в процессе выяснилось, что для взаимодействия с этим API достаточно отправлять GET-запросы и не нужно ничего "слушать" в ответ. В таком случае решение на Spring-Boot кажется излишним. Поэтому я вернулся к Callback api. Однако полученное в ходе работы с Long Poll API решение я тоже приложил.

Для корректного запуска либо указать в переменных окружения: `VK_ACCESS_TOKEN` и `VK_GROUP_ID`.
Либо при запуске вам будет предложенно ввести данные параметры из консоли.

[Исходный код](./botScript/src/main/kotlin)
