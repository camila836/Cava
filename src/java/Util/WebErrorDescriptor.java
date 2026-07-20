package Util;

/** Respuesta HTTP segura para la navegación HTML de CAVA. */
public final class WebErrorDescriptor {

    private final int status;
    private final String title;
    private final String message;

    public WebErrorDescriptor(int status, String title, String message) {
        this.status = status;
        this.title = title;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
}
