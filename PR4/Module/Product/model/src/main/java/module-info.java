module product.model {
    // Lombok annotations are SOURCE-retention; `static` keeps it out of the runtime graph.
    requires static lombok;

    exports com.example.product.model;
}
