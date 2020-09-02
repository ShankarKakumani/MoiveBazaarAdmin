package com.sparky.moivebazaaradmin.RecyclerItem;

import android.os.Bundle;

import com.sparky.moivebazaaradmin.R;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerItemActivity extends AppCompatActivity {

    //a list to store all the products
    List<Product> productList;

    //the recyclerview
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_item_adapter);

        //getting the recyclerview from xml
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initializing the productlist
        productList = new ArrayList<>();


        //adding some items to our list
        productList.add(
                new Product(
                        "Charlie Chaplin",
                        "https://media1.santabanta.com/full1/Global%20Celebrities(M)/Charlie%20Chaplin/charlie-chaplin-1a.jpg",
                        "dzj0FoaUrFE"));

        productList.add(
                new Product(
                        "You Laugh You Lose I Friendship Day Edition I Jordindian",
                        "https://i.ytimg.com/an_webp/17CRtzWQk-0/mqdefault_6s.webp?du=3000&sqp=CP392fkF&rs=AOn4CLBhMixKH9073J6XqK8R_SOBiwwpUQ",
                        "17CRtzWQk-0"));

        productList.add(
                new Product(
                        "I am Title Three",
                        "Url3",
                        "13.3 inch, Silver, 1.35 kg"));

        //creating recyclerview adapter
        ProductAdapter adapter = new ProductAdapter(this, productList);

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }
}
