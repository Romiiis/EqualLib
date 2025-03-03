open module EqualLibTest {
    requires EqualLib;  // Import hlavního modulu knihovny
    requires org.junit.jupiter.api;  // JUnit 5 API
    requires jdk.unsupported;  // Pro sun.misc Unsafe a podobné

}
