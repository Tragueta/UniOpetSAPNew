package com.example.guilhermetragueta.uniopetsap20.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guilhermetragueta.uniopetsap20.R;
import com.example.guilhermetragueta.uniopetsap20.model.Usuario;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PerfilUsuario extends Activity {

    private TextView txtNome;
    private TextView txtSobrenome;
    private TextView txtEmail;
    private Button btnAlterarDados;
    private Button btnVoltarHome;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference database;
    ArrayList<String> listDadosUsuarioLogado;
    private ImageView imgPerfil;
    private StorageReference storageRef;
    private File testeFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        txtNome = findViewById(R.id.txtNome);
        txtSobrenome = findViewById(R.id.txtSobrenome);
        txtEmail = findViewById(R.id.txtEmail);
        btnAlterarDados = findViewById(R.id.btnAlterarDados);
        btnVoltarHome = findViewById(R.id.btnVoltarHome);
        firebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        imgPerfil = findViewById(R.id.imgPerfil);

        //Recebendo o usuario logado no sistema
        ArrayList<String> listDadosAcesso = receberDadosUsuario();

        // Recuperacao e apresentação dos dados de acesso
        String idAcesso = listDadosAcesso.get(0);

        // Busca o usuario logado no sistema na base do Firebase
        database = firebaseDatabase.getReference("Usuario/" + idAcesso);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Montando o objeto usuario
                Usuario usuarioLogado = dataSnapshot.getValue(Usuario.class);
                txtNome.setText(usuarioLogado.getNome());
                txtSobrenome.setText(usuarioLogado.getSobrenome());
                txtEmail.setText(usuarioLogado.getEmailAcesso());
                String imgPerfil = usuarioLogado.getImagemPerfil().equals("") ? "" : usuarioLogado.getImagemPerfil().toString();
                usuarioLogado.setImagemPerfil(imgPerfil);

                if(!usuarioLogado.getImagemPerfil().equals("") || !usuarioLogado.getImagemPerfil().isEmpty()){

                    try {
                        setImagemPerfil(usuarioLogado.getImagemPerfil());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                convertUserToList(usuarioLogado);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(PerfilUsuario.this, "Ops! Houve um problema durante a recuperação do dados. Por gentileza, tente novamente.", Toast.LENGTH_SHORT).show();
            }
        });

        btnAlterarDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Recebendo o usuario logado no sistema
                ArrayList<String> listDadosAcesso = receberDadosUsuario();

                Intent alterarDados = new Intent(PerfilUsuario.this, AlterarPerfilUsuario.class);
                alterarDados.putStringArrayListExtra("listDadosAcesso", listDadosAcesso);
                startActivity(alterarDados);
                finish();
            }
        });

        // Metodo utilizado para redirecionar o usuario para tela 'Home'
        btnVoltarHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callActivity(Home.class);
            }
        });

    }

    // Metodo utilizado para recuperar e usar a imagem de perfil do usuario logado
    public void setImagemPerfil(String nomeImg) throws IOException {

        // Recebe a referente so storage
        storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://uniopetsapnew.appspot.com");

        // Localizo uma imagem de acordo com o parametro passado
        StorageReference storageReference = storageRef.child(nomeImg);

        // Crio um arquivo temporario para posteriormente passar a imge do Storage para o mesmo
        final File localFile = File.createTempFile("images", "jpg");
        storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Passa a imagem recuperada do storage para o arquivo temporario e para para o Bitmap
                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());

                // Exibe a imagem recuperada para o usuario
                imgPerfil.setImageBitmap(bitmap);
            }
        });
    }

    // Converte os dados do usuario em uma arraylist de string
    public void convertUserToList(Usuario user){
        this.listDadosUsuarioLogado.add(0, user.getidAcesso());
        this.listDadosUsuarioLogado.add(1, user.getEmailAcesso());
        this.listDadosUsuarioLogado.add(2, user.getId());
        this.listDadosUsuarioLogado.add(3, user.getNome());
        this.listDadosUsuarioLogado.add(4, user.getSobrenome());
        this.listDadosUsuarioLogado.add(5, user.getImagemPerfil());
    }

    // Metodo utilizado para recuperar uma lista de string com os dados de acesso do usuario logado
    public ArrayList<String> receberDadosUsuario() {
        Intent intent = getIntent();
        return this.listDadosUsuarioLogado = intent.getStringArrayListExtra("dadosAcesso");
    }

    // Metodo usado para trocar de telas
    private void callActivity(Class newActivity) {
        Intent newIntent = new Intent(PerfilUsuario.this,newActivity);
        startActivity(newIntent);
        finish();
    }
}
