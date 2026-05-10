# Розрахунково-графічна робота №2: JavaBeans

Проєкт містить два JavaBeans-компоненти для роботи з набором експериментальних даних:

- `mybeans.DataSheetTable` — табличне подання з додаванням, видаленням і редагуванням рядків.
- `mybeans.DataSheetGraph` — графічне подання точок із можливістю з'єднання точок лініями.

Додаток `myapplication.Test` використовує ці компоненти для відкриття, редагування та збереження XML-файлів із даними.

## Збірка

```powershell
powershell -ExecutionPolicy Bypass -File .\build.ps1 -Clean
```

Після збірки у папці `dist` будуть створені:

- `databeans.jar` — JAR-архів JavaBeans-компонентів зі службовим маніфестом.
- `rgr2-app.jar` — виконуваний JAR-архів додатку.

## Запуск

```powershell
java -jar .\dist\rgr2-app.jar
```

Для перевірки можна відкрити файл `data/sample.xml`.

## IntelliJ IDEA

Якщо IDE не бачить класи або підсвічує `package/import`:

1. Відкрити папку `D:\kros - program\ргр2` як проєкт.
2. Перевірити, що `src/main/java` позначено як `Sources Root`.
3. Перевірити, що `src/main/resources` позначено як `Resources Root`.
4. Для запуску обрати клас `myapplication.Test`.

Код приведено до синтаксису Java 8, тому він має компілюватися на JDK 8 і новіших JDK.

## Git

Для створення локальної історії роботи з автором `Dmytro Parafieinyk <dmytro.parafieinyk@student.karazin.ua>`:

```powershell
powershell -ExecutionPolicy Bypass -File .\setup-git.ps1
```

Або з Command Prompt:

```cmd
setup-git.cmd
```
