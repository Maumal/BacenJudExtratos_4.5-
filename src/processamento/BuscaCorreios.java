/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package processamento;

import BB_SISBB.SISBB;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import org.joda.time.LocalDate;

/**
 * @author f7057419 BUSCA OS CORREIOS E GERA UMA LISTA COM STRINGS DOS CORREIOS
 * QUE SERÃO TRATADOS EM OUTRO MÓDULO
 */
public class BuscaCorreios {

    @SuppressWarnings("empty-statement")    
    
    public ArrayList Sisbb(String txtChave, String txtSenha, ArrayList listaCorreios) throws Exception {

        SISBB p = new SISBB("p");
        String cel = "";
        ArrayList<String> lista = new ArrayList<>();
        //  BufferedWriter out = new BufferedWriter(new FileWriter("C:\\temp\\saida.txt"));

        // ABRE EMULADOR
        p.open();
        p.connect();
        p.waitFor("SISTEMA DE INFORMACOES BANCO DO BRASIL");
        p.enter();
        p.getVar().WaitForInput();
        // LOGIN
        p.waitFor("Código de Usuário:");
        p.set(txtChave, 13, 21);
        p.set(txtSenha, 14, 21);
        p.opcaoLogin(1);    //OPÇÃO PARA ENTRAR DIRETO NO CIC

        //ESCOLHER APLICATIVO
        p.waitFor("Navegador de Aplicativos");
        p.set("correio", 15, 14);
        p.set(txtSenha, 16, 14);
        p.enter();
        p.getVar().WaitForInput();
        p.waitFor("F5");
        //VERIFICA TELA DE AVISO
        /*while (!p.get(1, 10, 1).equals("0")) {        
         p.f3(); 
         //p.getVar().WaitForInput();
         }*/
        if (p.get(6, 21, 7).equals("Existem")) {
            p.f3();
            //p.getVar().WaitForInput();
        }
        p.waitFor("COEPC001");
        p.set("01", 21, 17);
        p.enter();
        p.getVar().WaitForInput();

        p.waitFor("COEM0015");
        p.set("21", 21, 18);
        p.enter();
        p.getVar().WaitForInput();

        p.waitFor("COEM1200");
        p.set("05", 14, 24);
        //p.set("sjoficio6", 17, 24);         
        //p.set("SJbacenimp", 17, 24); 
     //   p.set("01", 20, 24);
     //   p.set("1903", 20, 34);
       
     //   p.set("SJARQOFIC", 17, 24); 
      p.set("SJbacenext", 17, 24); 
        
        
        p.enter();
        p.getVar().WaitForInput();
        LocalDate dataDoCorreio = new LocalDate();
        LocalDate UmMesAtras = dataDoCorreio.plusMonths(-1);
      //  LocalDate UmMesAtras = dataDoCorreio.plusDays(-3);       
        //System.out.println(dataDoCorreio); 
        //System.out.println(UmMesAtras);
        //int dia=UmMesAtras.getDayOfMonth();        
        String dia = UmMesAtras.toString().substring(8, 10);
        //int mes=UmMesAtras.getMonthOfYear();
        String mes = UmMesAtras.toString().substring(5, 7);
        //int ano=UmMesAtras.getYearOfCentury();
        String ano = UmMesAtras.toString().substring(0, 4);
        //System.out.println(dia + " + " + mes + " + " + ano);    
        p.set("", 04, 39);
        p.set(dia, 04, 39);
        p.set(mes, 04, 41);
        p.set(ano, 04, 43);
       //JOptionPane.showMessageDialog(null, "fazer acerto quando cliente politicamente exposto---lista vazia");
        p.enter();
        p.getVar().WaitForInput();
        p.waitFor("COEM1260");
        do {
            if ("".equals(p.get(9, 3, 4))) {
                JOptionPane.showMessageDialog(null, "Não existem mensagens para serem carregadas. ");
                p.dispose();
                System.exit(0);
            }
            String i = p.get(9, 3, 4);
            int ii;
            int i2 = 8;
            ii = Integer.parseInt(i);
            //System.out.println(i);
            for (int j = ii; j < ii + 12; j++) {
                int linha = 0;
                String textLinha;
                String linha5, linha6, linha7, linha8, linha9, linha10, linha11, linha12, linha13, linha14, linha15, linha16, linha17, linha18, linha19, linha20, linha21, linha22;
                String textoCorreio = "";
                i2++;
                //System.out.println(j);                
                if ((p.get(i2, 38, 7).equals("Extrato"))) {
                    p.set(String.valueOf(j), 21, 28);
                    p.enter();
                    p.getVar().WaitForInput();
                    if (p.get(23, 5, 8).equals("registro")) {
                        //j = j++;
                        //p.set(String.valueOf(j), 21, 28);
                        //p.enter();
                        //p.getVar().WaitForInput();                   
                    } else {
                        p.getVar().WaitForInput();
                        p.waitFor("COEM1266");
                        do {
                            //Entrou na mensagem
                            linha5 = (p.get(5, 6, 74));
                            linha6 = (p.get(6, 6, 74));
                            linha7 = (p.get(7, 6, 74));
                            linha8 = (p.get(8, 6, 74));
                            linha9 = (p.get(9, 6, 74));
                            linha10 = (p.get(10, 6, 74));
                            linha11 = (p.get(11, 6, 74));
                            linha12 = (p.get(12, 6, 74));
                            linha13 = (p.get(13, 6, 74));
                            linha14 = (p.get(14, 6, 74));
                            linha15 = (p.get(15, 6, 74));
                            linha16 = (p.get(16, 6, 74));
                            linha17 = (p.get(17, 6, 74));
                            linha18 = (p.get(18, 6, 74));
                            linha19 = (p.get(19, 6, 74));
                            linha20 = (p.get(20, 6, 74));
                            linha21 = (p.get(21, 6, 74));
                            linha22 = (p.get(22, 6, 74));

                            textoCorreio = textoCorreio + "\n" + linha5 + "\n" + linha6 + "\n" + linha7 + "\n" + linha8 + "\n" + linha9 + "\n" + linha10 + "\n" + linha11 + "\n" + linha12 + "\n" + linha13 + "\n" + linha14 + "\n" + linha15 + "\n" + linha16 + "\n" + linha17 + "\n" + linha18 + "\n" + linha19 + "\n" + linha20 + "\n" + linha21 + "\n" + linha22;
                            textoCorreio = textoCorreio.replace("?", "");//.replaceAll("/", "");                               

                            if (!p.get(23, 4, 6).equals("Última")) {
                                p.f8();
                                p.getVar().WaitForInput();
                            }
                        } while (!p.get(23, 4, 6).equals("Última"));
                        //JOptionPane.showMessageDialog(null,textoCorreio );
                        System.out.println(textoCorreio);
                        listaCorreios.add(textoCorreio);
                        p.f3();
                        p.getVar().WaitForInput();
                        // System.out.println(listaCorreios.size());             
                    }
                }
                //  System.out.println("ssssssss");
            }
            p.f8();
            p.getVar().WaitForInput();


        } while (!p.get(23, 3, 6).equals("Última"));
   //  } while (p.get(23, 3, 6).equals("Última"));           
        //teste copiar só as mensagens da primeira página    
        //System.out.println("Chegou na Última tela");
        //System.out.println(listaCorreios.size());
        p.dispose();
        return listaCorreios;
    }

    // p.f5();
    // p.f5();
    /*// FINALIZA EMULADOR
     p.f3  
     p.f5();
     p.f5();
     p.dispose();       
     JOptionPane.showMessageDialog(null,
     "Captura finalizada com sucesso!" + "\r\n");
     */   
} // FIM - método sisbb
