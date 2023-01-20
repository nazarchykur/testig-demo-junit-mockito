package com.example.sbtestcontainers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/*


Сучасні програми не живуть ізольовано: зазвичай нам потрібно підключатися до різних зовнішніх компонентів, 
таких як PostgreSQL, Apache Kafka, Cassandra, Redis та інших зовнішніх API.

Припустімо, що ми розробляємо типову програму, яка використовує PostgreSQL як базу даних. 
        @Entity
        @Table(name = "articles")
        public class Article {
        
            @Id
            @GeneratedValue(strategy = IDENTITY)
            private Long id;
        
            private String title;
        
            private String content;
        
            // getters and setters
        }


Щоб переконатися, що ця сутність працює належним чином, ми повинні написати для неї тест, щоб перевірити її взаємодію 
з базою даних. Оскільки цьому тесту потрібно спілкуватися з реальною базою даних, ми повинні заздалегідь налаштувати 
екземпляр PostgreSQL.

Існують різні підходи до налаштування таких інфраструктурних інструментів під час виконання тестів . 
Власне, існує три основні категорії таких рішень:
    1. Встановіть десь окремий сервер бази даних лише для тестів
    2. Використовуйте кілька легких, специфічних для тестування альтернатив або підробок, таких як H2
    3. Нехай тест сам керує життєвим циклом бази даних
    
Оскільки ми не повинні розрізняти наші тестові та робочі середовища, є кращі альтернативи порівняно з використанням 
БД для тестів, таких як H2. Третій варіант, крім роботи з реальною базою даних, пропонує кращу ізоляцію для тестів . 
Крім того, за допомогою таких технологій, як Docker і Testcontainers , легко реалізувати третій варіант.

Ось як виглядатиме наш робочий процес тестування, якщо ми використовуємо такі технології, як Testcontainers:
    1. Налаштуйте такий компонент, як PostgreSQL, перед усіма тестами. Зазвичай ці компоненти слухають випадкові порти.
    2. Виконайте тести.
    3. Розберіть компонент.
    
Якщо наш контейнер PostgreSQL кожного разу буде слухати випадковий порт, тоді нам слід якось динамічно встановити 
та змінити властивість конфігурації spring.datasource.url . 
По суті, кожен тест повинен мати власну версію цієї властивості конфігурації.

Коли конфігурації статичні, ми можемо легко керувати ними за допомогою засобів керування конфігураціями Spring Boot . 
Однак, коли ми стикаємося з динамічними конфігураціями, те саме завдання може бути складним.


Перший підхід до реалізації динамічних властивостей полягає у використанні спеціального ApplicationContextInitializer . 
По суті, ми спочатку налаштовуємо нашу інфраструктуру та використовуємо інформацію з першого кроку, 
щоб налаштувати  ApplicationContext :
            
            @SpringBootTest
            @Testcontainers
            @ContextConfiguration(initializers = ArticleTraditionalLiveTest.EnvInitializer.class)
            class ArticleTraditionalLiveTest {
            
                @Container
                static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:11")
                  .withDatabaseName("prop")
                  .withUsername("postgres")
                  .withPassword("pass")
                  .withExposedPorts(5432);
            
                static class EnvInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
            
                    @Override
                    public void initialize(ConfigurableApplicationContext applicationContext) {
                        TestPropertyValues.of(
                          String.format("spring.datasource.url=jdbc:postgresql://localhost:%d/prop", postgres.getFirstMappedPort()),
                          "spring.datasource.username=postgres",
                          "spring.datasource.password=pass"
                        ).applyTo(applicationContext);
                    }
                }
            
                // omitted 
            }



JUnit створить і запустить контейнер перед всім іншим. Після того, як контейнер буде готовий, розширення Spring 
викличе ініціалізатор, щоб застосувати динамічну конфігурацію до середовища Spring . 
Зрозуміло, що цей підхід є дещо багатослівним і складним.

Тільки після цих кроків ми можемо написати наш тест:

                @Autowired
                private ArticleRepository articleRepository;
                
                @Test
                void givenAnArticle_whenPersisted_thenShouldBeAbleToReadIt() {
                    Article article = new Article();
                    article.setTitle("A Guide to @DynamicPropertySource in Spring");
                    article.setContent("Today's applications...");
                
                    articleRepository.save(article);
                
                    Article persisted = articleRepository.findAll().get(0);
                    assertThat(persisted.getId()).isNotNull();
                    assertThat(persisted.getTitle()).isEqualTo("A Guide to @DynamicPropertySource in Spring");
                    assertThat(persisted.getContent()).isEqualTo("Today's applications...");
                }



The @DynamicPropertySource
    
    Spring Framework 5.2.5 представив  анотацію @DynamicPropertySource  для полегшення додавання властивостей із 
    динамічними значеннями . Усе, що нам потрібно зробити, це створити статичний метод із анотацією 
    @DynamicPropertySource і мати лише один екземпляр DynamicPropertyRegistry  як вхідні дані:
        
    @SpringBootTest
    @Testcontainers
    public class ArticleLiveTest {
    
        @Container
        static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:11")
          .withDatabaseName("prop")
          .withUsername("postgres")
          .withPassword("pass")
          .withExposedPorts(5432);
    
        @DynamicPropertySource
        static void registerPgProperties(DynamicPropertyRegistry registry) {
            registry.add("spring.datasource.url", () -> String.format("jdbc:postgresql://localhost:%d/prop", postgres.getFirstMappedPort()));
            registry.add("spring.datasource.username", () -> "postgres");
            registry.add("spring.datasource.password", () -> "pass");
        }
        
        // tests are same as before
    }
    

    Як показано вище, ми використовуємо метод add(String, Supplier<Object>) у заданому  DynamicPropertyRegistry, 
    щоб додати деякі властивості до  середовища Spring . Цей підхід набагато чистіший порівняно з ініціалізатором, 
    який ми бачили раніше. Зверніть увагу, що методи, анотовані  @DynamicPropertySource  , мають бути оголошені як 
    статичні та приймати лише один аргумент типу  DynamicPropertyRegistry . 
    
    По суті, основна мотивація  анотації @DynmicPropertySource  полягає в тому, щоб полегшити те, що вже було можливо. 
    Хоча спочатку він був розроблений для роботи з Testcontainers, його можна використовувати скрізь, де нам потрібно 
    працювати з динамічними конфігураціями.


 ======================================================================================================================
   
   sb-testcontainers/build.gradle             
                
            ext {
                set('testcontainersVersion', "1.17.6")
            }
            
            dependencies {
                implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
                implementation 'org.springframework.boot:spring-boot-starter-web'
                implementation 'org.flywaydb:flyway-core'
                
                compileOnly 'org.projectlombok:lombok'
            
                annotationProcessor 'org.projectlombok:lombok'
                
                runtimeOnly 'org.postgresql:postgresql'
                
                testImplementation 'org.springframework.boot:spring-boot-starter-test'
                testImplementation 'org.testcontainers:junit-jupiter'                          (2)
                testImplementation 'org.testcontainers:postgresql'                             (3)
            }
            
            dependencyManagement {
                imports {
                    mavenBom "org.testcontainers:testcontainers-bom:${testcontainersVersion}"  (1)
                }
            }       


    (1) - Testcontainers provides a Bill of Material (BOM) to manage the versions for our project’s dependencies.
    (2) - The JUnit Jupiter Integration which provides a JUnit extension to bind docker containers to tests.
    (3) - One of the ready-to-use modules. This one contains a PostgreSQL Container.
    
    
    
How to use Testcontainers?

        With all the preparation done, we can now start to implement our first @DataJpaTest, 
        which will use a real PostgreSQL Database.
        
         RestaurantTest.java
                @Testcontainers                                                                                (1)
                @DataJpaTest
                @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)                   (2)
                @ContextConfiguration(initializers = RestaurantTest.DataSourceInitializer.class)               (3)
                class RestaurantTest {
                
                    @Container                                                                                 (4)
                    private static final PostgreSQLContainer<?> database = new PostgreSQLContainer<>("postgres:12.9-alpine");
                
                    // ...
                
                    public static class DataSourceInitializer                                                  (5)
                        implements ApplicationContextInitializer<ConfigurableApplicationContext> {
                
                        @Override
                        public void initialize(ConfigurableApplicationContext applicationContext) {
                            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                                    applicationContext,
                                    "spring.datasource.url=" + database.getJdbcUrl(),                          (6)
                                    "spring.datasource.username=" + database.getUsername(),
                                    "spring.datasource.password=" + database.getPassword()
                            );
                        }
                    }
                } 
                
                
      (1) - The @Testcontainers annotation is the JUnit Jupiter Extension that binds the lifecycle of a docker 
            container to the one of the tests. It will start each container marked with @Container at the beginning 
            of your test and will stop them at the end of the test. If the container is an instance field, the c
            ontainer will be started and stopped for each test. If it is a static field, the container will be 
            started before the first test of the class and stopped after the last one.
            
      (2) - Normally, Spring Boot will start an in-memory database for @DataJpaTest-Tests. 
            We use AutoConfigureTestDatabase to explicitly deactivate this default behavior. 
            This way, the tests will run against our PostgreSQL Database.
            
      (3) - To make some dynamic configurations at the runtime of our tests, we use @ContextConfiguration.
      
      (4) - The @Container annotation is a marker to tell @Testcontainers which container it should manage.
      
      (5) - This is the Initializer used in the @ContextConfiguration. We use it to configure the connection to 
            the PostgreSQL database running in the container.
            
      (6) - This is an example of the dynamic configuration of the data source URL. We assign the dynamic JDBC URL 
            of the database to Spring’s spring.datasource.url property.
                  
 */

