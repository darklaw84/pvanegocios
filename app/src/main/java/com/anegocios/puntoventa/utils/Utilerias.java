package com.anegocios.puntoventa.utils;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.anegocios.puntoventa.AbrirCajaActivity;
import com.anegocios.puntoventa.BuildConfig;
import com.anegocios.puntoventa.PuntoVentaActivity;
import com.anegocios.puntoventa.PuntoVentaChicoActivity;
import com.anegocios.puntoventa.R;
import com.anegocios.puntoventa.bdlocal.CajaDTOLocal;
import com.anegocios.puntoventa.bdlocal.ClienteXYDTOLocal;
import com.anegocios.puntoventa.bdlocal.ImagenTicketDTOLocal;
import com.anegocios.puntoventa.bdlocal.OpcionPaqueteProductoLocal;
import com.anegocios.puntoventa.bdlocal.ProductosXYDTOLocal;
import com.anegocios.puntoventa.bdlocal.RowTicketLocal;
import com.anegocios.puntoventa.bdlocal.TicketDTOLocal;
import com.anegocios.puntoventa.bdlocal.TicketProductoDTOLocal;
import com.anegocios.puntoventa.database.CajasDB;
import com.anegocios.puntoventa.database.ClientesDB;
import com.anegocios.puntoventa.database.ProductosDB;
import com.anegocios.puntoventa.database.TicketDB;
import com.anegocios.puntoventa.dtosauxiliares.ClienteXYDTOAux;
import com.anegocios.puntoventa.dtosauxiliares.ProductosXYDTOAux;
import com.anegocios.puntoventa.jsons.AbonosDTO;
import com.anegocios.puntoventa.jsons.ClienteXYDTO;
import com.anegocios.puntoventa.jsons.DireccionDetalleTicketDTO;
import com.anegocios.puntoventa.jsons.OpcionesPaqueteTicketDTO;
import com.anegocios.puntoventa.jsons.ProductosXYDTO;
import com.anegocios.puntoventa.jsons.ReporteTicketDetalleDTO;
import com.anegocios.puntoventa.jsons.Usuario;
import com.anegocios.puntoventa.jsons.VentasDetalleTicket;
import com.anegocios.puntoventa.jsons.VentasVentaTicketDTO;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.internal.Util;

public class Utilerias {

    Bitmap bitmap;


    public String serverAnterior() {
        return "https://www.anegocios.com/WebService/";
    }

    public String serverNuevo() {
        return "https://www.anegocios.com.mx/WebService/";
    }

