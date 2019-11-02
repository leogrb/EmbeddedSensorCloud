package mywebserver;

import java.time.LocalDate;

public class Temperature {

    private LocalDate date;
    private float temp;

    public Temperature(LocalDate date, float temp){
        this.date = date;
        this.temp = temp;
    }

    public float getTemp(){
        return this.temp;
    }

    public LocalDate getDate(){
        return this.date;
    }
}
