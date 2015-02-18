/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BB_SISBB;

import com.ibm.eNetwork.ECL.*;
import com.ibm.eNetwork.ECL.event.*;
import java.awt.HeadlessException;
import java.util.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author F2419488
 */
public class SISBB implements ECLPSListener, ECLCommNotify, ECLXferListener, ECLOIANotify {

   //ECLPSListener - Register the instance with the ECLPS.RegisterPSEvent() method
    //ECLCommNotify - Register the instance with the ECLConnection.RegisterCommEvent() method
    //ECLXferListener - Register the instance with the ECLXfer.RegisterXferEvent() method
    //ECLOIANotify - Register the instance with the ECLOIA.RegisterOIAEvent() method
    private ECLSession session; // a sessão
    private ECLPS ps; // o presentation space
    private ECLXfer xfer; // a tranferência
    private String id; // o Id (letra) da sessão
    private ECLOIA var;
    private ECLConnection conn;
    private Process proc;
    private String ID_Sessao; // Variável do ID da sessão
    private boolean respostaHost = false;
    private final char[] buf = new char[24 * 80]; // Buffer para recepção de dados do tamanho de uma tela padrão.

    /* Método construtor
     *
     * */
    public SISBB(String sessao) {
        this.ID_Sessao = sessao;
    }

    /** Método de abertura de sessão do Sisbb com próxima sessão disponível
    public void connect(String Id) {
    Properties p = new Properties();
    p.put(ECLSession.SESSION_HOST, new String("sisbb.ws"));
    if (null != ID_Sessao) {
    p.put(ECLSession.SESSION_NAME, new String(ID_Sessao));
    }
    session = new ECLSession(p);
    ID_Sessao = session.GetName();
    ps = session.GetPS();
    xfer = session.GetXfer();
     * @param args}*/
    
    
    public static void main(String[] args) {

    }

