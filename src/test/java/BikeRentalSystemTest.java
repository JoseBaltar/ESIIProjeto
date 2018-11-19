import Exceptions.UserAlreadyExists;
import Exceptions.UserDoesNotExists;
import Models.Bike;
import Models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * --Algumas observações--
 * <p>
 * => Podiam ter sido adicionados mais testes de boundary values do tipo:
 * - Verificar se existem bicicletas para alugar (getBicycle()) utilizando
 * um conjunto com mais de uma lock, ...
 * (Só esta a ser testado com uma lock)
 * - Podiam ter sido verificados mais valores...
 * <p>
 * => A verificação de IDUser negativo em todos os métodos é redundante, uma vez que
 * essa verificação já é feita no métodp registerUser(), fazendo com que seja 'impossível'
 * existir um Utilizador com o ID negativo
 */
public class BikeRentalSystemTest {
    private BikeRentalSystem brs;
    private User existingUser;

    /**
     * Este método cria uma instância da classe que contém os métodos a serem testados.
     * <p>
     * Por conveniência, também é inicializado um elemento 'User' para utilizar nos métodos de teste.
     */
    @BeforeEach
    public void setUp() {
        /* rentalFee = 1, significa que cada unidade de tempo de aluguer da bicicleta
         * corresponde a 1 unidade de crédito
         */
        brs = new BikeRentalSystem(1);

        try {
            //adicionar um utilizador para ser utilizado nos testes
            int IDUser = 55, rentalprogram = 1;
            String name = "Sterben";

            brs.registerUser(IDUser, name, rentalprogram);
            existingUser = brs.getUsers().get(0);
        } catch (UserAlreadyExists userAlreadyExists) {
            userAlreadyExists.printStackTrace();
        }
    }

    /**
     * Método a ser testado -> 'getBicycle()'
     */
    @Test
    public void testGetBicycleBemSucedido() {
        Assertions.assertAll(
                () -> {
                    final int IDUser = 55, IDDeposit = 5, starttime = 1, credit = 5,
                            //num contexto real de utilização, estas variáveis já teriam sido inicializadas
                            IDLock = 2, IDBike = 2;

                    //adicionar componentes ao User para que seja possivel executar os testes
                    existingUser.setCredit(credit);
                    //adicionar um deposit cheio
                    brs.addBicycle(IDDeposit, IDLock, IDBike);

                    //#TC0
                    Assertions.assertAll(
                            () -> Assertions.assertEquals(
                                    brs.getDeposits().get(0).getLocks().get(0).getBike().getIDBike(),
                                    brs.getBicycle(IDDeposit, IDUser, starttime),
                                    "Deveria ter sido retornado o ID da bicicleta."),

                            () -> Assertions.assertFalse(brs.getDeposits().get(0).getLocks().get(0).isInUse(),
                                    "O lugar deveria ter sido libertado."),

                            () -> Assertions.assertEquals(starttime, existingUser.getStartRental(),
                                    "Starttime sinalizado deveria ser igual ao introduzido.")
                    );
                }
        );
    }

