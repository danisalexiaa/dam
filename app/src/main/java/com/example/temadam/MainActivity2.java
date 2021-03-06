package com.example.temadam;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.ColorSpace;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity2 extends AppCompatActivity {

    private ListView listView;
    private Adapter twtAdapter;

    private PersoanaDAO persoanaDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        listView = findViewById(R.id.lista);

        twtAdapter = new Adapter(getList());
        listView.setAdapter(twtAdapter);

        persoanaDAO = Database.getInstance(this).getDatabase().persoanaDAO();

        writeDatabase();
        readFromDatabase();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Random r = new Random();
                int alegere = r.nextInt(2);
                if (alegere % 2 == 0) {
                    twtAdapter.updateLista(getList());
                } else {
                    twtAdapter.updateLista(getList2());
                }

            }



        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Tweets t1 = twtAdapter.getItem(position);
                Toast.makeText(MainActivity2.this, t1.toString(), Toast.LENGTH_LONG).show();
                return false;
            }
        });

        JSONReader reader = new JSONReader();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                reader.read("https://jsonkeeper.com/b/8490\n", new IResponse() {
                    @Override
                    public void onSuccess(List<Tweets> tweets) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity2.this, tweets.toString(), Toast.LENGTH_SHORT).show();
                                twtAdapter.updateLista(tweets);
                            }
                        });
                    }

                    @Override
                    public void onError(String mesaj) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity2.this, mesaj, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
        thread.start();

        Thread threadbd = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Persoana> lista = getPersoana();
                //persoanaDAO.insertAll(lista.get(0), lista.get(1));

                for(int i = 0; i<lista.size(); i++){
                    persoanaDAO.insertAll(lista.get(i));
                }

                List<Persoana> listaMail = persoanaDAO.getPersoanaNume("email@gmail.com");
                Log.v("Lungime email", listaMail.toString());

            }
        });
        threadbd.start();
    }

    private List<Tweets> getList() {
        ArrayList<Tweets> lista = new ArrayList<>();

        Tweets tweet1 = new Tweets(R.drawable.profilepic, "Tweet1", "user1", "username1@yahoo.com", "Ye on properly handsome returned throwing am no whatever. In without wishing he of picture no exposed talking minutes. Curiosity continual belonging offending so explained it exquisite. Do remember to followed yourself material mr recurred carriage. High drew west we no or at john. About or given on witty event. Or sociable up material bachelor bringing landlord confined. Busy so many in hung easy find well up. So of exquisite my an explained remainder. Dashwood denoting securing be on perceive my laughing so.");
        Tweets tweet2 = new Tweets(R.drawable.profilepic, "Tweet2", "user2", "username2@yahoo.com", "Is at purse tried jokes china ready decay an. Small its shy way had woody downs power. To denoting admitted speaking learning my exercise so in. Procured shutters mr it feelings. To or three offer house begin taken am at. As dissuade cheerful overcame so of friendly he indulged unpacked. Alteration connection to so as collecting me. Difficult in delivered extensive at direction allowance. Alteration put use diminution can considered sentiments interested discretion. An seeing feebly stairs am branch income me unable.");
        Tweets tweet3 = new Tweets(R.drawable.profilepic, "Tweet3", "user3", "username3@yahoo.com", "No depending be convinced in unfeeling he. Excellence she unaffected and too sentiments her. Rooms he doors there ye aware in by shall. Education remainder in so cordially. His remainder and own dejection daughters sportsmen. Is easy took he shed to kind.");

        lista.add(tweet1);
        lista.add(tweet2);
        lista.add(tweet3);

        return (lista);
    }

    private List<Tweets> getList2() {
        ArrayList<Tweets> lista = new ArrayList<>();

        Tweets tweet1 = new Tweets(R.drawable.profilepic, "Tweet1upd", "alexia", "alexia@yahoo.com", "Society excited by cottage private an it esteems. Fully begin on by wound an. Girl rich in do up or both. At declared in as rejoiced of together. He impression collecting delightful unpleasant by prosperous as on. End too talent she object mrs wanted remove giving.");
        Tweets tweet2 = new Tweets(R.drawable.profilepic, "Tweet2upd", "ad123", "ad123@yahoo.com", "Kept in sent gave feel will oh it we. Has pleasure procured men laughing shutters nay. Old insipidity motionless continuing law shy partiality. Depending acuteness dependent eat use dejection. Unpleasing astonished discovered not nor shy. Morning hearted now met yet beloved evening. Has and upon his last here must.");
        Tweets tweet3 = new Tweets(R.drawable.profilepic, "Tweet3upd", "andrei", "andrei@yahoo.comupd", "Pasture he invited mr company shyness. But when shot real her. Chamber her observe visited removal six sending himself boy. At exquisite existence if an oh dependent excellent. Are gay head need down draw. Misery wonder enable mutual get set oppose the uneasy. End why melancholy estimating her had indulgence middletons. Say ferrars demands besides her address. Blind going you merit few fancy their.");

        lista.add(tweet1);
        lista.add(tweet2);
        lista.add(tweet3);

        return (lista);
    }

    public List<Persoana> getPersoana(){
        Persoana pers1 = new Persoana("john", "john@mail.com", "14/01/1998", "password");
        Persoana pers2 = new Persoana("jim", "jim@yahoo.com", "7/02/1999", "password2");

        List<Persoana> lista = new ArrayList<>();
        lista.add(pers1);
        lista.add(pers2);
        return lista;

    }

    private void writeDatabase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ref");

        List<Tweets> lst = getList2();

        for(int i = 0; i< lst.size(); i++){
            DatabaseReference ref = database.getReference("Tweet" + i + "");
            ref.child("image").setValue(lst.get(i).getImage());
            ref.child("nume").setValue(lst.get(i).getNume());
            ref.child("user").setValue(lst.get(i).getUser());
            ref.child("mail").setValue(lst.get(i).getMail());
            ref.child("text").setValue(lst.get(i).getText());
        }

//        DatabaseReference myRef2 = database.getReference("ref2");
//
//        myRef.setValue("Hello, World!");
//        myRef2.setValue("test");
//        myRef2.child("copil").setValue("copil");
    }

    private void readFromDatabase(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Tweet1");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(Tweets.class).toString();
                Log.d("readdd", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("cancelled", "Failed to read value.", error.toException());
            }
        });
    }

}