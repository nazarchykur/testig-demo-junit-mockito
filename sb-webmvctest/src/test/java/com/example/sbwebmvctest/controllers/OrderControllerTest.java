package com.example.sbwebmvctest.controllers;

import com.example.sbwebmvctest.Receipt;
import com.example.sbwebmvctest.entities.Order;
import com.example.sbwebmvctest.entities.Payment;
import com.example.sbwebmvctest.exceptions.OrderAlreadyPaid;
import com.example.sbwebmvctest.services.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
    
    As we can see, there are at least five distinct cases that unit tests are unable to verify:

        > HTTP request mapping
        > Deserialization
        > Input field validation
        > Serialization
        > Error handling
        
    
    In a Spring application, the framework handles the concerns mentioned above. If we run the application, Spring 
    will introduce all the required beans in the application context.

    We need to be able to test these cases as well. When we introduce Spring to our tests, it means that we are 
    going to write integration tests.
    
    
    
    
    Write an Integration Test With @WebMvcTest
    
        Spring Boot offers several annotations for testing different parts of the application. These annotations scan 
        only a specific set of auto-configuration classes that only provide what is needed to test that part of the 
        application.

        We start by testing the web layer of our application, where we only care about the previously mentioned concerns. 
        We don’t want to involve database calls in those tests, for example.
        
        To test our Spring MVC controllers, we can use the @WebMvcTest annotation. The annotation scans only beans for 
        @Controller, @ControllerAdvice, and a few others related to the web layer.
    
                    @WebMvcTest(OrderController.class)
                    class OrderControllerTests {
                        @MockBean
                        private OrderService orderService;
                        @Autowired
                        private MockMvc mockMvc;
                    
                        @Test
                        void payOrder() throws Exception {
                            // ...
                        }
                    }

    
            We have annotated the test with @WebMvcTest and limited it to a single controller. Since we are 
            not interested in testing the other parts of the application, we have also mocked the OrderService 
            dependency with the @MockBean annotation.
        
            Remember that @WebMvcTest does not scan beans for our services. We have to provide a bean for anything that 
            the controller depends on. If we don’t pass the controller as a parameter to @WebMvcTest, Spring will scan 
            all the controllers, and we have to mock away all beans any controller depends on.
            
            Spring Boot also autoconfigures a MockMvc bean for us so that we can autowire that. Using MockMvc fakes 
            HTTP requests for us, making it possible to run the controller tests without starting an entier HTTP server.
    
    
     Verify HTTP Request Mapping And Deserialization
     
        To verify that the controller handles HTTP requests, we call the mockMvc.perform() to initiate a mock HTTP request. 
        The mock requests are constructed using builders for different HTTP methods like post(), get(), put() and delete(). 
        Taking it further, these builders take arguments like contentType() and content().
       
          ...
             mockMvc.perform(post("/order/{id}/payment", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"creditCardNumber\": \"4532756279624064\"}"))
                .andExpect(status().isCreated());
         ...
         
         
         Once we perform the request, MockMvc allows us to set some expectations through the andExpect() method. 
         Now we can verify that the request was completed successfully by checking the HTTP status code with the 
         status().isCreated() result matcher. By making the request and setting expectations, we verify that the 
         controller responds to a specific URL.

        The test also verifies that the content type is correct, and Spring correctly deserializes JSON input into 
        Java objects annotated with @RequestBody. The test verifies that the controller maps any path parameters 
        annotated with @PathVariable. If we had any query parameters annotated with @RequestParam, the test could 
        also verify that those are mapped correctly.
        
        
   Verify Field Validation

        In our controller, we have annotated the request body parameter with the @Valid annotation. 
        We have also given the request object a constraint:
                    public class PaymentRequest {
                        @NotNull
                        @CreditCardNumber
                        private String creditCardNumber;
                    }
                    
                    
        To verify that the fields gets validated correctly, we can provide a request that is missing the field:
                    @Test
                    void paymentFailsWhenCreditCardNumberNotGiven() throws Exception {
                        mockMvc.perform(post("/order/{id}/payment", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                                .andExpect(status().isBadRequest());
                    }
                    
                    
        If the validation fails, we should get an HTTP status 400 Bad Request as a result.
        
        If the request body has more fields, it can be tempting to validate all those fields in the controller test. 
        However, in controller tests, one could argue that it’s more important to test that validation happens. 
        We could, for example, forget to annotate the request body parameter with the @Valid annotation.
        
        It’s also possible to write separate unit tests for the validation rules. We need to call the Java Validator 
        methods directly and pass the validated object as an argument:
        
                    class PaymentRequestTests {
                        private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
                    
                        @Test
                        void creditCardNumberMustNotBeNull() {
                            String creditCardNumberWithInvalidChecksum = "4532756279624063";
                            PaymentRequest request = new PaymentRequest(creditCardNumberWithInvalidChecksum);
                    
                            Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(request);
                    
                            assertThat(violations).isNotEmpty();
                        }
                    }    
                       
        If we had many validation rules, we could validate the rules using such a unit test. In the controller test, 
        we don’t have to check all the rules - it is enough to trigger the validation once to make sure it happens.
        
        
        
    Verify Result Serialization
        So far we have focused on verifying the requests but what about the responses? It is not necessarily 
        immediately evident but Spring will automatically serialize our responses to JSON. We can verify the results 
        of that serialization using the jsonPath() matcher:
        
                    @Test
                    void getReceiptForOrder() throws Exception {
                        Receipt receipt = new Receipt(
                            LocalDateTime.now(),
                            "4532756279624064",
                            100.0);
                    
                        when(orderService.getReceipt(eq(1L))).thenReturn(receipt);
                    
                        mockMvc.perform(get("/order/{id}/receipt", 1L))
                            .andExpect(jsonPath("$.date").isNotEmpty())
                            .andExpect(jsonPath("$.creditCardNumber").value("4532756279624064"))
                            .andExpect(jsonPath("$.amount").value(100.0));
                    }
        
        
        Here we check that each of the fields for date, creditCardNumber, and amount have been serialized correctly 
        in the JSON response. When we use jsonPath(), it takes the response body and allows us to write JSONPath 
        expressions to validate the results.
            
            
            
            
                
    Verify Error Handling
    
        Spring handles many error cases for us by returning some default HTTP status codes from the controller. 
        However, if we throw any exceptions that we don’t handle, we will get HTTP status 500 Internal Server Error.
        
        Usually we want to translate these exception to more meaningful HTTP status codes. To translate our custom 
        exception we have this kind of exception handler in our controller:
        
                    @ExceptionHandler
                    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
                    @ResponseBody
                    public String handleOrderAlreadyPaid(OrderAlreadyPaid orderAlreadyPaid) {
                        return orderAlreadyPaid.getMessage();
                    }
        
        The simplest way to test that the exception handler is doing its job is to add an expectation about the HTTP status:
                    @Test
                    void cannotPayAlreadyPaidOrder() throws Exception {
                        when(orderService.pay(eq(1L), any())).thenThrow(OrderAlreadyPaid.class);
                    
                        mockMvc.perform(post("/order/{id}/payment", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"creditCardNumber\": \"4532756279624064\"}"))
                                .andExpect(status().isMethodNotAllowed());
                    }
                    
        To trigger the behavior, we are using Mockito to throw an OrderAlreadyPaid exception when the service method 
        gets called with the given order identifier.
        


    Don’t Forget the Business Logic
    
        In the beginning, we established that a unit test could not handle all the responsibilities the controller has. 
        However, we haven’t tested that the business logic gets correctly called!
        
        If we want to be sure that the controller and the service work correctly together, we have to test that the 
        service methods are called with correct arguments. Let’s take a look at a previous example:
        
                @Test
                void payOrder() throws Exception {
                    Order order = new Order(1L, LocalDateTime.now(), 100.0, false);
                    Payment payment = new Payment(1000L, order, "4532756279624064");
                
                    when(orderService.pay(any(), any())).thenReturn(payment);
                
                    mockMvc.perform(post("/order/{id}/payment", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"creditCardNumber\": \"4532756279624064\"}"))
                            .andExpect(status().isCreated());
                }
                
        Here we return a Payment response if the pay() method of the service is called with any arguments. 
        We don’t know if the controller calls the method with the correct arguments.
        
        We can fix the issue with a small change:
            when(orderService.pay(eq(1L), eq("4532756279624064"))).thenReturn(payment);
            
        Now the mock won’t return anything unless the controller is calling the method with these exact arguments. 
        Depending on other use cases, we might have to validate this differently, but this is now sufficient.

    
 */
@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @MockBean
    private OrderService orderService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void payOrder_successful() throws Exception {
        Order order = new Order(1L, LocalDateTime.now(), 100.0, false);
        Payment payment = new Payment(1000L, "4532756279624064", order);

        when(orderService.pay(eq(1L), eq("4532756279624064"))).thenReturn(payment);

        mockMvc.perform(post("/order/{id}/payment", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"creditCardNumber\": \"4532756279624064\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/order/1/receipt"));
    }

    @Test
    void paymentFailsWhenOrderIsNotFound() throws Exception {
        when(orderService.pay(eq(1L), any())).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(post("/order/{id}/payment", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"creditCardNumber\": \"4532756279624064\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void paymentFailsWhenCreditCardNumberNotGiven() throws Exception {
        mockMvc.perform(post("/order/{id}/payment", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void cannotPayAlreadyPaidOrder() throws Exception {
        when(orderService.pay(eq(1L), any())).thenThrow(OrderAlreadyPaid.class);

        mockMvc.perform(post("/order/{id}/payment", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"creditCardNumber\": \"4532756279624064\"}"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void receiptCanBeFound() throws Exception {
        Receipt receipt = new Receipt(LocalDateTime.now(), "4532756279624064", 100.0);

        when(orderService.getReceipt(eq(1L))).thenReturn(receipt);

        mockMvc.perform(get("/order/{id}/receipt", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void getReceiptForOrder() throws Exception {
        Receipt receipt = new Receipt(
                LocalDateTime.now(),
                "4532756279624064",
                100.0);

        when(orderService.getReceipt(eq(1L))).thenReturn(receipt);

        mockMvc.perform(get("/order/{id}/receipt", 1L))
                .andExpect(jsonPath("$.date").isNotEmpty())
                .andExpect(jsonPath("$.creditCardNumber").value("4532756279624064"))
                .andExpect(jsonPath("$.amount").value(100.0));
    }
}