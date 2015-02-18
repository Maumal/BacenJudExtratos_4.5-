/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processamento;

import BB_SISBB.SISBB;
import bacenjud.ContadoClientes;
import bacenjud.ContadoCorreio;
import bacenjud.Correio;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.joda.time.LocalDate;

/**
 *
 * @author f7057419 Recebe a Lista de strings de correios recorta os dados, cria
 * o correioetos correios e cria uma listaObjetoCorreio de correioetos que será
 * retornada
 */
public class TrataCorreios {

    @SuppressWarnings("empty-statement")
    public List<Correio> BuscaContasNoCliente(String txtChave, String txtSenha, ArrayList listaCorreio) throws InterruptedException, FileNotFoundException {
//      FileOutputStream arquivorede2 = null;
//      arquivorede2 = new FileOutputStream("c:/temp/teste1/Tipos de produtos xx10.txt");
//      PrintStream printStreamRede2 = new PrintStream(arquivorede2);

        ArrayList<Correio> listaCorreioClientes = new ArrayList<>();
        SISBB p = new SISBB("q");

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
        p.opcaoLogin(3);    //OPÇÃO PARA ENTRAR DIRETO NO CIC

        //ESCOLHER APLICATIVO
        p.waitFor("Navegador de Aplicativos");
        p.set("clientes", 15, 14);
        p.set(txtSenha, 16, 14);
        p.enter();
        p.getVar().WaitForInput();
        p.waitFor("SBBP6130");
        p.waitFor("Transação nao disponível");

        p.set("01", 21, 20);
        p.enter();
        p.getVar().WaitForInput();
        p.waitFor("MCIM0000");
        p.waitFor("Grupos Empresariais");

        p.set("01", 19, 18);
        p.enter();
        p.getVar().WaitForInput();
        p.waitFor("MCIM001A");
        p.waitFor("radical do CNPJ");

        String cpfCnpj;
        int tamanhoCpfCnpj;
        String tratamentoEspecial;

        int xc;
        xc = listaCorreio.size();

        for (Iterator iterator = listaCorreio.iterator(); iterator.hasNext();) {
            Correio correio = (Correio) iterator.next();

            System.out.println(correio.getDataInicio());
            System.out.println(correio.getDataFim());

            String dataInicioCorreioS;
            dataInicioCorreioS = correio.getDataInicio().replaceAll("/", "");
            dataInicioCorreioS = dataInicioCorreioS.substring(4, 8) + "-" + dataInicioCorreioS.substring(2, 4) + "-" + dataInicioCorreioS.substring(0, 2);

            String dataFimCorreioS;
            dataFimCorreioS = correio.getDataFim().replaceAll("/", "");
            dataFimCorreioS = dataFimCorreioS.substring(4, 8) + "-" + dataFimCorreioS.substring(2, 4) + "-" + dataFimCorreioS.substring(0, 2);

            LocalDate DataInicialCorreio = new LocalDate(dataInicioCorreioS); //Periodo do extrato
            LocalDate DataFinalCorreio = new LocalDate(dataFimCorreioS); //Periodo do extrato

            // Correio correio = new Correio();
            ArrayList<ContadoClientes> listaContaClientes = new ArrayList<>();

            tratamentoEspecial = "N";

            xc--;

            p.waitFor("MCIM001A");
            p.waitFor("radical do CNPJ");
            cpfCnpj = correio.getCpfCpj().trim();
            tamanhoCpfCnpj = cpfCnpj.length();

            System.out.println(xc);

            if (tamanhoCpfCnpj > 11) {//CNPJ                
                p.set("              ", 12, 37);
                p.set(cpfCnpj, 12, 37);
                p.enter();
                p.getVar().WaitForInput();
                
                  if (p.get(23, 3, 8).equals("Cadastro")) {
                    correio.setTratamentoEspecial("Cliente Politicamente Exposto.");
                    tratamentoEspecial = "S";
                    p.set("              ", 12, 37);
                } else if (p.get(1, 3, 8).equals("MCIM0002")) {
                    p.set("x", 6, 3);
                    p.enter();
                    p.getVar().WaitForInput();
                }
                
                if ("N".equals(tratamentoEspecial)) {
                      p.waitFor("F5");
                p.set("43", 21, 41);
                p.enter();
                p.getVar().WaitForInput();
                if (!p.get(9, 15, 4).equals("Moda")) {
                    p.f3();
                    p.getVar().WaitForInput();
                }
                }
                
                
                
              
                
                
                
                
            } else {//CPF                
                p.set("           ", 12, 13);
                p.set(cpfCnpj, 12, 13);
                p.enter();
                p.getVar().WaitForInput();
                if (p.get(23, 3, 8).equals("Cadastro")) {
                    correio.setTratamentoEspecial("Cliente Politicamente Exposto.");
                    tratamentoEspecial = "S";
                    p.set("           ", 12, 13);
                } else if (p.get(1, 3, 8).equals("MCIM0002")) {
                    p.set("x", 6, 3);
                    p.enter();
                    p.getVar().WaitForInput();
                }
                
                if ("N".equals(tratamentoEspecial)) {
                    p.waitFor("MCIM100F");
                    p.waitFor("do Cadastro");
                    p.set("17", 21, 41);
                    p.enter();
                    p.getVar().WaitForInput();
                    if (!p.get(9, 15, 4).equals("Moda")) {
                        p.f3();
                        p.getVar().WaitForInput();
                    }
                }
                
                
                
            }

            if ("N".equals(tratamentoEspecial)) {
                p.waitFor("Consulta");
                p.waitFor("Mostrar Não ativas");
                do {//Operações ativas                     
                    for (int j = 11; j < 21; j++) {
                        if (!p.get(j, 4, 1).equals("")) {
                            ContadoClientes contaClientes = new ContadoClientes();
                            contaClientes.setNumeroOperacao(p.get(j, 28, 20));
                            contaClientes.setTitular(p.get(j, 48, 5));
                            contaClientes.setAgencia(p.get(j, 57, 7));
                            contaClientes.setSituação(p.get(j, 65, 15));
                            int x;
                            x = j - 10;
                            String y;
                            y = Integer.toString(x);
                            //  System.out.println("Linha  " + y);
                            p.set(y, 21, 30);
                            p.enter();
                            p.getVar().WaitForInput();
                            while (p.get(3, 15, 7).equals("NENHUMA")) {
                                p.f3();
                                p.getVar().WaitForInput();//aguardar
                            }
                            p.waitFor("Consulta");
                            contaClientes.setProduto(p.get(7, 16, 35));
                            contaClientes.setModalidade(p.get(8, 17, 35));
                            contaClientes.setDataInicial(p.get(9, 64, 10).replaceAll("\\.", "/"));
                            contaClientes.setDataFinal(p.get(10, 64, 10).replaceAll("\\.", "/"));

                            //TRATAMENTO TIPO DE PERIODICIDADE DAS DATAS 
                            String dataInicioCorreioC;
                            dataInicioCorreioC = contaClientes.getDataInicial().replaceAll("/", "");
                            dataInicioCorreioC = dataInicioCorreioC.substring(4, 8) + "-" + dataInicioCorreioC.substring(2, 4) + "-" + dataInicioCorreioC.substring(0, 2);

                            String dataFimCorreioC;
                            dataFimCorreioC = contaClientes.getDataFinal().replaceAll("/", "");
                            dataFimCorreioC = dataFimCorreioC.substring(4, 8) + "-" + dataFimCorreioC.substring(2, 4) + "-" + dataFimCorreioC.substring(0, 2);

                            LocalDate DataInicialClientes = new LocalDate(dataInicioCorreioC);
                            LocalDate DataFinalClientes = new LocalDate(dataFimCorreioC);
                   
                            System.out.println("XXXXXXXXXX " + correio.getNumeroDoCorreio());
                            System.out.println("Inicio da conta: " + DataInicialClientes);
                            System.out.println("Fim da conta: " + DataFinalClientes);

                            System.out.println("Inicio do correio: " + DataInicialCorreio);
                            System.out.println("Fim do correio: " + DataFinalCorreio);
                            

                            if (DataInicialCorreio.isBefore(DataInicialClientes) && (DataFinalCorreio.isBefore(DataInicialClientes))) {
                                //                       |____________| data da conta
                                //           |_____|                            datas solicitadas   
                                System.out.println("0");
                                contaClientes.setDataInicioPesquisa("0000-00-00");//Não vai ter extrato, data solicitado fora da data da conta
                                contaClientes.setDataFimPesquisa("0000-00-00");
                                
                            } else if (DataInicialCorreio.isAfter(DataFinalClientes) && (DataFinalCorreio.isAfter(DataFinalClientes))) {
                                //               |____________| data da conta
                                //                                    |_____|    datas solicitadas                             
                                System.out.println("1");
                                contaClientes.setDataInicioPesquisa("0000-00-00");//Não vai ter extrato, data solicitado fora da data da conta
                                contaClientes.setDataFimPesquisa("0000-00-00");
                            } else if (DataInicialCorreio.isBefore(DataInicialClientes) && (DataFinalCorreio.isEqual(DataInicialClientes))) {
                                //                   |____________| data da conta
                                //             |_____|                     datas solicitadas                                               
                                System.out.println("2");
                                contaClientes.setDataInicioPesquisa("0000-00-00");//Não vai ter extrato, data solicitado fora da data da conta
                                contaClientes.setDataFimPesquisa("0000-00-00");
                            } else if (DataInicialCorreio.isBefore(DataInicialClientes) && (DataFinalCorreio.isBefore(DataFinalClientes))) {
                                //                |____________| data da conta
                                //             |_____|              datas solicitadas                                              
                                System.out.println("3");
                                contaClientes.setDataInicioPesquisa(DataInicialClientes.toString());
                                contaClientes.setDataFimPesquisa(DataFinalCorreio.toString());                      
                            } else if (DataInicialCorreio.isAfter(DataInicialClientes) && (DataFinalCorreio.isBefore(DataFinalClientes))) {
                                //              |____________| data da conta
                                //                |_____|    datas solicitadas                                              
                                System.out.println("4");
                                contaClientes.setDataInicioPesquisa(DataInicialCorreio.toString());
                                contaClientes.setDataFimPesquisa(DataFinalCorreio.toString()); 
                            } else if (DataInicialCorreio.isAfter(DataInicialClientes) && (DataFinalCorreio.isAfter(DataFinalClientes))) {
                                //                   |____________| data da conta
                                //                              |_____|    datas solicitadas                                              
                                System.out.println("5");
                                contaClientes.setDataInicioPesquisa(DataInicialCorreio.toString());
                                contaClientes.setDataFimPesquisa(DataFinalClientes.toString()); 
                            } else if (DataInicialCorreio.isEqual(DataInicialClientes) && (DataFinalCorreio.isAfter(DataFinalClientes))) {
                                //                 |____________| data da conta
                                //                              |_____|    datas solicitadas  
                                contaClientes.setDataInicioPesquisa("0000-00-00");//Não vai ter extrato, data solicitado fora da data da conta
                                contaClientes.setDataFimPesquisa("0000-00-00");
                                System.out.println("6");
                            } else if (DataInicialCorreio.isEqual(DataInicialClientes) && (DataFinalCorreio.isEqual(DataFinalClientes))) {
                                //                |________________| data da conta
                                //                |________________|    datas solicitadas                                              
                                System.out.println("7");
                                contaClientes.setDataInicioPesquisa(DataInicialClientes.toString());
                                contaClientes.setDataFimPesquisa(DataFinalCorreio.toString()); 
                            } else if (DataInicialCorreio.isBefore(DataInicialClientes) && (DataFinalCorreio.isAfter(DataFinalClientes))) {
                                //                   |____________| data da conta
                                //                 |________________|    datas solicitadas                                              
                                System.out.println("8");
                                contaClientes.setDataInicioPesquisa(DataInicialClientes.toString());
                                contaClientes.setDataFimPesquisa(DataFinalClientes.toString()); 
                            }
                            //FIM TRATAMENTO TIPO DE PERIODICIDADE DAS DATAS 
                            
                           
                            if (contaClientes.getProduto().contains("SMS")) {
                                // ' listaContaClientes.add(contaClientes);  
                            } else if (contaClientes.getProduto().contains("SEGURO")) {
                                // 'listaContaClientes.add(contaClientes);  
                            } else if (contaClientes.getProduto().contains("CAPITALIZACAO")) {
                                // 'listaContaClientes.add(contaClientes);  
                            } else if (contaClientes.getProduto().contains("CARTAO")) {
                                // 'listaContaClientes.add(contaClientes);
                            } else if (contaClientes.getProduto().contains("COBRANCA")) {
                                // 'listaContaClientes.add(contaClientes);
                            } else if (contaClientes.getProduto().contains("PRESTACAO")) {
                                // 'listaContaClientes.add(contaClientes);
                            } else if (contaClientes.getProduto().contains("LEASING")) {
                                // 'listaContaClientes.add(contaClientes);
                            } else if (contaClientes.getProduto().contains("CDC")) {
                                // 'listaContaClientes.add(contaClientes);
                            } else if (contaClientes.getProduto().contains("EMPRESTIMO")) {
                                // 'listaContaClientes.add(contaClientes); } else if (contaClientes.getProduto().toString().contains("CARTAO")) {
                                // 'listaContaClientes.add(contaClientes);
                            } else if (contaClientes.getProduto().contains("TITULOS")) {
                                // 'listaContaClientes.add(contaClientes);
                            } else if (contaClientes.getProduto().contains("DESCONTO")) {
                                // 'listaContaClientes.add(contaClientes);
                            } else if (contaClientes.getProduto().contains("FINANCIAMENTO")) {
                            } else if (contaClientes.getProduto().contains("FINAME ")) {
                            } else if (contaClientes.getProduto().contains("GIRO")) {
                            } else if (contaClientes.getProduto().contains("REESCALONAMENTO")) {
                            } else if (contaClientes.getProduto().contains("ADMINISTRACAO")) {
                            } else if (contaClientes.getProduto().contains("REESTRUTURACAO ")) {
                            } else if (contaClientes.getProduto().contains("ESTOQUE ")) {
                            } else if (contaClientes.getProduto().contains("BENEFICIO")) {
                            } else if (contaClientes.getProduto().contains("CONSORCIO")) {
                            } else if (contaClientes.getProduto().contains("FINANCIAMENTO")) {
                            } else if (contaClientes.getProduto().contains("SEGURO")) {
                            } else if (contaClientes.getProduto().contains("PREVIDENCIA")) {
                            } else if (contaClientes.getProduto().contains("PRONAMP")) {
                            } else if (contaClientes.getProduto().contains("PRONAF")) {
                            } else if (contaClientes.getProduto().contains("CHEQUE")) {
                            } else if (contaClientes.getProduto().contains("SECURITIZACAO")) {
                            } else if (contaClientes.getProduto().contains("FINAME ")) {
                            } else if (contaClientes.getProduto().contains("FUNCAFE ")) {
                            } else if (contaClientes.getProduto().contains("ADIANTAMENTO")) {
                            } else if (contaClientes.getProduto().contains("OUROCAP")) {
                                // 'listaContaClientes.add(contaClientes);
                            } else {
                                listaContaClientes.add(contaClientes);
                            }
//                                  printStreamRede2.println("Produto: "+contaClientes.getProduto()+" ;   Modalidade: "+contaClientes.getModalidade()); 
                            while (!p.get(1, 3, 8).equals("OPRM6498")) {
                                p.f3();
                                p.getVar().WaitForInput();
                            }
                        }
                    }
                    p.f8();
                    p.getVar().WaitForInput();
                } while (!p.get(23, 3, 6).equals("Última"));

                p.f11();
                p.getVar().WaitForInput();

                do {//Operações inativas                        
                    for (int j = 11; j < 21; j++) {
                        if (!p.get(j, 4, 1).equals("")) {
                            ContadoClientes contaClientes = new ContadoClientes();
                            contaClientes.setNumeroOperacao(p.get(j, 28, 20));
                            contaClientes.setTitular(p.get(j, 48, 5));
                            contaClientes.setAgencia(p.get(j, 57, 7));
                            contaClientes.setSituação(p.get(j, 65, 15));

                            int x;
                            x = j - 10;
                            String y;
                            y = Integer.toString(x);
                            //System.out.println("Linha  " + y);
                            p.set(y, 21, 30);
                            p.enter();
                            p.getVar().WaitForInput();
                            while (p.get(3, 15, 7).equals("NENHUMA")) {
                                p.f3();
                                p.getVar().WaitForInput();
                            }
                            p.waitFor("Consulta");
                            contaClientes.setProduto(p.get(7, 16, 35));
                            contaClientes.setModalidade(p.get(8, 16, 35));
                            contaClientes.setDataInicial(p.get(9, 64, 10).replaceAll("\\.", "/"));
                            contaClientes.setDataFinal(p.get(10, 64, 10).replaceAll("\\.", "/"));
                            
                            //TRATAMENTO TIPO DE PERIODICIDADE DAS DATAS 
                            String dataInicioCorreioC;
                            dataInicioCorreioC = contaClientes.getDataInicial().replaceAll("/", "");
                            dataInicioCorreioC = dataInicioCorreioC.substring(4, 8) + "-" + dataInicioCorreioC.substring(2, 4) + "-" + dataInicioCorreioC.substring(0, 2);

                            String dataFimCorreioC;
                            dataFimCorreioC = contaClientes.getDataFinal().replaceAll("/", "");
                            dataFimCorreioC = dataFimCorreioC.substring(4, 8) + "-" + dataFimCorreioC.substring(2, 4) + "-" + dataFimCorreioC.substring(0, 2);

                            LocalDate DataInicialClientes = new LocalDate(dataInicioCorreioC);
                            LocalDate DataFinalClientes = new LocalDate(dataFimCorreioC);
                            
                            System.out.println("XXXXXXXXXX " + correio.getNumeroDoCorreio());
                            System.out.println("Inicio da conta: " + DataInicialClientes);
                            System.out.println("Fim da conta: " + DataFinalClientes);

                            System.out.println("Inicio do correio: " + DataInicialCorreio);
                            System.out.println("Fim do correio: " + DataFinalCorreio);

                            if (DataInicialCorreio.isBefore(DataInicialClientes) && (DataFinalCorreio.isBefore(DataInicialClientes))) {
                                //                       |____________| data da conta
                                //           |_____|                            datas solicitadas   
                                System.out.println("0");
                                contaClientes.setDataInicioPesquisa("0000-00-00");//Não vai ter extrato, data solicitado fora da data da conta
                                contaClientes.setDataFimPesquisa("0000-00-00");
                                
                            } else if (DataInicialCorreio.isAfter(DataFinalClientes) && (DataFinalCorreio.isAfter(DataFinalClientes))) {
                                //               |____________| data da conta
                                //                                    |_____|    datas solicitadas                             
                                System.out.println("1");
                                contaClientes.setDataInicioPesquisa("0000-00-00");//Não vai ter extrato, data solicitado fora da data da conta
                                contaClientes.setDataFimPesquisa("0000-00-00");
                            } else if (DataInicialCorreio.isBefore(DataInicialClientes) && (DataFinalCorreio.isEqual(DataInicialClientes))) {
                                //                   |____________| data da conta
                                //             |_____|                     datas solicitadas                                               
                                System.out.println("2");
                                contaClientes.setDataInicioPesquisa("0000-00-00");//Não vai ter extrato, data solicitado fora da data da conta
                                contaClientes.setDataFimPesquisa("0000-00-00");
                            } else if (DataInicialCorreio.isBefore(DataInicialClientes) && (DataFinalCorreio.isBefore(DataFinalClientes))) {
                                //                |____________| data da conta
                                //             |_____|              datas solicitadas                                              
                                System.out.println("3");
                                contaClientes.setDataInicioPesquisa(DataInicialClientes.toString());
                                contaClientes.setDataFimPesquisa(DataFinalCorreio.toString());                      
                            } else if (DataInicialCorreio.isAfter(DataInicialClientes) && (DataFinalCorreio.isBefore(DataFinalClientes))) {
                                //              |____________| data da conta
                                //                |_____|    datas solicitadas                                              
                                System.out.println("4");
                                contaClientes.setDataInicioPesquisa(DataInicialCorreio.toString());
                                contaClientes.setDataFimPesquisa(DataFinalCorreio.toString()); 
                            } else if (DataInicialCorreio.isAfter(DataInicialClientes) && (DataFinalCorreio.isAfter(DataFinalClientes))) {
                                //                   |____________| data da conta
                                //                              |_____|    datas solicitadas                                              
                                System.out.println("5");
                                contaClientes.setDataInicioPesquisa(DataInicialCorreio.toString());
                                contaClientes.setDataFimPesquisa(DataFinalClientes.toString()); 
                            } else if (DataInicialCorreio.isEqual(DataInicialClientes) && (DataFinalCorreio.isAfter(DataFinalClientes))) {
                                //                 |____________| data da conta
                                //                              |_____|    datas solicitadas  
                                contaClientes.setDataInicioPesquisa("0000-00-00");//Não vai ter extrato, data solicitado fora da data da conta
                                contaClientes.setDataFimPesquisa("0000-00-00");
                                System.out.println("6");
                            } else if (DataInicialCorreio.isEqual(DataInicialClientes) && (DataFinalCorreio.isEqual(DataFinalClientes))) {
                                //                |________________| data da conta
                                //                |________________|    datas solicitadas                                              
                                System.out.println("7");
                                contaClientes.setDataInicioPesquisa(DataInicialClientes.toString());
                                contaClientes.setDataFimPesquisa(DataFinalCorreio.toString()); 
                            } else if (DataInicialCorreio.isBefore(DataInicialClientes) && (DataFinalCorreio.isAfter(DataFinalClientes))) {
                                //                   |____________| data da conta
                                //                 |________________|    datas solicitadas                                              
                                System.out.println("8");
                                contaClientes.setDataInicioPesquisa(DataInicialClientes.toString());
                                contaClientes.setDataFimPesquisa(DataFinalClientes.toString()); 
                            }
                            //FIM TRATAMENTO TIPO DE PERIODICIDADE DAS DATAS 

                            //Operações que não serão gravadas
                            if (contaClientes.getProduto().contains("SMS")) {
                                // ' listaContaClientes.add(contaClientes);  
                            } else if (contaClientes.getProduto().contains("SEGURO")) {
                                // 'listaContaClientes.add(contaClientes);  
                            } else if (contaClientes.getProduto().contains("CAPITALIZACAO")) {
                                // 'listaContaClientes.add(contaClientes);  
                            } else if (contaClientes.getProduto().contains("CARTAO")) {
                                // 'listaContaClientes.add(contaClientes);
                            } else if (contaClientes.getProduto().contains("COBRANCA")) {
                                // 'listaContaClientes.add(contaClientes);
                            } else if (contaClientes.getProduto().contains("PRESTACAO")) {
                                // 'listaContaClientes.add(contaClientes);
                            } else if (contaClientes.getProduto().contains("LEASING")) {
                                // 'listaContaClientes.add(contaClientes);
                            } else if (contaClientes.getProduto().contains("CDC")) {
                                // 'listaContaClientes.add(contaClientes);
                            } else if (contaClientes.getProduto().contains("EMPRESTIMO")) {
                                // 'listaContaClientes.add(contaClientes); } else if (contaClientes.getProduto().toString().contains("CARTAO")) {
                                // 'listaContaClientes.add(contaClientes);
                            } else if (contaClientes.getProduto().contains("TITULOS")) {
                                // 'listaContaClientes.add(contaClientes);
                            } else if (contaClientes.getProduto().contains("DESCONTO")) {
                                // 'listaContaClientes.add(contaClientes);
                            } else if (contaClientes.getProduto().contains("FINANCIAMENTO")) {
                            } else if (contaClientes.getProduto().contains("FINAME ")) {
                            } else if (contaClientes.getProduto().contains("GIRO")) {
                            } else if (contaClientes.getProduto().contains("REESCALONAMENTO")) {
                            } else if (contaClientes.getProduto().contains("ADMINISTRACAO")) {
                            } else if (contaClientes.getProduto().contains("REESTRUTURACAO ")) {
                            } else if (contaClientes.getProduto().contains("CHEQUE")) {
                            } else if (contaClientes.getProduto().contains("ESTOQUE ")) {
                            } else if (contaClientes.getProduto().contains("BENEFICIO")) {
                            } else if (contaClientes.getProduto().contains("CONSORCIO")) {
                            } else if (contaClientes.getProduto().contains("FINANCIAMENTO")) {
                            } else if (contaClientes.getProduto().contains("SEGURO")) {
                            } else if (contaClientes.getProduto().contains("PREVIDENCIA")) {
                            } else if (contaClientes.getProduto().contains("PRONAMP")) {
                            } else if (contaClientes.getProduto().contains("PRONAF")) {
                            } else if (contaClientes.getProduto().contains("SECURITIZACAO")) {
                            } else if (contaClientes.getProduto().contains("FINAME ")) {
                            } else if (contaClientes.getProduto().contains("FUNCAFE ")) {
                            } else if (contaClientes.getProduto().contains("OUROCAP")) {
                            } else if (contaClientes.getProduto().contains("ADIANTAMENTO")) {

                            } else {
                                listaContaClientes.add(contaClientes);
                            }
                          
//                            }
                            while (!p.get(1, 3, 8).equals("OPRM6498")) {
                                p.f3();
                                p.getVar().WaitForInput();
                            }
                        }
                    }
                    p.f8();
                    p.getVar().WaitForInput();
                } while (!p.get(23, 3, 6).equals("Última"));

            } else {
                //  Cliente Politicamente exposto                          
                ContadoClientes contaClientes1 = new ContadoClientes();
                contaClientes1.setAgencia("Cliente Politicamente Exposto. ");
                contaClientes1.setDataFinal("Cliente Politicamente Exposto. ");
                contaClientes1.setDataInicial("Cliente Politicamente Exposto. ");
                contaClientes1.setModalidade("Cliente Politicamente Exposto. ");
                contaClientes1.setNumeroOperacao("Cliente Politicamente Exposto. ");
                contaClientes1.setProduto("Cliente Politicamente Exposto. ");
                contaClientes1.setSituação("Cliente Politicamente Exposto. ");
                contaClientes1.setTitular("Cliente Politicamente Exposto. ");
                listaContaClientes.add(contaClientes1);
                // correio.setContasClientes(listaContaClientes);                
            }

            correio.setContasClientes(listaContaClientes);

            while (!p.get(1, 3, 8).equals("MCIM001A")) {
                p.f3();
                p.getVar().WaitForInput();
                //  p.getVar().WaitForInput();                     
            }

            //  }
            // }
        }
        p.dispose();
        return listaCorreio;
    }

