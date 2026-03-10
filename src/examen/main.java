package examen;

import java.util.Scanner;

public class main {
    public static Scanner reader = new Scanner(System.in);
    public static char[][] sala = new char[6][10];

    public static void main(String[] args) {
        // Inicializar sala con 'L' (libres)
        for (int i = 0; i < sala.length; i++) {
            for (int j = 0; j < sala[i].length; j++) {
                sala[i][j] = 'L';
            }
        }
        
        int opc = 0;
        do {
            System.out.println("TEATRO\n" +
                    "1. Mostrar sala\n" +
                    "2. Reservar asiento suelto\n" +
                    "3. Reservar grupo en una fila\n" +
                    "4. Confirmar reservas (R -> O)\n" +
                    "5. Cancelar reservas (R -> L)\n" +
                    "6. Estadísticas\n" +
                    "7. Salir\n");
            opc = reader.nextInt();
            reader.nextLine(); // Limpiar buffer
            
            try {
                switch (opc) {
                    case 1:
                        pintarSala(sala);
                        break;
                    case 2:
                        reservarAsientoIndividual(sala);
                        break;
                    case 3:
                        reservarGrupoEnFila(sala);
                        break;
                    case 4:
                        confirmarReservas(sala);
                        break;
                    case 5:
                        cancelarReservas(sala);
                        break;
                    case 6:
                        mostrarEstadisticas(sala);
                        break;
                    case 7:
                        System.out.println("¡Gracias por usar el sistema!");
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage() + ". Vuelva a intentarlo.");
            }
            System.out.println();
        } while (opc != 7);
        reader.close();
    }

    // Muestra la sala con índices (1-based para usuario)
    public static void pintarSala(char[][] sala) {
        // Encabezado columnas (1-10)
        System.out.print("   ");
        for (int a = 1; a <= sala[0].length; a++) {
            System.out.print(a + " ");
        }
        System.out.println();
        
        // Filas
        for (int i = 0; i < sala.length; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < sala[i].length; j++) {
                System.out.print(sala[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    // Valida bounds 
    public static boolean posicionExiste(int fila, int col, char[][] sala) {
        return fila >= 0 && fila < sala.length && col >= 0 && col < sala[0].length;
    }

    // valida si asiento está libre ('L')
    public static boolean puedeReservar(char[][] sala, int fila, int col) {
        return posicionExiste(fila, col, sala) && sala[fila][col] == 'L';
    }

    // Reserva asiento individual con validación completa
    public static void reservarAsientoIndividual(char[][] sala) {
        pintarSala(sala);
        System.out.print("Fila (1-6): ");
        int fila = reader.nextInt() - 1;
        System.out.print("Columna (1-10): ");
        int col = reader.nextInt() - 1;
        reader.nextLine();
        
        if (!posicionExiste(fila, col, sala)) {
            System.out.println("Posición inválida.");
            return;
        }
        
        if (puedeReservar(sala, fila, col)) {
            sala[fila][col] = 'R';
            System.out.println("Asiento reservado correctamente.");
        } else {
            System.out.println("Asiento no disponible.");
        }
    }

    // Reserva grupo consecutivo en fila
    public static void reservarGrupoEnFila(char[][] sala) {
        pintarSala(sala);
        System.out.print("Fila (1-6): ");
        int fila = reader.nextInt() - 1;
        System.out.print("Número de asientos: ");
        int num = reader.nextInt();
        reader.nextLine();
        
        if (!posicionExiste(fila, 0, sala) || num <= 0 || num > 10) {
            System.out.println("Fila o número inválido.");
            return;
        }
        
        int inicio = -1, countLibres = 0;
        // Buscar primer bloque de num libres consecutivos
        for (int j = 0; j <= 10 - num; j++) {
            boolean valido = true;
            for (int k = 0; k < num; k++) {
                if (!puedeReservar(sala, fila, j + k)) {
                    valido = false;
                    break;
                }
            }
            if (valido) {
                inicio = j;
                break;
            }
        }
        
        if (inicio != -1) {
            for (int k = 0; k < num; k++) {
                sala[fila][inicio + k] = 'R';
            }
            System.out.println("Grupo reservado en fila " + (fila + 1) + ", columnas " + 
                             (inicio + 1) + "-" + (inicio + num) + ".");
        } else {
            System.out.println("No hay " + num + " asientos consecutivos libres en fila " + (fila + 1) + ".");
        }
    }

    // Cuenta estado en toda la sala
    public static int contarEstado(char[][] sala, char estado) {
        int count = 0;
        for (int i = 0; i < sala.length; i++) {
            for (int j = 0; j < sala[i].length; j++) {
                if (sala[i][j] == estado) count++;
            }
        }
        return count;
    }

    // Cuenta estado en fila específica 
    public static int contarEstadoFila(char[][] sala, int fila, char estado) {
        int count = 0;
        for (int j = 0; j < sala[fila].length; j++) {
            if (sala[fila][j] == estado) count++;
        }
        return count;
    }

    // Confirma todas reservas 'R' -> 'O'
    public static void confirmarReservas(char[][] sala) {
        int confirmados = 0;
        for (int i = 0; i < sala.length; i++) {
            for (int j = 0; j < sala[i].length; j++) {
                if (sala[i][j] == 'R') {
                    sala[i][j] = 'O';
                    confirmados++;
                }
            }
        }
        System.out.println("Confirmados " + confirmados + " asientos.");
    }

    // Cancela todas reservas 'R' -> 'L'
    public static void cancelarReservas(char[][] sala) {
        int cancelados = 0;
        for (int i = 0; i < sala.length; i++) {
            for (int j = 0; j < sala[i].length; j++) {
                if (sala[i][j] == 'R') {
                    sala[i][j] = 'L';
                    cancelados++;
                }
            }
        }
        System.out.println("Cancelados " + cancelados + " asientos.");
    }

    // Estadísticas completas 
    public static void mostrarEstadisticas(char[][] sala) {
        int libres = contarEstado(sala, 'L');
        int reservados = contarEstado(sala, 'R');
        int ocupados = contarEstado(sala, 'O');
        int total = sala.length * sala[0].length;
        
        System.out.println("Libres: " + libres);
        System.out.println("Reservados: " + reservados);
        System.out.println("Ocupados: " + ocupados);
        System.out.printf("Ocupación: %.1f%%\n", (ocupados * 100.0f / total));
        
        // Fila con más ocupados
        int maxOcup = 0, filaMax = -1;
        for (int i = 0; i < sala.length; i++) {
            int countFila = contarEstadoFila(sala, i, 'O');
            if (countFila > maxOcup) {
                maxOcup = countFila;
                filaMax = i + 1;
            }
        }
        if (filaMax != -1) {
            System.out.println("Fila con más ocupados: " + filaMax + " (" + maxOcup + ")");
        } else {
            System.out.println("No hay asientos ocupados.");
        }
        System.out.println();
    }
}
