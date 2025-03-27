import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.io.*;
import java.util.*; // Este import trae java.util.ArrayList, etc.


// Clase Nodo
class Nodo {
    int valor;
    Nodo izquierda, derecha;

    public Nodo(int valor) {
        this.valor = valor;
        izquierda = derecha = null;
    }
}

// Clase ArbolBinario
class ArbolBinario {
    Nodo raiz;

    public ArbolBinario() {
        raiz = null;
    }

    // Inserta un nuevo nodo en el árbol
    public void insertar(int valor) {
        raiz = insertarRecursivo(raiz, valor);
    }

    private Nodo insertarRecursivo(Nodo nodo, int valor) {
        if (nodo == null) {
            return new Nodo(valor);
        }
        if (valor < nodo.valor) {
            nodo.izquierda = insertarRecursivo(nodo.izquierda, valor);
        } else if (valor > nodo.valor) {
            nodo.derecha = insertarRecursivo(nodo.derecha, valor);
        }
        return nodo;
    }

    // Elimina un nodo dado su valor
    public void eliminar(int valor) {
        raiz = eliminarRecursivo(raiz, valor);
    }

    private Nodo eliminarRecursivo(Nodo nodo, int valor) {
        if (nodo == null) {
            return null;
        }
        if (valor < nodo.valor) {
            nodo.izquierda = eliminarRecursivo(nodo.izquierda, valor);
        } else if (valor > nodo.valor) {
            nodo.derecha = eliminarRecursivo(nodo.derecha, valor);
        } else {
            // Caso de nodo con un solo hijo o sin hijos
            if (nodo.izquierda == null)
                return nodo.derecha;
            if (nodo.derecha == null)
                return nodo.izquierda;
            // Caso de dos hijos: se reemplaza con el mínimo del subárbol derecho
            nodo.valor = minValor(nodo.derecha);
            nodo.derecha = eliminarRecursivo(nodo.derecha, nodo.valor);
        }
        return nodo;
    }

    private int minValor(Nodo nodo) {
        while (nodo.izquierda != null) {
            nodo = nodo.izquierda;
        }
        return nodo.valor;
    }

    // Búsqueda de un nodo
    public Nodo buscar(int valor) {
        return buscarRecursivo(raiz, valor);
    }

    private Nodo buscarRecursivo(Nodo nodo, int valor) {
        if (nodo == null || nodo.valor == valor) {
            return nodo;
        }
        return valor < nodo.valor ? buscarRecursivo(nodo.izquierda, valor)
                                  : buscarRecursivo(nodo.derecha, valor);
    }

    // Obtiene la altura (nivel) de un nodo; la raíz está en altura 1
    public int obtenerAltura(int valor) {
        return buscarAltura(raiz, valor, 1);
    }

    private int buscarAltura(Nodo nodo, int valor, int altura) {
        if (nodo == null) {
            return -1; // No se encontró el nodo
        }
        if (nodo.valor == valor) {
            return altura;
        }
        return valor < nodo.valor ? buscarAltura(nodo.izquierda, valor, altura + 1)
                                  : buscarAltura(nodo.derecha, valor, altura + 1);
    }

    // Métodos para realizar recorridos y recopilar nodos en una lista (usamos java.util.List y ArrayList)
    public java.util.List<Integer> preOrdenCollect() {
        java.util.List<Integer> lista = new java.util.ArrayList<>();
        preOrdenRec(raiz, lista);
        return lista;
    }

    private void preOrdenRec(Nodo nodo, java.util.List<Integer> lista) {
        if (nodo != null) {
            lista.add(nodo.valor);
            preOrdenRec(nodo.izquierda, lista);
            preOrdenRec(nodo.derecha, lista);
        }
    }

    public java.util.List<Integer> inOrdenCollect() {
        java.util.List<Integer> lista = new java.util.ArrayList<>();
        inOrdenRec(raiz, lista);
        return lista;
    }

    private void inOrdenRec(Nodo nodo, java.util.List<Integer> lista) {
        if (nodo != null) {
            inOrdenRec(nodo.izquierda, lista);
            lista.add(nodo.valor);
            inOrdenRec(nodo.derecha, lista);
        }
    }

    public java.util.List<Integer> postOrdenCollect() {
        java.util.List<Integer> lista = new java.util.ArrayList<>();
        postOrdenRec(raiz, lista);
        return lista;
    }

