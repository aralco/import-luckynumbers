Adjunto los dos shell scripts para eliminar los archivos .out y .in
respectivamente del servidor de aplicaciones WebLogic en una fecha
determinada. El caso de uso sugerido es el siguiente:

Para archivos .out desde la línea de comandos, eliminará los archivos
.out a las 3AM cada 15 de mes:

$0 3 15 * * removeOutFilesFromServer.sh

Para archivos .in desde la línea de comandos, eliminará los archivos
.in a las 3AM cada 15 de mes:

$0 3 15 * *  removeInFilesFromServer.sh

En ambos casos previamente se debe editar los shell scripts para coincidir
con su ambiente. Previo a la ejecución de los scripts mencionados se
entiende que el administrador eliminará los archivos .out del servidor FTP
de acuerdo a políticas definidas internamente.
