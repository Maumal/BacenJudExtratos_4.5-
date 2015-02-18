/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Telas;

import DAO.Conexao;
import bacenjud.ContadoClientes;
import bacenjud.ContadoCorreio;
import bacenjud.Correio;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import processamento.BuscaCorreios;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JOptionPane;
import org.joda.time.DateTime;
import processamento.TrataCorreios;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.joda.time.LocalDate;
import pdf.GeraOficio;
import pdf.Oficio;

/**
 *
 * @author f7057419
 */
public final class FrmExtatos extends javax.swing.JFrame {

    /**
     * Creates new form JFMPrincipal
     *
     * @throws java.lang.Exception
     */
    public FrmExtatos() throws Exception {
        initComponents();

        //Verifica a versão e não abre caso não seja a correta
        buscaVersao("BACENJUDExtratos");
        
        
        //Verifica se o usuário está rodando direto da "G"
        String local = System.getProperty("user.dir");
        local = local.substring(0, 1);
        if ("G".equals(local)) {
            JOptionPane.showMessageDialog(rootPane, "Favor copiar programa para a pasta temp. ");
            System.exit(0);
        }
        
        
        //Preenche a chave no formulário
        txtChave.setText(System.getProperty("user.name").toUpperCase());
        //dá foco no campo de senha
        txtSenha.requestFocus();
        
        
        //Altera a cor do formulário
        //Color corDoformulario = new Color(0,56,168);//Azul do banco
        //Random gerador = new Random(); 
        //int R = gerador.nextInt(256);
        //int G = gerador.nextInt(256);
        //int B = gerador.nextInt(256);          
        //Color corDoformulario = new Color(R,G,B);
        // Color corDoformulario = new Color(0, 228, 255);
        Color corDoformulario = new Color(255, 204, 20);
        getContentPane().setBackground(corDoformulario); //Aqui o método chave
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //Altera o tamanho do form
        // setBounds(300, 200, 400, 300);
        
        //Para o form ficar no meio da tela    
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();
        Integer largura;
        Integer altura;

        largura = d.width;
        largura = (largura / 2) - (550 / 2);
        altura = d.height;
        altura = (altura / 2) - (595 / 2);

        setBounds(largura, altura, 595, 550);

        // Copiar arquivos de logo LogoRetangular.png e AR.pdf da rede para temp
        //File arIN = new File("c:/temp/AR.pdf");  
        File arIN = new File("\\\\Srispo02525\\cso1981servjudfunci$\\Publica\\INFORMACOES A TERCEIROS\\OFÍCIO\\OFICIO_PROG\\AR.pdf");
        //     \\Srispo02525\cso1981servjudfunci$\Publica\INFORMACOES A TERCEIROS\OFÍCIO\OFICIO_PROG

        File arOUT = new File("c:/temp/AR.pdf");
        copyFile(arIN, arOUT);

        // File logoIN = new File("c:/temp/LogoRetangular.png");  
        File logoIN = new File("\\\\Srispo02525\\cso1981servjudfunci$\\Publica\\INFORMACOES A TERCEIROS\\OFÍCIO\\OFICIO_PROG\\LogoRetangular.png");

        File logoOUT = new File("c:/temp/LogoRetangular.png");
        copyFile(logoIN, logoOUT);

        this.setIconImage(Toolkit.getDefaultToolkit().getImage("Logo.png"));

        fecharMeiaNoite();
        
        
        

    }

    public void fecharMeiaNoite() {
        new Thread() {
            @Override
            public void run() {

                DateTime dateTime1 = new DateTime();
                //     System.out.println(dateTime1.getMinuteOfDay());

                int dateTime3 = dateTime1.getDayOfMonth();
                dateTime3 = dateTime3 + 1;
                //   System.out.println(dateTime3);

                int dateTime4 = 0;
                while (dateTime3 != dateTime4) {
                    dateTime4 = dateTime1.getDayOfMonth();
                }
                System.exit(0);

            }
        }.start();
    }
    
