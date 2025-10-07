package payloads;

import com.github.javafaker.Faker;
import pojo.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class Payload {
    private static final Faker faker = new Faker();
    private static final String[] categories = {"furniture", "electronics"};
    private static final Random rnd = new Random();

    public static Product productPayload() {
        String name = faker.commerce().productName();
        Double price = Double.parseDouble(faker.commerce().price());
        String description = faker.lorem().sentence();
        String imgUrl = "https://fakestoreapi.com/img/71pWzhdJNwL._AC_UL640_QL65_ML3_t.png";
        String category = categories[rnd.nextInt(categories.length)];
        //Assign this data to Pojo class Product
        new Product(name, price, description, imgUrl, category);
        return new Product(name, price, description, imgUrl, category);
    }
    public static User userPayload() {

        String firstName = faker.name().firstName();
        String lastName  = faker.name().lastName();
        String city = faker.address().city();
        String street =faker.address().streetName();
        int streetNumber = Integer.parseInt(faker.address().streetAddressNumber());
        String zipcode = faker.address().zipCode();
        String lat = faker.address().latitude();
        String longt = faker.address().longitude();

        Name name = new Name (firstName, lastName);
        Geolocation geo = new Geolocation(lat, longt);
        Address address = new Address(city,street,streetNumber,zipcode,geo);

        String email = faker.internet().emailAddress();
        String username = faker.name().username();
        String password = faker.internet().password();
        String phone = String.valueOf(faker.phoneNumber());
        return new User(email,username, password, name, address, phone);
    }
    public static Cart cartPayload(int userId) {
        List<CartProduct> products = new ArrayList<>();


        // Adding one random product to the cart
        int productId = rnd.nextInt(100);
        int quantity = rnd.nextInt(10) + 1;

        CartProduct cartProduct= new CartProduct(productId, quantity);
        products.add(cartProduct);
        //new Date()  ----> Returns date like  Wed Feb 19 13:17:45 IST 202
        // We need to convert this to "yyyy-MM-dd" format in String
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);// Define output date format
        String date = outputFormat.format(new Date());//Converting to String

        return new Cart(userId, date, products);
    }
    //Login
    public static Login loginPayload()
    {
        String username=faker.name().username();
        String password=faker.internet().password();
        Login login=new Login(username,password);
        return login;

    }

}
