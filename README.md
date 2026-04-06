# DS-JSF Add-on

A DeltaSpike add-on that fires a CDI event on the very first JSF (Faces) request.
This enables a customizable and portable startup event as an alternative to
`@Singleton` + `@Startup` + `@PostConstruct`.

## Architecture

The add-on consists of two components:

- **`FirstFacesRequestListener`** -- an `@ApplicationScoped` CDI bean that observes
  JSF request-initialization events (via DeltaSpike's JSF module) and fires a
  configurable one-time CDI event (`FirstFacesRequestEvent` by default).
- **`VetoExtension`** -- a CDI portable extension that conditionally vetoes the
  listener based on DeltaSpike class-deactivation or project-stage configuration.

## Configuration

| Property | Default | Description |
|---|---|---|
| `first-faces-request_event-class` | `org.os890.ds.addon.jsf.api.event.FirstFacesRequestEvent` | Fully-qualified class name of the event to fire |
| `first-faces-request_project-stage` | *(none)* | If set, the listener only activates in the given DeltaSpike project stage |

The listener can also be deactivated via the standard DeltaSpike `ClassDeactivator` SPI.

## Requirements

- Java 25+
- Maven 3.6.3+
- Jakarta EE 10+ (CDI 4.1, Faces 4.1)
- Apache DeltaSpike 2.0.1

## Usage

Add the dependency to your project:

```xml
<dependency>
    <groupId>org.os890.ds.addon</groupId>
    <artifactId>ds-jsf-addon</artifactId>
    <version>1.0.0</version>
</dependency>
```

Then observe the event in any CDI bean:

```java
void onStartup(@Observes FirstFacesRequestEvent event) {
    // one-time initialization
}
```

## Building

```bash
mvn clean verify
```

## Testing

Tests use the [Dynamic CDI Test Bean Addon](https://github.com/os890/dynamic-cdi-test-bean-addon)
to boot a CDI SE container with `@EnableTestBeans`.

## Quality Plugins

The build enforces:

- **Compiler**: `-Xlint:all`, `failOnWarning=true`
- **Enforcer**: Java 25+, Maven 3.6.3+, dependency convergence, banned javax dependencies
- **Checkstyle**: no star imports, braces, whitespace, coding conventions
- **RAT**: Apache 2.0 license header verification
- **JaCoCo**: code coverage reporting
- **Surefire**: pinned version for reproducible test execution

## License

[Apache License, Version 2.0](LICENSE)
