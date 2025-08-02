package com.Challenge_02;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Conversor {

    public static void eleccionUserMenu() throws IOException, InterruptedException {
        Scanner teclado = new Scanner(System.in);
        String menu = """
                *************************************************
                Bienvenido/a al Conversor de Monedas 😊
                
                Elija una de las siguientes opciones:
                1) Realizar una conversión
                2) Ver historial
                3) Salir
                
                *************************************************
                """;

        int opcion = 0;
        String claveAPI = "2b37ebd4e1c79d6e827e1814";
        List<Conversion> historial = new ArrayList<>();


        Set<String> monedasValidas = ConversorApp.obtenerCodigosMoneda(claveAPI);
        if (monedasValidas.isEmpty()) {
            System.out.println("No se pudo obtener la lista de monedas. Verifique su clave de API y la conexión a internet.");
            return;
        }

        while (opcion != 3) {
            System.out.println(menu);
            if (teclado.hasNextInt()) {
                opcion = teclado.nextInt();
                teclado.nextLine();
            } else {
                System.out.println("Entrada no válida. Por favor, ingrese un número.");
                teclado.nextLine();
                continue;
            }

            switch (opcion) {
                case 1:
                    System.out.println("Ingrese la moneda base (ejemplo: USD):");
                    String monedaBase = teclado.nextLine().toUpperCase();

                    System.out.println("Ingrese la moneda de destino (ejemplo: EUR):");
                    String monedaDestino = teclado.nextLine().toUpperCase();


                    if (!monedasValidas.contains(monedaBase) || !monedasValidas.contains(monedaDestino)) {
                        System.out.println("Error: Uno o ambos códigos de moneda no son válidos. Por favor, consulte la lista de monedas soportadas.");
                        break;
                    }

                    System.out.println("Ingrese el valor que desea convertir:");

                    if (teclado.hasNextDouble()) {
                        double valorUsuario = teclado.nextDouble();
                        teclado.nextLine();

                        String url = "https://v6.exchangerate-api.com/v6/" + claveAPI + "/latest/" + monedaBase;
                        double tasa = ConversorApp.obtenerTasa(url, monedaDestino);

                        if (tasa > 0) {
                            double resultado = valorUsuario * tasa;
                            System.out.printf("El valor de %.2f %s corresponde al valor final de %.2f %s.\n", valorUsuario, monedaBase, resultado, monedaDestino);

                            Conversion nuevaConversion = new Conversion(monedaBase, monedaDestino, valorUsuario, resultado);
                            historial.add(nuevaConversion);
                        }
                    } else {
                        System.out.println("Entrada no válida. Por favor, ingrese un número para el valor.");
                        teclado.nextLine();
                    }
                    break;
                case 2:
                    System.out.println("\n*** Historial de Conversiones ***");
                    if (historial.isEmpty()) {
                        System.out.println("No hay conversiones en el historial aún.");
                    } else {
                        for (Conversion conv : historial) {
                            System.out.println(conv);
                        }
                    }
                    System.out.println("***********************************\n");
                    break;
                case 3:
                    System.out.println("Saliendo del programa, ¡gracias por usar el conversor!");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, elija una opción del menú.");
            }
        }
        teclado.close();
    }
}