import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListActivity extends AppCompatActivity {

    private Adapter mAdapter;
    private RecyclerView mRecyclerView;
    private SOService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.listActivity_Toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("List Activity");

        mService = ApiUtils.getSOService();
        
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mAdapter = new Adapter(this,new ArrayList<Item>(0), new Adapter.PostItemListener() {
            @Override
            public void onPostClick(long id) {
                Toast.makeText(ListActivity.this, "Post id is " + id, Toast.LENGTH_SHORT).show();
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        loadAnswers();
    }

    private void loadAnswers(){

        mService.getAnswers()
            .enqueue(new Callback<SOAnswersResponse>() {
            @Override
            public void onResponse(@NonNull Call<SOAnswersResponse> call, @NonNull Response<SOAnswersResponse> response) {

                if (response.isSuccessful()){

                    mAdapter.updateAnswers(response.body().getItems());
                }else {
                    int statusCode = response.code();
                    Toast.makeText(ListActivity.this,"Code "+statusCode,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SOAnswersResponse> call, @NonNull Throwable t) {
                Toast.makeText(ListActivity.this, t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

}
