import org.junit.jupiter.api.*;

import Exceptions.UserAlreadyExists;
import Exceptions.UserDoesNotExists;
import Models.User;

/**
 * Faltam Adicionar os testes das classes inválidas!!
 */
public class BikeRentalSystemTest {
    BikeRentalSystem brs;
    User existingUser;

    /**
     * Este método cria uma instância da classe que contém os métodos a serem testados.
     *
     * Por conveniência, também é inicializado um elemento 'User' para utilizar nos métodos de teste.
     */
    @BeforeEach
    public void setUp() {
        /* rentalFee = 1, significa que cada unidade de tempo de aluguer da bicicleta
         * corresponde a 1 unidade de crédito
         */
        brs = new BikeRentalSystem(1);

        //#TC21, método a ser testado -> "registerUser()"
        int IDUser=55, rentalprogram=2;
        String name="Sterben";

        Assertions.assertDoesNotThrow(()->brs.registerUser(IDUser, name, rentalprogram));

        //adicionar um utilizador para ser utilizado nos testes
        existingUser = brs.getUsers().get(0);
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

        //#TC1 É necessário retornar o saldo do cliente... Como? Nasce a mesma situação do teste case mais lá em cima...
        IDUser=55;IDDeposit=55;endtime=12;


        //#TC2
        IDUser=44; IDDeposit=66; endtime=13;
        int finalIDDeposit = IDDeposit, finalIDUser = IDUser, finalEndtime = endtime;
        Assertions.assertThrows(UserDoesNotExists.class,
                ()->brs.getBicycle(finalIDDeposit, finalIDUser, finalEndtime));

        //#TC3
        IDUser=23; IDDeposit=77; endtime=6;
        Assertions.assertEquals(-1,brs.returnBicycle(IDDeposit,IDUser,endtime),"Depósito deveria existir");

        //#TC4
        IDUser=66;IDDeposit=44;endtime=4;
        Assertions.assertEquals(-1,brs.returnBicycle(IDDeposit,IDUser,endtime),"Utilizador deveria ter uma bicicleta com aluguer ativo");

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
    public void testAdicionarCreditoAoUser() {

        //#TC18
        int IDUser = 55, amount=3;
        brs.addCredit(IDUser, amount);

        float expected = existingUser.getCredit() + amount;
        Assertions.assertEquals(expected, existingUser.getCredit(), "Deveria ter adicionado um montante ao existente");

        //#TC19
        int amount2 = 0;
        brs.addCredit(IDUser, amount2);

        expected = existingUser.getCredit() + amount2;
        Assertions.assertEquals(expected, existingUser.getCredit(), "Não deveria ter sido adicionado crédito");

        //Não faz sentido adicionar um teste quando o user não existe porque o método é do tipo 'Void' ...

    }

    @Test
    public void testVerificarSeUserFoiRegistado() {
        //#TC22
        int IDUser=55, rentalprogram=2;
        String name="Sterben";

        //utilizando o mesmo utilizador inserido anteriormente
        Assertions.assertThrows(UserAlreadyExists.class,
                () -> brs.registerUser(IDUser, name, rentalprogram));

    }
}
