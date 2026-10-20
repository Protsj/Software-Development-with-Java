import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class Cart {
    private List<Product> products = new ArrayList<>();

    public void addProduct(Product product) { products.add(product); }
    public void removeProduct(Product product) { products.remove(product); }
    public void clear() { products.clear(); }

    public double getTotalPrice() {
        double total = 0;
        for (Product product : products) {
            total += product.getPrice();
        }
        return total;
    }
}
