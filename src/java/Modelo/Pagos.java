/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.time.LocalDateTime;

public class Pagos {

    private int           idPagos;
    private LocalDateTime fechaPagos;
    private String        descripcionPagos;
    private double        monto;
    private String        referenciaPago;
    private String        comprobantePago;
    // FK
    private int           idMediosPagos;
    private int           idPedidosCabeza;

    public Pagos() {}


    public int           getIdPagos()                      { return idPagos; }
    public void          setIdPagos(int idPagos)           { this.idPagos = idPagos; }

    public LocalDateTime getFechaPagos()                           { return fechaPagos; }
    public void          setFechaPagos(LocalDateTime fechaPagos)   { this.fechaPagos = fechaPagos; }

    public String        getDescripcionPagos()                             { return descripcionPagos; }
    public void          setDescripcionPagos(String descripcionPagos)      { this.descripcionPagos = descripcionPagos; }

    public double        getMonto()                { return monto; }
    public void          setMonto(double monto)    { this.monto = monto; }

    public String        getReferenciaPago()                       { return referenciaPago; }
    public void          setReferenciaPago(String referenciaPago)  { this.referenciaPago = referenciaPago; }

    public String        getComprobantePago()                          { return comprobantePago; }
    public void          setComprobantePago(String comprobantePago)    { this.comprobantePago = comprobantePago; }

    public int           getIdMediosPagos()                        { return idMediosPagos; }
    public void          setIdMediosPagos(int idMediosPagos)       { this.idMediosPagos = idMediosPagos; }

    public int           getIdPedidosCabeza()                          { return idPedidosCabeza; }
    public void          setIdPedidosCabeza(int idPedidosCabeza)       { this.idPedidosCabeza = idPedidosCabeza; }

    @Override
    public String toString() {
        return "Pagos{id=" + idPagos + ", monto=" + monto + ", referencia='" + referenciaPago + "'}";
    }
}