
## 🏓🏓 Table Tennis Reservation API 🏓🏓

<p align="center">
  <img src="src/main/resources/logo/tt-logo.png" alt="Table Tennis API Logo">
</p>

The Booking Table API allows you to manage reservations for tables in Table Tennis Sant Andreu clubhouse.
It provides endpoints to create, retrieve, update, and delete reservations, as well as retrieve available tables and check their availability.

## Features

- Create a new booking with customer information and desired table.
- Retrieve information about existing bookings.
- Update or cancel existing bookings.
- Check the availability of tables for a specific date and time.
- Get a list of all available tables.

## 🌎 How To Run
1. Move to the project directory: `cd your-project-name`
2. Build the project for the first time: `./gradlew build`
3. Run all the checks: `./gradlew check`. This will do some checks that you can perform with isolated commands:
    1. [Klint](https://ktlint.github.io/) using [Spotless](https://github.com/diffplug/spotless): `./gradlew spotlessCheck`. If you want to fix style issues automatically: `./gradlew spotlessApply`.
    2. [Kotlin test](https://kotlinlang.org/api/latest/kotlin.test/): `./gradlew test`.
4. To just run the project execute: `./gradlew run`
5. Start coding!