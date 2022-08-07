package br.com.juan.integrationstests.controller

import br.com.juan.configs.TestConfig
import br.com.juan.model.Person
import br.com.juan.testcontainers.AbstractIntegrationClass
import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.log.LogDetail
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.specification.RequestSpecification
import junit.framework.TestCase.*
import org.junit.jupiter.api.*

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) //Que porta vamos a usar
@TestMethodOrder(OrderAnnotation::class) //Executar os testes em uma ordem
@TestInstance(TestInstance.Lifecycle.PER_CLASS) //1 ciclo de vida por classe
class PersonControllerTest : AbstractIntegrationClass() {

    private var specification: RequestSpecification? = null //Vou manipular nos tests
    private var objectMapper: ObjectMapper? = null //Para deserializar
    private var person: Person? = null

    @BeforeAll
    fun setup(){
        objectMapper = ObjectMapper() //setar uma feature
        objectMapper!!.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES) //Falhar para propriedades desconhecidas
        person = Person()
    }

    @Test //Cosas despues do contexto - definir a especificação
    @Order(1)
    fun postSetup(){
        specification = RequestSpecBuilder()
            .setBasePath("/person") //Pegado da URL
            .setPort(TestConfig.SERVER_PORT)
                .addFilter(RequestLoggingFilter(LogDetail.ALL)) //Passar todos os logs do controle ao logar
                .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()
    }

    @Test
    @Order(2)
    fun testCreate(){
        mockPerson()

        val content: String = given().spec(specification) //REST Assured
            .contentType(TestConfig.CONTENT_TYPE)
            .body(person)
            .`when`()
                .post()
            .then()
                .statusCode(200)
                .extract()
                    .body()
                        .asString()

        val createdPerson = objectMapper!!.readValue(content, Person::class.java) //Desearilizar

        person = createdPerson

        assertNotNull(createdPerson.id)
        assertNotNull(createdPerson.firstName)
        assertNotNull(createdPerson.lastName)
        assertNotNull(createdPerson.address)
        assertNotNull(createdPerson.gender)
        assertTrue(createdPerson.id!! > 0)

        assertEquals("Andrea", createdPerson.firstName)
        assertEquals("Flores", createdPerson.lastName)
        assertEquals("Montreal, Canadá", createdPerson.address)
        assertEquals("Female", createdPerson.gender)

    }
    private fun mockPerson() {
        person?.firstName = "Andrea"
        person?.lastName = "Flores"
        person?.address = "Montreal, Canadá"
        person?.gender = "Female"
    }

}