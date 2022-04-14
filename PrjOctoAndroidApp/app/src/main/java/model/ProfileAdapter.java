package model;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prjoctoandroidapp.PlaygroundActivity;
import com.example.prjoctoandroidapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;


import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileAdapter extends FirebaseRecyclerAdapter<Profile, ProfileAdapter.myViewHolder> {

    EditText nickName, myage;
    Button btnUpdate;
    Context context;


    public ProfileAdapter(@NonNull FirebaseRecyclerOptions<Profile> options, Context context) {
        super(options);
        this.context = context;
    }

    public ProfileAdapter(@NonNull FirebaseRecyclerOptions<Profile> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Profile profile) {
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();

        holder.nName.setText(profile.getNickName());

        Glide.with(holder.img.getContext())
                .load(profile.getAvatarUrl())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);


        //edit form pop up
            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
                        .setExpanded(true, 1400)
                        .create();

                View view = dialogPlus.getHolderView();

                nickName = view.findViewById(R.id.edNickname2);
                myage = view.findViewById(R.id.edAge2);

                btnUpdate = view.findViewById(R.id.btnUpdate);

                nickName.setText(profile.getNickName());
                myage.setText(String.valueOf(profile.getAge()));

                dialogPlus.show();

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("nickName", nickName.getText().toString());
                        map.put("age", Integer.parseInt(myage.getText().toString()));

                        FirebaseDatabase.getInstance().getReference("users").child(user).child("profiles")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.nName.getContext(), "Profile updated!", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(holder.nName.getContext(), "ERROR while updating.", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });
            }
        });


        holder.btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.img.getContext());
                builder.setTitle("Are you sure you want to delete this profile?");
                builder.setMessage("You will lose all progress.");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference("users").child(user).child("profiles")
                                .child(getRef(position).getKey()).removeValue();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.nName.getContext(), "Cancelled.", Toast.LENGTH_LONG).show();
                    }
                });

                builder.show();
            }
        });

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.img.getContext(), PlaygroundActivity.class);
               String key= FirebaseDatabase.getInstance().getReference("users").child(user).child("profiles")
                        .child(getRef(position).getKey()).getKey();
               intent.putExtra("key",key);
                view.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);
        //view.setOnClickListener();
        return new myViewHolder(view);
    }


    static class myViewHolder extends RecyclerView.ViewHolder {


        CircleImageView img;
        TextView nName;

        Button btnEdit, btnDelete;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = (CircleImageView)itemView.findViewById(R.id.imgDefault);
            nName = (TextView)itemView.findViewById(R.id.tvUsername);

            btnEdit = (Button)itemView.findViewById(R.id.btnEdit);
            btnDelete =(Button)itemView.findViewById(R.id.btnDelete);


        }

    }

}
