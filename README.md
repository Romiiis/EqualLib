<h1 align="center">
  <img src="src/main/resources/EqualLibIcon-noBgr.png" alt="EqualLib Icon" width="75"><br>
  EqualLib
</h1>
<p align="center">Know when two objects are truly equal</p>

---

## 🧠 Overview

EqualLib is a Java library for deep comparison of objects, taking into account attribute values, nested structures, and custom comparison configurations. It provides a simple static API that can be extended using a configuration class.

---

## 🚀 Features

- Deep recursive comparison of Java objects
- Supports custom configuration (ignored fields, max depth, etc.)
- Fluent API for configuration
- Designed for modular and non-modular Java projects
- Optional debug logging for troubleshooting

---

## 📦 Installation

EqualLib is available as an open-source project on [GitHub](https://github.com/Romiiis/EqualLib).

### 1. Clone the repository

```bash
git clone https://github.com/Romiiis/EqualLib
cd EqualLib
```

### 2. Build the project with Maven

To build the project and generate a JAR file:

```bash
mvn clean package
```

The compiled JAR file will be located in the `target/` directory.

### 3. Install to local Maven repository (optional)

If you want to use EqualLib as a dependency in other Maven-based projects:

```bash
mvn clean install
```

### 4. Add dependency to your `pom.xml`

```xml
<dependency>
  <groupId>com.romiis</groupId>
  <artifactId>EqualLib</artifactId>
  <version>YOUR_VERSION_HERE</version>
</dependency>
```

If you're using Gradle or another build tool, use the equivalent way of including a local or installed Maven dependency.

---

## ▶️ Usage

### Basic Usage

```java
EqualLib.areEqual(obj1, obj2); // returns true or false
```

### Custom Configuration

You can use a fluent API to configure how objects are compared:

```java
EqualLibConfig config = new EqualLibConfig()
    .setCompareCollectionsByElements(true) // compare elements in collections
    .setCompareInheritedFields(false)      // ignore inherited fields
    .setIgnoredFieldPaths("com.example.MyClass.myField") // skip specific fields
    .setCustomEqualsClasses("com.example.MyCustomClass") // use custom equals() for specific classes
    .setMaxComparisonDepth(3, true)        // compare up to depth 3, then fallback to equals()
    .setDebugEnabled(true);                // enable debug output

EqualLib.areEqual(obj1, obj2, config);
```

Each method call sets one aspect of the configuration, making it easy to tailor the comparison logic to your needs.

---

## ⚙️ Configuration Options

| Method | Description |
|--------|-------------|
| `setMaxComparisonDepth(int depth, boolean fallbackToEquals)` | Max comparison depth and what to do after that |
| `setIgnoredFieldPaths(String paths)` | Paths to fields that should be ignored |
| `setCustomEqualsClasses(String classPaths)` | Use custom `equals()` for listed classes |
| `setCompareInheritedFields(boolean flag)` | Whether to compare inherited fields |
| `setCompareCollectionsByElements(boolean flag)` | Compare collections element by element |
| `setDebugEnabled(boolean flag)` | Show debug information in logs |

---

## 🧩 Java Modules Support

If you're using Java modules, you need to explicitly open access to required modules:

### JVM Arguments Example

```bash
--add-opens java.base/java.util=EqualLib
--add-opens java.base/java.lang=EqualLib
--add-opens java.base/java.security=EqualLib
--add-opens java.base/java.io=EqualLib
--add-opens java.base/jdk.internal=EqualLib
```

### module-info.java Example

```java
requires EqualLib;
opens com.romiis.myPackage to EqualLib;
```

For non-modular applications, use the `ALL-UNNAMED` target:

```bash
--add-opens java.base/java.util=ALL-UNNAMED
--add-opens java.base/java.lang=ALL-UNNAMED
--add-opens java.base/java.security=ALL-UNNAMED
--add-opens java.base/java.io=ALL-UNNAMED
--add-opens java.base/jdk.internal=ALL-UNNAMED
```

---

## ⚠️ Limitations

- Not thread-safe (use appropriate synchronization in multi-threaded environments)
- Requires specific JVM arguments for access
- Uses reflection (consider security implications in sensitive environments)

---

## 📖 License & Contribution

This project is open-source under the MIT License.

Feel free to submit issues or pull requests. Contributions are welcome!

---

## 📚 Resources

- [Project Repository](https://github.com/Romiiis/EqualLib)
- [Issue Tracker](https://github.com/Romiiis/EqualLib/issues)