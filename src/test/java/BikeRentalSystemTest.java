import Exceptions.UserDoesNotExists;
import org.junit.jupiter.api.*;

/**
 * Por enquanto só estão escritos os métodos de teste principais e
 * um caso de teste de exemplo.
 *
 * Não esquecer de identificar o ID do caso de teste relativo
 * a cada expressão de assert, etc.!
 */
public class BikeRentalSystemTest {
    BikeRentalSystem brs;

    @BeforeEach
    public void setUp() {
        /* rentalFee = 1, significa que cada unidade de tempo de aluguer da bicicleta
         * corresponde a 1 unidade de crédito
         */
        brs = new BikeRentalSystem(1);
    }

    @Test
    public void testGetBicycle() {
        //Expressão relativa ao caso de teste com output:
        // -> UserDoesNotExists exception
        // (colocar o ID aqui)
        int IDUser = 66, IDDeposit = 12, startTime = 12;
        Assertions.assertThrows(UserDoesNotExists.class,
                () -> brs.getBicycle(IDDeposit, IDUser, startTime));

    }

    @Test
    public void testReturnBicycle() {

    }

    @Test
    public void testBicycleRentalFee() {

    }

    @Test
    public void testVerifyCredit() {

    }

    @Test
    public void testAddCredit() {

    }

    @Test
    public void testRegisterUser() {

    }
}
