/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

public class MediosPagos {

    private int    idMediosPagos;
    private String descripcionMediosPagos;

    public MediosPagos() {
    
    }

    public int    getIdMediosPagos()                       { 
        return idMediosPagos; 
    
    }
    
    
    public void   setIdMediosPagos(int idMediosPagos)      { 
        this.idMediosPagos = idMediosPagos; 
    
    }

    public String getDescripcionMediosPagos()              {
        return descripcionMediosPagos; 
    
    }
    
    
    public void   setDescripcionMediosPagos(String descripcionMediosPagos) { 
        this.descripcionMediosPagos = descripcionMediosPagos; 
    
    }
    

    @Override
    public String toString() {
        return "MediosPagos{id=" + idMediosPagos + ", descripcion='" + descripcionMediosPagos + "'}";
    }
}