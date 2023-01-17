package com.example.testingdemo.testweblayer.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*

    What is the difference between @ExtendWith(SpringExtension.class) and @ExtendWith(MockitoExtension.class)?


    When involving Spring:

        If you want to use Spring test framework features in your tests like for example @MockBean, then you have to 
        use @ExtendWith(SpringExtension.class). It replaces the deprecated JUnit4 @RunWith(SpringJUnit4ClassRunner.class)
    
    When NOT involving Spring:
    
        If you just want to involve Mockito and don't have to involve Spring, for example, when you just want to use 
        the @Mock / @InjectMocks annotations, then you want to use @ExtendWith(MockitoExtension.class), as it doesn't 
        load in a bunch of unneeded Spring stuff. It replaces the deprecated JUnit4 @RunWith(MockitoJUnitRunner.class).
    
    To answer your question:
    
        Yes you can just use @ExtendWith(SpringExtension.class), but if you're not involving Spring test framework 
        features in your tests, then you probably want to just use @ExtendWith(MockitoExtension.class).


------------------------------------------------------------------------------------------------------------------------

    What's the Purpose of the SpringExtension?
        
        let's investigate the cross-cutting functionality the SpringExtension implements.
                public class SpringExtension implements BeforeAllCallback, 
                    AfterAllCallback, 
                    TestInstancePostProcessor, 
                    BeforeEachCallback, 
                    AfterEachCallback, 
                    BeforeTestExecutionCallback, 
                    AfterTestExecutionCallback, 
                    ParameterResolver {
                  
                    // ...
                     
                }
        
       That's a lot. As we can see from the source code above, the SpringExtension is heavily involved in the lifecycle of our tests.

        Without diving too deep into the implementation, the main responsibilities of this extension are the following: 
            * manage the lifecycle of the Spring TestContext (e.g., start a new one or get a cached context)
            * support dependency injection for parameters (e.g., test class constructor or test method)
            * cleanup and housekeeping tasks after the test

                
                
                @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
                class ApplicationIT {
                 
                  @Autowired
                  private CustomerService customerService;    // injected by the DependencyInjectionTestExecutionListener
                 
                  @Test
                  void needsEnvironmentBeanToVerifySomething(
                    @Autowired 
                    Environment environment  ) {   // resolved by the SpringExtension
                  
                    assertNotNull(environment);
                  }
                }


    
    When Do We Need To Register the SpringExtension?
        
        @WebMvcTest / @SpringBootTest
        All Spring Boot test slice annotations and also @SpringBootTest register the SpringExtension out-of-the-box.
        
                @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
                @ExtendWith(SpringExtension.class) // not necassary and should be removed
                class ApplicationIT {
                 
                  // ...
                 
                }
        
        
        
     When Do We Need to Register the SpringExtension (Part II)?
        
        There are limited use cases where we have to explicitly register the SpringExtension manually. 
        One of such use cases is writing a custom test slice annotation.
        
        This hands-on example demonstrates how to only bootstrap Amazon SQS Listener relevant parts of our application. 
        As there is now Spring Boot test slice annotation available, we manually register the SpringExtension to work 
        with a Spring TestContext throughout the test:
        
                    @ExtendWith(SpringExtension.class)
                    @Import(BookSynchronizationListener.class)
                    @ImportAutoConfiguration(MessagingAutoConfiguration.class)
                    @Testcontainers(disabledWithoutDocker = true)
                    class BookSynchronizationListenerSliceTest {
                     
                       // test a SQS listener in isolation
                     
                     
                    }
        
        
        
        
        
        
        
       Guide to @SpringBootTest for Spring Boot Integration Tests
       
             The @SpringBootTest Annotation Under the Hood
                
                Every new Spring Boot project comes with an initial test inside src/test/resources that uses this annotation:
                            import org.junit.jupiter.api.Test;
                            import org.springframework.boot.test.context.SpringBootTest;
                             
                            @SpringBootTest
                            class DemoApplicationTest {
                             
                              @Test
                              void contextLoads() {
                              }
                             
                            }

            As the test method name applies, this initial test ensures the Spring ApplicationContext can successfully 
            start without any dependency injection errors (aka. NoSuchBeanDefinitionException) or missing 
            configuration properties.

            This default test is already one of our most important integration tests. We can detect potential 
            Spring Boot startup issues early on by launching the entire context during test execution.    
            
            
                    
                    @Target({ElementType.TYPE})
                    @Retention(RetentionPolicy.RUNTIME)
                    @Documented
                    @Inherited
                    @BootstrapWith(SpringBootTestContextBootstrapper.class)
                    @ExtendWith({SpringExtension.class})
                    public @interface SpringBootTest {
                     
                     // further attributes
                     
                    }
                    
                    
             Starting from the bottom, we can see that the @SpringBootTest meta-annotation registers the JUnit 
             Jupiter (part of JUnit 5) SpringExtension. This extension is essential for the seamless integration of 
             our test framework with Spring. Among other things, we'll be able to inject (@Autowired) beans from the 
             TestContext to our test classes.

            The next annotation (@BootstrapWith) does the entire heavy lifting and starts the context for our test. 
            In short, it searches for our main Spring Boot entry class (annotated with @SpringBootApplication) 
            to retrieve our context configuration and start it accordingly:

                    DemoApplication.javaJava
                    @SpringBootApplication
                    public class DemoApplication {
                     
                      public static void main(String[] args) {
                        SpringApplication.run(Application.class, args);
                      }
                     
                    }
             
             This will then trigger the component scanning mechanism and apply all auto-configurations.
             
             
             Configuration Options for Integration Tests With @SpringBootTest
             
                Let's take a look at the different configuration values for the webEnvironment attribute of the 
                @SpringBootTest annotation.

                This value impacts the type of context we test against. Furthermore, it defines if Spring Test 
                starts the embedded servlet container (Tomcat, Jetty, Undertow – Tomcat is the default).
                
                
                
           The Default: WebEnvironment.MOCK
           
                MOCK is the default configuration that is applied in case we don't specify any value.  
                Spring Test will create a WebApplicationContext with a mocked servlet environment 
                (when using Spring MVC)  for our test.
                
                That's similar to what we get when using MockMvc and writing tests for our Spring Web MVC 
                controllers using @WebMvcTest.
                
                The difference between @WebMvcTest and this setup is the number of Spring Beans that are part of the 
                Spring TestContext. While @WebMvcTest only populates web-related beans, with @SpringBootTest we 
                populate the entire context.

                                @SpringBootTest(webEnvironment = WebEnvironment.MOCK)
                                class ApplicationIT {
                                }
                                
                However, this configuration won't start the embedded servlet container.
                
                
                     
           Most Commonly Used for @SpringBootTest: WebEnvironment.RANDOM_PORT       
           
                With this configuration, Spring creates a WebApplicationContext for our integration test and starts 
                the embedded servlet container on a random ephemeral port.
                
                This also brings up the management server on a random port in case we expose our Actuator endpoints 
                from a different port. If we don't override management.server.port in any of our configuration files, 
                Spring will choose the same port for the management part as for our application.
                
                Furthermore, we can inject auto-configured  HTTP (WebTestClient or RestTestTemplate) clients that point 
                to the started application. There's no need to fiddle around with the port and hostname when accessing 
                our started application using these clients:
                        
                        @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
                        class ApplicationIT {
                          
                          @Autowired
                          private WebTestClient webTestClient; // available with Spring WebFlux
                         
                          @Autowired
                          private TestRestTemplate testRestTemplate; // available with Spring Web MVC
                          
                          @LocalServerPort
                          private Integer port;
                         
                          @Test
                          void httpClientExample() {
                         
                            // no need for this
                            WebClient webClient = WebClient.builder()
                              .baseUrl("http://localhost:" + port)
                              .build();
                            
                            this.webTestClient
                              .get()
                              .uri("/api/customers")
                              .exchange()
                              .expectStatus().is2xxSuccessful();
                            
                          }
                        }          
               
               In case we do need information about the chosen port for our application and the management server, 
               we can inject both port numbers into our test:   
                       @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
                        class ApplicationIT {
                         
                          @LocalServerPort
                          private Integer port;
                         
                          @LocalManagementPort
                          private Integer managementPort;
                         
                          @Test
                          void printPortsInUse() {
                            System.out.println(port);
                            System.out.println(managementPort);
                          }
                        }   
                        
               This configuration value should be our default choice when writing integration tests that verify 
               our application from the outside by accessing one of our HTTP endpoints.    
               
               
         Rarely Applicable for Integration Tests: WebEnvironment.DEFINED_PORT
         
                With DEFINED_PORT we instruct Spring to start the embedded servlet container on the pre-defined port. 
                By default, that's port 8080, but we can configure this port with the server.port property.      
               
                               @SpringBootTest(
                                      webEnvironment = WebEnvironment.DEFINED_PORT,
                                      properties = {
                                        "server.port=8042",
                                        "management.server.port=9042"
                                      })
                                class ApplicationDefinedPortIT {
                                 
                                  @LocalServerPort
                                  private Integer port;
                                 
                                  @LocalManagementPort
                                  private Integer managementPort;
                                 
                                  @Test
                                  void printPortsInUse() {
                                    System.out.println(port); // 8042
                                    System.out.println(managementPort); // 9042
                                  }
                                }
                    
                Running our integration tests with this configuration, we might clash with a running Spring Boot 
                application. As only one process can occupy a port at any given time, we run into conflicts if 
                that port is already in use.

                As an alternative, we can define a new port of our integration test to avoid any clash. However, 
                with this configuration, we won't be able to start multiple Spring TestContexts with @SpringBootTest. 
                Once we run the first integration test, all subsequent attempts to start the context will fail because 
                the port is already acquired.
              
              
              
        Rarely Applicable for Integration Tests: WebEnvironment.NONE
        
            As the name of the enum applies, Spring won't create a WebApplicationContext and won't start the embedded 
            servlet container for our test. This configuration isn't of big use when writing integration tests for our 
            application, where Spring MVC controllers are the main entry point.
            
            However, if our application has non-Spring MVC entry points, this configuration can help to test such parts. 
            That's the case when our application processes incoming messages (e.g., JMS or SQS messages) or performs batch jobs.
                  
                                
       
       
       
       Configure the Application Context and Environment
       
            When using a Spring TestContext for our integration tests, we might need to configure the context and 
            environments. This includes replacing or mocking beans and overriding application properties.
            
            A pattern we'll see quite often is the usage of a integration-test or a test profile in general. We can 
            place a new property file (application-integration-test.yml) inside src/test/resources and define or 
            override configuration values:
            
            
            src/test/resources/application-integration-test.yml       YAML
                spring:
                  application:
                    name: integration-test
                  jpa:
                    hibernate:
                      ddl-auto: validate
            

            What's left is to activate this profile using@ActiveProfiles  on top of our test class:
                @SpringBootTest(
                  webEnvironment = WebEnvironment.RANDOM_PORT,
                  properties = {
                    "my.custom.property=inlined",
                    "spring.main.banner-mode=off" // don't do this when Josh Long is pairing with you
                  })
                @ActiveProfiles("integration-test")
                class ApplicationConfigurationIT {
                
                  @Autowired
                  private Environment environment;
                
                  @Test
                  void shouldPrintConfigurationValues() {
                    System.out.println(environment.getProperty("my.custom.property")); // inlined
                    System.out.println(environment.getProperty("spring.application.name")); // integration-test
                  }
                }


            In case we want to override a specific property only for a particular test, we can also inline properties 
            using the properties attribute of the @SpringBootTest annotation.

            When it comes to replacing Spring Beans with a mocked version, Spring Boot provides a convenient 
            annotation for this:
                    @SpringBootTest
                    class ApplicationConfigurationIT {
                    
                      @MockBean
                      private CustomerService customerService;
                      
                      @Test
                      void shouldRegisterUnknownUser() {
                        Mockito.when(customerService.register("42")).thenReturn(7L);
                        
                        // test 
                      }
                    }
        
            With the @MockBean we replace the actual bean with a Mockito mock. In the example above, we're placing a 
            mocked version of the CustomerService into our TestContext. We can then instruct the behavior of this mock 
            during test execution by using Mockito as we would use it for unit tests. To avoid confusion, it's 
            important to understand the difference between @Mock and @MockBean.

            If a Spring Bean is missing, or we want to replace it with another bean, there are various other 
            strategies to fix no qualifying bean errors for Spring Boot tests.

        
        When To Avoid Using @SpringBootTest
            
            Equipped with this powerful test annotation, some Spring Boot newcomers try to use @SpringBootTest for 
            all their test cases. That's a bad practice for multiple reasons.

            First, we would have to ensure our application context can start all the time. While this is easy to 
            achieve when there are not external infrastructure components to integrate, this becomes a more complex 
            topic as soon as we integrate a database, a message queue, or any other external systems (Testcontainers to the rescue).
            
            Next, even though we only want to unit test a single class, we would need to start the entire context 
            and use @MockBean to mock all collaborators.
            
            The following test class showcases this bad practice:
                        // the same can be tested with a much faster unit test
                        // don't do this
                        @SpringBootTest
                        class CustomerServiceTest {
                         
                          @Autowired
                          private CustomerService cut;
                         
                          @MockBean
                          private CustomerRepository customerRepository;
                         
                          @Test
                          void shouldRegisterNewCustomer() {
                         
                            when(customerRepository.findByCustomerId("duke")).thenReturn(null);
                         
                            Long result = cut.register("duke");
                         
                            assertEquals(42L, result);
                          }
                        }
                        

             For the test case above, we're better off writing a good old unit test. This way, we only rely on 
             JUnit and Mockito without any Spring context support.

            In general, we should try to cover as much as possible with unit tests, use Spring Boot test slices 
            where applicable, and only some integration tests.
            
            Overusing @SpringBootTest while also creating multiple context configurations will lead to increased build 
            times as Spring won't be able to reuse a cached Spring TestContext. Such integration tests are rather expensive. 
            
            
            
            
            
            
    Summary of Using @SpringBootTest for Integration Tests
    
        In short, these should be the key takeaways for you:
            > @SpringBootTest is a powerful tool to write integration tests
            > not every part of your application should be tested with this expensive test setup
            > be aware that the default web environment is MOCK (no port and no Tomcat)
            > there are multiple strategies to tweak the context configuration and environment
            > be aware of the increased build time when creating different Spring TestContext configurations
        
        
        As a rule of thumb: Cover as much as possible with fast-running unit tests, use slice contexts where 
        applicable, and ensure the overall functionality with some integration tests using @SpringBootTest.            
                    

