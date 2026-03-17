package pt.hugo.LusApp.model.data.Enum;

public enum IPAddress {
    IP_ADDRESS ("http://192.168.0.55:8080");

    private final String url;

    IPAddress(String url) {
        this.url = url;
    }

    public String getUrl(){
        return url;
    }
}
