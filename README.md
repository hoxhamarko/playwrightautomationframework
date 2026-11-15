# Playwright + Cucumber Test Framework (Java)

Covers UI (Playwright), API (Playwright APIRequest), and database (JDBC) tests.
You write feature files and, when needed, step definitions. Everything else is reusable.

## Structure

- `src/test/resources/features/`  Gherkin scenarios (ui, api, db)
- `src/test/resources/config/`    Environment settings (qa.properties)
- `steps/`                        Cucumber step definitions and hooks
- `ui/pages/`                     Page objects (locators and page actions)
- `api/ApiClient.java`            HTTP helper
- `db/DbClient.java`              JDBC helper
- `core/ScenarioContext.java`     Per-scenario browser, API context, and shared data

## Setup (VS Code)

1. Install JDK 17 or newer.
2. Install the "Extension Pack for Java" and "Cucumber (Gherkin) Full Support" extensions.
3. Open this folder in VS Code.
4. Set the database password (PowerShell: `$env:DB_PASSWORD="..."`, bash: `export DB_PASSWORD="..."`).

## Run

- Install browsers (once): `./gradlew installBrowsers`
- All tests: `./gradlew test`
- UI only: `./gradlew test -Dtags=@ui`
- API only: `./gradlew test -Dtags=@api`
- DB only: `./gradlew test -Dtags=@db`
- Visible browser: `./gradlew test -Dtags=@ui -Dheadless=false`
- Another environment: `./gradlew test -Denv=staging`

On Windows, use `gradlew.bat` in place of `./gradlew`.

### Short command (recommended)

Use `-P` aliases; no quoting is needed in PowerShell:

```powershell
./gradlew.bat test -Ptags=@ui -Purl=https://the-internet.herokuapp.com -Pusername=tomsmith -Ppassword=SuperSecretPassword!
```

Aliases: `-Purl` (ui.baseUrl), `-Pusername`, `-Ppassword` (ui.*), `-Pheadless` (true/false), `-PapiUrl`, `-PapiToken`,
`-PdbUrl`, `-PdbUser`, `-PdbPassword`. Quote values containing `$`, `&`, or `;`.

### Configuration overrides

Settings are resolved in this order: JVM system property, environment variable,
then `src/test/resources/config/<env>.properties`. Gradle forwards `ui.*`, `api.*`,
and `db.*` system properties to the tests. For example, in PowerShell:

```powershell
./gradlew.bat test '-Dtags=@ui' '-Dui.baseUrl=https://the-internet.herokuapp.com' '-Dui.username=tomsmith'
```

For passwords and tokens, prefer environment variables to command-line arguments,
which can appear in shell history and process listings:

```powershell
$env:UI_USERNAME = 'tomsmith'
$env:UI_PASSWORD = '<password>'
./gradlew.bat test '-Dtags=@ui' '-Dui.baseUrl=https://the-internet.herokuapp.com'
```

Use the same convention for other settings, such as `API_BASEURL`, `API_TOKEN`,
`DB_URL`, `DB_USER`, and `DB_PASSWORD`. The default UI credentials are public
credentials for the example site; do not put real passwords in properties files.
Tests run on every invocation so environment-variable changes are picked up.

Reports: `target/cucumber-report.html`. Screenshots are attached to failed UI scenarios.

## Writing new tests

- UI: add a page class in `ui/pages/`, then reuse or add steps in `UiSteps`.
- API: use the existing steps. Add a new `When` in `ApiSteps` for new HTTP patterns.
- DB: use the generic steps in `DbSteps`. Write your own SQL in the feature file.
- Mix layers in one scenario if needed, for example, create a record through the API, then verify it in the database.
