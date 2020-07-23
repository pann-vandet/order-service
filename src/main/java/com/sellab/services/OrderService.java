package com.sellab.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sellab.common.Payment;
import com.sellab.common.TransactionRequest;
import com.sellab.common.TransactionResponse;
import com.sellab.entities.Order;
import com.sellab.repositories.OrderRepository;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private RestTemplate template;
	
	// Order and Payment integration 
	public TransactionResponse saveOrder(TransactionRequest request) {
		String response = "";
		Order order = request.getOrder();
		Payment payment  = request.getPayment();
		payment.setOrderId(order.getId());
		payment.setAmount(order.getPrice());
		// rest call
		Payment paymentResponse = template.postForObject("http://localhost:9191/payment/doPayment", payment, Payment.class);
		
		response = paymentResponse.getPaymentStatus().equals("success")?"payment processing and order placed":"there is a faulure in payment api, order added to cart";
		
		orderRepository.save(order);
		
		return new TransactionResponse(order, paymentResponse.getAmount(), paymentResponse.getTransactionId(), response);
	}


	public List<Order> getOrders() {
		return orderRepository.findAll();
	}
}
