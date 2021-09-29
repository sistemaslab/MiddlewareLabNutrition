package blacksip.vtex.client.apiStatus;

/**
 *
 * @author Alejandro Cadena
 */
public enum ApiComponent{
    
    VTEX ("vtex"),
    MIDDLEWARE ("middleware"),
    DASHBOARD ("dashboard"),
    CLIENTE ("navasoft"),
    COORDINADORA ("coordinadora");
    
    private String name;
    
    private ApiComponent(String name) {
        this.name =  name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static ApiComponent fromName(String name){
        for (ApiComponent value : ApiComponent.values()) {
            if (value.name.equalsIgnoreCase(name)) {
                return value;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name;
    }
    
}
