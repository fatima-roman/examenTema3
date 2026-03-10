package examen;

import java.util.Arrays;
import java.util.Scanner;
import java.util.spi.CalendarNameProvider;

public class main {
	public static Scanner reader = new Scanner(System.in);
	public static char[][] sala = new char[6][10];
	
	public static void main(String[] args) {
		//Crear la sala con los asientos por defecto libres 
		//char[][] sala = new char[6][10];
		for (int i = 0; i < sala.length; i++) {
			for (int j =0; j < sala[i].length; j++) {
				sala[i][j] = 'L';
			}
		}
		int opc =0;
		do {
			System.out.println("TEATRO\r\n"
					+ "1. Mostrar sala\r\n"
					+ "2. Reservar asiento suelto\r\n"
					+ "3. Reservar grupo en una fila\r\n"
					+ "4. Confirmar reservas (R -> O)\r\n"
					+ "5. Cancelar reservas (R -> L)\r\n"
					+ "6. Estadísticas\r\n"
					+ "7. Salir\r\n");
			opc = reader.nextInt();
			try {
				switch (opc) {
				case 1: {
					pintarSala(sala);
					break;
				}
				case 2: {
					boolean bandera = false;
					pintarSala(sala);
					while(!bandera) {
						System.out.println("Introduce el número de fila: ");
						int fila = reader.nextInt();
						System.out.println("Introduce el número de columna: ");
						int columna = reader.nextInt();
						if (!esPosicionValida(sala, fila -1, columna-1)) {
							System.out.println("u");
							reservarAsiento(sala, fila-1, columna-1);
						}else {
							bandera= true; ;
						}
						if (reservarAsiento(sala, fila-1, columna-1)) {
							bandera= true; 
							System.out.println("Asiento reservado.");
						}
					}
	
					break;
				}
				case 3: {
					boolean bandera = false;
					pintarSala(sala);
					System.out.println("Introduce el número de fila: ");
					int fila = reader.nextInt();
					System.out.println("Introduce el número de asientos: ");
					int columna = reader.nextInt();
					if (reservarGrupoEnFila(sala, fila-1, columna-1) != null) bandera = true ;
					break;
				}
				case 4: {
					confirmarReservas(sala);
					break;
				}
				case 5: {
					cancelarReservas(sala);
					break;
				}
				case 6: {
					mostrarEstadisticas(sala);
					break;
				}
				};
			}catch (Exception e) {
				System.out.println("Vuelva a intentarlo, error: " +e );
			}
			
			System.out.println();
		}while(opc != 7);
		

	}//Cierre main
	
	
	//Muestra por consola la sala con el número que corresponde al inicio de cada fila y columna 
	public static void pintarSala (char[][] sala) {
		for (int a =0; a< sala[1].length+1;a++) {
			System.out.print(a + " ");
		}
		System.out.println();
		for (int i = 0; i < sala.length; i++) {
			System.out.print((i+1) + " ");
			for (int j =0; j < sala[i].length; j++) {
				System.out.print(sala[i][j] + " ");
			}
			System.out.println();
		}
	}//Cierre pintarSala
	
	
	//Comprueba si la posición introducida existe en sala 
	public static boolean esPosicionValida(char[][] sala, int fila, int columna) {
		if (sala[fila][columna] == 'R') return true;
		if (sala[fila][columna] == 'L') return true;
		if (sala[fila][columna] == 'O') return true;
		try {
			char c = sala[fila][columna];
			return true;
		}catch (Exception e) {
			return false;
		}
	} //Cierre esPosicionValida
	
	
	//Cuenta cuantos asientos en 'L', 'R' u 'O' hay en la sala
	public static int contarEstado (char[][] sala, char estado) {
		int count =0; 
		for (int i = 0; i < sala.length; i++) {
			for (int j =0; j < sala[i].length; j++) {
				if (sala[i][j] == estado) count++;
			} 
		}
		return count;
	}//Cierre contarEstado
	
	//Comprobación para luego de cambiar el estado del asiento 
	public static boolean reservarAsiento(char[][] sala, int fila, int columna) {
				
		if (sala[fila][columna] == 'L') {
			sala[fila][columna] = 'R';
			return true;
		}else {
			return false;
		} 

	}//cierre reservarAsiento
	
	
	/**
	 * Comprueba que esten libres los asientos solicitados en la misma fila
	 *	Asigna el valor al Array que se devolverá según el valor que tome el contador en cada momento
	 */
	public static int [] reservarGrupoEnFila (char[][] sala, int fila, int numPersonas) {
		int[] libre = new int [2];
		int count =0; 
		for (int i = 0; i < sala[fila].length; i++) {
			 if (sala[fila][i] == 'L') {
				 if (count ==0 ) {
					 libre[0] = i;
				 }
				 count++;
				 if(count == numPersonas) {
					 libre[1] = i+2;
					 break;
				 }
				 
			 }else if (count < numPersonas){
				 libre[0] = 0;
				 count =0;
			 }
		}
		

		for (int i = libre[0]; i < libre[1]; i++) {
			if (esPosicionValida(sala, fila, i)) {
				reservarAsiento(sala, fila, i);
			}
		}
		return libre;

	
	} //Cierre reservarGrupoEnFila
	
	
	
	//Cambia las posiciones en reserva 'R' por posiciones ocupadas 'O'
	public static void confirmarReservas(char[][] sala) {
		for (int i = 0; i < sala.length; i++) {
			for (int j =0; j < sala[i].length; j++) {
				if (sala[i][j] == 'R') sala[i][j] = 'O';
			} 
		}
	}//Cierre confirmarReservas
	
	//Cambia las posiciones en reserva 'R' por posiciones libre 'L'
	public static void cancelarReservas(char[][] sala) {
		for (int i = 0; i < sala.length; i++) {
			for (int j =0; j < sala[i].length; j++) {
				if (sala[i][j] == 'R') sala[i][j] = 'L';
			} 
		}
	}//Cierre cancelarReservas

	//Muestra la ocupación de la sala 
	public static void mostrarEstadisticas(char[][] sala) {
		//Cuenta el número de asientos en cada condición 
		int count=0, countMayor = 0, fila = 0;
		System.out.println("Hay " + contarEstado(sala, 'R') + " asientos reservados");
		System.out.println("Hay " + contarEstado(sala, 'L') + " asientos libres");
		System.out.println("Hay " + contarEstado(sala, 'O') + " asientos ocupados");

		
		//Calcula el porcentaje de ocupación 
		float porcantajeOcupado = ((contarEstado(sala, 'O') / (sala.length + sala[1].length))*100.0f);
		System.out.println("La opcupación esta al " + porcantajeOcupado + "%" );
		
		//Cuenta la fila con más ocupados
		for (int i = 0; i < sala.length; i++) {
			for (int j =0; j < sala[i].length; j++) {
				if (sala[i][j] == 'O') count++;
			} 
			if (count > countMayor) {
				countMayor = count; 
				fila = i;
			}else {
				count = 0;
				fila = 0;
			}
		}
		System.out.println("La fila con más ocupados es: " + fila);
		
	}//mostrarEstadisticas

}