    private void postOrdenRec(Nodo nodo, java.util.List<Integer> lista) {
        if (nodo != null) {
            postOrdenRec(nodo.izquierda, lista);
            postOrdenRec(nodo.derecha, lista);
            lista.add(nodo.valor);
        }
    }

    // Métodos para retornar recorridos en forma de cadena
    public String preOrden() {
        return preOrdenCollect().toString();
    }

    public String inOrden() {
        return inOrdenCollect().toString();
    }

    public String postOrden() {
        return postOrdenCollect().toString();
    }

    public Nodo getRaiz() {
        return raiz;
    }
}

// Panel para dibujar el árbol gráficamente
class ArbolPanel extends JPanel {
    private ArbolBinario arbol;
    private int highlightValue = -1; // Nodo resaltado (por ejemplo, en búsquedas o animación)

    public ArbolPanel(ArbolBinario arbol) {
        this.arbol = arbol;
        setBackground(Color.WHITE);
    }

    public void setHighlightValue(int valor) {
        this.highlightValue = valor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (arbol.getRaiz() != null) {
            dibujarArbol(g, arbol.getRaiz(), getWidth() / 2, 50, getWidth() / 4);
        }
    }

    private void dibujarArbol(Graphics g, Nodo nodo, int x, int y, int espaciado) {
        if (nodo != null) {
            // Nodo resaltado se pinta en rojo
            g.setColor(nodo.valor == highlightValue ? Color.RED : Color.BLACK);
            g.fillOval(x - 15, y - 15, 30, 30);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 12));
            g.drawString(String.valueOf(nodo.valor), x - 7, y + 5);
            g.setColor(Color.BLACK);
            if (nodo.izquierda != null) {
                g.drawLine(x, y, x - espaciado, y + 50);
                dibujarArbol(g, nodo.izquierda, x - espaciado, y + 50, espaciado / 2);
            }
            if (nodo.derecha != null) {
                g.drawLine(x, y, x + espaciado, y + 50);
                dibujarArbol(g, nodo.derecha, x + espaciado, y + 50, espaciado / 2);
            }
        }
    }
}

// Clase para la interfaz gráfica y operaciones
class ArbolGUI extends JFrame {
    private ArbolBinario arbol;
    private JTextField inputValor;
    private JTextArea outputArea;
    private ArbolPanel panelArbol;

