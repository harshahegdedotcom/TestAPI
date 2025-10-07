package pojo;
import java.util.List;
public record Cart(int userId, String date, List<CartProduct> products) {

}
