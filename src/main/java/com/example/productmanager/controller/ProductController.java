package com.example.productmanager.controller;

import com.example.productmanager.entity.Product;
import com.example.productmanager.entity.Category;
import com.example.productmanager.entity.Review;
import com.example.productmanager.entity.User;
import com.example.productmanager.service.ImageService;
import com.example.productmanager.service.ProductService;
import com.example.productmanager.service.CategoryService;
import com.example.productmanager.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ImageService imageService; // Thêm ImageService
    private final CategoryService categoryService;
    private final ReviewService reviewService;


    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService, ImageService imageService, ReviewService reviewService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.imageService = imageService; // Inject ImageService

        this.reviewService = reviewService;
    }

    @PostMapping("/toggle-hide")
    public String toggleHideProduct(@RequestParam("productId") int productId) {
        // Tìm sản phẩm theo productId
        Product product = productService.findById(productId);

        // Đảo ngược giá trị hide
        product.setHide(!product.getHide()); // Sử dụng getter

        // Lưu sản phẩm vào cơ sở dữ liệu
        productService.save(product);

        if (!product.getHide()){
            return "redirect:/products/list-hidden";
        } else {
            return "redirect:/products/list-available";
        }
    }



    //=======================PRODUCT=======================================================
@GetMapping("/product-list")
public String showProductList(Model model, HttpSession session) {
    // Lấy thông tin người dùng từ session
    User loggedInUser = (User) session.getAttribute("loggedInUser");
    if (loggedInUser == null) {
        return "redirect:/users/login"; // Redirect to login if not logged in
    }
    if (loggedInUser.getRole() == UserController.UserRole.ADMIN.getValue()) {
        List<Product> products = productService.findByHideOrderByCreatedAtDesc(false); // Sắp xếp theo thời gian gần nhất
        model.addAttribute("products", products);
    }
    else if (loggedInUser.getRole() == UserController.UserRole.SELLER.getValue()) {
        List<Product> products = productService.findProductsBySeller(loggedInUser.getUserId(), false);
        model.addAttribute("products", products);
    }
    model.addAttribute("loggedInUser", loggedInUser);

    return "products/list-products";
}

    @GetMapping("/list-available")
    public String listAvailableProducts(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }
        if (loggedInUser.getRole() == 0) {
            return "home";
        }

        List<Product> availableProducts;
        if (loggedInUser.getRole() == 2) {
            availableProducts = productService.findAvailableProducts();
        } else {
            availableProducts = productService.findAvailableProductsBySeller(loggedInUser.getUserId());
        }
        model.addAttribute("loggedInUser", loggedInUser);

        model.addAttribute("products", availableProducts);
        return "products/list-products";
    }







    @GetMapping("/list-zero-quantity")
    public String listZeroQuantityProducts(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }
        if (loggedInUser.getRole() == 0) {
            return "home";
        }

        List<Product> zeroQuantityProducts;
        if (loggedInUser.getRole() == 2) {
            zeroQuantityProducts = productService.findZeroQuantityProducts();
        } else {
            zeroQuantityProducts = productService.findZeroQuantityProductsBySeller(loggedInUser.getUserId());
            // Lưu ý: Điều kiện "hide = false" có thể cần điều chỉnh theo yêu cầu thực tế.
        }
        model.addAttribute("loggedInUser", loggedInUser);

        model.addAttribute("products", zeroQuantityProducts);
        return "products/list-products";
    }

    @GetMapping("/list-hidden")
    public String listHiddenProducts(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }
        if (loggedInUser.getRole() == 0) {
            return "home";
        }

        List<Product> hiddenProducts;
        if (loggedInUser.getRole() == 2) {
            hiddenProducts = productService.findHiddenProducts();
        } else {
            hiddenProducts = productService.findProductsBySeller(loggedInUser.getUserId(), true);
        }
        model.addAttribute("loggedInUser", loggedInUser);

        model.addAttribute("products", hiddenProducts);
        return "products/list-products";
    }



    @GetMapping("/search")
    public String searchProducts(@RequestParam(name = "query", required = false) String query,
                                 Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser); // Cập nhật thông tin người dùng trong model

        List<Product> searchResults = productService.findAvailableProductsByName(query);
        Collections.sort(searchResults, (product1, product2) -> {
            // Sắp xếp dựa trên vip_id của người bán
            int vipComparison = Integer.compare(product2.getSeller().vip, product1.getSeller().vip);
            if (vipComparison != 0) {
                return vipComparison;
            }
            // Nếu vip_id bằng nhau hoặc không có vip_id, sắp xếp theo tên sản phẩm
            return product1.getName().compareToIgnoreCase(product2.getName());
        });

        model.addAttribute("products", searchResults);
        model.addAttribute("query", query); // Đảm bảo giá trị tìm kiếm được truyền vào view
        return "products/search-results"; // Trả về tên template hiển thị kết quả tìm kiếm
    }

    //=======================SEARCH==BY==CATEGORY=======================================================

    @GetMapping("/searchs")
    public String searchProductsByCategory(@RequestParam(name = "category_id", required = false) Long categoryId,
                                           Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser); // Cập nhật thông tin người dùng trong model

        List<Product> searchResults;
        if (categoryId == null) {
            return "home";
        }
            searchResults = productService.findAvailableProductsByCategoryId(categoryId);
            // Sắp xếp kết quả tìm kiếm theo vip_id của người bán
            Collections.sort(searchResults, (product1, product2) -> {
                int vipComparison = Integer.compare(product2.getSeller().vip,
                        product1.getSeller().vip);
                if (vipComparison != 0) {
                    return vipComparison;
                }
                return product1.getName().compareToIgnoreCase(product2.getName());
            });


        model.addAttribute("products", searchResults);
        return "products/search-results";
    }





    @GetMapping("/details/{productId}")
    public String showProductDetails(@PathVariable("productId") long productId, Model model, HttpSession session) {
        // Lấy thông tin người dùng từ session
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }
        Product product = productService.findById((int) productId);
        List<Product> sellerProducts = productService.findProductsBySeller(product.getSeller().getUserId(), false);
        model.addAttribute("sellerProducts", sellerProducts); // Truyền danh sách sản phẩm cùng người bán vào view


        // Lấy danh sách đánh giá của sản phẩm
        List<Review> reviews = reviewService.getReviewsByProduct(productId);

        // Tính trung bình rating của sản phẩm
        double averageRating = calculateAverageRating(reviews);

        model.addAttribute("sellerProducts", sellerProducts); // Truyền danh sách sản phẩm cùng người bán vào view
        model.addAttribute("product", product);
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("averageRating", averageRating); // Truyền giá trị trung bình rating vào view

        return "products/product-details";
    }

    // Method tính trung bình rating
    private double calculateAverageRating(List<Review> reviews) {
        if (reviews.isEmpty()) {
            return 0.0;
        }
        double totalRating = 0.0;
        for (Review review : reviews) {
            totalRating += review.getRating();
        }

        return totalRating / reviews.size();
    }


    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("productId") long productId, Model model, HttpSession session) {
        // Lấy thông tin người dùng từ session
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login"; // Redirect to login if not logged in
        }
        if (loggedInUser.getRole() == UserController.UserRole.USER.getValue()) {
            model.addAttribute("error", "Bạn không có quyền vào trang này");
            return "home"; // Redirect with error message
        }

        Product product = productService.findById((int) productId);
        List<Category> categories = categoryService.getAllCategories();

        // Kiểm tra nếu người bán không có quyền chỉnh sửa sản phẩm
        if (loggedInUser.getRole() == UserController.UserRole.SELLER.getValue() && product.getSeller().getUserId() != loggedInUser.getUserId()) {
            model.addAttribute("error", "Bạn không có quyền chỉnh sửa sản phẩm này");
            return "home"; // Redirect with error message
        }

        model.addAttribute("categories", categories);
        model.addAttribute("product", product);
        return "products/products-form";
    }


    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model model, HttpSession session) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        Product product = new Product();


        // Lấy thông tin người dùng từ session
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login"; // Redirect to login if not logged in
        }
        if (loggedInUser.role == 0){
            model.addAttribute("error", "bạn không có quyền vào trang này");
            return "home"; // Redirect to login if not logged in
        }
        product.setSeller(loggedInUser);

        model.addAttribute("product", product);
        return "products/products-form";
    }



    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") @Validated Product product,
                              BindingResult bindingResult,
                              @RequestParam("image") MultipartFile imageFile,
                              @RequestParam("oldImages") String oldImages) {
        if (bindingResult.hasErrors()) {
            return "products/products-form";
        }

        String newImageFileName = null;
        try {
            // Nếu có thay đổi ảnh mới, tải lên và lưu tên tệp ảnh mới
            if (!imageFile.isEmpty()) {
                newImageFileName = imageService.uploadImage(imageFile);
                product.setImages(newImageFileName);
            } else {
                // Nếu không có thay đổi ảnh, sử dụng ảnh cũ
                product.setImages(oldImages);
            }

            productService.save(product);
        } catch (IOException e) {
            e.printStackTrace(); // Xử lý lỗi tải lên hình ảnh
        }

        return "redirect:/products/product-list";
    }

    @PostMapping("/delete")
    public String deleteProduct(@RequestParam("productId") long productId,HttpSession session,Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login"; // Redirect to login if not logged in
        }
        if (loggedInUser.role == 0){
            model.addAttribute("error", "bạn không có quyền thực hiện");
            return "home"; // Redirect to login if not logged in
        }
        productService.deleteById((int) productId);
        return "redirect:/products/product-list";
    }
}
