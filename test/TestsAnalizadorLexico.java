import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import junit.framework.TestCase;

@RunWith(Parameterized.class)
public class TestsAnalizadorLexico extends TestCase
{
	//Excepción de salida
    protected static class ExitException extends SecurityException 
    {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public final int status;
        public ExitException(int status) 
        {
            super("There is no escape!");
            this.status = status;
        }
    }

    //Security Manager preparado para evitar la salida
    private static class NoExitSecurityManager extends SecurityManager 
    {
        @Override
        public void checkPermission(Permission perm) 
        {
            // allow anything.
        }
        @Override
        public void checkPermission(Permission perm, Object context) 
        {
            // allow anything.
        }
        @Override
        public void checkExit(int status) 
        {
            super.checkExit(status);
            throw new ExitException(status);
        }
    }
    
    
    //Buffers para usar en entrada-salida
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    
    @Override
    @Before
    public void setUp() throws Exception 
    {
        super.setUp();
      //Redirigir buffers de entrada-salida
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        //Cambiar gestor de seguridad
        System.setSecurityManager(new NoExitSecurityManager());
    }

    @Override
    @After
    public void tearDown() throws Exception 
    {
    	//Cambiar sistema de seguridad
        System.setSecurityManager(null); // or save and restore original
        //Limpiar buffers
        System.setOut(null);
        System.setErr(null);
        super.tearDown();
    }

