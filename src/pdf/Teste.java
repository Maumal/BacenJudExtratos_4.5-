/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdf;

/**
 *
 * @author f7057419
 */
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph; //contiene opciones
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;//se encarga
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;//hace referencia a
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream; //contiene el
import java.io.IOException;
import org.joda.time.LocalDate;
//codigo necesario para crear un archivo PDF

public class Teste {

    //en esta ocacion no vamos a utilizar el constructor
    //por lo que si queremos podemos borrarlo pero siempre
    //es una buena practica de programacion ponerlo.
    public Teste() {

    }

    //crearemo nuestro pdf directamente del metodo main
    public static void main(String[] args) throws IOException, DocumentException {
        
     //   String str= "01452330300_M9 SPORTS S\S LTDA.txt" ;
        
        
        

      LocalDate hoje = new LocalDate();
        String s = "dezembro";

        String mesS = null;
        int mes;
        int ano;
        int dia;
        String data = "07/10/2014";
        dia = hoje.getDayOfMonth();
        mes = hoje.getMonthOfYear();
        ano = hoje.getYear();
        
        
        System.out.println(dia);
        System.out.println(mes);
        System.out.println(ano);
        
        
        

        switch (mes) {
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

      File dir = new File( "\\\\Srispo02525\\cso1981servjudfunci$\\Publica\\CENTRAL DE OFICIOS\\BACENJUD SOLICITACAO\\");
        File dirAno = new File("\\\\Srispo02525\\cso1981servjudfunci$\\Publica\\CENTRAL DE OFICIOS\\BACENJUD SOLICITACAO\\"+ano);
        File dirMes = new File("\\\\Srispo02525\\cso1981servjudfunci$\\Publica\\CENTRAL DE OFICIOS\\BACENJUD SOLICITACAO\\"+ano+"/"+mesS);
        File dirDia = new File("\\\\Srispo02525\\cso1981servjudfunci$\\Publica\\CENTRAL DE OFICIOS\\BACENJUD SOLICITACAO\\"+ano+"/"+mesS+"/"+dia);      
        
        
        try {
            dir.mkdir();
        } catch (Exception e) {
            System.out.println(e);
        }

        try {
            dirAno.mkdir();
        } catch (Exception e) {
            System.out.println(e);
        }

        try {
            dirMes.mkdir();
        } catch (Exception e) {
            System.out.println(e);
        }
        
        try {
            dirDia.mkdir();
        } catch (Exception e) {
            System.out.println(e);
        }

//creamos una variable de tipo documento
/*        Document documento = new Document(PageSize.A4, 72, 72, 72, 72);

        //como vamos a trabajar con ficheros/archivos
        //debemos hacerlo dentro de un bloque try
        //para capturar los posibles errores
        try {
            //le decimos que nos genere un archivo en PDF
            // pasandole como parametros el contenido de
            //dicho archivo y el nombre de nuestro PDF
            PdfWriter writer = PdfWriter.getInstance(documento,
                    new FileOutputStream("ExemploPDF.pdf"));

            //  Rectangle rct = new Rectangle(36, 54, 559, 788);
            Rectangle rct = new Rectangle(36, 54, 559, 788);
            //Definimos un nombre y un tamaño para el PageBox los nombres posibles son: “crop”, “trim”, “art” and “bleed”.
            writer.setBoxSize("art", rct);
            HeaderFooter event = new HeaderFooter();

            writer.setPageEvent(event);

            //depois de criar a headerfooter
            //
            //abrimos nuestro documento para poder escribir en él.
            documento.open();
            //diriamos que escribimos un nuevo parrafo en nuestro
            //documento el cual contiene solo una linea de texto.
        
          /*  String corpo;
            String cpf="25254425576";
            String cpf="25254425576";
            
            corpo ="Meritíssimo(a) Juiz(a),"
                  //  + " " + "\n"
                    + " " + "\n"
                    + "         Em Atenção à solicitação de Vossa Excelência, por meio do ofício expedido nos autos do Processo em epígrafe, informamos:"                  
               //    + " " + "\n"
                    + " " + "\n"
                    + "Dados do remetente: "
             ///      + " " + "\n"
                    + " "+ "\n"
                    +"Cpf/Cnpj: "+cpf+ "                                                                                        "
                    + "Agência: "+agencia+"\n"                   
               //     + " " + "\n"
                  //  + " " + "\n"
                    + "Dados da transfência: "   
        //           + " " + "\n"
                    + " " + "\n"
                    +"Valor: R$ "+valor+"                                                                  "
                    +"Protocolo: "+protocolo+ "                                                    "
                    +"ID: "+comprovante.getId()+"                                                    "
                    +"Banco: "+banco+"                                                          "
                    +"Agência: "+nBanco+"                                                  "
                    +"Documento: "+comprovante.getComprovante()+"\n"
                                    
             //       + " " + "\n"
                  //  + " " + "\n"
                    + "         Essas informações são confidências e são encaminhadas com amparo na lei Complementar nº105/2001 que dispõe sobre o sigilo das operações e serviços prestados pelas instituições financeiras"
                    + ""+"\n"
                    + "         Colocamo-nos à disposição para maiores esclarecimentos porventura necessários."
                
               //     + " "+"\n"
                      + " "+"\n"
                    + "Respeitosamente.";
            
            
            */
            
            
            
            
            
            
     //      documento.add(new Paragraph(corpo); 
            
            
            
            
            
            
            
       /*   for (int i = 0; i < 50; i++) {
                documento.add(new Paragraph("Teste pdf"));
                documento.add(new Paragraph("Linha" + i));
            }*/

        } //aqui capturamos los errores que pudiera generar
        //nuestro documeto
  /*      catch (FileNotFoundException de) {
            //imprimimos los errores
            System.err.println(de.getMessage());
        }

        //finalmente cerramos nuestro documento
        documento.close();

        Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "start", "ExemploPDF.pdf"});

        //mandamos a imprimir que nuestro archivo esta listo
        System.out.println("Archivo generado exitosamente");
    }

    static class HeaderFooter extends PdfPageEventHelper {

        public void onEndPage(PdfWriter writer, Document document) {
            Rectangle rect = writer.getBoxSize("art");
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_CENTER, new Phrase("Linha 1"),
                    rect.getRight(), rect.getTop(), 0);
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_CENTER, new Phrase("Linha 2"),
                    rect.getRight(), rect.getTop() - 15, 0);

            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_CENTER, new Phrase("Rodapé"),
                    (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 18, 0);

        }
        
    }
*/
}
