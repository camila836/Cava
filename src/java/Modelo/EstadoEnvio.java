
package Modelo;


public class EstadoEnvio {

    private int    idEstadoEnvio;
    private String descripcionEstadoEnvio;
    

    
    public EstadoEnvio() {
    
    
    }
    

    public int    getIdEstadoEnvio()    {
        return idEstadoEnvio; 
    
    }
    
    
    public void   setIdEstadoEnvio(int idEstadoEnvio)  { 
        this.idEstadoEnvio = idEstadoEnvio; 
    
    }
    

    public String getDescripcionEstadoEnvio()  { 
        return descripcionEstadoEnvio; 
    
    }
    
    
    public void   setDescripcionEstadoEnvio(String descripcionEstadoEnvio)  { 
        this.descripcionEstadoEnvio = descripcionEstadoEnvio; 
    
    
    }
    

    @Override
    public String toString() {
        return "EstadoEnvio{id=" + idEstadoEnvio + ", descripcion='" + descripcionEstadoEnvio + "'}";
    }
}
    
