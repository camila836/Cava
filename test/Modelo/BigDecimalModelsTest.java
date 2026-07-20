package Modelo;

import java.math.BigDecimal;

/**
 * Prueba de regresion ejecutable sin framework ni acceso a la base de datos.
 */
public final class BigDecimalModelsTest {

    private BigDecimalModelsTest() {
    }

    public static void main(String[] args) {
        verificaContratosDeModels();
        verificaConstruccionDesdeTexto();
        verificaComparacionNumerica();
        verificaLimitesDecimalDiezDos();
        System.out.println("BIGDECIMAL_MODELS_OK");
    }

    private static void verificaContratosDeModels() {
        BigDecimal precio = new BigDecimal("12500.50");
        BigDecimal stock = new BigDecimal("0.00");
        BigDecimal total = new BigDecimal("25001.00");
        BigDecimal cantidad = new BigDecimal("2.00");
        BigDecimal subtotal = new BigDecimal("25001.00");
        BigDecimal monto = new BigDecimal("-1.00");

        Productos productos = new Productos();
        productos.setPrecioProductos(precio);
        verificaMismoValor(precio, productos.getPrecioProductos(), "precioProductos");

        Inventario inventario = new Inventario();
        inventario.setStock(stock);
        verificaMismoValor(stock, inventario.getStock(), "stock");

        PedidosCabeza cabeza = new PedidosCabeza();
        cabeza.setValorTotal(total);
        verificaMismoValor(total, cabeza.getValorTotal(), "valorTotal");

        PedidosDetalle detalle = new PedidosDetalle();
        detalle.setCantidadUnitaria(cantidad);
        detalle.setSubtotalPed(subtotal);
        verificaMismoValor(cantidad, detalle.getCantidadUnitaria(), "cantidadUnitaria");
        verificaMismoValor(subtotal, detalle.getSubtotalPed(), "subtotalPed");

        Pagos pagos = new Pagos();
        pagos.setMonto(monto);
        verificaMismoValor(monto, pagos.getMonto(), "monto negativo");

        productos.setPrecioProductos(null);
        if (productos.getPrecioProductos() != null) {
            throw new AssertionError("El Model debe conservar null para que la capa validadora o SQL lo rechace");
        }
    }

    private static void verificaConstruccionDesdeTexto() {
        BigDecimal decimal = new BigDecimal("10.25");
        verificaMismoValor(new BigDecimal("10.25"), decimal, "separador decimal con punto");
        esperaTextoInvalido("");
        esperaTextoInvalido("10,25");
    }

    private static void verificaComparacionNumerica() {
        if (new BigDecimal("1.0").compareTo(new BigDecimal("1.00")) != 0) {
            throw new AssertionError("compareTo debe ignorar diferencias exclusivamente de escala");
        }
    }

    private static void verificaLimitesDecimalDiezDos() {
        if (!cabeEnDecimalDiezDos(new BigDecimal("99999999.99"))) {
            throw new AssertionError("El maximo positivo debe ser compatible con DECIMAL(10,2)");
        }
        if (!cabeEnDecimalDiezDos(new BigDecimal("-99999999.99"))) {
            throw new AssertionError("El limite negativo debe ser representable aunque su uso dependa del negocio");
        }
        if (cabeEnDecimalDiezDos(new BigDecimal("100000000.00"))) {
            throw new AssertionError("Nueve enteros exceden DECIMAL(10,2)");
        }
        if (cabeEnDecimalDiezDos(new BigDecimal("0.001"))) {
            throw new AssertionError("Tres decimales exceden la escala 2 sin una regla de redondeo");
        }
    }

    private static boolean cabeEnDecimalDiezDos(BigDecimal valor) {
        int digitosEnteros = valor.precision() - valor.scale();
        return valor.scale() <= 2 && digitosEnteros <= 8;
    }

    private static void esperaTextoInvalido(String texto) {
        try {
            new BigDecimal(texto);
            throw new AssertionError("Se esperaba texto decimal invalido: '" + texto + "'");
        } catch (NumberFormatException esperado) {
            // Resultado esperado: no se acepta texto vacio ni coma decimal.
        }
    }

    private static void verificaMismoValor(BigDecimal esperado, BigDecimal actual, String campo) {
        if (actual == null || esperado.compareTo(actual) != 0) {
            throw new AssertionError("Valor inesperado para " + campo);
        }
    }
}