/*

    How can we run tests with the real database? Well, we could create a new instance locally and configure the test 
    environment by editing src/test/resources/application.yml. It does work but it makes the build hard to reproduce. 
    Every developer that is working on the project has to be sure that they have two separate databases. One for 
    development and another one for tests running. Besides, it makes executing the build on CI/CD environment a real challenge.
    
    So, Testcontainers to the rescue! It’s a Java library that launches the service within the Docker container, 
    runs tests, and eventually destroys the container. You don’t need to worry about anything, the framework does 
    the job. Just make sure you have Docker installed and then you are ready to go. The library supports dozens of 
    different databases and modules (PostgreSQL, MySQL, MongoDB, Kafka, Elasticsearch, Nginx, Toxiproxy, and many others). 
    Even if you didn’t find the one that you need, you could use generic container creation.

    The first step is to add the required dependencies.
            testImplementation 'org.testcontainers:junit-jupiter'
            testImplementation 'org.testcontainers:postgresql'
            runtimeOnly 'org.postgresql:postgresql'
    
    
    Then we need to create a separate configuration file in src/test/resources. Spring Boot is able to distinguish 
    different configuration files by the profiles. The profile name should be placed as a suffix like 
    application-PROFILE_NAME.yml. For example, the configuration file named application-test-containers.yml is 
    applied only when test-containers profile is active.
    
                spring:
                  datasource:
                    url: jdbc:tc:postgresql:9.6.8:///test_database
                    username: user
                    password: password
                  jpa:
                    hibernate:
                      ddl-auto: create
    
    
    Have you noticed the tc suffix in the JDBC-connection string? That’s the magic that comes with the union of 
    JUnit 5 and Testcontainers. The thing is that you don’t need any programmatic configurations at all! 
    When the framework sees that url contains the tc suffix it runs all necessary Docker commands internally.
    
            |    We set spring.jpa.hibernate.ddl-auto=create property
            |    so the database schema shall be created automatically
            |    according to definition of entity classes.
            |    Flyway integration is described in the next section.
            
            
     How do we run the test with Testcontainers PostgreSQL instance? All we have to do is to add two annotations. 
     @AutoConfigureTestDatabase though should be removed.

    1. @ActiveProfiles("test-containers") — activates the test-containers profile so the Spring could read the 
        configuration file that was described earlier
    2. @Testcontainers — tells to run PostgreSQL instance in Docker automagically   
    
            @SpringBootTest(webEnvironment = WebEnvironment.NONE)
            @Testcontainers
            @ActiveProfiles("test-containers")
            class PersonCreateServiceImplTestContainers {
            
              @Autowired
              private PersonRepository personRepository;
              @MockBean
              private PersonValidateService personValidateService;
              @Autowired
              private PersonCreateService personCreateService;
            
              @BeforeEach
              void init() {
                personRepository.deleteAll();
              }
            
              @Test
              void shouldCreateOnePerson() {
                final var people = personCreateService.createFamily(List.of("Simon"), "Kirekov");
                
                assertEquals(1, people.size());
                
                final var person = people.get(0);
                assertEquals("Simon", person.getFirstName());
                assertEquals("Kirekov", person.getLastName());
                assertTrue(person.getDateCreated().isBefore(ZonedDateTime.now()));
              }
            
              @Test
              void shouldRollbackIfAnyUserIsNotValidated() {
                doThrow(new ValidationFailedException(""))
                    .when(personValidateService)
                    .checkUserCreation("John", "Brown");
                    
                assertThrows(ValidationFailedException.class, 
                    () -> personCreateService.createFamily(List.of("Matilda", "Vasya", "John"), "Brown"));
                    
                assertEquals(0, personRepository.count());
              }
            }
    
    
    
    
    
    
    Repository Tests
    
    
    What about testing repository layers? Let’s see H2 example again.
            @DataJpaTest
            class PersonRepositoryDataJpaTest {
            
              @Autowired
              private PersonRepository personRepository;
            
              @Test
              void shouldReturnAlLastNames() {
                personRepository.saveAndFlush(new Person().setFirstName("John").setLastName("Brown"));
                personRepository.saveAndFlush(new Person().setFirstName("Kyle").setLastName("Green"));
                personRepository.saveAndFlush(new Person().setFirstName("Paul").setLastName("Brown"));
            
                assertEquals(Set.of("Brown", "Green"), personRepository.findAllLastNames());
              }
            }

    
        @DataJpaTest is annotated with @AutoConfigureTestDatabase itself. 
        This annotation replaces any data source with the H2 instance by default. 
        So, we need to override this behavior by adding replace=Replace.NONE property.
        
                @DataJpaTest
                @Testcontainers
                @ActiveProfiles("test-containers")
                @AutoConfigureTestDatabase(replace = Replace.NONE)
                class PersonRepositoryTestContainers {
                
                  @Autowired
                  private PersonRepository personRepository;
                
                  @Test
                  void shouldReturnAlLastNames() {
                    personRepository.saveAndFlush(new Person().setFirstName("John").setLastName("Brown"));
                    personRepository.saveAndFlush(new Person().setFirstName("Kyle").setLastName("Green"));
                    personRepository.saveAndFlush(new Person().setFirstName("Paul").setLastName("Brown"));
                
                    assertEquals(Set.of("Brown", "Green"), personRepository.findAllLastNames());
                  }
                }
                        
                        
   Flyway Integration
      
        The Evolutionary Database Design principle has been described a long time ago. 
        Nowadays it is standard routine to use tools that implement this pattern. 
        Flyway and Liquibase are the most popular ones in the Java world. 
        We are going to integrate Testcontainers with Flyway.

        1. Firstly, the Flyway dependency is required.
                implementation "org.flywaydb:flyway-core"
                
        2. Secondly, it’s necessary to disable Flyway in application-test-containers.yml 
        because there will be a separate configuration file.     
                spring:
                  datasource:
                    url: jdbc:tc:postgresql:9.6.8:///test_database
                    username: user
                    password: password
                  jpa:
                    hibernate:
                      ddl-auto: create
                  flyway:
                    enabled: false  
                    
        3. Then we are going to create application-test-containers-flyway.yml. 
            The library provides lots of auto-configuration. So, actually, we don’t need to tune anything.  
                      
               spring:
                  datasource:
                    url: jdbc:tc:postgresql:9.6.8:///test_database
                    username: user
                    password: password
        
        
        4. Now it’s time to add SQL migrations. The default directory is resources/db/migration.    
                create table person
                (
                    id           serial primary key,
                    first_name   text,
                    last_name    text,
                    date_created timestamp with time zone
                );     
                
                
       5. Finally, we need to replace test-containers profile with test-containers-flyway one.
            
            @SpringBootTest(webEnvironment = WebEnvironment.NONE)
            @Testcontainers
            @ActiveProfiles("test-containers-flyway")
            class PersonCreateServiceImplTestContainersFlyway {
            
              @Autowired
              private PersonRepository personRepository;
              @MockBean
              private PersonValidateService personValidateService;
              @Autowired
              private PersonCreateService personCreateService;
            
              @BeforeEach
              void init() {
                personRepository.deleteAll();
              }
            
              @Test
              void shouldCreateOnePerson() {
                final var people = personCreateService.createFamily(
                    List.of("Simon"),
                    "Kirekov"
                );
                assertEquals(1, people.size());
                final var person = people.get(0);
                assertEquals("Simon", person.getFirstName());
                assertEquals("Kirekov", person.getLastName());
                assertTrue(person.getDateCreated().isBefore(ZonedDateTime.now()));
              }
            
              @Test
              void shouldRollbackIfAnyUserIsNotValidated() {
                doThrow(new ValidationFailedException(""))
                    .when(personValidateService)
                    .checkUserCreation("John", "Brown");
                assertThrows(ValidationFailedException.class, () -> personCreateService.createFamily(
                    List.of("Matilda", "Vasya", "John"),
                    "Brown"
                ));
                assertEquals(0, personRepository.count());
              }
            }
             
 */

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest_2_DynamicPropertySource_new_IT {

    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:13.2-alpine");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @Autowired
    public BookRepository bookRepository;

    @Test
    void contextLoads() {
        Book book = new Book();
        String bookName = "Testcontainers";
        book.setName(bookName);

        bookRepository.save(book);

        Book book1 = bookRepository.findAll().get(0);

        assertThat(book1.getId()).isNotNull();
        assertThat(book1.getName()).isEqualTo(bookName);

        System.out.println("Context loads!");
    }
}