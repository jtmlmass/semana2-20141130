package com.example.personaform;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.Max;
import com.mobsandgeeks.saripaar.annotation.Min;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Validator.ValidationListener{

    @NotEmpty
    @Length(min = 9, max = 15)
    private EditText txtTelefono ;

    @NotEmpty
    @Length(min = 3, max = 10)
    private EditText txtNombre;

    @NotEmpty
    @Length(min = 3, max = 10)
    private EditText txtApellido;

    @NotEmpty
    @Email
    private EditText txtEmail;

    @NotEmpty
    private EditText dateFechaNacimiento;

    private Spinner spnEdad;
    private DatePickerDialog picker;
    private Validator validator;
    private Button btnEnviar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        validator = new Validator(this);
        validator.setValidationListener(this);

        ArrayList edades = new ArrayList();
        for(int i = 18; i < 24; i++){
            edades.add(i);
        }
        setContentView(R.layout.activity_main);
        spnEdad = findViewById(R.id.age_spinner);
        spnEdad.setAdapter(new ArrayAdapter<Integer>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, edades));
        txtApellido= findViewById(R.id.editTextApellido);
        txtNombre= findViewById(R.id.editTextNombre);
        txtEmail=findViewById(R.id.editTextEmail);
        txtTelefono=findViewById(R.id.editTextTelefono);
        dateFechaNacimiento=findViewById(R.id.editTextFechaNacimiento);
        dateFechaNacimiento.setInputType(InputType.TYPE_NULL);
        dateFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dateFechaNacimiento.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        btnEnviar=findViewById(R.id.button_send);
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSave_onClick(view);
            }
        });
    }

    public void enviarInformacion(View view) {
        String persona = new String(
                "Nombre: " + txtNombre.getText().toString() +
                        "\nApellido: " + txtApellido.getText().toString() +
                        "\nTelefono: " + txtTelefono.getText().toString() +
                        "\nEmail: " + txtEmail.getText().toString() +
                        "\nEdad: " + spnEdad.getSelectedItem().toString() +
                        "\nFecha Nacimiento: " + dateFechaNacimiento.getText().toString());
        Toast.makeText(this, persona, Toast.LENGTH_SHORT).show();
    }

    private void buttonSave_onClick(View view) {
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        String persona = new String(
                "Nombre: " + txtNombre.getText().toString() +
                        "\nApellido: " + txtApellido.getText().toString() +
                        "\nTelefono: " + txtTelefono.getText().toString() +
                        "\nEmail: " + txtEmail.getText().toString() +
                        "\nEdad: " + spnEdad.getSelectedItem().toString() +
                        "\nFecha Nacimiento: " + dateFechaNacimiento.getText().toString());
        Toast.makeText(this, persona, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
