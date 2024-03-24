package app.entities;

public class Booking {
    private long clientId;
    private String clientName;
    private int number;

    public Booking(long clientId, String clientName, int number) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.number = number;
    }

    public Booking() {
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "clientId=" + clientId +
                ", clientName='" + clientName + '\'' +
                ", number=" + number +
                '}';
    }
}
