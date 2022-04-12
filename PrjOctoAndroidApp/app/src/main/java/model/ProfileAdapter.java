package model;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.prjoctoandroidapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;


import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileAdapter extends FirebaseRecyclerAdapter<Profile, ProfileAdapter.myViewHolder> {

    EditText nickName, age;
    Button btnUpdate;

    public ProfileAdapter(@NonNull FirebaseRecyclerOptions<Profile> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Profile profile) {
        holder.nName.setText(profile.getNickName());

        Glide.with(holder.img.getContext())
                .load(profile.getAvatarUrl())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);

            /*holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
                        .setExpanded(true, 1400)
                        .create();

                View view = dialogPlus.getHolderView();

                uname = view.findViewById(R.id.edUsername);
                name = view.findViewById(R.id.edName);
                age = view.findViewById(R.id.edAge);

                btnUpdate = view.findViewById(R.id.btnUpdate);

                uname.setText(profile.getNickName());
                name.setText(profile.getPoints());
                age.setText(String.valueOf(profile.getAge()));

                dialogPlus.show();*/

/*                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("username", uname.getText().toString());
                        map.put("name", name.getText().toString());
                        map.put("age", age.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("profiles")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.username.getContext(), "Profile updated!", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(holder.username.getContext(), "ERROR while updating.", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });
            }
        }); */

        /*holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.img.getContext());
                builder.setTitle("Are you sure you want to delete this profile?");
                builder.setMessage("You will lose all progress.");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("profiles")
                                .child(getRef(position).getKey()).removeValue();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.username.getContext(), "Cancelled.", Toast.LENGTH_LONG).show();
                    }
                });

                builder.show();
            }
        });*/
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);
        return new myViewHolder(view);
    }


    class myViewHolder extends RecyclerView.ViewHolder {
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