    private void buscaVersao(String Programa) throws SQLException, IOException {//Busca o código do contratado para inserir no banco

        String versao = null;

        try (com.mysql.jdbc.Connection cnenv = (com.mysql.jdbc.Connection) new Conexao().conectar()) {

            String sqlBuscaVesao = "Select * from tbl_VersaoProgramas where tbl_VersaoProgramas.NOMEDOPROGRAMA= " + "'" + Programa + "'";

            java.sql.Statement stm = cnenv.createStatement();
            try {
                ResultSet rs = stm.executeQuery(sqlBuscaVesao);
                //cn.close();
                if (rs.next()) {
                    //Processar, do jeito que você já fez                    
                    versao = rs.getString("VERSAO");
                    //cn.close();                    
                } else {

                }
            } catch (SQLException e) {
            }
        }

        if ("4.5".equals(versao)) {
        } else {
            JOptionPane.showMessageDialog(this, "Versão desatualizada. "
                    + "Favor Copiar a nova em G:\\Publica\\CENTRAL DE OFICIOS");
            try {
                Runtime.getRuntime().exec("explorer G:\\Publica\\CENTRAL DE OFICIOS");
            } catch (IOException ex) {
                Logger.getLogger(FrmExtatos.class.getName()).log(Level.SEVERE, null, ex);
                Runtime.getRuntime().exec("\\\\Srispo02525\\cso1981servjudfunci$\\Publica\\CENTRAL DE OFICIOS");
            }
            System.exit(0);
        }

    }

