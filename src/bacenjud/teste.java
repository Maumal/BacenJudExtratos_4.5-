/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bacenjud;

import org.joda.time.LocalDate;

/**
 *
 * @author f7057419
 * 
 * 
 * 
 */

public class teste {

    public static void main(String[] args) {
        
        
        LocalDate DataInicialClientes = new LocalDate("2010-01-10");
        LocalDate DataFinalClientes = new LocalDate("2010-01-20");

        LocalDate DataInicialCorreio = new LocalDate("2008-01-08");
        LocalDate DataFinalCorreio = new LocalDate("2008-01-25");

        if (DataInicialCorreio.isBefore(DataInicialClientes) && (DataFinalCorreio.isBefore(DataInicialClientes))) {
            //                       |____________| data da conta
            //           |_____|                            datas solicitadas                             
            System.out.println("0");
        } else if (DataInicialCorreio.isAfter(DataFinalClientes) && (DataFinalCorreio.isAfter(DataFinalClientes))) {
            //               |____________| data da conta
            //                                    |_____|    datas solicitadas                             
            System.out.println("1");
        } else if (DataInicialCorreio.isBefore(DataInicialClientes) && (DataFinalCorreio.isEqual(DataInicialClientes))) {
            //                   |____________| data da conta
            //             |_____|                     datas solicitadas                                               
            System.out.println("2");
        } else if (DataInicialCorreio.isBefore(DataInicialClientes) && (DataFinalCorreio.isBefore(DataFinalClientes))) {
            //                |____________| data da conta
            //             |_____|              datas solicitadas                                              
            System.out.println("3");
        } else if (DataInicialCorreio.isAfter(DataInicialClientes) && (DataFinalCorreio.isBefore(DataFinalClientes))) {
            //              |____________| data da conta
            //                |_____|    datas solicitadas                                              
            System.out.println("4");
        } else if (DataInicialCorreio.isAfter(DataInicialClientes) && (DataFinalCorreio.isAfter(DataFinalClientes))) {
            //                   |____________| data da conta
            //                              |_____|    datas solicitadas                                              
            System.out.println("5");
        } else if (DataInicialCorreio.isEqual(DataInicialClientes) && (DataFinalCorreio.isAfter(DataFinalClientes))) {
            //                 |____________| data da conta
            //                             |_____|    datas solicitadas                                              
            System.out.println("6");
        } else if (DataInicialCorreio.isEqual(DataInicialClientes) && (DataFinalCorreio.isEqual(DataFinalClientes))) {
            //                |________________| data da conta
            //                |________________|    datas solicitadas                                              
            System.out.println("7");
        } else if (DataInicialCorreio.isBefore(DataInicialClientes) && (DataFinalCorreio.isAfter(DataFinalClientes))) {
            //                   |____________| data da conta
            //                 |________________|    datas solicitadas                                              
            System.out.println("8");
        }
    }
}
