
package Modelo;


public class Roles {

    private int    idRoles;
    private String descripcionRol;

    public Roles() {
    
    }

  
    public int    getIdRoles()                 { 
        return idRoles; 
    
    }
    
    
    public void   setIdRoles(int idRoles)      { 
        this.idRoles = idRoles; 
    
    }

    public String getDescripcionRol()           { 
        return descripcionRol; 
    
    }
    
    
    public void   setDescripcionRol(String descripcionRol)   { 
        this.descripcionRol = descripcionRol; 
    
    }

    @Override
    public String toString() {
        return "Roles{id=" + idRoles + ", descripcion='" + descripcionRol + "'}";
    }
}