    public void copyFile(File in, File out) throws Exception {
        FileChannel destinationChannel;
        try (FileChannel sourceChannel = new FileInputStream(in).getChannel()) {
            destinationChannel = new FileOutputStream(out).getChannel();
            sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
        }
        destinationChannel.close();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnVai = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtChave = new javax.swing.JTextField();
        txtSenha = new javax.swing.JPasswordField();
        jLabel9 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("EXtratos      ");
        setAlwaysOnTop(true);
        getContentPane().setLayout(null);

        btnVai.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnVai.setForeground(new java.awt.Color(0, 51, 255));
        btnVai.setText("Iniciar");
        btnVai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVaiActionPerformed(evt);
            }
        });
        getContentPane().add(btnVai);
        btnVai.setBounds(20, 230, 150, 60);

        jPanel1.setBackground(new java.awt.Color(255, 224, 39));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(null);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 204, 255));
        jLabel4.setText("* Somente Funcionários");
        jLabel4.setToolTipText("");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(30, 80, 118, 13);

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Telas/chave1.png"))); // NOI18N
        jPanel1.add(jLabel8);
        jLabel8.setBounds(10, 20, 100, 40);

        txtChave.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtChave.setText("f7057419");
        jPanel1.add(txtChave);
        txtChave.setBounds(110, 30, 140, 30);

        txtSenha.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtSenha.setToolTipText("");
        txtSenha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSenhaKeyPressed(evt);
            }
        });
        jPanel1.add(txtSenha);
        txtSenha.setBounds(380, 30, 140, 30);

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Telas/Senha.png"))); // NOI18N
        jLabel9.setText("jLabel9");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(280, 30, 100, 30);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(10, 90, 560, 100);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(80, 170, 251));
        jLabel5.setText("Desenvolvido por: F7057419 - Maurício da Silva Luiz");
        jLabel5.setToolTipText("");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(280, 490, 300, 14);

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Telas/SAIRM.png"))); // NOI18N
        getContentPane().add(jLabel7);
        jLabel7.setBounds(10, 430, 60, 60);

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Telas/hand_3_2.png"))); // NOI18N
        getContentPane().add(jLabel6);
        jLabel6.setBounds(390, 280, 150, 170);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Telas/nome.png"))); // NOI18N
        getContentPane().add(jLabel2);
        jLabel2.setBounds(290, 30, 266, 40);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Telas/fundo.png"))); // NOI18N
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, -10, 390, 560);

        getAccessibleContext().setAccessibleName("EXtratos       ");
        getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnVaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVaiActionPerformed
        // TODO add your handling code here:
     
        //Cria as Listas
        ArrayList<String> listaCorreios = new ArrayList<>();
        ArrayList<String> listaCorreiosRetaurn = new ArrayList<>();
        ArrayList<Correio> listaCorreio;
        ArrayList<Correio> listaCorreioClientes = null;
        ArrayList<Oficio> listaOficio = new ArrayList<>();
        ArrayList<String> listaCorreiosMudarPasta = new ArrayList<>();

        //Busca os correios e retorno uma lista de correios
        BuscaCorreios x = new BuscaCorreios();
        try {
            listaCorreiosRetaurn = x.Sisbb(txtChave.getText(), txtSenha.getText(), listaCorreios);
        } catch (Exception ex) {
            Logger.getLogger(FrmExtatos.class.getName()).log(Level.SEVERE, null, ex);
        }

       // System.out.println(listaCorreiosRetaurn.size());

        
        //trata a lista de objetos correio-separando os dados da lista correios
        TrataCorreios y = new TrataCorreios();
        listaCorreio = (ArrayList) y.CarregaDados(listaCorreiosRetaurn);

     //   System.out.println(listaCorreio.size());

        //Entra no clientes e carrega a lista de contaClientes
        TrataCorreios z = new TrataCorreios();
        try {
            try {
                listaCorreioClientes = (ArrayList) z.BuscaContasNoCliente(txtChave.getText(), txtSenha.getText(), listaCorreio);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FrmExtatos.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(FrmExtatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        ////
        LocalDate hoje = new LocalDate();

        System.out.println(listaCorreioClientes.size());

        //Criacao da pasta na rede
        LocalDate hojePasta = new LocalDate();
        String mesS = null;
        int mess;
        int ano;
        int dia;
        // String data = "07/10/2014";
        dia = hojePasta.getDayOfMonth();
        mess = hojePasta.getMonthOfYear();
        ano = hojePasta.getYear();

        System.out.println(dia);
        System.out.println(mess);
        System.out.println(ano);

        switch (mess) {
            case 1:
                mesS = "Janeiro";
                break;
            case 2:
                mesS = "Fevereiro";
                break;
            case 3:
                mesS = "Março";
                break;
            case 4:
                mesS = "Abril";
                break;
            case 5:
                mesS = "Maio";
                break;
            case 6:
                mesS = "Junho";
                break;
            case 7:
                mesS = "Julho";
                break;
            case 8:
                mesS = "Agosto";
                break;
            case 9:
                mesS = "Setembro";
                break;
            case 10:
                mesS = "Outubro";
                break;
            case 11:
                mesS = "Novembro";
                break;
            case 12:
                mesS = "Dezembro";
                break;
        }

        File dirrede = new File("G:\\Publica\\CENTRAL DE OFICIOS\\BACENJUD EXTRATOS\\");
        File dirAnorede = new File("G:\\Publica\\CENTRAL DE OFICIOS\\BACENJUD EXTRATOS\\" + ano);
        File dirMesrede = new File("G:\\Publica\\CENTRAL DE OFICIOS\\BACENJUD EXTRATOS\\" + ano + "\\" + mesS);
        File dirDiarede = new File("G:\\Publica\\CENTRAL DE OFICIOS\\BACENJUD EXTRATOS\\" + ano + "\\" + mesS + "\\" + dia);
        String dataExtenso;
        dataExtenso = "São Paulo," + hoje.getDayOfMonth() + " de " + mesS + " de " + hoje.getYear() + "";

        try {
            //dir.mkdir();
            dirrede.mkdir();
        } catch (Exception e) {
            System.out.println(e);
        }

        try {
            //dirAno.mkdir();
            dirAnorede.mkdir();
        } catch (Exception e) {
            System.out.println(e);
        }

        try {
            //dirMes.mkdir();
            dirMesrede.mkdir();
        } catch (Exception e) {
            System.out.println(e);
        }

        try {
            //dirDia.mkdir();
            dirDiarede.mkdir();
        } catch (Exception e) {
            System.out.println(e);
        }

        /*FileOutputStream arquivo;
         arquivo = new FileOutputStream("c:/temp/OFICIO/BACENJUD/"+ano+"/"+mesS+"/"+dia+"/ForaPadrão_Data_"+now().toLocalTime().toString().replaceAll("/", "").replaceAll("-", "").replaceAll(":", "").substring(0, 6)+".txt");
         PrintStream printStream = new PrintStream(arquivo);*/
        //Rede
        FileOutputStream arquivorede = null;
        //FileOutputStream arquivorede2 = null;

        //Percorre a lista de objetos correios e cria um txt com os dados que os funcionários usarão para gerar o extrato no informação a terceiros 
        for (Correio correio : listaCorreioClientes) {
           try {
                //  arquivorede = new FileOutputStream("G:\\Publica\\CENTRAL DE OFICIOS\\BACENJUD EXTRATOS\\"+ano+"\\"+mesS+"\\"+dia+"\\"+correio.getNumeroDoCorreio().replaceAll("/", "").trim()+"_"+correio.getReu()+".txt");  
                arquivorede = new FileOutputStream("G:\\Publica\\CENTRAL DE OFICIOS\\BACENJUD EXTRATOS\\" + ano + "\\" + mesS + "\\" + dia + "\\" + correio.getReu() + "_" + correio.getNumeroDoCorreio().replaceAll("/", "").trim() + ".txt");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FrmExtatos.class.getName()).log(Level.SEVERE, null, ex);
            }
            PrintStream printStreamRede = new PrintStream(arquivorede);

            printStreamRede.println("CORREIO:  " + correio.getNumeroDoCorreio());
            printStreamRede.println("DATA: " + correio.getData());
            printStreamRede.println("PROTOCOCOLO: " + correio.getProtocolo());
            printStreamRede.println("SEQUENCIAL: " + correio.getSequencial());
            printStreamRede.println("PROCESSO: " + correio.getProcesso());
            printStreamRede.println("REU: " + correio.getReu());
            printStreamRede.println("CPF/CNPJ: " + correio.getCpfCpj());
            printStreamRede.println("DATA INICIAL: " + correio.getDataInicio());
            printStreamRede.println("DATA FINAL: " + correio.getDataFim());

            printStreamRede.println("");
            printStreamRede.println("");

            printStreamRede.println("------------------------------------------------");
            printStreamRede.println("CONTAS DO  CORREIO");
            printStreamRede.println("------------------------------------------------");

            ArrayList<ContadoCorreio> listaTestaContasCorreio = null;
            ArrayList<ContadoClientes> listaTestaContasClientes = null;
            listaTestaContasCorreio = correio.getContasCorreio();
            listaTestaContasClientes = correio.getContasClientes();

            String contas = "";

            System.out.println(listaTestaContasCorreio.size());

            for (ContadoCorreio correio1 : listaTestaContasCorreio) {
                printStreamRede.println("AGENCIA: " + correio1.getAgencia());
                printStreamRede.println("CONTA: " + correio1.getConta());
                printStreamRede.println("");
                printStreamRede.println("");
                contas = contas + correio1.getAgencia() + "-" + correio1.getConta() + "-\n";                
            }

      //    contas=contas+"&";
            System.out.println(contas);

            printStreamRede.println("------------------------------------------------");
            printStreamRede.println("OPERAÇÔES DO APLICATIVO CLIENTES");
            printStreamRede.println("------------------------------------------------");

            
            //Cria o objeto oficio e adiciona na lista--Sera usado para gerar o pdf
            //OFICIO
            
            Oficio oficio = new Oficio();
            oficio.setOficio(correio.getNumeroDoCorreio().trim());
            oficio.setDataExtenso(dataExtenso);
            oficio.setReu(correio.getReu());
            oficio.setData(correio.getData());
            oficio.setProtocolo(correio.getProtocolo());
            oficio.setSequencial(correio.getSequencial());
            oficio.setProcesso(correio.getProcesso());
            oficio.setDestinario(correio.getJuiz());

            //corpo
            //Carrega o Corpo   
            String corpo;

            corpo = "&&Meritíssimo(a) Juiz(a),\n"
                    //  + " " + "\n"
                    //  + " " + "\n"
                    + "          Em atenção aos termos do ofício supracitado, referente à solicitação de extrato de contas e/ou aplicações "
                    + "financeiras, no período de " + correio.getDataInicio() + " a " + correio.getDataFim() + ", vimos pela presente informar a V.Exa. o que segue:\n"
                    //    + " " + "\n"
                    //   + " " + "\n"
                    + "Réu: " + correio.getReu() + "\n"
                    + "CPF/CNPJ: " + correio.getCpfCpj() + "\n"
                    //      + " " + "\n"
                    //      + " "+ "\n"
                    + "Agência/Contas:\n"
                    + "" + contas + ""
                    + "       As informações contidas neste ofício são confidenciais e estão sendo encaminhadas à autoridade competente e requisitante com o devido "
                    + "amparo da Lei Complementar nº105, de 10 de janeiro de 2001, que dispõe sobre o sigilo das operações e serviços prestados pelas Instituições Financeiras.\n"
                    + "       Colocamo-nos à disposição para eventuais esclarecimentos e informações porventura necessários, aproveitando o ensejo para enviar protestos de elevada "
                    + "estima e consideração.\n"
                    + "             Respeitosamente,##";

            oficio.setCorpo(corpo);

            //Carrega  o endereço
            String endereco;

            endereco = "" + correio.getTribunal() + "\n"
                    + correio.getVara() + "\n"
                    + correio.getLougradouro() + " , " + correio.getNumero() + " -  " + correio.getBairro() + "\n"
                    + "CEP " + correio.getCep() + " " + correio.getCidade() + "/" + correio.getUf() + ""
                    + "";

            oficio.setEndereco(endereco);

            /*
            System.out.println("1111 " + oficio.getCorpo());
            System.out.println("2222 " + oficio.getData());
            System.out.println("333333 " + oficio.getDataExtenso());
            System.out.println("44444" + oficio.getDestinario());
            System.out.println("55555 " + oficio.getEndereco());
            System.out.println("666666 " + oficio.getLinhas());
            System.out.println("77777 " + oficio.getOficio());
            System.out.println("88888 " + oficio.getProcesso());
            System.out.println("99999 " + oficio.getProtocolo());
            System.out.println("1010101 " + oficio.getReu());
            System.out.println("11 11 11 11 " + oficio.getSequencial());*/

            listaOficio.add(oficio);

            System.out.println(listaTestaContasClientes.size());

            for (ContadoClientes correio2 : listaTestaContasClientes) {
                printStreamRede.println("Número da Operacao:" + correio2.getNumeroOperacao());
                printStreamRede.println("Titular: " + correio2.getTitular());
                printStreamRede.println("Agencia: " + correio2.getAgencia());
                printStreamRede.println("Situação: " + correio2.getSituação());
                printStreamRede.println("Produto: " + correio2.getProduto());//+"  ;   Modalidade: "+correio2.getModalidade()); 
                printStreamRede.println("Modalidade: " + correio2.getModalidade());
                printStreamRede.println("Data de Abertura da Conta : " + correio2.getDataInicial());
                printStreamRede.println("Data de Encerramento da Conta: " + correio2.getDataFinal());
                if ("0000-00-00".equals(correio2.getDataInicioPesquisa())) {
                    printStreamRede.println("SEM EXTRATOS, PERÍODO SOLICITADO ANTES DA ABERTURA DA CONTA OU APÓS O SEU ENCERRAMENTO");

                } else {
                    printStreamRede.println("Data Inicial da Pesquisa: " + correio2.getDataInicioPesquisa());
                    printStreamRede.println("Data Final da Pesquisa: " + correio2.getDataFimPesquisa());
                }

                printStreamRede.println("");
            }
        }

        
        //Percorre a lista oficio e gera os pdfs
        for (Oficio oficio : listaOficio) {

            GeraOficio GO = new GeraOficio();
            try {
                GO.Gerar(oficio);
            } catch (IOException | DocumentException | InterruptedException ex) {
                Logger.getLogger(FrmExtatos.class.getName()).log(Level.SEVERE, null, ex);
            }

            String correioBD;
            correioBD = oficio.getOficio();
            correioBD = "'" + correioBD + "'";

            String dataBD;
            dataBD = "'" + ano + "-" + mess + "-" + dia + "'";

            String tipoBD;
            tipoBD = "'EXT'";

            Connection cn = (Connection) new Conexao().conectar();
            // String sqlGrava = "INSERT INTO hc_honor (ID_H,DATA_EVT,NPJ,EVT,ADV,VALOR,DEF,MOTIVO,FUNCI_DEF,DATA_DEF,FUNCI_CAD,DATA_CAD,DATA_INI)VALUES (NULL,"+ dataHonorariosBd + "," + npjHonorariosBD + "," + buscaEnventosBD + "," + contratadoHonorariosBD + "," + valorHonorariosBD + "," + "'0'" + "," + "'0'" + "," + "''" + ",   '0000-00-00 00:00:00' ," + usuarioBD + ",now(),NULL);";    
            String sqlGrava = "INSERT INTO `bacenjud_quant` (id,correio, data,tipo) VALUES (null," + correioBD + ", " + dataBD + "," + tipoBD + ");";

            Statement stm = null;
            try {
                stm = (Statement) cn.createStatement();
            } catch (SQLException ex) {
            }
            int rs = 0;
            try {
                rs = stm.executeUpdate(sqlGrava);
                cn.close();
            } catch (SQLException ex) {
            }
            GeraOficio Ga = new GeraOficio();
            try {
                Ga.GerarAR(oficio);
            } catch (IOException | DocumentException ex) {
                Logger.getLogger(FrmExtatos.class.getName()).log(Level.SEVERE, null, ex);
            }

            ArrayList<InputStream> list = new ArrayList<>();
            ArrayList<InputStream> listrede = new ArrayList<>();
            try {
                // Source pdfs
                list.add(new FileInputStream(new File("c:\\temp\\OficioComLogo.pdf")));
                // list.add(new FileInputStream(new File("c:\\temp\\OficioComLogo.pdf")));
                list.add(new FileInputStream(new File("c:\\temp\\ARF.pdf")));

                listrede.add(new FileInputStream(new File("c:\\temp\\OficioComLogo.pdf")));
                // listrede.add(new FileInputStream(new File("c:\\temp\\OficioComLogo.pdf")));
                listrede.add(new FileInputStream(new File("c:\\temp\\ARF.pdf")));

   //                
//String caminhoRede="G:\\Publica\\CENTRAL DE OFICIOS\\BACENJUD EXTRATOS\\"+ano+"\\"+mesS+"\\"+dia+"\\"+oficio.getOficio().replaceAll("/", "").trim()+"_"+oficio.getReu()+".pdf";      
                String caminhoRede = "G:\\Publica\\CENTRAL DE OFICIOS\\BACENJUD EXTRATOS\\" + ano + "\\" + mesS + "\\" + dia + "\\" + oficio.getReu() + "_" + oficio.getOficio().replaceAll("/", "").trim() + ".pdf";
//TESTE                
//String caminhoRede="G:\\Publica\\CENTRAL DE OFICIOS\\BACENJUD SOLICITACAO\\"+ano+"\\"+mesS+"\\"+dia+"\\teste\\"+oficio.getOficio().replaceAll("/", "")+"_"+oficio.getAutor().replaceAll("/", "").replace("\\.","").toUpperCase()+".pdf";
//String caminho="c:\\temp\\teste\\Oficio.pdf";       

                //     OutputStream out = new FileOutputStream(new File(caminho));
                OutputStream outrede = new FileOutputStream(new File(caminhoRede));

                // doMerge(list, out);
                try {
                    //              doMerge(list, out); 
                    doMerge(listrede, outrede);
                    listaCorreiosMudarPasta.add(oficio.getOficio());

                } catch (DocumentException | IOException e) {
                }

            } catch (FileNotFoundException e) {
                System.out.println(e);
            }
        }

        System.out.println(listaCorreiosMudarPasta.size());

        TrataCorreios MudadePasta = new TrataCorreios();

        try {
            MudadePasta.TrocaDePasta(txtChave.getText(), txtSenha.getText(), listaCorreiosMudarPasta);
        } catch (SQLException | InterruptedException ex) {
            Logger.getLogger(FrmExtatos.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.exit(0);

    }//GEN-LAST:event_btnVaiActionPerformed

    private void txtSenhaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSenhaKeyPressed
        // TODO add your handling code here:
        int size = txtSenha.getText().length();
        if (size == 7) {
            btnVai.requestFocus();
            //  txtSenha.requestFocus();
        }
        if (size > 8) {
            txtSenha.setText(txtSenha.getText().substring(0, 8));
        }

    }//GEN-LAST:event_txtSenhaKeyPressed
    public static String soNumeros(String str) {
        if (str != null) {
            return str.replaceAll("[^,0123456789]", "");
        } else {
            return "";
        }
    }

    public static void doMerge(ArrayList<InputStream> list, OutputStream outputStream)
        throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();
        PdfContentByte cb = writer.getDirectContent();

        for (InputStream in : list) {
            PdfReader reader = new PdfReader(in);
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                document.newPage();
                //import the page from source pdf
                PdfImportedPage page = writer.getImportedPage(reader, i);
                //add the page to the destination pdf
                cb.addTemplate(page, 0, 0);
            }
        }
        outputStream.flush();
        document.close();
        outputStream.close();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmExtatos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmExtatos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmExtatos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmExtatos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new FrmExtatos().setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(FrmExtatos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnVai;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtChave;
    private javax.swing.JPasswordField txtSenha;
    // End of variables declaration//GEN-END:variables
}
