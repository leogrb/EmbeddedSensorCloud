package mywebserver.Temperature;

import java.time.LocalDate;

public class Temperature {

    private int id;
    private LocalDate date;
    private float temp;

    public Temperature(LocalDate date, float temp) {
        this.date = date;
        this.temp = temp;
    }

    public Temperature() {
    }

    public int getId() {
        return this.id;
    }

    public float getTemp() {
        return this.temp;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }
}
