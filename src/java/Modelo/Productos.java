/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.math.BigDecimal;

public class Productos {

    private int    idProductos;
    private String descripcionProductos;
    private BigDecimal precioProductos;
    // FK
    private int    idUnidadesMedida;
    private int    idCategoriaProductos;

    public Productos() {
    
    }

   
    public int    getIdProductos()                   { 
        return idProductos; 
    
    }
    
    
    public void   setIdProductos(int idProductos)     { 
        this.idProductos = idProductos; 
    
    }

    public String getDescripcionProductos()            { 
        return descripcionProductos; 
    
    }
    
    
    public void   setDescripcionProductos(String descripcionProductos)   { 
        this.descripcionProductos = descripcionProductos; 
    
    }

    public BigDecimal getPrecioProductos()                 {
        return precioProductos; 
    
    }
    
    
    public void   setPrecioProductos(BigDecimal precioProductos)   {
        this.precioProductos = precioProductos; 
    
    }

    public int    getIdUnidadesMedida()               { 
        return idUnidadesMedida; 
    
    }
    
    public void   setIdUnidadesMedida(int idUnidadesMedida)    { 
        this.idUnidadesMedida = idUnidadesMedida; 
    
    }

    public int    getIdCategoriaProductos()                 { 
        return idCategoriaProductos; 
    
    }
    
    
    public void   setIdCategoriaProductos(int idCategoriaProductos)    { 
        this.idCategoriaProductos = idCategoriaProductos; 
    
    }

    @Override
    public String toString() {
        return "Productos{id=" + idProductos + ", descripcion='" + descripcionProductos + "', precio=" + precioProductos + "}";
    }
}
