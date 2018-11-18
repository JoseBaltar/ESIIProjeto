import Exceptions.UserAlreadyExists;
import Exceptions.UserDoesNotExists;
import Models.User;
import org.junit.Assert;
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
       /* int IDUser = 66, IDDeposit = 12, startTime = 12;
        Assertions.assertThrows(UserDoesNotExists.class,
                () -> brs.getBicycle(IDDeposit, IDUser, startTime));
                Deixei estar por ser o original que foi feito
                */
        int IDUser, IDDeposit, starttime;


        //#TC5
        IDUser=55; IDDeposit=55; starttime=12;
        //Como comparar em Assertions.assertEquals(), se não sabemos o IDlock que é suposto retornar? Rever amanhã

        //#TC6
        IDUser=66; IDDeposit=44; starttime=12;
        int finalIDDeposit = IDDeposit, finalIDUser = IDUser, finalStarttime = starttime;
        Assertions.assertThrows(UserDoesNotExists.class,
                ()->brs.getBicycle(finalIDDeposit, finalIDUser, finalStarttime));

        //#TC7
        IDUser=23; IDDeposit=77; starttime=6;
        Assertions.assertEquals(-1,brs.returnBicycle(IDDeposit,IDUser,starttime),"Depósito não existe, devia existir");

        //#TC8
        IDUser=66; IDDeposit=12; starttime=0;
        Assertions.assertEquals(-1,brs.returnBicycle(IDDeposit,IDUser,starttime),"Saldo deveria ser superior a 1");

        //#TC9
        IDUser=55; IDDeposit=12; starttime=5;
        Assertions.assertEquals(-1,brs.returnBicycle(IDDeposit,IDUser,starttime),"O Utilizador não deveria ter aluguer ativo");

        //#TC10
        IDUser=44; IDDeposit=0; starttime=12;
        Assertions.assertEquals(-1,brs.returnBicycle(IDDeposit,IDUser,starttime),"Deveriam existir bicicletas disponíveis");


    }

    @Test
    public void testReturnBicycle() {

        int IDUser, IDDeposit, endtime;


    }

    @Test
    public void testBicycleRentalFee() {

    //estava a confundir o fee com o RentalProgram... Pronto, já percebi LMAO

        int rentalProgram, starttime,endtime, nRentals, expectedResult;

        //#TC11
        rentalProgram=1;starttime=2;endtime=6; nRentals=3;
        expectedResult=(endtime-starttime)*brs.getRentalFee();
        Assertions.assertEquals(expectedResult,brs.bicycleRentalFee(rentalProgram,starttime,endtime,nRentals),"Mensagem");

        //#TC12
        rentalProgram=2;starttime=5;endtime=7; nRentals=6;
        expectedResult=(endtime-starttime)*brs.getRentalFee();
        Assertions.assertEquals(expectedResult,brs.bicycleRentalFee(rentalProgram,starttime,endtime,nRentals),"Mensagem");

        //#TC13
        rentalProgram=2;starttime=5;endtime=17; nRentals=9;
        expectedResult=10*brs.getRentalFee()+((endtime-starttime)-10)*brs.getRentalFee()/2;
        Assertions.assertEquals(expectedResult,brs.bicycleRentalFee(rentalProgram,starttime,endtime,nRentals),"Mensagem");

        //#TC14
        rentalProgram=2;starttime=5;endtime=12; nRentals=10;
        expectedResult=0;
        Assertions.assertEquals(expectedResult,brs.bicycleRentalFee(rentalProgram,starttime,endtime,nRentals),"Mensagem");


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
