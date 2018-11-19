package com.dnielectura;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.dnielectura.jj2000.J2kStreamDecoder;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import de.tsenger.androsmex.mrtd.DG11;
import de.tsenger.androsmex.mrtd.DG1_Dnie;
import de.tsenger.androsmex.mrtd.DG2;
import de.tsenger.androsmex.mrtd.DG7;

public class DataResult extends Activity {

    private DG1_Dnie m_dg1;
    private DG11     m_dg11;
    private DG2      m_dg2;
    private DG7      m_dg7;

    private Bitmap loadedImage;
    private Bitmap loadedSignature;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Quitamos la barra del título
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.data_result);

        // Almacenamos el contexto
        Context myContext = DataResult.this;

        Bundle extras = getIntent().getExtras();
        if(extras != null) {

            // Recuperamos los datos obtenidos en la lectura anterior
            byte [] m_dataDG1	= extras.getByteArray("DGP_DG1");
            byte []  m_dataDG2	= extras.getByteArray("DGP_DG2");
            byte [] m_dataDG7	= extras.getByteArray("DGP_DG7");
            byte [] m_dataDG11 	= extras.getByteArray("DGP_DG11");

            // Construimos los objetos Data Group que hayamos leído
            if(m_dataDG1!=null) m_dg1   = new DG1_Dnie(m_dataDG1);
            if(m_dataDG2!=null) m_dg2   = new DG2(m_dataDG2);
            if(m_dataDG7!=null) m_dg7   = new DG7(m_dataDG7);
            if(m_dataDG11!=null)m_dg11  = new DG11(m_dataDG11);

            TextView tvloc;

            /* CÓDIGO MÍO */
            String textoAMostrar = "";
            String nombre = "";
            String apellidos = "";
            String fecNac = "";
            String lugNac = "";
            Date fecNacDate = null;
            TextView textoMostrar;
            ////////////////////////////////////////////////////////////////////////
            // Información del DG1, si la tenemos
            if(m_dg1!=null) {

                nombre = m_dg1.getName();

                apellidos = m_dg1.getSurname();

                fecNac = m_dg1.getDateOfBirth();
                SimpleDateFormat formatoFecha = new SimpleDateFormat("dd.MM.yyyy");
                try{
                    fecNacDate = formatoFecha.parse(fecNac);
                } catch (ParseException ex){
                    ex.printStackTrace();
                }

            }

            ////////////////////////////////////////////////////////////////////////
            // Información del DG11, si la tenemos
            if(m_dg11!=null) {

                lugNac = m_dg11.getBirthPlace().replace("<", " (") + ")";

            }

            ////////////////////////////////////////////////////////////////////////
            // Información del DG2 (foto), si la tenemos
            ImageView ivFoto = (ImageView) findViewById(R.id.CITIZEN_data_tab_00);
            if(m_dataDG2!=null){
                try {
                    // Parseo de la foto en formato JPEG-2000
                    byte [] imagen = m_dg2.getImageBytes();
                    J2kStreamDecoder j2k = new J2kStreamDecoder();
                    ByteArrayInputStream bis = new ByteArrayInputStream(imagen);
                    loadedImage = j2k.decode(bis);
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
            }

            // Mostramos la foto si hemos podido decodificarla
            if(loadedImage!=null)
                ivFoto.setImageBitmap(loadedImage);
            else
                ivFoto.setImageResource(R.drawable.noface);

            ////////////////////////////////////////////////////////////////////////
            // Información del DG7, si la tenemos


            Date hoy = new Date();

            Calendar nac = Calendar.getInstance();
            nac.setTime(fecNacDate);


            Calendar today = Calendar.getInstance();
            today.setTime(hoy);

            //nac.set(Calendar.MONTH, today.get(Calendar.YEAR));

            nac.set(Calendar.YEAR, today.get(Calendar.YEAR));

           if(nac.before(today)) {
                nac.set(Calendar.YEAR, nac.get(Calendar.YEAR) + 1);
            }


            long diffTime = (nac.getTimeInMillis() - today.getTimeInMillis());

            textoAMostrar = "Hola amigo " + nombre + apellidos + " veo que estás usando la app de la asignatura de Criptografía.\n" +
                    "¿El día "+fecNac+" es tu cumpleaños? Solo te quedan " + (int)TimeUnit.MILLISECONDS.toDays(Math.abs(diffTime)) + "días" + ". Ve a celebrarlo a "+lugNac+"\n" +
                    "No olvides pedir un aprobado en Criptografía al soplar las velas";

        //textoAMostrar = "HOLA: " + nac.getTime() + " Mes: " + nac.get(Calendar.MONTH) + " Dia: "+nac.get(Calendar.DAY_OF_MONTH) + " Año: " +nac.get(Calendar.YEAR);
            textoMostrar = (TextView) findViewById(R.id.textomostrar);
            textoMostrar.setText(textoAMostrar);
        }
        ///////////////////////////////////////////////////////////////////////////////////
        // Botón de vuelta al Activity anterior
        Button btnNFCBack = (Button)findViewById(R.id.butVolver);
        btnNFCBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Volvemos a la pantalla principal
                Intent intent = new Intent(DataResult.this, DNIeLectura.class);
                startActivity(intent);
            }
        });
    }
}
