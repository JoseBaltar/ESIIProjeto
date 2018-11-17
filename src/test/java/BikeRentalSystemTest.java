import Exceptions.UserAlreadyExists;
import Exceptions.UserDoesNotExists;
import Models.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.ThrowingSupplier;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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

        //#TC15
        IDUser=23;
        Assertions.assertTrue(brs.verifyCredit(IDUser),"Utilizador existe e crédito superior a 1");

        //#TC16
        IDUser=4;
        Assertions.assertFalse(brs.verifyCredit(IDUser),"Utilizador existe e crédito inferior a 1");

        //#TC17
        IDUser=66;
        Assertions.assertFalse(brs.verifyCredit(IDUser),"Utilizador não existe");


    }

    @Test
    public void testAddCredit() {

        int IDUser, amount;
        User u=null;

        //#TC18
        IDUser=55;amount=3;
        Assertions.assertNotEquals(0,amount,"Amount diferente de 0, crédito");
        Assertions.assertNotNull(IDUser,"User não é nulo e existe, atribuido");

        //#TC19
        amount=0;
        Assertions.assertEquals(0,amount,"Amount igual a 0, não é atribuído crédito");
        Assertions.assertNotNull(IDUser,"User não é nulo existe.");

        //TC20
        IDUser=66; amount=5;
        Assertions.assertNotEquals(0,amount,"Amount diferente de 0");
        Assertions.assertNull(u,"Utilizador é nulo, não é possível atribuir crédito");

    }

    @Test
    public void testRegisterUser() {

        int IDUser, rentalprogram;
        String name;

        //#TC21
        IDUser=55;rentalprogram=2;
        name="Sterben";

        int finalIDUser = IDUser; int finalRentalprogram = rentalprogram;
        String finalName = name;

        Assertions.assertDoesNotThrow(()->brs.registerUser(finalIDUser, finalName, finalRentalprogram));

       //#TC22
        IDUser=66;rentalprogram=1;
        name="Palpatine";

        int finalIDUser1 = IDUser; int finalRentalprogram1 = rentalprogram;
        String finalName1 = name;

        Assertions.assertThrows(UserAlreadyExists.class,
                () -> brs.registerUser(finalIDUser1, finalName1, finalRentalprogram1));

    }
}
