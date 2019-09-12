package com.example.aditya.shopit;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.aditya.shopit.Database.Database;
import com.example.aditya.shopit.Model.Item;
import com.example.aditya.shopit.Model.Order;
import com.example.aditya.shopit.ViewHolder.ItemViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Item_details extends AppCompatActivity {

    TextView Item_name, Item_price, Item_description;
    ImageView Item_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;
    FirebaseDatabase database;
    DatabaseReference Items;
    Item currentItem;

    String ItemId = "";
    FirebaseRecyclerAdapter<Item, ItemViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        database = FirebaseDatabase.getInstance();
        Items = database.getReference("Items");
        numberButton = (ElegantNumberButton) findViewById(R.id.number_button);
        btnCart = (FloatingActionButton) findViewById(R.id.btnCart);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Database(getBaseContext()).addToCart(new Order(
                        ItemId,
                        currentItem.getName(),
                        numberButton.getNumber(),
                        currentItem.getPrice(),
                        currentItem.getDiscount()
                ));

                Toast.makeText(Item_details.this, "Added to cart", Toast.LENGTH_SHORT).show();

            }
        });

        Item_description = (TextView) findViewById(R.id.Item_description);
        Item_name = (TextView) findViewById(R.id.Item_name);
        Item_price = (TextView) findViewById(R.id.Item_price);
        Item_image = (ImageView) findViewById(R.id.img_Item);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        if (getIntent() != null)
            ItemId = getIntent().getStringExtra("ItemId");
        if (!ItemId.isEmpty()) {
            getDetailItem(ItemId);
        }
    }
                 private void getDetailItem(String ItemID){
                    Items.child(ItemId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                              currentItem = dataSnapshot.getValue(Item.class);

                                 Picasso.get().load(currentItem.getImage()).into(Item_image);

                                 collapsingToolbarLayout.setTitle(currentItem.getName());

                                 Item_price.setText(currentItem.getPrice());

                                 Item_name.setText(currentItem.getName());

                                 Item_description.setText(currentItem.getDescription());

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
                });
    }
}