------------------------------------------------------------------------------------------------------------------------


    
    Extension that initializes mocks and handles strict stubbings. This extension is the JUnit Jupiter equivalent of 
    our JUnit4 MockitoJUnitRunner. Example usage:

             @ExtendWith(MockitoExtension.class)
             public class ExampleTest {
            
                 @Mock
                 private List<Integer> list;
            
                 @Test
                 public void shouldDoSomething() {
                     list.add(100);
                 }
             } 1
 */

/*

    https://rieckpil.de/difference-between-mock-and-mockbean-spring-boot-applications/

    @Mock vs. @MockBean
    
    Use @Mock when unit testing your business logic (only using JUnit and Mockito). 
    Use @MockBean when you write a test that is backed by a Spring Test Context and you want to add or replace 
    a bean with a mocked version of it.
    
        When testing some class in isolation we only need two tools: JUnit (4 or 5) and Mockito.
        
        While writing unit tests for the StockService , we mock its collaborators. We don't want a change in their 
        implementation to affect our unit test. In this example, we mock the StockApiClient as our class under test 
        (short cut) only has one collaborator.
        
                @ExtendWith(MockitoExtension.class)
                class StockServiceTest {
                 
                  @Mock
                  private StockApiClient stockApiClient;
                 
                  @InjectMocks
                  private StockService cut;
                 
                  @Test
                  void shouldReturnDefaultPriceWhenClientThrowsException() {
                 
                    when(stockApiClient.getLatestStockPrice("AMZN"))
                      .thenThrow(new RuntimeException("Remote System Down!"));
                 
                    BigDecimal result = cut.getLatestPrice("AMZN");
                 
                    assertEquals(BigDecimal.ZERO, result);
                  }
                }
        
        The test above uses JUnit Jupiter (a module of JUnit 5) and Mockito. With @Mock we instruct Mockito to create 
        a mock as we don't want a real instance of this class. Mockito's JUnit Jupiter extension will then take care 
        to instantiate the mock and inject it to our class under test.
        
        @InjectMocks Mockito will search for a suitable public constructor to create an instance of our StockService 
        and pass all mocks (we only have one) to it.
        
        
    
    Using @MockBean to Add or Replace Beans with a Mock
    
        While the previous section was true for using plain JUnit 5 (or JUnit 4) with Mockito and independent of the 
        application framework, what follows is only applicable for the Spring Framework.

        With Spring Boot's excellent test support, we can create a custom Spring Context for our test. Most of the time 
        we either populate the full Spring Context (@SpringBootTest) or use a sliced context 
        (e.g. @WebMvcTest or @DataJpaTest).  This allows us to test the integration of multiple classes or 
        for example the web layer in isolation with a mocked Servlet environment.
        
        Such tests now use a Spring Test Context. This context is similar to the application context during runtime 
        as we can request beans from it (@Autowired). Usually, it only contains a subset of our beans (making our tests faster).
        
        When starting the Spring Test Context we have to satisfy all dependencies (speak collaborator) of our Spring beans. 
        We do this by providing an instance of them inside the Spring Context so that Spring can inject them. 
        Otherwise, the context won't start.
        
        As we can customize the Spring Test Context to our needs, we can decide whether we want the actual or a mocked 
        version of one of our beans inside the context.
        
        An example might illustrate this better. Let's say we have the following Spring MVC controller:
                @RestController
                @RequestMapping("/api/stocks")
                public class StockController {
                
                  private final StockService stockService;
                
                  public StockController(StockService stockService) {
                    this.stockService = stockService;
                  }
                
                  @GetMapping
                  public BigDecimal getStockPrice(@RequestParam("stockCode") String stockCode) {
                    return stockService.getLatestPrice(stockCode);
                  }
                }
            
        With Spring Boot's @WebMvcTest we can now start a sliced Spring context that includes only beans that are 
        relevant for our web-layer. As this Test Context also includes a bean of type StockController, we have to 
        provide a bean of type StockServiceas otherwise, our context won't start.
        
        We could add the actual implementation of the StockService, but this would mean that we also have to provide all 
        collaborators of the StockService . This might end up in a big hierarchy of dependencies that we would have to provide.
        
        On the other side, we also want to test our web-layer in isolation and don't care what's going on inside the 
        StockService for such a test. That's why we use @MockBean here to place a mocked version of StockService inside 
        the context to satisfy our StockController:
        
                @WebMvcTest(StockController.class)
                class StockControllerTest {
                
                  @MockBean
                  private StockService stockService;
                
                  @Autowired
                  private MockMvc mockMvc;
                
                  @Test
                  void shouldReturnStockPriceFromService() throws Exception {
                    when(stockService.getLatestPrice("AMZN"))
                      .thenReturn(BigDecimal.TEN);
                
                    this.mockMvc
                      .perform(get("/api/stocks?stockCode=AMZN"))
                      .andExpect(status().isOk());
                  }
                }
                
         The @MockBean annotation is part of Spring Test and will place a mock of type StockService inside the Spring 
         Test Context. We can then define the behavior of this mock using the well-known Mockito stubbing setup: 
         when().thenReturn().

        You can use this annotation whenever our test deals with a Spring Context.
        
        
        
        Let's say we want to write an integration test that involves all our beans. However, we have a 
        ExpensiveRealtimeStockApiClient that talks to a remote system and we are billed for every API call. 
        The lazy developer's approach could be to mock this bean for our integration test 
        (PS: I know that WireMock would fit better here).
        
                @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
                class ApplicationTest {
                
                  @Autowired
                  private StockApiClient stockApiClient;
                
                  @MockBean
                  private ExpensiveRealtimeStockApiClient expensiveRealtimeStockApiClient;
                
                  @Test
                  void contextLoadsWithAllBeans() {
                    // e.g. now use the WebTestClient to access our endpoint
                  }
                }
           
           
           
    General Advice on Using @MockBean for Your Spring Boot Tests
    
        Always prefer to write a unit test that makes use of JUnit and Mockito, as they are fast. 
        As general advice, don't overdo it with @MockBean.
        
        However, there are parts of your application where plain unit tests won't add many benefits. 
        Your controller endpoints are a good example (see Tom Homberg's blog post for a good explanation of this) 
        and here it definitely makes sense to use a combination of @WebMvcTest and @MockBean.
        
        Be aware that whenever you use @MockBean Spring can't reuse an already existing context that includes the 
        actual bean of this type. This means you'll get a fresh new context (if there isn't a test with a similar setup) 
        and this adds time to your test execution. If done right, you can improve your build times with 
        Spring's Context Caching mechanism a lot.
        
 */


@SpringBootTest
@AutoConfigureMockMvc
class WelcomeControllerAcceptanceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void welcome_get_withDefaultName() throws Exception {
        mockMvc.perform(get("/welcome"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Welcome Stranger")));
    }

    @Test
    public void welcome_get_withCustomProvidedName() throws Exception {
        mockMvc.perform(get("/welcome?name=John"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Welcome John")));
    }

}