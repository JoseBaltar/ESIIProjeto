import org.junit.jupiter.api.*;

import Exceptions.UserAlreadyExists;
import Exceptions.UserDoesNotExists;
import Models.User;
import Models.Bike;

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

        //#TC19, método a ser testado -> "registerUser()"
        int IDUser=55, rentalprogram=1;
        String name="Sterben";

        Assertions.assertDoesNotThrow(() -> brs.registerUser(IDUser, name, rentalprogram));

        //adicionar um utilizador para ser utilizado nos testes
        existingUser = brs.getUsers().get(0);

    }

    @Test
    public void testGetBicycleBemSucedido() throws UserDoesNotExists {
        int IDUser = 55, IDDeposit = 5, starttime = 1,
                //num contexto real de utilização, estas variáveis já teriam sido inicializadas
                IDLock = 1, IDBike = 1;

        //adicionar componentes ao User para que seja possivel executar os testes
        existingUser.setCredit(5);
        ///adicionar um deposit cheio
        brs.addBicycle(IDDeposit, IDLock, IDBike);

        //#TC0
        Assertions.assertEquals(starttime, existingUser.getStartRental(),
                "Starttime sinalizado deveria ser igual ao introduzido.");

        if ( brs.getDeposits().get(0).getIDDeposit() == IDDeposit) {
            if (brs.getDeposits().get(0).getLocks().get(0).getIDLock() == IDLock) {

                Assertions.assertFalse(brs.getDeposits().get(0).getLocks().get(0).isInUse(),
                        "O lugar deveria ter sido libertado.");

                Assertions.assertEquals(
                        brs.getDeposits().get(0).getLocks().get(0).getBike().getIDBike(),
                        brs.getBicycle(IDDeposit, IDUser, starttime),
                        "Deveria ter sido retornado o ID da bicicleta.");
            }
        }

    }

    @Test
    public void testGetBicycleMalSucedido() throws UserDoesNotExists {
        int IDUser = 55, IDDeposit = 5, starttime = 1, expected = -1,
                //num contexto real de utilização, estas variáveis já teriam sido inicializadas
                IDLock = 1, IDBike = 1;

        //adicionar componentes ao User para que seja possivel executar os testes
        existingUser.setCredit(5);
        //adicionar um deposit vazio
        brs.addLock(IDDeposit, IDLock);

        //#TC1
        Assertions.assertEquals(expected,
                brs.getBicycle(IDDeposit,IDUser,starttime),
                "Não deveriam existir bikes disponíveis. (return -1)");

        //adicionar um deposit cheio
        brs.addBicycle(IDDeposit, IDLock, IDBike);

        //#TC2
        IDUser=44;
        int finalIDDeposit = IDDeposit, finalIDUser = IDUser, finalStarttime = starttime;
        Assertions.assertThrows(UserDoesNotExists.class,
                () -> brs.getBicycle(finalIDDeposit, finalIDUser, finalStarttime));

        //#TC3
        IDUser=55; IDDeposit = 9;
        Assertions.assertEquals(expected,
                brs.getBicycle(IDDeposit,IDUser,starttime),
                "Depósito não deveria existir. (return -1)");

        //#TC4
        existingUser.setCredit(0);
        IDDeposit=5;
        Assertions.assertEquals(expected,
                brs.getBicycle(IDDeposit,IDUser,starttime),
                "User não deveria ter crédito. (return -1)");

        //#TC5
        existingUser.setCredit(5);
        //Adicionar uma bike ao User
        Bike testBike = new Bike(IDBike+1);
        existingUser.setBike(testBike);
        //colocar a bike em aluguer ativo
        existingUser.getBike().setInUSe(true);

        Assertions.assertEquals(expected,
                brs.getBicycle(IDDeposit,IDUser,starttime),
                "User deveria ter um aluguer já ativo. (return -1)");


    }

    @Test
    public void testReturnBicycleBemSucedido() {
        int IDUser = 55, IDDeposit = 5, endtime = 12,
                //num contexto real de utilização, estas variáveis já teriam sido inicializadas
                IDLock = 1, IDBike = 1, starttime = 2;

        //Adicionar uma bike ao User
        Bike testBike = new Bike(IDBike);
        existingUser.setBike(testBike);
        //colocar a bike em aluguer ativo
        existingUser.getBike().setInUSe(true);
        //adicionar componentes ao User para que seja possivel executar os testes
        existingUser.setCredit(5);
        existingUser.setStartRental(starttime);
        //adicionar um deposit vazio
        brs.addLock(IDDeposit, IDLock);

        //#TC6
        float inicialCredit = existingUser.getCredit();
        Assertions.assertNotEquals(inicialCredit,
                brs.returnBicycle(IDDeposit, IDUser, endtime),
                "Deveria retornar o saldo atual do user após feito o cálculo do " +
                        "método BicycleRentalFee().");
    }

    @Test
    public void testReturnBicycleMalSucedido() {
        int IDUser = 55, IDDeposit = 5, endtime = 12, expected = -1,
                //num contexto real de utilização, estas variáveis já teriam sido inicializadas
                IDLock = 1, IDBike = 1, starttime = 2;

        //Adicionar uma bike ao user
        Bike testBike = new Bike(IDBike);
        existingUser.setBike(testBike);
        //colocar a bike em aluguer ativo
        existingUser.getBike().setInUSe(true);
        //adicionar componentes ao User para que seja possivel executar os testes
        existingUser.setCredit(5);
        existingUser.setStartRental(starttime);
        //adicionar um deposit cheio
        brs.addBicycle(IDDeposit, IDLock, IDBike+1);

        //#TC7
        Assertions.assertEquals(expected,
                brs.returnBicycle(IDDeposit,IDUser,endtime),
                "Não deveria haver um lugar disponível para entrega. (return -1)");

        //adicionar um deposit vazio
        brs.addLock(IDDeposit, IDLock+1);

        //#TC8
        //retirar a bike do aluguer ativo
        existingUser.getBike().setInUSe(false);
        Assertions.assertEquals(expected,
                brs.returnBicycle(IDDeposit,IDUser,endtime),
                "Bicicleta não deveria ter aluguer ativo. (return -1)");

        //#TC9
        existingUser.getBike().setInUSe(true);
        IDDeposit=42;
        Assertions.assertEquals(expected,
                brs.returnBicycle(IDDeposit,IDUser,endtime),
                "O Depósito não deveria existir. (return -1)");

        //#TC10
        IDUser=66; IDDeposit = 1;
        Assertions.assertEquals(expected,
                brs.returnBicycle(IDDeposit,IDUser,endtime),
                "Utilizador não deveria existir. (return -1)");

    }

    @Test
    public void testBicycleRentalFee() {
        int rentalProgram, starttime, endtime, nRentals, expectedResult;

        //#TC11
        rentalProgram = 1;starttime = 2;endtime = 6; nRentals = 3;
        expectedResult = (endtime-starttime)*brs.getRentalFee();
        Assertions.assertEquals(expectedResult,
                brs.bicycleRentalFee(rentalProgram,starttime,endtime,nRentals),
                "Cálculos errados. Fórmula = (endtime - starttime) * rentalFee");

        //#TC12
        rentalProgram = 2;starttime = 5;endtime = 7; nRentals = 5;
        expectedResult = (endtime-starttime)*brs.getRentalFee();
        Assertions.assertEquals(expectedResult,
                brs.bicycleRentalFee(rentalProgram,starttime,endtime,nRentals),
                "Cálculos errados. Fórmula = (endtime - starttime) * rentalFee");

        //#TC13
        rentalProgram = 2;starttime = 5;endtime = 17; nRentals = 15;
        expectedResult = 10*brs.getRentalFee()+((endtime-starttime)-10)*brs.getRentalFee()/2;
        Assertions.assertEquals(expectedResult,
                brs.bicycleRentalFee(rentalProgram,starttime,endtime,nRentals),
                "Cálculos errados. Fórmula = 10*rentalFee + ((endtime - startime)-10)* rentalFee / 2");

        //#TC14
        rentalProgram = 2;starttime = 5;endtime = 12; nRentals = 20;
        expectedResult = 0;
        Assertions.assertEquals(expectedResult,
                brs.bicycleRentalFee(rentalProgram,starttime,endtime,nRentals),
                "Retorno deveria ser 0");

    }

    @Test
    public void testVerifyCredit() {

        int IDUser = 55;

        //#TC15
        float testCredit = 5;
        existingUser.setCredit(testCredit);
        Assertions.assertTrue(brs.verifyCredit(IDUser),"Crédito deveria ser positivo (retornar true)");

        //#TC16
        testCredit = 0;
        existingUser.setCredit(testCredit);
        Assertions.assertFalse(brs.verifyCredit(IDUser),"Crédito não deveria ser positivo (retornar false)");

        //Novamente, não faz sentido adicionar um teste quando o user não existe porque nada é retornado para testar isso
        //(o método retorna false, mas isso está relacionado com o crédito e não com o user)

    }

    @Test
    public void testAdicionarCreditoAoUser() {

        //#TC17
        int IDUser = 55, amount=3;
        brs.addCredit(IDUser, amount);

        float expected = existingUser.getCredit() + amount;
        Assertions.assertEquals(expected, existingUser.getCredit(), "Deveria ter adicionado um montante ao existente");

        //#TC18
        int amount2 = 0;
        brs.addCredit(IDUser, amount2);

        expected = existingUser.getCredit() + amount2;
        Assertions.assertEquals(expected, existingUser.getCredit(), "Não deveria ter sido adicionado crédito");

        //Não faz sentido adicionar um teste quando o user não existe porque o método é do tipo 'Void' ...

    }

    @Test
    public void testVerificarSeUserFoiRegistado() {
        //#TC20
        int IDUser=55, rentalprogram=2;
        String name="Sterben";

        //utilizando o mesmo utilizador inserido anteriormente
        Assertions.assertThrows(UserAlreadyExists.class,
                () -> brs.registerUser(IDUser, name, rentalprogram));

    }
}
