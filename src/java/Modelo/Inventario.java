/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

public class Inventario {

    private int    idInventario;
    private String descripcionInventario;
    private double stock;
    private int    idProductos;
    
    
   
    public Inventario() {
    
    
    }

  
    public int    getIdInventario()                      { 
        return idInventario; 
    
    }
    
    public void   setIdInventario(int idInventario)      { 
        this.idInventario = idInventario; 
    
    }

    public String getDescripcionInventario()             {
        return descripcionInventario; 
    
    }
    
    public void   setDescripcionInventario(String descripcionInventario) { 
        this.descripcionInventario = descripcionInventario; 
    
    }

    public double getStock()               { 
        return stock; 
    
    }
    
    public void   setStock(double stock)   { 
        this.stock = stock; 
    
    }

    public int    getIdProductos()        { 
        return idProductos; 
    
    }
    
    public void   setIdProductos(int idProductos)    { 
        this.idProductos = idProductos; 
    
    }

    @Override
    public String toString() {
        return "Inventario{id=" + idInventario + ", stock=" + stock + "}";
    }
}