

package Modelo;

import java.time.LocalDateTime;

public class Envios {

    private int           idEnvios;
    private LocalDateTime fechaEnvios;
    private String        descripcionEnvios;
    private String        numeroGuia;
    private int           idPedidosCabeza;
    private int           idEstadoEnvio;
    private int           idTransportadoras;

    
    
    public Envios() {
        
    
    }
    
    
    public int  getIdEnvios()        { 
        return idEnvios; 
    
    }
    
    
    public void   setIdEnvios(int idEnvios)       { 
        this.idEnvios = idEnvios; 
    
    }

    public LocalDateTime getFechaEnvios()       { 
        return fechaEnvios; 
    
    }
    
    
    public void setFechaEnvios(LocalDateTime fechaEnvios)     { 
        this.fechaEnvios = fechaEnvios; 
    
    }

    public String getDescripcionEnvios()         { 
        return descripcionEnvios; 
    
    }
    
    
    public void setDescripcionEnvios(String descripcionEnvios)     { 
        this.descripcionEnvios = descripcionEnvios; 
    
    }

    public String getNumeroGuia()      {       
        return numeroGuia; 
    
    }
    
    
    public void setNumeroGuia(String numeroGuia)    { 
        this.numeroGuia = numeroGuia; 
    
    
    }

    public int getIdPedidosCabeza()    {
        return idPedidosCabeza; 
    
    }
    
    
    public void  setIdPedidosCabeza(int idPedidosCabeza)      { 
        this.idPedidosCabeza = idPedidosCabeza; 
    
    }

    public int getIdEstadoEnvio()    { 
        return idEstadoEnvio; 
    
    }
    
    
    public void setIdEstadoEnvio(int idEstadoEnvio)    { 
        this.idEstadoEnvio = idEstadoEnvio; 
    
    }

    public int getIdTransportadoras()     { 
        return idTransportadoras; 
    
    }
    
    
    public void setIdTransportadoras(int idTransportadoras)     { 
        this.idTransportadoras = idTransportadoras; 
    
    }

    @Override
    public String toString() {
        return "Envios{id=" + idEnvios + ", guia='" + numeroGuia + "'}";
    }
}