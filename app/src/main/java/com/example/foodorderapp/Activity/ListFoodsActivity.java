package com.example.foodorderapp.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.Adapter.FoodListAdapter;
import com.example.foodorderapp.Domain.Foods;
import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.ActivityListFoodsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListFoodsActivity extends BaseActivity {

    ActivityListFoodsBinding binding;
    private RecyclerView.Adapter adapterListFood;
    private  int categoryId;
    private String categoryName;
    private String searchText;
    private boolean isSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityListFoodsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        initList();
        setVariable();


    }

    private void setVariable() {

    }

    private void initList() {
        DatabaseReference myRef = database.getReference("Foods");
        binding.progressBar.setVisibility(View.VISIBLE);
        ArrayList<Foods> list = new ArrayList<>();

        Query query;
        if(isSearch){
            query = myRef.orderByChild("Title").startAt(searchText).endAt(searchText + '\uf8ff');
        }else {
            query = myRef.orderByChild("CategoryId").equalTo(categoryId);
            Log.d("ListFoodsActivity", "Querying for CategoryId: " + categoryId); // Thêm log để kiểm tra

        }
        Log.d("ListFoodsActivity", "CategoryId: " + categoryId); // Thêm dòng này để kiểm tra
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot issue : snapshot.getChildren()){
                        list.add(issue.getValue(Foods.class));
                        Log.d("FirebaseData", "CategoryId: " + categoryId); // Thêm log để kiểm tra
                    }
                    if (list.size() > 0){
                        binding.foodListView.setLayoutManager(new GridLayoutManager(ListFoodsActivity.this, 2));
                        adapterListFood = new FoodListAdapter(list);
                        binding.foodListView.setAdapter(adapterListFood);
                    }
                    binding.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ListFoodsActivity", "Database error: " + error.getMessage()); // Thêm log lỗi
            }
        });
    }

    private void getIntentExtra() {
        categoryId = getIntent().getIntExtra("CategoryId", 0);
        categoryName = getIntent().getStringExtra("CategoryName");
        searchText = getIntent().getStringExtra("text");
        isSearch = getIntent().getBooleanExtra("isSearch",false);

        Log.d("ListFoodsActivity", "Received CategoryId: " + categoryId);
        Log.d("ListFoodsActivity", "Received CategoryName: " + categoryName);
        Log.d("ListFoodsActivity", "Received SearchText: " + searchText);
        Log.d("ListFoodsActivity", "Received isSearch: " + isSearch);

        binding.titleTxt.setText(categoryName);
        binding.backBtn.setOnClickListener(v -> finish());
    }
}