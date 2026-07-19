package Modelo;

public class UnidadesMedida {

    private int    idUnidadesMedida;
    private String descripcionUnidadesM;

    public UnidadesMedida() {
    
    }

    public int    getIdUnidadesMedida()                    { 
        return idUnidadesMedida; 
    
    }
    
    
    public void   setIdUnidadesMedida(int idUnidadesMedida)     { 
        this.idUnidadesMedida = idUnidadesMedida; 
    
    }

    public String getDescripcionUnidadesM()                    { 
        return descripcionUnidadesM; 
    
    }
    
    
    public void   setDescripcionUnidadesM(String descripcionUnidadesM)  { 
        this.descripcionUnidadesM = descripcionUnidadesM;
    
    }

    @Override
    public String toString() {
        return "UnidadesMedida{id=" + idUnidadesMedida + ", descripcion='" + descripcionUnidadesM + "'}";
    }
}