    public ArbolGUI() {
        try {
            // Aplicar LookAndFeel del sistema
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            // Si falla, se sigue con el LookAndFeel por defecto
        }

        arbol = new ArbolBinario();
        setTitle("Árbol Binario de Búsqueda");
        setSize(950, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel de controles con diseño mejorado
        JPanel panelControl = new JPanel();
        panelControl.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelControl.setBorder(new EmptyBorder(10, 10, 10, 10));

        inputValor = new JTextField(5);
        JButton btnInsertar = new JButton("Insertar Nodo");
        JButton btnEliminar = new JButton("Eliminar Nodo");
        JButton btnBuscar = new JButton("Buscar Nodo");
        JButton btnPreOrden = new JButton("Recorrido PreOrden");
        JButton btnInOrden = new JButton("Recorrido InOrden");
        JButton btnPostOrden = new JButton("Recorrido PostOrden");
        JButton btnCargarArchivo = new JButton("Cargar Archivo");
        JButton btnGuardarResultados = new JButton("Guardar Resultados");

        panelControl.add(new JLabel("Valor:"));
        panelControl.add(inputValor);
        panelControl.add(btnInsertar);
        panelControl.add(btnEliminar);
        panelControl.add(btnBuscar);
        panelControl.add(btnPreOrden);
        panelControl.add(btnInOrden);
        panelControl.add(btnPostOrden);
        panelControl.add(btnCargarArchivo);
        panelControl.add(btnGuardarResultados);
        add(panelControl, BorderLayout.NORTH);

        // Área de salida para mostrar resultados
        outputArea = new JTextArea(10, 60);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollSalida = new JScrollPane(outputArea);
        scrollSalida.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(scrollSalida, BorderLayout.SOUTH);

        // Panel para el árbol gráfico
        panelArbol = new ArbolPanel(arbol);
        panelArbol.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(panelArbol, BorderLayout.CENTER);

        // Acciones de botones
        btnInsertar.addActionListener(e -> insertarValor());
        btnEliminar.addActionListener(e -> eliminarValor());
        btnBuscar.addActionListener(e -> buscarValor());
        btnPreOrden.addActionListener(e -> animarRecorrido("PreOrden"));
        btnInOrden.addActionListener(e -> animarRecorrido("InOrden"));
        btnPostOrden.addActionListener(e -> animarRecorrido("PostOrden"));
        btnCargarArchivo.addActionListener(e -> cargarArchivo());
        btnGuardarResultados.addActionListener(e -> guardarResultados());
    }

    private void insertarValor() {
        try {
            int valor = Integer.parseInt(inputValor.getText().trim());
            arbol.insertar(valor);
            outputArea.append("Insertado: " + valor + "\n");
            panelArbol.repaint();
        } catch (NumberFormatException ex) {
            outputArea.append("Ingrese un número válido.\n");
        }
    }

    private void eliminarValor() {
        try {
            int valor = Integer.parseInt(inputValor.getText().trim());
            arbol.eliminar(valor);
            outputArea.append("Eliminado: " + valor + "\n");
            panelArbol.repaint();
        } catch (NumberFormatException ex) {
            outputArea.append("Ingrese un número válido.\n");
        }
    }

    private void buscarValor() {
        try {
            int valor = Integer.parseInt(inputValor.getText().trim());
            Nodo encontrado = arbol.buscar(valor);
            if (encontrado != null) {
                int altura = arbol.obtenerAltura(valor);
                panelArbol.setHighlightValue(valor);
                outputArea.append("El nodo " + valor + " fue encontrado en la altura " + altura + ".\n");
            } else {
                panelArbol.setHighlightValue(-1);
                outputArea.append("El nodo " + valor + " no está en el árbol.\n");
            }
            panelArbol.repaint();
        } catch (NumberFormatException ex) {
            outputArea.append("Ingrese un número válido.\n");
        }
    }

    // Método para animar el recorrido seleccionado resaltando cada nodo
    private void animarRecorrido(String tipo) {
        java.util.List<Integer> recorrido;
        switch (tipo) {
            case "PreOrden":
                recorrido = arbol.preOrdenCollect();
                break;
            case "InOrden":
                recorrido = arbol.inOrdenCollect();
                break;
            case "PostOrden":
                recorrido = arbol.postOrdenCollect();
                break;
            default:
                recorrido = new java.util.ArrayList<>();
        }
        // Mostrar el resultado completo en el área de salida
        outputArea.append(tipo + ": " + recorrido.toString() + "\n");

        // Usamos javax.swing.Timer para la animación (evitando ambigüedad con java.util.Timer)
        javax.swing.Timer timer = new javax.swing.Timer(700, null);
        final int[] index = {0};
        timer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (index[0] < recorrido.size()) {
                    int valor = recorrido.get(index[0]);
                    panelArbol.setHighlightValue(valor);
                    panelArbol.repaint();
                    index[0]++;
                } else {
                    timer.stop();
                    // Quita el resaltado al terminar
                    panelArbol.setHighlightValue(-1);
                    panelArbol.repaint();
                }
            }
        });
        timer.start();
    }

    // Cargar archivo de texto con números separados por coma (o espacios)
    private void cargarArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        int seleccion = fileChooser.showOpenDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try (Scanner sc = new Scanner(archivo)) {
                sc.useDelimiter("[,\\s]+");
                while (sc.hasNext()) {
                    String token = sc.next().trim();
                    if (!token.isEmpty()) {
                        try {
                            int valor = Integer.parseInt(token);
                            arbol.insertar(valor);
                            outputArea.append("Insertado desde archivo: " + valor + "\n");
                        } catch (NumberFormatException nfe) {
                            outputArea.append("Valor inválido en archivo: " + token + "\n");
                        }
                    }
                }
                panelArbol.repaint();
            } catch (FileNotFoundException ex) {
                outputArea.append("Archivo no encontrado.\n");
            }
        }
    }

    // Guarda el contenido del área de salida en un archivo de texto
    private void guardarResultados() {
        JFileChooser fileChooser = new JFileChooser();
        int seleccion = fileChooser.showSaveDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
                pw.print(outputArea.getText());
                outputArea.append("Resultados guardados en: " + archivo.getAbsolutePath() + "\n");
            } catch (IOException ex) {
                outputArea.append("Error al guardar el archivo.\n");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ArbolGUI().setVisible(true));
    }
}
