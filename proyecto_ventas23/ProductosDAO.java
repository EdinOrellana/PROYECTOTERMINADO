package proyecto_ventas23;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ProductosDAO {

    PreparedStatement ps;
    ResultSet rs;
    Connection con;
    Conexion  conectar = new Conexion ();
    Object resultado [] = new Object[3];
    Object datos[][];
    public Object[] filtro(int codigo) {

        String sql = "select * from productos where codigo = " + "'" + codigo + "'" + ";";
        try {
            con = conectar.Conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                resultado[0] = rs.getInt(1);
                resultado[1] = rs.getString(2);
                resultado[2] = rs.getDouble(5);
                
                return resultado;
            }

        } catch (SQLException e) {
            System.out.println(e);
        }

        return null;
    }
      

    public Object[][] listar_tabla() {
        String instruccion = "select * from productos";

        try {
            int x = 0;
            con = conectar.Conectar();
            ps = con.prepareStatement(instruccion);
            rs = ps.executeQuery();

            while (rs.next()) {
                x++;
            }
            datos = new Object[x][5];
            x = 0;
            con = conectar.Conectar();
            ps = con.prepareStatement(instruccion);
            rs = ps.executeQuery();

            while (rs.next()) {
                datos[x][0] = rs.getInt(1);
                datos[x][1] = rs.getString(2);
                datos[x][2] = rs.getString(3);
                datos[x][3] = rs.getInt(4);
                datos[x][4] = rs.getInt(5);
                x++;
            }

        } catch (SQLException e) {
            System.out.println(e);
        }

        return datos;
    }

    public void crear(int codigo, String nombre, String descripcion, int cantidad, double precio) {
        String sql = "insert into productos(codigo, nombre, descripcion, cantidad, precio) values (?,?,?,?,?)";
        try {
            con = conectar.Conectar();
            ps = con.prepareStatement(sql);
            ps.setInt(1, codigo);
            ps.setString(2, nombre);
            ps.setString(3, descripcion);
            ps.setInt(4, cantidad);
            ps.setDouble(5, precio);
            ps.executeUpdate();

        } catch (Exception e) {
        }
    }

    public void modificar(int codigo, String nombre, String descripcion, int cantidad, double precio) {
        String sql = "update productos set nombre='" + nombre + "', descripcion= '" + descripcion + "', cantidad = '" + cantidad + "', precio ='" + precio + "' where codigo='" + codigo + "'";

        try {
            con = conectar.Conectar();
            ps = con.prepareStatement(sql);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void pdf() throws FileNotFoundException, DocumentException {

        FileOutputStream gen = new FileOutputStream("Reporte-Productos.pdf");
        Document documento = new Document();

        PdfWriter.getInstance(documento, gen);
        documento.open();

        Paragraph parrafo = new Paragraph("Reporte de Productos Base de datos");
        parrafo.setAlignment(1);
        documento.add(parrafo);
        documento.add(new Paragraph("\n"));

        String sql = "select * from productos";
        try {
            con = conectar.Conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
               
                documento.add(new Paragraph("Código: " + rs.getInt(1)));
                documento.add(new Paragraph("Nombre: " + rs.getString(2)));
                documento.add(new Paragraph("Descripcion: " + rs.getString(3)));
                documento.add(new Paragraph("Cantidad: " + rs.getInt(4)));
                documento.add(new Paragraph("Precio: " + rs.getDouble(5)));
                documento.add(new Paragraph("\n\n"));
            }
        } catch (Exception e) {
        }

        documento.close();
        JOptionPane.showMessageDialog(null, "El archivo se creo correctamente");
        try {
            File productos_doc = new File("Reporte-Productos.pdf");
            Desktop.getDesktop().open(productos_doc);
        } catch (Exception e) {
        }

    }

    public void eliminar(int x) {
        String sql = "delete from productos where codigo =" + x;
        try {

            con = conectar.Conectar();
            ps = con.prepareStatement(sql);
            ps.executeUpdate();

        } catch (Exception e) {
        }
    }

    

}