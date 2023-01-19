package com.example.mikhalychgarage;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mikhalychgarage.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class scanItemsActivity extends AppCompatActivity {
    public static EditText resultsearcheview;
    private FirebaseAuth firebaseAuth;
    ImageButton scantosearch;
    Button searchbtn;
    Adapter adapter;
    RecyclerView mrecyclerview;
    DatabaseReference mdatabaseReference;
    TextView CategoryType;

    String[] foundItemNames = { "Название", "Марка авто", "Цена", "Штрих-код"};
    String[] findItems = {"itemname", "itemcategory", "itemprice", "itembarcode"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_scan_items);
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser users = firebaseAuth.getCurrentUser();
        String finaluser=users.getEmail();
        String resultemail = finaluser.replace(".","");
        mdatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(resultemail).child("Items");
        resultsearcheview = findViewById(R.id.searchfield);
        scantosearch = findViewById(R.id.imageButtonsearch);
        searchbtn = findViewById(R.id.searchbtnn);
        CategoryType = findViewById(R.id.category);

        mrecyclerview = findViewById(R.id.recyclerViews);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mrecyclerview.setLayoutManager(manager);
        mrecyclerview.setHasFixedSize(true);


        mrecyclerview.setLayoutManager(new LinearLayoutManager(this));


        scantosearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ScanCodeActivitysearch.class));
            }
        });

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchtext = resultsearcheview.getText().toString();
                firebasesearch(searchtext);
            }
        });

        Spinner spinner = findViewById(R.id.spinner);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, foundItemNames);
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(itemSelectedListener);

    }

    public void firebasesearch(String searchtext){

        Query firebaseSearchQuery = mdatabaseReference.orderByChild((String) CategoryType.getText()).startAt(searchtext).endAt(searchtext+"\uf8ff");
        FirebaseRecyclerAdapter<Items, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Items, UsersViewHolder>
                (  Items.class,
                        R.layout.list_layout,
                        UsersViewHolder.class,
                        firebaseSearchQuery )
        {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, Items model,int position){

                viewHolder.setDetails(getApplicationContext(),model.getItembarcode(),model.getItemcategory(),model.getItemname(),model.getItemprice());
            }
        };

        mrecyclerview.setAdapter(firebaseRecyclerAdapter);
    }
    AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            // Получаем выбранный объект
            String item = (String)parent.getItemAtPosition(position);
            ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            ((TextView) parent.getChildAt(0)).setTextSize(20);
            CategoryType.setText(findItems[position].toString());

            Toast.makeText(getApplicationContext(), "Критерий поиска: " + foundItemNames[position],
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    public static class UsersViewHolder extends RecyclerView.ViewHolder{
        View mView;
            public UsersViewHolder(View itemView){
            super(itemView);
            mView =itemView;
        }

    public void setDetails(Context ctx,String itembarcode, String itemcategory, String itemname, String itemprice){
                TextView item_barcode = (TextView) mView.findViewById(R.id.viewitembarcode);
                TextView item_name = (TextView) mView.findViewById(R.id.viewitemname);
                TextView item_category = (TextView) mView.findViewById(R.id.viewitemcategory);
                TextView item_price = (TextView) mView.findViewById(R.id.viewitemprice);

                item_barcode.setText(itembarcode);
                item_category.setText(itemcategory);
                item_name.setText(itemname);
                item_price.setText(itemprice);
    }

    }
}
