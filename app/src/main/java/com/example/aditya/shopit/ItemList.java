package com.example.aditya.shopit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.aditya.shopit.Interface.ItemClickListener;
import com.example.aditya.shopit.Model.Category;
import com.example.aditya.shopit.Model.Item;
import com.example.aditya.shopit.ViewHolder.ItemViewHolder;
import com.example.aditya.shopit.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ItemList extends AppCompatActivity {


    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    private final static String TAG = "ItemList";



    MaterialSearchBar materialSearchBar;

    FirebaseDatabase database = null;
    DatabaseReference ItemList = null;
    String categoryId = "";
    FirebaseRecyclerAdapter<Item, ItemViewHolder> adapter;

    FirebaseRecyclerAdapter<Item, ItemViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        //firebase
        database = FirebaseDatabase.getInstance();
        ItemList = database.getReference("Items");
        recycler_menu = (RecyclerView) findViewById(R.id.recycler_item);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);


        if(getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if(!categoryId.isEmpty() && categoryId != null){

            loadItem(categoryId);
        }


        materialSearchBar = (MaterialSearchBar)findViewById(R.id.searchBar);
        materialSearchBar.setHint("Search Item");
        loadSuggest();

        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);

        addTextChangeListener();
        onSearchActionListener();
    }
    private void loadItem(String categoryID){
        FirebaseRecyclerOptions<Item> options =
                new FirebaseRecyclerOptions.Builder<Item>()
                        .setQuery(ItemList.orderByChild("MenuId").equalTo(categoryId), Item.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Item, ItemViewHolder>(options)  {
            @Override
            protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull Item model) {


                holder.Item_name.setText(model.getName());
                Picasso.get().load(model.getImage()).into(holder.Item_image);
                final Item clickItem = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent Item = new Intent(ItemList.this,Item_details.class);
                        Item.putExtra("ItemId",adapter.getRef(position).getKey());
                        startActivity(Item);
                    }

                });
            }

            @NonNull
            @Override
            public  ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items,viewGroup, false);
                ItemViewHolder viewHolder = new ItemViewHolder(v);
                return viewHolder;
            }

        };
        Log.d("TAG", ""+adapter.getItemCount());
        recycler_menu.setAdapter(adapter);
        adapter.startListening();
    }

    private void addTextChangeListener() {
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                List<String> suggest = new ArrayList<>();
                for (String search : suggestList) {
                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase())) {
                        suggest.add(search);
                    }
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void onSearchActionListener() {
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                Log.d(TAG, "enabled: " + enabled);
                if(!enabled) {
                    recycler_menu.setAdapter(adapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                Log.d(TAG, "text: " + text);
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }


    private void startSearch(CharSequence text) {
        FirebaseRecyclerOptions<Item> options =
                new FirebaseRecyclerOptions.Builder<Item>()
                        .setQuery(ItemList.orderByChild("Name").equalTo(text.toString()), Item.class)
                        .build();

        searchAdapter = new FirebaseRecyclerAdapter<Item, ItemViewHolder>(options)  {
            @Override
            protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull Item model) {


                holder.Item_name.setText(model.getName());
                Picasso.get().load(model.getImage()).into(holder.Item_image);
                final Item clickItem = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent Item = new Intent(ItemList.this,Item_details.class);
                        Item.putExtra("ItemId",searchAdapter.getRef(position).getKey());
                        startActivity(Item);
                    }

                });
            }

            @NonNull
            @Override
            public  ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items,viewGroup, false);
                ItemViewHolder viewHolder = new ItemViewHolder(v);
                return viewHolder;
            }

        };
        recycler_menu.setAdapter(searchAdapter);
        searchAdapter.startListening();
    }


    private void loadSuggest() {
        ItemList.orderByChild("MenuId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Item item = postSnapshot.getValue(Item.class);
                            suggestList.add(item.getName());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

}