package es.deusto.prog3.cap00.ejercicios.edicionSprites;

/** Ejercicio de creación de ventanas complejas
 * En este mismo paquete verás un fichero 
 *   <a href="file:./ej-sprites-1.jpg">ej-sprites-1.jpg</a>
 * con una propuesta de ventana. Diseña con las variantes que veas oportuna esa ventana (en principio, sin programarla).<br/>
 * Hay otro fichero con marcas:
 *   <a href="file:./ej-sprites-2.jpg">ej-sprites-2.jpg</a>
 * sobre el que puedes observar los comentarios de cada parte de la ventana. Lo que se pretende es:<br/>
 *  - Objetivo de la ventana: editar y visualizar animaciones de sprites partiendo de gráficos ya existentes<br/>
 *  - Funcionamiento: al pulsar el botón (2) aparecerá una selección de ficheros (JFileChooser) que permitirá
 *    seleccionar una CARPETA cualquiera del disco <br/>
 *  - Los ficheros png/jpg/gif [recomendado pngs transparentes] que estén en esa carpeta aparecerán en (1) <br/>
 *  - (1) es un JList con desplazamiento (dentro de un JScrollPane) donde aparecerán los nombres de ficheros existentes en la carpeta <br/>
 *  - Entre el botón (2) y la lista (1) aparece el nombre de la carpeta seleccionada (JLabel)<br/>
 *  - Al cambiar la carpeta se crea una selección nueva vacía (lista (5)) <br/>
 *  - Al hacer doble click en un fichero de (1) se añade al final de la lista (5) <br/>
 *  - Al seleccionar un fichero en (5) se ve en el panel de preview (3), centrado con respecto al panel <br/>
 *  - Los componentes que hay a la derecha de la lista (5) tienen los siguientes significados: <br/>
 *  &nbsp;&nbsp;- Un deslizador (JSlider) de nivel de zoom, desde 10% hasta 200% <br/>
 *  &nbsp;&nbsp;- Un deslizador (JSlider) de rotación, desde 0º hasta 360º <br/>
 *  &nbsp;&nbsp;- Un JTextField de desplazamiento a derecha de pixels (0 por defecto) <br/>
 *  &nbsp;&nbsp;- Un JTextField de desplazamiento abajo de pixels (0 por defecto) <br/>
 *  &nbsp;&nbsp;- Una indicación de milisegundos de cada imagen dentro de la secuencia del sprite (100 por defecto) <br/>
 *  &nbsp;&nbsp;Estos valores indicados configuran cada imagen dentro de la secuencia (y por tanto deben almacenarse y restaurarse por cada imagen de la secuencia). Lo demás es para toda la secuencia:<br/>
 *  &nbsp;&nbsp;- Checkbox de ciclo (al visualizar la secuencia, tras la última imagen vuelve a empezar la primera) <br/>
 *  &nbsp;&nbsp;- Botones de arriba/abajo que suben o bajan una posición la imagen seleccionada con respecto al resto de la lista (5) <br/>
 *  &nbsp;&nbsp;- Debajo de (5) dos JTextField de ancho y alto en píxels de toda la secuencia <br/>
 *  &nbsp;&nbsp;- (6) Botones de nuevo/save/load para reiniciar, guardar o cargar una secuencia configurada (en save y load aparecerá un cuadro de diálogo que pide el nombre y localización) <br/>
 *  - (7) es un panel donde se define la animación de movimiento del sprite en la "arena" (4). Su contenido es: <br/>
 *  &nbsp;&nbsp;- Píxels de origen x e y de la animación dentro de la arena<br/>
 *  &nbsp;&nbsp;- Velocidad inicial de la animación en píxels por segundo, con un cuadro de texto y un JSlider entre 0 y 200<br/>
 *  &nbsp;&nbsp;- Ángulo inicial de la animación en grados, con un cuadro de texto y un JSlider entre 0 y 90<br/>
 *  &nbsp;&nbsp;- Gravedad de la animación, con un JSlider entre 0.0 y 10.0  (9.8 por defecto)<br/>
 *  - (8) es un panel donde se complementa la animación de movimiento del sprite. Contenido: <br/>
 *  &nbsp;&nbsp;- Rotación de la animación en grados por segundo, con un cuadro de texto y un JSlider entre 0 y 360 (0 por defecto)<br/>
 *  &nbsp;&nbsp;- Zoom de la animación en % por segundo, con un cuadro de texto y un JSlider entre 50 y 200 (100 por defecto)<br/>
 *  &nbsp;&nbsp;- Checkbox de si la animación se hace cíclica (infinita) <br/>
 *  &nbsp;&nbsp;- Checkbox de si la animación se hace con retorno (va y vuelve) <br/>
 *  - (9) son los tres botones de animación:<br/>
 *  &nbsp;&nbsp;- Anima solo la secuencia sin mover el sprite en la arena<br/>
 *  &nbsp;&nbsp;- Hace el movimiento sin animar la secuencia (toma el sprite seleccionado actualmente)<br/>
 *  &nbsp;&nbsp;- Realiza a la vez las dos animaciones, la de secuencia y la de movimiento<br/>
 *  Otras anotaciones:<br/>
 *  - Valora los layouts más adecuados para cada panel. Intenta utilizar los más sencillos.<br/>
 *  - Los cuadros de texto que van con sliders asociados deben alimentarse mutuamente (si se cambia el slider cambia el texto y viceversa). Intenta hacerlo de una forma metódica en lugar de repitiendo código.<br/>
 *  - Haz una ventana interna (JInternalFrame) en lugar de un JFrame normal y así podría integrarse con otras ventanas de la misma aplicación<br/>
 *  - La Arena debería ocupar el máximo espacio posible de la ventana. El resto de los paneles el mínimo necesario.<br/>
 *  - En la zona (9) se podría añadir una JProgressBar que vaya mostrando la progresión de la animación en curso<br/>
 *  - Añade tooltips a las partes que consideres interesante<br/>
 *  - Si quieres probar los SplitPane puedes hacer uno entre (3) y (4)<br/>
 *  
 * <br/>
 * Programación posterior:<br/>
 *  Hay que definir modelos de datos para las listas (¿de qué tipo cada JList y cada modelo?)<br/>
 *  Hilos para los plays (¿cuántos? ¿cuándo?)<br/>
 *  Hay que usar alguna estructura (ArrayList por ejemplo) para guardar toda la configuración de la secuencia (¿varios arraylists o un arraylist de una clase nueva?)<br/>
 */
public class VentanaEdicionSprites {

}
