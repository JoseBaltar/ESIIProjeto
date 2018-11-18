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
    public void testAdicionarCreditoAoUser() throws UserAlreadyExists {

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