    public void iniciarReal(Context context) {
        Realm.init(context);
       /* RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(1) // Must be bumped when the schema changes
                .migration(new MigracionRealm()) // Migration to run instead of throwing an exception
                .build();*/

        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);
    }

    public static String obtenerServer(Context context) {

        String url = obtenerValor("urlServer", context);
        return url;
    }


    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public boolean emailValido(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;
    }


    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

    public void guardarValor(String key, String value, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public Map<String, ?> obtenerAllKeys(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Map<String, ?> todas = prefs.getAll();
        return todas;
    }

    public void quitarValor(String key, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);
        editor.commit();
    }

    public static String obtenerValor(String key, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(key, null);
    }

    public boolean tienePermisos(Context context, Activity activity) {
        boolean tienePermisos = true;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            tienePermisos = false;
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            tienePermisos = false;
        }


        if (Build.VERSION.SDK_INT < 30) {

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                tienePermisos = false;
            }

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                tienePermisos = false;
            }
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            tienePermisos = false;
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            tienePermisos = false;
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            tienePermisos = false;
        }

        return tienePermisos;
    }


    public void pedirPermisos(Context context, Activity activity) {


        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_NETWORK_STATE}, 1);


    }


    public boolean obtenerModoAplicacion(Context context) {
        String modoApp = obtenerValor("modoApp", context);
        if (modoApp == null) {
            return true;
        } else {
            if (modoApp.equals("on")) {
                return true;
            } else {
                return false;
            }

        }
    }

    public String obtenerSerial(Context context, Activity activity) {
        String serialNumber = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_PHONE_STATE}, 1);

            }
            try {
                serialNumber = Build.getSerial();
            } catch (Exception e) {
                serialNumber = "65152088";
            }
        } else {
            serialNumber = Build.SERIAL;

        }

        return serialNumber;
    }

    public boolean verificaConexion(Context ctx) {
        boolean bConectado = false;
        ConnectivityManager connec = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] redes = connec.getAllNetworkInfo();

        for (int i = 0; i < 2; i++) {
            // ¿Tenemos conexión? ponemos a true
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                bConectado = true;
            }
        }
        return bConectado;
    }


    public int parseInt(String valor) {
        int valorI = 0;
        try {
            valorI = Integer.parseInt(valor);
        } catch (Exception e) {

        }
        return valorI;
    }

    public double parseDouble(String valor) {
        double valorI = 0;
        try {
            valorI = Double.parseDouble(valor);
        } catch (Exception e) {

        }
        return valorI;
    }

    public String formatoDouble(double valor) {
        String dato = String.format("%.2f", valor);
        return dato.replace(",", ".");
    }

    public String convertirPass(String pass) {
        //aqui entra algo asi kjhsfgsg6516s1fd6g1sd65g1651$$&&==
        //se convierte y regresa el chido
        return "123";
    }

    public String obtenerFechaActualFormateada() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.format(new Date());
    }


    public String generarPassword(String password, String salt) {
        String salted = password + "{" + salt + "}";
        String generado = hash(salted);

        if (generado != null) {
            for (int i = 1; i < 3; i++) {
                generado = hash(generado + salted);

            }
        }

        return generado;
    }


    private String hash(String passwordToHash) {

        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] digest = md.digest(passwordToHash.getBytes());

            generatedPassword = Base64.encodeToString(digest, Base64.DEFAULT);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        generatedPassword = generatedPassword.replace("\n", "");
        return generatedPassword;
    }


    public String encryptPassword(String password, String salt) {

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (md != null) {
            String salted = password + '{' + salt + '}';
            md.reset();
            md.update(salted.getBytes());
            byte[] byteData = md.digest();

            byte[] combined;
            for (int i = 1; i < 5000; i++) {
                combined = CombinedByte(byteData, salted.getBytes());

                md.update(combined);
                byteData = md.digest();
            }
            String base64 = Base64.encodeToString(byteData, Base64.NO_WRAP);
            return base64;
        }
        return "nada";
    }

    public byte[] CombinedByte(byte[] one, byte[] two) {

        byte[] combined = new byte[one.length + two.length];
        for (int i = 0; i < combined.length; ++i) {
            combined[i] = i < one.length ? one[i] : two[i - one.length];
        }
        return combined;
    }

    public CajaDTOLocal abrirCaja(Context c, Activity a, double montoInical, Realm realm) {
        CajasDB cdb = new CajasDB();
        return cdb.abrirNuevaCaja(montoInical, obtenerFechaActualFormateada(),
                Integer.parseInt(obtenerValor("idUsuario", c)),
                Integer.parseInt(obtenerValor("idTienda", c)), realm);
    }


    public CajaDTOLocal obtenerCajaActual(Context c, Activity a, Realm realm) {
        CajasDB cdb = new CajasDB();
        return cdb.obtenerCajaActual(Integer.parseInt(obtenerValor("idUsuario", c)),
                Integer.parseInt(obtenerValor("idTienda", c)), realm);
    }


    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 5, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public byte[] BitMapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 5, baos);
        return baos.toByteArray();

    }


    public String BitMapToStringLocal(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public Bitmap StringToBitMapLocal(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }


    public Realm obtenerInstanciaBD(Context context) {
        Realm realm = null;


        try {
            realm = Realm.getDefaultInstance();

        } catch (Exception ex) {
            log(context, "sin datos", ex);
            ex.printStackTrace();
        }
        if (realm != null) {
            return realm;
        } else {
            iniciarReal(context);
            try {
                realm = Realm.getDefaultInstance();

            } catch (Exception ex) {
                log(context, "reiniciado Realm", ex);
                ex.printStackTrace();
            }
        }

        return realm;
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length, options);

            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public Bitmap ByteToBitMap(byte[] encodeByte) {
        try {

            BitmapFactory.Options options = new BitmapFactory.Options();
            //options.inSampleSize = 8;
            bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length, options);

            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public Bitmap ByteToBitMapNuevo(byte[] encodeByte) {
        try {

            BitmapFactory.Options options = new BitmapFactory.Options();
            //options.inSampleSize = 8;
            Bitmap bitmap1 = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length, options);

            return bitmap1;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }


    public void guardarPermisosUsuario(Usuario u, Context c, Boolean imagenes) {
        if (u != null) {
            if (u.getUsarCaja() != null)
                guardarValor("usarCaja", u.getUsarCaja().toString(), c);
            if (u.getCortarCaja() != null)
                guardarValor("cortarCaja", u.getCortarCaja().toString(), c);
            if (u.getCotizar() != null)
                guardarValor("cotizar", u.getCotizar().toString(), c);
            if (u.getPedido() != null)
                guardarValor("pedido", u.getPedido().toString(), c);
            if (u.getClt_C() != null)
                guardarValor("nuevoCliente", u.getClt_C().toString(), c);
            if (u.getClt_R() != null)
                guardarValor("consultarCliente", u.getClt_R().toString(), c);
            if (u.getClt_U() != null)
                guardarValor("editarCliente", u.getClt_U().toString(), c);
            if (u.getProd_C() != null)
                guardarValor("nuevoProducto", u.getProd_C().toString(), c);
            if (u.getProd_R() != null)
                guardarValor("consultarProducto", u.getProd_R().toString(), c);
            if (u.getProd_U() != null)
                guardarValor("editarProducto", u.getProd_U().toString(), c);
            if (u.getCajaDesc() != null)
                guardarValor("cajaDesc", u.getCajaDesc().toString(), c);
            if (u.getCajaPropina() != null)
                guardarValor("cajaPropina", u.getCajaPropina().toString(), c);
            if (u.getEditPrecio() != null)
                guardarValor("editPrecio", u.getEditPrecio().toString(), c);
            guardarValor("imagenesPV", imagenes.toString(), c);
        }
        //guardarValor("imagenesPV", "false", c);

    }


    public Usuario obtenerPermisosUsuario(Context c) {
        Usuario u = new Usuario();
        u.setUsarCaja(Boolean.parseBoolean(obtenerValor("usarCaja", c)));
        u.setCortarCaja(Boolean.parseBoolean(obtenerValor("cortarCaja", c)));
        u.setCotizar(Boolean.parseBoolean(obtenerValor("cotizar", c)));
        u.setPedido(Boolean.parseBoolean(obtenerValor("pedido", c)));
        u.setClt_C(Boolean.parseBoolean(obtenerValor("nuevoCliente", c)));
        u.setClt_R(Boolean.parseBoolean(obtenerValor("consultarCliente", c)));
        u.setClt_U(Boolean.parseBoolean(obtenerValor("editarCliente", c)));
        u.setProd_C(Boolean.parseBoolean(obtenerValor("nuevoProducto", c)));
        u.setProd_R(Boolean.parseBoolean(obtenerValor("consultarProducto", c)));
        u.setProd_U(Boolean.parseBoolean(obtenerValor("editarProducto", c)));
        u.setCajaDesc(Boolean.parseBoolean(obtenerValor("cajaDesc", c)));
        u.setCajaPropina(Boolean.parseBoolean(obtenerValor("cajaPropina", c)));
        u.setEditPrecio(Boolean.parseBoolean(obtenerValor("editPrecio", c)));
        u.setMostrarImagenes(Boolean.parseBoolean(obtenerValor("imagenesPV", c)));
        return u;
    }


    public String armarTicket(long idTicket, Realm realm) {

        //obtenemos el ultimo


        CajasDB cdb = new CajasDB();
        realm.refresh();
        TicketDTOLocal ultimoTicket = cdb.obtenerTicketCaja(idTicket, realm);
        String s = "";
        if (ultimoTicket != null) {

            String cliente = ultimoTicket.getIdCliente();

            String tipoCliente = ultimoTicket.getTipoCliente();
            String negocio = "";
            String telefono = "";
            String direccion = "";
            String ticketWeb = "";
            String ticketApp = "";
            if (ultimoTicket.getIdFolioServer() > 0) {
                ticketWeb = "" + ultimoTicket.getIdFolioServer();
            } else {
                ticketWeb = "";
            }

            ticketApp = "" + ultimoTicket.getIdTicket();

            String fecha = ultimoTicket.getFecha();
            if (cliente != null && !cliente.equals("") && tipoCliente != null && !tipoCliente.equals("")) {
                ClientesDB cldb = new ClientesDB();
                ClienteXYDTOAux cli = new ClienteXYDTOAux();
                if (tipoCliente.equals("S")) {
                    ClienteXYDTO cliSer = cldb.obtenerClienteServer(Integer.parseInt(cliente), realm);
                    if (cliSer != null) {
                        cli = new ClienteXYDTOAux(cliSer, "S");
                    }
                } else {
                    ClienteXYDTOLocal clil = cldb.obtenerClienteLocal(Integer.parseInt(cliente), realm);
                    if (clil != null) {
                        cli = new ClienteXYDTOAux(clil, "L");
                    }
                }
                String nombre = cli.getNombre() == null ? "" : cli.getNombre();
                String aPaterno = cli.getApellidoP() == null ? "" : cli.getApellidoP();
                String aMaterno = cli.getApellidoM() == null ? "" : cli.getApellidoM();
                cliente = nombre + " " + aPaterno
                        + " " + aMaterno;
                telefono = cli.getTelefono() == null ? "" : cli.getTelefono();
                String calle = cli.getCalle() == null ? "" : cli.getCalle();
                String ext = cli.getNumeroExt() == null ? "" : cli.getNumeroExt();
                String inter = cli.getNumeroInt() == null ? "" : cli.getNumeroInt();
                String colonia = cli.getColonia() == null ? "" : cli.getColonia();
                String municipio = cli.getMunicipio() == null ? "" : cli.getMunicipio();
                String estado = cli.getEstado() == null ? "" : cli.getEstado();
                direccion = calle + " " +
                        ext + " " +
                        inter + " " +
                        colonia + " " +
                        municipio + " " +
                        estado;
            } else {
                cliente = "";
            }
            /*UsuariosDB udb = new UsuariosDB();
            Usuario usuario = udb.obtenerUsuario(Integer.parseInt(obtenerValor("idUsuario", c)));
            */


            if (!cliente.equals("")) {
                s += "Cliente: " + cortarString(cliente, 24, 23);
                s += "Telefono: " + telefono + "\n";
                s += "Direccion: " + cortarString(direccion, 32, 21);
            }
            s += "          Ticket App: " + ticketApp + "\n";
            if (!ticketWeb.equals("")) {
                s += "          Ticket Web: " + ticketWeb + "\n";
            }
            s += "      " + fecha + "\n";
            s += "------------------------------\n";
            s += "\n";
            s += "Cant.  Pre. Uni.  Precio   IVA\n";
            s += armarProductosTicket(ultimoTicket, realm);
            s += "\n\n";
            s += "             Subtotal:" + formatDoubleTicket(ultimoTicket.getSubtotal(), 8, "$") + "\n";
            s += "                  IVA:" + formatDoubleTicket(ultimoTicket.getIva(), 8, "$") + "\n";
            s += "            Descuento:" + formatDoubleTicket(ultimoTicket.getDescuentoTotal(), 8, "$") + "\n";
            s += " Propina y/o comision:" + formatDoubleTicket(ultimoTicket.getPropinaTotal(), 8, "$") + "\n";
            s += "________________________________\n";
            s += "                TOTAL:" + formatDoubleTicket(ultimoTicket.getTotal(), 8, "$") + "\n";
            s += "             EFECTIVO:" + formatDoubleTicket(ultimoTicket.getEfectivo(), 8, "$") + "\n";
            s += "               CAMBIO:" + formatDoubleTicket(ultimoTicket.getCambio(), 8, "$") + "\n";

            if (ultimoTicket.getTipo().equals("pedido") && (ultimoTicket.getEfectivo() > 0 || ultimoTicket.getTarjeta() > 0)) {
                //agregamos el saldo y el abono
                double abono = ultimoTicket.getEfectivo() + ultimoTicket.getTarjeta();
                s += "                ABONO:" + formatDoubleTicket(abono, 8, "$") + "\n";
                s += "                SALDO:" + formatDoubleTicket(ultimoTicket.getTotal() - abono, 8, "$") + "\n";


                s += "\n";
                s += "\n";
                s += "            ABONOS" + "\n";

                s += "    Fecha             Abono   \n";
                String precios = "";
                precios = obtenerFechaActualFormateada().substring(0, 10);

                precios += "          ";
                precios += formatDoubleTicket(abono, 8, "$");
                s += precios;
            }

            s += "\n";
            s += "\n";
            s += " Comentario:\n";
            s += cortarString(ultimoTicket.getComentario(), 32, 32);
            s += "\n";

            if (ultimoTicket.isProdEntregado()) {
                s += "  ___________________________  " + "\n";
                s += "             FIRMA" + "\n";
                s += "      PRODUCTO ENTREGADO" + "\n";
            }
        }

        return s;


    }


    public String armarCorte(double efectivoContado, double efectivoCalculado,
                             double tarjetaContado, double tarjetaCalculado) {


        String s = "";
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        s += "Fecha Corte: " + df.format(new Date()) + "\n";


        s += "\n";
        s += "------------------------------\n";
        s += "\n";

        s += "Efectivo Calculado :" + formatDoubleTicket(efectivoCalculado, 8, "$") + "\n";
        s += "  Efectivo Contado :" + formatDoubleTicket(efectivoContado, 8, "$") + "\n";
        s += " Tarjeta Calculado :" + formatDoubleTicket(tarjetaCalculado, 8, "$") + "\n";
        s += "   Tarjeta Contado :" + formatDoubleTicket(tarjetaContado, 8, "$") + "\n";
        s += "\n";
        s += "________________________________\n";
        s += "\n";

        int primerRenglon = 32;
        if (efectivoCalculado == efectivoContado) {
            s += cortarString("¡Felicidades la diferencia fue de : $0 en pago con EFECTIVO! ", 32, primerRenglon);

        } else if (efectivoContado > efectivoCalculado) {
            double diferencia = efectivoContado - efectivoCalculado;
            s += cortarString("¡Sobraron: $" + diferencia + " en EFECTIVO! ", 32, primerRenglon);

        } else if (efectivoContado < efectivoCalculado) {
            double diferencia = efectivoContado - efectivoCalculado;
            s += cortarString("¡Faltaron: $" + diferencia + " en EFECTIVO!  ", 32, primerRenglon);

        }


        if (tarjetaCalculado == tarjetaContado) {
            s += cortarString("¡Felicidades la diferencia fue de : $0 en pago con TARJETA!", 32, primerRenglon);

        } else if (tarjetaContado > tarjetaCalculado) {
            double diferencia = tarjetaContado - tarjetaCalculado;
            s += cortarString("¡Sobraron: $" + diferencia + " en TARJETA!", 32, primerRenglon);

        } else if (tarjetaContado < tarjetaCalculado) {
            double diferencia = tarjetaContado - tarjetaCalculado;
            s += cortarString("¡Faltaron: $" + diferencia + " en TARJETA!", 32, primerRenglon);
        }


        return s;


    }


    public String armarTicketReimprimir(VentasDetalleTicket detalle) {

        //obtenemos el ultimo
        boolean prodEntregado = detalle.isProdEntregado();
        String cliente = "";
        if (detalle.getCliente() != null) {
            cliente = detalle.getCliente().getNombre() + " " + detalle.getCliente().getApellidoP() + " " + detalle.getCliente().getApellidoM();
        }


        String telefono = "";
        if (detalle.getCliente() != null && detalle.getCliente().getTelefonos() != null
                && detalle.getCliente().getTelefonos().size() > 0) {
            telefono = detalle.getCliente().getTelefonos().get(0).getNumero();
        }


        String ticketWeb = "";
        String ticketApp = "";

        ticketWeb = detalle.getFolio();

        if (detalle.getFolioApp() == null) {
            ticketApp = "";
        } else {
            ticketApp = "" + detalle.getFolioApp();
        }

        String fecha = detalle.getFecha();
        String direccion = "";
        if (detalle.getCliente() != null && detalle.getCliente().getDirecciones() != null
                && detalle.getCliente().getDirecciones().size() > 0) {
            DireccionDetalleTicketDTO dirObj = detalle.getCliente().getDirecciones().get(0);
            direccion = dirObj.getCalle() + " " + dirObj.getNumeroExt() + " " +
                    dirObj.getNumeroInt() + " " + dirObj.getColonia() + " " + dirObj.getMunicipio() + "" +
                    dirObj.getCp();
        }


        String s = "";
        if (!cliente.equals("")) {
            s += "Cliente: " + cortarString(cliente, 24, 23);
            s += "Telefono: " + telefono + "\n";
            s += "Direccion: " + cortarString(direccion, 32, 21);
        }
        s += "          Ticket App: " + ticketApp + "\n";
        if (!ticketWeb.equals("")) {
            s += "          Ticket Web: " + ticketWeb + "\n";
        }
        s += "      " + fecha + "\n";
        s += "------------------------------\n";
        s += "\n";
        s += "Cant.  Pre. Uni.  Precio   IVA\n";
        s += armarProductosTicketReimprimir(detalle.getVentas());
        s += "\n\n";
        s += "             Subtotal:" + formatDoubleTicket(detalle.getSubTotal(), 8, "$") + "\n";
        s += "                  IVA:" + formatDoubleTicket(detalle.getIva(), 8, "$") + "\n";
        s += "            Descuento:" + formatDoubleTicket(detalle.getDescuento(), 8, "$") + "\n";
        s += " Propina y/o comision:" + formatDoubleTicket(detalle.getPropina(), 8, "$") + "\n";
        s += "________________________________\n";
        s += "                TOTAL:" + formatDoubleTicket(detalle.getTotal(), 8, "$") + "\n";


        if (detalle.getPagosDiferidos() != null && detalle.getPagosDiferidos().size() > 0) {
            //agregamos el saldo y el abono
            double totalAbonos = 0;
            for (AbonosDTO ab : detalle.getPagosDiferidos()
            ) {
                try {
                    NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
                    Number cantidadAsi = format.parse(ab.getCantidad());
                    double cantidadAsignada = cantidadAsi.doubleValue();
                    totalAbonos += cantidadAsignada;
                } catch (Exception ex) {
                    Utilerias.log(ctx, "Error al formatear cantidad" + ex.getMessage(), ex);
                }
            }

            s += "                ABONO:" + formatDoubleTicket(totalAbonos, 8, "$") + "\n";
            s += "                SALDO:" + formatDoubleTicket(detalle.getSaldo(), 8, "$") + "\n";

            s += "\n";
            s += "\n";
            s += "            ABONOS" + "\n";

            s += "  Fecha     Usuario      Abono\n";
            s += armarAbonosTicket(detalle.getPagosDiferidos());
        }


        s += "\n";
        s += "\n";

        s += " Comentario:\n";
        if (detalle.getComentario() != null) {
            s += cortarString(detalle.getComentario(), 32, 32);
        }
        s += "\n";
        s += "\n";


        if (prodEntregado) {
            s += "  ___________________________  " + "\n";
            s += "             FIRMA" + "\n";
            s += "      PRODUCTO ENTREGADO" + "\n";
        }

        return s;


    }

    public String armarTicketSDK(long idTicket, Realm realm) {

        //obtenemos el ultimo

        CajasDB cdb = new CajasDB();
        TicketDTOLocal ultimoTicket = cdb.obtenerTicketCaja(idTicket, realm);

        String cliente = ultimoTicket.getIdCliente();
        String tipoCliente = ultimoTicket.getTipoCliente();
        String negocio = "";
        String telefono = "";
        String direccion = "";
        String ticket = "";
        if (ultimoTicket.getIdFolioServer() > 0) {
            ticket = "" + ultimoTicket.getIdFolioServer();
        } else {
            ticket = "" + ultimoTicket.getIdTicket();
        }

        String fecha = ultimoTicket.getFecha();
        if (cliente != null && !cliente.equals("") && tipoCliente != null && !tipoCliente.equals("")) {
            ClientesDB cldb = new ClientesDB();
            ClienteXYDTOAux cli = new ClienteXYDTOAux();
            if (tipoCliente.equals("S")) {
                ClienteXYDTO cliSer = cldb.obtenerClienteServer(Integer.parseInt(cliente), realm);
                cli = new ClienteXYDTOAux(cliSer, "S");
            } else {
                ClienteXYDTOLocal clil = cldb.obtenerClienteLocal(Integer.parseInt(cliente), realm);
                cli = new ClienteXYDTOAux(clil, "L");
            }
            String nombre = cli.getNombre() == null ? "" : cli.getNombre();
            String aPaterno = cli.getApellidoP() == null ? "" : cli.getApellidoP();
            String aMaterno = cli.getApellidoM() == null ? "" : cli.getApellidoM();
            cliente = nombre + " " + aPaterno
                    + " " + aMaterno;
            telefono = cli.getTelefono() == null ? "" : cli.getTelefono();
            String calle = cli.getCalle() == null ? "" : cli.getCalle();
            String ext = cli.getNumeroExt() == null ? "" : cli.getNumeroExt();
            String inter = cli.getNumeroInt() == null ? "" : cli.getNumeroInt();
            String colonia = cli.getColonia() == null ? "" : cli.getColonia();
            String municipio = cli.getMunicipio() == null ? "" : cli.getMunicipio();
            String estado = cli.getEstado() == null ? "" : cli.getEstado();
            direccion = calle + " " +
                    ext + " " +
                    inter + " " +
                    colonia + " " +
                    municipio + " " +
                    estado;
        } else {
            cliente = "";
        }
            /*UsuariosDB udb = new UsuariosDB();
            Usuario usuario = udb.obtenerUsuario(Integer.parseInt(obtenerValor("idUsuario", c)));
            */

        String s = "";
        if (!cliente.equals("")) {
            s += "Cliente: " + cortarString(cliente, 24, 23);
            s += "Telefono: " + telefono + "\n";
            s += "Direccion: " + cortarString(direccion, 32, 21);
        }
        s += "          Ticket:" + ticket + "\n";
        s += "      " + fecha + "\n";
        s += "------------------------------\n";
        s += "\n";
        s += "Cant.  Pre. Uni.  Precio   IVA\n";
        s += armarProductosTicket(ultimoTicket, realm);
        s += "\n\n";
        s += "             Subtotal:" + formatDoubleTicket(ultimoTicket.getSubtotal(), 8, "$") + "\n";
        s += "                  IVA:" + formatDoubleTicket(ultimoTicket.getIva(), 8, "$") + "\n";
        s += "            Descuento:" + formatDoubleTicket(ultimoTicket.getDescuentoTotal(), 8, "$") + "\n";
        s += " Propina y/o comision:" + formatDoubleTicket(ultimoTicket.getPropinaTotal(), 8, "$") + "\n";
        s += "________________________________\n";
        s += "                TOTAL:" + formatDoubleTicket(ultimoTicket.getTotal(), 8, "$") + "\n";
        s += "             EFECTIVO:" + formatDoubleTicket(ultimoTicket.getEfectivo(), 8, "$") + "\n";
        s += "               CAMBIO:" + formatDoubleTicket(ultimoTicket.getCambio(), 8, "$") + "\n";
        s += "\n";
        s += "\n";
        s += " Comentario:\n";
        s += cortarString(ultimoTicket.getComentario(), 32, 32);
        s += "\n";
        return s;


    }


    private String armarProductosTicket(TicketDTOLocal ti, Realm realm) {
        ProductosDB pdb = new ProductosDB();
        String ps = "";
        CajasDB cdb = new CajasDB();
        List<TicketProductoDTOLocal> productosTicket = cdb.obtenerProductosTicket(ti.getIdTicket(), realm);
        for (TicketProductoDTOLocal p : productosTicket
        ) {
            ProductosXYDTO prodServ;
            ProductosXYDTOLocal prodLo;
            ProductosXYDTOAux proAux;
            if (p.getIdProdcutoServer() > 0) {
                prodServ = pdb.obtenerproductoServer(p.getIdProdcutoServer(), realm);
                proAux = new ProductosXYDTOAux(prodServ, "S");
                proAux.setPrecioVenta(p.getPrecioVenta());
                proAux.setCantidad(p.getCantidad());
                proAux.setIvaTotal(p.getIvaTotal());
            } else {
                prodLo = pdb.obtenerproductoLocal(p.getIdProductoLocal(), realm);
                proAux = new ProductosXYDTOAux(prodLo, "L");
                proAux.setPrecioVenta(p.getPrecioVenta());
                proAux.setCantidad(p.getCantidad());
                proAux.setIvaTotal(p.getIvaTotal());
            }

            List<OpcionPaqueteProductoLocal> opciones = cdb.obtenerOpcionesPaquetesProductos(p.getId(), realm);
            if (opciones != null && opciones.size() > 0) {

                ps += cortarString(proAux.getDescripcionCorta(), 32, 32);

                for (OpcionPaqueteProductoLocal o : opciones
                ) {
                    String opcionString = "";
                    if (o.isMostrar()) {
                        opcionString = "   * " + o.getCantidad() + " - " + o.getDescripcion();
                        ps += cortarString(opcionString, 32, 32);
                    }


                }

                ps += cortarString(armarPreciosProducto(proAux.getCantidad(), proAux.getPrecioVenta(),
                        proAux.getCantidad() * proAux.getPrecioVenta(),
                        proAux.getIvaTotal()), 32, 32);


            } else {

                ps += cortarString(proAux.getDescripcionCorta(), 32, 32);
                ps += cortarString(armarPreciosProducto(proAux.getCantidad(), proAux.getPrecioVenta(),
                        proAux.getCantidad() * proAux.getPrecioVenta(),
                        proAux.getIvaTotal()), 32, 32);
            }
        }
        return ps;
    }


    private String armarProductosTicketReimprimir(List<VentasVentaTicketDTO> productosTicket) {

        String ps = "";

        for (VentasVentaTicketDTO p : productosTicket
        ) {
            ProductosXYDTO prodServ;
            ProductosXYDTOLocal prodLo;
            ProductosXYDTOAux proAux;


            if (p.getOpcionesPkt() != null && p.getOpcionesPkt().size() > 0) {

                ps += cortarString(p.getProducto(), 32, 32);

                for (OpcionesPaqueteTicketDTO o : p.getOpcionesPkt()
                ) {
                    String opcionString = "";

                    opcionString = "   * " + o.getCantidad() + " - " + o.getProducto();
                    ps += cortarString(opcionString, 32, 32);


                }

                ps += cortarString(armarPreciosProducto(p.getCantidad(), p.getPrecioUnit(),
                        p.getCantidad() * p.getPrecioUnit(),
                        p.getIva()), 32, 32);


            } else {

                ps += cortarString(p.getProducto(), 32, 32);
                ps += cortarString(armarPreciosProducto(p.getCantidad(), p.getPrecioUnit(),
                        p.getCantidad() * p.getPrecioUnit(),
                        p.getIva()), 32, 32);
            }
        }
        return ps;
    }


    private String armarAbonosTicket(List<AbonosDTO> abonos) {

        String ps = "";

        for (AbonosDTO p : abonos
        ) {
            try {
                NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
                Number cantidadAsi = format.parse(p.getCantidad());
                double cantidadAsignada = cantidadAsi.doubleValue();

                ps += cortarString(
                        armarAbono(p.getFecha().substring(0, 10), p.getUsuario(), cantidadAsignada)
                        , 32, 32);
            } catch (Exception ex) {
                Utilerias.log(ctx, "Error 2 al formatear " + ex.getMessage(), ex);
            }

        }
        return ps;
    }

    private String armarAbono(String fecha, String usuario, double cantidad) {
        String precios = "";
        precios += fecha;
        precios += " ";

        if (usuario == null) {
            precios += "          ";
        } else {
            precios += cortarUsuarioString(usuario, 11);
        }
        precios += " ";
        precios += formatDoubleTicket(cantidad, 8, "$");

        return precios;
    }

    private String armarPreciosProducto(double cantidad, double punitario, double subtotal, double iva) {
        String precios = "";
        precios += formatDoubleTicket(cantidad, 6, " ");
        precios += " ";
        precios += formatDoubleTicket(punitario, 8, "$");
        precios += " ";
        precios += formatDoubleTicket(subtotal, 8, "$");
        precios += " ";
        precios += formatDoubleTicket(iva, 6, "$");
        return precios;
    }

    public String formatDouble(double cantidad) {
        return new DecimalFormat("#0.00").format(cantidad);
    }

    public String formatDoubleTicket(double cantidad, int espacios, String caracter) {
        String formateado = "";
        String espaciosS = "";
        formateado = new DecimalFormat("#0.00").format(cantidad);
        if (formateado.length() < espacios) {
            int faltantes = espacios - formateado.length();
            for (int i = 1; i < faltantes; i++) {
                espaciosS += " ";
            }
        }
        formateado = espaciosS + caracter + formateado;
        formateado = formateado.replace(",", ".");
        return formateado;
    }


    public String cortarString(String text, int size, int primerRenglon) {
        String cortada = "";
        if (text.length() >= primerRenglon) {
            cortada = text.substring(0, primerRenglon - 1) + "\n";
            text = text.substring(primerRenglon - 1, text.length());
            for (int start = 0; start < text.length(); start += size) {
                cortada += text.substring(start, Math.min(text.length(), start + size)) + "\n";
            }
        } else {
            cortada = text + "\n";
        }
        return cortada;
    }

    public String cortarUsuarioString(String text, int size) {
        String cortada = "";
        if (text.length() >= size) {
            cortada = text.substring(0, size - 1);

        } else {
            cortada = text;
            if (cortada.length() < size) {
                int espaciosfaltan = size - cortada.length();
                for (int i = 1; i <= espaciosfaltan; i++) {
                    cortada += " ";
                }
            }
        }
        return cortada;
    }

    public void enviarTicketWhatssap(String url, Activity activity) {


        try {

            if (appInstalledOrNot("com.whatsapp", activity)) {

                Intent waIntent = new Intent(Intent.ACTION_SEND);
                waIntent.setType("text/plain");
                String text = url;

                // PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                //Check if package exists or not. If not then code
                //in catch block will be called
                waIntent.setPackage("com.whatsapp");
                //com.whatsapp.w4b

                waIntent.putExtra(Intent.EXTRA_TEXT, text);
                activity.startActivity(Intent.createChooser(waIntent, "Share with"));
            } else if (appInstalledOrNot("com.whatsapp.w4b", activity)) {
                Intent waIntent = new Intent(Intent.ACTION_SEND);
                waIntent.setType("text/plain");
                String text = url;

                // PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                //Check if package exists or not. If not then code
                //in catch block will be called
                waIntent.setPackage("com.whatsapp.w4b");
                //

                waIntent.putExtra(Intent.EXTRA_TEXT, text);
                activity.startActivity(Intent.createChooser(waIntent, "Compartir Ticket"));
            }

        } catch (Exception e) {
            System.out.println("no esta instalado el whatssapp");
        }


    }

    public boolean isBluetoothEnabled() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null) {
            return mBluetoothAdapter.isEnabled();
        } else {
            return false;
        }

    }

    public BluetoothDevice obtenerImpresora(String nombreImpresora) {
        try {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();


            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    // RPP300 is the name of the bluetooth printer device
                    // we got this name from the list of paired devices

                    if (device.getName().equals(nombreImpresora)) {

                        int bondstate = device.getBondState();
                        if (bondstate == BluetoothDevice.BOND_NONE) {
                            if (device.createBond()) {
                                return device;
                            } else {
                                return null;
                            }
                        } else {
                            return device;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            log(ctx, "Intentando obtener impresora", ex);
        }

        return null;
    }

    public void imprimirCorte(Context c, double efectivoContado, double efectivoCalculado,
                              double tarjetaContado, double tarjetaCalculado) {
        Utilerias ut = new Utilerias();

        if (isBluetoothEnabled()) {

            String nombreImpresora = ut.obtenerValor("nombreImpresora", c);
            if (nombreImpresora != null) {

                BluetoothDevice device = obtenerImpresora(nombreImpresora);
                if (device != null) {
                    String corte = ut.armarCorte(efectivoContado, efectivoCalculado,
                            tarjetaContado, tarjetaCalculado);
                    if (corte != null) {
                        imprimirBackGroundCorte(device, corte);
                    }
                } else {
                    System.out.println("No se encontró la impresora configurada");
                }
            } else {
                System.out.println("Por favor configure primero una impresora");
            }
        } else {
            System.out.println("El Bluetooth esta apagado, por favor verifique");
        }
    }

    public void imprimirTicket(Context c, Activity a, long idTiendaGlobal, int idTicketImprimir) {
        Utilerias ut = new Utilerias();

        if (isBluetoothEnabled()) {

            String nombreImpresora = ut.obtenerValor("nombreImpresora", c);
            if (nombreImpresora != null) {

                BluetoothDevice device = obtenerImpresora(nombreImpresora);
                if (device != null) {
                    CajasDB cdb = new CajasDB();
                    Realm realmN = obtenerInstanciaBD(c);

                    String ticket = ut.armarTicket(idTicketImprimir, realmN);
                    if (ticket != null) {
                        imprimirBackGround(device, idTiendaGlobal, ticket);
                    }
                } else {
                    System.out.println("No se encontró la impresora configurada");
                }
            } else {
                System.out.println("Por favor configure primero una impresora");
            }
        } else {
            System.out.println("El Bluetooth esta apagado, por favor verifique");
        }
    }


    public void reImprimirTicket(Context c, Activity a, long idTiendaGlobal, ReporteTicketDetalleDTO detalle) {
        Utilerias ut = new Utilerias();

        if (isBluetoothEnabled()) {

            String nombreImpresora = ut.obtenerValor("nombreImpresora", c);
            if (nombreImpresora != null) {

                BluetoothDevice device = obtenerImpresora(nombreImpresora);
                if (device != null) {

                    String ticket = ut.armarTicketReimprimir(detalle.getVentas().get(0));
                    if (ticket != null) {

                        imprimirBackGround(device, idTiendaGlobal, ticket);

                    } else {
                        System.out.println("No se pudo generar el ticket");
                    }
                } else {
                    System.out.println("No se encontró la impresora configurada");
                }
            } else {
                System.out.println("Por favor configure primero una impresora");
            }
        } else {
            System.out.println("El Bluetooth esta apagado, por favor verifique");
        }
    }


  /*  public void cashdrawerOpen() {

        byte[] open = {27, 112, 48, 55, 121};
// byte[] cutter = {29, 86,49};
        String printer = PrinterName;
        PrintServiceAttributeSet printserviceattributeset = new HashPrintServiceAttributeSet();
        printserviceattributeset.add(new PrinterName(printer,null));
        PrintService[] printservice = PrintServiceLookup.lookupPrintServices(null, printserviceattributeset);
        if(printservice.length!=1){
            System.out.println("Printer not found");
        }
        PrintService pservice = printservice[0];
        DocPrintJob job = pservice.createPrintJob();
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        Doc doc = new SimpleDoc(open,flavor,null);
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        try {
            job.print(doc, aset);
        } catch (PrintException ex) {
            System.out.println(ex.getMessage());
        }
    }*/


    private String guardarImagenTemporal(byte[] jpeg) {
        File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "temp.jpg");
        String path = "";


        try {
            if (!photo.exists()) {
                photo.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(photo);
            path = photo.getAbsolutePath();
            fos.write(jpeg);
            fos.flush();
            fos.getFD().sync();
            fos.close();
        } catch (java.io.IOException e) {
            Log.e("PictureDemo", "Exception in photoCallback", e);
        }

        return path;
    }


    private void imprimirBackGround(BluetoothDevice device, long idTiendaGlobal, String bytes) {
        String tipoImpresora = "GENE";

        Utilerias ut = new Utilerias();

        UtileriasImpresion uim = new UtileriasImpresion();
        uim.setCenter(false);
        uim.setBold(false);
        uim.setdH(false);
        uim.setdW(false);


        TicketDB tdb = new TicketDB();
        Realm realm2 = ut.obtenerInstanciaBD(ctx);
    /*    ImagenTicketDTOLocal imagen = tdb.obtenerImagen(idTiendaGlobal, realm2);

        if (imagen != null && imagen.getImagen() != null) {
            uim.setTipoLetra(-1);

            uim.imprimirImagen(imagen.getImagen(), device, tipoImpresora, 3000);
        }
        */


        List<RowTicketLocal> headerst = tdb.obtenerRows("H", idTiendaGlobal, realm2);

        for (RowTicketLocal he : headerst
        ) {
            uim.setTipoLetra(he.getTamanioLetra());
            uim.mandarLetra(device, tipoImpresora, 400);
            uim.imprimirTicket((he.getTexto() + "\n"), device, tipoImpresora, 400);
        }
        String lineaCorte = "\n\n";
        uim.setTipoLetra(1);
        uim.mandarLetra(device, tipoImpresora, 400);
        uim.imprimirTicket(lineaCorte, device, tipoImpresora, 400);
        String error = uim.imprimirTicket(bytes, device, tipoImpresora, 400);

        List<RowTicketLocal> footers = tdb.obtenerRows("F", idTiendaGlobal, realm2);

        for (RowTicketLocal fo : footers
        ) {
            uim.setTipoLetra(fo.getTamanioLetra());
            uim.mandarLetra(device, tipoImpresora, 400);
            uim.imprimirTicket((fo.getTexto() + "\n"), device, tipoImpresora, 400);
        }
        uim.setTipoLetra(1);
        uim.imprimirTicket(lineaCorte, device, tipoImpresora, 400);
        //esto abre el cajon
        try {
            uim.setTipoLetra(0);
            uim.mandarLetra(device, tipoImpresora, 400);
        } catch (Exception ex) {
            System.out.println("Ocurrió un problema abriendo el cajon");
        }
        //
        if (realm2 != null && !realm2.isClosed()) {
            realm2.close();
        }


    }


    private void imprimirBackGroundCorte(BluetoothDevice device, String bytes) {
        String tipoImpresora = "GENE";

        Utilerias ut = new Utilerias();

        UtileriasImpresion uim = new UtileriasImpresion();
        uim.setCenter(false);
        uim.setBold(false);
        uim.setdH(false);
        uim.setdW(false);

        String lineaCorte = "\n\n";
        uim.setTipoLetra(1);
        uim.mandarLetra(device, tipoImpresora, 400);
        uim.imprimirTicket(lineaCorte, device, tipoImpresora, 400);
        String error = uim.imprimirTicket(bytes, device, tipoImpresora, 400);

        uim.setTipoLetra(1);
        uim.imprimirTicket(lineaCorte, device, tipoImpresora, 400);

    }


    private boolean appInstalledOrNot(String uri, Activity a) {
        PackageManager pm = a.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    Context ctx;
    Activity act;

    public void validaIrPuntoVenta(Context context, final Activity activity, Realm realm, Usuario permisos, String ventaVengo) {

        ctx = context;
        act = activity;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Utilerias ut = new Utilerias();
                if (ut.verificaConexion(ctx)) {
                    Realm realm = ut.obtenerInstanciaBD(ctx);
                    UtileriasSincronizacion uts = new UtileriasSincronizacion(ctx);
                    uts.sincronizarTodo(ctx, act, realm, Long.parseLong(ut.obtenerValor("idTienda", ctx)));
                    if (realm != null && !realm.isClosed()) {
                        realm.close();
                    }
                }
            }
        });

        if (realm == null || realm.isClosed()) {
            realm = obtenerInstanciaBD(ctx);
        }

        if (permisos.getUsarCaja()) {


            Utilerias ut = new Utilerias();
            ut.guardarValor("ventaVengo", ventaVengo, context);

            boolean seguir = true;
            if (ut.obtenerModoAplicacion(context)) {
                if (ut.verificaConexion(context)) {


                    CajaDTOLocal caja = ut.obtenerCajaActual(context, activity, realm);

                    if (caja != null) {
                        cerrarRealmN(realm);
                        if (esPantallaChica(activity)) {
                            Intent i = new Intent(context, PuntoVentaChicoActivity.class);
                            activity.startActivity(i);
                        } else {
                            Intent i = new Intent(context, PuntoVentaActivity.class);
                            activity.startActivity(i);
                        }

                    } else {
                        Intent i = new Intent(context, AbrirCajaActivity.class);
                        activity.startActivity(i);
                    }

                } else {
                    seguir = false;
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(activity,
                                    "No puedes ingresar en modo online, ya que no tienes una conexón a la red", Toast.LENGTH_LONG);
                        }
                    });

                }
            } else {


                CajaDTOLocal caja = ut.obtenerCajaActual(context, activity, realm);

                if (caja != null) {
                    cerrarRealmN(realm);
                    if (esPantallaChica(activity)) {
                        Intent i = new Intent(context, PuntoVentaChicoActivity.class);
                        activity.startActivity(i);
                    } else {
                        Intent i = new Intent(context, PuntoVentaActivity.class);
                        activity.startActivity(i);
                    }

                } else {
                    Intent i = new Intent(context, AbrirCajaActivity.class);
                    activity.startActivity(i);
                }
            }


        } else {
            Toast.makeText(context, "No tienes permiso para usar el punto de venta", Toast.LENGTH_LONG);
        }
    }

    public void cerrarRealmN(Realm realm) {
        if (realm != null && !realm.isClosed()) {
            realm.close();
        }
    }

    public boolean esPantallaChica(Activity activity) {
        int screenSize = activity.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        boolean mostrarPantallaChica = true;
        switch (screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                mostrarPantallaChica = true;
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                mostrarPantallaChica = true;
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                mostrarPantallaChica = true;
                break;
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                mostrarPantallaChica = false;
                break;
            default:
                mostrarPantallaChica = true;
        }

        return mostrarPantallaChica;
    }


    public Set<BluetoothDevice> obtenerDispositivos() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        return pairedDevices;
    }


    public static void log(Context context, String datos, Exception ex) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        String sFileName = "logAnegocios" + df.format(new Date()) + ".txt";
        try {
            File root = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DOWNLOADS);
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);

            FileWriter writer = new FileWriter(gpxfile, true);
            String version = BuildConfig.VERSION_NAME;
            writer.append("\r\n" + df2.format(new Date()) + " -> " + version + " -> " + datos);
            if (ex != null) {
                StackTraceElement[] valores = ex.getStackTrace();
                if (valores != null && valores.length > 0) {
                    for (StackTraceElement l : valores) {
                        writer.append("\r\n Datos: " + l.getFileName() + " - " + l.getMethodName() + " - " + l.getLineNumber());
                    }
                }
            }
            writer.flush();
            writer.close();
            scanFile(context, gpxfile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void scanFile(Context ctxt, File f) {
        MediaScannerConnection
                .scanFile(ctxt, new String[]{f.getAbsolutePath()},
                        null, null);
    }

}
