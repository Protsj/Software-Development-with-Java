module order.storage {
    requires product.model;
    requires order.processing;

    exports com.example.order.storage;
    // com.example.order.storage.internal is intentionally NOT exported:
    // its classes cannot be referenced from any other module.
}
