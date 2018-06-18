package com.example.guilhermetragueta.uniopetsap20.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guilhermetragueta.uniopetsap20.model.Usuario;
import com.example.guilhermetragueta.uniopetsap20.R;
import com.example.guilhermetragueta.uniopetsap20.util.UtilityStorage;
import com.google.android.gms.internal.zzdwf;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AlterarPerfilUsuario extends Activity{

    private EditText nomeAlterado;
    private EditText sobrenomeAlterado;
    private EditText emailAlterado;
    private Button btnSalvarDados;
    private Button btnVoltarPerfilUsuario;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseRef;
    private TextView txtAlterarSenha;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    // Objeto do Firebase para trabalhar com o armazenamento de arquivo no firebase
    private StorageReference storageRef;
    // Objeto que exibirá a imagem selecionada pelo usuário*/
    private ImageView imagemPerfil;
    // Valores que vão aparecer na caixa de diálogo para que o usuário escolha o que fazer.*/
    private String[] items = {"Selecionar da Biblioteca.", "Cancelar"};
    // Constante utilizada para informar que o usuario escolheu a opçcao 'Selecionar da Biblioteca'
    private int SELECT_FILE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_perfil_usuario);

        nomeAlterado = findViewById(R.id.nomeAlterado);
        sobrenomeAlterado = findViewById(R.id.sobrenomeAterado);
        emailAlterado = findViewById(R.id.emailAlterado);
        btnSalvarDados = findViewById(R.id.btnSalvarDados);
        txtAlterarSenha = findViewById(R.id.txtAlterarSenha);
        btnVoltarPerfilUsuario = findViewById(R.id.btnVoltarPerfilUsuario);
        storageRef = FirebaseStorage.getInstance().getReference();
        imagemPerfil = findViewById(R.id.imgPerfilAlterarDados);

        // Recuperando os dados do usuario
        ArrayList<String> listDadosUsuarioLogado = receberDadosUsuario();

        nomeAlterado.setText(listDadosUsuarioLogado.get(3));
        sobrenomeAlterado.setText(listDadosUsuarioLogado.get(4));
        emailAlterado.setText(listDadosUsuarioLogado.get(1));
        String imagemPerfil = listDadosUsuarioLogado.get(5);
        if(!imagemPerfil.equals("") || !imagemPerfil.isEmpty()){

            try {
                setImagemPerfil(imagemPerfil);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        btnSalvarDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Validando os campos informados
                String nome = nomeAlterado.getText().toString();
                String sobrenome = sobrenomeAlterado.getText().toString();
                String email = emailAlterado.getText().toString();

                if(nome.isEmpty() || nome.equals("")){
                    nomeAlterado.setError("Favor preencher o campo 'Nome'.");
                    nomeAlterado.requestFocus();
                } else if(sobrenome.isEmpty() || sobrenome.equals("")){
                    sobrenomeAlterado.setError("Favor preencher o campo 'Sobrenome'.");
                    sobrenomeAlterado.requestFocus();
                } else if(email.isEmpty() || email.equals("")){
                    emailAlterado.setError("Favor preencher o campo 'E-mail'.");
                    emailAlterado.requestFocus();
                } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailAlterado.setError(" O endereço de e-mail informado está incorreto.\n Por gentileza, verifique e tente novamente.");
                    emailAlterado.requestFocus();
                } else{

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    user.updateEmail(emailAlterado.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(AlterarPerfilUsuario.this,
                                                "Ops! Houve um problema durante a aatualização dos seus dados" +
                                                        "\n Por gentileza, gente novamente.",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Usuario usuarioAlterado = new Usuario();
                                        usuarioAlterado.setNome(nomeAlterado.getText().toString());
                                        usuarioAlterado.setSobrenome(sobrenomeAlterado.getText().toString());
                                        usuarioAlterado.setEmailAcesso(emailAlterado.getText().toString());
                                        atualizarDados(usuarioAlterado);
                                        callActivity(Home.class);
                                    }
                                }
                            });
                }

            }
        });

        txtAlterarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> listDadosUsuarioLogado = receberDadosUsuario();
                Intent alterarDados = new Intent(AlterarPerfilUsuario.this, AlterarSenha.class);
                alterarDados.putStringArrayListExtra("listDadosAcesso", listDadosUsuarioLogado);
                startActivity(alterarDados);
                finish();
            }
        });

        btnVoltarPerfilUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callActivity(Home.class);
            }
        });
    }

    // Metodo utilizado para recuperar uma lista de string com os dados de acesso do usuario logado
    public ArrayList<String> receberDadosUsuario() {
        Intent intent = getIntent();
        ArrayList<String> listDadosAcesso = new ArrayList<>();
        return listDadosAcesso = intent.getStringArrayListExtra("listDadosAcesso");
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
                imagemPerfil.setImageBitmap(bitmap);
            }
        });
    }

    // Metodo resposavel por apresentar a nova tela
    private void callActivity(Class newActivity) {
        Intent newIntent = new Intent(AlterarPerfilUsuario.this, newActivity);
        startActivity(newIntent);
        finish();
    }

    public void alterarImagemPerfil(View v){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AlterarPerfilUsuario.this);
        alertBuilder.setTitle("Alterar Foto!");
        alertBuilder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int item) {
                boolean result = UtilityStorage.checkPermission(AlterarPerfilUsuario.this);
                if (items[item].equals("Selecionar da Biblioteca")) {
                    if(result)
                        selecionarImagemGaleria();
                } else if (items[item].equals("Cancelar")) {
                    dialogInterface.dismiss();
                }
            }
        });

        alertBuilder.show();
    }

    // Metodo responsavel por subir e atualizar a imagem do perfil
    public void selecionarImagemGaleria(){
        Intent intent = new Intent();
        /*Definimos que o filtro desta intent será qualquer tipo de imagem,
         * assim aparecendo apenas arquivos que respeitem este filtro*/
        intent.setType("image/*");
        /*A Action desta Intent será de seleção de conteúdo*/
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //Definimos que o usuário selecionou a requisição de galeria
        startActivityForResult(Intent.createChooser(intent, "Selecionar Arquivo"), SELECT_FILE);
    }

    /*Função que é executada caso seja solicitada alguma permissão em tempo de execução(Camera,Arquivos, etc...)*/
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case UtilityStorage.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selecionarImagemGaleria();
                }
                break;
            case UtilityStorage.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selecionarImagemGaleria();
                }
            break;
        }
    }

    /*Função executada após o resultado de uma Activity (cameraIntent() ou galleryIntent()*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
                onSelectFromGalleryResult(data);
        }
    }

    /*Função chamada caso o usuário tenha selecionado uma imagem da galeria*/
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bitmap = null;
        Uri filePath = null;
        if (data != null) {
            try {
                /*path armazena o caminho onde está salvo o arquivo a ser transferido para o servidor*/
                filePath = data.getData();
                /*o bitmap que será preenchido com a imagem selecionada pelo usuário na galeria*/
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            /*Criamos um arquivo temporário que será enviado para o FireBase*/
            uploadFile(filePath);
            //Exibimos a imagem selecionada pelo usuário.
            imagemPerfil.setImageBitmap(bitmap);
        }
    }

    /*Função que envia uma Imagem local para o FireBase*/
    private void uploadFile(Uri filePath){

        final String nomeImg = getNomeImagem();
        storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference storageReference = storageRef.child(nomeImg);

        if(filePath != null){

            storageReference.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();

                            firebaseDatabase = FirebaseDatabase.getInstance();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            databaseRef = firebaseDatabase.getReference("Usuario/" + user.getUid());

                            // Inserindo a imagem cadastrada nos dados do usuario logado no app
                            Map<String,Object> map = new HashMap<>();
                            String imagemPerfil = nomeImg;
                            setImagemPerfilInMap(map, imagemPerfil);

                            if(map.isEmpty()){
                                Toast.makeText(AlterarPerfilUsuario.this, "Problemas identificados durante salvamento da imagem.",
                                        Toast.LENGTH_SHORT).show();
                            } else{
                                databaseRef.updateChildren(map);
                                Toast.makeText(AlterarPerfilUsuario.this, "Imagem alterada com sucesso!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(AlterarPerfilUsuario.this, "Ops! Houve um problema durante a alteração da sua foto de eprfil",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }


    }

    // Metodo utilizado para alterar os dados do usuario
    public void atualizarDados(Usuario usuarioAlterado) {
        this.user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseRef = firebaseDatabase.getReference("Usuario/" + this.user.getUid());

        Map<String,Object> map = new HashMap<>();
        setNomeInMap(map, usuarioAlterado.getNome());
        setSobrenomeInMap(map, usuarioAlterado.getSobrenome());
        setEmailAcessoInMap(map, usuarioAlterado.getEmailAcesso());

        if(map.isEmpty()){
            Toast.makeText(AlterarPerfilUsuario.this, "Problemas identificados durante a alteração dos dados.",
                    Toast.LENGTH_SHORT).show();
        } else{
            databaseRef.updateChildren(map);
            Toast.makeText(AlterarPerfilUsuario.this, "Cadastro alterado com sucesso!", Toast.LENGTH_SHORT).show();
        }
    }

    // Metodo resposavel por atribuir o nome para imagem
    public String getNomeImagem() {

        // Recebendo e formatando a data atual
        SimpleDateFormat formatarData = new SimpleDateFormat("dd-MM-yyyy");
        Date dataAtual = new Date();
        String dataFormatada = formatarData.format(dataAtual);

        String randomUID = UUID.randomUUID().toString();

        String nomeImg = randomUID + "-" + dataFormatada + ".jpg";

        return nomeImg;
    }

    // Metodos do map
    public void setNomeInMap(Map<String,Object> map, String nomeAlterado){
        map.put("nome", nomeAlterado);
    }

    public void setSobrenomeInMap(Map<String,Object> map, String sobrenomeAlterado){
        map.put("sobrenome", sobrenomeAlterado);
    }

    public void setEmailAcessoInMap(Map<String,Object> map, String emailAcesso){
        map.put("emailAcesso", emailAcesso);
    }

    public void setImagemPerfilInMap(Map<String,Object> map, String imagemPerfil){
        map.put("imagemPerfil", imagemPerfil);
    }

}
