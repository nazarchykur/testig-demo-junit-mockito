package com.example.sbtesting.services;

import com.example.sbtesting.entities.Order;
import com.example.sbtesting.entities.Payment;
import com.example.sbtesting.exceptions.PaymentException;
import com.example.sbtesting.repositories.OrderRepository;
import com.example.sbtesting.repositories.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/*
    https://www.arhohuttunen.com/spring-boot-unit-testing/
    
    https://www.youtube.com/watch?v=Ae5ukd136pc&list=PLRhHH6sj6xkxp5qxb5g3Rlfj_8lRNK2Qi
    
    
    
    we should not test the implementation but the behaviour
    
    A test is not a unit test if:
        > It talks to the database
        > It communicates across the network
        > It touches the file system
        > It can’t run at the same time as any of your other unit tests
        > You have to do special things to your environment (such as editing config files) to run it
        
        
    If your test does any of the above, it’s an integration test. Some people think that integration testing means 
    that you test the entire application, but that’s not true. You could, for example, integration test your data 
    access layer in isolation.
    
    
        
            
    Don’t Use Spring to Write Unit Tests
    
         Furthermore, here is a test that tests the service:

                @SpringBootTest
                class OrderServiceTests {
                    @Autowired
                    private OrderRepository orderRepository;
                    @Autowired
                    private OrderService orderService;
                
                    @Test
                    void payOrder() {
                        Order order = new Order(1L, false);
                        orderRepository.save(order);
                
                        Payment payment = orderService.pay(1L, "4532756279624064");
                
                        assertThat(payment.getOrder().isPaid()).isTrue();
                        assertThat(payment.getCreditCardNumber()).isEqualTo("4532756279624064");
                    }
                }
                
                
       So, what’s wrong with a test like this? Well, this is not a unit test. When we use the @SpringBootTest annotation, 
       Spring loads up an application context for the test. In practice, we have started the whole application only 
       to autowire the OrderService into the test.

        Another problem is that we have to write orders to and read them from the database. While this could be 
        something that we want to do in the integration tests, it’s not desirable in unit tests. Remember that we 
        want to test the unit in isolation.
        
        If we want to isolate the test from the database and we are already familiar with Spring Boot and Mockito, 
        we might ask: why not just annotate the repositories with @MockBean then?

                    @SpringBootTest
                    class OrderServiceTests {
                        @MockBean
                        private OrderRepository orderRepository;
                        @MockBean
                        private PaymentRepository paymentRepository;
                        @Autowired
                        private OrderService orderService;
                    
                        @Test
                        void payOrder() {
                            Order order = new Order(1L, false);
                            when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
                            when(paymentRepository.save(any())).then(returnsFirstArg());
                    
                            Payment payment = orderService.pay(1L, "4532756279624064");
                    
                            assertThat(payment.getOrder().isPaid()).isTrue();
                            assertThat(payment.getCreditCardNumber()).isEqualTo("4532756279624064");
                        }
                    }


        We could use mocks here, and it’s something we can use in our integration tests. However, it’s still going to 
        be much slower than writing a plain unit test.

        Furthermore, every time we use @MockBean in our tests, Spring will create a new application context in the tests, 
        unable to use a cached version of the context. Having to create new context adds to the overall execution 
        time of the tests.
        
        
        we had a service where we injected the repositories as fields. There’s no way to pass the repository 
        instances to the service if we instantiate with the new operator:
                        @Service
                        public class OrderService {
                            @Autowired
                            private OrderRepository orderRepository;
                            @Autowired
                            private PaymentRepository paymentRepository;
                        
                            public Payment pay(Long orderId, String creditCardNumber) {
                                Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
                        
                                if (order.isPaid()) {
                                    throw new PaymentException();
                                }
                        
                                orderRepository.save(order.markPaid());
                                return paymentRepository.save(new Payment(order, creditCardNumber));
                            }
                        }
                        

        The solution is not to use field injection at all. Instead, we should use constructor injection:
        
                        @Service
                        public class OrderService {
                            private final OrderRepository orderRepository;
                            private final PaymentRepository paymentRepository;
                        
                            public OrderService(OrderRepository orderRepository, PaymentRepository paymentRepository) {
                                this.orderRepository = orderRepository;
                                this.paymentRepository = paymentRepository;
                            }
                            
                            // ...
                        }
                        
                        
                        
        When we provide a constructor with the repositories as parameters, Spring will automatically inject those 
        into the service. We can also make the repository fields final because there’s no need for them to change.
        
        We can also reduce boilerplate code by using Lombok:
        
                @Service
                @RequiredArgsConstructor
                public class OrderService {
                    private final OrderRepository orderRepository;
                    private final PaymentRepository paymentRepository;
                    
                    // ...
                } 
                
    When the class has final fields, using the Lombok @RequiredArgsConstructor will automatically create a constructor 
    with those parameters.
    
    
    
    
    
    Write a Unit Test
    
        It’s now possible to pass the repository instances to the service as constructor arguments. 
        Now we can write a unit test for the service:
        
                class OrderServiceTests {
                    private OrderRepository orderRepository;
                    private PaymentRepository paymentRepository;
                    private OrderService orderService;
                
                    @BeforeEach
                    void setupService() {
                        orderRepository = mock(OrderRepository.class);
                        paymentRepository = mock(PaymentRepository.class);
                        orderService = new OrderService(orderRepository, paymentRepository);
                    }
                    
                    @Test
                    void payOrder() {
                    ...
                    }
                }
                
                
                
        Since we don’t want to touch the database, we are using Mockito to replace the actual implementations of 
        the repositories with mocks. The test now runs in milliseconds instead of seconds.

        We can further reduce boilerplate in the test code if we use the MockitoExtension extension:
                @ExtendWith(MockitoExtension.class)
                class OrderServiceTests {
                    @Mock
                    private OrderRepository orderRepository;
                    @Mock
                    private PaymentRepository paymentRepository;
                    @InjectMocks
                    private OrderService orderService;
                    
                    // ...
                }
                
        
        With quite a simple change, we managed to make the test independent of Spring. The test is now fast and isolated.       
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private OrderService serviceUnderTest;

    @Test
    void pay_successful() throws Exception {
        Order order = new Order(1L, false);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(paymentRepository.save(any())).then(returnsFirstArg());
        
        Payment payment = serviceUnderTest.pay(1L, "1111");

        assertThat(payment.getOrder().isPaid()).isTrue();
        assertThat(payment.getCreditCardNumber()).isEqualTo("1111");
    }

    @Test
    void pay_alreadyPaid() {
        Order order = new Order(1L, true);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(PaymentException.class,
                () -> serviceUnderTest.pay(1L, "1111"));
    }
}