    /**
     * Método a ser testado -> 'getBicycle()'
     */
    @Test
    public void testGetBicycleMalSucedido() {
        final int IDDeposit = 5, IDUser = 55, starttime = 1, expected = -1, defaultCredit = 5,
                //num contexto real de utilização, estas variáveis já teriam sido inicializadas
                IDLock = 2, IDBike = 2;

        //adicionar componentes ao User para que seja possivel executar os testes
        existingUser.setCredit(defaultCredit);
        //adicionar um deposit vazio
        brs.addLock(IDDeposit, IDLock);

        Assertions.assertAll(
                //#TC1
                () -> Assertions.assertEquals(expected,
                        brs.getBicycle(IDDeposit, IDUser, starttime),
                        "Não deveriam existir bikes disponíveis. (return -1)"),

                () -> {
                    //colocar uma bicicleta no deposito vazio
                    brs.addBicycle(IDDeposit, IDLock, IDBike);

                    Assertions.assertAll(
                            //#TC2
                            () -> {
                                int invalidIDUser = 44;
                                Assertions.assertThrows(UserDoesNotExists.class,
                                        () -> brs.getBicycle(IDDeposit, invalidIDUser, starttime),
                                        "Deveria ter sido retornado uma exceção 'UserDoesNotExists'");
                            },
                            //#TC3
                            () -> {
                                int invalidIDDeposit = 9;
                                Assertions.assertEquals(expected,
                                        brs.getBicycle(invalidIDDeposit, IDUser, starttime),
                                        "Depósito não deveria existir. (return -1)");
                            },
                            //#TC4
                            () -> {
                                existingUser.setCredit(0);
                                Assertions.assertEquals(expected,
                                        brs.getBicycle(IDDeposit, IDUser, starttime),
                                        "User não deveria ter crédito. (return -1)");
                            },
                            //#TC5
                            //Test Failed!!
                            //Código fonte com condições por verificar em falta!
                            () -> {
                                //Adicionar uma bike ao User
                                Bike testBike = new Bike(IDBike + 1);
                                existingUser.setBike(testBike);
                                //colocar a bike em aluguer ativo
                                existingUser.getBike().setInUSe(true);

                                //teste devia ter falhado.
                                //o programa não deteta a bike em uso!
                                Assertions.assertEquals(expected,
                                        brs.getBicycle(IDDeposit, IDUser, starttime),
                                        "User deveria ter um aluguer já ativo. (return -1)");
                            }
                    );
                }
        );
    }

