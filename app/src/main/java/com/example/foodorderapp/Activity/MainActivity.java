package com.example.foodorderapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.Adapter.BestFoodsAdapter;
import com.example.foodorderapp.Adapter.CategoryAdapter;
import com.example.foodorderapp.Domain.Category;
import com.example.foodorderapp.Domain.Foods;
import com.example.foodorderapp.Domain.Location;
import com.example.foodorderapp.Domain.Price;
import com.example.foodorderapp.Domain.Time;
import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLocation();
        initTime();
        initPrice();
        initBestFood();
        initCategory();
        setVariable();
    }

    private void setVariable() {
        binding.logoutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });

        binding.searchBtn.setOnClickListener(v -> {
            String text = binding.searchEdt.getText().toString();
            if(!text.isEmpty()){
                Intent intent = new Intent(MainActivity.this, ListFoodsActivity.class);
                intent.putExtra("text", text);
                intent.putExtra("isSearch",true);
                startActivity(intent);
            }
        });

        binding.cartBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CartActivity.class)));
    }

    private void initBestFood() {
        DatabaseReference myRef = database.getReference("Foods");
        binding.progressBarBestFood.setVisibility(View.VISIBLE);
        ArrayList<Foods> list = new ArrayList<>();
        Query query = myRef.orderByChild("BestFood").equalTo(true);
        // Đọc dữ lệu từ firebase
        //Khi thay BestFood trong Foods thành true thì sẽ hiển thị món đó lên mục BestFood

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //check data tồn tại hay không
              if(snapshot.exists()){
                    for (DataSnapshot issue: snapshot.getChildren()){
                      list.add(issue.getValue(Foods.class));
                   }
            //snapshot.getChildren() trả về một danh sách các DataSnapshot con (các phần tử trong node "Foods").

            //issue.getValue(Foods.class) chuyển đổi dữ liệu từ DataSnapshot thành đối tượng Foods (đảm bảo class Foods có constructor mặc định và các trường dữ liệu khớp với dữ liệu trên Firebase).

            //list.add(...) thêm đối tượng Foods vào danh sách list.

                   if(list.size() > 0 ){
                        binding.bestFoodView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL,false));
                       RecyclerView.Adapter adapter = new BestFoodsAdapter(list);
                        binding.bestFoodView.setAdapter(adapter);
                  }

            //list.size() > 0 kiểm tra xem danh sách list có phần tử nào không.
            //
            //binding.bestFoodView.setLayoutManager(...) thiết lập LayoutManager cho RecyclerView với hướng ngang (LinearLayoutManager.HORIZONTAL).
            //
            //RecyclerView.Adapter adapter = new BestFoodsAdapter(list) tạo một adapter mới với danh sách list.
            //
            //binding.bestFoodView.setAdapter(adapter) gán adapter cho RecyclerView để hiển thị dữ liệu.
                               binding.progressBarBestFood.setVisibility(View.GONE);
//                  Ẩn ProgressBar sau khi dữ liệu đã được tải xong.
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initCategory() {
        DatabaseReference myRef = database.getReference("Category");
        binding.progressBarCategory.setVisibility(View.VISIBLE);
        ArrayList<Category> list = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot issue : snapshot.getChildren()){
                        list.add(issue.getValue(Category.class));
                    }

                    if(list.size() > 0 ){
                        binding.categoryView.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));
                        RecyclerView.Adapter adapter = new CategoryAdapter(list);
                        binding.categoryView.setAdapter(adapter);
                    }

                    binding.progressBarCategory.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initLocation() {
        DatabaseReference myRef=database.getReference("Location");
        ArrayList<Location> list = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot issue: snapshot.getChildren()){
                        list.add(issue.getValue(Location.class));
                    }
                    ArrayAdapter<Location> adapter = new ArrayAdapter<>(MainActivity.this,R.layout.sp_item,list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.locationSp.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initTime() {
        DatabaseReference myRef=database.getReference("Time");
        ArrayList <Time> list = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot issue: snapshot.getChildren()){
                        list.add(issue.getValue(Time.class));
                    }
                    ArrayAdapter<Time> adapter = new ArrayAdapter<>(MainActivity.this,R.layout.sp_item,list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.timeSp.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initPrice() {
        DatabaseReference myRef=database.getReference("Price");
        ArrayList <Price> list = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot issue: snapshot.getChildren()){
                        list.add(issue.getValue(Price.class));
                    }
                    ArrayAdapter<Price> adapter = new ArrayAdapter<>(MainActivity.this,R.layout.sp_item,list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.priceSp.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}