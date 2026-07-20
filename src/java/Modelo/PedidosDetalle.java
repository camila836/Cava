/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.math.BigDecimal;

public class PedidosDetalle {

    private int    idPedidosDetalle;
    private BigDecimal cantidadUnitaria;
    private BigDecimal subtotalPed;
    private int    idPedidosCabeza;
    private int    idProductos;

    public PedidosDetalle() {
    
    }

  
    public int    getIdPedidosDetalle()              { 
        return idPedidosDetalle; 
    
    }
    
    
    public void   setIdPedidosDetalle(int idPedidosDetalle)    { 
        this.idPedidosDetalle = idPedidosDetalle; 
    
    }
    

    public BigDecimal getCantidadUnitaria()                      {
        return cantidadUnitaria; 
    
    }
    
    
    public void   setCantidadUnitaria(BigDecimal cantidadUnitaria)     {
        this.cantidadUnitaria = cantidadUnitaria; 
    
    }
    

    public BigDecimal getSubtotalPed()                  {
        return subtotalPed; 
    
    }
    
    
    public void   setSubtotalPed(BigDecimal subtotalPed)   {
        this.subtotalPed = subtotalPed; 
    
    }

    public int    getIdPedidosCabeza()                 { 
        return idPedidosCabeza; 
    
    }
    
    
    public void   setIdPedidosCabeza(int idPedidosCabeza)      { 
        this.idPedidosCabeza = idPedidosCabeza; 
    
    }

    public int    getIdProductos()                { 
        return idProductos; 
    
    }
    public void   setIdProductos(int idProductos)    { 
        this.idProductos = idProductos; }
    
    

    @Override
    public String toString() {
        return "PedidosDetalle{id=" + idPedidosDetalle + ", cantidad=" + cantidadUnitaria + ", subtotal=" + subtotalPed + "}";
    }
}