    //Passo a lista de correios"Strings" e retorno uma lista de Objetos "Correios"
    public List<Correio> CarregaDados(ArrayList<String> ListaCorreio) {

        List<Correio> listaObjetoCorreio = new ArrayList<>();

        //   System.out.println(ListaCorreio.size());
        for (String correio : ListaCorreio) {
            // System.out.println(correio);
            Correio correioObjeto = new Correio();

            //     System.out.println(correio);
            //pegar o número do correio          
            String c[] = correio.split("-BACEN JUD 2.0");
            String numeroCorreio = (c[0]).replaceAll("Número - Título:", "");
            // JOptionPane.showMessageDialog(null, numeroCorreio);
            correioObjeto.setNumeroDoCorreio(numeroCorreio);

            //pegar o número do protolo
            String B[] = correio.toUpperCase().split("PROTOCOLO..:");
            String Protocolo = (B[1]);
            String b[] = Protocolo.toUpperCase().split("\n");
            Protocolo = (b[0]);
            correioObjeto.setProtocolo(Protocolo);

            //pegar o número do SEQUENCIAL
            String C[] = correio.toUpperCase().split("SEQUENCIAL.:");
            String numeroSequencial = (C[1]);
            String cc[] = numeroSequencial.split("\n");
            numeroSequencial = (cc[0]);
            correioObjeto.setSequencial(numeroSequencial);

            //pegar o número do DATA
            String D[] = correio.toUpperCase().split("DATA.......:");
            String numeroData = (D[1].replaceAll("-", "/"));
            String d[] = numeroData.split("\n");
            numeroData = (d[0]);
            correioObjeto.setData(numeroData);

            //pegar o número do PROCESSO
            String E[] = correio.toUpperCase().split("PROCESSO...:");
            String processo = (E[1]);
            String e[] = processo.split("\n");
            processo = (e[0]);
            correioObjeto.setProcesso(processo);

            //pegar o número do RÉU
            String F[] = correio.toUpperCase().split("RÉU........:");
            String reu = (F[1]);
            reu = reu.replaceAll("/", "");
            String f[] = reu.split("\n");
            reu = (f[0]);
            correioObjeto.setReu(reu);

            //pegar o número do CPF/CNPJ
            String G[] = correio.toUpperCase().split("CPF/CNPJ...:");
            String CPF_CNPJ = (G[1]);
            String g[] = CPF_CNPJ.split("\n");
            CPF_CNPJ = (g[0]);
                              
            correioObjeto.setCpfCpj(CPF_CNPJ);

            //pegar o número das contas
            String H[] = correio.toUpperCase().split("AGÊNCIA/CONTA:");
            String contas = (H[1]);
            String I[] = contas.toUpperCase().split("EXTRATO:");
            contas = (I[0]);

            contas = contas.replaceAll("\\s+", "").replaceAll("-", "");

            int tamanhoContas;
            int vezesNoFor;
            tamanhoContas = contas.length();

            vezesNoFor = tamanhoContas / 16;

            ArrayList<ContadoCorreio> ContasCorreio = new ArrayList<>();
            String agencia;
            String conta;

            System.out.println(correioObjeto.getNumeroDoCorreio());

            for (int i = 0; i < vezesNoFor; i++) {
                String contasGravar = contas.substring(0, 16);
                String Cg[] = contasGravar.split("/");
                agencia = Cg[0];
                conta = Cg[1];

                ContadoCorreio contaCorreio = new ContadoCorreio();
                contaCorreio.setAgencia(agencia);
                contaCorreio.setConta(conta);
                ContasCorreio.add(contaCorreio);
                contas = contas.replaceAll(contasGravar, "");

            }

//System.out.println(correioObjeto.getNumeroDoCorreio());   
            correioObjeto.setContasCorreio(ContasCorreio);

//pegar a data inicio da pesquisa
            String J[] = correio.split("Data inicio.....:");
            String dataInicio = (J[1].trim());
            dataInicio = dataInicio.substring(0, 10);
            correioObjeto.setDataInicio(dataInicio.replaceAll("\\.", "/"));

            System.out.println(correioObjeto.getNumeroDoCorreio());

            //pegar a data fim da pesquisa
            String L[] = correio.split("fim:");
            String datafim = (L[1].trim());
            datafim = datafim.substring(0, 10);
            correioObjeto.setDataFim(datafim.replaceAll("\\.", "/"));

            System.out.println(correioObjeto.getNumeroDoCorreio());

            //pegar o Juiz
            String M[] = correio.split("Juiz solicitante:");
            String Juiz = (M[1].trim());
            String m[] = Juiz.toUpperCase().split("\n");
            Juiz = (m[0]);
            correioObjeto.setJuiz(Juiz);

            System.out.println(correioObjeto.getNumeroDoCorreio());

            //pegar o Tribunal
            String N[] = correio.split("Tribunal...:");
            String tribunal = (N[1]);
            String n[] = tribunal.toUpperCase().split("\n");
            tribunal = (n[0]);
            correioObjeto.setTribunal(tribunal);

            System.out.println(correioObjeto.getNumeroDoCorreio());

            //pegar o Vara
            String O[] = correio.split("Vara.......:");
            String vara = (O[1]);
            String o[] = vara.toUpperCase().split("\n");
            vara = (o[0]);
            correioObjeto.setVara(vara);

            System.out.println(correioObjeto.getNumeroDoCorreio());

            //pegar o Lougradouro
            String P[] = correio.split("Logradouro.:");
            String lougradouro = (P[1]);
            String p[] = lougradouro.toUpperCase().split("\n");
            lougradouro = (p[0]);
            correioObjeto.setLougradouro(lougradouro);

            System.out.println(correioObjeto.getNumeroDoCorreio());

            //pegar o Numero
            String Q[] = correio.split("Numero.....:");
            String numero = (Q[1]);
            String q[] = numero.toUpperCase().split("\n");
            numero = (q[0]);
            correioObjeto.setNumero(numero);

            System.out.println(correioObjeto.getNumeroDoCorreio());

            //pegar o Complemento
            String R[] = correio.split("Complemento:");
            String complemento = (R[1]);
            String r[] = complemento.toUpperCase().split("\n");
            complemento = (r[0]);
            correioObjeto.setComplemento(complemento);

            System.out.println(correioObjeto.getNumeroDoCorreio());

            //pegar o Bairro
            String S[] = correio.split("Bairro.....:");
            String bairro = (S[1]);
            String s[] = bairro.toUpperCase().split("\n");
            bairro = (s[0]);
            correioObjeto.setBairro(bairro);

            System.out.println(correioObjeto.getNumeroDoCorreio());

            //pegar o Cep
            String T[] = correio.split("CEP........:");
            String cep = (T[1]);
            String t[] = cep.toUpperCase().split("\n");
            cep = (t[0]);
            
         //    System.out.println(cep);
           // cep =cep.substring(1, 9);
           cep =cep.substring(1, 3)+"."+cep.substring(3, 6)+"-"+cep.substring(6, 9);
            
           // System.out.println(cep);
            
            
            correioObjeto.setCep(cep);

            System.out.println(correioObjeto.getNumeroDoCorreio());

            //pegar o Cidade
            String Ss[] = correio.split("Cidade.....:");
            String cidade = (Ss[1]);
            String ss[] = cidade.toUpperCase().split("\n");
            cidade = (ss[0]);
            correioObjeto.setCidade(cidade);

            System.out.println(correioObjeto.getNumeroDoCorreio());

            //pegar o UF
            String Tt[] = correio.split("UF.........:");
            String uf = (Tt[1]);
            String tt[] = uf.toUpperCase().split("\n");
            uf = (tt[0]);
            correioObjeto.setUf(uf);

            //  String j[] = dataInicio.split("\n");   
            //  dataInicio=(j[0]);  
            System.out.println(correioObjeto.getNumeroDoCorreio());

            listaObjetoCorreio.add(correioObjeto);
        }
        return listaObjetoCorreio;

    }

