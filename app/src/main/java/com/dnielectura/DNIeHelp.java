package com.dnielectura;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class DNIeHelp extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Quitamos la barra del título
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.help);

		Context myContext = DNIeHelp.this;

		// Ajustamos el tipo de letra de textos y de toda la tabla
		Typeface typeface = Typeface.createFromAsset(myContext.getAssets(),"fonts/HelveticaNeue.ttf");
		TableLayout miTabla = (TableLayout) findViewById(R.id.help_table);
		for(int i = 0, j = miTabla.getChildCount(); i < j; i++) {
			View view = miTabla.getChildAt(i);
			if (view instanceof TableRow) {
				TableRow row = (TableRow) view;
				for(int idx = 0; idx < row.getChildCount(); idx++)
				{
					View viewText = row.getChildAt(idx);
					if (viewText instanceof TextView)
						((TextView)viewText).setTypeface(typeface);
				}
			}
		}

		///////////////////////////////////////////////////////////////////////////////////
		// Botón de vuelta al Activity anterior
		Button btnNFCBack = (Button)findViewById(R.id.butDataVolver);
		btnNFCBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onBackPressed();
			}
		});

		///////////////////////////////////////////////////////////////////////////////////
		// Botón de lectura de nuevo documento
		Button btnLectura = (Button)findViewById(R.id.butDataLeer);
		btnLectura.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				// Devolvemos el activity adecuado
				Intent intent = new Intent(DNIeHelp.this, DNIeCanSelection.class);
				startActivity(intent);
			}
		});

	}
}