package Source;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Image extends JEditorPane {
  // PROPIEDADES
  private static final long serialVersionUID = 1L;
  JScrollPane scrollPane;
  int width, height;
  String src;

  public Image(String src, int width, int height) {
    // VALORES INICIALES
    this.src = src.contains(".bmp") ? src : src + ".bmp";
    this.height = height;
    this.width = width;

    // CONFIGURAR PANEL
    setEditable(false);
    setOpaque(true);

    // AGREGAR SCROLL
    scrollPane = new JScrollPane(this);
    scrollPane.setPreferredSize(new Dimension(width, height));
    scrollPane.setBorder(null);
    scrollPane.setViewportBorder(null);
  }

  // ACTUALIZAR IMAGEN
  public void updateSrc(String path) {
    this.src = path.contains(".bmp") ? path : path + ".bmp";
    repaint();
  }

  // OBTENER PANEL
  public JScrollPane getCPane() {
    return scrollPane;
  }

  // CONVERTIR A BYTES[]
  private static byte[] toByteArray(File file) throws FileNotFoundException {
    // ABRIR STREAM
    FileInputStream fis = new FileInputStream(file);
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    byte[] buf = new byte[1024];

    // CREAR BYTES CON STREAM
    try {
      for (int readNum; (readNum = fis.read(buf)) != -1;)
        bos.write(buf, 0, readNum);

      // CERRAR STREAMS
      bos.close();
      fis.close();
    } catch (IOException ex) {
      System.out.print(ex);
    }

    // RETORNAR BYTES[]
    return bos.toByteArray();
  }

  // OBTENER ENTERO ORDENADO LITTLE-ENDIAN
  private int toInt(byte[] a, int offs) {
    return (a[offs + 3] & 0xFF) << 24 | (a[offs + 2] & 0xFF) << 16 | (a[offs + 1] & 0xFF) << 8 | a[offs] & 0xFF;
  }

  // RENDERIZAR IMAGEN
  public void paint(Graphics g) {
    try {
      // LEER ARCHIVO Y OBTENER BYTES
      File imgFile = new File(src);
      byte[] byteArry = toByteArray(imgFile);

      // OBTENER ANCHO DEL BYTE 18-21
      int width = toInt(byteArry, 18);

      // OBTENER ALTO DEL BYTE 22-25
      int height = toInt(byteArry, 22);

      // ASIGNAR DIMENSION
      setSize(width, height);

      // VALOR INICIAL
      int x = width, y = 0;

      // RECORRER BITMAP
      for (int i = byteArry.length - 4; i > 54; i -= 3) {
        // OBTENER COLORES
        int B = (byteArry[i + 1] & 0xFF);
        int G = (byteArry[i + 2] & 0xFF);
        int R = (byteArry[i + 3] & 0xFF);

        // LIMITAR VALORES PARA LOS FILTROS EN PROXIMA FASE
        if (B > 255)
          B = 255;
        if (G > 255)
          G = 255;
        if (R > 255)
          R = 255;
        if (B < 0)
          B = 0;
        if (G < 0)
          G = 0;
        if (R < 0)
          R = 0;

        // AGREGAR COLOR Y DIBUJAR
        g.setColor(new Color(R, G, B));
        g.drawLine(x, y, x, y);

        // REINICIAR CONTADORES
        x--;
        if (x == 0) {
          x = width;
          y++;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
