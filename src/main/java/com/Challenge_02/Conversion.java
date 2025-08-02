package com.Challenge_02;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Conversion {
    private String monedaBase;
    private String monedaDestino;
    private double valorOriginal;
    private double valorConvertido;
    private LocalDateTime fecha;

    public Conversion(String monedaBase, String monedaDestino, double valorOriginal, double valorConvertido) {
        this.monedaBase = monedaBase;
        this.monedaDestino = monedaDestino;
        this.valorOriginal = valorOriginal;
        this.valorConvertido = valorConvertido;
        this.fecha = LocalDateTime.now();
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("[%s] %.2f %s = %.2f %s",
                this.fecha.format(formatter),
                this.valorOriginal,
                this.monedaBase,
                this.valorConvertido,
                this.monedaDestino);
    }
}
