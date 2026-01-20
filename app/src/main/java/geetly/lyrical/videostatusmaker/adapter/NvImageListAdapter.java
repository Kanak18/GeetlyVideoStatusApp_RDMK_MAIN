package geetly.lyrical.videostatusmaker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import geetly.lyrical.videostatusmaker.R;
import geetly.lyrical.videostatusmaker.activity.NV_VideoEditorActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class NvImageListAdapter extends Adapter<NvImageListAdapter.MyViewHolder> {
    Context context;
    NV_VideoEditorActivity.clickAdapter clickAdapter;
    String[] tempImgUrl;
    String[] textArr;
    String[] txtLableArr;
    String[] txtValueArr;

    int videoArr = 0;
    int editTextArr = 0;

    BottomSheetDialog bottomSheetDialog;

    public NvImageListAdapter(Context context, int i, String[] strArr, NV_VideoEditorActivity.clickAdapter clickAdapter,int txtboxCount, String[] txtArr, String[] txtLableArr, String[] txtValueArr) {
        this.context = context;
        this.videoArr = i;
        this.tempImgUrl = strArr;
        this.clickAdapter = clickAdapter;
        this.editTextArr = txtboxCount;
        this.textArr = txtArr;
        this.txtLableArr = txtLableArr;
        this.txtValueArr = txtValueArr;
    }

    public int getItemCount() {
        return this.videoArr;
    }





    private void showBottomSheetDialog() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout);
        bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        TextView save_text = bottomSheetDialog.findViewById(R.id.btn_save_text);

        String[] strInput = getArrayList("lable");

        LinearLayout rootView;

        for (int count = 0; count < strInput.length; count++) {
           // Log.e("DEBUG", "Value of Json in adapter  "+strInput[count]);

            rootView = bottomSheetDialog.findViewById(R.id.rootView);
            View view = LayoutInflater.from(context).inflate(R.layout.temp_layout, null);
            TextInputLayout userNameIDTextInputLayout=view.findViewById(R.id.userIDTextInputLayout);
            TextInputEditText userNameInputEditText = view.findViewById(R.id.userIDTextInputEditText);
            userNameIDTextInputLayout.setHint(strInput[count]);
            //userNameInputEditText.setTag(count);
            userNameInputEditText.setId(count);
            rootView.addView(view);
        }

        TextView tvTotalElement =  bottomSheetDialog.findViewById(R.id.totalCount);
        tvTotalElement.setText(String.valueOf(strInput.length));

        save_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView tvTotalElement =  bottomSheetDialog.findViewById(R.id.totalCount);
                int totalEditInput = Integer.parseInt(tvTotalElement.getText().toString());

               // Log.e("DEBUG_FOOTSHEET", "Array After from EDITBOX (HIDDEN VALUE OF TEXTOBX) : "+totalEditInput);

                String[] totalTexts = new String[totalEditInput];

                for (int count = 0; count < totalEditInput; count++) {
                    EditText data = bottomSheetDialog.findViewById(count);

                    if (String.valueOf(data.getText()).trim().equals("")) {
                        Toast.makeText(context, "Required value of "+data.getHint(), Toast.LENGTH_SHORT).show();
                        data.requestFocus();
                        return;
                    }

                    totalTexts[count] = String.valueOf(data.getText());
                }

                for (int count = 0; count < totalTexts.length; count++) {
                   // Log.e("DEBUG_FOOTSHEET", "Get Value of rom EDITBOX "+totalTexts[count]);
                }

                updatearaylist(totalTexts);

                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }


    public void updatearaylist(String[] totalTexts){
        this.txtValueArr = totalTexts;
    }
    public String[] getArrayList(String retType){

        if(retType.compareTo("lable")==0){
            return this.txtLableArr;
        }
        else {
            return this.txtValueArr;
        }
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i) {

       // Log.e("DEBUG_CARD", String.valueOf(i)+" Array Image Count "+this.videoArr);

        if(this.editTextArr > 0 && i==(this.videoArr-1)) {
           // Log.e("DEBUG_CARD", String.valueOf(i)+"  show popup from bottom");

            myViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showBottomSheetDialog();
                }
            });


        }
        else {
            Glide.with(context).load(new File(this.tempImgUrl[i])).into(myViewHolder.imageView);

            // myViewHolder.imageView.setOnClickListener(view -> NvImageListAdapter.this.clickAdapter.clickEvent(view, i));

            Uri filePath = Uri.parse(this.tempImgUrl[i]);

            myViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    //BitmapFactory.decodeFile(new File(filePath, options);
                    BitmapFactory.decodeFile(new File(filePath.getPath()).getAbsolutePath(), options);
                    int imageHeight = options.outHeight;
                    int imageWidth = options.outWidth;

                    NV_VideoEditorActivity.ratio_image = imageWidth + ","+imageHeight;
                   // Log.e("CROP_DEBUG"," Ration Starat "+ " Width:"+imageWidth+ " Height:"+ imageHeight);
                    NvImageListAdapter.this.clickAdapter.clickEvent(v,i);
                }
            });
        }


    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int i=viewType; //position

       // Log.e("DEBUG_CARD", " My View Holder "+ this.editTextArr + " "+ i);

        if(this.editTextArr > 0 && i==(this.videoArr-1)) {
            //Log.e("DEBUG_CARD", " show text list");
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_text_list_, viewGroup, false));
        }
        else {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_img_list_, viewGroup, false));
        }
    }
    @Override
    public int getItemViewType(int position) {
        //...
        return position;
    }

    public static class MyViewHolder extends ViewHolder {
        ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.gallery);
        }
    }
}
