// biblioteca para entrada de usuario.
import java.util.Scanner;

// Clase curva
public class Curva {

	long a;
	long b;
	long mod;

	// Constructor que representa la curva elíptica y² = x³+ax + b (mod n) 
	public Curva(long a,long b,long mod){
		this.a 	 = a;
		this.b 	 = b;
		this.mod = mod;
	}

	// Regresa un punto sobre la curva.
	long[] getPunto(){
		long temp;
		for (long x = 0;x<=this.mod ; x++ ) {
			temp = ((x*x*x) + (x*this.a) + this.b);
			// Buscamos la y que satisface la ecuación.
			for (long y = 0;y<=this.mod ; y++ ) {
				if ((y*y)%this.mod == temp%this.mod) {
					return new long[] {x%this.mod,y%this.mod};
				}
			}
		}
		// Si no encontró nada.
		return null;
	}

	// Suma dos puntos de la curva elíptica.
	long[] sum (long x1,long y1,long x2,long y2) {
		// Si tienen la misma coordena en x.
		if (x1 == x2) return sum_equals(x1,y1,x2,y2);
		// Sino.
		long den = (x2-x1);
		long [] aux = gcd(den,this.mod);
		long mcd = aux[0];
		den = aux[1] ;
		// Si no son primos relativos.
		if (mcd != 1) {
			System.out.println("No es primo, sus factores: " + mcd +","+  (this.mod/mcd));
			return null;
		}
		long nom = y2-y1;
		long s = (nom*den)%this.mod;
		long x3 = (s*s) - x1 -x2;
		long y3 = s*(x1-x3) - y1;
		// Sacamos módulo.
		x3 = modulo(x3);
		y3 = modulo(y3);
		return new long[]{x3,y3};
	}

	// Suma dos puntos con la misma coordeana en x.
	long[] sum_equals (long x1,long y1,long x2,long y2){
		long den = 2*y1;
		long [] aux = gcd(den,this.mod);
		long mcd = aux[0];
		den = aux[1] ;
		// Si no son primos relativos.
		if (mcd != 1) {
			System.out.println("No es primo, sus factores: " + mcd +","+  (this.mod/mcd));
			return null;
		}
		long nom = (3*x1*x1) + this.a;
		long s = (nom*den)%this.mod;
		long x3 = (s*s) - (2*x1);
		long y3 = s*(x1-x3) - y1;
		// Sacamos módulo
		x3 = modulo(x3);
		y3 = modulo(y3);

		return new long[]{x3,y3};
	}

	// Algoritmo extendido de Euclides.
	long[] gcd(long a, long b) {
		if (a < 0) a = modulo(a);
		if (b < 0) b = modulo(b);	
		// System.out.println("Recibe " + a + "," + b);
        long x = 0, y = 1, lastx = 1, lasty = 0, temp;
        while (b != 0)
        {
            long q = a / b;
            long r = a % b;
 
            a = b;
            b = r;
 
            temp = x;
            x = lastx - q * x;
            lastx = temp;
 
            temp = y;
            y = lasty - q * y;
            lasty = temp;            
        }
        // Regresamos el mcd y el inverso multiplicativo.
        return new long[] {a,lastx};
    }

	// Itera un punto sobre la curva elíptica regresa true si encontró los factores primos.
	boolean iteraPunto (){
		long[] punto = getPunto();
		punto = new long[] {0,1};
		long[] punto_aux = getPunto();
		punto_aux = new long[] {0,1};
		boolean encontroNumero = false;
		int contador = 0;
		while (contador <= this.mod){
			punto = sum(punto[0],punto[1],punto_aux[0],punto_aux[1]);
			if (punto == null) {
				encontroNumero = true;
				break;
			}

			contador++;
		}

		return encontroNumero;
	}

	// Función auxiliar para evitar números negativos.
	long modulo (long n) {
		long aux = n%this.mod;
		while (aux < 0) {
			aux+=this.mod;
		}

		return aux;
	}

	static void principal(){
		System.out.println("Ingresa el número que deseas factorizar. Si quieres salir, ingresa 0");
		Scanner s = new Scanner(System.in);
		long n = 0;
		try {
			n = s.nextLong();
			if (n == 0) {
				System.out.println("Adiós.");
				return;
			}
		} catch (Exception e) {
			System.out.println("Lo que ingresaste no es válido, intenta de nuevo.");
			// Llamada recursiva.
			principal();
		}
		//  y² = x³ + 2x + 7
		Curva c = new Curva (2,7,n);
		// Si no encontró factorización con la curva, intentamos con otra.
		if (!c.iteraPunto()){
			System.out.println("Buscando con otra curva...");
			//  y² = x³ − 20x + 21 
			c = new Curva (-20,21,n);
			c.iteraPunto();
		}
		// Llamada recursiva.
		principal();
	} 

	public static void main(String[] args) {
		principal();
	}
}