
package Modelo;


public class Roles {

    private int    idRoles;
    private String codigoRol;
    private String descripcionRol;

    public Roles() {
    
    }

  
    public int    getIdRoles()                 { 
        return idRoles; 
    
    }
    
    
    public void   setIdRoles(int idRoles)      { 
        this.idRoles = idRoles; 
    
    }

    public String getCodigoRol() {
        return codigoRol;
    }

    public void setCodigoRol(String codigoRol) {
        this.codigoRol = codigoRol;
    }

    public String getDescripcionRol()           { 
        return descripcionRol; 
    
    }
    
    
    public void   setDescripcionRol(String descripcionRol)   { 
        this.descripcionRol = descripcionRol; 
    
    }

    @Override
    public String toString() {
        return "Roles{id=" + idRoles + ", codigo='" + codigoRol
                + "', descripcion='" + descripcionRol + "'}";
    }
}
