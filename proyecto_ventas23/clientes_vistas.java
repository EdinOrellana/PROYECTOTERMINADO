package proyecto_ventas23;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.itextpdf.text.DocumentException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class clientes_vistas {

    JPanel clientes = new JPanel();
    JTable tabla = new JTable();
    JScrollPane sp = new JScrollPane();

    private void botones() {

        clientes.setLayout(null);
        JButton crear = new JButton("Crear");
        crear.setBounds(500, 100, 130, 50);
        clientes.add(crear);

        ActionListener funcion_crear = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crear();
            }
        };

        crear.addActionListener(funcion_crear);

        JButton carga = new JButton("Carga Masiva");
        carga.setBounds(670, 100, 130, 50);
        clientes.add(carga);

        ActionListener funcion_carga = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    carga_masiva();
                } catch (IOException ex) {
                    Logger.getLogger(clientes_vistas.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(clientes_vistas.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

        carga.addActionListener(funcion_carga);

        JButton actualizar = new JButton("Actualizar");
        actualizar.setBounds(500, 250, 130, 50);
        clientes.add(actualizar);
        
        ActionListener funcion_actualizar = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificar();
            }
        };

        actualizar.addActionListener(funcion_actualizar);

        JButton eliminar = new JButton("Eliminar");
        eliminar.setBounds(670, 250, 130, 50);
        clientes.add(eliminar);
        
        ActionListener funcion_eliminar = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminar_opcion();
            }
        };

        eliminar.addActionListener(funcion_eliminar);

        JButton exportar = new JButton("Exportar PDF");
        exportar.setBounds(500, 400, 300, 50);
        clientes.add(exportar);
        
        ActionListener funcion_pdf = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientesDAO sd = new ClientesDAO();
                try {
                    sd.pdf();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(clientes_vistas.class.getName()).log(Level.SEVERE, null, ex);
                } catch (DocumentException ex) {
                    Logger.getLogger(clientes_vistas.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

        exportar.addActionListener(funcion_pdf);

    }
    
    private void eliminar_opcion(){
    
    ClientesDAO sf = new ClientesDAO();
    sf.eliminar(Integer.parseInt(tabla.getValueAt(tabla.getSelectedRow(), 0)+""));
    
    }

    private String leerarchivo() {

        JPanel c1 = new JPanel();
        JFileChooser fc = new JFileChooser();
        int op = fc.showOpenDialog(c1);
        String content = "";
        if (op == JFileChooser.APPROVE_OPTION) {

            File pRuta = fc.getSelectedFile();
            String ruta = pRuta.getAbsolutePath();
            File archivo = null;
            FileReader fr = null;
            BufferedReader br = null;

            try {
                archivo = new File(ruta);
                fr = new FileReader(archivo);
                br = new BufferedReader(fr);
                String linea = "";

                while ((linea = br.readLine()) != null) {

                    content += linea + "\n";
                }
                return content;

            } catch (FileNotFoundException ex) {
                String resp = (String) JOptionPane.showInputDialog(null, "No se encontro el archivo");
            } catch (IOException ex) {
                String resp = (String) JOptionPane.showInputDialog(null, "No se pudo abrir el archivo");
            } finally {
                try {
                    if (null != fr) {
                        fr.close();
                    }
                } catch (Exception e2) {
                    String resp = (String) JOptionPane.showInputDialog(null, "No se encontro el archivo");
                    return "";
                }

            }
            return content;

        }
        return null;

    }

    private void carga_masiva() throws FileNotFoundException, IOException, ParseException {

        String archivo_retorno = leerarchivo();

        JsonParser parse = new JsonParser();
        JsonArray matriz = parse.parse(archivo_retorno).getAsJsonArray();

        for (int i = 0; i < matriz.size(); i++) {
            JsonObject objeto = matriz.get(i).getAsJsonObject();
            ClientesDAO sd = new ClientesDAO();
            sd.crear(objeto.get("codigo").getAsInt(), objeto.get("nombre").getAsString(), objeto.get("nit").getAsInt(), objeto.get("correo").getAsString(), objeto.get("genero").getAsString());
        }

    }

    private void tabla() {

        String columnas[] = {"Código", "Nombre", "NIT", "Correo", "Genero"};
        ClientesDAO sd = new ClientesDAO();
        Object filas[][] = sd.listar_tabla();
        tabla = new JTable(filas, columnas);
        sp = new JScrollPane(tabla);
        sp.setBounds(10, 20, 430, 600);
        clientes.add(sp);

    }

    private void crear() {

        JFrame frame_ventas = new JFrame();
        frame_ventas.setTitle("Nueva Sucursal");
        frame_ventas.setLocationRelativeTo(null);
        frame_ventas.setBounds(50, 175, 350, 400);
        frame_ventas.setVisible(true);

        JPanel p1 = new JPanel();
        p1.setLayout(null);
        frame_ventas.add(p1);

        JLabel l1 = new JLabel("Código");
        l1.setBounds(50, 20, 80, 50);
        p1.add(l1);

        JTextField t1 = new JTextField();
        t1.setBounds(150, 32, 130, 25);
        p1.add(t1);

        JLabel l2 = new JLabel("Nombre");
        l2.setBounds(50, 80, 80, 50);
        p1.add(l2);

        JTextField t2 = new JTextField();
        t2.setBounds(150, 92, 130, 25);
        p1.add(t2);

        JLabel l3 = new JLabel("NIT");
        l3.setBounds(50, 140, 80, 50);
        p1.add(l3);

        JTextField t3 = new JTextField();
        t3.setBounds(150, 152, 130, 25);
        p1.add(t3);

        JLabel l4 = new JLabel("Correo");
        l4.setBounds(50, 200, 80, 50);
        p1.add(l4);

        JTextField t4 = new JTextField();
        t4.setBounds(150, 212, 130, 25);
        p1.add(t4);

        JLabel l5 = new JLabel("Genero");
        l5.setBounds(50, 260, 80, 50);
        p1.add(l5);

        JTextField t5 = new JTextField();
        t5.setBounds(150, 272, 130, 25);
        p1.add(t5);

        JButton b1 = new JButton("Guardar");
        b1.setBounds(130, 330, 80, 15);
        p1.add(b1);

        ActionListener guardar = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientesDAO sd = new ClientesDAO();
                sd.crear(Integer.parseInt(t1.getText()), t2.getText(),Integer.parseInt(t3.getText()), t4.getText(), t5.getText());
                frame_ventas.setVisible(false);
            }
        };

        b1.addActionListener(guardar);

    }
    
     private void modificar() {

        JFrame frame_ventas = new JFrame();
        frame_ventas.setTitle("Modificar Clientes");
        frame_ventas.setLocationRelativeTo(null);
        frame_ventas.setBounds(50, 175, 350, 400);
        frame_ventas.setVisible(true);

        JPanel p1 = new JPanel();
        p1.setLayout(null);
        frame_ventas.add(p1);

        JLabel l1 = new JLabel("Código");
        l1.setBounds(50, 20, 80, 50);
        p1.add(l1);

        JTextField t1 = new JTextField();
        t1.setBounds(150, 32, 130, 25);
        t1.setText(tabla.getValueAt(tabla.getSelectedRow(), 0)+"");
        t1.setEnabled(false);
        p1.add(t1);

        JLabel l2 = new JLabel("Nombre");
        l2.setBounds(50, 80, 80, 50);
        p1.add(l2);

        JTextField t2 = new JTextField();
        t2.setBounds(150, 92, 130, 25);
        t2.setText(tabla.getValueAt(tabla.getSelectedRow(), 1)+"");
        p1.add(t2);
        

        JLabel l3 = new JLabel("NIT");
        l3.setBounds(50, 140, 80, 50);
        p1.add(l3);

        JTextField t3 = new JTextField();
        t3.setBounds(150, 152, 130, 25);
        t3.setText(tabla.getValueAt(tabla.getSelectedRow(), 2)+"");
        p1.add(t3);

        JLabel l4 = new JLabel("Correo");
        l4.setBounds(50, 200, 80, 50);
        p1.add(l4);

        JTextField t4 = new JTextField();
        t4.setBounds(150, 212, 130, 25);
        t4.setText(tabla.getValueAt(tabla.getSelectedRow(), 3)+"");
        p1.add(t4);

        JLabel l5 = new JLabel("Genero");
        l5.setBounds(50, 260, 80, 50);
        p1.add(l5);

        JTextField t5 = new JTextField();
        t5.setBounds(150, 272, 130, 25);
        t5.setText(tabla.getValueAt(tabla.getSelectedRow(), 4)+"");
        p1.add(t5);

        JButton b1 = new JButton("Guardar");
        b1.setBounds(130, 330, 80, 15);
        p1.add(b1);

        ActionListener guardar = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientesDAO sd = new ClientesDAO();
                sd.modificar(Integer.parseInt(t1.getText()), t2.getText(),Integer.parseInt(t3.getText()), t4.getText(),t5.getText());
                frame_ventas.setVisible(false);
            }
        };

        b1.addActionListener(guardar);

    }

    public void ejecutar() {
        botones();
        tabla();
    }

}