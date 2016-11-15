package br.com.gotorcida.gotorcida.adapter.user;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.utils.ItemClickListener;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_IMAGES_BASE;

public class TeamRosterListAdapter extends RecyclerView.Adapter {

    List<JSONObject> athlete;
    Context context;
    JSONObject data;
    TeamRosterListHolder holder;

    public TeamRosterListAdapter(List<JSONObject> athlete, Context context){
        this.athlete = athlete;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_team_roster, parent, false);
        holder = new TeamRosterListHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        data = athlete.get(position);
        holder.setIsRecyclable(false);

         final TeamRosterListHolder holder = (TeamRosterListHolder) viewHolder;
        try {
            holder.nameAthlete.setText(data.getString("name"));
            String positionFull = data.getString("position");
            String positionSig = positionFull.substring(positionFull.indexOf("-")+1, positionFull.length());
            holder.athletePosition.setText(positionSig);
            holder.athleteId.setText(data.getString("id"));

            String auxImg = data.getString("urlImage");
            if(!auxImg.isEmpty()){
                Glide.with(context).load(URL_IMAGES_BASE + auxImg +".png").asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.athleteImagePernil) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        holder.athleteImagePernil.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int pos) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return athlete.size();
    }

}

