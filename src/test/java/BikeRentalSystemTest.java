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

        int IDUser, IDDeposit, endtime;
        IDUser=55; IDDeposit=55; endtime=12;


    }

    @Test
    public void testBicycleRentalFee() {

        //rentalProgram tem de ser igual ao construtor?

        int rentalProgram, starttime,endtime, nRentals;

    }

    @Test
    public void testVerifyCredit() {

        int IDUser;
        IDUser=23;


    }

    @Test
    public void testAddCredit() {

        int IDUser, amount;
        IDUser=55;amount=3;

    }

    @Test
    public void testRegisterUser() {

        int IDUser, rentalprogram;
        String name;
        IDUser=55;rentalprogram=2;
        name="Sterben";


    }
}