    public void TrocaDePasta(String txtChave, String txtSenha, ArrayList<String> ListaCorreio) throws SQLException, InterruptedException {
        SISBB p = new SISBB("O");

        //ABRE EMULADOR
        p.open();
        p.connect();
        p.waitFor("SISTEMA DE INFORMACOES BANCO DO BRASIL");
        p.enter();
        p.getVar().WaitForInput();

        // LOGIN
        p.waitFor("Código de Usuário:");
        p.set(txtChave, 13, 21);
        p.set(txtSenha, 14, 21);
        p.opcaoLogin(3);    //OPÇÃO PARA ENTRAR DIRETO NO CIC

        //ESCOLHER APLICATIVO
        p.waitFor("Navegador de Aplicativos");
        p.set("correio", 15, 14);
        p.set(txtSenha, 16, 14);
        p.enter();
        p.getVar().WaitForInput();

        p.waitFor("COEPC001");

        p.set("01", 21, 17);
        p.enter();
        p.getVar().WaitForInput();

        p.waitFor("COEM0015");

        p.set("21", 21, 18);
        p.enter();
        p.getVar().WaitForInput();

        for (String correio : ListaCorreio) {
            p.waitFor("COEM1200");

            System.out.println(correio);

            p.set("05", 14, 24);
            p.set(correio.substring(0, 4), 18, 24);
            p.set(correio.substring(5, 13), 18, 31);
            p.enter();
            p.getVar().WaitForInput();

            p.set("SJBACENIMP", 4, 13);
            p.enter();
            p.getVar().WaitForInput();
            p.set("ROTINA AUTOMATIZADA CONCLÚIDA PARA IMPRESSÃO.", 9, 6);
            p.enter();
            p.getVar().WaitForInput();
            p.waitFor("Confirma");
            p.set("s", 22, 50);
            p.enter();
            p.getVar().WaitForInput();

            while (!p.get(1, 3, 8).equals("COEM1200")) {
                p.f3();
                p.getVar().WaitForInput();
            }
        }
        p.close();
    }

}
