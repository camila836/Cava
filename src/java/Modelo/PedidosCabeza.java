/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PedidosCabeza {

    private int           idPedidosCabeza;
    private String        numeroPedido;
    private LocalDateTime fechaPedido;
    private String        descripcionPedido;
    private BigDecimal    valorTotal;
    // FK
    private int           idUsuarios;

    public PedidosCabeza() {}

    

    public int           getIdPedidosCabeza()                          { return idPedidosCabeza; }
    public void          setIdPedidosCabeza(int idPedidosCabeza)       { this.idPedidosCabeza = idPedidosCabeza; }

    public String        getNumeroPedido()                         { return numeroPedido; }
    public void          setNumeroPedido(String numeroPedido)      { this.numeroPedido = numeroPedido; }

    public LocalDateTime getFechaPedido()                              { return fechaPedido; }
    public void          setFechaPedido(LocalDateTime fechaPedido)     { this.fechaPedido = fechaPedido; }

    public String        getDescripcionPedido()                            { return descripcionPedido; }
    public void          setDescripcionPedido(String descripcionPedido)    { this.descripcionPedido = descripcionPedido; }

    public BigDecimal    getValorTotal()                       { return valorTotal; }
    public void          setValorTotal(BigDecimal valorTotal)  { this.valorTotal = valorTotal; }

    public int           getIdUsuarios()                   { return idUsuarios; }
    public void          setIdUsuarios(int idUsuarios)     { this.idUsuarios = idUsuarios; }

 
    }