    @Parameters(name = "{index}: fuente:{0}")
    //Parámetros de los distintos tests
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {  
        	//Pruebas del profesor
            
                 {"resources/fuentes/p01.txt", Arrays.asList(new Token(1,1,"main",13)), 
                	 "","Error lexico (1,5): caracter '.' incorrecto", -1 },
                 {"resources/fuentes/p02.txt", Arrays.asList(new Token(1,1,"main",13), new Token(1,5,"(",0), new Token(1,6,")",1),
                		 new Token(2,1,"{",7), new Token(3,3,"int",12), new Token(3,7,"a",15), new Token(3,8,";",4),
                		 new Token(3,10,"int",12), new Token(3,14,"b",15), new Token(3,15,";",4), new Token(5,3,"a",15),
                		 new Token(5,5,"=",6), new Token(5,7,"7",14), new Token(6,3,"b",15), new Token(6,5,"=",6),
                		 new Token(6,7,"8",14), new Token(6,8,";",4), new Token(8,1,"}",8)), 
                		 "", "", 0 },
                 {"resources/fuentes/p03.txt", Arrays.asList(new Token(2,1,"double",11), new Token(2,8,"funsion",15), new Token(2,15,"(",0), new Token(2,16,")",1),
                		 new Token(3,1,"{",7), new Token(4,3,"int",12), new Token(4,7,"a",15), new Token(4,8,",",5),
                		 new Token(4,9,"aa",15), new Token(4,11,"[",9), new Token(4,12,"10",14), new Token(4,14,"]",10),
                		 new Token(4,15,";",4), new Token(5,3,"double",11), new Token(5,10,"b",15), new Token(5,11,";",4),
                		 new Token(7,3,"a",15), new Token(7,5,"=",6), new Token(7,7,"7",14), new Token(7,8,"+",3)
                		 , new Token(7,9,"3",14), new Token(7,10,";",4), new Token(8,3,"b",15), new Token(8,5,"=",6)
                		 , new Token(8,7,"2",14), new Token(8,8,"+",3), new Token(8,9,"3",14), new Token(8,10,"*",2)
                		 , new Token(8,11,"4",14), new Token(8,12,";",4), new Token(9,3,"b",15), new Token(9,5,"=",6)
                		 , new Token(9,7,"2.5",16), new Token(9,11,"*",2), new Token(9,13,"a",15), new Token(9,14,";",4)
                		 , new Token(10,3,"{",7), new Token(11,5,"double",11), new Token(11,12,"c",15), new Token(11,13,";",4)
                		 , new Token(13,5,"c",15), new Token(13,7,"=",6), new Token(13,9,"b",15), new Token(13,10,";",4)
                		 , new Token(14,5,"c",15), new Token(14,7,"=",6), new Token(14,9,"a",15), new Token(14,10,";",4)
                		 , new Token(15,3,"}",8), new Token(16,1,"}",8), new Token(18,1,"main",13), new Token(18,5,"(",0)
                		 , new Token(18,6,")",1), new Token(19,1,"{",7), new Token(20,3,"double",11), new Token(20,10,"d",15)
                		 , new Token(20,11,";",4), new Token(22,3,"{",7), new Token(23,5,"d",15), new Token(23,7,"=",6)
                		 , new Token(23,9,"8.5",16), new Token(23,12,";",4), new Token(24,5,"int",12), new Token(24,9,"e",15)
                		 , new Token(24,10,";",4), new Token(26,5,"{",7), new Token(27,7,"double",11), new Token(27,14,"e",15)
                		 , new Token(27,15,";",4), new Token(29,7,"e",15), new Token(29,9,"=",6), new Token(29,11,"d",15)
                		 , new Token(29,12,"-",3), new Token(29,13,"7.5",16), new Token(29,16,";",4), new Token(30,5,"}",8)
                		 , new Token(31,3,"}",8), new Token(32,1,"}",8)), 
                			 "", "", 0 },
                 {"resources/fuentes/p04.txt", Arrays.asList(new Token(1,1,"main",13), new Token(1,5,"(",0), 
                		 new Token(1,6,")",1), new Token(2,1,"{",7), new Token(3,3,"int",12),new Token(3,7,"a",15),
                		 new Token(3,8,";",4), new Token(5,3,"{",7), new Token(6,5,"double",11), new Token(6,12,"b",15),
                		 new Token(6,13,";",4), new Token(8,5,"b",15), new Token(8,6,"=",6), new Token(8,7,"7",14),
                		 new Token(8,8,";",4), new Token(9,5,"{",7), new Token(10,7,"int",12), new Token(10,11,"c",15),
                		 new Token(10,12,";",4), new Token(12,7,"c",15), new Token(12,9,"=",6), new Token(12,11,"8",14),
                		 new Token(12,12,";",4), new Token(13,5,"}",8), new Token(14,3,"}",8), new Token(15,3,"a",15),
                		 new Token(15,5,"=",6), new Token(15,7,"c",15), new Token(15,8,";",4), new Token(16,1,"}",8)), 
                				 "","", 0 },
                 {"resources/fuentes/p05.txt", Arrays.asList(new Token(1,1,"main",13), new Token(1,5,"(",0), 
                		 new Token(1,6,")",1), new Token(1,8,"{",7),
                		 new Token(2,3,"double",11), new Token(2,10,"da",15), new Token(2,12,";",4), new Token(3,3,"double",11),
                		 new Token(3,10,"db",15), new Token(3,12,";",4), new Token(4,3,"double",11), new Token(4,10,"dc",15),
                		 new Token(4,12,";",4), new Token(5,3,"double",11), new Token(5,10,"dd",15), new Token(5,12,";",4),
                		 new Token(6,3,"int",12), new Token(6,7,"ea",15), new Token(6,9,";",4), new Token(7,3,"int",12)
                		 , new Token(7,7,"eb",15), new Token(7,9,";",4), new Token(8,3,"int",12), new Token(8,7,"ec",15)
                		 , new Token(8,9,";",4), new Token(9,3,"int",12), new Token(9,7,"ed",15), new Token(9,9,";",4)
                		 , new Token(25,3,"da",15), new Token(25,6,"=",6), new Token(25,8,"2",14), new Token(25,10,"+",3)
                		 , new Token(25,12,"1",14), new Token(25,13,";",4), new Token(29,3,"db",15), new Token(29,6,"=",6)
                		 , new Token(29,8,"2.0",16), new Token(29,12,"+",3), new Token(29,14,"1",14), new Token(29,15,";",4)
                		 , new Token(33,3,"dc",15), new Token(33,6,"=",6), new Token(33,8,"2",14), new Token(33,10,"+",3)
                		 , new Token(33,12,"1.0",16), new Token(33,15,";",4), new Token(37,3,"dd",15), new Token(37,6,"=",6)
                		 , new Token(37,8,"2.0",16), new Token(37,12,"+",3), new Token(37,14,"1.0",16), new Token(37,17,";",4)
                		 , new Token(41,3,"ea",15), new Token(41,6,"=",6), new Token(41,8,"3",14), new Token(41,10,"+",3)
                		 , new Token(41,12,"7",14), new Token(41,13,";",4), new Token(46,3,"double",11), new Token(46,10,"nuevaD",15)
                		 , new Token(46,16,";",4), new Token(48,3,"nuevaD",15), new Token(48,10,"=",6), new Token(48,12,"11.0",16)
                		 , new Token(48,16,";",4), new Token(50,3,"int",12), new Token(50,7,"nuevaI",15), new Token(50,13,";",4)
                		 , new Token(52,3,"nuevaI",15), new Token(52,10,"=",6), new Token(52,12,"12",14), new Token(52,14,";",4)
                		 , new Token(56,3,"double",11), new Token(56,10,"x",15), new Token(56,11,";",4), new Token(58,3,"x",15)
                		 , new Token(58,5,"=",6), new Token(58,7,"nuevaD",15), new Token(58,13,";",4), new Token(63,3,"nuevaD",15),
                		 new Token(63,10,"=",6), new Token(63,12,"x",15), new Token(63,14,"/",2), new Token(63,16,"10.0",16),
                		 new Token(63,20,";",4), new Token(64,1,"}",8)), 
                					 "","", 0 },
                 {"resources/fuentes/p06.txt", Arrays.asList(new Token(2,1,"main",13), new Token(2,5,"(",0), 
                		 new Token(2,6,")",1), new Token(3,1,"{",7), new Token(4,3,"int",12),new Token(4,7,"a",15),
                		 new Token(4,8,";",4), new Token(5,3,"double",11), new Token(5,10,"b",15),
                		 new Token(5,11,";",4), new Token(7,3,"a",15), new Token(7,5,"=",6), new Token(7,7,"7",14),
                		 new Token(7,8,";",4), new Token(8,3,"a",15), new Token(8,5,"=",6), new Token(8,7,"a",15),
                		 new Token(8,8,"/",2), new Token(8,9,"1",14), new Token(8,10,";",4), new Token(9,3,"b",15),
                		 new Token(9,5,"=",6), new Token(9,7,"7.5",16), new Token(9,10,";",4), new Token(10,3,"b",15),
                		 new Token(10,5,"=",6), new Token(10,7,"1.0",16), new Token(10,10,"*",2), new Token(10,11,"b",15),
                		 new Token(10,12,";",4), new Token(11,3,"b",15), new Token(11,5,"=",6), new Token(11,7,"a",15),
                		 new Token(11,8,"+",3), new Token(11,9,"b",15), new Token(11,10,";",4), new Token(12,3,"b",15),
                		 new Token(12,5,"=",6), new Token(12,7,"a",15), new Token(12,8,"+",3), new Token(12,9,"7.5",16),
                		 new Token(12,12,"/",2), new Token(12,13,"7.5",16), new Token(12,16,"*",2), new Token(12,17,"a",15),
                		 new Token(12,18,";",4), new Token(13,1,"}",8)), 
                						 "","", 0 },
                 {"resources/fuentes/p07.txt", Arrays.asList(new Token(1,1,"main",13), new Token(1,5,"(",0), 
                		 new Token(1,6,")",1), new Token(2,1,"{",7), new Token(3,3,"double",11),new Token(3,10,"a",15),
                		 new Token(3,11,";",4), new Token(5,3,"a",15), new Token(5,4,"=",6), new Token(5,5,"7",14)), 
                							 "","Error lexico (5,6): caracter '.' incorrecto", -1 },
                 {"resources/fuentes/p08.txt", Arrays.asList(new Token(1,1,"main",13), new Token(1,5,"(",0), 
                		 new Token(1,6,")",1), new Token(2,1,"{",7), new Token(3,3,"double",11),new Token(3,10,"a",15),
                		 new Token(3,11,";",4), new Token(5,3,"a",15), new Token(5,5,"=",6), new Token(5,7,"7655",14)), 
                								 "","Error lexico (5,11): caracter ':' incorrecto", -1 },
                 {"resources/fuentes/p09.txt", Arrays.asList(new Token(1,1,"main",13), new Token(1,5,"(",0), 
                		 new Token(1,6,")",1)), 
                				 "","Error lexico: fin de fichero inesperado", -1 },
                 {"resources/fuentes/p10.txt", Arrays.asList(new Token(1,1,"main",13), new Token(1,6,"main",13),
                		 new Token(1,10,"(",0), new Token(1,11,")",1)), 
                					 "","", 0 },
                 //Pruebas propias
                 {"resources/fuentes/felixem/comentarios01.txt", Arrays.asList(new Token(11,5,"main",13), new Token(11,9,"(",0),
                		 new Token(11,10,")",1), new Token(12,1,"{",7),  new Token(13,1,"}",8)), 
                					 "","", 0 },
                 {"resources/fuentes/pacocr/main01.txt", Arrays.asList(new Token(1,1,"ma",15), new Token(2,1,"in",15), new Token(2,3,"(",0), 
                                 new Token(2,4,")",1), new Token(3,1,"{",7), new Token(4,1,"}",8)),"","",0},
                 
                 {"resources/fuentes/pacocr/main02.txt",Arrays.asList(new Token(1,1, "main",13)),"","",0},
           });
    }

    //Atributos del test
    private String fichero;
    private List<Token> tokensEsperados;
    private String salidaEsperada;
    private String errorEsperado;
    private int codigoErrorEsperado;
    
    //Constructor por parámetros
	public TestsAnalizadorLexico(String fichero, List<Token> tokensEsperados, String salidaEsperada, String errorEsperado, int codigoErrorEsperado) {
		super();
		this.fichero = fichero;
		this.tokensEsperados = tokensEsperados;
		this.salidaEsperada = salidaEsperada;
		this.errorEsperado = errorEsperado;
		this.codigoErrorEsperado = codigoErrorEsperado;
	}
	
	//Comparar dos listas de tokens
	public void compareTokensList(List<Token> expectedTokens, List<Token> resultTokens)
	{
		Assert.assertEquals("Cantidad de tokens", expectedTokens.size(), resultTokens.size());
		//Comparar token a token
		for(int i=0; i<expectedTokens.size(); ++i)
		{
			Token expected = expectedTokens.get(i);
			Token result = resultTokens.get(i);
			//Comparar atributos
			Assert.assertEquals("Token "+i+" Fila",expected.fila, result.fila);
			Assert.assertEquals("Token "+i+" Columna",expected.columna, result.columna);
			Assert.assertEquals("Token "+i+" Lexema",expected.lexema, result.lexema);
			Assert.assertEquals("Token "+i+" Tipo",expected.tipo, result.tipo);
		}
	}

    //Tests lexicos del profesor
	@Test
	public void test() 
	{
        AnalizadorLexico al;
        Token t;

        RandomAccessFile entrada = null;
        List<Token> tokensExtraidos = new ArrayList<Token>();

        try {
        	//Abrir archivo de entrada
            entrada = new RandomAccessFile(fichero,"r");
            al = new AnalizadorLexico(entrada);

            while ((t=al.siguienteToken()).tipo != Token.EOF) 
            {
            	//Añadir tokens extraidos
            	tokensExtraidos.add(t);
            }
        }
        catch (FileNotFoundException e) {
          Assert.fail("Error, fichero no encontrado: " + fichero);
        }
        catch(ExitException e)
        {
            //Comparar salida esperada y error esperado
        	Assert.assertEquals("Código de error esperado", codigoErrorEsperado, e.status);
            //Comparar salida esperada y error esperado
            this.compareTokensList(this.tokensEsperados, tokensExtraidos);
            Assert.assertEquals("Salida esperada",salidaEsperada, outContent.toString());
            Assert.assertEquals("Error esperado",errorEsperado, errContent.toString());
            return;
        }

        //Comprobar si se esperaba error
        if(codigoErrorEsperado != 0)
        	Assert.fail("Se esperaba que el programa saliera con código de error "+codigoErrorEsperado);
        
        //Comparar salida esperada y error esperado
        this.compareTokensList(this.tokensEsperados, tokensExtraidos);
        Assert.assertEquals("Salida esperada",salidaEsperada, outContent.toString());
        Assert.assertEquals("Error esperado",errorEsperado, errContent.toString());
	}

}
