package com.byteshaft.solidariedadediria.sidebar_fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.byteshaft.solidariedadediria.R;
import com.byteshaft.solidariedadediria.database.DatabaseClient;
import com.byteshaft.solidariedadediria.database.Movement;
import com.byteshaft.solidariedadediria.utils.AppGlobals;

import java.util.List;

public class Movements extends Fragment {

    private View mBaseView;
    private RecyclerView recyclerView;
    private ListAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.movements));
        mBaseView = inflater.inflate(R.layout.fragment_movements, container, false);
        recyclerView = mBaseView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getAllRecords();

        return mBaseView;
    }

    private void getAllRecords() {
        class GetTasks extends AsyncTask<Void, Void, List<Movement>> {
            @Override
            protected List<Movement> doInBackground(Void... voids) {
                List<Movement> detailList = DatabaseClient
                        .getInstance(getContext())
                        .getAppDatabase()
                        .movementDao()
                        .getAllMovements(Integer.parseInt(AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_ID)));
                return detailList;
            }

            @Override
            protected void onPostExecute(List<Movement> detailList) {
                super.onPostExecute(detailList);
                adapter = new ListAdapter(detailList);
                recyclerView.setAdapter(adapter);
                System.out.println(detailList);
            }
        }
        GetTasks gt = new GetTasks();
        gt.execute();
    }

    public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
        private List<Movement> arrayList;

        private ListAdapter(List<Movement> arrayList) {
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delegate_movement, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, final int position) {
            System.out.println(arrayList.get(position).getInstituteName());
            holder.instituteName.setText(arrayList.get(position).getInstituteName());
            holder.money.setText(String.valueOf(arrayList.get(position).getMoney()));

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView instituteName;
            TextView money;

            ViewHolder(View view) {
                super(view);
                instituteName = view.findViewById(R.id.institute_name);
                money = view.findViewById(R.id.institute_money);
            }
        }
    }
}
