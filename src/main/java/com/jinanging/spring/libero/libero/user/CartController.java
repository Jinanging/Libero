package com.jinanging.spring.libero.libero.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jinanging.spring.libero.libero.book.aladin.Service.AladinService;
import com.jinanging.spring.libero.libero.user.domain.Cart;
import com.jinanging.spring.libero.libero.user.domain.OrderInfo;
import com.jinanging.spring.libero.libero.user.service.CartService;
import com.jinanging.spring.libero.libero.user.service.OrderInfoService;
import com.jinanging.spring.libero.libero.user.service.OrderService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/libero/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final OrderInfoService orderInfoService;
    private final OrderService orderService;
    private final AladinService aladinService;
    @GetMapping("/end")
    public String endView() {
    	return "book/end";
    }
    // 1️⃣ 장바구니 페이지 조회 (책 정보 포함)
    @GetMapping("/view")
    public String cartView(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/libero/user/login-view";
        }

        List<Cart> cartList = cartService.getCartList(userId);
        List<Map<String, Object>> cartWithBookList = new ArrayList<>();

        for (Cart cart : cartList) {
            Map<String, Object> book = aladinService.getBookById(cart.getBookId());
            if (book != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("cart", cart);
                map.put("title", book.get("title"));
                map.put("cover", book.get("cover"));
                map.put("price", book.get("priceSales")); // priceSales로 변경
                cartWithBookList.add(map);
            }
        }

        model.addAttribute("cartList", cartWithBookList);
        return "user/cart";
    }

    // 2️⃣ 수량 증가
    @PostMapping("/increase/{cartId}")
    public String increaseCount(@PathVariable long cartId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/libero/user/login-view";

        cartService.increaseCount(cartId);
        return "redirect:/libero/cart/view";
    }

    // 3️⃣ 수량 감소
    @PostMapping("/decrease/{cartId}")
    public String decreaseCount(@PathVariable long cartId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/libero/user/login-view";

        cartService.decreaseCount(cartId);
        return "redirect:/libero/cart/view";
    }

    // 4️⃣ 장바구니 삭제
    @PostMapping("/delete/{cartId}")
    public String deleteCart(@PathVariable long cartId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/libero/user/login-view";

        cartService.deleteCart(cartId);
        return "redirect:/libero/cart/view";
    }

    // 5️⃣ 전체 주문
    @PostMapping("/orderAll")
    public String orderAll(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/libero/user/login-view";

        // TODO: 주문 정보는 실제 폼 입력 등에서 받아서 처리할 것
        String address = "기본주소";       
        String addressDetail = "상세주소"; 
        String addressNumber = "12345";   
        String status = "결제대기";       

        // 1. 주문정보 생성
        OrderInfo orderInfo = orderInfoService.createOrderInfo(userId, address, addressDetail, addressNumber, status);

        // 2. 장바구니 항목으로 주문 생성
        orderService.createOrdersFromCart(orderInfo, userId);

        // 3. 장바구니는 서비스에서 자동으로 비워짐
        return "redirect:/libero/order/history";
    }

    // 6️⃣ 장바구니에 상품 추가 - AJAX 호출용 JSON 반환 메서드로 수정
    @PostMapping("/add")
    @ResponseBody
    public Map<String, Object> addToCart(@RequestParam("bookId") long bookId,
                                         @RequestParam(value = "count", defaultValue = "1") int count,
                                         HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            result.put("success", false);
            result.put("message", "로그인이 필요합니다.");
            return result;
        }

        Cart cart = cartService.addToCart(userId, bookId, count);

        if (cart != null) {
            result.put("success", true);
            result.put("message", "장바구니에 추가되었습니다.");
        } else {
            result.put("success", false);
            result.put("message", "장바구니 추가에 실패했습니다.");
        }

        return result;
    }
}
