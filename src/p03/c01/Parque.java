package src.p03.c01;

import java.util.Enumeration;
import java.util.Hashtable;

public class Parque implements IParque{

	private int contadorPersonasTotales;
	private Hashtable<String, Integer> contadoresPersonasPuerta;
	private static final int aforoMaximo = 50;
	
	public Parque() {	
		contadorPersonasTotales = 0;
		contadoresPersonasPuerta = new Hashtable<String, Integer>();
		
	}


	@Override
	public synchronized void entrarAlParque(String puerta){
		
		//Comprobamos que el parque no se encuentre lleno.
		comprobarAntesDeEntrar();
		// Si no hay entradas por esa puerta, inicializamos
		if (contadoresPersonasPuerta.get(puerta) == null){
			contadoresPersonasPuerta.put(puerta, 0);
		}		
		// Aumentamos el contador total y el individual
		contadorPersonasTotales++;		
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta)+1);
		
		// Imprimimos el estado del parque
		imprimirInfo(puerta, "Entrada");
		this.notifyAll();
		checkInvariante();
		
	}
	
	
	@Override
	public synchronized void salirDelParque(String puerta) {
		
		//Comprobamos que exita alguien que pueda salir del parque.
		comprobarAntesDeSalir();
		// Si no hay salida por esa puerta, inicializamos
		if (contadoresPersonasPuerta.get(puerta) == null){
			contadoresPersonasPuerta.put(puerta, 0);
		}		
		// Disminuimos el contador total y aumentamos el individual
		contadorPersonasTotales--;		
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta)-1);	
		// Imprimimos el estado del parque
		imprimirInfo(puerta, "Salida");
		this.notifyAll();
		checkInvariante();
		
	}
	
	private void imprimirInfo (String puerta, String movimiento){
		System.out.println(movimiento + " por puerta " + puerta);
		System.out.println("--> Personas en el parque " + contadorPersonasTotales);
		// Iteramos por todas las puertas e imprimimos sus entradas y salidas
		for(String p: contadoresPersonasPuerta.keySet()){
			System.out.println("----> Por puerta " + p + " " + contadoresPersonasPuerta.get(p));
		}
		System.out.println(" ");
	}
	
	private int sumarContadoresPuerta() {
		int sumaContadoresPuerta = 0;
			Enumeration<Integer> iterPuertas = contadoresPersonasPuerta.elements();
			while (iterPuertas.hasMoreElements()) {
				sumaContadoresPuerta += iterPuertas.nextElement();
			}
		return sumaContadoresPuerta;
	}
	
	protected void checkInvariante() {
		assert sumarContadoresPuerta() == contadorPersonasTotales : "INV: La suma de contadores de las puertas debe ser igual al valor del contador del parte";
		assert contadorPersonasTotales < aforoMaximo : "INV: El parque supera el aforo maximo";
		assert contadorPersonasTotales > 0 : "INV: El parque esta vacio";
	}

	protected void comprobarAntesDeEntrar(){
        while(contadorPersonasTotales >= aforoMaximo) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void comprobarAntesDeSalir(){
        while(contadorPersonasTotales <= 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }





}
