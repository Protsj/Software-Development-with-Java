module order.app {
    requires product.model;
    requires order.processing;
    requires order.stream;
    requires order.storage;

    // Maven places JavaFaker on the module path as the automatic module "javafaker".
    requires javafaker;

    // SnakeYAML (pulled in by JavaFaker) references java.sql.Date.
    requires java.sql;

    // No exports: this is an application module, not a library.
}
