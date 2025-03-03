# EqualLib library for deepComparison

--add-opens java.base/java.lang=ALL-UNNAMED
--add-opens java.base/java.util=ALL-UNNAMED
--add-opens java.base/java.io=ALL-UNNAMED
--add-opens java.base/java.nio=ALL-UNNAMED
--add-opens java.base/java.math=ALL-UNNAMED

This library implementing "areEqual" method that accepts two objects and deeply compares them. Every field is compared deeply. Library using reflection. Be careful.