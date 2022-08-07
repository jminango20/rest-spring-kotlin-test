package br.com.juan.unittests.services

import br.com.erudio.unittests.mocks.MockPerson
import br.com.juan.model.Person
import br.com.juan.repository.PersonRepository
import br.com.juan.services.PersonServices
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension

@TestInstance(TestInstance.Lifecycle.PER_CLASS) //1 ciclo de vida por classe
@ExtendWith(MockitoExtension::class)
class PersonServicesTest {

    private var input: MockPerson? = null

    @InjectMocks
    private val service: PersonServices? = null

    @Mock
    var repository: PersonRepository? = null

    @BeforeEach //Antes de cada test
    fun setupMock(){
        input = MockPerson()
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testFindAll(){
        val list: List<Person> = input!!.mockEntityList()
        `when`(repository?.findAll()).thenReturn(list) //Quando encontre um repository definido no código

        val persons = service!!.findAll()

        assertNotNull(persons)
        assertEquals(14, persons.size)

        val personOne = persons[1]
        assertNotNull(personOne)
        assertNotNull(personOne.id)

        assertEquals("Addres Test1", personOne.address)
        assertEquals("First Name Test1", personOne.firstName)
        assertEquals("Last Name Test1", personOne.lastName)
        assertEquals("Female", personOne.gender)

        val personFour = persons[4]
        assertNotNull(personFour)
        assertNotNull(personFour.id)

        assertEquals("Addres Test4", personFour.address)
        assertEquals("First Name Test4", personFour.firstName)
        assertEquals("Last Name Test4", personFour.lastName)
        assertEquals("Male", personFour.gender)

        val personSeven = persons[7]
        assertNotNull(personSeven)
        assertNotNull(personSeven.id)

        assertEquals("Addres Test7", personSeven.address)
        assertEquals("First Name Test7", personSeven.firstName)
        assertEquals("Last Name Test7", personSeven.lastName)
        assertEquals("Female", personSeven.gender)
    }
}