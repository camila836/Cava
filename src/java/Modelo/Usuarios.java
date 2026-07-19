package Modelo;

import java.time.LocalDate;

public class Usuarios {

    private int     idUsuarios;
    private String  nombres;
    private String  apellidos;
    private String  identificacion;
    private String  correo;
    private String  direccion;
    private String  telefono;
    private String  clave;
    private boolean isActivo;
    private LocalDate fechaNacimiento;
    private LocalDate fechaVencimientoClave;
    private boolean   autorizacionTratamientoDatos;
    private int     idRoles;
    private int     idTipoDocumento;
    private int     idCiudades;

    public Usuarios() {
    
    }

    public int     getIdUsuarios()                  { 
        return idUsuarios; 
    
    }
    
    public void    setIdUsuarios(int idUsuarios)     { 
        this.idUsuarios = idUsuarios; 
    
    }
    

    public String  getNombres()                      { 
        return nombres; 
    
    }
    
    
    public void    setNombres(String nombres)        { 
        this.nombres = nombres; 
    
    }

    public String  getApellidos()                    { 
        return apellidos; 
    
    }
    
    
    public void    setApellidos(String apellidos)     { 
        this.apellidos = apellidos; 
    
    }
    

    public String  getIdentificacion()               { 
        return identificacion; 
    
    }
    
    
    public void    setIdentificacion(String identificacion)    { 
        this.identificacion = identificacion; 
    
    }
    

    public String  getCorreo()                 { 
        return correo; 
    
    }
    
    
    public void    setCorreo(String correo)    { 
        this.correo = correo; 
    
    }
    

    public String  getDireccion()                 {
        return direccion; 
    
    }
    
    
    public void    setDireccion(String direccion)      { 
        this.direccion = direccion; 
    
    }

    public String  getTelefono()                { 
        return telefono; 
    
    }
    
    
    public void    setTelefono(String telefono)    { 
        this.telefono = telefono; 
    
    }

    public String  getClave()                { 
        return clave; 
    
    }
    
    
    public void    setClave(String clave)      { 
        this.clave = clave; 
    
    }
    

    public boolean isActivo()                      { 
        return isActivo; 
    
    }
    
    
    public void    setIsActivo(boolean isActivo)   { 
        this.isActivo = isActivo; 
    
    }

    public LocalDate getFechaNacimiento()               { 
        return fechaNacimiento; 
    
    }
    
    
    public void       setFechaNacimiento(LocalDate fechaNacimiento)    { 
        this.fechaNacimiento = fechaNacimiento; 
    
    }

    public LocalDate getFechaVencimientoClave()                       { 
        return fechaVencimientoClave; 
    
    }
    
    
    public void       setFechaVencimientoClave(LocalDate fechaVencimientoClave)  { 
        this.fechaVencimientoClave = fechaVencimientoClave; 
    
    }

    public boolean isAutorizacionTratamientoDatos()                       { 
        return autorizacionTratamientoDatos; 
    
    }
    
    
    public void    setAutorizacionTratamientoDatos(boolean autorizacionTratamientoDatos) { 
        this.autorizacionTratamientoDatos = autorizacionTratamientoDatos; }

    public int     getIdRoles()                  { 
        return idRoles; 
    
    }
    
    
    public void    setIdRoles(int idRoles)       { 
        this.idRoles = idRoles; 
    
    }
    

    public int     getIdTipoDocumento()                 { 
        return idTipoDocumento; 
    
    }
    
    
    public void    setIdTipoDocumento(int idTipoDocumento)     { 
        this.idTipoDocumento = idTipoDocumento; 
    
    }

    public int     getIdCiudades()          { 
        return idCiudades; 
    }
     
    public void    setIdCiudades(int idCiudades)       { this.idCiudades = idCiudades; }

    @Override
public String toString() {
    return "Usuarios{" +
            "idUsuarios=" + idUsuarios +
            ", nombres='" + nombres + '\'' +
            ", apellidos='" + apellidos + '\'' +
            ", correo='" + correo + '\'' +
            ", isActivo=" + isActivo +
            '}'; // clave NO se incluye a propósito
}
   
}
