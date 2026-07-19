
package Modelo;

public class Transportadoras {

    private int    idTransportadoras;
    private String nombreTransportadoras;
    private String nit;
    private String correo;
    private String telefono;   

    public Transportadoras() {
    
    
    }
    
    
    public int    getIdTransportadoras()                   { 
        return idTransportadoras; 
    
    }
    
    
    public void   setIdTransportadoras(int idTransportadoras)    { 
        this.idTransportadoras = idTransportadoras; 
    
    }

    public String getNombreTransportadoras()                     { 
        return nombreTransportadoras; 
    
    }
    
    
    public void   setNombreTransportadoras(String nombreTransportadoras)       { 
        this.nombreTransportadoras = nombreTransportadoras; 
    
    }

    public String getNit()              { 
        return nit; 
    
    }
    
    
    public void   setNit(String nit)    { 
        this.nit = nit; }

    public String getCorreo()               { 
        return correo; 
    
    }
    
    public void   setCorreo(String correo)  { 
        this.correo = correo; 
    
    }

    public String getTelefono()                 { 
        return telefono; 
    
    }
    
    public void   setTelefono(String telefono)  { 
        this.telefono = telefono; 
    
    }

    @Override
    public String toString() {
        return "Transportadoras{id=" + idTransportadoras + ", nombre='" + nombreTransportadoras + "'}";
    }
}