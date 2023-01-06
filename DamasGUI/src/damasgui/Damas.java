package damasgui;

public class Damas {

	private String[][] tablero;
	private int contadorTurno;
//	private int turno = turno();
	private int contadorBlancas;
	private int contadorNegras;

	private static String[] aliadas = new String[2];
	private static String[] enemigas = new String[2];
	private static String pieza;
	private static boolean capturaEnCadena = false;

	public Damas() {
		contadorTurno = 0;
		contadorBlancas = 12;
		contadorNegras = 12;
		tablero = inicioTablero();
		aliadas();
		enemigas();
	}

	public String[][] getTablero(){
		return tablero;
	}
	
	public int getContadorBlancas() {
		return contadorBlancas;
	}
	
	public int getContadorNegras() {
		return contadorNegras;
	}
	
	private String[][] inicioTablero() {
		String[][] res = new String[8][8];

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				res[i][j] = "";
				String jugador = "";
				if (i < 3) {
					jugador = "*";
				} else if (i >= 5) {
					jugador = "o";
				}
				if (i % 2 == 0 && j % 2 != 0 || i % 2 != 0 && j % 2 == 0)
					res[i][j] = jugador;
			}
		}

		return res;
	}

	public int turno() {
		if (contadorTurno % 2 == 0) {
			return 1;
			
		} else {
			return -1;
		}

	}

	private void aliadas() {
		if (turno() == 1) {
			aliadas[0] = "o";
			aliadas[1] = "do";
		} else {
			aliadas[0] = "*";
			aliadas[1] = "d*";
		}
	}

	private void enemigas() {
		if (turno() == 1) {
			enemigas[0] = "*";
			enemigas[1] = "d*";
		} else {
			enemigas[0] = "o";
			enemigas[1] = "do";
		}
	}

	public boolean juego(int filaPos, int columnaPos, int filaMov, int columnaMov) {

		boolean res = false;
		
//		System.out.println("La pieza es correcta: " + piezaCorrecta(filaPos,columnaPos));
		// Compruebo que la pieza elegida y la posicion final son correctas
		if (piezaCorrecta(filaPos, columnaPos) && coordenadaCorrecta(filaMov, columnaMov)) {
			
			// si es dama
			if (pieza.equals(aliadas[0])) {
				// Compruebo que el movimiento es correcto
				boolean movimientoCorrecto = compruebaMovimientoDama(filaPos, columnaPos, filaMov, columnaMov);

				// Compruebo si hay o no una pieza en el movimiento para avanzar
				boolean hayPieza = hayPieza(filaMov, columnaMov);

				// Compruebo que hay una pieza enemiga delante
				boolean enemigoDelante = enemigoDelante(columnaPos, filaMov, columnaMov);

				// Compruebo que el movimiento para capturar es correcto
				boolean movimientoCaptura = movimientoCaptura(filaPos, columnaPos, filaMov, columnaMov);

				// Si delante no hay pieza, el movimiento es correcto y no hay captura en
				// cadena, puedo mover
				if (!hayPieza && movimientoCorrecto && !capturaEnCadena) {
					movimientoNormal(filaPos, columnaPos, filaMov, columnaMov);
					res = true;
					
					contadorTurno++;
				} else if (!hayPieza && enemigoDelante && movimientoCaptura) {
					capturo(filaPos, columnaPos, filaMov, columnaMov);

					capturaEnCadena = capturaEnCadena(filaMov, columnaMov);

					if (turno() == 1) {
						contadorNegras--;
					} else {
						contadorBlancas--;
					}

					if (!capturaEnCadena) {
						contadorTurno++;
					}
					res = true;
				} 

				// para la reina
			} else {
				// Compruebo que no hay otras piezas en el camino y el movimiento en la diagonal
				// es correcto
				boolean movimientoNormal = compruebaMovimientoNormalReina(filaPos, columnaPos, filaMov, columnaMov);

				// Compruebo si hay o no una pieza en el movimiento
				boolean hayPieza = hayPieza(filaMov, columnaMov);

				// Compruebo si el movimiento es correcto y hay 1 enemiga
				boolean comprueboMov = compruebaMovimientoCorrectoReina(filaPos, columnaPos, filaMov, columnaMov);
				
				// Si no hay pieza, el movimiento es correcto, no hay piezas en la diagonal y no hay captura en cadena, puedo mover
				if (!hayPieza && movimientoNormal && !capturaEnCadena) {
					
					movimientoReina(filaPos, columnaPos, filaMov, columnaMov);
					contadorTurno++;
					res = true;
					
				// Si no hay pieza, el movimiento es correcto y hay un enemigo solo
				} else if (!hayPieza && comprueboMov) {
					
					// Capturo con dama
					capturaConDama(filaPos, columnaPos, filaMov, columnaMov);
					// Compruebo si hay captura en cadena
					capturaEnCadena = capturaEnCadenaReina(filaMov, columnaMov);

					// Actualizo el contador de piezas
					if (turno() == 1) {
						contadorNegras--;
					} else {
						contadorBlancas--;
					}
					
					if (!capturaEnCadena) {
						contadorTurno++;
					}
					res = true;
				}
			}
		} 
		aliadas();
		enemigas();
		return res;
	}

	public boolean capturo(int filaPos, int columnaPos, int filaMov, int columnaMov) {

		if (filaMov == 0 && pieza.equals("o")) {
			pieza = "do";
		} else if (filaMov == 7 && pieza.equals("*")) {
			pieza = "d*";
		}

		int columnaEnemigo;
		if (columnaPos < columnaMov) {
			columnaEnemigo = columnaMov - 1;
		} else {
			columnaEnemigo = columnaMov + 1;
		}

		tablero[filaPos][columnaPos] = "";
		tablero[(filaMov + (1 * turno()))][columnaEnemigo] = "";
		tablero[filaMov][columnaMov] = pieza;

		return true;
	}

	public boolean piezaCorrecta(int filaPos, int columnaPos) {
		boolean correcta = false;
		pieza = tablero[filaPos][columnaPos];
		// Primero compruebo que la pieza ea es aliada
		if (compruebaPieza(pieza)) {

			// Compruebo que las damas no están encerradas
			if (pieza.equals(aliadas[0]) && !encerrada(filaPos, columnaPos)) {
				
				correcta = true;
			} else if (pieza.equals(aliadas[1])) {
				correcta = true;
			}
		}
		return correcta;
	}

	private boolean compruebaPieza(String pieza) {

//		boolean correcta = false;

		boolean dama = pieza.equals(aliadas[0]);
		boolean reina = pieza.equals(aliadas[1]);
		
		System.out.println("Dama: " + dama);
		System.out.println("Reina: " + reina);
		// Si la pieza es dama o reina aliada, devuelvo true
		if (dama || reina) {
			return true;
		} else {
			return false;
		}

//		return correcta;
	}

	private boolean encerrada(int filaPos, int columnaPos) {

		// Una pieza está encerrada si en las casillas adyacentes hay aliadas o dos
		// enemigas
		boolean encerrada = false;
		int turno = turno();
		switch (columnaPos) {
		case 0:

			encerrada = (tablero[filaPos - (1 * turno)][columnaPos + 1].equals(aliadas[0])
					|| tablero[filaPos - (1 * turno)][columnaPos + 1].equals(aliadas[1]))
					|| (hayPieza(filaPos - (1 * turno), columnaPos + 1)
							&& hayPieza(filaPos - (2 * turno), columnaPos + 2));
			break;
		case 1:
			encerrada = ((tablero[filaPos - (1 * turno)][columnaPos + 1].equals(aliadas[0])
					|| tablero[filaPos - (1 * turno)][columnaPos + 1].equals(aliadas[1]))
					&& (hayPieza(filaPos - (1 * turno), 0)))
					|| (hayPieza(filaPos - (1 * turno), columnaPos + 1)
							&& hayPieza(filaPos - (2 * turno), columnaPos + 2) && hayPieza(filaPos - (1 * turno), 0));
			break;

		case 6:
			encerrada = ((tablero[filaPos - (1 * turno)][columnaPos - 1].equals(aliadas[0])
					|| tablero[filaPos - (1 * turno)][columnaPos - 1].equals(aliadas[1]))
					&& (hayPieza(filaPos - (1 * turno), 7)))
					|| (hayPieza(filaPos - (1 * turno), columnaPos - 1)
							&& hayPieza(filaPos - (2 * turno), columnaPos - 2) && hayPieza(filaPos - (1 * turno), 7));
			break;

		case 7:
			encerrada = (tablero[filaPos - (1 * turno)][columnaPos - 1].equals(aliadas[0])
					|| tablero[filaPos - (1 * turno)][columnaPos - 1].equals(aliadas[1]))
					|| (hayPieza(filaPos - (1 * turno), columnaPos - 1)
							&& hayPieza(filaPos - (2 * turno), columnaPos - 2));
			break;

		default:
			encerrada = ((tablero[filaPos - (1 * turno)][columnaPos + 1].equals(aliadas[0])
					|| tablero[filaPos - (1 * turno)][columnaPos + 1].equals(aliadas[1]))
					&& (tablero[filaPos - (1 * turno)][columnaPos - 1].equals(aliadas[0])
							|| tablero[filaPos - (1 * turno)][columnaPos - 1].equals(aliadas[1])))
					|| (hayPieza(filaPos - (1 * turno), columnaPos + 1)
							&& hayPieza(filaPos - (2 * turno), columnaPos + 2)
							&& hayPieza(filaPos - (1 * turno), columnaPos - 1)
							&& hayPieza(filaPos - (2 * turno), columnaPos - 2));
			break;
		}
		
		System.out.println("Encerrada: " + encerrada);
		return encerrada;

	}

	private boolean hayPieza(int i, int j) {
		if (!tablero[i][j].equals(""))
			return true;
		else
			return false;
	}

	public boolean coordenadaCorrecta(int fila, int columna) {

		boolean f = fila >= 0 && fila <= 7;
		boolean c = columna >= 0 && columna <= 7;

		return f && c;
	}

	public boolean movimientoNormal(int filaPos, int columnaPos, int filaMov, int columnaMov) {

		if (filaMov == 0 && pieza.equals("o")) {
			pieza = "do";
		} else if (filaMov == 7 && pieza.equals("*")) {
			pieza = "d*";
		}

		tablero[filaMov][columnaMov] = pieza;
		tablero[filaPos][columnaPos] = "";

		return true;
	}

	public boolean compruebaMovimientoDama(int filaPos, int columnaPos, int filaMov, int columnaMov) {

		boolean correcto = false;

		// El movimiento es correcto si la fila es la adyacente y la columna es -1 o +1
		// que la anterior
		boolean movFila = (filaPos == filaMov + (1 * turno()));
		boolean movColumna = (columnaPos + 1 == columnaMov) || (columnaPos - 1 == columnaMov);

		if (movFila && movColumna) {
			correcto = true;
		}

		return correcto;
	}

	public boolean enemigoDelante(int columnaPos, int filaMov, int columnaMov) {

		boolean enemigoDelante = false;
		int turno = turno();
		int columnaEnemigo;
		if (columnaPos < columnaMov) {
			columnaEnemigo = columnaMov - 1;
		} else {
			columnaEnemigo = columnaMov + 1;
		}

		boolean dama = tablero[(filaMov + (1 * turno))][columnaEnemigo].equals(enemigas[0]);
		boolean reina = tablero[(filaMov + (1 * turno))][columnaEnemigo].equals(enemigas[1]);

		if (dama || reina) {
			enemigoDelante = true;
		}

		return enemigoDelante;
	}

	public boolean movimientoCaptura(int filaPos, int columnaPos, int filaMov, int columnaMov) {

		boolean correcto = false;

		// El movimiento es correcto si la fila es la adyacente y la columna es -1 o +1
		// que la anterior
		boolean movFila = (filaPos == filaMov + (2 * turno()));
		boolean movColumna = (columnaPos + 2 == columnaMov) || (columnaPos - 2 == columnaMov);

		if (movFila && movColumna) {
			correcto = true;
		}

		return correcto;
	}

	public boolean capturo(int filaPos, int columnaPos, int filaMov, int columnaMov, String pieza) {

		if (filaMov == 0 && pieza.equals("o")) {
			pieza = "do";
		} else if (filaMov == 7 && pieza.equals("*")) {
			pieza = "d*";
		}

		int columnaEnemigo;
		if (columnaPos < columnaMov) {
			columnaEnemigo = columnaMov - 1;
		} else {
			columnaEnemigo = columnaMov + 1;
		}

		tablero[filaPos][columnaPos] = "";
		tablero[(filaMov + (1 * turno()))][columnaEnemigo] = "";
		tablero[filaMov][columnaMov] = pieza;

		return true;
	}

	public boolean capturaEnCadena(int filaMov, int columnaMov) {

		boolean capturaEnCadena;

		int filaMovSiguiente = filaMov - (2 * turno());
		int filaEnemigo = filaMov - (1 * turno());

		int columnaEnemigoDer = columnaMov + 1;
		int columnaEnemigoIzq = columnaMov - 1;

		int columnaMovDer = columnaMov + 2;
		int columnaMovIzq = columnaMov - 2;

		boolean casillaLibreDer = false;
		boolean casillaLibreIzq = false;

		boolean enemigoIzq = false;
		boolean enemigoDer = false;

		if (filaMovSiguiente >= 0 && filaMovSiguiente <= 7) {

			if (columnaMovDer <= 7) {
				casillaLibreDer = !hayPieza(filaMovSiguiente, columnaMovDer);
				enemigoDer = tablero[filaEnemigo][columnaEnemigoDer].equals(enemigas[0])
						|| tablero[filaEnemigo][columnaEnemigoDer].equals(enemigas[1]);
			}
			if (columnaMovIzq >= 0) {
				casillaLibreIzq = !hayPieza(filaMovSiguiente, columnaMovIzq);
				enemigoIzq = tablero[filaEnemigo][columnaEnemigoIzq].equals(enemigas[0])
						|| tablero[filaEnemigo][columnaEnemigoIzq].equals(enemigas[1]);
			}
		}

		capturaEnCadena = (casillaLibreDer && enemigoDer) || (casillaLibreIzq && enemigoIzq);

		return capturaEnCadena;
	}

	public boolean compruebaMovimientoCorrectoReina(int filaPos, int columnaPos, int filaMov, int columnaMov) {

		boolean correcto = false;
		int cuentaEnemigos = 0;

		int fila = filaPos;
		int columna = columnaPos;

		// Arriba
		if (filaPos > filaMov) {

			// Derecha
			if (columnaPos < columnaMov) {
				fila--;
				columna++;
				while (fila > filaMov) {
					if (tablero[fila][columna].equals(enemigas[0]) || tablero[fila][columna].equals(enemigas[1])) {
						cuentaEnemigos++;
					}

					fila--;
					columna++;
				}
				// Izquierda
			} else {
				fila--;
				columna--;

				while (fila > filaMov) {
					if (tablero[fila][columna].equals(enemigas[0]) || tablero[fila][columna].equals(enemigas[1])) {
						cuentaEnemigos++;
					}
					fila--;
					columna--;
				}
			}

			// Abajo
		} else {

			// Derecha
			if (columnaPos < columnaMov) {
				fila++;
				columna++;
				while (fila < filaMov) {
					if (tablero[fila][columna].equals(enemigas[0]) || tablero[fila][columna].equals(enemigas[1])) {
						cuentaEnemigos++;
					}
					fila++;
					columna++;
				}

				// Izquierda
			} else {
				fila++;
				columna--;
				while (fila < filaMov) {
					if (tablero[fila][columna].equals(enemigas[0]) || tablero[fila][columna].equals(enemigas[1])) {
						cuentaEnemigos++;
					}
					fila++;
					columna--;
				}
			}
		}

		if (fila == filaMov && columna == columnaMov && cuentaEnemigos == 1) {
			correcto = true;
		}

		return correcto;
	}

	public boolean compruebaMovimientoNormalReina(int filaPos, int columnaPos, int filaMov, int columnaMov) {
		boolean correcto = false;
		boolean vacia = true;

		int fila = filaPos;
		int columna = columnaPos;
		// Compruebo que las casillas están vacías desde la posición de la dama a la
		// casilla elegida
		// Arriba
		if (filaPos > filaMov) {

			// Derecha
			if (columnaPos < columnaMov) {
				fila--;
				columna++;
				while (fila > filaMov) {

					if (hayPieza(fila, columna)) {
						vacia = false;
					}
					fila--;
					columna++;
				}
				// Izquierda
			} else {
				fila--;
				columna--;

				while (fila > filaMov) {

					if (hayPieza(fila, columna)) {
						vacia = false;
					}
					fila--;
					columna--;
				}
			}

			// Abajo
		} else {

			// Derecha
			if (columnaPos < columnaMov) {
				fila++;
				columna++;
				while (fila < filaMov) {

					if (hayPieza(fila, columna)) {
						vacia = false;
					}
					fila++;
					columna++;
				}

				// Izquierda
			} else {
				fila++;
				columna--;
				while (fila < filaMov) {

					if (hayPieza(fila, columna)) {
						vacia = false;
					}
					fila++;
					columna--;
				}
			}
		}

		// Si las casillas están vacías y el movimiento en la diagonal es correcto,
		// devuelvo true
		if (vacia && fila == filaMov && columna == columnaMov) {
			correcto = true;
		}

		return correcto;
	}

	public boolean movimientoReina(int filaPos, int columnaPos, int filaMov, int columnaMov) {

		tablero[filaPos][columnaPos] = "";
		tablero[filaMov][columnaMov] = pieza;

		return true;
	}

	public boolean buscaEnemigoSolo(int filaPos, int columnaPos, int filaMov, int columnaMov) {

		boolean enemigoSolo = false;

		int fila = filaPos;
		int columna = columnaPos;

		// Arriba
		if (filaPos > filaMov) {

			// Derecha
			if (columnaPos < columnaMov) {
				fila--;
				columna++;
				while (!enemigoSolo && fila >= filaMov) {
					if ((tablero[fila][columna].equals(enemigas[0]) || tablero[fila][columna].equals(enemigas[1]))
							&& !hayPieza(fila -= 1, columna += 1)) {
						enemigoSolo = true;

					}
					fila--;
					columna++;
				}
				// Izquierda
			} else {
				fila--;
				columna--;
				while (!enemigoSolo && fila >= filaMov) {

					if ((tablero[fila][columna].equals(enemigas[0]) || tablero[fila][columna].equals(enemigas[1]))
							&& !hayPieza(fila -= 1, columna -= 1)) {
						enemigoSolo = true;

					}

					fila--;
					columna--;
				}
			}

			// Abajo
		} else {

			// Derecha
			if (columnaPos < columnaMov) {
				fila++;
				columna++;
				while (!enemigoSolo && fila <= filaMov) {

					if ((tablero[fila][columna].equals(enemigas[0]) || tablero[fila][columna].equals(enemigas[1]))
							&& !hayPieza(fila += 1, columna += 1)) {
						enemigoSolo = true;

					}
					fila++;
					columna++;
				}

				// Izquierda
			} else {
				fila++;
				columna--;
				while (!enemigoSolo && fila <= filaMov) {
					if ((tablero[fila][columna].equals(enemigas[0]) || tablero[fila][columna].equals(enemigas[1]))
							&& !hayPieza(fila += 1, columna -= 1)) {
						enemigoSolo = true;

					}
					fila++;
					columna--;
				}
			}
		}

		return enemigoSolo;
	}

	public boolean capturaEnCadenaReina(int filaPos, int columnaPos) {
		boolean capturaEnCadena = false;

		boolean arribaIzq = false;
		boolean arribaDer = false;
		boolean abajoIzq = false;
		boolean abajoDer = false;

		if (filaPos <= 1) {

			if (columnaPos <= 1) {
				abajoDer = buscaEnemigoSolo(filaPos, columnaPos, 7 - columnaPos - 1, 7 - filaPos - 1);
			} else if (columnaPos >= 6) {
				abajoIzq = buscaEnemigoSolo(filaPos, columnaPos, 6, 1);
			} else {
				abajoDer = buscaEnemigoSolo(filaPos, columnaPos, 7 - columnaPos - 1, 6);
				abajoIzq = buscaEnemigoSolo(filaPos, columnaPos, columnaPos - 1, 1);
			}

		} else if (filaPos >= 6) {

			if (columnaPos <= 1) {
				arribaDer = buscaEnemigoSolo(filaPos, columnaPos, 1, 6);
			} else if (columnaPos >= 6) {
				arribaIzq = buscaEnemigoSolo(filaPos, columnaPos, 7 - columnaPos + 1, 7 - filaPos + 1);
			} else {
				arribaDer = buscaEnemigoSolo(filaPos, columnaPos, columnaPos + 1, 6);
				arribaIzq = buscaEnemigoSolo(filaPos, columnaPos, 7 - columnaPos + 1, 1);
			}

		} else {
			if (columnaPos <= 1) {
				arribaDer = buscaEnemigoSolo(filaPos, columnaPos, 1, filaPos - 1);
				abajoDer = buscaEnemigoSolo(filaPos, columnaPos, 6, 7 - filaPos - 1);
			} else if (columnaPos >= 6) {
				arribaIzq = buscaEnemigoSolo(filaPos, columnaPos, 1, 7 - filaPos + 1);
				abajoIzq = buscaEnemigoSolo(filaPos, columnaPos, 6, filaPos + 1);
			} else {
				arribaIzq = buscaEnemigoSolo(filaPos, columnaPos, 1, 1);
				arribaDer = buscaEnemigoSolo(filaPos, columnaPos, 1, 6);
				abajoIzq = buscaEnemigoSolo(filaPos, columnaPos, 6, 1);
				abajoDer = buscaEnemigoSolo(filaPos, columnaPos, 6, 6);
			}
		}

		capturaEnCadena = arribaDer || arribaIzq || abajoDer || abajoIzq;

		return capturaEnCadena;
	}

	public boolean capturaConDama(int filaPos, int columnaPos, int filaMov, int columnaMov) {

		boolean captura = false;

		int fila = filaPos;
		int columna = columnaPos;

		// Arriba
		if (filaPos > filaMov) {

			// Derecha
			if (columnaPos < columnaMov) {
				fila--;
				columna++;

				while (!captura) {

					if (hayPieza(fila, columna)) {

						captura = true;
						tablero[filaPos][columnaPos] = "";
						tablero[fila][columna] = "";
						tablero[filaMov][columnaMov] = pieza;
					}
					fila--;
					columna++;

				}

				// Izquierda
			} else {
				fila--;
				columna--;
				while (!captura) {
					if (hayPieza(fila, columna)) {
						captura = true;
						tablero[filaPos][columnaPos] = "";
						tablero[fila][columna] = "";
						tablero[filaMov][columnaMov] = pieza;
					}

					fila--;
					columna--;
				}
			}

			// Abajo
		} else {

			// Derecha
			if (columnaPos < columnaMov) {
				fila++;
				columna++;
				while (!captura) {
					if (hayPieza(fila, columna)) {
						captura = true;
						tablero[filaPos][columnaPos] = "";
						tablero[fila][columna] = "";
						tablero[filaMov][columnaMov] = pieza;
					}
					fila++;
					columna++;
				}

				// Izquierda
			} else {
				fila++;
				columna--;
				while (!captura) {
					if (hayPieza(fila, columna)) {
						captura = true;
						tablero[filaPos][columnaPos] = "";
						tablero[fila][columna] = "";
						tablero[filaMov][columnaMov] = pieza;
					}
					fila++;
					columna--;
				}
			}
		}

		return true;
	}

}