    /**
     * Método a ser testado -> 'returnBicycle()'
     */
    @Test
    public void testReturnBicycleBemSucedido() {
        Assertions.assertAll(
                () -> {
                    final int IDUser = 55, IDDeposit = 5, endtime = 12,
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
                            "Deveria retornar o saldo atual do User após ter sido feito o cálculo do " +
                                    "método BicycleRentalFee().");
                }
        );
    }

    /**
     * Método a ser testado -> 'returnBicycle()'
     */
    @Test
    public void testReturnBicycleMalSucedido() {
        final int IDUser = 55, IDDeposit = 5, endtime = 12, expected = -1,
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
        brs.addBicycle(IDDeposit, IDLock, IDBike + 1);

        Assertions.assertAll(
                //#TC7
                () -> Assertions.assertEquals(expected,
                        brs.returnBicycle(IDDeposit, IDUser, endtime),
                        "Não deveria haver um lugar disponível para entrega. (return -1)"),

                () -> {
                    //adicionar um deposit vazio
                    brs.addLock(IDDeposit, IDLock + 1);

                    Assertions.assertAll(
                            //#TC8
                            () -> {
                                //retirar a bike do aluguer ativo
                                existingUser.getBike().setInUSe(false);
                                Assertions.assertEquals(expected,
                                        brs.returnBicycle(IDDeposit, IDUser, endtime),
                                        "Bicicleta não deveria ter aluguer ativo. (return -1)");
                            },
                            //#TC9
                            () -> {
                                int invalidIDDeposit = 42;
                                Assertions.assertEquals(expected,
                                        brs.returnBicycle(invalidIDDeposit, IDUser, endtime),
                                        "O Depósito não deveria existir. (return -1)");
                            },
                            //#TC10
                            () -> {
                                int invalidIDUser = 66;
                                Assertions.assertEquals(expected,
                                        brs.returnBicycle(IDDeposit, invalidIDUser, endtime),
                                        "Utilizador não deveria existir. (return -1)");
                            }
                    );
                }
        );
    }

    /**
     * Método a ser testado -> 'bicycleRentalFee()'
     */
    @Test
    public void testCalculoBicycleRentalFee() {
        Assertions.assertAll(
                //#TC11
                () -> {
                    int rentalProgram = 1, starttime = 2,
                            endtime = 6, nRentals = 3;
                    int expectedResult = (endtime - starttime) * brs.getRentalFee();
                    Assertions.assertEquals(expectedResult,
                            brs.bicycleRentalFee(rentalProgram, starttime, endtime, nRentals),
                            "Cálculos errados. Fórmula = (endtime - starttime) * rentalFee");
                },
                //#TC12
                () -> {
                    int rentalProgram = 2, starttime = 5,
                            endtime = 7, nRentals = 5;
                    int expectedResult = (endtime - starttime) * brs.getRentalFee();
                    Assertions.assertEquals(expectedResult,
                            brs.bicycleRentalFee(rentalProgram, starttime, endtime, nRentals),
                            "Cálculos errados. Fórmula = (endtime - starttime) * rentalFee");
                },
                //#TC13
                () -> {
                    int rentalProgram = 2, starttime = 5,
                            endtime = 17, nRentals = 15;
                    int expectedResult = 10 * brs.getRentalFee() + ((endtime - starttime) - 10) * brs.getRentalFee() / 2;
                    Assertions.assertEquals(expectedResult,
                            brs.bicycleRentalFee(rentalProgram, starttime, endtime, nRentals),
                            "Cálculos errados. Fórmula = 10*rentalFee + ((endtime - startime)-10)* rentalFee / 2");
                },
                //#TC14
                () -> {
                    int rentalProgram = 2, starttime = 5,
                            endtime = 17, nRentals = 20;
                    int expectedResult = 0;
                    Assertions.assertEquals(expectedResult,
                            brs.bicycleRentalFee(rentalProgram, starttime, endtime, nRentals),
                            "Retorno deveria ser 0");
                }
        );
    }

    /**
     * Método a ser testado -> 'bicycleRentalFee()'
     */
    @Test
    public void testCalculoInvalidoBicycleRentalFee() {
        Assertions.assertAll(
                //#TC15
                //Test Failed!!
                //Código fonte com condições por verificar em falta!
                () -> {
                    int rentalProgram = 2, starttime = 5,
                            endtime = 17, nRentals = -3;
                    int expectedResult = -1;
                    Assertions.assertEquals(expectedResult,
                            brs.bicycleRentalFee(rentalProgram, starttime, endtime, nRentals),
                            "Retorno deveria ser -1(não aceitar input) (nRentals negativo)");
                },
                //#TC16
                //Test Failed!!
                //Código fonte com condições por verificar em falta!
                () -> {
                    int rentalProgram = 2, starttime = 15,
                            endtime = 5, nRentals = 3;
                    int expectedResult = -1;
                    Assertions.assertEquals(expectedResult,
                            brs.bicycleRentalFee(rentalProgram, starttime, endtime, nRentals),
                            "Retorno deveria ser -1(não aceitar input) (start>end)");
                }
        );
    }

    /**
     * Método a ser testado -> "verifyCredit()"
     */
    @Test
    public void testVerifyCredit() {
        int IDUser = 55;

        Assertions.assertAll(
                //#TC17
                () -> {
                    float testCredit = 5;
                    existingUser.setCredit(testCredit);
                    Assertions.assertTrue(brs.verifyCredit(IDUser),
                            "Crédito deveria ser positivo (retornar true)");
                },
                //#TC18
                () -> {
                    float testCredit = 0;
                    existingUser.setCredit(testCredit);
                    Assertions.assertFalse(brs.verifyCredit(IDUser),
                            "Crédito não deveria ser positivo (retornar false)");
                }
        );
    }

    /**
     * Método a ser testado -> "addCredit()"
     */
    @Test
    public void testAdicionarCreditoAoUser() {
        int IDUser = 55;

        Assertions.assertAll(
                //#TC19
                () -> {
                    int amount = 3;
                    float expected = existingUser.getCredit() + amount;

                    brs.addCredit(IDUser, amount);
                    Assertions.assertEquals(expected, existingUser.getCredit(),
                            "Deveria ter adicionado um montante ao existente " +
                                    "(credito inicial = 0)");
                },
                //#TC21
                () -> {
                    int amount = 3;
                    existingUser.setCredit(15);
                    float expected = existingUser.getCredit() + amount;

                    brs.addCredit(IDUser, amount);
                    Assertions.assertEquals(expected, existingUser.getCredit(),
                            "Deveria ter adicionado um montante ao existente " +
                                    "(crédito inicial > 0)");
                }
        );
    }

    /**
     * Método a ser testado -> "addCredit()"
     */
    @Test
    public void testAdicionarCreditoAoUserInvalido() {
        int IDUser = 55;
        Assertions.assertAll(
                //#TC20
                () -> {
                    int amount = 0;
                    float expected = existingUser.getCredit() + amount;

                    brs.addCredit(IDUser, amount);
                    Assertions.assertEquals(expected, existingUser.getCredit(),
                            "Não deveria ter sido adicionado crédito (amount=0)");
                },
                //#TC22
                () -> {
                    int amount = -3;
                    float expected = existingUser.getCredit();

                    brs.addCredit(IDUser, amount);
                    Assertions.assertEquals(expected, existingUser.getCredit(),
                            "Não deveria ter sido adicionado crédito (amount<0)");
                }
        );
    }

    /**
     * Método a ser testado -> "registerUser()"
     */
    @Test
    public void testVerificarSeUserRegistaEJaExiste() {
        Assertions.assertAll(
                //#TC23 - Novo User
                () -> {
                    int IDUser = 60, rentalprogram = 2;
                    String name = "Cyrodil";

                    Assertions.assertDoesNotThrow(
                            () -> brs.registerUser(IDUser, name, rentalprogram));
                },
                //#TC25 - User existente
                () -> {
                    int existingIDUser = 55, existingRentalprogram = 1;
                    String existingName = "Sterben";
                    //utilizando o mesmo utilizador inserido anteriormente
                    Assertions.assertThrows(UserAlreadyExists.class,
                            () -> brs.registerUser(existingIDUser, existingName, existingRentalprogram));

                    //#TC24 - Verificar se foi adicionado
                    Assertions.assertNotNull(findUserByID(existingIDUser),
                            "User deveria ter sido adicionado");
                }
        );
    }

    /**
     * Método a ser testado -> "registerUser()"
     */
    @Test
    public void testVerificarQuandoUserNaoDeveRegistar() {
        Assertions.assertAll(
                //#TC26
                //Test Failed!!
                //Código fonte com condições por verificar em falta!
                () -> {
                    int IDUser = 60, rentalprogram = 1;
                    String name = null;

                    brs.registerUser(IDUser, name, rentalprogram);
                    Assertions.assertNull(findUserByID(IDUser),
                            "User não deveria ter sido adicionado (name=null)");
                },
                //#TC27
                //Test Failed!!
                //Código fonte com condições por verificar em falta!
                () -> {
                    int IDUser = 65, rentalprogram = 4;
                    String name = "Jabba";

                    brs.registerUser(IDUser, name, rentalprogram);
                    Assertions.assertNull(findUserByID(IDUser),
                            "User não deveria ter sido adicionado " +
                                    "(rentalProgram!=1 && rentalProgram!=2)");
                },
                //#TC28
                //Test Failed!!
                //Código fonte com condições por verificar em falta!
                () -> {
                    int IDUser = -10, rentalprogram = 2;
                    String name = "Palpatine";

                    //Retorna NullPointerException devido a ser um número negativo.
                    brs.registerUser(IDUser, name, rentalprogram);
                    Assertions.assertNull(findUserByName(name),
                            "User não deveria ter sido adicionado (IDUser < 0)");
                }
        );
    }

    /**
     * Encontrar um utilizador existente
     *
     * @param IDUser id do user a encontrar
     * @return User - se existe, null - se não existe
     */
    private User findUserByID(int IDUser) {
        int i = 0;
        while (i < brs.getUsers().size()) {
            if (brs.getUsers().get(i).getIDUser() == IDUser) {
                return brs.getUsers().get(i);
            }
            i++;
        }
        return null;
    }

    /**
     * Encontrar um utilizador existente
     *
     * @param name nome do user a encontrar
     * @return User - se existe, null - se não existe
     */
    private User findUserByName(String name) {
        int i = 0;
        while (i < brs.getUsers().size()) {
            if (brs.getUsers().get(i).getName().equals(name)) {
                return brs.getUsers().get(i);
            }
            i++;
        }
        return null;
    }
}
