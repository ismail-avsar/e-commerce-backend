package com.bandage.ecommerce.config;

import com.bandage.ecommerce.entity.Address;
import com.bandage.ecommerce.entity.AppUser;
import com.bandage.ecommerce.entity.Category;
import com.bandage.ecommerce.entity.CreditCard;
import com.bandage.ecommerce.entity.Product;
import com.bandage.ecommerce.entity.Role;
import com.bandage.ecommerce.entity.Store;
import com.bandage.ecommerce.repository.AddressRepository;
import com.bandage.ecommerce.repository.AppUserRepository;
import com.bandage.ecommerce.repository.CategoryRepository;
import com.bandage.ecommerce.repository.CreditCardRepository;
import com.bandage.ecommerce.repository.ProductRepository;
import com.bandage.ecommerce.repository.RoleRepository;
import com.bandage.ecommerce.repository.StoreRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {
    private static final String IMAGE_BASE = "http://localhost:8080/images/products/";

    @Bean
    CommandLineRunner seedData(
            RoleRepository roleRepository,
            AppUserRepository userRepository,
            StoreRepository storeRepository,
            CategoryRepository categoryRepository,
            ProductRepository productRepository,
            AddressRepository addressRepository,
            CreditCardRepository creditCardRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (roleRepository.count() == 0) {
                roleRepository.saveAll(List.of(
                        new Role("Customer", "customer"),
                        new Role("Store", "store"),
                        new Role("Admin", "admin")
                ));
            }

            Role customerRole = roleRepository.findByCode("customer").orElseThrow();
            Role storeRole = roleRepository.findByCode("store").orElseThrow();
            Role adminRole = roleRepository.findByCode("admin").orElseThrow();

            AppUser customer = createUserIfMissing(userRepository, passwordEncoder, "Mock Customer", "customer@commerce.com", customerRole);
            AppUser storeUser = createUserIfMissing(userRepository, passwordEncoder, "Mock Store", "store@commerce.com", storeRole);
            createUserIfMissing(userRepository, passwordEncoder, "Mock Admin", "admin@commerce.com", adminRole);

            Store store = storeRepository.findAll().stream().findFirst().orElseGet(() -> {
                Store newStore = new Store();
                newStore.setName("Bandage Store");
                newStore.setPhone("+905551112233");
                newStore.setTaxNo("T1234V123456");
                newStore.setBankAccount("TR123456789012345678901234");
                newStore.setUser(storeUser);
                return storeRepository.save(newStore);
            });

            if (categoryRepository.count() == 0) {
                categoryRepository.saveAll(List.of(
                        new Category("k:tisort", "T-shirt", "k", 4.8, IMAGE_BASE + "product-1.jpg"),
                        new Category("k:elbise", "Elbise", "k", 4.7, IMAGE_BASE + "product-2.jpg"),
                        new Category("k:ceket", "Ceket", "k", 4.6, IMAGE_BASE + "product-3.jpg"),
                        new Category("k:ayakkabi", "Ayakkabi", "k", 4.5, IMAGE_BASE + "product-4.jpg"),
                        new Category("e:tisort", "T-shirt", "e", 4.8, IMAGE_BASE + "product-5.jpg"),
                        new Category("e:gomlek", "Gomlek", "e", 4.7, IMAGE_BASE + "product-6.jpg"),
                        new Category("e:ceket", "Ceket", "e", 4.5, IMAGE_BASE + "product-7.jpg"),
                        new Category("e:ayakkabi", "Ayakkabi", "e", 4.4, IMAGE_BASE + "product-8.jpg")
                ));
            }

            if (productRepository.count() == 0) {
                List<Category> categories = categoryRepository.findAll();
                productRepository.saveAll(List.of(
                        product("Gri Regular Astar", "Gri regular astar detayli dokuma blazer ceket", "k:ceket", store, categories, "461.99", 140, 3.64, 281, "product-1.jpg"),
                        product("Beyaz Basic T-shirt", "Pamuklu rahat kesim beyaz basic kadin t-shirt", "k:tisort", store, categories, "149.99", 85, 4.60, 120, "product-2.jpg"),
                        product("Siyah Midi Elbise", "Gunluk kullanim icin siyah midi elbise", "k:elbise", store, categories, "389.90", 47, 4.72, 96, "product-3.jpg"),
                        product("Krem Sneaker", "Rahat tabanli krem kadin sneaker", "k:ayakkabi", store, categories, "699.90", 32, 4.42, 52, "product-4.jpg"),
                        product("Erkek Lacivert T-shirt", "Yumusak dokulu lacivert erkek t-shirt", "e:tisort", store, categories, "179.99", 105, 4.51, 144, "product-5.jpg"),
                        product("Mavi Oxford Gomlek", "Slim fit mavi oxford erkek gomlek", "e:gomlek", store, categories, "329.90", 64, 4.33, 83, "product-6.jpg"),
                        product("Yesil Classic Product", "Modern kesim yesil erkek ceket", "e:ceket", store, categories, "899.90", 24, 4.21, 47, "product-7.jpg"),
                        product("Siyah Deri Ayakkabi", "Klasik siyah erkek deri ayakkabi", "e:ayakkabi", store, categories, "1199.90", 18, 4.78, 67, "product-8.jpg"),
                        product("Pembe Crop T-shirt", "Yaz koleksiyonu pembe crop t-shirt", "k:tisort", store, categories, "159.90", 76, 4.12, 38, "product-1.jpg"),
                        product("Kahverengi Trenckot", "Mevsimlik kahverengi trenckot", "k:ceket", store, categories, "799.90", 19, 4.59, 75, "product-2.jpg"),
                        product("Sari Yazlik Elbise", "Hafif kumas sari yazlik elbise", "k:elbise", store, categories, "429.90", 39, 4.36, 61, "product-3.jpg"),
                        product("Beyaz Spor Ayakkabi", "Gunluk beyaz spor ayakkabi", "k:ayakkabi", store, categories, "649.90", 42, 4.68, 99, "product-4.jpg"),
                        product("Kirmizi Oversize T-shirt", "Oversize kalip kirmizi erkek t-shirt", "e:tisort", store, categories, "199.90", 58, 4.05, 44, "product-5.jpg"),
                        product("Beyaz Keten Gomlek", "Yazlik beyaz keten gomlek", "e:gomlek", store, categories, "379.90", 28, 4.30, 51, "product-6.jpg"),
                        product("Antrasit Bomber Ceket", "Antrasit erkek bomber ceket", "e:ceket", store, categories, "749.90", 33, 4.19, 34, "product-7.jpg"),
                        product("Kahverengi Loafer", "Kahverengi klasik loafer ayakkabi", "e:ayakkabi", store, categories, "999.90", 22, 4.57, 63, "product-8.jpg"),
                        product("Mint Basic T-shirt", "Mint yesili rahat kesim kadin t-shirt", "k:tisort", store, categories, "139.90", 91, 4.44, 88, "product-1.jpg"),
                        product("Desenli Elbise", "Cicek desenli midi kadin elbise", "k:elbise", store, categories, "459.90", 26, 4.66, 70, "product-2.jpg"),
                        product("Kot Ceket", "Mavi denim kadin kot ceket", "k:ceket", store, categories, "529.90", 31, 4.27, 49, "product-3.jpg"),
                        product("Siyah Sneaker", "Minimal siyah sneaker", "k:ayakkabi", store, categories, "719.90", 37, 4.49, 58, "product-4.jpg"),
                        product("Gri Erkek T-shirt", "Gri regular fit erkek t-shirt", "e:tisort", store, categories, "169.90", 112, 4.22, 101, "product-5.jpg"),
                        product("Siyah Gomlek", "Siyah klasik erkek gomlek", "e:gomlek", store, categories, "349.90", 44, 4.14, 46, "product-6.jpg"),
                        product("Lacivert Blazer", "Lacivert slim fit erkek blazer ceket", "e:ceket", store, categories, "1299.90", 14, 4.80, 25, "product-7.jpg"),
                        product("Beyaz Casual Ayakkabi", "Casual beyaz erkek ayakkabi", "e:ayakkabi", store, categories, "849.90", 29, 4.33, 41, "product-8.jpg"),
                        product("Siyah Basic T-shirt", "Siyah basic pamuklu kadin t-shirt", "k:tisort", store, categories, "149.90", 100, 4.70, 130, "product-1.jpg")
                ));
            }

            if (addressRepository.findByUserOrderByIdAsc(customer).isEmpty()) {
                Address home = new Address();
                home.setUser(customer);
                home.setTitle("Ev");
                home.setName("Mock");
                home.setSurname("Customer");
                home.setPhone("05551234567");
                home.setCity("Istanbul");
                home.setDistrict("Kadikoy");
                home.setNeighborhood("Caferaga");
                home.setAddress("Moda Cad. No:1");
                addressRepository.save(home);
            }

            if (creditCardRepository.findByUserOrderByIdAsc(customer).isEmpty()) {
                CreditCard card = new CreditCard();
                card.setUser(customer);
                card.setCardNo("************3456");
                card.setExpireMonth(12);
                card.setExpireYear(2028);
                card.setNameOnCard("Mock Customer");
                creditCardRepository.save(card);
            }
        };
    }

    private AppUser createUserIfMissing(
            AppUserRepository userRepository,
            PasswordEncoder passwordEncoder,
            String name,
            String email,
            Role role
    ) {
        return userRepository.findByEmailIgnoreCase(email).orElseGet(() -> {
            AppUser user = new AppUser();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRole(role);
            return userRepository.save(user);
        });
    }

    private Product product(
            String name,
            String description,
            String categoryCode,
            Store store,
            List<Category> categories,
            String price,
            int stock,
            double rating,
            int sellCount,
            String image
    ) {
        Category category = categories.stream()
                .filter(item -> item.getCode().equals(categoryCode))
                .findFirst()
                .orElseThrow();

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setCategory(category);
        product.setStore(store);
        product.setPrice(new BigDecimal(price));
        product.setStock(stock);
        product.setRating(rating);
        product.setSellCount(sellCount);
        product.addImage(IMAGE_BASE + image, 0);
        return product;
    }
}
