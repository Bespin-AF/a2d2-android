package af.bespin.a2d2.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import af.bespin.a2d2.R;
import af.bespin.a2d2.utilities.FormatUtils;

import java.util.List;

public class RideRequestDetailAdapter extends RecyclerView.Adapter<RideRequestDetailAdapter.DetailViewHolder>{
    public class DetailViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView value;

        private DetailViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.request_detail_title);
            value = itemView.findViewById(R.id.request_detail_value);
        }
    }


    //List of pairs containing the details' title (first) and value (second)
    private List<Pair<String, String>> details;


    public RideRequestDetailAdapter(List<Pair<String, String>> adapterDetails) {
        details = adapterDetails;
    }


    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View requestDetail = LayoutInflater.from(viewGroup.getContext())
                                           .inflate(R.layout.card_ride_request_detail, viewGroup, false);

        return new DetailViewHolder(requestDetail);
    }


    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder detailViewHolder, int position) {
        Pair<String, String> detail = details.get(position);
        String title = FormatUtils.formatString("%s:", detail.first);

        detailViewHolder.title.setText(title);
        detailViewHolder.value.setText(detail.second);
    }


    @Override
    public int getItemCount() {
        return details.size();
    }

}
