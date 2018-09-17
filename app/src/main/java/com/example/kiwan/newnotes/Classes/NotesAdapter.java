package com.example.kiwan.newnotes.Classes;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiwan.newnotes.Activity.MainActivity;
import com.example.kiwan.newnotes.Activity.ShowNotes;
import com.example.kiwan.newnotes.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.HolderNotes> {

    long id;
    int countFirst = 0;
    MainActivity mainActivity;
    boolean isChecked = false;
    ArrayList<Content> arrayList;
    ArrayList<Long> selection_list;
    ArrayList<Content> contentArrayList;
    Context context;
    public String[] array;


    public NotesAdapter( Context context, ArrayList<Content> arrayList ) {
        this.arrayList = arrayList;
        this.context = context;
        mainActivity = (MainActivity) context;
    }

    @NonNull
    @Override
    public HolderNotes onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {
        selection_list = new ArrayList<>();
        contentArrayList = new ArrayList<>();
        return new HolderNotes(LayoutInflater.from(context).inflate(R.layout.item, parent, false), mainActivity);
    }

    @Override
    public void onBindViewHolder( @NonNull final HolderNotes holder, final int position ) {

        final Content content = arrayList.get(position);
        holder.title.setText(content.getTitle());
        holder.subject.setText(content.getSubject());
        holder.Date.setText(content.getDate());

        if (content.getArray_voice() != null) {
            holder.imageMic.setVisibility(View.VISIBLE);
        }


        holder.itemView.setTag(content.get_id());

        if (content.getImagePhoto() != null) {

            holder.imageNotes.setVisibility(View.VISIBLE);
            holder.imageNotes.setImageBitmap(content.getImagePhoto());
        } else {
            holder.imageNotes.setVisibility(View.INVISIBLE);
        }


        if (mainActivity.is_in_action_mode)

        {
            holder.checkBox.setVisibility(View.VISIBLE);

        } else

        {
            holder.checkBox.setVisibility(View.INVISIBLE);
        }


        holder.checkBox.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick( View v ) {

                if (((CheckBox) v).isChecked()) {

                    selection_list.add(arrayList.get(position).get_id());
                    contentArrayList.add(arrayList.get(position));
                    Toast.makeText(mainActivity, String.valueOf(arrayList.get(position).get_id()), Toast.LENGTH_SHORT).show();
                    countFirst = countFirst + 1;
                    updateCounter(countFirst);
                    mainActivity.clickCheckBox();

                } else {

                    selection_list.remove(arrayList.get(position).get_id());
                    contentArrayList.remove(arrayList.get(position));
                    countFirst = countFirst - 1;
                    updateCounter(countFirst);
                }

                if (countFirst == arrayList.size()) {
                    mainActivity.checkAll.setChecked(true);
                } else {
                    mainActivity.checkAll.setChecked(false);
                }
                if (countFirst == 1) {
                    mainActivity.share.setVisibility(View.VISIBLE);
                } else {
                    mainActivity.share.setVisibility(View.INVISIBLE);
                }


            }
        });


        // when checked all(Checkbox in menu)  checkboxes in recyclerView is checked
        mainActivity.checkAll.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick( View v ) {

                if (((CheckBox) v).isChecked()) {

                    isChecked = true;
                    notifyDataSetChanged();
                    countFirst = arrayList.size();

                    updateCounter(countFirst);
                } else {
                    isChecked = false;
                    notifyDataSetChanged();
                    updateCounter(0);
                    countFirst = 0;
                }


            }


        });

        mainActivity.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                NotesHelper notesHelper = new NotesHelper(context);

                notesHelper.deleteData(selection_list);
                removeData(contentArrayList);

                mainActivity.is_in_action_mode = false;
                mainActivity.checkAll.setVisibility(View.INVISIBLE);
                mainActivity.counterText.setVisibility(View.INVISIBLE);
                countFirst = 0;
                updateCounter(countFirst);

            }
        });

        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                //     Toast.makeText(context, "clicked ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ShowNotes.class);
                long _idd = (long) holder.itemView.getTag();
                intent.putExtra("idd", _idd);
                context.startActivity(intent);
            }
        });


        holder.v.setOnLongClickListener(new View.OnLongClickListener()

        {
            @Override
            public boolean onLongClick( View v ) {

                mainActivity.doOnLongClick();
                return true;
            }
        });


        if (isChecked)

        {
            holder.checkBox.setChecked(true);

        } else

        {
            holder.checkBox.setChecked(false);

        }

    }


    @Override
    public int getItemCount() {

        return arrayList.size();
    }

    public void updateCounter( int count ) {
        if (count < 0) {
            return;
        }
        if (count == 0) {
            mainActivity.counterText.setText("0");
        } else {
            mainActivity.counterText.setText(String.valueOf(count));
        }
    }

    public void removeData( ArrayList<Content> list ) {
        for (Content content : list) {
            arrayList.remove(content);
        }
        notifyDataSetChanged();
    }

    public class HolderNotes extends RecyclerView.ViewHolder {
        TextView title, subject, Date;
        public CheckBox checkBox;
        ImageView imageNotes, imageMic;
        MainActivity mainActivity;

        View v;

        public HolderNotes( View itemView, MainActivity mainActivity ) {
            super(itemView);
            this.v = itemView;
            this.mainActivity = mainActivity;
            title = itemView.findViewById(R.id.title_notes);
            subject = itemView.findViewById(R.id.subject_notes);
            Date = itemView.findViewById(R.id.date);
            checkBox = itemView.findViewById(R.id.checkbox);
            imageNotes = itemView.findViewById(R.id.image_show);
            imageMic = itemView.findViewById(R.id.micImage);

        }

    }
}
