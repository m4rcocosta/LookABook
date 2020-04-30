package uni.mobile.mobileapp.guiAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import uni.mobile.mobileapp.R;
import uni.mobile.mobileapp.rest.DownloadImageTask;


public class ListAdapter extends BaseAdapter {

    Context context;
    private final ArrayList<String> titles;
    private final ArrayList<String> authors;
    private final ArrayList<String> images;
    private final int defaultImage;

    public ListAdapter(Context context, ArrayList<String> titles, ArrayList<String> authors, ArrayList<String> images,int defaultImage) {
        //super(context, R.layout.single_list_app_item, utilsArrayList);
        this.context = context;
        this.titles = titles;
        this.authors = authors;
        if (images != null){
            this.images = images;
    }
        else {
            this.images = null;
        }
        this.defaultImage=defaultImage;
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.single_list_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.aNametxt);
            viewHolder.txtVersion = (TextView) convertView.findViewById(R.id.aVersiontxt);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.appIconIV);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.txtName.setText(titles.get(position) );
        if(authors!=null)
            viewHolder.txtVersion.setText("Authors: " + authors.get(position)!=null?authors.get(position):"" );
        if(images!=null && images.get(position)!=null)
            new DownloadImageTask(viewHolder.icon).execute(images.get(position));
        else
        viewHolder.icon.setImageResource(defaultImage);

        return convertView;
    }

    private static class ViewHolder {

        TextView txtName;
        TextView txtVersion;
        ImageView icon;

    }
}