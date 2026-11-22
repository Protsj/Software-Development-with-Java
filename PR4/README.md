# Multithreaded Order Management System (JPMS + Generics)

A Maven multi-module build where each module is also a **Java module**.
The `order.app` module provides an interactive CLI for managing orders.
Orders are generated with JavaFaker, created through Lombok `@Builder`, filtered
and processed with lambdas/method references, executed on multiple threads, and
stored in thread-safe generic storage.

The source folders are grouped under `Module/`:

```
Module/
├── Product/
│   └── model/          (product.model)
└── Order/
    ├── app/            (order.app)
    ├── processing/     (order.processing)
    ├── stream/         (order.stream)
    └── storage/        (order.storage)
```

Java module names keep their `order.*` / `product.*` prefixes; only the folders
use the short purpose names.

## Modules

| Folder | Maven artifact | JPMS module | Responsibility |
|---|---|---|---|
| `Module/Product/model` | `org.example:model` | `product.model` | `Product` hierarchy, `Electronics`, `Clothing`, Lombok builders |
| `Module/Order/processing` | `org.example:processing` | `order.processing` | `OrderProcessor<T extends Product>`, generic-specific exceptions |
| `Module/Order/stream` | `org.example:stream` | `order.stream` | Parallel streams, lambdas, method references, executor threads |
| `Module/Order/storage` | `org.example:storage` | `order.storage` | Concurrent generic storage; hidden `internal` package |
| `Module/Order/app` | `org.example:app` | `order.app` | JavaFaker data generation and runnable demo |

Module graph (arrows mean `requires`):

```
order.app -> order.stream -> order.processing -> product.model
    |            \               /
    |             -> order.storage
    ----------------------------^
```

`order.storage` exports only `com.example.order.storage`;
`com.example.order.storage.internal` is not exported, so its `StorageIndex`
class is inaccessible from other modules. `order.app` exports nothing.

## Key Java features used

- **Generic bound:** `OrderProcessor<T extends Product>`, `OrderStorage<T extends Product>`.
- **Erasure-safe validation:** a `Class<T>` token and `GenericTypeMismatchException`
  catch a wrong subtype that raw/unchecked code could otherwise pass silently.
- **Exception handling:** step failures are wrapped in `OrderProcessingException`.
- **Lombok:** `@Builder` constructors generate `Electronics.builder()` / `Clothing.builder()`.
- **JavaFaker:** seeded fake names, prices, brands, sizes and materials.
- **Lambdas / method references:** `processor::process`, `type::cast`,
  `electronicsStorage::save`, lambda predicates and `Callable` tasks.
- **Multithreading:** a fixed `ExecutorService` for electronics and a parallel
  stream (ForkJoin common pool) for clothing.
- **JPMS:** `module-info.java` in every module with explicit `requires`/`exports`.

## Build and run

Java 17+ is required for the compiled bytecode; this project was verified with
JDK 27 and Lombok 1.18.48 (the first Lombok release supporting JDK 27).

The included Maven wrapper downloads Maven 3.9.16, so no global Maven install is
needed.

### Easy launcher (recommended)

From the PR4 root folder:

```powershell
.\run.cmd
```

That builds the modules and opens the interactive CLI in one step. To run the
original non-interactive demo instead:

```powershell
.\run.cmd demo
```

On macOS/Linux/Git Bash:

```bash
sh ./run.sh
sh ./run.sh demo
```

Both launchers use the JDK path known to work on this machine. If your JDK is
somewhere else, set `PR4_JAVA_HOME` before running them.

### Manual Maven commands

```powershell
# If JAVA_HOME is stale on this machine, point it at the installed JDK first:
$env:JAVA_HOME = 'C:\Program Files\Java\jdk-27.0.1.1'

.\mvnw.cmd clean verify
.\mvnw.cmd -pl Module/Order/app -am package exec:java
```

The second command opens the interactive CLI. The original demo can also be run
manually:

```powershell
.\mvnw.cmd -pl Module/Order/app -am package exec:java '-Dexec.args=--demo'
```

`verify` also runs the JUnit tests in `Module/Order/processing`,
`Module/Order/stream` and `Module/Order/storage`. Compiled jars are written to
each module's `target/` folder.

### CLI menu

The CLI uses plain-language stages and shows the current counts on every screen:

- **Waiting** — created, but not checked yet
- **Checked** — passed the checks, ready to save
- **Saved** — stored, searchable by full ID

| Option | Action |
|---|---|
| 1 | Guided tour — does the whole flow once (recommended first run) |
| 2 | Add orders — type your own or generate fake ones |
| 3 | See waiting orders |
| 4 | Check waiting orders — sequential, parallel, or fixed worker team |
| 5 | See checked results |
| 6 | Save checked results |
| 7 | Browse saved orders, find by ID, show workers |
| 8 | Find orders of one type in the mixed waiting list |
| 9 | Try the generic type-safety demo |
| 10 | More options — seed, reset, original demo |
| 0 | Quit |

Shortcuts work at menus: `b` = back in submenus, `h` or `?` = help, `q` = quit.
Copy the **full ID** from a saved listing when using *Find by ID*.

### Note on JavaFaker and JPMS

JavaFaker 1.0.2 ships no module descriptor and no `Automatic-Module-Name`, so
Maven derives the automatic module name `javafaker` from the jar filename;
`order.app` declares `requires javafaker;` in its `module-info.java`. Its
`requires java.sql;` is needed because JavaFaker's SnakeYAML dependency
references `java.sql.Date`, a module that is not resolved by default.
