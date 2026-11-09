# MenuMentor

A desktop app that plans a week of meals for you. You add your ingredients and dishes once, set a daily calorie range, and MenuMentor generates a weekly menu of lunches and dinners (first and second course each) that respects how often you want to eat each dish, how long to wait before repeating it, and your calorie limits. It then builds the shopping list for the week.

Built with **Java (Swing)** and **SQLite**.

## Features

- **Ingredients & dishes:** store ingredients with their calories, and dishes with their ingredients (in grams), an optional recipe link, whether they're for lunch and/or dinner, and whether they're a first or second course.
- **Weekly menu generation:** picks dishes at random from the ones that meet every rule:
  - **Frequency:** at most *n* times in the last 28 days
  - **Waiting time:** at least *n* days since it was last eaten
  - **Calories:** stays within your daily minimum and maximum
- **Unfulfilled requirements:** when no dish meets every rule, MenuMentor still fills the slot and tells you which rule couldn't be met.
- **Swap menus:** exchange the meals of two days in the week.
- **Calorie overview:** calories per course, plus the week's total and daily average.
- **Shopping list:** total ingredients needed for the week, ready to print.
- **Enable/disable dishes:** take a dish out of rotation without deleting it.

## Running it

You need a **JDK 8 or newer** (`javac` and `java` on your PATH).

```sh
# Windows
run.bat

# macOS / Linux
./run.sh
```

The script compiles `src/` into `out/` and starts the app.

On first run, MenuMentor creates `InternalDB.db` from the example data in `data/sample.db` (30 ingredients, 20 dishes), so there's something to try straight away. Your own data stays in `InternalDB.db`, which is not committed.

To keep the database somewhere else, create a `Config.txt` next to the app containing the path to the `.db` file.

## Project structure

```
src/         Java sources (Swing windows + domain classes)
resources/   Icons used by the UI
lib/         SQLite JDBC driver
data/        Example database copied in on first run
```

The database schema is created automatically on start-up (`Main.generarDB`).