    /** Método de abertura de sessão do Sisbb com o ID definido como "Z"
     *
     * @return aberto
     */
    public boolean open() {
        boolean aberto = false;
        try {
            proc = Runtime.getRuntime().exec("pcsws /s=" + ID_Sessao + " SisBB.WS");
            Thread.sleep(11000);
            aberto = true;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return (aberto);
    }

    /** Método de conexão de sessão do Sisbb
     *
     * @return conectado
     */
    public boolean connect() {
        boolean conectado = false;
        try {
            //session = null;
            Properties p = new Properties();
            p.put(ECLSession.SESSION_HOST, "sisbb.ws");
            p.put(ECLSession.SESSION_NAME, ID_Sessao);
            p.put(ECLSession.SESSION_PS_SIZE, "2");
            session = new ECLSession(p);
            ps = session.GetPS();
            ps.RegisterPSEvent(this);
            setVar(session.GetOIA());
            xfer = session.GetXfer();
            xfer.RegisterXferEvent(this);
            conectado = true;
        } catch (ECLErr e) {
        }
        return (conectado);
    }

    public boolean connectWeb() {
        boolean conectado = false;
        try {
            //session = null;
            Properties p = new Properties();
            p.put(ECLSession.SESSION_HOST, "sisbb.ws");
            p.put(ECLSession.SESSION_NAME, ID_Sessao);
            p.put(ECLSession.SESSION_PS_SIZE, "2");
            session = new ECLSession(p);
            ps = session.GetPS();
            ps.RegisterPSEvent(this);
            setVar(session.GetOIA());
            xfer = session.GetXfer();
            xfer.RegisterXferEvent(this);
            conectado = true;
        } catch (ECLErr e) {
        }
        return (conectado);
    }

    public void start() {
        start(null);
    }

    /**
     * inicializa sessão com Id (letra da sessão) fornecido.
     * se Id for null iniciará sessão nova.
     * caso contrátio tentará obter a sessão do Id fornecido.
     *
     */
    public void start(String Id) {
        Properties p = new Properties();
        p.put(ECLSession.SESSION_HOST, new String("sisbb.ws"));
        if (null != Id) {
            p.put(ECLSession.SESSION_NAME, new String(Id));
        }
        try {
            session = new ECLSession(p);
        } catch (ECLErr ex) {
            Logger.getLogger(SISBB.class.getName()).log(Level.SEVERE, null, ex);
        }
        id = session.GetName();
        ps = session.GetPS();
        xfer = session.GetXfer();
    }

    /**
     * Pára sessão mas não termina pcom
     */
    public void stop() {
        session.StopCommunication();
        ps = null;
        session = null;
        xfer = null;
    }

    /**
     * Pára sessão, termina pcom e fecha janela se estiver aberta
     */
    public void close() {
        session.StopCommunication();
        session.dispose();
        ps = null;
        session = null;
        xfer = null;
    }

    /**
     * Fecha janela se estiver aberta
     */
    public void dispose() {
        session.dispose();
        ps = null;
        session = null;
        xfer = null;
    }

    /**
     * Retorna String contendo o conteúdo exposto na tela.
     * !!! Retorna o conteúdo de campos ocultos a exemplo do campo senha !!!
     * !!! Cuidado na utilização !!!
     * parâmetros:
     * row - linha
     * col - coluna
     * size - tamanho a ser retornado
     */
    public String get(int row, int col, int size) {
        try {
            //def buf = new char[size+1];
            ps.GetString(buf, buf.length, row, col, size);
        } catch (ECLErr ex) {
            Logger.getLogger(SISBB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (new String(Arrays.copyOfRange(buf, 0, size))).trim() + "";
    }

    /**
     * Retorna toda a tela numa única String sem quebra de linhas
     */
    public String get() {
        return get(1, 1, 24 * 80);
    }

    public void set(String arg) {
        try {
            ps.SendKeys(arg);
        } catch (ECLErr ex) {
            Logger.getLogger(SISBB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Envia texto para posição na tela.
     * parâmetros:
     * arg - texto a ser enviado.
     * row - linha.
     * col - coluna.
     */
    public void set(String arg, int row, int col) {
        try {
            ps.SendKeys(arg, row, col);
        } catch (ECLErr ex) {
            Logger.getLogger(SISBB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Variação de set que envia arg1 para tela e aguarda até que arg2 apareça na tela.
     */
    public void setWF(String arg1, int arg2, int arg3, String arg4) {
        set(arg1, arg2, arg3);
        set("[enter]");
        waitFor(arg4);
    }

    /**
     * Variação de set que envia arg1 para tela e aguarda até que arg2 apareça na tela
     * ou que o tempo de arg3 em ms seja atingido.
     */
    public void setWF(String arg1, int arg2, int arg3, String arg4, int arg5) {
        set(arg1);
        set("[enter]");
        waitFor(arg4, arg5);
    }

    /**
     * Variação de set que envia arg1 para tela e aguarda enquanto arg4 está na tela.
     */
    public void setWW(String arg1, int arg2, int arg3, String arg4) {
        set(arg1);
        waitWhile(arg4);
    }

    /**
     * Variação de set que envia arg1 para tela e aguarda enquanto arg2 está na tela.
     */
    public void setWW(String arg1, String arg2) {
        set(arg1);
        waitWhile(arg2);
    }

    /**
     * Variação de set que envia arg1 para tela e aguarda enquanto arg2 está na tela
     * ou que o tempo de arg3 em ms seja atingido.
     */
    public void setWW(String arg1, String arg2, int arg3) {
        set(arg1);
        waitWhile(arg2, arg3);
    }

    /**
     * Aguarda até que arg apareça na tela ou até timeout em ms ser atingido.
     */
    public void waitFor(String arg, int timeout) {
        try {
            ps.WaitForStringInRect(arg, 1, 1, 24, 80, timeout, true, false);
        } catch (InterruptedException ex) {
            Logger.getLogger(SISBB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Aguarda até que arg apareça na tela.
     */
    public void waitFor(String arg) {
        try {
            ps.WaitForStringInRect(arg, 1, 1, 24, 80);
        } catch (InterruptedException ex) {
            Logger.getLogger(SISBB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Aguarda enquanto arg estiver na tela ou até timeout em ms ser atingido.
     */
    public void waitWhile(String arg, int timeout) {
        try {
            ps.WaitWhileStringInRect(arg, 1, 1, 24, 80, timeout, true, false);
        } catch (InterruptedException ex) {
            Logger.getLogger(SISBB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Aguardo enquanto arg estiver na tela.
     */
    public void waitWhile(String arg) {
        try {
            ps.WaitWhileStringInRect(arg, 1, 1, 24, 80);
        } catch (InterruptedException ex) {
            Logger.getLogger(SISBB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Aguarda até que cursor esteja em posição
     * parâmetros:
     * row - linha.
     * col - coluna.
     */
    public void waitForCursor(int row, int col) {
        try {
            ps.WaitForCursor(row, col);
        } catch (InterruptedException ex) {
            Logger.getLogger(SISBB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Permite setar o cursor num lugar definido na tela
     * @param linha
     * @param coluna
     */
    public void setCursor(int linha, int coluna) {
        try {
            ps.SetCursorPos(linha, coluna);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retorna booleano true se arg estiver na tela
     */
    @SuppressWarnings("static-access")
    public boolean contains(String arg) {
        boolean teste = false;
        try {
            if (ps.SearchString(arg, ps.SEARCH_FORWARD) > 0) {
                teste = true;
            }
        } catch (ECLErr ex) {
            Logger.getLogger(SISBB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return teste;
    }

    /**
     * Retorna booleano true se primeiras letras da tela forem arg.
     * Ignora espações em branco inicais.
     */
    public boolean startsWith(String arg) {
        get(1, 1, 80 * 3).trim().startsWith(arg);
        return true;
    }

    /**
     * Recebe arquivo BBM do utilit.
     * parâmetros:
     * nomeBBM - nome completo do arquivo BBM (ex.:BBM.CRMF936.O9912.D8558.DYYMMDD.M250).
     * nomeLocal - nome completo do arquivo no sistema de arquivo local (ex.:C://CRMF936.txt).
     * texto - booleano que indica se arquivo BBM é texto (true) ou binário (false)
     */
    public void receiveFile(String nomeBBM, String nomeLocal, boolean texto) {
        try {
            xfer.SetPCCodePage("037");
            xfer.SetCodePage("037");
            xfer.SetTimeout(30);
            xfer.ReceiveFile(nomeBBM, nomeLocal, (texto ? "ASCII CRLF TIME" : ""));
        } catch (ECLErr ex) {
            Logger.getLogger(SISBB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /** Método que permite tratar as telas da Agência de Notícias
     *
     */
    public void trataAgNoticias() {
        this.waitFor("SISBB");
        if (this.get(3, 32, 7).equals("Agência")) {
            this.f3();
        }
    }

    /** Método que permite tratar as telas exibidas aos Estagiários
     *
     */
    public void trataTerceiro() {
        this.waitFor("SISBB");
        if (this.get(1, 2, 4).equals("BB64")) {
            this.enter();
        }
    }

    /** Método para inicializar o Sisbb e efetuar o login
     *
     * @param s Sessão do Sisbb recebida do robô
     * @param chave Chave recebida do robô
     * @param senha Senha recebida do robô
     * @param opcaoLogin Opção de login recebida do robô
     */
    public void inicializar(String chave, String senha, int opcaoLogin) throws InterruptedException {
        try {
            if (open()) { // Testa se houve abertura da sessão
                if (connect()) { // Testa se houve conexão da sessão
                    waitFor("Terminal", 10000);
                    enter();
                    waitFor("Código de Usuário");
                    set(chave, 13, 21);
                    set(senha, 14, 21);
                    // Switch para escolha de login
                    opcaoLogin(opcaoLogin);
                }
            }
        } catch (HeadlessException ex) {
            ex.printStackTrace();
            close();
        }
    }

    /** Método para inicializar o Sisbb e efetuar o login
     *
     * @param s Sessão do Sisbb recebida do robô
     * @param chave Chave recebida do robô
     * @param senha Senha recebida do robô
     * @param opcaoLogin Opção de login recebida do robô
     */
    public void opcaoLogin(int opcaoLogin) throws InterruptedException {
        switch (opcaoLogin) {
            case 1: // Acesso direto
                enter();
                 Thread.sleep(300);
                trataTerceiro(); // Trata tela para estagiários
                 Thread.sleep(300);
                trataAgNoticias(); // Trata tela de agência de notícias
                 Thread.sleep(300);
                waitFor("Navegador");
                break;
            case 2: // Acesso #MAIS
                set("#MAIS", 15, 21);
                enter();
                trataTerceiro(); // Trata tela para estagiários
                trataAgNoticias(); // Trata tela de agência de notícias
                waitFor("#MAIS");
                set("16", 2, 15);
                enter();
                waitFor("Navegador");
                break;
            case 3: // Acesso com CIC
                set("CIC", 15, 21);
                enter();
                 Thread.sleep(300);
                trataTerceiro(); // Trata tela para estagiários
                 Thread.sleep(300);
                trataAgNoticias(); // Trata tela de agência de notícias
                 Thread.sleep(300);
                waitFor("CICS");
                if (get(23, 2, 32).equals("Usuário sem acessos cadastrados.")) {
                    JOptionPane.showMessageDialog(null, "Acesso ao CIC não autorizado!", "Erro", JOptionPane.ERROR_MESSAGE);
                    dispose();
                }
                set("50", 21, 19);
                enter();
                waitFor("Navegador");
                break;
        }
    }

    public void msgFinalizado() {
        JOptionPane.showMessageDialog(null,
                "O processo foi concluído.", "Informação",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /** Método que permite enviar o comando [enter] para a sessão do Sisbb
     *
     */
    public void enter() {
        set("[enter]");
    }

    /** Método que permite enviar o comando [F1] para a sessão do Sisbb
     *
     */
    public void f1() {
        set("[pf1]");
    }

    /** Método que permite enviar o comando [F2] para a sessão do Sisbb
     *
     */
    public void f2() {
        set("[pf2]");
    }

    /** Método que permite enviar o comando [F3] para a sessão do Sisbb
     *
     */
    public void f3() {
        set("[pf3]");
    }

    /** Método que permite enviar o comando [F4] para a sessão do Sisbb
     *
     */
    public void f4() {
        set("[pf4]");
    }

    /** Método que permite enviar o comando [F5] para a sessão do Sisbb
     *
     */
    public void f5() {
        set("[pf5]");
    }

    /** Método que permite enviar o comando [F6] para a sessão do Sisbb
     *
     */
    public void f6() {
        set("[pf6]");
    }

    /** Método que permite enviar o comando [F7] para a sessão do Sisbb
     *
     */
    public void f7() {
        set("[pf7]");
    }

    /** Método que permite enviar o comando [F8] para a sessão do Sisbb
     *
     */
    public void f8() {
        set("[pf8]");
    }

    /** Método que permite enviar o comando [F9] para a sessão do Sisbb
     *
     */
    public void f9() {
        set("[pf9]");
    }

    /** Método que permite enviar o comando [F10] para a sessão do Sisbb
     *
     */
    public void f10() {
        set("[pf10]");
    }

    /** Método que permite enviar o comando [F11] para a sessão do Sisbb
     *
     */
    public void f11() {
        set("[pf11]");
    }

    /** Método que permite enviar o comando [F12] para a sessão do Sisbb
     *
     */
    public void f12() {
        set("[pf12]");
    }

    /**
     * Retorna o Ps (presentation space) do pcom
     */
    public ECLPS getPs() {
        return ps;
    }

    /**
     * Retorna a sessão do pcom
     */
    public ECLSession getSession() {
        return session;
    }

    /**
     * Retorna o objeto transfer do pcom
     */
    public ECLXfer getXfer() {

        return xfer;
    }

    /**
     * Retorna o id da sessão ativa
     */
    public String getId() {
        return id;
    }

    /**
     * Seta Id (letra da sessão) para a sessão e inicializa pccom
     */
    void setId(String arg) {
        start(arg);
    }

    public void setID_Sessao(String ID_Sessao) {
        this.ID_Sessao = ID_Sessao;
    }

    /** Método PSNotifyEvent
     * is called whenever the presentation space is updated. The presentation
     * space can be updated by the host (when a new screen is sent) or through
     * user input via ECLPS methods. An application should not assume it will
     * receive a distinct event for every update. An update event is defined as
     * one or more changes in the PS since the last time an event was generated
     * (or since registration, if there was no prior update event).
     *
     * @param evt The ECLPSEvent object.
     */
    public void PSNotifyEvent(ECLPSEvent evt) {
        if (evt.GetType() == 1) {
            respostaHost = true;
        }
    }

    /** Método PSNotifyStop
     * is called when event generation is stopped for any reason. Event
     * generation might stop because of an error condition or because the
     * ECLPS.UnregisterPSEvent() method was called.
     *
     * @param ps The object which generated the event.
     * @param reason This parameter is not currently used.
     */
    public void PSNotifyStop(ECLPS ps, int reason) {
    }

    /** Método PSNotifyError
     * is called whenever the ECLPS object detects an error during event
     * generation. An ECLErr object containing information about the error is
     * passed to this method. If event generation stops due to an error, the
     * PSNotifyStop() method is also called.
     *
     * @param ps The object which generated the event.
     * @param err The ECLErr object containing error data.
     */
    public void PSNotifyError(ECLPS ps, ECLErr err) {
        System.out.println("ErroConexao");
    }

    /** Method NotifyEvent
     * is called whenever the connection status changes. The connection status
     * is either true (connected) or false (not connected).
     *
     * @param conn The object which generated the event.
     * @param c Boolean value which is true if there is a connection, otherwise it is false.
     */
    public void NotifyEvent(ECLConnection conn, boolean c) {
        System.out.println("conectou");
    }

    /** Method NotifyStop
     * is called when event generation is stopped for any reason. Event
     * generation can be stopped because of an error condition or because the
     * ECLConnection.UnregisterCommEvent() method was called.
     *
     * @param conn The object which generated the event.
     * @param c This parameter is not currently used
     */
    public void NotifyStop(ECLConnection conn, int c) {
    }

    /** Method NotifyError
     * is called whenever the ECLConnection object detects an error during
     * event generation. An ECLErr object containing information about the error
     * is passed to this method. If event generation stops due to an error, the
     * NotifyStop() method is also called.
     *
     * @param conn The object which generated the event.
     * @param c The ECLErr object containing error data
     */
    public void NotifyError(ECLConnection conn, ECLErr c) {
    }

    /** Method XferNotifyError
     * is called whenever the ECLXfer object detects an error during event
     * generation. An ECLErr object containing information about the error is
     * passed to this method. If event generation stops due to an error, the
     * XferNotifyStop() method is also called.
     *
     * @param xfer The object which generated the event.
     * @param err The ECLErr object containing error data.
     */
    public void XferNotifyError(ECLXfer xfer, ECLErr err) {
        System.out.print("erro transferencia");
    }

    /** Method XferNotifyEvent
     *  is called whenever a xfer data buffer has been successfully sent to or
     * received from the host.
     *
     * @param evt The ECLXferEvent object
     */
    public void XferNotifyEvent(ECLXferEvent evt) {
        System.out.print("buffer recebido/enviado");
    }

    /** Method XferNotifyStop
     * is called when event generation is stopped for any reason. Event
     * generation might stop because of an error condition or because the
     * ECLXfer.UnregisterXferEvent() method was called.
     *
     * @param xfer The object which generated the event.
     * @param reason This parameter is not currently used
     */
    public void XferNotifyStop(ECLXfer xfer, int reason) {
        System.out.print("transferencia cancelada");
    }

    /** Method NotifyEvent
     * is called whenever a status indicator within ECLOIA changes. Status
     * changes may be caused by input inhibit stat changes, the keyboard state
     * changes, and communications state changes.
     *
     * @param oia The object which generated the event.
     * @param newState The current state of the OIA.
     * @param changedMask The bits in this mask indicate which bits in newState
     * have changed.
     * @param data Information such as a communications indicator like "655".
     * The data is specific to the state as indicated by newState.
     */
    public void NotifyEvent(ECLOIA oia, int newState, int changedMask, String data) {
        System.out.print("ecloia notify");
    }

    /** Method NotifyStop
     * is called when event generation is stopped for any reason. Event
     * generation might be stopped because of an error condition or because the
     * ECLOIA.UnregisterOIAEvent() method was called.
     *
     * @param oia The object which generated the event.
     * @param reason This parameter is not currently used.
     */
    public void NotifyStop(ECLOIA oia, int reason) {
        System.out.print("ecloia stop");
    }

    /** Method NotifyError
     * is called whenever the ECLOIA object detects an error during event
     * generation. An ECLErr object containing information about the error is
     * passed to this method. If event generation stops dueto an error, the
     * NotifyStop() method is also called.
     *
     * @param oia The object which generated the event.
     * @param e The ECLErr object containing error data.
     */
    public void NotifyError(ECLOIA oia, ECLErr e) {
        System.out.print("ecloia error");
    }

    /**
     * @return the var
     */
    public ECLOIA getVar() {
        return var;
    }

    /**
     * @param var the var to set
     */
    public void setVar(ECLOIA var) {
        this.var = var;
    }

    public void set(Integer Linha, int i, int i0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
