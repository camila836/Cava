package Modelo;

/** Proyección mínima y privada usada exclusivamente durante autenticación. */
public final class CredencialesUsuario {

    private final int idUsuarios;
    private final String nombres;
    private final String claveHash;
    private final boolean activo;
    private final String codigoRol;

    public CredencialesUsuario(int idUsuarios, String nombres, String claveHash,
            boolean activo, String codigoRol) {
        this.idUsuarios = idUsuarios;
        this.nombres = nombres;
        this.claveHash = claveHash;
        this.activo = activo;
        this.codigoRol = codigoRol;
    }

    public int getIdUsuarios() {
        return idUsuarios;
    }

    public String getNombres() {
        return nombres;
    }

    public String getClaveHash() {
        return claveHash;
    }

    public boolean isActivo() {
        return activo;
    }

    public String getCodigoRol() {
        return codigoRol;
    }
}
