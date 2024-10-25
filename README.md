He implementado las siguientes opciones:

Reiniciar partida: Ahora la opción de reiniciar partida, al darle click, vuelve todas las semillas a su sitio inicial y reinicia el marcador a 0.
Guardar partida: Ahora se puede guardar una partida en un archivo llamado partida.txt, que guarda las semillas en cada posición y el turno correspondiente.
Recuperar partida: Ahora, al dar click al símbolo de recuperar partida (flecha girando hacia atrás) se recupera de partida.txt la última partida guardada, 
para estas últimas dos tareas fueron implementados los métodos de serialize y deserialize.
Guardar puntuación: Implementado en Room, al terminar una partida, se guarda el resultado en una tabla llamada "puntuaciones". Bug: Toma varios clicks salir del diálogo del final y el marcador se guarda varias veces. 
Mejores resultados: Se muestran los 10 mejores resultados según los criterios especificados. Bug: El botón para eliminarlos aparece solo en vista vertical y la lista no se refresca una vez borrados de la base de datos. 

No fueron implementadas características opcionales. 
