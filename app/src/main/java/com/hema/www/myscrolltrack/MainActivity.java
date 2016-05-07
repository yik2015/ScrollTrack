package com.hema.www.myscrolltrack;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hema.www.myscrolltrack.scroll.BottomTrackListener;
import com.hema.www.myscrolltrack.scroll.TopTrackListener;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        recyclerView.addItemDecoration(new TopDecoration(findViewById(R.id.main_top_view)));
        recyclerView.addOnScrollListener(new TopTrackListener(findViewById(R.id.main_top_view)));

        recyclerView.addOnScrollListener(
                new BottomTrackListener(findViewById(R.id.main_bottom_view)));

        recyclerView.setAdapter(new SimpleAdapter());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_check_resource_code:
                Intent it = new Intent(Intent.ACTION_VIEW);
                it.setData(Uri.parse("https://github.com/boybeak/ScrollTrack"));
                startActivity(it);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class SimpleAdapter extends RecyclerView.Adapter<SimpleHolder> {

        @Override
        public SimpleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View convertView = LayoutInflater.from(MainActivity.this)
                    .inflate(R.layout.layout_rv_item, null);
            return new SimpleHolder(convertView);
        }

        @Override
        public void onBindViewHolder(SimpleHolder holder, int position) {
            holder.mTv.setText("Test Item #" + position);
        }

        @Override
        public int getItemCount() {
            return 200;
        }
    }

    private class SimpleHolder extends RecyclerView.ViewHolder {
        public TextView mTv;

        public SimpleHolder(View itemView) {
            super(itemView);
            mTv = (TextView) itemView.findViewById(R.id.item_tv);
        }
    }
}

