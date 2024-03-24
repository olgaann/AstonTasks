package app.entities;

public class Booking {
    private long client_id;
    private String client_name;
    private int number;

    public Booking(long client_id, String client_name, int number) {
        this.client_id = client_id;
        this.client_name = client_name;
        this.number = number;
    }

    public Booking() {
    }

    public long getClient_id() {
        return client_id;
    }

    public void setClient_id(long client_id) {
        this.client_id = client_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
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
                "client_id=" + client_id +
                ", client_name='" + client_name + '\'' +
                ", number=" + number +
                '}';
    }
}
